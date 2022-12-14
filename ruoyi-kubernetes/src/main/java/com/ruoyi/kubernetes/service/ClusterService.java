package com.ruoyi.kubernetes.service;

import com.ruoyi.kubernetes.domain.Cluster;

import java.util.List;

public interface ClusterService {

    public int addCluster(Cluster cluster);
    public int deleteCluster(String clusterCode);
    public Cluster queryByid(int clusterid);
    public Cluster queryByName(String clusterCode);
    public int updateCluster(Cluster cluster);
    public List<Cluster> queryAll();
    public List<String> queryClusterCodes() throws Exception;
}
