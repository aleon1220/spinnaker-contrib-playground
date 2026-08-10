package myproject;

import com.pulumi.Context;
import com.pulumi.Pulumi;
import com.pulumi.core.Output;

public class App {
    public static void main(String[] args) {
        Pulumi.run(App::stack);
    }

    private static void stack(Context ctx) {
        var config = ctx.config();
        var projectName = config.require("projectName");
        var environment = config.require("environment");
        var sshPublicKey = config.require("sshPublicKey");
        var location = config.get("location").orElse("us-east1");

        // Defaults to "all" clouds if not specified
        var targetCloud = config.get("targetCloud").orElse("all").toLowerCase();

        if (targetCloud.equals("all") || targetCloud.equals("aws")) {
            Output<String> awsIp = AwsInfra.build(projectName, environment, sshPublicKey);
            ctx.export("AwsPublicIp", awsIp);
        }

        if (targetCloud.equals("all") || targetCloud.equals("azure")) {
            Output<String> azureIp = AzureInfra.build(projectName, environment, sshPublicKey);
            ctx.export("AzurePublicIp", azureIp);
        }

        if (targetCloud.equals("all") || targetCloud.equals("gcp")) {
            Output<String> gcpIp = GcpInfra.build(projectName, environment, sshPublicKey, location);
            ctx.export("GcpPublicIp", gcpIp);
        }
    }
}
