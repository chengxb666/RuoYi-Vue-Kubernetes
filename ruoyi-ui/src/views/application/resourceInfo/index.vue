<template>
  <div>
    <el-row :gutter="20">
      <!--资源数据-->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
          <el-form-item label="资源名称" prop="resourceName">
            <el-input
              v-model="queryParams.resourceName"
              placeholder="请输入资源名称"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="dateRange"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-table v-loading="loading" :data="resourceInfoList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="资源编号" align="center" key="resourceId" prop="resourceId" v-if="columns[0].visible" />
          <el-table-column label="资源名称" align="center" key="resourceName" prop="resourceName" v-if="columns[1].visible" :show-overflow-tooltip="true" />
          <el-table-column label="资源种类" align="center" key="resourceKind" prop="resourceKind" v-if="columns[2].visible"></el-table-column>
          <el-table-column label="资源名称空间" align="center" key="namespaceCode" prop="namespaceCode" v-if="columns[3].visible"></el-table-column>
          <el-table-column label="Yaml文件名称" align="center" key="yamlName" prop="yamlName" v-if="columns[4].visible"></el-table-column>
          <el-table-column label="状态" align="center" key="status" prop="status" v-if="columns[5].visible"></el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns[6].visible" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { listResourceInfo, delResourceInfo } from "@/api/application/resourceInfo";
import { createResourceCluster } from "@/api/application/resourceCluster";

export default {
  name: "resourceInfo",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      resourceInfoList: null,
      title: "",
      resourceInfoOptions: undefined,
      resourceName: undefined,
      open: false,
      dateRange: [],
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        resourceName: undefined,
        status: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `资源编号`, visible: true },
        { key: 1, label: `资源名称`, visible: true },
        { key: 2, label: `资源种类`, visible: true },
        { key: 3, label: `资源名称空间`, visible: true },
        { key: 4, label: `Yaml文件名称`, visible: true },
        { key: 5, label: `状态`, visible: true },
        { key: 6, label: `创建时间`, visible: true }
      ]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listResourceInfo(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.resourceInfoList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    handleNodeClick(data) {
      this.queryParams.resourceId = data.id;
      this.handleQuery();
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        resourceId: undefined,
        resourceName: undefined,
        status: "",
        remark: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.resourceId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    handleCommit(row) {
      const resourceName = row.resourceName;
      this.$modal.confirm('是否确认部署名为"' + resourceName + '"的数据项？').then(function() {
        return createResourceCluster(row);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("部署成功");
      }).catch(() => {});
    },
    handleDelete(row) {
      const resourceName = row.resourceName;
      this.$modal.confirm('是否确认删除名为"' + resourceName + '"的数据项？').then(function() {
        return delResourceInfo(resourceName);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>