package myproject;

import java.util.Optional;

import com.pulumi.Context;
import com.pulumi.Pulumi;
import com.pulumi.core.Output;
import com.pulumi.azurenative.dbforpostgresql.Database;
import com.pulumi.azurenative.dbforpostgresql.DatabaseArgs;
import com.pulumi.azurenative.dbforpostgresql.FirewallRule;
import com.pulumi.azurenative.dbforpostgresql.FirewallRuleArgs;
import com.pulumi.azurenative.dbforpostgresql.SingleServer;
import com.pulumi.azurenative.dbforpostgresql.SingleServerArgs;
import com.pulumi.azurenative.dbforpostgresql.enums.SslEnforcementEnum;
import com.pulumi.azurenative.dbforpostgresql.inputs.ServerPropertiesForDefaultCreateArgs;
import com.pulumi.azurenative.dbforpostgresql.inputs.SingleServerSkuArgs;
import com.pulumi.azurenative.dbforpostgresql.inputs.StorageProfileArgs;
import com.pulumi.azurenative.redis.Redis;
import com.pulumi.azurenative.redis.RedisArgs;
import com.pulumi.azurenative.redis.RedisFirewallRule;
import com.pulumi.azurenative.redis.RedisFirewallRuleArgs;
import com.pulumi.azurenative.redis.enums.PublicNetworkAccess;
import com.pulumi.azurenative.redis.enums.SkuFamily;
import com.pulumi.azurenative.redis.enums.SkuName;
import com.pulumi.azurenative.redis.enums.TlsVersion;
import com.pulumi.azurenative.redis.inputs.SkuArgs;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var config = ctx.config();
            var location = config.get("azure-native:location").orElse("eastus");
            var resourceGroupName = config.require("resourceGroupName");
            var sqlAdminUser = config.get("sqlAdminUser").orElse("spinnakeradmin");
            var sqlAdminPassword = ctx.config().requireSecret("sqlAdminPassword");
            var redisSubnetId = config.get("redisSubnetId");
            var redisFirewallStartIp = config.get("redisFirewallStartIp").orElse("0.0.0.0");
            var redisFirewallEndIp = config.get("redisFirewallEndIp").orElse("0.0.0.0");

            var postgresServerName = "spinnaker-postgres-server";
            var postgresServer = new SingleServer("postgresServer", SingleServerArgs.builder()
                    .location(location)
                    .resourceGroupName(resourceGroupName)
                    .serverName(postgresServerName)
                    .sku(SingleServerSkuArgs.builder()
                            .capacity(2)
                            .family("Gen5")
                            .name("B_Gen5_2")
                            .tier("Basic")
                            .build())
                    .properties(ServerPropertiesForDefaultCreateArgs.builder()
                            .administratorLogin(sqlAdminUser)
                            .administratorLoginPassword(sqlAdminPassword)
                            .createMode("Default")
                            .minimalTlsVersion("TLS1_2")
                            .sslEnforcement(SslEnforcementEnum.Enabled)
                            .storageProfile(StorageProfileArgs.builder()
                                    .backupRetentionDays(7)
                                    .geoRedundantBackup("Disabled")
                                    .storageMB(51200)
                                    .build())
                            .build())
                    .build());

            new FirewallRule("postgresFirewallRule", FirewallRuleArgs.builder()
                    .resourceGroupName(resourceGroupName)
                    .serverName(postgresServerName)
                    .firewallRuleName("allowAzureServices")
                    .startIpAddress("0.0.0.0")
                    .endIpAddress("0.0.0.0")
                    .build());

            new Database("orcaDatabase", DatabaseArgs.builder()
                    .databaseName("orca")
                    .resourceGroupName(resourceGroupName)
                    .serverName(postgresServerName)
                    .build());

            new Database("clouddriverDatabase", DatabaseArgs.builder()
                    .databaseName("clouddriver")
                    .resourceGroupName(resourceGroupName)
                    .serverName(postgresServerName)
                    .build());

            new Database("front50Database", DatabaseArgs.builder()
                    .databaseName("front50")
                    .resourceGroupName(resourceGroupName)
                    .serverName(postgresServerName)
                    .build());

            var redisArgsBuilder = RedisArgs.builder()
                    .location(location)
                    .resourceGroupName(resourceGroupName)
                    .sku(SkuArgs.builder()
                            .capacity(1)
                            .family(SkuFamily.C)
                            .name(SkuName.Standard)
                            .build())
                    .minimumTlsVersion(TlsVersion._1_2)
                    .enableNonSslPort(false);

            if (redisSubnetId.isPresent()) {
                redisArgsBuilder
                        .subnetId(redisSubnetId.get())
                        .publicNetworkAccess(PublicNetworkAccess.Disabled);
            } else {
                redisArgsBuilder.publicNetworkAccess(PublicNetworkAccess.Enabled);
            }

            var redisCache = new Redis("spinnakerRedis", redisArgsBuilder.build());

            if (redisSubnetId.isEmpty()) {
                new RedisFirewallRule("redisFirewallRule", RedisFirewallRuleArgs.builder()
                        .cacheName(redisCache.name())
                        .resourceGroupName(resourceGroupName)
                        .ruleName("allowAzureServices")
                        .startIP(redisFirewallStartIp)
                        .endIP(redisFirewallEndIp)
                        .build());
            }

            ctx.export("postgresServerName", postgresServer.name());
            ctx.export("postgresServerFqdn", postgresServer.fullyQualifiedDomainName()
                    .applyValue(host -> host.orElse("unknown")));
            ctx.export("postgresAdminUser", sqlAdminUser);
            ctx.export("postgresJdbcUrlTemplate", postgresServer.fullyQualifiedDomainName()
                    .applyValue(host -> host
                            .map(value -> "jdbc:postgresql://" + value + ":5432/%s?sslmode=require")
                            .orElse("jdbc:postgresql://<server>.postgres.database.azure.com:5432/%s?sslmode=require")));
            ctx.export("redisHostName", redisCache.hostName());
            ctx.export("redisSslPort", redisCache.sslPort());
            ctx.export("redisEndpoint", redisCache.hostName()
                    .applyValue(host -> "rediss://" + host + ":6380"));
        });
    }
}
