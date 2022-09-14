package com.ruoyi.kubernetes.domain;

public enum ResourceKind {
    pod,
    deployment,
    replicaset,
    statefulset,
    daemonset,
    job,
    cronjob,
    service,
    ingress,
    pv,
    pvc,
    configmap,
    secret,
    node;
}
