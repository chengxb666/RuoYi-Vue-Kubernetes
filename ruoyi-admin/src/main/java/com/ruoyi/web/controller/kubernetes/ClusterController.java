package com.ruoyi.web.controller.kubernetes;

import com.ruoyi.kubernetes.domain.Cluster;
import com.ruoyi.kubernetes.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cluster")
public class ClusterController {

    @Autowired
    private ClusterService clusterService;

    @GetMapping
    public List<Cluster> queryAll(){
        return  clusterService.queryAll();
    }

    @GetMapping("/id/{id}")
    public Cluster queryClusterById(@PathVariable("id") int id){
        return clusterService.queryByid(id);
    }

    @GetMapping("/name/{name}")
    public List<Cluster> queryClusterByName(@PathVariable("name") String name){
        return clusterService.queryByName(name);
    }

    @PostMapping
    public int createCluster(@RequestBody Cluster cluster){
        return clusterService.addCluster(cluster);
    }

    @PutMapping
    public int updateCluster(@RequestBody Cluster cluster){
        return clusterService.updateCluster(cluster);
    }

    @DeleteMapping("/{id}")
    public int removeCluster(@PathVariable("id") int id){
        return clusterService.deleteCluster(id);
    }

}

