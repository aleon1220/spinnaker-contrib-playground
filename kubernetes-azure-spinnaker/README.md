 # Pulumi Template: Azure Native Java Storage

 A minimal Pulumi template for creating a Resource Group and Storage Account on Azure using Java and the Azure Native provider. This template provisions:

 - An Azure Resource Group.
 - An Azure Storage Account (Standard_LRS, StorageV2).
 - Exports the storage account name.

 ## Prerequisites

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



PLURALSIGHT_RG_NAME="" && pulumi config set resourceGroupName $PLURALSIGHT_RG_NAME

- run the pulumi stack


## Outputs

 - `storageAccountName`: The name of the Storage Account.

---

 ## Next Steps

 - Extend `App.java` to add more Azure resources (for example, Cosmos DB, Functions, or Networking).
 - Use multiple Pulumi stacks for different environments (development, staging, production).
 - Integrate Pulumi into your CI/CD pipeline.
 - Explore the Pulumi Azure Native SDK in the [Pulumi Registry](https://www.pulumi.com/registry/packages/azure-native/).

## Getting Started

 To create a new project from this template, run:

 ```bash
 pulumi new azure-java
 ```

 Follow the interactive prompts:

 - Project name
 - Project description
 - `azure-native:location`: The Azure location to use (default: WestUS2)

 Then, change into your project directory and preview or deploy your stack:

 ```bash
 cd <project-directory>
 pulumi up
 ```

 ## Project Layout

 ```plaintext
 .
 ├── Pulumi.yaml         # Project and template metadata
 ├── pom.xml             # Maven project file with dependencies
 └── src
     └── main
         └── java
             └── myproject
                 └── App.java  # Main program defining Azure resources
 ```

 ## Configuration

 | Key                       | Description                     | Default  |
 |---------------------------|---------------------------------|----------|
 | `azure-native:location`   | Azure region for resources      | WestUS2  |

 To override the default location, run:

 ```bash
 pulumi config set azure-native:location <your-region>
 ```


 ## Getting Help

 If you have questions or encounter any issues:

 - Check out the [Pulumi Documentation](https://www.pulumi.com/docs/).
 - Join the [Pulumi Community Slack](https://slack.pulumi.com/) for support.
 - File an issue in this repository.