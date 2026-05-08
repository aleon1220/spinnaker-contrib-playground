package myproject;

import com.pulumi.Pulumi;
import com.pulumi.azurenative.containerservice.ManagedCluster;
import com.pulumi.azurenative.containerservice.ManagedClusterArgs;
import com.pulumi.azurenative.containerservice.inputs.ManagedClusterAgentPoolProfileArgs;
import com.pulumi.azurenative.containerservice.inputs.ManagedClusterIdentityArgs;
import com.pulumi.azurenative.containerservice.enums.ResourceIdentityType;
import com.pulumi.azurenative.resources.ResourceGroup;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var resourceGroup = new ResourceGroup("resourceGroup");

            var cluster = new ManagedCluster("aksCluster", ManagedClusterArgs.builder()
                    .resourceGroupName(resourceGroup.name())
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
