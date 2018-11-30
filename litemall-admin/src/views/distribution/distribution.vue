<template xmlns:c="http://www.w3.org/1999/html">
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入申请人" v-model="listQuery.createUserName">
      </el-input>

      <el-select v-model="listQuery.distributionType" placeholder="请选择分销类型" style = "top: -4px;">
        <el-option value="">请选择</el-option>
        <el-option
          v-for="item in distributionTypeList"
          :key="item.value"
          :label="item.name"
          :value="item.value">
        </el-option>
      </el-select>


      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-download" @click="handleDownload" :loading="downloadLoading">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column align="center" min-width="100px" label="申请人" prop="nickName">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="申请日期" prop="createTimeDisp">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="分销类型" prop="distributionType">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="附件" prop="">
        <template slot-scope="scope">
              <a v-for="item in scope.row.picUrls" :href="item" >查看附件</a>
              <a v-for="item in scope.row.picUrls"  :href="item"  :download="item"  >下载附件</a>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="审批状态" prop="auditStatus"  :formatter="showAuditStatus">
      </el-table-column>

      <el-table-column align="center" label="操作" width="300" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="success" class="filter-item" size="mini" v-if="scope.row.auditStatus  == 0" @click="handleUpdate(scope.row)">通过</el-button>
          <el-button type="warning" class="filter-item" size="mini" v-if="scope.row.auditStatus  == 0" @click="handleReject(scope.row)">驳回</el-button>

          <router-link v-if="scope.row.auditStatus  == 1" ref='tag' :to="{path:'/distribution/labelmanage',query: {id: scope.row.createUserId}}">
            <el-button class="filter-item" type="primary" size="mini" style="width: inherit;">标签管理</el-button>
          </router-link>
          <router-link ref='tag' :to="{path:'/user/userDetail',query: {id: scope.row.createUserId}}">
            <el-button class="filter-item" type="primary" size="mini" style="width: inherit;">我的分销</el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="驳回" :visible.sync="rejectDialog">
      <el-form ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>

        <el-form-item label="驳回理由" prop="remark">
          <el-input v-model="dataForm.remark"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="rejectDialog = false">取消</el-button>
        <el-button type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.page"
        :page-sizes="[10,20,30,50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 130px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }

  a:link {
    color:blue;
  }

</style>

<script>
import { listDistribution, selectDistributionTypeList,updateDistribution } from '@/api/distribution'
import waves from '@/directive/waves' // 水波纹指令

export default {
  name: 'reserve',
  directives: {
    waves
  },
  data() {
    return {
      list: undefined,
      distributionTypeList:undefined,
      total: undefined,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        sort: '+id'
      },
      dataForm: {
        id: undefined,
        createUserId: undefined,
        auditStatus:undefined,
        remark:undefined
      },
      downloadLoading: false,
      rejectDialog:false
    }
  },
  created() {
    this.getList();
    this.selectDistributionTypeList();
  },
  methods: {
    getList() {
      this.listLoading = true
      listDistribution(this.listQuery).then(response => {
        this.list = response.data.data.items
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },

    selectDistributionTypeList(){
      selectDistributionTypeList({
        groupCode: 'distribution_type'
      }).then(response => {
        this.distributionTypeList = response.data.data

      }).catch(() => {
        this.distributionTypeList = []
      })
    },

    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.listQuery.limit = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.getList()
    },

    showAuditStatus(data){
      if(data.auditStatus == 0){
        return "未审批"
      }
      if(data.auditStatus == 1){
        return "审批通过"
      }

      if(data.auditStatus == 2){
        return "审批驳回"
      }
    },
    handleUpdate(row) {
      this.dataForm.id = row.id
      this.dataForm.createUserId = row.createUserId
      this.dataForm.auditStatus = 1
      updateDistribution(this.dataForm).then(response => {
        this.getList()
        this.$notify({
          title: '成功',
          message: '审批成功',
          type: 'success',
          duration: 2000
        })
      })
    },


    resetForm(row) {
      this.dataForm.id = row.id
      this.dataForm.createUserId = row.createUserId
      this.dataForm.auditStatus = 2
    },

    handleReject(row){
      this.resetForm(row)
      this.rejectDialog = true
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateDistribution(this.dataForm).then(response => {
            this.getList()
            this.rejectDialog = false
            this.$notify({
              title: '成功',
              message: '审批成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },

    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['申请人', '申请时间', '分销类型']
        const filterVal = ['createUserName', 'createTimeDisp', 'distributionType']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '分销申请信息')
        this.downloadLoading = false
      })
    }
  }
}
</script>
