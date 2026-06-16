# ==============================================================================
# Azure IaC Environment Variables Template
# ==============================================================================
# INSTRUCTIONS:
# 1. Do NOT insert raw secrets into this file.
# 2. Replace <vault-name> with the name of your 1Password vault (e.g., 'DevOps' or 'Private').
# 3. Replace <item-name> with the name of your 1Password item (e.g., 'Azure-SPN-Sandbox').
# 4. Use `op inject` to generate your local .env file, or `op run` to inject at runtime.
# ==============================================================================

# Azure Service Principal Authentication
ARM_CLIENT_ID=""op://Pro-IT Projects/azure pluralsight temp sandbox/Azure Sandbox programatic Access/Application Client ID""
ARM_CLIENT_SECRET="op://Pro-IT Projects/azure pluralsight temp sandbox/Azure Sandbox programatic Access/Secret"
ARM_TENANT_ID="op://Pro-IT Projects/azure pluralsight temp sandbox/Azure Sandbox programatic Access/ARM_TENANT_ID"

# Optional: Pulumi Passphrase (If you are using a local or Azure Blob state backend)
# PULUMI_CONFIG_PASSPHRASE="op://<vault-name>/Pulumi-State-Key/credential"