# 🌀 spinnaker-contrib-playground

**A collaborative lab for experimenting [Spinnaker](https://spinnaker.io/) installs with Infrastructure as Code (IaC)**

This repository is a community-driven playground for exploring Continuous Delivery automation, multi-cloud deployments, and integration patterns with tools like Iac focusing on Pulumi, Kubernetes, and multi-cloud with emphasis Azure.

the purpose is to repeatably install Spinnaker so many times that it gets more and more excellence and simplicity.

2025 experimenting with pulumi and Azure AKS

---

### 🧭 Roadmap
* [x] Get a stable pulumi IaC AKS cluster and access it from the CLI ✅ 📅 2026-05-09
* [x] Add pulumi flow to deploy AKS: runs as a gradle project calling the gradle subprojects
* [x] Add Pulumi modules for Azure environments with the azure-native provider
* [ ] enable semVer to handle the IaC pulumi releases
* [ ] define and document execution flow local, in Cloud shells and from Github actions
* [ ] install Spinnaker with Kustomize
* [ ] Create example Spinnaker pipeline for multi-cloud deployment
* [ ] Integrate with GitHub Actions for CI
* [ ] Explore Spinnaker Operator for Kubernetes
* [ ] Add contributors and community guidelines

---

## Spinnaker running in AKS via Pulumi IaC

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

## Cloud execution

todo: fetch the artifact and deploy the infrastructure from a cloud-shell

## Local Execution 🚀

### ⚙️ Getting Started

* clone this repo

```bash
git clone https://github.com/aleon1220/spinnaker-contrib-playground.git
```

* **install pulumi if not present**

```bash
curl -fsSL https://get.pulumi.com | sh
```

* pulumi check

```bash
pulumi version
```

**Run IaC examples**

* go to the directory with the pulumi stack and execute

```bash
pulumi up
```

---

## Spinnaker installation

> it seems spinnaker is hard to install

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
