<template>
  <div>
    <el-row :gutter="20">
      <!--yaml文件数据-->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
          <el-form-item label="Yaml名称" prop="yamlName">
            <el-input
              v-model="queryParams.yamlName"
              placeholder="请输入Yaml文件名称"
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
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['application:yamlTemplate:remove']"
            >删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="info"
              plain
              icon="el-icon-upload2"
              size="mini"
              @click="handleImport"
              v-hasPermi="['application:yamlTemplate:import']"
            >导入</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="yamlTemplateList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="Yaml编号" align="center" key="yamlTemplateId" prop="yamlTemplateId" v-if="columns[0].visible" />
          <el-table-column label="Yaml名称" align="center" key="yamlName" prop="yamlName" v-if="columns[1].visible" :show-overflow-tooltip="true" />
          <el-table-column label="状态" align="center" key="status" prop="status" v-if="columns[2].visible"></el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns[3].visible" width="160">
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
                v-hasPermi="['application:yamlTemplate:remove']"
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

    <!-- yaml文件上传对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".yaml, .yml"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的Yaml文件
          </div>
          <span>仅允许导入yml,yaml格式文件。</span>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listYaml, getYaml, getYamlById, delYaml, addYaml, updateYaml } from "@/api/application/yamlTemplate";

export default {
  name: "yamlTemplate",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      yamlTemplateList: null,
      title: "",
      yamlOptions: undefined,
      yamlName: undefined,
      open: false,
      dateRange: [],
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      upload: {
        open: false,
        title: "",
        isUploading: false,
        updateSupport: 0,
        headers: {},
        url: process.env.VUE_APP_BASE_API + "/yaml/upload"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        yamlName: undefined,
        status: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `Yaml编号`, visible: true },
        { key: 1, label: `Yaml名称`, visible: true },
        { key: 2, label: `状态`, visible: true },
        { key: 3, label: `创建时间`, visible: true }
      ],
      // 表单校验
      rules: {
        yamlName: [
          { required: true, message: "Yaml名称不能为空", trigger: "blur" },
          { min: 2, max: 20, message: 'Yaml名称长度必须介于 2 和 20 之间', trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listYaml(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.yamlTemplateList = response.rows;
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
      this.queryParams.yamlTemplateId = data.id;
      this.handleQuery();
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        yamlTemplateId: undefined,
        yamlName: undefined,
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
      this.ids = selection.map(item => item.yamlTemplateId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    handleDelete(row) {
      const yamlName = row.yamlName;
      this.$modal.confirm('是否确认删除名为"' + yamlName + '"的数据项？').then(function() {
        return delYaml(yamlName);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleImport() {
      this.upload.title = "上传Yaml文件";
      this.upload.open = true;
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    }
  }
};
</script>