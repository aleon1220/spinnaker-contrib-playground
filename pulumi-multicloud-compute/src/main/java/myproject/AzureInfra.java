package myproject;

import com.pulumi.azurenative.compute.VirtualMachine;
import com.pulumi.azurenative.compute.VirtualMachineArgs;
import com.pulumi.azurenative.compute.inputs.HardwareProfileArgs;
import com.pulumi.azurenative.compute.inputs.ImageReferenceArgs;
import com.pulumi.azurenative.compute.inputs.LinuxConfigurationArgs;
import com.pulumi.azurenative.compute.inputs.NetworkProfileArgs;
import com.pulumi.azurenative.compute.inputs.NetworkInterfaceReferenceArgs;
import com.pulumi.azurenative.compute.inputs.OSDiskArgs;
import com.pulumi.azurenative.compute.inputs.OSProfileArgs;
import com.pulumi.azurenative.compute.inputs.SshConfigurationArgs;
import com.pulumi.azurenative.compute.inputs.SshPublicKeyArgs;
import com.pulumi.azurenative.network.NetworkInterface;
import com.pulumi.azurenative.network.NetworkInterfaceArgs;
import com.pulumi.azurenative.network.PublicIPAddress;
import com.pulumi.azurenative.network.PublicIPAddressArgs;
import com.pulumi.azurenative.network.Subnet;
import com.pulumi.azurenative.network.SubnetArgs;
import com.pulumi.azurenative.network.VirtualNetwork;
import com.pulumi.azurenative.network.VirtualNetworkArgs;
import com.pulumi.azurenative.network.inputs.AddressSpaceArgs;
import com.pulumi.azurenative.network.inputs.NetworkInterfaceIPConfigurationArgs;
import com.pulumi.azurenative.network.inputs.PublicIPAddressSkuArgs;
import com.pulumi.azurenative.network.enums.PublicIPAddressSkuName;
import com.pulumi.azurenative.network.enums.IPAllocationMethod;
import com.pulumi.azurenative.resources.ResourceGroup;
import com.pulumi.azurenative.resources.ResourceGroupArgs;
import com.pulumi.azurenative.storage.StorageAccount;
import com.pulumi.azurenative.storage.StorageAccountArgs;
import com.pulumi.azurenative.storage.inputs.SkuArgs;
import com.pulumi.azurenative.storage.enums.SkuName;
import com.pulumi.azurenative.storage.enums.Kind;
import com.pulumi.core.Output;

public class AzureInfra {

    public static Output<String> build(String projectName, String environment, String sshPublicKey) {
        var rg = new ResourceGroup("azure-rg", ResourceGroupArgs.builder()
            .resourceGroupName(projectName + "-rg")
            .build());

        var vnet = new VirtualNetwork("azure-vnet", VirtualNetworkArgs.builder()
            .resourceGroupName(rg.name())
            .addressSpace(AddressSpaceArgs.builder().addressPrefixes("10.0.0.0/16").build())
            .build());

        var subnet = new Subnet("azure-subnet", SubnetArgs.builder()
            .resourceGroupName(rg.name())
            .virtualNetworkName(vnet.name())
            .addressPrefix("10.0.2.0/24")
            .build());

        var publicIp = new PublicIPAddress("azure-pip", PublicIPAddressArgs.builder()
            .resourceGroupName(rg.name())
            .publicIPAllocationMethod(IPAllocationMethod.Static)
            .sku(PublicIPAddressSkuArgs.builder().name(PublicIPAddressSkuName.Standard).build())
            .build());

        var nic = new NetworkInterface("azure-nic", NetworkInterfaceArgs.builder()
            .resourceGroupName(rg.name())
            .ipConfigurations(NetworkInterfaceIPConfigurationArgs.builder()
                .name("testconfiguration1")
                .subnet(com.pulumi.azurenative.network.inputs.SubnetArgs.builder().id(subnet.id()).build())
                .privateIPAllocationMethod(IPAllocationMethod.Dynamic)
                .publicIPAddress(com.pulumi.azurenative.network.inputs.PublicIPAddressArgs.builder().id(publicIp.id()).build())
                .build())
            .build());

        var storage = new StorageAccount("azurestor", StorageAccountArgs.builder()
            .resourceGroupName(rg.name())
            .accountName((projectName + "stor").replace("-", "").toLowerCase())
            .sku(SkuArgs.builder().name(SkuName.Standard_LRS).build())
            .kind(Kind.StorageV2)
            .build());

        var vm = new VirtualMachine("azure-vm", VirtualMachineArgs.builder()
            .resourceGroupName(rg.name())
            .hardwareProfile(HardwareProfileArgs.builder().vmSize("Standard_DS1_v2").build())
            .networkProfile(NetworkProfileArgs.builder()
                .networkInterfaces(NetworkInterfaceReferenceArgs.builder().id(nic.id()).primary(true).build())
                .build())
            .storageProfile(com.pulumi.azurenative.compute.inputs.StorageProfileArgs.builder()
                .imageReference(ImageReferenceArgs.builder()
                    .publisher("Canonical")
                    .offer("ubuntu-24_04-lts")
                    .sku("server")
                    .version("latest")
                    .build())
                .osDisk(OSDiskArgs.builder()
                    .name("myosdisk1")
                    .caching(com.pulumi.azurenative.compute.enums.CachingTypes.ReadWrite)
                    .createOption("FromImage")
                    .managedDisk(com.pulumi.azurenative.compute.inputs.ManagedDiskParametersArgs.builder()
                        .storageAccountType("Standard_LRS")
                        .build())
                    .build())
                .build())
            .osProfile(OSProfileArgs.builder()
                .computerName("azurevm")
                .adminUsername("ubuntu")
                .customData(CloudInit.getCloudInitScript(projectName, "azurevm"))
                .linuxConfiguration(LinuxConfigurationArgs.builder()
                    .disablePasswordAuthentication(true)
                    .ssh(SshConfigurationArgs.builder()
                        .publicKeys(SshPublicKeyArgs.builder()
                            .keyData(sshPublicKey)
                            .path("/home/ubuntu/.ssh/authorized_keys")
                            .build())
                        .build())
                    .build())
                .build())
            .build());

        return publicIp.ipAddress().applyValue(ip -> ip.orElse("Dynamic/Unknown"));
    }
}
