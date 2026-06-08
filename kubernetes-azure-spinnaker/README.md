# Pulumi java AKS

leveraging the pulumi Azure Native provider. This template provisions:

* An Azure Resource Group
* the rg name or use the rg provided by Pluralsight Sandboxes
* AKS cluster

## Prerequisites

* launch [Pluralsight Hands-On Playground](https://app.pluralsight.com/hands-on/playground/cloud-sandboxes)
* prefer a browser with incognito mode

### local execution Azure Cloud Shell

* create cloud Shell Azure
* clone this repo

    ```bash
    git clone https://github.com/aleon1220/spinnaker-contrib-playground.git
    ```

* navigate to `workdir`

  ```bash
  pushd kubernetes-azure-spinnaker
  ```

* install sdkman to handle gradle and java

    ```bash
    curl -s "https://get.sdkman.io" | bash
    ```

* get JDK25

    ```bash
    SDK_MAN_JAVA_VERSION="25.0.3-ms"
    sdk install java $SDK_MAN_JAVA_VERSION
    ```

* install gradle

    ```bash
    sdk install gradle
    ```

* **install pulumi**

    ```bash
    curl -fsSL https://get.pulumi.com | sh
    bash
    pulumi version
    ```

* create the AKS cluster

```bash
pulumi up
```

---

### local execution Linux instance

* set pulumi

    ```bash
    curl -fsSL https://get.pulumi.com | sh
    bash
    pulumi version
    ```

* set authentication using a Service Principal

    ```bash
    PULUMI_STACK="aleon1220/kubernetes-azure-spinnaker/cloudshell-aks-spinnaker"
    pulumi stack select $PULUMI_STACK
    
    PLURALSIGHT_RG_NAME="todo_name_given_by_pluralsight_cloud_sandbox" 
    pulumi config set resourceGroupName $PLURALSIGHT_RG_NAME
    
    pulumi config set azure-native:useDefaultAzureCredential false
    pulumi config set azure-native:subscriptionId $ARM_SUBSCRIPTION_ID
     
    gradle clean build 2>&1
    
    tree -L 2
    
    pulumi config list
    pulumi config -j | jq
    pulumi up
    ```

* An Azure account with credentials configured (for example, via `az login` or setting environment variables `ARM_CLIENT_ID`, `ARM_CLIENT_SECRET`, and `ARM_SUBSCRIPTION_ID`).

Azure publishes an open configuration file for every domain that exposes the Tenant ID publicly.

* set the domain tenant e.g. the pluralsight cloud hands on boxes domain

```bash
DOMAIN_TENANT="realhandsonlabs.com"
```

* extract tenant id from domain name

  ```bash
  export ARM_TENANT_ID=$(curl -s https://login.microsoftonline.com/${DOMAIN_TENANT}/.well-known/openid-configuration | grep -o 'https://sts.windows.net/[^/]*' | cut -d '/' -f 4)

  echo $ARM_TENANT_ID
  ```

* Java 11 or higher installed

```bash

```

* install gradle

  ```bash

  ```

* Pulumi CLI installed and logged in

Pulumi [tokens](https://app.pulumi.com/account/tokens)

  ```bash

  ```

* refer to [Pulumi Azure Native Java Getting Started](https://www.pulumi.com/docs/intro/languages/java/)
* refer to [Pulumi Azure Native Getting Started](https://www.pulumi.com/docs/iac/get-started/azure/)

* connect to the target azure account

* set variables according to plural sight restrictions

  ```bash
  PLURALSIGHT_RG_NAME="" && pulumi config set resourceGroupName $PLURALSIGHT_RG_NAME
  ```

* run the pulumi stack

  ```bash
  pulumi up
  ```

---

## Outputs

* pulumi stack shows the priority outputs

## Connect to the AKS cluster

* set `kubectl` with details of the AKS cluster

```bash
az aks get-credentials --resource-group $PLURALSIGHT_RG_NAME --name $CLUSTER_NAME --admin
```

* smoke test connectivity to cluster

```bash
kubectl get nodes
```

---

## Next Steps

* use deployment with FatJar
* Use multiple Pulumi stacks for different environments (development, staging, production).
* Integrate Pulumi into your CI/CD pipeline.
* Explore the Pulumi Azure Native SDK in the [Pulumi Registry](https://www.pulumi.com/registry/packages/azure-native/).

## Getting Help

you have questions or encounter any issues:

* Check out the [Pulumi Documentation](https://www.pulumi.com/docs/).
* Join the [Pulumi Community Slack](https://slack.pulumi.com/) for support.
* File an issue in this repository.

## Azure Authentication & Robustness Notes

### Azure CLI Authentication using a Service Principal

For detailed instructions on authenticating with Azure CLI using a Service Principal and environment variables, please refer to the official [Azure CLI Documentation](https://learn.microsoft.com/en-us/cli/azure/authenticate-azure-cli-service-principal). You will need to have the following environment variables available: `ARM_CLIENT_ID`, `ARM_CLIENT_SECRET`, `ARM_TENANT_ID`, and `ARM_SUBSCRIPTION_ID`.

### Azure CLI validation commands

```bash
export ARM_SUBSCRIPTION_ID="80ea84e8-afce-4851-928a-9e2219724c69"
export ARM_USE_DEFAULT_AZURE_CREDENTIAL=false

echo $ARM_SUBSCRIPTION_ID
echo $ARM_CLIENT_ID
echo $ARM_CLIENT_SECRET
echo $ARM_TENANT_ID
echo $ARM_LOCATION_NAME

AZURE_TENANT_ID="$ARM_TENANT_ID"

export APP_ID="$ARM_CLIENT_ID"
export CLIENT_SECRET="$ARM_CLIENT_SECRET"
export TENANT_ID="$ARM_TENANT_ID"

echo $APP_ID && echo $CLIENT_SECRET && echo $TENANT_ID 

az login --service-principal --username $APP_ID --password $CLIENT_SECRET --tenant $TENANT_ID

```

* validate `az` cli

    ```bash
    az account list --output table
    az account show
    az account list --query '[].{subscriptionName:name,subscriptionId:id}' -o tsv
    az account show | jq
    az group list --query "[?location=='westus']" 
    az account show --query id -o tsv
    az account set --subscription $ARM_SUBSCRIPTION_ID
    az group list
    ```

### Robust AKS Cluster Connection

To ensure a robust connection to the AKS cluster, verify that the `PLURALSIGHT_RG_NAME` and `CLUSTER_NAME` variables are set, and use the `--overwrite-existing` flag if you want to overwrite any existing cluster configurations in your kubeconfig

```bash
if [ -z "$PLURALSIGHT_RG_NAME" ] || [ -z "$CLUSTER_NAME" ]; then
  echo "Error: PLURALSIGHT_RG_NAME or CLUSTER_NAME is not set."
fi

az aks get-credentials --resource-group "$PLURALSIGHT_RG_NAME" --name "$CLUSTER_NAME" --admin --overwrite-existing
```

### smoke test a Kubernetes deployment

from docs in K8s <https://kubernetes.io/docs/tutorials/kubernetes-basics/deploy-app/deploy-intro/>

## Notes running cloud Shell Azure

```bash
cloud [ ~ ]$ vim Pulumi.yaml

cloud [ ~ ]$ pulumi stack
Please choose a stack, or create a new one: <create a new stack>
Please enter your desired stack name.
To create a stack in an organization, use the format <org-name>/<stack-name> (e.g. `acmecorp/dev`): aleon1220/kubernetes-azure-spinnaker/cloudshell-aks-spinnaker 
Please enter your desired stack name.
Current stack is cloudshell-aks-spinnaker:
    Owner: aleon1220
    Tags:
        pulumi:description  A precompiled Java Pulumi program
        pulumi:project      cloudshell-aks-spinnaker
        pulumi:runtime      java
Current stack resources (0):
    No resources currently in this stack

More information at: https://app.pulumi.com/aleon1220/cloudshell-aks-spinnaker/cloudshell-aks-spinnaker

Use `pulumi stack select` to change stack; `pulumi stack ls` lists known ones

echo "Pulumi.yaml needs to be present"

name: kubernetes-azure-spinnaker 
description: A precompiled Java Pulumi program 
runtime:
  name: java
  options:
    binary: kubernetes-azure-spinnaker-0.1.0-2026-05-may-improvements-SNAPSHOT-all.jar


pulumi stack select aleon1220/kubernetes-azure-spinnaker/cloudshell-aks-spinnaker
```

### experiment Build the java Native Distribution

upload local build of distro
chmod +x kubernetes-azure-spinnaker/bin/kubernetes-azure-spinnaker

Pulumi.yaml

wget --verbose https://raw.githubusercontent.com/aleon1220/spinnaker-contrib-playground/refs/heads/2026-05-may-improvements/kubernetes-azure-spinnaker/Pulumi.yaml

pulumi login
<token https://app.pulumi.com/account/tokens>
PULUMI_STACK="aleon1220/kubernetes-azure-spinnaker/cloudshell-aks-spinnaker"
pulumi stack select $PULUMI_STACK

PLURALSIGHT_RG_NAME="todo_name_given_by_pluralsight_cloud_sandbox"
pulumi config set resourceGroupName $PLURALSIGHT_RG_NAME

pulumi up --verbose 3 # observe behavior

## References

* <https://pubs.opengroup.org/onlinepubs/9799919799/utilities/V3_chap02.html>
* [Azure RBAC for AKS](https://learn.microsoft.com/en-us/azure/aks/azure-rbac?tabs=azure-cli)
