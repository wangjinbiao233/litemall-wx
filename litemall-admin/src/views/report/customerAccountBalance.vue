<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入会员编号" v-model="listQuery.memberId">
      </el-input>
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入会员名称" v-model="listQuery.username">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter" >查找</el-button>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-download" @click="handleDownload" :loading="downloadLoading" >导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row style="width: 100%">

      <el-table-column align="center" label="会员编号" prop="memberId">
      </el-table-column>

      <el-table-column align="center" label="会员名称" prop="username">
      </el-table-column>

      <el-table-column align="center" label="存储金" prop="rechargeMoney">
      </el-table-column>

      <el-table-column align="center" label="分销金" prop="money">
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
    width: 200px;
    color: #99a9bf;
  }

  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
  }

</style>

<script>
  import {listAccountBanlance} from '@/api/report'
  import waves from '@/directive/waves' // 水波纹指令

  export default {
    directives: {
      waves
    },
    data() {
      return {
        list: [],
        total: 0,
        listLoading: true,
        storeList: [],
        listQuery: {
          page: 1,
          limit: 20,
          memberId: '',
          username: ''
        },
        downloadLoading: false
      }
    },
    created() {
      this.getList()
    },
    methods: {
      getList() {
        this.listLoading = true
        listAccountBanlance(this.listQuery).then(response => {
          this.list = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.list = []
          this.total = 0
          this.listLoading = false
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
      handleDownload() {
        this.downloadLoading = true
        var list = Object.assign({}, this.listQuery)
        list.page = undefined;
        list.limit = undefined;
        listAccountBanlance(list).then(response => {
          let listData = response.data.data.items
          import('@/vendor/Export2Excel').then(excel => {
            const tHeader = ['会员编号', '会员名称', '存储金', '分销金']
            const filterVal = ['memberId', 'username', 'rechargeMoney', 'money']
            excel.export_json_to_excel2(tHeader, listData, filterVal, '用户账户余额')
            this.downloadLoading = false
          })
        }).catch(() => {
        })
      }
    }
  }
</script>
<style>
  .ad-avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .ad-avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .ad-avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }

  .ad-avatar {
    display: block;
  }
</style>
