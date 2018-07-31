<template xmlns:c="http://www.w3.org/1999/html">
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入预约人" v-model="listQuery.username">
      </el-input>

      <el-select v-model="listQuery.storeId" placeholder="请选择门店" style = "top: -4px;">
        <el-option value="">请选择</el-option>
        <el-option
          v-for="item in storeList"
          :key="item.id"
          :label="item.storeName"
          :value="item.id">
        </el-option>
      </el-select>

      <el-date-picker  clearable class="filter-item" v-model="listQuery.startTimeStart" type="date" placeholder="选择服务开始时间起" value-format="yyyy-MM-dd">
      </el-date-picker>
      <el-date-picker clearable class="filter-item" v-model="listQuery.startTimeEnd" type="date" placeholder="选择服务开始时间止" value-format="yyyy-MM-dd">
      </el-date-picker>


      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-download" @click="handleDownload" :loading="downloadLoading">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" min-width="100px" label="门店名称" prop="storeName">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="项目名称" prop="goodsName">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="预约人" prop="username">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="服务顾问" prop="doctorName">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="服务开始时间" prop="startTime">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="服务结束时间" prop="endTime">
      </el-table-column>
    </el-table>

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

</style>

<script>
import { listReserve, updateReserve, cancelReserve, listStore } from '@/api/reserve'
import waves from '@/directive/waves' // 水波纹指令

export default {
  name: 'reserve',
  directives: {
    waves
  },
  data() {
    return {
      list: undefined,
      storeList:undefined,
      total: undefined,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        username: undefined,
        reserveStatus: 1,
        storeId:undefined,
        startTimeStart: undefined,
        startTimeEnd: undefined,
        sort: '+id'
      },
      downloadLoading: false,
      dataForm: {
        id: undefined,
        reserveTime: undefined,
        reserveDate: undefined
      },
      reserveTimeUpdateDialog: false
    }
  },
  created() {
    this.getList()
    this.selectStoreList();
  },
  methods: {
    getList() {
      this.listLoading = true
      listReserve(this.listQuery).then(response => {
        this.list = response.data.data.items
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },

    selectStoreList(){
      listStore({
      }).then(response => {
        this.storeList = response.data.data.allStoreList
      }).catch(() => {
        this.storeList = []
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

    resetForm(row) {
      this.dataForm.id = row.id
      this.dataForm.reserveTime = row.reserveTime
      this.dataForm.reserveDate = row.reserveDate
    },

    handleUpdate(row) {
      this.resetForm(row)
      this.reserveTimeUpdateDialog = true
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateReserve(this.dataForm).then(response => {
            const updateReserve = response.data.data
            for (const v of this.list) {
              if (v.id === updateReserve.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, updateReserve)
                break
              }
            }
            this.getList()
            this.reserveTimeUpdateDialog = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },

    handleCancel(row) {
      cancelReserve(row).then(response => {
        this.getList()
        this.$notify({
          title: '成功',
          message: '取消成功',
          type: 'success',
          duration: 2000
        })
      })
    },

    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['门店名称', '项目名称', '预约人', '服务顾问', '服务开始时间', '服务结束时间']
        const filterVal = ['storeName', 'goodsName', 'username', 'doctorName', 'startTime', 'endTime']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '治疗信息')
        this.downloadLoading = false
      })
    }
  }
}
</script>
