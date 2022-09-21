<template>
  <div>
    <el-row :gutter="20">
      <!--资源数据-->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
          <el-form-item label="资源配置名称" prop="resourceConfigName">
            <el-input
              v-model="queryParams.resourceConfigName"
              placeholder="请输入资源配置名称"
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

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
            >新增</el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="resourceconfiglist" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="资源配置编号" align="center" key="resourceConfigId" prop="resourceConfigId" v-if="columns[0].visible" />
          <el-table-column label="资源配置名称" align="center" key="resourceConfigName" prop="resourceConfigName" v-if="columns[1].visible" :show-overflow-tooltip="true" />
          <el-table-column label="资源名称" align="center" key="resourceName" prop="resourceName" v-if="columns[2].visible"></el-table-column>
          <el-table-column label="资源类型" align="center" key="resourceKind" prop="resourceKind" v-if="columns[3].visible"></el-table-column>
          <el-table-column label="名称空间" align="center" key="namespaceCode" prop="namespaceCode" v-if="columns[4].visible"></el-table-column>
          <el-table-column label="集群名称" align="center" key="clusterCode" prop="clusterCode" v-if="columns[5].visible"></el-table-column>
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

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="资源配置名称" prop="resourceConfigName">
              <el-input v-model="form.resourceConfigName" placeholder="请输入资源配置名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资源名称" prop="resourceName">
              <el-input v-model="form.resourceName" placeholder="请输入资源名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资源种类" prop="resourceKind">
              <el-input v-model="form.resourceKind" placeholder="请输入资源种类" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="名称空间" prop="namespaceCode">
              <el-input v-model="form.namespaceCode" placeholder="请输入名称空间" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="集群名称" prop="clusterCode">
              <el-select v-model="form.clusterCode" placeholder="请选择集群名称" clearable :style="{width: '100%'}">
                <el-option v-for="(item, index) in ClusterCodeOptions" :key="index" :label="item"
                  :value="item" :disabled="item.disabled"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listResourceConfigs,  createResourceConfig, updateResourceConfig, delResourceConfigs } from "@/api/application/resourceConfig";
import { getClusterCodes } from "@/api/infrastruct/cluster";

export default {
  name: "resourceConfig",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      resourceconfiglist: null,
      title: "",
      formData: {
        clusterCode: undefined,
      },
      clusterOptions: undefined,
      resourceConfigName: undefined,
      open: false,
      dateRange: [],
      ClusterCodeOptions: [],
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        clusterCode: undefined,
        status: undefined
      },
      rules: {
        resourceName: [
          { required: true, message: "资源名称不能为空", trigger: "blur" }
        ],
        resourceKind: [
          { required: true, message: "资源类型不能为空", trigger: "blur" }
        ],
        namespaceCode: [
          { required: true, message: "名称空间不能为空", trigger: "blur" }
        ],
        clusterCode: [
          { required: true, message: "集群名称不能为空", trigger: "blur" }
        ]
      },
      // 列信息
      columns: [
        { key: 0, label: `资源配置编号`, visible: true },
        { key: 1, label: `资源配置名称`, visible: true },
        { key: 2, label: `资源名称`, visible: true },
        { key: 3, label: `资源类型`, visible: true },
        { key: 4, label: `名称空间`, visible: true },
        { key: 5, label: `集群名称`, visible: true },
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
      listResourceConfigs(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.resourceconfiglist = response.rows;
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
      this.queryParams.clusterId = data.id;
      this.handleQuery();
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        resourceConfigId: undefined,
        resourceConfigName: undefined,
        resourceName: undefined,
        resourceKind: undefined,
        namespaceCode: undefined,
        clusterCode: undefined,
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
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.resourceConfigId != undefined) {
            updateResourceConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            createResourceConfig(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleAdd(row) {
      this.reset();
      this.open = true;
      getClusterCodes().then(response => {
        this.ClusterCodeOptions = response.rows;
      });
      this.title = "添加资源配置信息";
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.resourceConfigId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    handleDelete(row) {
      const resourceconfigName = row.resourceconfigName;
      this.$modal.confirm('是否确认删除名为"' + resourceconfigName + '"的数据项？').then(function() {
        return delResourceConfigs(resourceconfigName);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>