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

        // AWS
        Output<String> awsIp = AwsInfra.build(projectName, environment, sshPublicKey);
        ctx.export("AwsPublicIp", awsIp);

        // Azure
        Output<String> azureIp = AzureInfra.build(projectName, environment, sshPublicKey);
        ctx.export("AzurePublicIp", azureIp);

        // GCP
        Output<String> gcpIp = GcpInfra.build(projectName, environment, sshPublicKey);
        ctx.export("GcpPublicIp", gcpIp);
    }
}
