package com.ruoyi.kubernetes.mapper;

import com.ruoyi.kubernetes.domain.Cluster;

import java.util.List;

public interface ClusterMapper {
    public int addCluster(Cluster cluster);
    public int deleteCluster(int clusterid);
    public Cluster queryByid(int clusterid);
    public Cluster queryByName(String clusterCode);
    public int updateCluster(Cluster cluster);
    List<Cluster> query();
}
