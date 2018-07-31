<template >
  <div class="pad">
    <el-row class="panel-group" :gutter="40" v-if="isManagers=='yes'">
      <el-input clearable  placeholder=""  v-model="listQuery.storeId" v-show="false">
      </el-input>
      <router-link to="/doctor">
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class='card-panel' @click="handleSetLineChartData('newVisitis')">
            <div class="card-panel-icon-wrapper icon-people">
              <svg-icon icon-class="peoples" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">到店数</div>
              <count-to class="card-panel-num" :startVal="0" :endVal="comeTotal" :duration="2600"></count-to> /
              <count-to class="card-panel-num" :startVal="0" :endVal="userTotal" :duration="2600"></count-to>
            </div>
          </div>
        </el-col>
      </router-link>

      <!--
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('purchases')">
          <div class="card-panel-icon-wrapper icon-money">
            <svg-icon icon-class="message" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">当天预约数</div>
            <count-to class="card-panel-num" :startVal="0" :endVal="productTotal" :duration="3200"></count-to>
          </div>
        </div>
      </el-col>
      -->

      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('shoppings')">
          <div class="card-panel-icon-wrapper icon-shoppingCard">
            <svg-icon icon-class="money" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">当天收入</div>
            <count-to class="card-panel-num" :startVal="0.00" :endVal="orderTotal" :duration="3600" :decimals="2"></count-to>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 美容师登录显示 -->
    <el-row class="panel-group" :gutter="40" v-else>
      <router-link to="/cosmetologist">
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class='card-panel' >
            <div class="card-panel-icon-wrapper icon-people">
              <svg-icon icon-class="peoples" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">预约顾客到店数</div>
              <count-to class="card-panel-num" :startVal="0" :endVal="doctorComeTotal" :duration="2600"></count-to> /
              <count-to class="card-panel-num" :startVal="0" :endVal="doctorUserTotal" :duration="2600"></count-to>
            </div>
          </div>
        </el-col>
      </router-link>
    </el-row>

  </div>
</template>



<script>
import { reservationTotal,doctorReserTotal } from '@/api/padindex'
import CountTo from 'vue-count-to'

export default {
  components: {
    CountTo
  },
  data() {
    return {
      comeTotal:0,
      userTotal: 0,
      orderTotal: 0.00,
      doctorComeTotal: 0,
      doctorUserTotal: 0,
      listQuery: {
        doctorId: undefined,
        storeId: undefined,
        time: undefined
      }
    }
  },
  watch: {
    dateTimes(curVal,oldVal) {
      console.log(curVal,oldVal);
      this.listQuery.time = curVal
      this.init()
    },
    storeId: function(value){
      console.log(value);
      this.listQuery.storeId =  value
      this.init()
    },
  },
  computed: {
    isManagers:function(){
      return this.$store.state.user.isManagers
    },
    dateTimes: function(){
      return this.$store.state.user.dateTimes
    },
    storeId:function(){
      return this.$store.state.user.storeId
    },
    userId:function(){
      return this.$store.state.user.userId
    }
  },
  methods: {
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    },
    init() {
      console.log(" init again ");
      if(this.isManagers=='yes'){//管理员登录
        reservationTotal(this.listQuery).then(response => {
          this.userTotal = response.data.data.reserveCount//预约总数
          this.comeTotal = response.data.data.comeCount;//来的数量
          this.orderTotal = response.data.data.orderIncomeCount;//当天收入
        })
      }else{
        doctorReserTotal(this.listQuery).then(response => {
          this.doctorComeTotal = response.data.data.serviceCount//来的数量
          this.doctorUserTotal = response.data.data.doctorReserveCount;//预约总数
        })
      }

    }
  },
  created() {
    this.listQuery.storeId=this.$store.state.user.storeId;
    this.listQuery.time = this.dateTimes;
    this.listQuery.doctorId = this.userId;
    this.init()
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.pad{
  background-color: #fff;
  padding: 0 30px;
}
.panel-group {
  margin-top: 30px;

  .card-panel-col{
    margin-bottom: 32px;
  }
  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }
      .icon-people {
         background: #40c9c6;
      }
      .icon-message {
        background: #36a3f7;
      }
      .icon-money {
        background: #f4516c;
      }
      .icon-shoppingCard {
        background: #34bfa3
      }
    }
    .icon-people {
      color: #40c9c6;
    }
    .icon-message {
      color: #36a3f7;
    }
    .icon-money {
      color: #f4516c;
    }
    .icon-shoppingCard {
      color: #34bfa3
    }
    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }
    .card-panel-icon {
      float: left;
      font-size: 48px;
    }
    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;
      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }
      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}
</style>
