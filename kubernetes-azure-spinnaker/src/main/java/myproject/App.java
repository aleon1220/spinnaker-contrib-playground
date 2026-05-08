package myproject;

import com.pulumi.Pulumi;

import com.pulumi.azurenative.containerservice.ManagedCluster;
import com.pulumi.azurenative.containerservice.ManagedClusterArgs;
import com.pulumi.azurenative.containerservice.inputs.ManagedClusterAgentPoolProfileArgs;
import com.pulumi.azurenative.containerservice.inputs.ManagedClusterIdentityArgs;
import com.pulumi.azurenative.containerservice.enums.ResourceIdentityType;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var config = ctx.config();
            var location = config.get("azure-native:location").orElse("eastus");
            var resourceGroup = config.get("resourceGroupName").orElse("rg-tmp-spinnaker");

            var cluster = new ManagedCluster("aksCluster", ManagedClusterArgs.builder()
                    .resourceGroupName(resourceGroup)
                    .location(location)
                    .dnsPrefix("aks-dns-prefix")
                    .agentPoolProfiles(ManagedClusterAgentPoolProfileArgs.builder()
                            .name("agentpool")
                            .count(3)
                            .vmSize("Standard_DS2_v2")
                            .mode("System")
                            .build())
                    .identity(ManagedClusterIdentityArgs.builder()
                            .type(ResourceIdentityType.SystemAssigned)
                            .build())
                    .build());

            ctx.export("clusterName", cluster.name());
        });
    }
}
