<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
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
      <el-select v-model="listQuery.optType" clearable placeholder="请选择佣金状态" style="top: -4px;">
        <el-option
          v-for="item in profitStatusList"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-date-picker
        v-model="listQuery.beginDate"
        type="date"
        value-format="yyyy-MM-dd"
        format="yyyy-MM-dd"
        placeholder="开始日期" style="width: 200px;top: -4px;">
      </el-date-picker>
      <el-date-picker
        v-model="listQuery.endDate"
        type="date"
        value-format="yyyy-MM-dd"
        format="yyyy-MM-dd"
        placeholder="结束日期" style="width: 200px;top: -4px;">
      </el-date-picker>
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入分销商" v-model="listQuery.distributionName">
       </el-input>
      <el-input clearable class="filter-item" style="width: 200px;margin-top: 7px;" placeholder="请输入分销商标签" v-model="listQuery.distributionLabelNames">
      </el-input>
       <el-select v-model="listQuery.orderStatus" clearable placeholder="请选择订单状态" style="">
              <el-option
                v-for="item in orderStatusList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter" style="margin-top: 7px;">查找</el-button>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-download" @click="handleDownload" :loading="downloadLoading" style="margin-top: 7px;">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row style="width: 100%">


      <el-table-column align="center" width="150" label="订单号" prop="orderSn">
      </el-table-column>

      <el-table-column align="center" width="150" label="分销商" prop="distributionName">
      </el-table-column>

      <el-table-column align="center" width="150" label="分销商标签" prop="distributionLabelNames">
      </el-table-column>

      <el-table-column align="center" width="150" label="佣金状态" prop="operationTypeName">
      </el-table-column>

      <el-table-column align="center" width="150" label="佣金" prop="profitMoney">
      </el-table-column>

 <el-table-column align="center" width="150" label="分销比例(百分比)" prop="distributionRate">
      </el-table-column>

       <el-table-column align="center" width="150" label="分销等级" prop="distributeClass">
            </el-table-column>

      <el-table-column align="center" width="100" label="日期" prop="orderDate">
      </el-table-column>

      <el-table-column align="center" width="120" label="订单状态" prop="orderStatusName">
      </el-table-column>

      <el-table-column align="center" width="200" label="商品名称" prop="goodsName">
      </el-table-column>

      <el-table-column align="center" width="80" label="数量" prop="goodsNumber">
      </el-table-column>

      <el-table-column align="center" width="80" label="单价" prop="unitPrice">
      </el-table-column>

      <el-table-column align="center" width="80" label="金额" prop="totalPrices">
      </el-table-column>

      <el-table-column align="center" width="80" label="疗程数" prop="treatmentNum">
      </el-table-column>

      <el-table-column align="center" width="100" label="商品归属" prop="goodsFlagName">
      </el-table-column>

      <el-table-column align="center" width="80" label="疗程总数" prop="treatmentNumCount">
      </el-table-column>

      <el-table-column align="center" width="150" label="会员编号" prop="memberId">
      </el-table-column>

      <el-table-column align="center" width="150" label="会员名称" prop="username">
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
  import {listStore} from '@/api/reserve'
  import {distributionReportList} from '@/api/distribution'
  import waves from '@/directive/waves' // 水波纹指令

  export default {
    name: 'distributionReport',
    directives: {
      waves
    },
    data() {
      return {
        list: [],
        total: 0,
        listLoading: true,
        storeList: [],
        orderStatusList: [
          //{label: '未付款', value: 101},
          //{label: '已取消', value: 102},
          {label: '已付款', value: 201},
          {label: '已退款', value: 202},
          {label: '已发货', value: 301},
          {label: '部分发货', value: 302},
          {label: '退款中', value: 303},
          {label: '已退款', value: 304},
          {label: '已收货', value: 401},
          {label: '已收货(系统)', value: 402},
          {label: '部分收货', value: 403},
          {label: '已完成', value: 501}
        ],
        profitStatusList: [
                  {label: '订单佣金', value: 1},
                  {label: '提取', value: 2},
                  {label: '充值', value: 3},
                  {label: '消费', value: 4},
                  {label: '退款', value: 5},
                  {label: '提现失败', value: 6}
                ],
        listQuery: {
          page: 1,
          limit: 20,
          storeId: '',
          memberId: '',
          username: '',
          goodsFlag: '',
          goodsName: '',
          orderStatus: '',
          beginDate: '',
          endDate: '',
          optType:''
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
        distributionReportList(this.listQuery).then(response => {
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
        distributionReportList(list).then(response => {
                  let listData = response.data.data.items
                  import('@/vendor/Export2Excel').then(excel => {
                            const tHeader = ['订单号', '分销商', '分销商标签', '佣金状态', '佣金','分销比例(百分比)','分销等级', '日期', '订单状态', '商品名称', '数量', '单价', '金额', '疗程数', '商品归属', '疗程总数', '会员编号', '会员名称', '订单金额', '券抵扣', '实付金额']
                            const filterVal = ['orderSn', 'distributionName', 'distributionLabelNames', 'operationTypeName', 'profitMoney','distributionRate','distributeClass', 'orderDate', 'orderStatusName', 'goodsName', 'goodsNumber', 'unitPrice', 'totalPrices', 'treatmentNum', 'goodsFlagName', 'treatmentNumCount', 'memberId', 'username', 'orderPrice',
                             'couponPrice', 'actualPrice']
                            excel.export_json_to_excel2(tHeader,listData, filterVal, '分销报表')
                             this.downloadLoading = false;
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
