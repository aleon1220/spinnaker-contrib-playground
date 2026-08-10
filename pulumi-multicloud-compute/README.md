# Pulumi Multi-Cloud Compute

This project deploys a compute instance (Ubuntu 24.04 with Docker installed) across AWS, Azure, and GCP simultaneously using Pulumi and Java.

## Prerequisites

* Java 21+
* Pulumi CLI

### Cloud Provider access

> the three of them or just access to one via Plural sight cloud sandboxes.

* AWS Account
  * AWS CLI
* Azure Subscription
  * Azure CLI
* Google Cloud project
  * Google Cloud SDK (gcloud)

## Authentication

Before running Pulumi, authenticate to all three cloud providers:

### AWS

* access AWS programatically

    ```bash
    aws configure
    ```

* use env vars

    ```bash
    export $AWS_ACCESS_KEY_ID  $AWS_SECRET_ACCESS_KEY
    ```

### Azure

* authentication 

    ```bash
    az login
    az account set --subscription="<YOUR_SUBSCRIPTION_ID>"
    ```

### GCP

* login authenticate to GCP

    ```bash
    gcloud auth application-default login
    ```

* Using Service Account. Get the `JSON` file

    ```bash
    gcloud auth activate-service-account --key-file="$HOME/workspace/gcp/sa-plural-sight.json"
    ```

* get GCP project from the service account `JSON` file

* set GCP project

    ```bash
    YOUR_PROJECT_ID=$(jq --raw-output .project_id "$HOME/workspace/gcp/sa-plural-sight.json")

    gcloud config set project "$YOUR_PROJECT_ID"
    ```

## Build

Since this is part of a larger Gradle workspace, you can 

* build the project from this directory:

    ```bash
    ./gradlew build
    ```

* build from the parent directory:

    ```bash
    ./gradlew :pulumi-multicloud-compute:build
    ```

## Deploy the infra stack using pulumi orchestration

1. go to the project

    ```bash
    pushd pulumi-multicloud-compute/
    ```

2. Initialize a new Pulumi stack (e.g., `dev`) or select it

* new execution

    ```bash
    pulumi stack init dev
    ```

* choose stack

    ```bash
    pulumi stack select aleon1220/multicloud-compute/windows11-enterprise
    ```

1. Review the `Pulumi.$STACK_NAME.yaml` and set your configuration variables if needed. Note that a sample SSH key is provided but should be replaced with your own public key:

* set the project name

    ```bash
    pulumi config set projectName $YOUR_PROJECT_ID
    ```

* set the project ID

    ```bash
    pulumi config set --path 'gcp:project' $YOUR_PROJECT_ID
    ```

* get the public SSH key from the default location

    ```bash
    cat ~/.ssh/id_rsa.pub | xclip
    ```

* set the public SSH key

    ```bash
    pulumi config set sshPublicKey "ssh-rsa SSH_PUBLIC_KEY..."
    ```

By default, the project will deploy instances to all three clouds. You can restrict the deployment to a single cloud by setting the `targetCloud` configuration variable for the target **pulumi stack**

* set one of the Options: all (default), aws, azure, gcp

    ```bash
    pulumi config set targetCloud azure
    ```

1. Run the infra deployment

    ```bash
    pulumi up
    ```

This will preview the infrastructure changes and prompt for confirmation before provisioning the resources. The outputs will display the public IP addresses for the VM in either

1. AWS
2. Azure
3. GCP instances.

## Migration Notes

This project replaces the older Terraform setup:
* **AWS**: The older Terraform code (`t3.medium`, `Ubuntu 22.04`, `50GB gp3`) is now migrated to Pulumi `aws:ec2:Instance`. The AMI filter targets Ubuntu 24.04 (`ubuntu-noble-24.04-amd64-server-*`).

* **Azure**: The older AzureRM configuration is now using the `azure-native` Pulumi provider. The `custom_data` block (which was commented out in TF) is now populated with a Cloud-Init script to align Docker setup with AWS. It provisions `Ubuntu 24.04` on a `Standard_DS1_v2` instance.

* **GCP**: The Terraform `gcp/` folder was previously empty of compute resources. It now provisions an `e2-medium` instance running `Ubuntu 24.04` and dynamically injects the startup script via the `user-data` metadata field, ensuring Docker is provisioned consistently across all three clouds.

* **Docker Installation**: The original startup script used short flags (`-y`, etc.). The new setup uses strict long-format flags (`--yes`, `--quiet`, `--parents`, `--verbose`) for all commands as requested.
