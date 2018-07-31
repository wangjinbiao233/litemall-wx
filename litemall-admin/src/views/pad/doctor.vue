<template>
  <div class="pad-doctor">
    <el-row type="flex" class="row-bg">
      <template v-for="reinfo in list">
        <el-col :span="7" class="client-box">
          <div class="client-info">
            <div class="client-portrait-box">
              <img class="client-portrait" v-bind:src="reinfo.avatar" alt="">
              <span class="client-portrait-state">
                <span class="client-portrait-yuan load"></span>
              </span>
            </div>
            <div class="client-msg-box">
              <p class="client-msg-name">{{reinfo.username}}</p>
              <p class="client-msg-serial"></p>
              <p class="client-msg-serial"></p>
            </div>
            <el-button v-if="reinfo.doctorId !=null "  class="client-btn" type="primary">已分配</el-button>
            <el-button v-else  @click="getCosmelist(reinfo.id)" class="client-btn" type="primary">分配</el-button>            
          </div>
          <p class="client-content"><span class="client-content-title">预约项目：</span> {{reinfo.goodsName}}</p>
          <p class="client-content">
            <span class="client-content-title">预约时间：</span>{{reinfo.reserveTime}}             
          </p>
          <p class="client-content"><span class="client-content-title">订单编号：</span> {{reinfo.orderSn}}</p>
        </el-col>
      </template>
    </el-row>
    
    <!--
    <div class="pad-state">
      <span class="pad-state-box"> <span class="pad-state-yuan load"></span> 进行中</span>
      <span class="pad-state-box"> <span class="pad-state-yuan await"></span> 等待中</span>
      <span class="pad-state-box"> <span class="pad-state-yuan complete"></span> 已完成</span>
      <span class="pad-state-box pad-state-msg"> 当前进行<span class="warn">第九位</span>，共<span class="warn">20位</span>顾客 </span>
    </div>
    -->

    <el-dialog
      title="请选择美容师"
      :visible.sync="dialogVisible"
      width="30%">
      <div class="doctor-List">
        <div v-bind:key="i" v-for="(item, i) in coslist" class="doctor-box" @click="selectCosme(i,item.id)" >
          <img class="doctor-avatar" v-bind:src="item.avatar" alt="">
          <p class="doctor-name">{{item.username}}</p>
          <span v-show="i == choiceCos" class="doctor-state" >
            <i class="el-icon-success doctor-icon"></i>
          </span>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveReserCosme">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { reservationStoreData, cosmeStoreData, saveReserCosme } from '@/api/padindex'

export default {
  data() {
    return {
      list: undefined,
      coslist: undefined,
      listQuery: {
        storeId: undefined,
        time: undefined
      },
      dialogVisible: false,
      choiceCos: 0,
      reserid: undefined,
      cosmeid: undefined,
      saveForm:{
        reserveId: undefined,
        doctorId: undefined
      }
    }
  },
  watch: {
    dateTimes(curVal,oldVal) {
      console.log("-- doctor.vue =="+curVal,oldVal);
      this.listQuery.time = curVal
      this.getList()
    },
    storeId: function(value){
      console.log("-- doctor.vue =="+value);
      this.listQuery.storeId =  value
      this.getList()
    },
  }, 
  computed: {
    dateTimes: function(){
      return this.$store.state.user.dateTimes
    },
    storeId:function(){
      return this.$store.state.user.storeId      
    }
  }, 
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listQuery.storeId=this.storeId
      this.listQuery.time = this.dateTimes
      this.listLoading = true
      reservationStoreData(this.listQuery).then(response => {
        this.list = response.data.data.reserveDetail
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.listLoading = false
      })
    },

    getCosmelist(reid){
      //debugger
      this.reserid = reid;
      this.dialogVisible=true
      this.listQuery.storeId=this.$store.state.user.storeId;
      this.listLoading = true
      cosmeStoreData(this.listQuery).then(response => {
        this.coslist = response.data.data.userList
        this.listLoading = false
        this.selectCosme(0,response.data.data.userList[0].id);
      }).catch(() => {
        this.coslist = []
        this.listLoading = false
      })

    },

    selectCosme(index,cosid){
      this.choiceCos = index;
      this.cosmeid= cosid;
    },

    saveReserCosme(){
      this.dialogVisible = false
      this.saveForm.reserveId = this.reserid  //顾客预约信息
      this.saveForm.doctorId = this.cosmeid  //美容师id
      saveReserCosme(this.saveForm).then(response => {
        this.dialogVisible = false
        const res = response.data.data.res
        if(res=='ok'){
          this.$notify({
            title: '成功',
            message: '分配成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        }else{
          this.$notify({
            title: '失败',
            message: '分配失败',
            type: 'error',
            duration: 2000
          })
        }        
      }).catch(() => {
        this.$notify({
          title: '失败',
          message: '分配失败',
          type: 'error',
          duration: 2000
        })
      })
    }

  }

}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .pad-doctor{
    padding: 20px 30px;
    margin-bottom: 60px;
  }
  .row-bg{
    flex-wrap: wrap;
  }
  .client-box{
    padding: 30px 8px;
    border-radius: 4px;
    background-color: #fff;
    border: 1px solid #bcbcbc;
    box-shadow: 0 2px 10px 0 #dcdcdc;
    margin: 10px;
    p{
      margin: 0;
    }
    .client-portrait-box{
      display: inline-block;
      position: relative
    }
    .client-portrait{
      height: 65px;
      width: 65px;
      border-radius: 50%;
      margin-right: 15px;
    }
    .client-portrait-state{
      display: inline-block;
      width: 20px;
      height: 20px;
      background-color: #fff;
      position: absolute;
      right: 8px;
      top: 25px;
      border-radius: 50%;
      line-height: 20px;
      text-align: center;
    }
    .client-portrait-yuan{
      width: 10px;
      height: 10px;
      display: inline-block;
      border-radius: 50%;
    }
    .client-msg-box{
      display: inline-block;
      margin-bottom: 10px;
      vertical-align: top;
    }
    .client-msg-name{
      font-size: 18px;
      color: #333;
      font-weight: bold;
      padding-bottom: 10px;
    }
    .client-msg-serial{
      padding-bottom: 5px;
      font-size: 14px;
      color: #929292;
    }
    .client-btn{
      background-color: #8ac2d6;
      border: 1px solid #8ac2d6;
      padding: 8px;
      border-radius: 2px;
      font-size: 12px;
      position: absolute;
      right: 0;
      top: 0px;
    }
    .client-info{
      padding-bottom: 30px;
      position: relative;
    }
    .client-content{
      padding-bottom: 20px;
      font-size: 14px;
      color: #444;
    }
    .client-content:last-child{
      padding: 0;
    }
    .client-content-title{
      color: #929292;
    }
  }
  .pad-state{
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: #fff;
    padding: 20px 30px;
    .pad-state-box{
      color: #929292;
      font-size: 15px;
      margin-right: 20px;
    }
    .pad-state-yuan{
      display: inline-block;
      height: 10px;
      width: 10px;
      border-radius: 50%;
      margin-right: 10px;
    }
    .warn{
      color: #ffae4a;
    }
    .pad-state-msg{
      margin-left: 50px;
      color: #8ac2d6;
    }
  }
  .load{
    background-color: #50c14e;
  }
  .await{
    background-color: #f65177;
  }
  .complete{
    background-color: #929292;
  }
  .client-box-title{
    color: #929292;
    font-size: 13px;
    padding-left: 15px;
  }
  .client-box-item{
    background-color: #fff;
    padding: 2px 15px;
    position: relative;
    cursor: pointer;
  }
  .client-box-item:hover{
    background-color: #eff0f4;
  }
  .client-box-head{
    height: 36px;
    width: 36px;
    display: inline-block;
    border-radius: 50%;
    color: #fff;
    text-align: center;
    line-height: 30px;
    border: 3px solid #fff;
    font-size: 16px;
  }
  .client-box-head.red{
    background-color: #eb3a57;
  }
  .client-box-head.blue{
    background-color: #2064f5;
  }
  .client-box-head.orange{
    background-color: #ffb93f;
  }
  .client-box-text{
    font-size: 15px;
    color: #333;
    margin-left: 10px;
    display: inline-block;
  }
  .el-popper{
    padding: 0;
  }
  .client-box-icon{
    color: #8ac2d6;
    font-size: 20px;
    position: absolute;
    right: 15px;
    top: 10px
  }
  .el-dropdown-link{
    cursor: pointer;
  }
  .doctor-List{
    .doctor-box{
      display: inline-block;
      cursor: pointer;
      width: 60px;
      padding: 5px 0;
      text-align: center;
      position: relative;
    }
    .doctor-name{
      margin: 0;
      font-size: 12px;
      color: #666;
    }
    .doctor-avatar{
      height: 40px;
      width: 40px;
      border-radius: 50%;
    }
    .doctor-state{
      position: absolute;
      top: 0px;
      right: 3px;
    }
    .doctor-icon{
      color: #50c14e;
    }
  }
</style>
