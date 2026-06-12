# Pulumi Java AKS

This template provisions:

* Azure Resource Group
* AKS cluster
* Optionally use an existing Pluralsight sandbox resource group

## Prerequisites

* Azure account with credits
* Pluralsight Hands-On Cloud Playground (optional)
* Java 17 or higher installed
* Gradle 9 or higher installed
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

using <https://www.mermaidflow.app/editor> i can see the icons

```mermaid
architecture-beta
    %% Define the services and groups
    service user(logos:cloud)[User]
    service linux(logos:linux-tux)[Linux Instance]
    service pulumi(logos:pulumi)[Pulumi v3245]
    
    group azure(logos:azure)[Azure Account]
    service aks(logos:kubernetes)[AKS Cluster] in azure
    
    %% Define the directional connections (L=Left, R=Right)
    user:R -- L:linux
    linux:R -- L:pulumi
    pulumi:R -- L:aks
```

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
./gradlew clean build 2>&1
```

* Validate Pulumi config and deploy

```bash
pulumi config
pulumi config -j | jq
pulumi up
```

* Confirm Java is installed

```bash
java --version
```

* Set the Pulumi resource group name provided by Pluralsight Azure Sandbox

```bash
PLURALSIGHT_RG_NAME=$(az group list --query "[0].name" --output tsv)
pulumi config set resourceGroupName $PLURALSIGHT_RG_NAME
```

* Deploy the stack

```bash
pulumi up
```

* check structure of working dir

```bash
tree -L 2
```

---

### Azure authentication and setup

* Verify Azure CLI service principal authentication

```bash
export APP_ID="$ARM_CLIENT_ID"
export CLIENT_SECRET="$ARM_CLIENT_SECRET"
export TENANT_ID="$ARM_TENANT_ID"

az login --service-principal --username $APP_ID --password $CLIENT_SECRET --tenant $TENANT_ID
az account set --subscription $ARM_SUBSCRIPTION_ID
az group list
```

### Spinnaker running in AKS via Pulumi IaC Windows

* prepare for deploying the stack

```PowerShell

pulumi config set azure-native:useDefaultAzureCredential false
pulumi config set azure-native:subscriptionId $env:ARM_SUBSCRIPTION_ID
```

* Build and validate the project

```PowerShell
./gradlew.bat clean build :kubernetes-azure-spinnaker:build
```

* Validate Pulumi config. 

pulumi will ask you to authenticate and select or create a stack

```PowerShell
pulumi config
```

* Set the Pulumi resource group name provided by Pluralsight Azure Sandbox

```PowerShell
$env:PLURALSIGHT_RG_NAME = (az group list --query "[0].name" --output tsv)
pulumi config set resourceGroupName $env:PLURALSIGHT_RG_NAME
```

* Deploy the stack

```PowerShell
pulumi up
```

### Azure validation

* Validate the account

```bash
az account list --output table
az account list --query '[].{subscriptionName:name,subscriptionId:id}' -o tsv

az account show | jq
```

* validate resource group

```bash
az group list
```

* check rg in specific location

```bash
az group list --query "[?location=='westus']"
```

## Robust AKS kubeconfig connection linux

* Ensure the resource group and cluster name are set

```bash
if [ -z "$PLURALSIGHT_RG_NAME" ] || [ -z "$CLUSTER_NAME" ]; then
  echo "Error: PLURALSIGHT_RG_NAME or CLUSTER_NAME is not set."
fi

echo "proceed"

az aks get-credentials --resource-group "$PLURALSIGHT_RG_NAME" --name "$CLUSTER_NAME" --admin --overwrite-existing
```

* set clustername

```bash
CLUSTER_NAME=$(az aks list --resource-group $PLURALSIGHT_RG_NAME --query "[0].name" -o tsv)
```

* Verify it worked

```bash
echo $CLUSTER_NAME
```

### Outputs

* Review the Pulumi stack output for cluster details and any exported values
* get AKS cluster

```bash
az aks list --query "[0].name" -o tsv
```

### Connect to the AKS cluster

* Set kubectl credentials

```bash
az aks get-credentials --resource-group $PLURALSIGHT_RG_NAME --name $CLUSTER_NAME --admin
```

* Verify cluster nodes

```bash
kubectl get nodes
```

## AKS kubeconfig connection windows

```powershell
$env:CLUSTER_NAME = (az aks list --query "[0].name")

az aks get-credentials --resource-group $env:PLURALSIGHT_RG_NAME --name "$env:CLUSTER_NAME" --admin --overwrite-existing
```

---

### spinnaker validation

* use kustomize to deploy

* after deploying spinnaker with kustomize

* watch pods in the spinnaker namespace

```bash
watch kubectl get pods -n spinnaker
```

* get ingress address

```bash
kubectl get ingress -n spinnaker
```

* update `/etc/hosts`

## Next steps

* Deploy your application using a FatJar
* Use multiple Pulumi stacks for development, staging, and production
* Integrate Pulumi into CI/CD pipelines
* Explore the Pulumi Azure Native SDK in the [Pulumi Registry](https://www.pulumi.com/registry/packages/azure-native/)

## Getting help

* Check out the [Pulumi Documentation](https://www.pulumi.com/docs/)
* Join the [Pulumi Community Slack](https://slack.pulumi.com/)
* File an issue in this repository

## Spinnaker installation

> it seems spinnaker is hard to install. Halyard is deprecated

* [Install Halyard](https://spinnaker.io/docs/setup/install/halyard/)

### effort 2026-02-04

```bash
Halyard version will be 1.70.0 
Halyard will be downloaded from the spinnaker-community repository 
Halconfig will be stored at /home/spinnaker/.hal/config
Uninstall script is located at /usr/local/bin/uninstall-halyard.sh

# lots of install output
Halyard version: 2025.4.1
```

* Configure your cloud provider account (Azure, AWS, GCP)
* Apply sample pipeline manifests from `/pipelines`

## References

* <https://pubs.opengroup.org/onlinepubs/9799919799/utilities/V3_chap02.html>
* [Azure RBAC for AKS](https://learn.microsoft.com/en-us/azure/aks/azure-rbac?tabs=azure-cli)
