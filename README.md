## 🌀 spinnaker-contrib-playground

**A collaborative lab for experimenting with [Spinnaker](https://spinnaker.io/) and Infrastructure as Code (IaC)**
This repository is a community-driven playground for exploring Continuous Delivery automation, multi-cloud deployments, and integration patterns with tools like Terraform, Kubernetes, and Azure.

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
should be as simple and intuitive as possible. I am working on a flat structure to get straight away what is needed.

---

### ⚙️ Getting Started

1. **Clone the repo**

   ```bash
   git clone https://github.com/aleon1220/spinnaker-contrib-playground.git
   ```

2. **Set up Spinnaker locally or in a test environment**

   * [Install Halyard](https://spinnaker.io/docs/setup/install/halyard/)
   * Configure your cloud provider account (e.g., Azure, AWS)
   * Apply sample pipeline manifests from `/pipelines`

3. **Run IaC examples**

   ```bash
   cd infra/terraform
   terraform init
   terraform apply
   ```

4. **Experiment, tweak, contribute 🚀**

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

---

### 🧭 Roadmap

* [ ] Create example Spinnaker pipeline for multi-cloud deployment
* [ ] Add pulumi flow to deploy AKS
* [ ] Add Terraform modules for Azure/AWS environments
* [ ] Integrate with GitHub Actions for CI
* [ ] Explore Spinnaker Operator for Kubernetes
* [ ] Add contributors and community guidelines

---

### 💡 Vision

To create a **collaborative lab** where engineers, DevOps practitioners, and cloud enthusiasts can explore how **Spinnaker + IaC** can enable scalable, repeatable, and reliable delivery pipelines.
