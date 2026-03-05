# Pulumi Azure AKS
## Execution forms
to run the stacks you need a bash, clone of this repo, pulumi installed and dotnet > v8.0

1. Execute from Azure Shell
2. Execute from code Spaces
3. Run locally
leveraging pluralsight cloud sandboxes with the benefit of using free cloud resources for quick and clean experimentation.

when using pluralsight Azure cloud sandboxes some restrictions [are in place](https://help.pluralsight.com/hc/en-us/articles/24392988447636-Azure-cloud-sandbox)

## DotNet Azure classes
```mermaid
classDiagram
    direction BT
    
    class Config {
        +Get(key)
        +Require(key)
    }
    class ResourceGroup {
        +String Name
    }
    class VirtualNetwork {
        +String ResourceGroupName
        +AddressSpace AddressSpace
    }
    class Subnet {
        +String ResourceGroupName
        +String VirtualNetworkName
        +String AddressPrefix
    }
    class ManagedCluster {
        +String ResourceGroupName
        +AgentPoolProfile AgentPoolProfiles
        +NetworkProfile NetworkProfile
        +LinuxProfile LinuxProfile
    }

    VirtualNetwork --> ResourceGroup : deployed in
    Subnet --> VirtualNetwork : contained in
    Subnet --> ResourceGroup : deployed in
    ManagedCluster --> ResourceGroup : deployed in
    ManagedCluster --> Subnet : agent pool uses (subnet1)
    ManagedCluster ..> Config : uses settings
    VirtualNetwork ..> Config : uses settings
```
