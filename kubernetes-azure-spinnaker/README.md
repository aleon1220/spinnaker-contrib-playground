 # Pulumi Template: Azure Native Java Storage

 A minimal Pulumi template for creating a Resource Group and Storage Account on Azure using Java and the Azure Native provider. This template provisions:

 - An Azure Resource Group.
 - An Azure Storage Account (Standard_LRS, StorageV2).
 - Exports the storage account name.

 ## Prerequisites

- launch [PluralsightHands-On Playground](https://app.pluralsight.com/hands-on/playground/cloud-sandboxes) 
- todo: instructions to reference to az cli docs on how to Authenticate using a Service Principal having the env variables available

- set authentication using a Service Principal

```bash
 pulumi version
 PLURALSIGHT_RG_NAME="1-6be2cc6b-playground-sandbox" && pulumi config set resourceGroupName $PLURALSIGHT_RG_NAME
 az group list
 az account show --query id -o tsv
 pulumi config set azure-native:useDefaultAzureCredential false
 
 az account set --subscription $ARM_SUBSCRIPTION_ID
 
 export ARM_SUBSCRIPTION_ID="80ea84e8-afce-4851-928a-9e2219724c69"
 echo $ARM_SUBSCRIPTION_ID
 az account set --subscription $ARM_SUBSCRIPTION_ID

 pulumi config set azure-native:subscriptionId $ARM_SUBSCRIPTION_ID
 az account show
 az account list
 az account show | jq
 az group list --query "[?location=='westus']"
 
 gradle clean
 gradle clean build 2>&1
 tree -L 2
 git status
 AZURE_TENANT_ID="$ARM_TENANT_ID" 
 az group list --query "[?location=='westus']"
 az account show | jq
 az login
 az group list --query "[?location=='westus']"
 az account show --query id -o tsv
 az account list --query '[].{subscriptionName:name,subscriptionId:id}' -o tsv
 az group list --query "[?location=='westus']"
 pulumi up
 pulumi config set azure-native:useDefaultAzureCredential false
 echo $ARM_SUBSCRIPTION_ID
 export ARM_USE_DEFAULT_AZURE_CREDENTIAL=false
 az account list --query '[].{subscriptionName:name,subscriptionId:id}' -o tsv
 echo $ARM_CLIENT_ID
 echo $ARM_CLIENT_SECRET
 echo $ARM_TENANT_ID
 echo $ARM_SUBSCRIPTION_ID
 echo $ARM_LOCATION_NAME
 pulumi config list
 pulumi config -j
 pulumi config -j | jq
 pulumi up

```
az account show --query id -o tsv

 - An Azure account with credentials configured (for example, via `az login` or setting environment variables `ARM_CLIENT_ID`, `ARM_CLIENT_SECRET`, and `ARM_SUBSCRIPTION_ID`).

Azure publishes an open configuration file for every domain that exposes the Tenant ID publicly.

- set domain e.g. the pluralsight cloud hands on boxes domain

```bash
DOMAIN_TENANT="realhandsonlabs.com"
```

- extract tenant id from domain name

```bash
export ARM_TENANT_ID=$(curl -s https://login.microsoftonline.com/${DOMAIN_TENANT}/.well-known/openid-configuration | grep -o 'https://sts.windows.net/[^/]*' | cut -d '/' -f 4)

echo $ARM_TENANT_ID
```

 - Java 11 or higher installed.
 - Maven installed.
 - Pulumi CLI installed and logged in
 - refer to [Pulumi Azure Native Java Getting Started](https://www.pulumi.com/docs/intro/languages/java/)
 - refer to [Pulumi Azure Native Getting Started](https://www.pulumi.com/docs/iac/get-started/azure/)

## Getting Started
 
- connect to the target azure account

pushd kubernetes-azure-spinnaker

- set variables according to plural sight restrictions

```bash
PLURALSIGHT_RG_NAME="" && pulumi config set resourceGroupName $PLURALSIGHT_RG_NAME
```

- run the pulumi stack

## Outputs

 - `storageAccountName`: The name of the Storage Account.

---

 ## Next Steps

 - Extend `App.java` to add more Azure resources (for example, Cosmos DB, Functions, or Networking).
 - Use multiple Pulumi stacks for different environments (development, staging, production).
 - Integrate Pulumi into your CI/CD pipeline.
 - Explore the Pulumi Azure Native SDK in the [Pulumi Registry](https://www.pulumi.com/registry/packages/azure-native/).

## Getting Help

 you have questions or encounter any issues:

- Check out the [Pulumi Documentation](https://www.pulumi.com/docs/).
- Join the [Pulumi Community Slack](https://slack.pulumi.com/) for support.
- File an issue in this repository.

## Connect to the AKS cluster

```bash
az aks get-credentials --resource-group $PLURALSIGHT_RG_NAME --name $CLUSTER_NAME --admin
```

```bash
kubectl get nodes
```
