<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入分店名称" v-model="listQuery.storeName">
      </el-input>
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入分店所在城市" v-model="listQuery.storeCity">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate" icon="el-icon-edit">添加门店</el-button>
      <el-button class="filter-item" type="primary" :loading="downloadLoading" v-waves icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>

   <!--   <el-table-column type="selection" width="55">
      </el-table-column>-->

      <el-table-column align="center" width="50px" label="门店ID" prop="id" sortable v-if='show' >
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="门店名称" prop="storeName">
      </el-table-column>

      <el-table-column align="center" min-width="80px" label="店长" prop="managerName">
      </el-table-column>


      <el-table-column align="center" min-width="80px" label="美疗师" prop="doctor">
      </el-table-column>


      <el-table-column align="center" min-width="80px" label="服务上限" prop="servicePeo">
      </el-table-column>


      <el-table-column prop="storeImg" align="center" label="门店图片" width="180">
        <template slot-scope="scope">
          <img  :src="scope.row.storeImg"  alt="" style="width: 150px;height: 120px">
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="80px" label="所在省" prop="provinceName">
      </el-table-column>


      <el-table-column align="center" min-width="80px" label="所在城市" prop="cityName">
      </el-table-column>

    <!--  <el-table-column align="center" min-width="100px" label="所在城市" prop="storeProvince">
      </el-table-column>-->

      <el-table-column align="center" min-width="120px" label="门店地址" prop="storeAddress">
      </el-table-column>

      <el-table-column align="center" min-width="80px" label="联系方式" prop="storeTel">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="门店坐标" prop="storeCoordinate"  v-if='show'>
      </el-table-column>


      <el-table-column align="center" min-width="90px" label="创建时间" prop="createDateStr"  v-if='show'>
      </el-table-column>

      <el-table-column align="left" label="操作" width="320px" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button type="danger" size="mini"  @click="handleDelete(scope.row)">删除</el-button>
        <!--  <el-button type="primary" size="mini" style="padding-left: 7px"  @click="setStoreManage(scope.row)">设置店长</el-button>-->
          <!--<el-button type="primary" size="mini" style="padding-left: 7px"  @click="setProject(scope.row)">添加项目</el-button>-->
          <el-button type="primary" size="mini" style="padding-left: 6px"  @click="editProject(scope.row)">项目查看</el-button>
<!--          <el-button type="primary" size="mini" style="padding-left: 7px"  @click="setDoctor(scope.row)">设置医生</el-button>
          <el-button type="primary" size="mini" style="padding-left: 6px"  @click="editDoctor(scope.row)">编辑医生</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.page"
                     :page-sizes="[10,20,30,50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <el-tooltip placement="top" content="返回顶部">
      <back-to-top :visibilityHeight="100" ></back-to-top>
    </el-tooltip>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form  :inline="true" :rules="rules"   class="demo-form-inline" ref="dataForm" :model="dataForm" status-icon label-position="left" >
        <el-form-item label="门店名称" prop="storeName">
          <el-input v-model="dataForm.storeName"></el-input>
        </el-form-item>
        <el-form-item label="门店电话" prop="storeTel" >
          <el-input v-model="dataForm.storeTel"></el-input>
        </el-form-item>

        <el-form-item label="门店省份" prop="storeProvince">
          <el-select v-model="dataForm.storeProvince" clearable placeholder="请选择"  @change="selectCity">
            <el-option
              v-for="item in provinceOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>



        <el-form-item label="门店城市" prop="storeCity" >
          <el-select v-model="dataForm.storeCity" clearable placeholder="请选择">
            <el-option
              v-for="item in cityOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>



        <el-form-item label="地址信息" prop="storeAddress">
          <el-input     type="textarea" :rows="2" placeholder="请输入内容" v-model="dataForm.storeAddress" style="width: 610px;" ></el-input>
        </el-form-item>

        <el-form-item label="业务介绍"   prop="storeRemark">
          <el-input     type="textarea" :rows="2" placeholder="请输入内容" v-model="dataForm.storeRemark" style="width: 610px;" ></el-input>
        </el-form-item>

        <el-form-item label="服务上限人数" prop="servicePeo">
          <el-input v-model="dataForm.servicePeo"></el-input>
        </el-form-item>

        <!--   <el-form-item label="宣传照片" >
             <el-input v-model="dataForm.goodsBrief"></el-input>
           </el-form-item>-->
        <el-form-item label="宣传照片" style="display: block !important; margin-left: 10px">
          <el-upload
            class="avatar-uploader"
            action="/admin/storage/create"
            :data="upLoadData"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <template v-if="dataForm.storeImg" >
              <img :src="dataForm.storeImg" class="avatar">
            </template>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
          <span class="imgspefi">上传图片规格：245*143 图片宽度扩大两倍，高度等比例扩大</span>
        </el-form-item>


        <el-form-item label="门店坐标" prop="storeCoordinate">
          <el-input v-model="dataForm.storeCoordinate"></el-input>
        </el-form-item>
        <a href="http://lbs.qq.com/tool/getpoint/" target="_blank" style="color:blue">点击查询并获取地点坐标复制到文本框内</a>

        <!--<el-form-item label="门店类型" prop="isHot" style=" margin-left: 10px">-->
          <!--<el-select v-model="dataForm.isHot" placeholder="请选择">-->
            <!--<el-option label="自营" :value="true">-->
            <!--</el-option>-->
            <!--<el-option label="托管" :value="false">-->
            <!--</el-option>-->
          <!--</el-select>-->
        <!--</el-form-item>-->

        <!--<el-form-item label="门店星级" prop="isNew">-->
          <!--<el-select v-model="dataForm.isNew" placeholder="请选择">-->
            <!--<el-option label="*" :value="true">-->
            <!--</el-option>-->
            <!--<el-option label="**" :value="false">-->
            <!--</el-option>-->
          <!--</el-select>
        </el-form-item>
        -->






      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>


    <!--设置店长-->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogManageFormVisible">

      <div class="filter-container">
        <el-input clearable class="filter-item" style="width: 200px;" placeholder="请填写用户名称" v-model="userlistQuery.username">
        </el-input>

        <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleUserFilter">查找</el-button>

      </div>

      <!-- 查询结果 -->
      <el-table size="small" :data="userList" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>

        <el-table-column align="center" width="100px" label="用户ID" prop="id" sortable v-if='show' >
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="用户名称" prop="username">
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="手机号码" prop="mobile">
        </el-table-column>

        <el-table-column align="center" label="操作" width="250" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="handleSetManage(scope.row)">设置</el-button>
          </template>
        </el-table-column>

      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination background @size-change="handleUserSizeChange" @current-change="handleUserCurrentChange" :current-page="userlistQuery.page"
                       :page-sizes="[10,20,30,50]" :page-size="userlistQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="usertotal">
        </el-pagination>
      </div>
    </el-dialog>

    <!--添加项目和商品-->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogProjectFormVisible">

      <div class="filter-container">
        <el-input clearable class="filter-item" style="width: 200px;" placeholder="请填写商品名称" v-model="goodslistQuery.name">
        </el-input>

        <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleGoodsFilter">查找</el-button>

      </div>

      <!-- 查询结果 -->
      <el-table   ref="multipleTable" size="small"
                  :data="goodsList" v-loading="listLoading"
                  @selection-change="handleSelectionChange"
                  element-loading-text="正在查询中。。。" border fit highlight-current-row>

        <el-table-column type="selection" width="55">
        </el-table-column>

        <el-table-column align="center" width="100px" label="商品ID" prop="id" sortable>
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="商品编号" prop="goodsSn">
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="名称" prop="name">
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="专柜价格" prop="counterPrice">
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="当前价格" prop="retailPrice">
        </el-table-column>


      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination background @size-change="handleGoodsSizeChange" @current-change="handleGoodsCurrentChange" :current-page="goodslistQuery.page"
                       :page-sizes="[10,20,30,50]" :page-size="goodslistQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="goodstotal">
        </el-pagination>
      </div>

      <div  slot="footer" class="dialog-footer">
        <el-button @click="dialogProjectFormVisible = false">取消</el-button>
        <el-button  type="primary" @click="createGoodsData">添加</el-button>
      </div>
    </el-dialog>

    <!--编辑项目和商品-->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogEditProjectFormVisible">

      <div class="filter-container">
        <el-input clearable class="filter-item" style="width: 200px;" placeholder="请填写商品名称" v-model="goodslistQuery.name">
        </el-input>

        <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleGoodsFilter">查找</el-button>

      </div>

      <!-- 查询结果 -->
      <el-table   ref="multipleTable" size="small"
                  :data="goodsList" v-loading="listLoading"
                  @selection-change="handleSelectionChange"
                  element-loading-text="正在查询中。。。" border fit highlight-current-row>
        <el-table-column align="center" width="100px" label="商品ID" prop="id" sortable>
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="商品编号" prop="goodsSn">
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="名称" prop="name">
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="专柜价格" prop="counterPrice">
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="当前价格" prop="retailPrice">
        </el-table-column>

        <!--<el-table-column align="center" label="操作" width="80" class-name="small-padding fixed-width">-->
          <!--<template slot-scope="scope">-->
            <!--<el-button type="danger" size="mini"  @click="handleDeleteProject(scope.row)">删除</el-button>-->
          <!--</template>-->
        <!--</el-table-column>-->

      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination background @size-change="handleGoodsSizeChange" @current-change="handleGoodsCurrentChange" :current-page="goodslistQuery.page"
                       :page-sizes="[10,20,30,50]" :page-size="goodslistQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="goodstotal">
        </el-pagination>
      </div>


    </el-dialog>

    <!--分配医生-->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogDoctorFormVisible">

      <div class="filter-container">
        <el-input clearable class="filter-item" style="width: 200px;" placeholder="请填写用户名称" v-model="doctorlistQuery.username">
        </el-input>

        <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleDoctorFilter">查找</el-button>

      </div>

      <!-- 查询结果 -->
      <el-table size="small" :data="doctorList"
                v-loading="listLoading"
                ref="multipleTable"
                @selection-change="handleDocSelectionChange"
                element-loading-text="正在查询中。。。" border fit highlight-current-row>

        <el-table-column type="selection" width="55">
        </el-table-column>

        <el-table-column align="center" width="100px" label="用户ID" prop="id" sortable v-if='show' >
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="医生名称" prop="username">
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="手机号码" prop="mobile">
        </el-table-column>



      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination background @size-change="handleUserSizeChange" @current-change="handleUserCurrentChange" :current-page="doctorlistQuery.page"
                       :page-sizes="[10,20,30,50]" :page-size="doctorlistQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="doctortal">
        </el-pagination>
      </div>


      <div  slot="footer" class="dialog-footer">
        <el-button @click="dialogDoctorFormVisible = false">取消</el-button>
        <el-button  type="primary" @click="handleSetDoctor">添加</el-button>
      </div>
    </el-dialog>

    <!--编辑医生-->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogEditDoctorFormVisible">

      <div class="filter-container">
        <el-input clearable class="filter-item" style="width: 200px;" placeholder="请填写用户名称" v-model="doctorlistQuery.username">
        </el-input>

        <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleDoctorFilter">查找</el-button>

      </div>

      <!-- 查询结果 -->
      <el-table size="small" :data="doctorList"
                v-loading="listLoading"
                element-loading-text="正在查询中。。。" border fit highlight-current-row>



        <el-table-column align="center" width="100px" label="用户ID" prop="id" sortable v-if='show' >
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="医生名称" prop="username">
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="手机号码" prop="mobile">
        </el-table-column>

        <el-table-column align="center" label="操作" width="80" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button type="danger" size="mini"  @click="handleDeleteDoctor(scope.row)">删除</el-button>
          </template>
        </el-table-column>

      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination background @size-change="handleUserSizeChange" @current-change="handleUserCurrentChange" :current-page="doctorlistQuery.page"
                       :page-sizes="[10,20,30,50]" :page-size="doctorlistQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="doctortal">
        </el-pagination>
      </div>
      <div  slot="footer" class="dialog-footer">
        <el-button @click="dialogEditDoctorFormVisible = false">取消</el-button>
      </div>
    </el-dialog>
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
  .el-dialog {
    width: 750px;
  }

  .el-dialog .el-form .el-input{
    width: 258px;
  }
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
  .imgspefi{
    color:#F00;
    vertical-align: top;
    line-height: 50px;
    margin-left: 20px;
  }
  .avatar-uploader{
    display: inline-block;
  }

</style>

<script>
  import { listStore, createStore, updateStore, deleteStore, listUser, createManage, /*listOptions,*/
    listProvinceOptions, addProject, listCityOptions, listGoods, deleteStoreGoods, listDoctor, createDoctor, deleteStoreDoctor } from '@/api/store'
  import waves from '@/directive/waves' // 水波纹指令
  import BackToTop from '@/components/BackToTop'
  import Tinymce from '@/components/Tinymce'
  export default {
    name: 'LitemallStore',
    components: { BackToTop, Tinymce },
    directives: { waves },
    data() {
      return {
        userList: undefined,
        goodsList: undefined,
        doctorList: undefined,
        list: undefined,
        total: undefined,
        goodstotal: undefined,
        usertotal: undefined,
        doctortal: undefined,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          storeName: undefined,
          storeCity: undefined,
          province: undefined,
          sort: '+id'
        },
        value4: '',
        multipleSelection: [],
        multipleSelectionDoctor: [],
        rows: [],
        userlistQuery: {
          page: 1,
          limit: 20,
          username: undefined,
          sort: '+id'
        },
        doctorlistQuery: {
          page: 1,
          limit: 20,
          username: undefined,
          storeId: undefined,
          sflag:  undefined,
          sort: '+id'
        },
        goodslistQuery: {
          page: 1,
          limit: 20,
          name: undefined,
          storeId: undefined,
          sflag:  undefined,
          sort: '+id'
        },

        dataForm: {
          id: undefined,
          storeName: undefined,
          storeTel: undefined,
          storeAddress: undefined,
          storeRemake: undefined,
          storeProvince: undefined,
          storeImg: undefined,
          servicePeo: undefined,
          storeCoordinate: undefined,
          storeCity: undefined
        },

        dataProjectForm: {
          options: undefined
        },

        provinceOptions:  undefined,
        cityOptions:  undefined,
        province:  undefined,

        dataProjectGoodsForm: {
          storeId:  undefined,
          goodsId:  ''

        },
        dataDoctorGoodsForm:  {
          storeId:  undefined,
          doctorId:  undefined
        },
        manageData: {
          storeId: undefined,
          userId: undefined,
          flag: '0'//判断为店长
        },
        doctorData: {
          storeId: undefined,
          flag: '1',//判断为医生
          userId: undefined
        },
        upLoadData: {
          imgBelongs: '1' //表示属于门店
        },
        dialogFormVisible: false,
        dialogManageFormVisible: false,
        dialogProjectFormVisible: false,
        dialogDoctorFormVisible:  false,
        dialogEditDoctorFormVisible:  false,
        dialogEditProjectFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑门店',
          create: '创建门店',
          project: '添加项目',
          editProject: '编辑项目',
          manage: '设置店长',
          doctor: '设置医生',
          eidtDoctor: '编辑医生'

        },
        rules: {
          storeName: [{ required: true, message: '门店名称不能为空', trigger: 'blur' }],
          storeTel: [{ required: true, message: '门店电话不能为空', trigger: 'blur' }],
          storeAddress: [{ required: true, message: '门店地址不能为空', trigger: 'blur' }],
          storeRemark: [{ required: true, message: '业务介绍不能为空', trigger: 'blur' }],
          storeProvince: [{ required: true, message: '门店省份不能为空', trigger: 'blur' }],
          servicePeo: [{ required: true, message: '项目限制不能为空', trigger: 'blur' }],
          storeCoordinate: [{ required: true, message: '门店坐标不能为空', trigger: 'blur' }],
          storeCity: [{ required: true, message: '门店城市不能为空', trigger: 'blur' }]
        },
        rulesProject: {
          projectTime: [{ required: true, message: '项目时间不能为空', trigger: 'blur' }],
          goodsSn: [{ required: true, message: '项目不能为空', trigger: 'blur' }],
          servicePeo: [{ required: true, message: '项目限制不能为空', trigger: 'blur' }]
        },
        downloadLoading: false,
        imageUrl: '',
        fileImgUrl: process.env.BASE_API + '/storage/create'
      }
    },
    created() {
      this.getList(),
      this.getProvinceOptions(),
     // this.selectCity(),
      this.getUserList()
    },
    methods: {
      handleSelectionChange(val) {
        this.multipleSelection=''
        this.multipleSelection = val

      },
      handleDocSelectionChange(val) {
        this.multipleSelectionDoctor = ''
        this.multipleSelectionDoctor = val

      },
   /* toggleSelection() {
        debugger
        if (this.multipleSelection) {
          this.multipleSelection.forEach(row => {
            this.$refs.multipleTable.toggleRowSelection(row)
          })
        }
      },*/
      clear() {
        this.$refs.multipleTable.clearSelection()
      },

      getList() {
        this.listLoading = true
        listStore(this.listQuery).then(response => {
          this.list = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.list = []
          this.total = 0
          this.listLoading = false
        })
      },

      getUserList() {
        this.listLoading = true
        listUser(this.userlistQuery).then(response => {
          this.userList = response.data.data.items
          this.usertotal = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.userList = []
          this.usertotal = 0
          this.listLoading = false
        })
      },
      getGoodsList() {
        this.listLoading = true
        listGoods(this.goodslistQuery).then(response => {
          this.goodsList = response.data.data.items
          this.goodstotal = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.goodsList = []
          this.goodstotal = 0
          this.listLoading = false
        })
      },

      getDoctorList() {
        this.listLoading = true
        listDoctor(this.doctorlistQuery).then(response => {
          this.doctorList = response.data.data.items
          this.doctortal = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.List = []
          this.goodstotal = 0
          this.listLoading = false
        })
      },

    /*  getOptions()  {
        listOptions().then(response => {
          this.dataProjectForm.options = response.data.data.items
          this.listLoading = false
        }).catch(() => {
          this.dataProjectForm.options = []
          this.listLoading = false
        })
      },*/

      getProvinceOptions()  {
        listProvinceOptions().then(response => {
          this.provinceOptions = response.data.data.items
          this.listLoading = false
        }).catch(() => {
          this.provinceOptions = []
          this.listLoading = false
        })
      },

      selectCity()  {
        this.listQuery.province = this.dataForm.storeProvince
        listCityOptions(this.listQuery).then(response => {
          this.cityOptions = response.data.data.items
          this.listLoading = false
        }).catch(() => {
          this.cityOptions = []
          this.listLoading = false
        })
      },
      handleFilter() {
        this.listQuery.page = 1
        this.getList()
      },

      handleUserFilter() {
        this.userlistQuery.page = 1
        this.getUserList()
      },

      handleDoctorFilter() {
        this.doctorlistQuery.page = 1
        this.getDoctorList()
      },
      handleGoodsFilter() {
        this.goodslistQuery.page = 1
        this.getGoodsList()
      },

      handleSizeChange(val) {
        this.userlistQuery.limit = val
        this.getList()
      },
      handleCurrentChange(val) {
        this.listQuery.page = val
        this.getList()
      },

      handleUserSizeChange(val) {
        this.userlistQuery.limit = val
        this.getUserList()
      },
      handleUserCurrentChange(val) {
        this.userlistQuery.page = val
        this.getUserList()
      },

      handleGoodsSizeChange(val) {
        this.goodslistQuery.limit = val
        this.getGoodsList()
      },
      handleGoodsCurrentChange(val) {
        this.goodslistQuery.page = val
        this.getGoodsList()
      },

      resetForm() {
        this.dataForm = {
          id: undefined,
          storeName: undefined,
          storeTel: undefined,
          storeAddress: undefined,
          storeRemake: undefined,
          storeProvince: undefined,
          storeImg: undefined,
          storeCity: undefined
        }
        this.dataProjectGoodsForm = {
          storeId:  undefined,
          goodsId:  undefined

        }
      },
      filterLevel(value, row) {
        return row.level === value
      },
      handleCreate() {
        this.resetForm()
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        this.cityOptions = []
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      setStoreManage(row) {
        this.resetForm()
        this.dialogStatus = 'manage'
        this.dialogManageFormVisible = true
        this.manageData.storeId = row.id
      },
      //添加项目弹出框
      setProject(row) {
        this.resetForm()
        this.goodslistQuery.storeId = row.id
        this.goodslistQuery.sflag = '1'
        this.dialogStatus = 'project'
        this.dialogProjectFormVisible = true
        this.dataProjectGoodsForm.storeId = row.id
        this.getGoodsList()
      },

      //编辑项目弹出框
      editProject(row) {
        this.resetForm()
        this.goodslistQuery.storeId = row.id
        this.goodslistQuery.sflag = '0'
        this.dialogStatus = 'editProject'
        this.dialogEditProjectFormVisible = true
        this.dataProjectGoodsForm.storeId = row.id
        this.getGoodsList()
      },
      //添加医生弹出框
      setDoctor(row) {
        this.resetForm()
        this.doctorlistQuery.storeId = row.id
        this.doctorlistQuery.sflag = '1'
        this.dialogStatus = 'doctor'
        this.dialogDoctorFormVisible = true
        this.dataDoctorGoodsForm.storeId = row.id
        this.doctorData.storeId = row.id
        this.getDoctorList()
      },

      //编辑医生弹出框
      editDoctor(row) {
        this.resetForm()
        this.doctorlistQuery.storeId = row.id
        this.doctorlistQuery.sflag = '0'
        this.dialogStatus = 'eidtDoctor'
        this.dialogEditDoctorFormVisible = true
        this.dataDoctorGoodsForm.storeId = row.id
        this.doctorData.storeId = row.id
        this.getDoctorList()
      },


      //设置店长
      handleSetManage(row) {
        this.manageData.userId = row.id
        //alert(this.manageData.storeId+"----------"+this.manageData.userId)
        createManage(this.manageData).then(response => {
          //this.list.unshift(response.data.data)
          //重新查询list 开始======
          this.listQuery.page = 1
          this.getList()
          //重新查询list 结束======
          this.dialogManageFormVisible = false
          this.$notify({
            title: '成功',
            message: '创建成功',
            type: 'success',
            duration: 2000
          })
        })
      },

      //设置医生
      handleSetDoctor(row) {
        this.doctorData.userId = ''
        this.multipleSelectionDoctor.forEach(row => {
          this.doctorData.userId = this.doctorData.userId + row.id + ','
        })
       /* alert(this.doctorData.storeId+"----------"+this.doctorData.userId)*/
        createDoctor(this.doctorData).then(response => {
          //重新查询list 开始======
          this.listQuery.page = 1
          this.getList()
          //重新查询list 结束======
          this.dialogDoctorFormVisible = false
          this.$notify({
            title: '成功',
            message: '创建成功',
            type: 'success',
            duration: 2000
          })
        })
      },


      createData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            createStore(this.dataForm).then(response => {
              //this.list.unshift(response.data.data)
              //重新查询list 开始======
              this.listQuery.page = 1
              this.getList()
              //重新查询list 结束======
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              })
            })
          }
        })
      },

      //新增服务 获取所有已经勾选的项目服务
      createGoodsData() {
        this.dataProjectGoodsForm.goodsId = ''
        this.multipleSelection.forEach(row =>{
          this.dataProjectGoodsForm.goodsId = this.dataProjectGoodsForm.goodsId + row.id + ','
        })
        //请求后台插入数据
        addProject(this.dataProjectGoodsForm).then(response => {
          //重新查询list 开始======
          this.listQuery.page = 1
          this.getList()
          //重新查询list 结束======
          this.dialogProjectFormVisible = false
          this.$notify({
            title: '成功',
            message: '创建成功',
            type: 'success',
            duration: 2000
          })
        })
      },

      handleUpdate(row) {
        this.selectCity()
        this.dataForm = Object.assign({}, row)
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      updateData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            updateStore(this.dataForm).then(() => {
              for (const v of this.list) {
                if (v.id === this.dataForm.id) {
                  const index = this.list.indexOf(v)
                  this.list.splice(index, 1, this.dataForm)
                  break
                }
              }
              //重新查询list 开始======
              this.listQuery.page = 1
              this.getList()
              //重新查询list 结束======
              this.dialogFormVisible = false
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
      handleDelete(row) {
        deleteStore(row).then(response => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.goodslistQuery.sflag = '0'
          this.goodslistQuery.page = 1
          this.getGoodsList()
          const index = this.list.indexOf(row)
          this.list.splice(index, 1)
        })
      },
      //删除项目
      handleDeleteProject(row) {
        this.dataProjectGoodsForm.goodsId = row.id
        deleteStoreGoods(this.dataProjectGoodsForm).then(response => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.handleGoodsFilter()
        })
      },

      //删除医生
      handleDeleteDoctor(row) {
        this.doctorData.userId = row.id
        deleteStoreDoctor(this.doctorData).then(response => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.handleDoctorFilter()
        })
      },

      //图片上传开始


      handleAvatarSuccess(res) {
        debugger
        //this.imageUrl = res.data.url;
        this.dataForm.storeImg = res.data.url;
      },
      /*  uploadSuccess (response) {
          let code = response.returncode;
          let msg = response.msg;
          this.open(msg, code);
        },*/
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg'
        const isLt2M = file.size / 1024 / 1024 < 2


        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!')
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!')
        }
        return isJPG && isLt2M
      },
      //图片上传结束

      handleDownload() {
        this.downloadLoading = true
        import('@/vendor/Export2Excel').then(excel => {
          const tHeader = ['门店ID', '门店名称', '店长', '地址', '联系方式']
          const filterVal = ['id', 'storeName', 'managerName', 'storeAddress', 'storeTel' ]
          excel.export_json_to_excel2(tHeader, this.list, filterVal, '门店信息')
          this.downloadLoading = false
        })
      }
    }
  }
</script>
