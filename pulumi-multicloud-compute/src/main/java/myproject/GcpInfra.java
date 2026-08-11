package myproject;

import com.pulumi.gcp.compute.Firewall;
import com.pulumi.gcp.compute.FirewallArgs;
import com.pulumi.gcp.compute.Instance;
import com.pulumi.gcp.compute.InstanceArgs;
import com.pulumi.gcp.compute.Network;
import com.pulumi.gcp.compute.NetworkArgs;
import com.pulumi.gcp.compute.Subnetwork;
import com.pulumi.gcp.compute.SubnetworkArgs;
import com.pulumi.gcp.compute.inputs.FirewallAllowArgs;
import com.pulumi.gcp.compute.inputs.InstanceBootDiskArgs;
import com.pulumi.gcp.compute.inputs.InstanceBootDiskInitializeParamsArgs;
import com.pulumi.gcp.compute.inputs.InstanceNetworkInterfaceArgs;
import com.pulumi.gcp.compute.inputs.InstanceNetworkInterfaceAccessConfigArgs;
import com.pulumi.core.Output;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class GcpInfra {
        public static Output<String> build(String projectName, String environment, String sshPublicKey,
                        String location, boolean gcpCreateNetwork) {
                Output<String> networkName;
                Output<String> networkId;

                switch ((Boolean) gcpCreateNetwork) {
                        case Boolean b when b -> {
                                var network = new Network("gcp-network", NetworkArgs.builder()
                                                .name(projectName + "-network")
                                                .autoCreateSubnetworks(false)
                                                .build());

                                var subnet = new Subnetwork("gcp-subnet", SubnetworkArgs.builder()
                                                .name(projectName + "-subnet")
                                                .network(network.id())
                                                .region(location)
                                                .ipCidrRange("10.10.0.0/24")
                                                .build());

                                networkName = network.name();
                                networkId = network.id();
                        }
                        case Boolean b when !b -> {
                                networkName = Output.of("default");
                                networkId = Output.of("default");
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + gcpCreateNetwork);
                }

                var firewall = new Firewall("gcp-firewall", FirewallArgs.builder()
                                .name(projectName + "-firewall")
                                .network(networkName)
                                .allows(
                                                FirewallAllowArgs.builder().protocol("tcp")
                                                                .ports("22", "80", "443", "8080").build())
                                .sourceRanges("0.0.0.0/0")
                                .build());

                // Use user-data metadata to execute the cloud init script
                String cloudInit;
                try {
                        cloudInit = Files.readString(Paths.get("cloud-init.yaml"))
                                        .replace("${HOSTNAME}", "gcpvm")
                                        .replace("${PROJECT_NAME}", projectName);
                } catch (IOException e) {
                        throw new RuntimeException("Failed to read cloud-init.yaml", e);
                }

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
                                                .network(networkId)
                                                .accessConfigs(InstanceNetworkInterfaceAccessConfigArgs.builder()
                                                                .build())
                                                .build())
                                .metadata(Map.of(
                                                "ssh-keys", "ubuntu:" + sshPublicKey,
                                                "user-data", cloudInit))
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
