<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-select v-model="listQuery.storeId" clearable placeholder="请选择门店" style="top: -4px;">
        <el-option
          v-for="item in storeList"
          :key="item.id"
          :label="item.storeName"
          :value="item.id">
        </el-option>
      </el-select>
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入会员编号" v-model="listQuery.memberId">
      </el-input>
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入会员名称" v-model="listQuery.username">
      </el-input>
      <el-select v-model="listQuery.goodsFlag" clearable placeholder="商品归属" style="top: -4px;">
        <el-option label="实物商品" :key="'1'" :value="'1'">
        </el-option>
        <el-option label="服务类商品" :key="'2'" :value="'2'">
        </el-option>
      </el-select>
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入商品名称" v-model="listQuery.goodsName">
      </el-input>
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入服务顾问名称" v-model="listQuery.doctorName">
      </el-input>
      <el-date-picker
        v-model="listQuery.beginDate"
        type="date"
        value-format="yyyy-MM-dd"
        format="yyyy-MM-dd"
        placeholder="开始日期" style="width: 200px;top:-3px;">
      </el-date-picker>
      <el-date-picker
        v-model="listQuery.endDate"
        type="date"
        value-format="yyyy-MM-dd"
        format="yyyy-MM-dd"
        placeholder="结束日期" style="width: 200px;top:-3px;">
      </el-date-picker>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-download" @click="handleDownload" :loading="downloadLoading">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row style="width: 100%">

      <el-table-column align="center" width="150" label="订单号" prop="orderSn">
      </el-table-column>

      <el-table-column align="center" width="100" label="订单日期" prop="orderDate">
      </el-table-column>

      <el-table-column align="center" width="100" label="执行日期" prop="executeDate">
      </el-table-column>

      <el-table-column align="center" width="200" label="门店名称" prop="storeName">
      </el-table-column>
      
      <el-table-column align="center" width="200" label="商品名称" prop="goodsName">
      </el-table-column>

      <el-table-column align="center" width="80" label="疗程总数" prop="treatmentNumCount">
      </el-table-column>

      <el-table-column align="center" width="100" label="商品归属" prop="goodsFlagName">
      </el-table-column>

      <el-table-column align="center" width="150" label="会员编号" prop="memberId">
      </el-table-column>

      <el-table-column align="center" width="150" label="会员名称" prop="username">
      </el-table-column>

      <el-table-column align="center" width="150" label="服务顾问" prop="doctorName">
      </el-table-column>

      <el-table-column align="center" width="80" label="订单金额" prop="orderPrice">
      </el-table-column>

      <el-table-column align="center" width="80" label="券抵扣" prop="couponPrice">
      </el-table-column>

      <el-table-column align="center" width="80" label="实付金额" prop="actualPrice">
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
  import { listStore } from '@/api/reserve'
  import { listSaleExecute } from '@/api/report'
  import waves from '@/directive/waves' // 水波纹指令

  export default {
    name: 'saleExcute',
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
          storeId: '',
          memberId: '',
          username: '',
          goodsFlag: '',
          goodsName: '',
          doctorName: '',
          beginDate: '',
          endDate: ''
        },
        downloadLoading: false
      }
    },
    created() {
      this.queryStoreList()
      this.getList()
    },
    methods: {
      // 获取门店列表
      queryStoreList() {
        listStore({}).then(response => {
          this.storeList = response.data.data.allStoreList
        }).catch(() => {
          this.storeList = []
        })
      },
      getList() {
        this.listLoading = true
        listSaleExecute(this.listQuery).then(response => {
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
        import('@/vendor/Export2Excel').then(excel => {
          const tHeader = ['门店名称', '订单日期', '订单号', '执行日期', '商品名称', '疗程总数', '商品归属', '会员编号', '会员名称', '服务顾问', '订单金额', '券抵扣', '实付金额']
          const filterVal = ['storeName', 'orderDate', 'orderSn', 'executeDate', 'goodsName', 'treatmentNumCount', 'goodsFlagName', 'memberId', 'username', 'doctorName', 'orderPrice', 'couponPrice', 'actualPrice']
          excel.export_json_to_excel2(tHeader, this.list, filterVal, '销售执行统计')
          this.downloadLoading = false
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
