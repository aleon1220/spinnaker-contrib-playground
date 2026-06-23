# Pulumi Multi-Cloud Compute

This project deploys a compute instance (Ubuntu 24.04 with Docker installed) across AWS, Azure, and GCP simultaneously using Pulumi and Java.

## Prerequisites

- Java 21+
- Pulumi CLI
- AWS CLI
- Azure CLI
- Google Cloud SDK (gcloud)

## Authentication

Before running Pulumi, authenticate to all three cloud providers:

### AWS
```bash
aws configure
# or export AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY
```

### Azure
```bash
az login
az account set --subscription="<YOUR_SUBSCRIPTION_ID>"
```

### GCP
```bash
gcloud auth application-default login
gcloud config set project <YOUR_PROJECT_ID>
```

## Build

Since this is part of a larger Gradle workspace, you can build the project from this directory:
```bash
../gradlew build
```
Or from the parent directory:
```bash
./gradlew :pulumi-multicloud-compute:build
```

## Deploy

1. Initialize a new Pulumi stack (e.g., `dev`):
```bash
pulumi stack init dev
```

2. Review the `Pulumi.dev.yaml` and set your configuration variables if needed. Note that a sample SSH key is provided but should be replaced with your own public key:
```bash
pulumi config set --path 'gcp:project' your-gcp-project-id
pulumi config set sshPublicKey "ssh-rsa YOUR_PUBLIC_KEY..."
```

3. Run the deployment:
```bash
pulumi up
```
This will preview the infrastructure changes and prompt for confirmation before provisioning the resources. The outputs will display the public IP addresses for the AWS, Azure, and GCP instances.

## Migration Notes

This project replaces the older Terraform setup:
- **AWS**: The older Terraform code (`t3.medium`, `Ubuntu 22.04`, `50GB gp3`) is now migrated to Pulumi `aws:ec2:Instance`. The AMI filter targets Ubuntu 24.04 (`ubuntu-noble-24.04-amd64-server-*`).
- **Azure**: The older AzureRM configuration is now using the `azure-native` Pulumi provider. The `custom_data` block (which was commented out in TF) is now populated with a Cloud-Init script to align Docker setup with AWS. It provisions `Ubuntu 24.04` on a `Standard_DS1_v2` instance.
- **GCP**: The Terraform `gcp/` folder was previously empty of compute resources. It now provisions an `e2-medium` instance running `Ubuntu 24.04` and dynamically injects the startup script via the `user-data` metadata field, ensuring Docker is provisioned consistently across all three clouds.
- **Docker Installation**: The original startup script used short flags (`-y`, etc.). The new setup uses strict long-format flags (`--yes`, `--quiet`, `--parents`, `--verbose`) for all commands as requested.
