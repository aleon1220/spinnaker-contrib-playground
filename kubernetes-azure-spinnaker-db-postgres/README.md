# Pulumi Java Spinnaker Backend DB

This Pulumi Java project provisions Azure backend resources for Spinnaker:

* PostgreSQL server with three logical databases: `orca`, `clouddriver`, `front50`
* PostgreSQL firewall rule for Azure service access
* Standard tier Azure Cache for Redis
* Optional Redis subnet injection when `redisSubnetId` is configured

## Prerequisites

* Azure account and subscription
* Existing Azure Resource Group for deployment
* Java 25 installed
* Gradle installed
* Pulumi CLI installed and logged in
* Azure CLI authenticated to your subscription

## Configuration

Required Pulumi config values:

* `resourceGroupName` - existing Azure resource group name
* `sqlAdminUser` - PostgreSQL administrator login
* `sqlAdminPassword` - PostgreSQL administrator password (secret)

Optional Pulumi config values:

* `azure-native:location` - Azure region (default: `eastus`)
* `redisSubnetId` - full subnet resource ID for Redis private deployment
* `redisFirewallStartIp` and `redisFirewallEndIp` - Redis firewall range when not using subnet injection

## Deploy

```bash
cd spinnaker-contrib-playground/kubernetes-azure-spinnaker-db
./gradlew clean build
pulumi stack init dev
pulumi config set resourceGroupName <your-rg>
pulumi config set sqlAdminUser spinnakeradmin
pulumi config set --secret sqlAdminPassword <your-secret>
pulumi config set azure-native:location eastus
# Optional: subnet injection
# pulumi config set redisSubnetId "/subscriptions/<sub>/resourceGroups/<rg>/providers/Microsoft.Network/virtualNetworks/<vnet>/subnets/<subnet>"
# Optional: Redis firewall rule when no subnet injection is used
# pulumi config set redisFirewallStartIp 0.0.0.0
# pulumi config set redisFirewallEndIp 0.0.0.0
pulumi up
```

## Outputs

* `postgresServerFqdn` - PostgreSQL host name
* `postgresJdbcUrlTemplate` - JDBC connection template for the logical databases
* `redisHostName` - Redis hostname
* `redisEndpoint` - Redis TLS endpoint url
