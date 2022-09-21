package com.ruoyi.web.controller.kubernetes;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.kubernetes.domain.Cluster;
import com.ruoyi.kubernetes.domain.ResourceInfo;
import com.ruoyi.kubernetes.service.ClusterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/cluster")
public class ClusterController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ClusterController.class);

    @Autowired
    private ClusterService clusterService;

    @GetMapping
    public TableDataInfo findAll(){
        startPage();
        List<Cluster> list = clusterService.queryAll();;
        return getDataTable(list);
    }

    @GetMapping("/codes")
    public TableDataInfo queryAll() throws Exception {
        List<String> clusterCodes = clusterService.queryClusterCodes();
        if(clusterCodes.size()>0){
            TableDataInfo rspData = new TableDataInfo();
            rspData.setCode(HttpStatus.SUCCESS);
            rspData.setMsg("查询成功");
            rspData.setRows(clusterCodes);
            rspData.setTotal(clusterCodes.size());
            return rspData;
        }else{
            TableDataInfo rspData = new TableDataInfo();
            rspData.setCode(HttpStatus.NO_CONTENT);
            rspData.setMsg("查询失败");
            rspData.setRows(null);
            rspData.setTotal(0);
            return rspData;
        }
    }

    @GetMapping("/id/{id}")
    public TableDataInfo queryClusterById(@PathVariable("id") int id){
        Cluster cluster = clusterService.queryByid(id);
        if(null != cluster){
            TableDataInfo rspData = new TableDataInfo();
            rspData.setCode(HttpStatus.SUCCESS);
            rspData.setMsg("查询成功");
            List<Cluster> list = new ArrayList<Cluster>();
            list.add(cluster);
            rspData.setRows(list);
            rspData.setTotal(0);
            return rspData;
        }else {
            TableDataInfo rspData = new TableDataInfo();
            rspData.setCode(HttpStatus.NO_CONTENT);
            rspData.setMsg("查询失败");
            rspData.setRows(null);
            rspData.setTotal(0);
            return rspData;
        }
    }

    @GetMapping("/name/{name}")
    public TableDataInfo queryClusterByName(@PathVariable("name") String name){
        Cluster cluster = clusterService.queryByName(name);
        if(null != cluster){
            TableDataInfo rspData = new TableDataInfo();
            rspData.setCode(HttpStatus.SUCCESS);
            rspData.setMsg("查询成功");
            List<Cluster> list = new ArrayList<Cluster>();
            list.add(cluster);
            rspData.setRows(list);
            rspData.setTotal(0);
            return rspData;
        }else {
            TableDataInfo rspData = new TableDataInfo();
            rspData.setCode(HttpStatus.NO_CONTENT);
            rspData.setMsg("查询失败");
            rspData.setRows(null);
            rspData.setTotal(0);
            return rspData;
        }
    }

    @PostMapping
    public AjaxResult createCluster(@RequestBody Cluster cluster){
        return toAjax(clusterService.addCluster(cluster));
    }

    @PutMapping
    public AjaxResult updateCluster(@RequestBody Cluster cluster){
        return toAjax(clusterService.updateCluster(cluster));
    }

    @DeleteMapping("/{name}")
    public AjaxResult removeCluster(@PathVariable("name") String name){
        return toAjax(clusterService.deleteCluster(name));
    }

}

