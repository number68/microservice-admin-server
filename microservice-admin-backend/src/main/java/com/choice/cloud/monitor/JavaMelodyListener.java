package com.choice.cloud.monitor;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceDeregisteredEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceRegisteredEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import lombok.extern.slf4j.Slf4j;
import net.bull.javamelody.CollectorServlet;
import reactor.core.publisher.Mono;

/**
 * Listener class to register and unregister applications. 
 */
@Configuration
@Slf4j
public class JavaMelodyListener extends AbstractEventNotifier {

    public JavaMelodyListener(InstanceRepository repository) {
        super(repository);
    }

    @Override
    public boolean shouldNotify(InstanceEvent event, Instance instance) {
        if (event instanceof InstanceRegisteredEvent || event instanceof InstanceDeregisteredEvent || event instanceof InstanceStatusChangedEvent) {
            return true;
        }
        return false;
    }

    @Override
    public Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceRegisteredEvent) {
                try {
                    CollectorServlet.addCollectorApplication(instance.getId().getValue(),
                        instance.getRegistration().getServiceUrl());
                    log.info("Added application {} to javamelody", instance.getId().getValue());
                } catch (final IOException e) {
                    log.warn("Failed to register application {} to javamelody" + instance.getId().getValue());
                }
            } else if (event instanceof InstanceDeregisteredEvent) {
                try {
                    CollectorServlet.removeCollectorApplication(instance.getId().getValue());
                    log.info("Removed application {} from javamelody", instance.getId().getValue());
                } catch (final IOException e) {
                    log.warn("Failed to unregister application {} from javamelody" + instance.getId().getValue());
                }
            } else if (event instanceof InstanceStatusChangedEvent) {
                InstanceStatusChangedEvent copyEvent = (InstanceStatusChangedEvent)event;
                StatusInfo statusInfo = copyEvent.getStatusInfo();
                if (statusInfo.isUp()) {
                    try {
                        CollectorServlet.addCollectorApplication(instance.getId().getValue(),
                                instance.getRegistration().getServiceUrl());
                        log.info("Added application {} to javamelody", instance.getId().getValue());
                    } catch (final IOException e) {
                        log.warn("Failed to register application {} to javamelody" + instance.getId().getValue());
                    }
                } else if(statusInfo.isOffline() || statusInfo.isUnknown()){
                    try {
                        CollectorServlet.removeCollectorApplication(instance.getId().getValue());
                        log.info("Removed application {} from javamelody", instance.getId().getValue());
                    } catch (final IOException e) {
                        log.warn("Failed to unregister application {} from javamelody" + instance.getId().getValue());
                    }
                }
            }
        });
    }
}
