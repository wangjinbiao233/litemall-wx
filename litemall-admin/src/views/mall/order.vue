<template xmlns:c="http://www.w3.org/1999/html">
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入客户名称" v-model="listQuery.username">
      </el-input>
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入订单编号" v-model="listQuery.orderSn">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-download" @click="handleDownload" :loading="downloadLoading">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="订单编号">
              <span>{{ props.row.orderSn }}</span>
            </el-form-item>

            <el-form-item label="订单状态">
              <span>{{ props.row.orderStatusDisp }}</span>
            </el-form-item>

            <el-form-item label="订单金额">
              <span>{{ props.row.orderPrice }}</span>
            </el-form-item>

            <el-form-item label="实际金额" >
              <span>{{ props.row.actualPrice }}</span>
            </el-form-item>

            <el-form-item label="自取门店" v-show="props.row.getStoreId != null" >
              <span>{{ props.row.storeName }}</span>
            </el-form-item>

            <el-form-item label="自取时间" v-show="props.row.getStoreId != null" >
              <span>{{ props.row.confirmTimeDisp }}</span>
            </el-form-item>

            <el-form-item label="收货人名称" v-show="props.row.getStoreId == null" >
              <span>{{ props.row.consignee }}</span>
            </el-form-item>

            <el-form-item label="收货人手机号" v-show="props.row.getStoreId == null">
              <span>{{ props.row.mobile }}</span>
            </el-form-item>

            <el-form-item label="收货人地址" v-show="props.row.getStoreId == null">
              <span>{{ props.row.address }}</span>
            </el-form-item>

            <el-form-item label="商品总费用">
              <span>{{ props.row.goodsPrice }}</span>
            </el-form-item>

            <el-form-item label="配送费用" v-show="props.row.getStoreId == null">
              <span>{{ props.row.freightPrice }}</span>
            </el-form-item>

            <el-form-item label="优惠券减免">
              <span>{{ props.row.couponPrice }}</span>
            </el-form-item>

            <el-form-item label="用户积分减免">
              <span>{{ props.row.integralPrice }}</span>
            </el-form-item>

            <el-form-item label="微信付款编号">
              <span>{{ props.row.payId }}</span>
            </el-form-item>

            <el-form-item label="支付状态">
              <span>{{ props.row.payStatus }}</span>
            </el-form-item>

            <el-form-item label="微信付款时间" >
              <span>{{ props.row.payTimeDisp }}</span>
            </el-form-item>

            <el-form-item label="发货编号">
              <span>{{ props.row.shipSn }}</span>
            </el-form-item>

            <el-form-item label="发货快递公司" v-show="props.row.getStoreId == null">
              <span>{{ props.row.shipChannel }}</span>
            </el-form-item>

            <el-form-item label="发货时间" v-show="props.row.getStoreId == null">
              <span>{{ props.row.shipStartTimeDisp }}</span>
            </el-form-item>

            <!--<el-form-item label="发货结束时间" >-->
              <!--<span>{{ props.row.shipEndTimeDisp }}</span>-->
            <!--</el-form-item>-->

            <!--<el-form-item label="用户确认收货时间">-->
              <!--<span>{{ props.row.confirmTimeDisp }}</span>-->
            <!--</el-form-item>-->

            <el-table size="small" :data="props.row.litemallOrderGoodsList"  border fit highlight-current-row>
                <el-table-column align="center" width="100px" label="商品编号" prop="goodsSn" sortable>
                </el-table-column>

                <el-table-column align="center" min-width="100px" label="商品名称" prop="goodsName">
                </el-table-column>

                <el-table-column align="center" min-width="100px" label="商品数量" prop="number">
                </el-table-column>

                <el-table-column align="center" min-width="100px" label="商品价格" prop="retailPrice">
                </el-table-column>

                <el-table-column align="center" min-width="100px" label="商品规格" prop="goodsSpecificationValues">
                </el-table-column>

                <el-table-column align="center" min-width="100px" label="" prop="flag">
                <template slot-scope="scope">
                  <el-button v-if="scope.row.flag  == 2" type="primary" size="mini" @click="showReserveDetail(scope.row)">预约详情</el-button>
                  <el-button v-if="scope.row.flag  == 1 && scope.row.isReturn == 'true'" type="primary" size="mini" @click="handleReturn(scope.row)">退货</el-button>
                  <el-span v-else></el-span>
                </template>
                </el-table-column>

            </el-table>

          </el-form>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="会员编号" prop="memberId">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="客户名称" prop="username">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="订单编号" prop="orderSn">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="订单状态" prop="orderStatusDisp">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="下单时间" prop="addTimeDisp">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="发货时间" prop="shipStartTimeDisp">
      </el-table-column>

      <!--<el-table-column align="center" min-width="100px" label="是否删除" prop="isDelete">-->
        <!--<template slot-scope="scope">-->
          <!--<el-tag :type="scope.row.isDelete ? 'success' : 'error' ">{{scope.row.isDelete ? '未删除' : '已删除'}}</el-tag>-->
        <!--</template>-->
      <!--</el-table-column>-->

      <el-table-column align="center" min-width="100px" label="订单金额" prop="orderPrice">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="实际金额" prop="actualPrice">
      </el-table-column>

      <el-table-column align="center" label="操作" width="250" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary"  v-if="scope.row.isShip == 'true' && scope.row.getStoreId == null"  @click="handleSend(scope.row)">发货</el-button>
          <el-button type="primary"  v-if="scope.row.isShip == 'true' && scope.row.getStoreId != null"  @click="handleSend(scope.row)">自提确认</el-button>
          <el-button type="primary"  v-if="scope.row.isCancel == 'true'" @click="handleCancel(scope.row)">取消订单</el-button>
          <el-button type="primary"  v-if="scope.row.isReturn == 'true'"  @click="handleReturn(scope.row)">退货</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.page"
        :page-sizes="[10,20,30,50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 发货对话框 -->
    <el-dialog title="发货" :visible.sync="sendDialogFormVisible">
      <el-form :rules="rules" ref="dataForm" :model="dataForm"  label-position="left" inline class="demo-table-expand1">

          <el-form-item label="快递公司" prop="shipChannel">
            <el-select v-model="dataForm.shipChannel" placeholder="请选择">
              <el-option
                v-for="item in expressList"
                :key="item.expressSn"
                :label="item.expressName"
                :value="item.expressSn">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="快递编号" prop="shipSn">
            <el-input v-model="dataForm.shipSn"></el-input>
          </el-form-item>
        <el-form-item label="快递发货时间" prop="shipStartTime">
          <el-date-picker v-model="dataForm.shipStartTime" type="datetime"  formatter="moment" placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="sendDialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="sendData">确定</el-button>
      </div>
    </el-dialog>

    <!-- 收货对话框 -->
    <el-dialog title="收货" :visible.sync="recvDialogFormVisible">
      <el-form ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>
       <el-form-item label="快递公司" prop="shipChannel">
          <el-input disabled v-model="dataForm.shipChannel"></el-input>
        </el-form-item>
        <el-form-item label="快递编号" prop="shipSn">
          <el-input disabled v-model="dataForm.shipSn"></el-input>
        </el-form-item>
        <el-form-item label="快递发货时间" prop="shipStartTime">
          <el-date-picker disabled v-model="dataForm.shipStartTime" type="datetime" placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="快递收货时间" prop="shipEndTime">
          <el-date-picker v-model="dataForm.shipEndTime" type="datetime" placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="recvDialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="recvData">确定</el-button>
      </div>
    </el-dialog>

    <!-- 预约详情对话框 -->
    <el-dialog title="预约详情" :visible.sync="reserveDetailDialogFormVisible">

        <el-table size="small" :data="reserveList" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
          <el-table-column align="center" min-width="100px" label="预约人" prop="username">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="预约日期" prop="reserveDate">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="预约时间" prop="reserveTime">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="预约人手机号" prop="mobile">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="门店名称" prop="storeName">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="项目名称" prop="goodsName">
          </el-table-column>

        </el-table>

  </el-dialog>


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


  .demo-table-expand1 {
    font-size: 0;
  }
  .demo-table-expand1 label {
    width: 100px;
    color: #99a9bf;
  }
  .demo-table-expand1 .el-form-item {
    margin-right: 0;
    margin-bottom:20px;
    width: 50%;
  }

</style>

<script>
import { listOrder, listExpress, reserveDetail, updateOrder, updateOrderStatus,updateOrderStore, orderReturn } from '@/api/order'
import waves from '@/directive/waves' // 水波纹指令

export default {
  name: 'Order',
  directives: {
    waves
  },
  data() {
    return {
      labelPosition: 'right',
      list: undefined,
      expressList: undefined,
      total: undefined,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        username: undefined,
        orderSn: undefined,
        sort: '+id'
      },
      detailListQuery: {
        orderGoodsId: undefined
      },
      dataForm: {
        id: undefined,
        shipChannel: undefined,
        shipSn: undefined,
        shipStartTime: undefined,
        shipEndTime: undefined
      },
      sendDialogFormVisible: false,
      recvDialogFormVisible: false,
      reserveDetailDialogFormVisible: false,
      downloadLoading: false,
      rules: {
        shipChannel: [{required: true, message: '快递公司不能为空', trigger: 'blur'}],
        shipSn: [{required: true, message: '快递单号不能为空', trigger: 'blur'}],
        shipStartTime: [{required: true, message: '发货时间不能为空', trigger: 'blur'}]
      }

    }
  },
  created() {
    this.getList()
    this.getExpressList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listOrder(this.listQuery).then(response => {
        this.list = response.data.data.items
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },
    getExpressList() {
      listExpress(this.listQuery).then(response => {
        this.expressList = response.data.data.items
      }).catch(() => {
        this.expressList = []
      })
    },

    getReserveDetail() {
      reserveDetail(this.detailListQuery).then(response => {
        if (response.data.data.item != null) {
          this.reserveList = response.data.data.item
        }
      }).catch(() => {
        this.reserveList = null
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
      this.dataForm.shipChannel = row.shipChannel
      this.dataForm.shipSn = row.shipSn
      this.dataForm.shipStartTime = row.shipStartTime
      this.dataForm.shipEndTime = row.shipEndTime
    },

    handleSend(row) {
      debugger
      var getStoreId=row.getStoreId;
      if(getStoreId){
        //非空
          this.sendDataStore(row.id);
      }else{
             this.resetForm(row)
             this.sendDialogFormVisible = true
             this.$nextTick(() => {
               this.$refs['dataForm'].clearValidate()
             })
      }

    },
    // 取消订单
    handleCancel(row) {
      this.dataForm.id = row.id
      updateOrderStatus(this.dataForm).then(response => {
        this.getList()
        this.$notify({
          title: '成功',
          message: '取消成功',
          type: 'success',
          duration: 2000
        })
      }).catch(() => {
        this.$notify({
          title: '失败',
          message: '不能取消订单',
          type: 'error',
          duration: 2000
        })
      })
    },
    // 退货
    handleReturn(row) {
      this.dataForm.id = row.id
      orderReturn(this.dataForm).then(response => {
        if (response.data.errmsg != null  && response.data.errmsg != '成功') {
          this.$notify({
            title: '失败',
            message: response.data.errmsg,
            type: 'error',
            duration: 2000
          })
        } else {
          this.$notify({
            title: '成功',
            message: '退货成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        }
      })
    },
    //店面自取的发货
    sendDataStore(id){
      debugger
                var data= {id:id};
                updateOrderStore(data).then(response => {
                  if (response.data.errmsg != null && response.data.errmsg != '成功') {
                    this.$notify({
                      title: '失败',
                      message: response.data.errmsg,
                      type: 'error',
                      duration: 2000
                    })
                  } else {
                    this.$notify({
                      title: '成功',
                      message: '更新成功',
                      type: 'success',
                      duration: 2000
                    })
                    this.sendDialogFormVisible = false
                    this.getList()
                  }
                }).catch((err) => {
                console.log(err);
                  this.$notify({
                    title: '失败',
                    message: '更新失败',
                    type: 'error',
                    duration: 2000
                  })
                })

    },
    sendData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateOrder(this.dataForm).then(response => {
            if (response.data.errmsg != null && response.data.errmsg != '成功') {
              this.$notify({
                title: '失败',
                message: response.data.errmsg,
                type: 'error',
                duration: 2000
              })
            } else {
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
              this.sendDialogFormVisible = false
              this.getList()
            }
          }).catch(() => {
            this.$notify({
              title: '失败',
              message: '更新失败',
              type: 'error',
              duration: 2000
            })
          })
        }
      })
    },
    handleRecv(row) {
      this.resetForm(row)
      this.recvDialogFormVisible = true
    },
    recvData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateOrder(this.dataForm).then(response => {
            const updatedOrder = response.data.data
            for (const v of this.list) {
              if (v.id === updatedOrder.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, updatedOrder)
                break
              }
            }
            this.recvDialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          }).catch(() => {
            this.$notify({
              title: '失败',
              message: '更新失败',
              type: 'error',
              duration: 2000
            })
          })
        }
      })
    },
    showReserveDetail(row) {
      this.reserveDetailDialogFormVisible = true
      this.detailListQuery.orderGoodsId = row.id
      this.getReserveDetail()
    },

    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['订单编号','会员编号', '客户名称', '商品名称', '订单状态', '收货人', '收货联系电话', '收货地址']
        const filterVal = ['orderSn','memberId', 'username', 'orderGoodsName', 'orderStatusDisp', 'consignee', 'mobile', 'address']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '订单信息')
        this.downloadLoading = false
      })
    },

  }
}
</script>
