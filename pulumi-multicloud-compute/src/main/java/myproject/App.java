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
        var gcpCreateNetwork = config.getBoolean("gcpCreateNetwork").orElse(false);

        switch (targetCloud) {
            case String s when s.equals("all") || s.equals("aws") -> {
                Output<String> awsIp = AwsInfra.build(projectName, environment, sshPublicKey);
                ctx.export("AwsPublicIp", awsIp);
            }
            default -> {}
        }

        switch (targetCloud) {
            case String s when s.equals("all") || s.equals("azure") -> {
                Output<String> azureIp = AzureInfra.build(projectName, environment, sshPublicKey);
                ctx.export("AzurePublicIp", azureIp);
            }
            default -> {}
        }

        switch (targetCloud) {
            case String s when s.equals("all") || s.equals("gcp") -> {
                Output<String> gcpIp = GcpInfra.build(projectName, environment, sshPublicKey, location, gcpCreateNetwork);
                ctx.export("GcpPublicIp", gcpIp);
            }
            default -> {}
        }
    }
}
