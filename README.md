# 获取 Kubernetes 集群 Token

1. 在 Kubernetes 集群部署 `admin.yaml`，yaml 内容如下：

   ```yaml
   apiVersion: v1
   kind: ServiceAccount
   metadata:
     name: admin-token
     namespace: kube-system
   kind: ClusterRoleBinding
   apiVersion: rbac.authorization.k8s.io/v1
   metadata:
     name: admin-token
   subjects:
       kind: ServiceAccount
       name: admin-token
       namespace: kube-system
   roleRef:
     kind: ClusterRole
     name: cluster-admin
     apiGroup: rbac.authorization.k8s.io
   ```

2. 应用 k8s-admin.yaml 配置

   ```bash
   $ kubectl apply -f admin.yaml
   ```

3. 获取 admin-token 名字

   ```bash
   $ kubectl get secret -n kube-system|grep admin
   -->$(secret-name)
   ```

4. 查询 token 内容

   ```bash
   $ kubectl describe secret $(secret-name) -n kube-system 

5.  token 大概如下：

   ```bash
   [root@localhost ~]# minikube kubectl -- describe secret admin-token-token-n95mz -n kube-system
   Name:         admin-token-token-n95mz
   Namespace:    kube-system
   Labels:       <none>
   Annotations:  kubernetes.io/service-account.name: admin-token
                 kubernetes.io/service-account.uid: 65c0af3e-f4fb-44dc-be8b-db0ca303b541
   
   Type:  kubernetes.io/service-account-token
   
   Data
   ====
   ca.crt:     1111 bytes
   namespace:  11 bytes
   token:      eyJhbGciOiJSUzI1NiIsImtpZCI6IlJqbW9oaks4d0NqVEJSTy1kUjM2akgxSzlDblZTXzBNMFJoQ0dRblBzLWsifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJhZG1pbi10b2tlbi10b2tlbi1uOTVteiIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJhZG1pbi10b2tlbiIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjY1YzBhZjNlLWY0ZmItNDRkYy1iZThiLWRiMGNhMzAzYjU0MSIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlLXN5c3RlbTphZG1pbi10b2tlbiJ9.cUAJXfVZbmacNRvfLebjkHOQysLKMBAAoC_pVwCaWR6HrQucsYKtgY007OKdQsJdydB28Ofxtlo4tI393ktQx6Z1_VXv11XYFg3pUgmrjoZF21rdIvVDZZD4TT6sdiAf1LqRqDxDOaxnD0aCC8DQ3Xi7nM5SKLU9MPHMFf5R5SJcLfp8u806rGRxM8GWeWPupSNAa31Wjr0ALXHHUFebT81PEt280Bihm3I6OHzIF3tC1douuEy9kwFZkQD1enSb9-5982TM_B-ONrrPqNMWDAAVsnE4XA8IP9ku3mHyZ9iBr7fO93elf6dedqljaYFmV2xdDN6hMLNekJEf_ojSHg
   ```
