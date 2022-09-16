package com.ruoyi.kubernetes.domain;

public enum ResourceKind {
    Pod,
    Deployment,
    ReplicaSet,
    StatefulSet,
    DaemonSet,
    Job,
    CronJob,
    Service,
    Ingress,
    PersistentVolume,
    PersistentVolumeClaim,
    ConfigMap,
    Secret;
}
