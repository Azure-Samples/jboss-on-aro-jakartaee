package org.jboss.eap.demo;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Readiness
public class DBConnectionHealthCheck implements HealthCheck {
    private static final String HC_NAME = DBConnectionHealthCheck.class.getSimpleName();

    @PersistenceContext
    EntityManager em;

    @Override
    public HealthCheckResponse call() {
        try {
            em.createNativeQuery("SELECT 'test-connection'").getSingleResult();
            return HealthCheckResponse.up(HC_NAME);
        } catch (Exception e) {
            return HealthCheckResponse.builder()
                    .name(HC_NAME)
                    .down()
                    .withData("exception", e.toString())
                    .build();
        }
    }
}
