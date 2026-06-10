# Azure SQL Server and Database Provisioning (Java)

connect with DB String below
> ${outputs.baseSQLConnectionString}Encrypt=True;TrustServerCertificate=False;

This Pulumi stack provisions a logical Azure SQL Server and an associated Azure SQL Database using the `pulumi-azure-native` provider in Java. 

**git Repository:** [Azure Infrastructure as Code Provisioning Patterns - Enterprise App Java](https://github.com/aleon1220/azure-iac-provisioning-patterns/tree/main/pulumi/azure-enterprise-app-java)

## Overview

The stack defines the infrastructure required to host a relational database in Azure. Specifically, it creates:
* **Azure SQL Server:** A logical server (version `12.0`) configured with minimal TLS `1.2` and public network access enabled.
* **Azure SQL Database:** A database scaled to the `Basic` tier with `Local` backup storage redundancy, designed for development and testing purposes.

These resources are deployed to the `WestUS2` region in the `anz-devops-sre-platform-engineering-research-dev` resource group.

## Prerequisites 🔗

Before running this Pulumi stack, ensure you have the following tools installed and configured:
* [Pulumi CLI](https://www.pulumi.com/docs/install/)
* Java Development Kit (JDK) 11 or later
* Apache Maven or Gradle (depending on your project setup)
* [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli) (authenticated via `az login`)

## Configuration in Pulumi

This stack requires some configuration variables to be set before deployment. Needs the administrator credentials for the SQL Server.

Set these values using the Pulumi CLI:

#### Set the SQL Admin Username
```shell
pulumi config set sqlAdminUsername <your-admin-username>
```

#### Set the SQL Admin Password as a secure secret
```shell
pulumi config set --secret sqlAdminPassword <your-secure-password>
```