package myproject;

import com.pulumi.aws.ec2.Ami;
import com.pulumi.aws.ec2.Ec2Functions;
import com.pulumi.aws.ec2.Instance;
import com.pulumi.aws.ec2.InstanceArgs;
import com.pulumi.aws.ec2.KeyPair;
import com.pulumi.aws.ec2.KeyPairArgs;
import com.pulumi.aws.ec2.SecurityGroup;
import com.pulumi.aws.ec2.SecurityGroupArgs;
import com.pulumi.aws.ec2.inputs.GetAmiArgs;
import com.pulumi.aws.ec2.inputs.GetAmiFilterArgs;
import com.pulumi.aws.ec2.inputs.InstanceRootBlockDeviceArgs;
import com.pulumi.aws.ec2.inputs.SecurityGroupEgressArgs;
import com.pulumi.aws.ec2.inputs.SecurityGroupIngressArgs;
import com.pulumi.core.Output;

import java.util.List;
import java.util.Map;

public class AwsInfra {

    public static Output<String> build(String projectName, String environment, String sshPublicKey) {
        var keyName = "key-InfrastructureAsACode-" + projectName;

        var keyPair = new KeyPair("aws-keypair", KeyPairArgs.builder()
            .keyName(keyName)
            .publicKey(sshPublicKey)
            .build());

        var securityGroup = new SecurityGroup("aws-secgroup", SecurityGroupArgs.builder()
            .name("secg-" + projectName + "-" + environment)
            .description("Default security group to allow inbound/outbound")
            .ingress(
                SecurityGroupIngressArgs.builder().description("SSH Access").protocol("tcp").fromPort(22).toPort(22).cidrBlocks("0.0.0.0/0").build(),
                SecurityGroupIngressArgs.builder().description("HTTP").protocol("tcp").fromPort(80).toPort(80).cidrBlocks("0.0.0.0/0").build(),
                SecurityGroupIngressArgs.builder().description("HTTPS").protocol("tcp").fromPort(443).toPort(443).cidrBlocks("0.0.0.0/0").build(),
                SecurityGroupIngressArgs.builder().description("App port").protocol("tcp").fromPort(8080).toPort(8080).cidrBlocks("0.0.0.0/0").build()
            )
            .egress(
                SecurityGroupEgressArgs.builder().description("All Outbound").protocol("-1").fromPort(0).toPort(0).cidrBlocks("0.0.0.0/0").ipv6CidrBlocks("::/0").build()
            )
            .tags(Map.of("Name", "secg-" + projectName + "-" + environment))
            .build());

        var ubuntuAmi = Ec2Functions.getAmi(GetAmiArgs.builder()
            .mostRecent(true)
            .filters(
                GetAmiFilterArgs.builder().name("name").values("ubuntu/images/hvm-ssd-gp3/ubuntu-noble-24.04-amd64-server-*").build(),
                GetAmiFilterArgs.builder().name("virtualization-type").values("hvm").build()
            )
            .owners("099720109477") // Canonical
            .build());

        var instance = new Instance("aws-ec2-compute", InstanceArgs.builder()
            .ami(ubuntuAmi.applyValue(result -> result.id()))
            .instanceType("t3.medium")
            .keyName(keyPair.keyName())
            .vpcSecurityGroupIds(Output.all(securityGroup.id()).applyValue(ids -> ids))
            .rootBlockDevice(InstanceRootBlockDeviceArgs.builder()
                .deleteOnTermination(true)
                .volumeSize(50)
                .volumeType("gp3")
                .tags(Map.of("Name", "ebs-root-" + projectName + "-" + environment))
                .build())
            .userDataBase64(CloudInit.getCloudInitScript(projectName, "awsec2compute"))
            .tags(Map.of("Name", "ec2-" + projectName + "-" + environment))
            .build());

        return instance.publicIp();
    }
}
