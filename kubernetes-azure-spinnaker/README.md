# Pulumi Java AKS

This template provisions:

* Azure Resource Group
* AKS cluster
* Optionally use an existing Pluralsight sandbox resource group

## Prerequisites

* Azure account with credits
* Pluralsight Hands-On Cloud Playground (optional)
* Java 11 or higher installed
* Gradle installed
* Pulumi CLI installed and logged in
* Azure CLI configured for your target subscription

## Local execution using Azure Cloud Shell

* Clone the repo

```bash
git clone https://github.com/aleon1220/spinnaker-contrib-playground.git
```

* Change into the Pulumi project directory

```bash
pushd spinnaker-contrib-playground/kubernetes-azure-spinnaker
```

* Install SDKMAN to manage Java and Gradle

```bash
curl -s "https://get.sdkman.io" | bash
```

* Install JDK 25

```bash
SDK_MAN_JAVA_VERSION="25.0.3-ms"
sdk install java $SDK_MAN_JAVA_VERSION
```

* Install Gradle

```bash
sdk install gradle
```

* Install Pulumi

```bash
curl -fsSL https://get.pulumi.com | sh
```

* Reload the shell after installation

```bash
bash
```

* Build the Java project

```bash
gradle clean build
```

* Set the target resource group name

```bash
export PLURALSIGHT_RG_NAME=$(az group list --query "[0].name" --output tsv)
echo $PLURALSIGHT_RG_NAME
```

* Select the Pulumi stack

```bash
pulumi stack select aleon1220/kubernetes-azure-spinnaker/cloudshell-aks-spinnaker
```

* Configure Pulumi with the resource group

```bash
pulumi config set resourceGroupName $PLURALSIGHT_RG_NAME
```

* Deploy the AKS cluster

```bash
pulumi up
```

---

## Local execution on Linux

* Install Pulumi

```bash
curl -fsSL https://get.pulumi.com | sh
bash
pulumi version
```

* Select the Pulumi stack

```bash
PULUMI_STACK="azure-k8s-playground"
pulumi stack select "aleon1220/kubernetes-azure-spinnaker/$PULUMI_STACK"
```

* Configure Azure subscription credentials

```bash
DOMAIN_TENANT="realhandsonlabs.com"
export ARM_TENANT_ID=$(curl -s https://login.microsoftonline.com/${DOMAIN_TENANT}/.well-known/openid-configuration | grep -o 'https://sts.windows.net/[^/]*' | cut -d '/' -f 4)
echo $ARM_TENANT_ID
```

* Set required service principal environment variables

```bash
export ARM_CLIENT_ID="<YOUR_APPLICATION_CLIENT_ID>"
export ARM_CLIENT_SECRET="<YOUR_SECRET>"
export ARM_SUBSCRIPTION_ID=$(az account show --query id -o tsv)
export ARM_TENANT_ID=$(az account show --query tenantId -o tsv)
```

* Login with Azure CLI using service principal

```bash
az login --service-principal --username $ARM_CLIENT_ID --password $ARM_CLIENT_SECRET --tenant $ARM_TENANT_ID
```

* Configure Pulumi to use explicit Azure credentials

```bash
pulumi config set azure-native:useDefaultAzureCredential false
pulumi config set azure-native:subscriptionId $ARM_SUBSCRIPTION_ID
```

* Build and validate the project

```bash
gradle clean build 2>&1
tree -L 2
```

* Validate Pulumi config and deploy

```bash
pulumi config list
pulumi config -j | jq
pulumi up
```

* Confirm Java is installed

```bash
java --version
```

* Set the Pulumi resource group for Pluralsight

```bash
PLURALSIGHT_RG_NAME=""
pulumi config set resourceGroupName $PLURALSIGHT_RG_NAME
```

* Deploy the stack

```bash
pulumi up
```

---

## Azure authentication and setup

* Verify Azure CLI service principal authentication

```bash
export APP_ID="$ARM_CLIENT_ID"
export CLIENT_SECRET="$ARM_CLIENT_SECRET"
export TENANT_ID="$ARM_TENANT_ID"
az login --service-principal --username $APP_ID --password $CLIENT_SECRET --tenant $TENANT_ID
az account set --subscription $ARM_SUBSCRIPTION_ID
az group list
```

* Validate the `az` CLI state

```bash
az account list --output table
az account show
az account list --query '[].{subscriptionName:name,subscriptionId:id}' -o tsv
az account show | jq
az group list --query "[?location=='westus']"
```

### Robust AKS kubeconfig connection

* Ensure the resource group and cluster name are set

```bash
if [ -z "$PLURALSIGHT_RG_NAME" ] || [ -z "$CLUSTER_NAME" ]; then
  echo "Error: PLURALSIGHT_RG_NAME or CLUSTER_NAME is not set."
fi

az aks get-credentials --resource-group "$PLURALSIGHT_RG_NAME" --name "$CLUSTER_NAME" --admin --overwrite-existing
```

## Outputs

* Review the Pulumi stack output for cluster details and any exported values

## Connect to the AKS cluster

* Set kubectl credentials

```bash
az aks get-credentials --resource-group $PLURALSIGHT_RG_NAME --name $CLUSTER_NAME --admin
```

* Verify cluster nodes

```bash
kubectl get nodes
```

---

## Next steps

* Deploy your application using a FatJar
* Use multiple Pulumi stacks for development, staging, and production
* Integrate Pulumi into CI/CD pipelines
* Explore the Pulumi Azure Native SDK in the [Pulumi Registry](https://www.pulumi.com/registry/packages/azure-native/)

## Getting help

* Check out the [Pulumi Documentation](https://www.pulumi.com/docs/)
* Join the [Pulumi Community Slack](https://slack.pulumi.com/)
* File an issue in this repository

## References

* <https://pubs.opengroup.org/onlinepubs/9799919799/utilities/V3_chap02.html>
* [Azure RBAC for AKS](https://learn.microsoft.com/en-us/azure/aks/azure-rbac?tabs=azure-cli)
