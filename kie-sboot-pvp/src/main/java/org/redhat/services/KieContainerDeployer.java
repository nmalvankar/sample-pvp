package org.redhat.services;

import org.redhat.services.model.Kjar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceFilter;
import org.kie.server.services.api.KieServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Deploys KIE Containers from app properties
 */
@Configuration
@ConfigurationProperties(prefix = "deployment")
public class KieContainerDeployer {

    private Logger log = LoggerFactory.getLogger(KieContainerDeployer.class);
    
    @Autowired
    KieServer kieServer;

    private List<Kjar> kjars;

    @Bean
    CommandLineRunner deployAndValidate() {
        return new CommandLineRunner() {

            @Override
            public void run(String... strings) throws Exception {

                log.info("Kie Containers listed for deployments :: {}", kjars);

                // Check which containers are already deployed
                List<KieContainerResource> result = kieServer.listContainers(KieContainerResourceFilter.ACCEPT_ALL).getResult().getContainers();
                result.forEach(c -> {
                    log.info("KIE Containers already deployed {}", c);
                });

                // Deploy containers specified in props
                kjars.forEach(k -> {
                    KieContainerResource resource = new KieContainerResource(k.getContainerId(), k.getReleaseId());
                    resource.setResolvedReleaseId(k.getReleaseId());
                    resource.setContainerAlias(k.getAlias());

                    if (!isDeployed(resource, result)) {
                        log.info("Deploying KIE Container : {} using custom deployer", k);
                        resource.setResolvedReleaseId(null);
                        kieServer.createContainer(k.getContainerId(), resource);
                    } else {
                        log.info("Skipping deployment of KIE Container : {} b/c it's already deployed", k);
                    }
                });
            }
        };
    }

    private boolean isDeployed(KieContainerResource resource, List<KieContainerResource> result) {
        return (result.contains(resource)) ? true : false;
    }

    public List<Kjar> getKjars() {
        return kjars;
    }

    public void setKjars(List<Kjar> kjars) {
        this.kjars = kjars;
    }
}