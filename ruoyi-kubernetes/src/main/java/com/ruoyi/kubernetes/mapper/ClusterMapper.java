package com.ruoyi.kubernetes.mapper;

import com.ruoyi.kubernetes.domain.Cluster;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClusterMapper {
    public int addCluster(Cluster cluster);
    public int deleteCluster(int clusterId);
    public int deleteClusterByName(String clusterCode);
    public Cluster queryById(int clusterId);
    public Cluster queryByName(String clusterCode);
    public int updateCluster(Cluster cluster);
    List<Cluster> query();
}
