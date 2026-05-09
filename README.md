# 🌀 spinnaker-contrib-playground

**A collaborative lab for experimenting [Spinnaker](https://spinnaker.io/) installs with Infrastructure as Code (IaC)**

This repository is a community-driven playground for exploring Continuous Delivery automation, multi-cloud deployments, and integration patterns with tools like Pulumi, Kubernetes, and Azure.

the purpose is to repeatably install Spinnaker so many times that it gets more and more excellence and simplicity.

2025 experimenting with pulumi and Azure AKS

---

### 🧭 Roadmap
* [x] Get a stable pulumi IaC AKS cluster and access it from the CLI ✅ 📅 2026-05-09
* [ ] Create example Spinnaker pipeline for multi-cloud deployment
* [ ] Add pulumi flow to deploy AKS
* [ ] Add Pulumi modules for Azure environments with the azure-native provider
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

should be as simple and intuitive as possible. I am working on a flat structure to get straight away what is needed.

requirements inside each directory.

---

### ⚙️ Getting Started

- clone this repo

```bash
git clone https://github.com/aleon1220/spinnaker-contrib-playground.git
```

- **install pulumi if not present**

```bash
curl -fsSL https://get.pulumi.com | sh
```

- pulumi check

```bash
pulumi version
```

**Run IaC examples**

- go to the directory with the pulumi stack and execute

### Experiment, tweak, contribute 🚀

---

## Spinnaker installation

> it seems spinnaker is hard to install. 

**Set up Spinnaker locally or in a test environment**
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

* Configure your cloud provider account (e.g., Azure, AWS)
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
