# 🌀 spinnaker-contrib-playground

**A collaborative lab for experimenting [Spinnaker](https://spinnaker.io/) installs with Infrastructure as Code (IaC)**

This repository is a community-driven playground for exploring Continuous Delivery automation, multi-cloud deployments, and integration patterns with tools like Iac focusing on Pulumi, Kubernetes, and multi-cloud with emphasis Azure.

the purpose is to repeatably install Spinnaker so many times that it gets more and more excellence and simplicity.

2025 experimenting with pulumi and Azure AKS

---

### 🚀 Purpose

This project serves as a **sandbox** for:

* Experimenting with **Spinnaker pipelines**, triggers, and deployment strategies
* Building **Infrastructure as Code** workflows for consistent, automated environments
* Sharing **community contributions**, scripts, and reusable modules
* Testing integration with major cloud providers (Azure, AWS, GCP)

---

### 🧩 Structure

should be as simple and intuitive as possible. A gradle project with subprojects to execute different platform engineering experiments.

---

## 🧭 Roadmap
* [x] Get a stable pulumi IaC AKS cluster and access it from the CLI ✅ 📅 2026-05-09
* [x] Add pulumi flow to deploy AKS: runs as a gradle project calling the gradle subprojects
* [x] Add Pulumi modules for Azure environments with the azure-native provider
* [x] enable semVer to handle the IaC pulumi releases
* [ ] define and document execution flow local, in Cloud shells and from Github actions
* [x] install Spinnaker with Kustomize ✅ 📅 2026-06-09 i have to upgrade dependencies and is not a fully functional spinnaker.
* [ ] Create example Spinnaker pipeline for multi-cloud deployment
* [ ] Integrate with GitHub Actions for CI
* [ ] Explore Spinnaker Operator for Kubernetes
* [ ] Add contributors and community guidelines
* [] fetch the artifact and deploy the infrastructure from a cloud-shell

---

## Plural sight Azure Cloud execution

### create a temporary Azure sandbox

1. https://app.pluralsight.com/hands-on/playground/cloud-sandboxes 
2. open sandbox 
3. use a browser with private or incognito mode
4. you will get the following
    - Username to access the azure portal
    - Password to access the azure portal
    - Application Client ID: to be used with the `az` cli
    - Application Client Secret: to be used with the `az` cli for authentication

### connect azure CLI to azure sandbox subscription Linux

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
```

* Login with Azure CLI using service principal

    ```bash
    az login --service-principal --username $ARM_CLIENT_ID --password $ARM_CLIENT_SECRET --tenant $ARM_TENANT_ID
    ```

* login to [azure portal](https://portal.azure.com/) 

* open a cloud shell bash

* obtain the tenant ID

    ```bash
    export ARM_TENANT_ID=$(az account show --query tenantId -o tsv)
    ```

* set resource group

```bash
PLURALSIGHT_RG_NAME=$(az group list --query "[?location=='westus']" | jq -r '.[0].name')
echo $PLURALSIGHT_RG_NAME
```

### connect azure CLI to azure sandbox subscription Windows 🚀



---

### 🤝 Contributing

Contributions are welcome!
If you’d like to:

* Add new pipeline templates
* Improve IaC examples
* Document deployment scenarios

Please open a **pull request** or start a **discussion** in the repo.

**Contribution guidelines**:

* Use clear commit messages
* Include minimal reproducible examples
* Document any required credentials or secrets (but never include secrets directly!)

---

### 📚 Learning Resources

* [Spinnaker Official Docs](https://spinnaker.io/docs/)
* [Terraform by HashiCorp](https://developer.hashicorp.com/terraform)
* [Kubernetes Deployment Strategies](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/)
* [GitOps and Continuous Delivery Patterns](https://www.weave.works/technologies/gitops/)

### 💡 Vision

To create a **collaborative lab** where engineers, DevOps practitioners, and cloud enthusiasts can explore how **Spinnaker + IaC** can enable scalable, repeatable, and reliable delivery pipelines.

## Getting Started with a pulumi project

To create a new project from this template, run:

 ```bash
 pulumi new azure-java
 ```

Follow the interactive prompts:

* Project name
* Project description
* `azure-native:location`: The Azure location to use (default: WestUS2)
* change into your project directory and preview or deploy your stack:

    ```bash
    cd <project-directory>
    pulumi up
    ```

## Project Layout

* each directory is a gradle sub-project. Preference is given to Java Pulumi.

## Configuration

* suggest to customise the pulumi flows by setting values and environment variables using pulumi cli
* Secret management leverages 1Password to inject secrets and tokens

To override the default location, run:

```bash
pulumi config set azure-native:location <your-region>
```
