package com.ruoyi.kubernetes.utils;

import io.fabric8.kubernetes.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientUtils {

    private static final Logger log = LoggerFactory.getLogger(ClientUtils.class);


    public KubernetesClient ClientBuilder(String url,String token){
        Config config = new ConfigBuilder().withTrustCerts(true).withMasterUrl(url).withOauthToken(token).build();
        try (final KubernetesClient client = new DefaultKubernetesClient(config)) {

            log.info("Received pods {}", client.pods().list());
            return client;

        } catch (Exception e) {
            log.error(e.getMessage(), e);

            Throwable[] suppressed = e.getSuppressed();
            if (suppressed != null) {
                for (Throwable t : suppressed) {
                    log.error(t.getMessage(), t);
                }
            }
        }
        return null;
    }
}
