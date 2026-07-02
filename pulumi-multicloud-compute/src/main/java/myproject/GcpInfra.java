package myproject;

import com.pulumi.gcp.compute.Firewall;
import com.pulumi.gcp.compute.FirewallArgs;
import com.pulumi.gcp.compute.Instance;
import com.pulumi.gcp.compute.InstanceArgs;
import com.pulumi.gcp.compute.Network;
import com.pulumi.gcp.compute.NetworkArgs;
import com.pulumi.gcp.compute.inputs.FirewallAllowArgs;
import com.pulumi.gcp.compute.inputs.InstanceBootDiskArgs;
import com.pulumi.gcp.compute.inputs.InstanceBootDiskInitializeParamsArgs;
import com.pulumi.gcp.compute.inputs.InstanceNetworkInterfaceArgs;
import com.pulumi.gcp.compute.inputs.InstanceNetworkInterfaceAccessConfigArgs;
import com.pulumi.core.Output;

import java.util.Map;

public class GcpInfra {
    public static Output<String> build(String projectName, String environment, String sshPublicKey) {
        var network = new Network("gcp-network", NetworkArgs.builder()
            .name(projectName + "-network")
            .autoCreateSubnetworks(true)
            .build());

        var firewall = new Firewall("gcp-firewall", FirewallArgs.builder()
            .name(projectName + "-firewall")
            .network(network.name())
            .allows(
                FirewallAllowArgs.builder().protocol("tcp").ports("22", "80", "443", "8080").build()
            )
            .sourceRanges("0.0.0.0/0")
            .build());
            
        // Use user-data metadata to execute the cloud init script
        var cloudInit = CloudInit.getCloudInitScript(projectName, "gcpvm");
        var decodedCloudInit = new String(java.util.Base64.getDecoder().decode(cloudInit));

        var instance = new Instance("gcp-instance", InstanceArgs.builder()
            .name(projectName + "-vm")
            .machineType("e2-medium")
            .bootDisk(InstanceBootDiskArgs.builder()
                .initializeParams(InstanceBootDiskInitializeParamsArgs.builder()
                    .image("ubuntu-os-cloud/ubuntu-2404-lts-amd64")
                    .size(50)
                    .build())
                .build())
            .networkInterfaces(InstanceNetworkInterfaceArgs.builder()
                .network(network.id())
                .accessConfigs(InstanceNetworkInterfaceAccessConfigArgs.builder().build())
                .build())
            .metadata(Map.of(
                "ssh-keys", "ubuntu:" + sshPublicKey,
                "user-data", decodedCloudInit
            ))
            .build());

        return instance.networkInterfaces().applyValue(interfaces -> {
            if (interfaces != null && !interfaces.isEmpty()) {
                var accessConfigs = interfaces.get(0).accessConfigs();
                if (accessConfigs != null && !accessConfigs.isEmpty()) {
                    return accessConfigs.get(0).natIp().orElse("Unknown");
                }
            }
            return "Unknown";
        });
    }
}
