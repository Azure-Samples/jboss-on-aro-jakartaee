package org.jboss.eap.demo;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;

@Liveness
@ApplicationScoped
public class SuccessfulCheck implements HealthCheck {
    private static final String HC_NAME = SuccessfulCheck.class.getSimpleName();

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up(HC_NAME);
    }
}
