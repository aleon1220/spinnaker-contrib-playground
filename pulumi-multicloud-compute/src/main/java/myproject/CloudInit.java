package myproject;

import java.util.Base64;

public class CloudInit {
    public static String getCloudInitScript(String projectName, String hostname) {
        String script = """
            #!/bin/bash -eu
            exec > /var/log/userdata.log 2>&1
            
            HOME="/home/ubuntu"
            PURPOSE="%s"
            EC2_HOSTNAME="%s"
            COMPOSE_VERSION="v2.18.1"
            DOCKER_COMPOSE_INSTALL="/usr/local/libexec/docker/cli-plugins"
            MACHINE_HARDWARE=$(uname --kernel-name)
            LOWER_CASE_MACHINE="linux"
            KERNEL_NAME=$(uname --machine)
            printf "\\n\\t\\t VM setup via Pulumi for project %%s \\n\\n" "$PURPOSE"
            sudo hostnamectl set-hostname "$EC2_HOSTNAME"
            export PATH=~/bin:$PATH
            
            printf "\\n export PATH=~/bin:%%s \\n" "$PATH" >> ~/.bashrc
            printf "\\n\\t\\t HOSTNAME set to %%s \\n\\n" "$(hostname)"
            
            sleep 30
            printf "\\n\\t\\t Waited 30 seconds... \\n\\n"
            echo 'Acquire::Retries "3";' > /etc/apt/apt.conf.d/80retries
            
            sudo apt-get update --quiet --yes && \\
            sudo apt-get install --quiet --yes \\
               ca-certificates \\
               curl \\
               gnupg \\
               lsb-release
            
            sudo mkdir --parents /etc/apt/keyrings
            curl --fail --silent --show-error --location https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor --output /etc/apt/keyrings/docker.gpg
            
            echo \\
              "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \\
              $(lsb_release --short --codename) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
            
            sudo apt-get update --quiet --yes && sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin --quiet --yes
            printf "\\n\\t\\t Installed Docker"
            
            sudo usermod --append --groups docker ubuntu
            printf "\\n\\t\\t Finished Docker Post-install steps"
            printf "\\n\\t\\t VM hardware %%s \\n" "$MACHINE_HARDWARE"
            sudo mkdir --parents --verbose $DOCKER_COMPOSE_INSTALL
            sudo curl --location "https://github.com/docker/compose/releases/download/$COMPOSE_VERSION/docker-compose-$LOWER_CASE_MACHINE-$KERNEL_NAME" --output "$DOCKER_COMPOSE_INSTALL/docker-compose"
            sudo chmod --changes +x "$DOCKER_COMPOSE_INSTALL/docker-compose"
            sudo cp --verbose "$DOCKER_COMPOSE_INSTALL/docker-compose" /usr/bin/
            docker-compose --version
            printf "\\n\\t\\t Installed docker-compose %%s \\n" "$COMPOSE_VERSION"
            printf "\\n\\t\\t Finished Setting up Infrastructure environment configurations. Rebooting next \\n"
            sudo reboot
            """.formatted(projectName, hostname);
            
        return Base64.getEncoder().encodeToString(script.getBytes());
    }
}
