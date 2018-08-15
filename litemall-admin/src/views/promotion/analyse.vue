<template xmlns:c="http://www.w3.org/1999/html">
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入" v-model="listQuery.username">
      </el-input>

      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-download" @click="handleDownload" :loading="downloadLoading">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" min-width="100px" label="时间" prop="createDate">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="来源" prop="shopName">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="手机号" prop="mobile">
        <template scope="scope">
          <div style="color:red;text-decoration:underline;cursor:pointer;" @click="showDetail(scope.row)">{{ scope.row.mobile }}</div>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="微信昵称" prop="wxNickName">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="头像" prop="icon">
        <template slot-scope="scope">
          <img :src= "scope.row.icon"  width="100px" height="80px"/>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="性别" prop="gender" :formatter="showGender" >
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="出生日期" prop="birthday">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="朝向" prop="faceType"  :formatter="showFaceType" >
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="自然光" prop="zeroZrg">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.zeroZrg" style="max-height: 500px;max-width: 500px"/>
            <img slot="reference" :src= "'http://img.philab.net/'+scope.row.zeroZrg" width="auto" height="90px"/>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="表皮" prop="zeroBp">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.zeroBp" style="max-height: 500px;max-width: 500px"/>
            <img slot="reference" :src= "'http://img.philab.net/'+scope.row.zeroBp" width="auto" height="90px"/>
          </el-popover>
        </template>
      </el-table-column>


      <el-table-column align="center" min-width="100px" label="真皮" prop="zeroZp">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.zeroZp" style="max-height: 500px;max-width: 500px"/>
            <img slot="reference" :src= "'http://img.philab.net/'+scope.row.zeroZp" width="auto" height="90px"/>
          </el-popover>
        </template>
      </el-table-column>


      <el-table-column align="center" min-width="100px" label="荧光" prop="zeroYg">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.zeroYg" style="max-height: 500px;max-width: 500px"/>
            <img slot="reference" :src= "'http://img.philab.net/'+scope.row.zeroYg" width="auto" height="90px"/>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="UV色斑" prop="zeroUv">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.zeroUv" style="max-height: 500px;max-width: 500px"/>
            <img slot="reference" :src= "'http://img.philab.net/'+scope.row.zeroUv" width="auto" height="90px"/>
          </el-popover>
        </template>
      </el-table-column>


      <el-table-column align="center" min-width="100px" label="棕色斑点" prop="zeroBrown">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.zeroBrown" style="max-height: 500px;max-width: 500px"/>
            <img slot="reference" :src= "'http://img.philab.net/'+scope.row.zeroBrown" width="auto" height="90px"/>
          </el-popover>
        </template>  zeroUv
      </el-table-column>


      <el-table-column align="center" min-width="100px" label="红色区块" prop="zeroRed">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.zeroRed" style="max-height: 500px;max-width: 500px"/>
            <img slot="reference" :src= "'http://img.philab.net/'+scope.row.zeroRed" width="auto" height="90px"/>
          </el-popover>
        </template>
      </el-table-column>


      <el-table-column align="center" min-width="100px" label="红色打标" prop="imageRed">
        <template slot-scope="scope">
          <el-popover v-if="scope.row.imageRed != null"
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.imageRed" style="max-height: 500px;max-width: 500px"/>
            <img slot="reference" :src= "'http://img.philab.net/'+scope.row.imageRed" width="auto" height="90px"/>
          </el-popover>
          <span else slot="reference">未打标</span>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="绿色打标" prop="imageGreen">
        <template slot-scope="scope">
          <el-popover v-if="scope.row.imageGreen != null"
                      placement="right"
                      title=""
                      trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.imageGreen" style="max-height: 500px;max-width: 500px"/>
            <img slot="reference" :src= "'http://img.philab.net/'+scope.row.imageGreen" width="auto" height="90px"/>
          </el-popover>
          <span else slot="reference">未打标</span>
        </template>
      </el-table-column>


      <el-table-column align="center" min-width="100px" label="绿色打标" prop="imageBlue">
        <template slot-scope="scope">
          <el-popover v-if="scope.row.imageBlue != null"
                      placement="right"
                      title=""
                      trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.imageBlue" style="max-height: 500px;max-width: 500px"/>
            <img slot="reference" :src= "'http://img.philab.net/'+scope.row.imageBlue" width="auto" height="90px"/>
          </el-popover>
          <span else slot="reference">未打标</span>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="黑色打标" prop="imageBlack">
        <template slot-scope="scope">
          <el-popover v-if="scope.row.imageBlack != null"
                      placement="right"
                      title=""
                      trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.imageBlack" style="max-height: 500px;max-width: 500px"/>
            <img slot="reference" :src= "'http://img.philab.net/'+scope.row.imageBlack" width="auto" height="90px"/>
          </el-popover>
          <span else slot="reference">未打标</span>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="白色打标" prop="imageWhite">
        <template slot-scope="scope">
          <el-popover v-if="scope.row.imageWhite != null"
                      placement="right"
                      title=""
                      trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.imageWhite" style="max-height: 500px;max-width: 500px"/>
            <img slot="reference" :src= "'http://img.philab.net/'+scope.row.imageWhite" width="auto" height="90px"/>
          </el-popover>
          <span else slot="reference">未打标</span>
        </template>
      </el-table-column>


      <el-table-column align="center" min-width="100px" label="灰色打标" prop="imageGray">
        <template slot-scope="scope">
          <el-popover v-if="scope.row.imageGray != null"
                      placement="right"
                      title=""
                      trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.imageGray" style="max-height: 500px;max-width: 500px"/>
            <img slot="reference" :src= "'http://img.philab.net/'+scope.row.imageGray" width="auto" height="90px"/>
          </el-popover>
          <span else slot="reference">未打标</span>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="医生建议" prop="message">

      </el-table-column>


      <el-table-column align="center" min-width="100px" label="毛孔粗大" prop="score0">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.sourceImg0" style="max-height: 500px;max-width: 500px"/>
            <span slot="reference">{{scope.row.score0}}<br>{{scope.row.score0jibai}}</span>

          </el-popover>
        </template>

      </el-table-column>

      <el-table-column align="center" min-width="100px" label="卟啉油脂" prop="score2">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.sourceImg2" style="max-height: 500px;max-width: 500px"/>
            <span slot="reference">{{scope.row.score2}}<br>{{scope.row.score2jibai}}</span>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="肤色白皙(L)" prop="score4">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.sourceImg4" style="max-height: 500px;max-width: 500px"/>
            <span slot="reference">{{scope.row.score4}}<br>{{scope.row.score4jibai}}</span>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="UV色斑" prop="score3">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.sourceImg3" style="max-height: 500px;max-width: 500px"/>
            <span slot="reference">{{scope.row.score3}}<br>{{scope.row.score3jibai}}</span>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="水份保湿" prop="score5">
        <template slot-scope="scope">
            <span>{{scope.row.score5}}<br>{{scope.row.score5jibai}}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="棕色斑点" prop="score6">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.sourceImg5" style="max-height: 500px;max-width: 500px"/>
            <span slot="reference">{{scope.row.score6}}<br>{{scope.row.score6jibai}}</span>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="色素斑点" prop="score9">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.sourceImg8" style="max-height: 500px;max-width: 500px"/>
            <span slot="reference">{{scope.row.score9}}<br>{{scope.row.score9jibai}}</span>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="细纹皱纹" prop="score10">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.sourceImg9" style="max-height: 500px;max-width: 500px"/>
            <span slot="reference">{{scope.row.score10}}<br>{{scope.row.score10jibai}}</span>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="粗糙纹理" prop="score11">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.sourceImg10" style="max-height: 500px;max-width: 500px"/>
            <span slot="reference">{{scope.row.score11}}<br>{{scope.row.score11jibai}}</span>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="红色区块" prop="score12">
        <template slot-scope="scope">
          <el-popover
            placement="right"
            title=""
            trigger="click">
            <img :src="'http://img.philab.net/'+scope.row.sourceImg11" style="max-height: 500px;max-width: 500px"/>
            <span slot="reference">{{scope.row.score12}}<br>{{scope.row.score12jibai}}</span>
          </el-popover>
        </template>
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
import { listAnalyse } from '@/api/analyse'
import waves from '@/directive/waves' // 水波纹指令

export default {
  name: 'analyse',
  directives: {
    waves
  },
  data() {
    return {
      list: undefined,
      detailList: undefined,
      total: undefined,
      listLoading: true,
      detailListLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        username: undefined,
        sort: '+id'
      },
      detailListQuery: {
        page: 1,
        limit: 10,
        detailSort: '+id',
        sort: undefined
      },

      downloadLoading: false,
      detailDialogFormVisible: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listAnalyse(this.listQuery).then(response => {
        this.list = response.data.data.items
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },

    showGender(data){
      if(data.gender == 'M'){
        return "男"
      }
      if(data.gender == 'F'){
        return "女"
      }
    },

    showFaceType(data){
      if(data.faceType == '0'){
        return "正脸"
      }else if(data.faceType == '-1'){
        return "左脸"
      }else{
        return "右脸"
      }
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

    showDetail(row) {
      window.router.push({name: 'analyseDetail', query: {userId: row.userId}})
    },

    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['预约人', '预约日期', '预约时间', '门店名称', '项目名称']
        const filterVal = ['username', 'reserveDate', 'reserveTime', 'storeName', 'goodsName']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '预约信息')
        this.downloadLoading = false
      })
    }
  }
}
</script>
