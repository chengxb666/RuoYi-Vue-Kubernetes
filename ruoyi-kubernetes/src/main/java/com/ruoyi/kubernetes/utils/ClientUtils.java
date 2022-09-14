package com.ruoyi.kubernetes.utils;

import com.ruoyi.kubernetes.domain.Cluster;
import com.ruoyi.kubernetes.service.ClusterService;
import io.fabric8.kubernetes.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientUtils {

    @Autowired
    private ClusterService clusterService;

    private static final Logger log = LoggerFactory.getLogger(ClientUtils.class);


    public KubernetesClient ClientBuilder(String clusterCode){
        Cluster targetCluster = clusterService.queryByName(clusterCode);
        String url = targetCluster.getMasterUrl();
        String token = targetCluster.getToken();
        Config config = new ConfigBuilder().withTrustCerts(true).withMasterUrl(url).withOauthToken(token).build();
        try (final KubernetesClient client = new DefaultKubernetesClient(config)) {

            log.info("Received pods {}", client.pods().list());
            return client;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
