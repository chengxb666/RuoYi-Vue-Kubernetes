package com.ruoyi.kubernetes.service;

import com.ruoyi.kubernetes.domain.Cluster;

import java.util.List;

public interface ClusterService {

    public int addCluster(Cluster cluster);
    public int deleteCluster(int clusterid);
    public Cluster queryByid(int clusterid);
    public List<Cluster> queryByName(String clusterCode);
    public int updateCluster(Cluster cluster);
    public List<Cluster> queryAll();

}
