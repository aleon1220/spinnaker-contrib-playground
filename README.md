# 🌀 spinnaker-contrib-playground

**A collaborative lab for experimenting with [Spinnaker](https://spinnaker.io/) and Infrastructure as Code (IaC)**
This repository is a community-driven playground for exploring Continuous Delivery automation, multi-cloud deployments, and integration patterns with tools like Terraform, Kubernetes, and Azure.

the purpose is to repeatably install Spinnaker so many times that it gets more and more excellence and simplicity.

2025 experimenting with pulumi and Azure AKS

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

2. clone this repo
```bash
git clone https://github.com/aleon1220/spinnaker-contrib-playground.git
```

1. **install pulumi if not present**
```bash
curl -fsSL https://get.pulumi.com | sh
```

3. pulumi check
```bash
pulumi version
```

**Run IaC examples**

```bash
cd pulumi
pulumi up
```

4. **Experiment, tweak, contribute 🚀**

#### Run the pulumi stack as a binary

---

## Spinnaker installation
> it seems spinnaker is hard to install. 
**Set up Spinnaker locally or in a test environment**
   * [Install Halyard](https://spinnaker.io/docs/setup/install/halyard/)

2026-02-04
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

---

### 🧭 Roadmap
* [ ] Get a stable pulumi IaC AKS cluster and access it from the CLI
* [ ] Create example Spinnaker pipeline for multi-cloud deployment
* [ ] Add pulumi flow to deploy AKS
* [ ] Add Terraform modules for Azure/AWS environments
* [ ] Integrate with GitHub Actions for CI
* [ ] Explore Spinnaker Operator for Kubernetes
* [ ] Add contributors and community guidelines

---

### 💡 Vision

To create a **collaborative lab** where engineers, DevOps practitioners, and cloud enthusiasts can explore how **Spinnaker + IaC** can enable scalable, repeatable, and reliable delivery pipelines.

