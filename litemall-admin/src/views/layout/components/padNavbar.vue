<template>
  <el-menu class="navbar padNavbar" mode="horizontal">
    <div class="dayState">
      <el-select v-model="storeId" placeholder="请选择"  @change="selectStore(this)">
        <el-option
          v-for="item in storeOptions"
          :key="item.id"
          :label="item.storeName"
          :value="item.id">
        </el-option>          
      </el-select>
      <span class="dayState-box dayState-time" v-if="isManagers=='yes'">
        门店管理员，欢迎您的登录
      </span>
      <span class="dayState-box dayState-time" v-else>{{name}}美疗师，欢迎您的登录</span>
    </div>
    <div class="dayState">
      <i class="el-icon-arrow-left dayState-icon" @click="toPreDay"></i>      
      <span class="dayState-date">
        <el-date-picker v-model="dateTimes" @change="changeDateTimes" type="date" placeholder="选择日期" value-format="yyyy-MM-dd">
        </el-date-picker>
      </span>
      <i class="el-icon-arrow-right dayState-icon" @click="toNextDay"></i>
      <div class="search-input">
        <!--
        <el-input
          class="searchInput" :class="input23.length > 0 ? 'focusInput' : ''"
          placeholder="请输入手机号方便查找"
          v-model="input23">
          <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
        -->
      </div>
    </div>
    <div class="right-menu">
      <span class="avatar-name">{{name}}</span> 
      <el-dropdown class="avatar-container right-menu-item" trigger="click">
        <div class="avatar-wrapper">
          <img class="user-avatar" :src="avatar+'?imageView2/1/w/80/h/80'">
          <i class="el-icon-arrow-down"></i>
        </div>
        <el-dropdown-menu slot="dropdown">
          <a href="#/">
            <el-dropdown-item>
              主页
            </el-dropdown-item>
          </a>
          <el-dropdown-item divided>
            <span @click="logout" style="display:block;">退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </el-menu>
</template>

<script>
import { reservationTotal  } from '@/api/padindex'

export default {
  components: {
    
  },
  data() {
    return {      
      input23: '',
      today:['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],      
      currentYear: 1970,   // 年份222
      currentMonth: 1,  // 月份
      currentDay: 1,    // 日期
      currentWeek: 1,    // 星期
      dateTimes:'',
      listQuery: {    
        userId: undefined,    
        storeId: undefined,
        dateTimes: undefined
      },
      currentStore: undefined
    }
  },
  created () {
    this.initData()   
    //alert(this.$store.state.user.storeId); 
  },
  computed: {
    storeId:{
      get:function(){
        return this.$store.state.user.storeId
      },
      set: function (newValue) {
          this.$store.state.user.storeId = newValue;
      }
    },
    storename:function(){
      return this.$store.state.user.storename
    },
    isManagers:function(){
      return this.$store.state.user.isManagers
    },
    name:function(){
      return this.$store.state.user.name
    },
    avatar:function(){
      return this.$store.state.user.avatar
    },
    storeOptions:function(){
      return this.$store.state.user.storeOptions
    }
  },  
  methods: {
    //格式化日期
    formatDate (year, month, day) {        
        const y = year
        let m = month
        if (m < 10) m = `0${m}`
        let d = day
        if (d < 10) d = `0${d}`
        return `${y}-${m}-${d}`
    },
    commonHandleDay(date){
      this.currentYear = date.getFullYear()       // 当前年份
      this.currentMonth = date.getMonth() + 1    // 当前月份
      this.currentDay = date.getDate()          // 今日日期 几号         
      this.currentWeek = this.today[date.getDay()]   // 今天是星期几       
      this.dateTimes = this.formatDate(this.currentYear, this.currentMonth, this.currentDay)// 今日日期 年-月-日
      this.$store.dispatch('setDateTimes',this.dateTimes)      
    },
    initData(){      
      let date = date = new Date();
      this.commonHandleDay(date);      
    },
    toPreDay(){
      let dataValue = new Date(new Date(this.dateTimes).getTime()-1000 * 60 * 60 * 24);
      this.commonHandleDay(dataValue); 
    },
    toNextDay(){   
      let dataValue = new Date(new Date(this.dateTimes).getTime()+1000 * 60 * 60 * 24);  
      this.commonHandleDay(dataValue); 
    },

    changeDateTimes(){
      this.$store.dispatch('setDateTimes',this.dateTimes)
    },    
    selectStore(obj){
      this.$store.dispatch('changeStore',this.storeId)
    },

    toggleSideBar() {
      this.$store.dispatch('toggleSideBar')
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload()// In order to re-instantiate the vue-router object to avoid bugs
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
  line-height: 50px;
  padding-top: 10px;
  border-radius: 0px !important;
  text-align: center;
  position: relative;
  box-shadow: 0 0 20px 0 #e6e6e7;
  border-bottom: 0px none;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .breadcrumb-container{
    float: left;
  }
  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }
  .right-menu {
    position: absolute;
    right: 0;
    top: 0;
    &:focus{
     outline: none;
    }
    .right-menu-item {
      display: inline-block;
      margin: 0 8px;
    }
    .screenfull {
      height: 20px;
    }
    .avatar-name{
      font-size: 14px;
      color: #333;
      position: relative;
      bottom: 13px;
    }
    .avatar-container {
      height: 50px;
      margin-right: 30px;
      .avatar-wrapper {
        cursor: pointer;
        margin-top: 5px;
        position: relative;
        .user-avatar {
          width: 40px;
          height: 40px;
          border-radius: 50%;
        }
        .el-icon-arrow-down {
          position: relative;
          bottom: 10px;
          left: 3px;
          font-size: 18px;
          color: #93969b;
        }
      }
    }
  }
  .dayState{
    margin: 0 auto;
    position: relative;
    .weather-icon{
      font-size: 30px;
      color: #92c6d9;
    }
    .search-input{
      width: 210px;
      position: absolute;
      right: 30px;
      top: 0;
    }
    .dayState-box{
      padding: 0 20px 0 2px;
    }
    .dayState-degree{
      color: #929292;
      font-size: 18px;
    }
    .dayState-time{
      color: #8ac2d6;
      font-size: 18px;
    }
    .dayState-date{
      font-size: 15px;
      color: #929292;
      padding: 0 20px;
    }
    .dayState-icon{
      font-size: 23px;
      color: #8ac2d6;
      position: relative;
      top: 3px;
      cursor: pointer;
    }
  }
}
</style>
<style>
.padNavbar .el-input--medium .el-input__inner{
  border-radius: 30px;
  height: 30px;
  text-align: center;
  background-color: #f8f8f8;
  color: #bcbcbc;
  font-size: 13px;
}
.padNavbar .el-input--medium.focusInput .el-input__inner{
  background-color: #fff;
  color: #333;
}
</style>

