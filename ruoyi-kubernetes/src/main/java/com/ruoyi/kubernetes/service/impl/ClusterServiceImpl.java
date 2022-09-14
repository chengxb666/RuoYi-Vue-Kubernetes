package com.ruoyi.kubernetes.service.impl;

import com.ruoyi.kubernetes.domain.Cluster;
import com.ruoyi.kubernetes.mapper.ClusterMapper;
import com.ruoyi.kubernetes.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.StringUtils;

import java.util.List;

@Service
public class ClusterServiceImpl implements ClusterService{

    @Autowired
    private ClusterMapper clusterMapper;

    @Override
    public int addCluster(Cluster cluster) {
        int row = clusterMapper.addCluster(cluster);
        return row>0?row:0;
    }

    @Override
    public int deleteCluster(int clusterid) {
        int row = clusterMapper.deleteCluster(clusterid);
        return row>0?row:0;
    }

    @Override
    public Cluster queryByid(int clusterid) {
        if(clusterid>0){
            Cluster theOnlyCluster = clusterMapper.queryByid(clusterid);
            if(StringUtils.isNotNull(theOnlyCluster)){
                return theOnlyCluster;
            }
        }
        return null;
    }

    @Override
    public List<Cluster> queryByName(String clusterCode) {
        if(StringUtils.isNotNull(clusterCode)){
            List<Cluster> theClusterList = clusterMapper.queryByName(clusterCode);
            if(StringUtils.isNotNull(theClusterList)){
                return theClusterList;
            }
        }
        return null;
    }

    @Override
    public int updateCluster(Cluster cluster) {
        int row = clusterMapper.updateCluster(cluster);
        return row>0?row:0;
    }

    @Override
    public List<Cluster> queryAll() {
        return clusterMapper.query();
    }
}
