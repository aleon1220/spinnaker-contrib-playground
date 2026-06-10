package myproject;

import com.pulumi.Pulumi;
import com.pulumi.Config;
import com.pulumi.core.Output;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

// Updated Imports for MySQL Flexible Server
import com.pulumi.azurenative.dbformysql.Server;
import com.pulumi.azurenative.dbformysql.ServerArgs;
import com.pulumi.azurenative.dbformysql.Database;
import com.pulumi.azurenative.dbformysql.DatabaseArgs;
import com.pulumi.azurenative.dbformysql.inputs.SkuArgs;

public class App {
        public static void main(String[] args) {
                Pulumi.run(ctx -> {
                        Config config = null; 
                        var resourceGroup = "anz-devops-sre-platform-engineering-research-dev";
                        var locationName = "WestUS2";

                        // Server configuration - use secrets for credentials
                        var sqlAdminUsername = ctx.config().require("sqlAdminUsername");
                        var sqlAdminPassword = ctx.config().requireSecret("sqlAdminPassword");

                        // Create Azure Database for MySQL Flexible Server
                        // Note: Version "8.0.21" is the standard Pulumi string for provisioning a MySQL 8 server.
                        // Azure will automatically deploy the latest supported minor version (e.g., 8.0.36+) behind the scenes.
                        var mysqlServer = new Server("mysqlserver", ServerArgs.builder()
                                        .resourceGroupName(resourceGroup)
                                        .location(locationName)
                                        .administratorLogin(sqlAdminUsername)
                                        .administratorLoginPassword(sqlAdminPassword)
                                        .version("8.0.21") 
                                        .sku(SkuArgs.builder()
                                                        .name("Standard_B1ms") // Options: Standard_B1ms, Standard_D2ds_v4, etc.
                                                        .tier("Burstable")     // Options: Burstable, GeneralPurpose, MemoryOptimized
                                                        .build())
                                        .build());

                        // Create MySQL Database
                        var mysqlDatabase = new Database("mysqldb", DatabaseArgs.builder()
                                        .resourceGroupName(resourceGroup)
                                        .serverName(mysqlServer.name())
                                        .charset("utf8mb4") // Recommended character set for MySQL 8
                                        .collation("utf8mb4_unicode_ci")
                                        .build());

                        // Export outputs
                        try {
                                var readme = Files.readString(Paths.get("./Pulumi.README.md"));
                                ctx.export("readme", Output.of(readme));
                                ctx.export("mysqlServerName", mysqlServer.name());
                                ctx.export("mysqlServerFqdn", mysqlServer.fullyQualifiedDomainName());
                                ctx.export("mysqlDatabaseName", mysqlDatabase.name());
                                
                                // Connection string for MySQL (password omitted for security)
                                ctx.export("baseMySQLConnectionString", Output
                                                .all(mysqlServer.fullyQualifiedDomainName(), mysqlDatabase.name())
                                                .applyValue(values -> String.format(
                                                                "Server=%s;Port=3306;Database=%s;Uid=%s;",
                                                                values.get(0), values.get(1), sqlAdminUsername)));
                        } catch (IOException e) {
                                throw new RuntimeException(e);
                        }
                });
        } // end of main
} // end of class