package com.ruoyi.kubernetes.utils;

import com.ruoyi.common.utils.StringUtils;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.CronJob;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import io.fabric8.kubernetes.api.model.rbac.ClusterRole;
import io.fabric8.kubernetes.api.model.rbac.ClusterRoleBinding;
import io.fabric8.kubernetes.api.model.rbac.Role;
import io.fabric8.kubernetes.api.model.rbac.RoleBinding;
import io.fabric8.kubernetes.api.model.storage.StorageClass;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.PodResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class Commit {

    @Autowired
    private ClientUtils clientUtils;

    private static final Logger log = LoggerFactory.getLogger(Commit.class);

    public Boolean CreateWorkLoad(String fileName,String namespace,String targetClusterCode,String action) throws FileNotFoundException {

        if(StringUtils.isEmpty(action) || StringUtils.isNull(action)) {
            log.error("action should not be null");
            return false;
        }
        if(!(action.equals("update") || action.equals("delete"))){
            log.error("action should be update or delete");
            return false;
        }

        File file = new File(fileName);
        if (!file.exists() || !file.isFile()) {
            log.error("File {} does not exist", fileName);
            return false;
        }

        try (KubernetesClient client = clientUtils.ClientBuilder(targetClusterCode)) {

            List<HasMetadata> resources = client.load(new FileInputStream(fileName)).get();
            if (resources.isEmpty()) {
                log.error("No resources loaded from file: {}", fileName);
                return false;
            }
            HasMetadata resource = resources.get(0);
            if (resource instanceof Pod){
                Pod po = (Pod) resource;
                log.info("Creating pod in namespace {}", namespace);
                NonNamespaceOperation<Pod, PodList, PodResource<Pod>> pods = client.pods().inNamespace(namespace);
                if(action.equals("update")){
                    Pod pod = pods.createOrReplace(po);
                    log.info("Created pod {} in namespace {} of cluster {}", pod.getMetadata().getName(),namespace,targetClusterCode);
                }else{
                    Boolean delete = pods.delete(po);
                    log.info("pod {} in namespace {} of cluster {} is {} deleted", po.getMetadata().getName(),namespace,targetClusterCode,delete);
                }
            } else if (resource instanceof Deployment) {
                Deployment deploy = (Deployment) resource;
                log.info("Creating deploy in namespace {}", namespace);
                if(action.equals("update")){
                    Deployment deployment = client.apps().deployments().inNamespace(namespace).createOrReplace(deploy);
                    log.info("Created deployment {} in namespace {} of cluster {}", deployment.getMetadata().getName(),namespace,targetClusterCode);
                }else{
                    Boolean delete = client.apps().deployments().inNamespace(namespace).delete(deploy);
                    log.info("pod {} in namespace {} of cluster {} is {} deleted", deploy.getMetadata().getName(),namespace,targetClusterCode,delete);
                }
            }else if (resource instanceof DaemonSet) {
                DaemonSet daemon = (DaemonSet) resource;
                log.info("Creating daemon in namespace {}", namespace);
                DaemonSet daemonSet = client.apps().daemonSets().inNamespace(namespace).createOrReplace(daemon);
                log.info("Created daemonset {} in namespace {} of cluster {}", daemonSet.getMetadata().getName(),namespace,targetClusterCode);
            }else if (resource instanceof StatefulSet) {
                StatefulSet stateful = (StatefulSet) resource;
                log.info("Creating stateful in namespace {}", namespace);
                StatefulSet statefulset = client.apps().statefulSets().inNamespace(namespace).createOrReplace(stateful);
                log.info("Created statefulset {} in namespace {} of cluster {}", statefulset.getMetadata().getName(),namespace,targetClusterCode);
            }else if (resource instanceof Job) {
                Job jo = (Job) resource;
                log.info("Creating jo in namespace {}", namespace);
                Job job = client.batch().v1().jobs().inNamespace(namespace).createOrReplace(jo);
                log.info("Created job {} in namespace {} of cluster {}", job.getMetadata().getName(),namespace,targetClusterCode);
            }else if (resource instanceof CronJob) {
                CronJob cron = (CronJob) resource;
                log.info("Creating cron in namespace {}", namespace);
                CronJob cronjob = client.batch().v1().cronjobs().inNamespace(namespace).createOrReplace(cron);
                log.info("Created cronjob {} in namespace {} of cluster {}", cronjob.getMetadata().getName(),namespace,targetClusterCode);
            } else {
                log.error("Loaded resource {} is not a valid resourceKind of workload! ", resource);
                return false;
            }
        }catch (Exception e){
            log.error(e.getMessage());
        } finally {
            return true;
        }
    }

    public void CreateConfig(String fileName,String namespace,String targetClusterCode) throws FileNotFoundException{
        File file = new File(fileName);
        if (!file.exists() || !file.isFile()) {
            log.error("File {} does not exist", fileName);
            return;
        }

        try (KubernetesClient client = clientUtils.ClientBuilder(targetClusterCode)) {

            List<HasMetadata> resources = client.load(new FileInputStream(fileName)).get();
            if (resources.isEmpty()) {
                log.error("No resources loaded from file: {}", fileName);
                return;
            }
            HasMetadata resource = resources.get(0);
            if (resource instanceof ConfigMap){
                ConfigMap config = (ConfigMap) resource;
                log.info("Creating cm in namespace {}", namespace);
                ConfigMap configMap = client.configMaps().inNamespace(namespace).createOrReplace(config);
                log.info("Created cronjob {} in namespace {} of cluster {}", configMap.getMetadata().getName(),namespace,targetClusterCode);
            }else if (resource instanceof Secret){
                Secret sec = (Secret) resource;
                log.info("Creating secret in namespace {}", namespace);
                Secret secret = client.secrets().inNamespace(namespace).createOrReplace(sec);
                log.info("Created secret {} in namespace {} of cluster {}", secret.getMetadata().getName(),namespace,targetClusterCode);
            }else {
                log.error("Loaded resource {} is not a valid configure! ", resource);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public void CreateNetwork(String fileName,String namespace,String targetClusterCode) throws FileNotFoundException{
        File file = new File(fileName);
        if (!file.exists() || !file.isFile()) {
            log.error("File {} does not exist", fileName);
            return;
        }

        try (KubernetesClient client = clientUtils.ClientBuilder(targetClusterCode)) {

            List<HasMetadata> resources = client.load(new FileInputStream(fileName)).get();
            if (resources.isEmpty()) {
                log.error("No resources loaded from file: {}", fileName);
                return;
            }
            HasMetadata resource = resources.get(0);
            if (resource instanceof Service){
                Service svc = (Service) resource;
                log.info("Creating service in namespace {}", namespace);
                Service service = client.services().inNamespace(namespace).createOrReplace(svc);
                log.info("Created service {} in namespace {} of cluster {}", service.getMetadata().getName(),namespace,targetClusterCode);
            }else if (resource instanceof Ingress){
                Ingress ing = (Ingress) resource;
                log.info("Creating ingress in namespace {}", namespace);
                Ingress ingress = client.extensions().ingresses().inNamespace(namespace).createOrReplace(ing);
                log.info("Created ingress {} in namespace {} of cluster {}", ingress.getMetadata().getName(),namespace,targetClusterCode);
            }else {
                log.error("Loaded resource {} is not a valid networking resurce! ", resource);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public void CreateStorage(String fileName,String namespace,String targetClusterCode) throws FileNotFoundException{
        File file = new File(fileName);
        if (!file.exists() || !file.isFile()) {
            log.error("File {} does not exist", fileName);
            return;
        }

        try (KubernetesClient client = clientUtils.ClientBuilder(targetClusterCode)) {

            List<HasMetadata> resources = client.load(new FileInputStream(fileName)).get();
            if (resources.isEmpty()) {
                log.error("No resources loaded from file: {}", fileName);
                return;
            }
            HasMetadata resource = resources.get(0);
            if (resource instanceof PersistentVolume){
                PersistentVolume oldpv = (PersistentVolume) resource;
                log.info("Creating pv {}", oldpv);
                PersistentVolume newpv = client.persistentVolumes().createOrReplace(oldpv);
                log.info("Created pv {} in cluster {}", newpv.getMetadata().getName(), targetClusterCode);
            }else if (resource instanceof PersistentVolumeClaim){
                PersistentVolumeClaim oldpvc = (PersistentVolumeClaim) resource;
                log.info("Creating pvc {} in namespace {}", oldpvc,namespace);
                PersistentVolumeClaim newpvc = client.persistentVolumeClaims().inNamespace(namespace).createOrReplace(oldpvc);
                log.info("Created pvc {} in namespace {} of cluster {}", newpvc.getMetadata().getName(),namespace,targetClusterCode);
            }else if (resource instanceof StorageClass){
                StorageClass oldsc = (StorageClass) resource;
                log.info("Creating sc  {}", oldsc);
                StorageClass newsc = client.storage().storageClasses().createOrReplace(oldsc);
                log.info("Created sc {} in  cluster {}", newsc.getMetadata().getName(),targetClusterCode);
            }else {
                log.error("Loaded resource {} is not a valid storage! ", resource);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public void CreateResource(String fileName,String namespace,String targetClusterCode) throws FileNotFoundException{
        File file = new File(fileName);
        if (!file.exists() || !file.isFile()) {
            log.error("File {} does not exist", fileName);
            return;
        }

        try (KubernetesClient client = clientUtils.ClientBuilder(targetClusterCode)) {

            List<HasMetadata> resources = client.load(new FileInputStream(fileName)).get();
            if (resources.isEmpty()) {
                log.error("No resources loaded from file: {}", fileName);
                return;
            }
            HasMetadata resource = resources.get(0);
            if (resource instanceof Namespace){
                Namespace oldns = (Namespace) resource;
                log.info("Creating ns {}", oldns);
                Namespace newns = client.namespaces().createOrReplace(oldns);
                log.info("Created ns {} in cluster {}", newns.getMetadata().getName(), targetClusterCode);
            }else if (resource instanceof Role){
                Role ro = (Role) resource;
                log.info("Creating role {}", ro);
                Role role = client.rbac().roles().createOrReplace(ro);
                log.info("Created role {} of cluster {}", role.getMetadata().getName(),targetClusterCode);
            }else if (resource instanceof RoleBinding){
                RoleBinding roleBind = (RoleBinding) resource;
                log.info("Creating roleBinding {}", roleBind);
                RoleBinding roleBinding = client.rbac().roleBindings().createOrReplace(roleBind);
                log.info("Created roleBinding {} in  cluster {}", roleBinding.getMetadata().getName(),targetClusterCode);
            }else if (resource instanceof ClusterRole){
                ClusterRole cro = (ClusterRole) resource;
                log.info("Creating clusterRole {}", cro);
                ClusterRole clusterRole = client.rbac().clusterRoles().createOrReplace(cro);
                log.info("Created clusterRole {} of cluster {}", clusterRole.getMetadata().getName(),targetClusterCode);
            }else if (resource instanceof ClusterRoleBinding){
                ClusterRoleBinding clusterRoleBind = (ClusterRoleBinding) resource;
                log.info("Creating clusterRoleBinding {}", clusterRoleBind);
                ClusterRoleBinding clusterRoleBinding = client.rbac().clusterRoleBindings().createOrReplace(clusterRoleBind);
                log.info("Created clusterRoleBinding {} in  cluster {}", clusterRoleBinding.getMetadata().getName(),targetClusterCode);
            }else if (resource instanceof ServiceAccount){
                ServiceAccount serviceAcc = (ServiceAccount) resource;
                log.info("Creating ServiceAccount {}", serviceAcc);
                ServiceAccount serviceAccount = client.serviceAccounts().inNamespace(namespace).createOrReplace(serviceAcc);
                log.info("Created ServiceAccount {} in namespace {} of cluster {}", serviceAccount.getMetadata().getName(),namespace,targetClusterCode);
            }else {
                log.error("Loaded resource {} is not a valid resource! ", resource);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
