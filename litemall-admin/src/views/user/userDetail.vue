<template>
  <div class="userDetail">
    <div class="user-info">
      <div class="user-face">
        <img :src="userDataForm.avatar" style="width: 50px;height: 50px;border-radius: 50%"/>
      </div>
      <div class="user-msg">
        <p>
          <span class="user-msg-item">{{userDataForm.username}}</span>
          <span class="user-msg-item">{{userDataForm.memberId}}</span>
          <span class="user-msg-item">{{userDataForm.userLevel}}</span>
          <span class="user-msg-item">{{userDataForm.mobile}}</span>
        </p>
      </div>
    </div>

    <el-tabs type="border-card" @tab-click="handleClick">

      <el-tab-pane label="基本信息" name="0">
        <el-form :rules="user_rules" :inline="true" ref="userDataForm" :model="userDataForm" status-icon label-position="left" label-width="100px" style='margin-left:50px;'>


          <el-form-item width="100px" label="会员编号" prop="memberId">
            <span>{{ userDataForm.memberId }}</span>
          </el-form-item>
          <el-form-item label="注册时间"  prop="addTime">
            <span>{{ userDataForm.addTime }}</span>
          </el-form-item>

          <el-form-item label="用户名" prop="username">
            <el-input v-model="userDataForm.username"></el-input>
          </el-form-item>
          <el-form-item width="100px" label="用户昵称" prop="nickname">
            <span>{{ userDataForm.nickname }}</span>
          </el-form-item>
          <el-form-item label="手机号码" prop="mobile">
            <el-input v-model="userDataForm.mobile"></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-select v-model="userDataForm.gender" placeholder="请选择">
              <el-option label="未知" value="未知">
              </el-option>
              <el-option label="男" value="男">
              </el-option>
              <el-option label="女" value="女">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="用户等级" prop="userLevel">
            <el-select v-model="userDataForm.userLevel" placeholder="请选择">
              <el-option label="普通用户" value="普通用户">
              </el-option>
              <el-option label="VIP用户" value="VIP用户">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="userDataForm.status" placeholder="请选择">
              <el-option label="可用" value="可用">
              </el-option>
              <el-option label="禁用" value="禁用">
              </el-option>
            </el-select>
          </el-form-item>
          <!--
          <el-form-item label="积分" prop="point">
            <el-input v-bind:disabled="userDataForm.point" v-model="userDataForm.point"></el-input>
          </el-form-item>
          -->
          <el-form-item label="我的存储金"  prop="money">
            <el-input v-bind:disabled="userDataForm.rechargeMoney" v-model="userDataForm.rechargeMoney"></el-input>
            <el-button type="primary" @click="moneyDetail">余额详情</el-button>
          </el-form-item>
        </el-form>

        <el-button type="primary" @click="updateUserData">保存</el-button>


      </el-tab-pane>

      <el-tab-pane label="订单列表" name="1" >

        <el-table size="small" :data="orderList" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
          <el-table-column align="center" min-width="100px" label="订单编号" prop="orderSn">
          </el-table-column>

          <el-table-column align="center" min-width="80px" label="订单状态" prop="orderStatusDisp">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="下单时间" prop="addTimeDisp">
          </el-table-column>

          <el-table-column align="center" min-width="90px" label="订单费用" prop="orderPrice">
          </el-table-column>

          <el-table-column align="center" min-width="90px" label="实际费用" prop="actualPrice">
          </el-table-column>
        </el-table>

      </el-tab-pane>
      <el-tab-pane label="预约列表" name="2" >
        <el-table size="small" :data="reserveList" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
            <el-table-column align="center" min-width="100px" label="门店名称" prop="storeName">
            </el-table-column>

            <el-table-column align="center" min-width="100px" label="项目名称" prop="goodsName">
            </el-table-column>


            <el-table-column align="center" min-width="100px" label="预约日期" prop="reserveDate">
            </el-table-column>

            <el-table-column align="center" min-width="100px" label="预约状态" prop="reserveStatus" :formatter="judgeReserve" >
            </el-table-column>

            <el-table-column align="center" min-width="100px" label="预约时间" prop="reserveTime">
            </el-table-column>
        </el-table>

      </el-tab-pane>

      <el-tab-pane label="治疗记录" name="3" >
        <el-table size="small" :data="storeServiceList" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>

          <el-table-column align="center" min-width="100px" label="门店名称" prop="storeName">
          </el-table-column>
          <el-table-column align="center" min-width="100px" label="项目名称" prop="goodsName">
          </el-table-column>
          <el-table-column align="center" min-width="100px" label="治疗医生" prop="doctorName">
          </el-table-column>
          <el-table-column align="center" min-width="100px" label="服务开始时间" prop="startTime">
          </el-table-column>
          <el-table-column align="center" min-width="100px" label="服务结束时间" prop="endTime">
          </el-table-column>

        </el-table>
      </el-tab-pane>


      <!--<el-tab-pane label="会员收藏" name="" >
        <el-table size="small" :data="collectList" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
          <el-table-column align="center" width="100px" label="收藏ID" prop="id" sortable>
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="商品名称" prop="goodName">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="添加时间" prop="addTime">
          </el-table-column>
        </el-table>
      </el-tab-pane>-->

      <el-tab-pane label="会员足迹" name="4" >

        <el-table size="small" :data="footprintList" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
          <el-table-column align="center" width="100px" label="足迹ID" prop="id" sortable>
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="商品名称" prop="goodName">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="添加时间" prop="addTime">
          </el-table-column>

        </el-table>

      </el-tab-pane>

      <el-tab-pane label="搜索历史" name="5" >

        <el-table size="small" :data="historyList" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
          <el-table-column align="center" width="100px" label="搜索ID"  prop="id" sortable>
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="关键字" prop="keyword">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="添加时间" prop="addTime">
          </el-table-column>

        </el-table>

      </el-tab-pane>

      <el-tab-pane label="购物车" name="6" >

        <el-table size="small" :data="cartList" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
          <el-table-column align="center" min-width="100px" label="购物车ID" prop="id" sortable>
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="商品编码" prop="goodsSn">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="商品名称" prop="goodsName">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="货品ID" prop="productId">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="货品价格" prop="retailPrice">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="货品数量" prop="number">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="添加时间" prop="addTime">
          </el-table-column>

        </el-table>

      </el-tab-pane>

      <el-tab-pane label="优惠券" name="7" >

        <el-table size="small" :data="discountList" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
          <el-table-column align="center" width="200px" label="优惠券key" prop="key" sortable>
          </el-table-column>

          <el-table-column align="center" min-width="90px" label="名称" prop="discountName">
          </el-table-column>

          <el-table-column align="center" min-width="50px" label="是否已使用" prop="isUse" :formatter="judgeDiscount" >
          </el-table-column>

          <el-table-column align="center" min-width="60px" label="开始时间" prop="startTime">
          </el-table-column>

          <el-table-column align="center" min-width="60px" label="结束时间" prop="endTime">
          </el-table-column>

          <el-table-column align="center" label="操作" width="80" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button type="primary" v-if="scope.row.isUse==1" size="mini" @click="discountDetails(scope.row.id)">详情</el-button>
            </template>
          </el-table-column>

        </el-table>

      </el-tab-pane>

      <el-tab-pane label="分销" name="8" >

        <!--<el-card class="box-card">
          我的上级：

        </el-card>-->

        <el-collapse size="small" v-model="activeNames" accordion @change="handleChange">
          <el-collapse-item title="我的上级：" name="1">

            <el-table size="small" :data="distributionParent" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
              <el-table-column align="center" min-width="100px" label="头像" prop="avatar">
                <template slot-scope="scope">
                  <img :src="scope.row.avatar" style="width: 50px;height: 50px;border-radius: 50%"/>
                </template>
              </el-table-column>
              <el-table-column align="center" min-width="100px" label="用户名" prop="username">
              </el-table-column>
              <el-table-column align="center" min-width="100px" label="注册时间" prop="addTime">
              </el-table-column>
            </el-table>

          </el-collapse-item>

          <el-collapse-item title="我的下级（一级）：" name="2">

            <el-table size="small" :data="distributionSub1List" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
              <el-table-column align="center" min-width="100px" label="头像" prop="avatar">
                <template slot-scope="scope">
                  <img :src="scope.row.avatar" style="width: 50px;height: 50px;border-radius: 50%"/>
                </template>
              </el-table-column>
              <el-table-column align="center" min-width="100px" label="用户名" prop="username">
              </el-table-column>

              <el-table-column align="center" min-width="100px" label="标签" prop="lebelName">
              </el-table-column>

              <el-table-column align="center" min-width="100px" label="注册时间" prop="add_time">
              </el-table-column>
            </el-table>


          </el-collapse-item>
          <el-collapse-item title="我的下级（二级）：" name="3">

            <el-table size="small" :data="distributionSub2List" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
              <el-table-column align="center" min-width="100px" label="头像" prop="avatar">
                <template slot-scope="scope">
                  <img :src="scope.row.avatar" style="width: 50px;height: 50px;border-radius: 50%"/>
                </template>
              </el-table-column>
              <el-table-column align="center" min-width="100px" label="用户名" prop="username">
              </el-table-column>
              <el-table-column align="center" min-width="100px" label="注册时间" prop="add_time">
              </el-table-column>
            </el-table>


          </el-collapse-item>
        </el-collapse>

      </el-tab-pane>

      <el-tab-pane label="充值记录" name="9" >
        <div class="filter-container">
          <el-button type="primary" @click="recharge()">充值</el-button>
        </div>

        <el-table size="small" :data="TransactionRecordList" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
          <el-table-column align="center" width="100px" label="交易ID" prop="id" sortable>
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="交易金额" prop="rechargeMoney">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="交易类型" prop="remark">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="充值方式" prop="rechargeType"  :formatter="rechargeTypeFormatter" >
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="交易时间" prop="operationTime">
          </el-table-column>

        </el-table>

      </el-tab-pane>


      <el-tab-pane label="收货地址" name="10" >
        <el-table size="small" :data="addressList" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
          <el-table-column align="center" width="100px" label="地址ID" prop="id" sortable>
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="收货人名称" prop="name">
          </el-table-column>

          <el-table-column align="center" min-width="100px" label="手机号码" prop="mobile">
          </el-table-column>

          <el-table-column align="center" min-width="300px" label="地址" prop="address">
            <template slot-scope="scope">
              {{scope.row.province + scope.row.city + scope.row.area + scope.row.address}}
            </template>
          </el-table-column>

          <el-table-column align="center" min-width="80px" label="默认" prop="isDefault">
            <template slot-scope="scope">
              {{scope.row.isDefault ? '是' : '否'}}
            </template>
          </el-table-column>
        </el-table>

      </el-tab-pane>


      <!-- 分页 -->

      <div v-if="visible">
        <div class="pagination-container" v-if="flag">
          <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.page"
                         :page-sizes="[10,20,30,50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </div>

        <div class="pagination-container" v-else>
          <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryList.page"
                         :page-sizes="[10,20,30,50]" :page-size="queryList.limit" layout="total, sizes, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </div>
      </div>

      <!--<el-tab-pane label="快递公司" name="3" >快递公司</el-tab-pane>-->

    </el-tabs>


    <!-- 添加或修改对话框 -->
    <el-dialog title="充值" :visible.sync="dialogFormVisible">
      <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>
        <el-form-item label="充值金额" prop="money">
          <el-input v-model="dataForm.money" type="number"></el-input>
        </el-form-item>

        <el-form-item label="充值方式" prop="rechargeType">
          <el-select v-model="dataForm.rechargeType" placeholder="请选择">
            <el-option label="银联" value="1">
            </el-option>
            <el-option label="支付宝" value="2">
            </el-option>
            <el-option label="微信" value="3">
            </el-option>
          </el-select>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="createData">确定</el-button>
      </div>
    </el-dialog>

    <!-- 优惠券详情对话框 -->
    <el-dialog title="优惠券详情" :visible.sync="discountOrderDialogVisible">
      <el-form :rules="rules" ref="discountOrderDetailForm" :model="discountOrderDetailForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>

        <el-form-item width="100px" label="订单编号" prop="orderSn">
          <span>{{ discountOrderDetailForm.orderSn }}</span>
        </el-form-item>
        <el-form-item label="订单状态"  prop="orderStatusDisp">
          <span>{{ discountOrderDetailForm.orderStatusDisp }}</span>
        </el-form-item>
        <el-form-item label="下单时间"  prop="addTimeDisp">
          <el-input v-model="discountOrderDetailForm.addTimeDisp"></el-input>
        </el-form-item>

        <el-form-item label="订单金额" prop="orderPrice">
          <el-input v-model="discountOrderDetailForm.orderPrice"></el-input>
        </el-form-item>
        <el-form-item label="实际金额" prop="actualPrice">
          <el-input v-model="discountOrderDetailForm.actualPrice"></el-input>
        </el-form-item>

        <el-form-item label="收货人姓名" prop="consignee">
          <el-input v-model="discountOrderDetailForm.consignee"></el-input>
        </el-form-item>
        <el-form-item label="收货人手机号" prop="mobile">
          <el-input v-model="discountOrderDetailForm.mobile"></el-input>
        </el-form-item>

        <el-form-item label="收货人地址" prop="address">
          <el-input v-model="discountOrderDetailForm.orderPrice"></el-input>
        </el-form-item>
        <el-form-item label="商品总费用" prop="goodsPrice">
          <el-input v-model="discountOrderDetailForm.goodsPrice"></el-input>
        </el-form-item>

        <el-form-item label="配送费用" prop="freightPrice">
          <el-input v-model="discountOrderDetailForm.freightPrice"></el-input>
        </el-form-item>
        <el-form-item label="优惠券减免" prop="couponPrice">
          <el-input v-model="discountOrderDetailForm.couponPrice"></el-input>
        </el-form-item>
        <el-form-item label="发货编号" prop="shipSn">
          <el-input v-model="discountOrderDetailForm.shipSn"></el-input>
        </el-form-item>
        <el-form-item label="发货时间" prop="shipStartTimeDisp">
          <el-input v-model="discountOrderDetailForm.shipStartTimeDisp"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="discountOrderDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { getOrderList, getReserveList, getDiscountList, selectParentUserInfo, selectSubUserInfo,
  selectSubSubUserInfo, getTransactionRecordList, createRecharge, updateUser, readUser, getDiscountOrderDetail } from '@/api/user'
import { listAddress } from '@/api/address'
import { listCollect } from '@/api/collect'
import { listFootprint } from '@/api/footprint'
import { listHistory } from '@/api/history'
import { listCart } from '@/api/cart'
import { listReserve } from '@/api/reserve'

export default {
  name: 'userDetail',
  data() {
    return {
      activeNames: ['1'],
      id: undefined,
      orderList: null,
      reserveList: null,
      addressList: null,
      collectList: null,
      footprintList: null,
      historyList: null,
      cartList: null,
      discountList: null,
      storeServiceList: null,
      distributionParent: null,
      distributionSub1List: null,
      distributionSub2List: null,
      TransactionRecordList: null,
      dialogFormVisible: false,
      total: null,
      listLoading: false,
      queryType: 'baseInfo',
      activeTab: undefined,
      flag: undefined,
      visible: undefined,
      listQuery: {
        id: undefined,
        page: 1,
        limit: 20
      },
      queryList: {
        userId: undefined,
        reserveStatus: undefined,
        page: 1,
        limit: 20
      },
      dataForm: {
        money: undefined,
        rechargeType:undefined,
        userId: undefined
      },
      userDataForm: {
        id: undefined,
        username: '',
        mobile: '',
        gender: '男',
        userLevel: '普通用户',
        point: '',
        status: '可用'
      },
      rules: {
        money: [{ required: true, message: '充值金额不能为空', trigger: 'blur' }]
      },
      user_rules: {
        username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }]
      },
      discountOrderDetailForm: {
        id: undefined,
        orderSn: undefined,
        orderStatusDisp: undefined,
        orderPrice: undefined,
        actualPrice: undefined,
        consignee: undefined,
        mobile: undefined,
        address: undefined,
        goodsPrice: undefined,
        freightPrice: undefined,
        couponPrice: undefined,
        integralPrice: undefined,
        shipSn: undefined,
        shipStartTimeDisp: undefined,
        addTimeDisp: undefined
      },
      discountOrderDialogVisible: false,
      discountOrderQuery: {
        couponId: undefined
      }
    }
  },
  methods: {
    judgeReserve(data){
        if(data.reserveStatus==0){
          return '预定未使用'
        }else if(data.reserveStatus==1){
          return '预定已使用'
        }else{
          return '预定取消'
        }
    },
    judgeDiscount(data){
      return data.isUse=='0' ? '未使用' : '已使用'
    },

    rechargeTypeFormatter(data){
      if(data.rechargeType==1){
        return '银联'
      }else if(data.rechargeType==2){
        return '支付宝'
      }else{
        return '微信'
      }
    },
    handleChange(val) {
      console.log(val)
      this.getUserInfoList(val)
    },
    getList() {
      if (this.queryType === 'baseInfo') {
        readUser({ 'id': this.id }).then(response => {
          this.userDataForm = response.data.data.items
          this.$nextTick(() => {
            this.$refs['userDataForm'].clearValidate()
          })
        })
      } else if (this.queryType === 'order') {
        this.listLoading = true
        getOrderList(this.listQuery).then(response => {
          this.orderList = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.orderList = []
          this.total = 0
          this.listLoading = false
        })
      } else if (this.queryType === 'reserve') {
        this.listLoading = true
        getReserveList(this.listQuery).then(response => {
          this.reserveList = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.reserveList = []
          this.total = 0
          this.listLoading = false
        })
      } else if (this.queryType === 'address') {
        this.listLoading = true
        listAddress(this.queryList).then(response => {
          this.addressList = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.addressList = []
          this.total = 0
          this.listLoading = false
        })
      } else if (this.queryType === 'collect') {
        this.listLoading = true
        listCollect(this.queryList).then(response => {
          this.collectList = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.collectList = []
          this.total = 0
          this.listLoading = false
        })
      } else if (this.queryType === 'footprint') {
        this.listLoading = true
        listFootprint(this.queryList).then(response => {
          this.footprintList = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.footprintList = []
          this.total = 0
          this.listLoading = false
        })
      } else if (this.queryType === 'history') {
        this.listLoading = true
        listHistory(this.queryList).then(response => {
          this.historyList = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.historyList = []
          this.total = 0
          this.listLoading = false
        })
      } else if (this.queryType === 'cart') {
        this.listLoading = true
        listCart(this.queryList).then(response => {
          this.cartList = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.cartList = []
          this.total = 0
          this.listLoading = false
        })
      } else if (this.queryType === 'discount') {
        this.listLoading = true
        getDiscountList(this.listQuery).then(response => {
          this.discountList = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.discountList = []
          this.total = 0
          this.listLoading = false
        })
      } else if (this.queryType === 'distribution') {
        this.listLoading = true
        this.getUserInfoList('1')
        this.listLoading = false
      } else if (this.queryType === 'recharge') {
        this.listLoading = true
        getTransactionRecordList({ 'id': this.id }).then(response => {
          this.TransactionRecordList = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.TransactionRecordList = []
          this.total = 0
          this.listLoading = false
        })
      } else if(this.queryType === 'storeService'){
        this.listLoading = true
        this.queryList.reserveStatus = 1
        listReserve(this.queryList).then(response => {
          this.storeServiceList = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false

        }).catch(() => {
          this.storeServiceList = []
          this.total = 0
          this.listLoading = false
        })
      }
    },
    getUserInfoList(val) {
      if (val === '1') {
        selectParentUserInfo({ 'id': this.id }).then(response => {
          this.distributionParent = response.data.data.items
        }).catch(() => {
          this.distributionParent = []
        })
      } else if (val === '2') {
        selectSubUserInfo({ 'id': this.id }).then(response => {
          this.distributionSub1List = response.data.data.items
        }).catch(() => {
          this.distributionSub1List = []
        })
      } else if (val === '3') {
        selectSubSubUserInfo({ 'id': this.id }).then(response => {
          this.distributionSub2List = response.data.data.items
        }).catch(() => {
          this.distributionSub2List = []
        })
      }
    },
    //充值
    recharge() {
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createRecharge(this.dataForm).then(response => {
            if (response.data.data.state === 1) {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '充值成功',
                type: 'success',
                duration: 2000
              })
            }
            this.getList()
          })
        }
      })
    },
    updateUserData() {
      this.$refs['userDataForm'].validate((valid) => {
        if (valid) {
          updateUser(this.userDataForm).then(() => {
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
    handleSizeChange(val) {
      this.listQuery.limit = val
      this.queryList.limit = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.queryList.page = val
      this.getList()
    },
    getWaterDetails() {
      if (this.activeTab === '0') {
        console.log('baseInfo')
        this.visible = false
        this.flag = true
        this.queryType = 'baseInfo'
        this.getList()
      } else if (this.activeTab === '1') {
        console.log('handleUserOrder')
        this.visible = true
        this.flag = true
        this.queryType = 'order'
        this.getList()
      } else if (this.activeTab === '2') {
        console.log('handleUserReserve')
        this.visible = true
        this.flag = true
        this.queryType = 'reserve'
        this.getList()
      } else if (this.activeTab === '3') {
        this.visible = true
        this.flag = false
        this.queryType = 'storeService'
        this.getList()
      } else if (this.activeTab === '4') {
        this.visible = true
        this.flag = false
        this.queryType = 'footprint'
        this.getList()
      } else if (this.activeTab === '5') {
        this.visible = true
        this.flag = false
        this.queryType = 'history'
        this.getList()
      } else if (this.activeTab === '6') {
        this.visible = true
        this.flag = false
        this.queryType = 'cart'
        this.getList()
      } else if (this.activeTab === '7') {
        this.visible = true
        this.flag = true
        this.queryType = 'discount'
        this.getList()
      } else if (this.activeTab === '8') {
        this.visible = false
        this.flag = true
        this.queryType = 'distribution'
        this.getList()
      } else if (this.activeTab === '9') {
        this.visible = false
        this.flag = true
        this.queryType = 'recharge'
        this.getList()
      } else if (this.activeTab === '10') {
        this.visible = true
        this.flag = false
        this.queryType = 'address'
        this.getList()
      } else {
        console.log('this.activeTab !!!')
      }
    },
    //点击选项卡
    handleClick(tab, e) {
      this.activeTab = tab.index
      this.getWaterDetails()
    },
    moneyDetail(){
      this.activeTab = '9'
      let eleNav =  document.getElementsByClassName('el-tabs__nav')[0].children;
      eleNav[9].click();
      this.getWaterDetails()
    },
    //优惠券详情
    discountDetails(couponid){
      this.discountOrderQuery.couponId = couponid
      getDiscountOrderDetail(this.discountOrderQuery).then(response => {
        console.log(response.data.data.items)
        if(response.data.data.items!=null){
          this.discountOrderDetailForm = response.data.data.items
          this.discountOrderDialogVisible = true
          this.$nextTick(() => {
            this.$refs['discountOrderDetailForm'].clearValidate()
          })
        }else{
          this.$message.success('没有相关数据')
        }
      }).catch(() => {
        this.$message.error('失败')
      })
    }

  },
  watch: {
    '$route'(to, from) {
      var id = this.$route.query.id
      if (id) {
        this.id = id
        this.listQuery.id = id
        this.queryList.userId = id
        this.dataForm.userId = id
        this.getList()
        readUser({ 'id': id }).then(response => {
          this.userDataForm = response.data.data.items
          this.$nextTick(() => {
            this.$refs['userDataForm'].clearValidate()
          })
        })
      }
    }
  },
  created() {
    var id = this.$route.query.id
    if (id) {
      this.id = id
      this.listQuery.id = id
      this.queryList.userId = id
      this.dataForm.userId = id
      this.getList()
    }
  }
}
</script>

<style>
  .userDetail{
    padding: 20px 30px;
  }
  .userDetail .el-input{
    width: 200px;
  }
  .userDetail .el-form-item{
    width: 45%;
  }
  .userDetail .user-info{
    display: flex;
    align-items: center;

  }
  .userDetail .user-msg{
    padding-left: 10px;
  }
  .userDetail .user-msg-item{
    font-size: 14px;
    color: #666;
    padding-right: 10px;
    min-width: 100px;
    display: inline-block;
  }
</style>

