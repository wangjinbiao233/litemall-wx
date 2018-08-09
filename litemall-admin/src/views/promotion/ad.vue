<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入广告标题" v-model="listQuery.name">
      </el-input>
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入广告内容" v-model="listQuery.content">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-download" @click="handleDownload" :loading="downloadLoading">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>


      <el-table-column align="center" width="100px" label="广告ID" prop="id" sortable>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="广告标题" prop="name">
      </el-table-column>

      <el-table-column align="center" min-width="180px" label="广告内容" prop="content">
      </el-table-column>

      <el-table-column align="center" min-width="200px" label="广告图片" prop="url">
        <template slot-scope="scope">
          <img :src= "scope.row.url" width="180px" height="90px"/>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="50px" label="广告位置" prop="position">
      </el-table-column>

      <el-table-column align="center" min-width="200px" label="活动链接" prop="linkName">
      </el-table-column>

      <el-table-column align="center" min-width="80px" label="是否启用" prop="enabled">
        <template slot-scope="scope">
          <el-tag :type="scope.row.enabled ? 'success' : 'error' ">{{ scope.row.enabled ? '启用' : '不启用' }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="240px" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button type="danger" size="mini"  @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.page"
        :page-sizes="[10,20,30,50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>
        <el-form-item label="广告标题" prop="name">
          <el-input v-model="dataForm.name"></el-input>
        </el-form-item>
        <el-form-item label="广告内容" prop="content">
          <el-input v-model="dataForm.content"></el-input>
        </el-form-item>

        <el-form-item label="广告图片" prop="url">
          <el-tooltip content="建议图片规格：375*208" placement="top-start">
            <el-upload 
              class="ad-avatar-uploader"
              v-model="dataForm.url"
              :action="fileImgUrl"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <template v-if="dataForm.url">
                <img :src="dataForm.url" class="ad-avatar" width="300px" height="150px">
              </template>
              <i v-else class="el-icon-plus ad-avatar-uploader-icon"></i>         
            </el-upload>            
          </el-tooltip>
        </el-form-item>

        <el-form-item label="广告位置" prop="position">
          <el-select v-model="dataForm.position" placeholder="请选择">
            <el-option label="首页" :value="1">
            </el-option>
          </el-select>
        </el-form-item>        
        <el-form-item label="活动链接" prop="link">
          <!--<el-input v-model="dataForm.link"></el-input>-->
          <el-select clearable v-model="dataForm.link" placeholder="请选择活动链接"  @change="onSelectedLinkFlag($event, item)">
            <el-option
              v-for="infos in advertisingLinkList"
              :key="infos.codeid"
              :label="infos.name"
              :value="infos.codeid">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="优惠券类别" v-show="discountVisible">
          <el-select clearable v-model="dataForm.linkDetailid" placeholder="请选择优惠券类别" >
            <el-option
              v-for="item in couponsTypeList"
              :key="item.codeid"
              :label="item.name"
              :value="item.codeid">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="活动内容编号" v-show="nocodeVisible">
          <el-input v-model="dataForm.linkDetailid"  placeholder="请填写活动链接所选的链接内容编号" ></el-input>
        </el-form-item>
        
        <el-form-item label="是否启用" prop="enabled">
          <el-select v-model="dataForm.enabled" placeholder="请选择">
            <el-option label="启用" :value="true">
            </el-option>
            <el-option label="不启用" :value="false">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
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
  
</style>

<script>
import { listAd, createAd, updateAd, deleteAd, getDictionaryTypeList } from '@/api/ad'
import { createStorage } from '@/api/storage'
import waves from '@/directive/waves' // 水波纹指令

export default {
  name: 'Ad',
  directives: {
    waves
  },
  data() {
    return {
      list: undefined,
      total: undefined,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        name: undefined,
        content: undefined,
        sort: '+id'
      },
      dataForm: {
        id: undefined,
        name: undefined,
        content: undefined,
        url: undefined,
        link: undefined,
        position: 1,
        enabled: true,
        linkDetailid: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        name: [{ required: true, message: '广告标题不能为空', trigger: 'blur' }],
        content: [{ required: true, message: '广告内容不能为空', trigger: 'blur' }],
        url: [{ required: true, message: '广告链接不能为空', trigger: 'blur' }]
      },
      fileImgUrl: process.env.BASE_API + '/storage/uploadPic',
      downloadLoading: false,
      advertisingLinkList: undefined,
      couponsTypeList: undefined,
      discountVisible: false,
      nocodeVisible: false
    }
  },
  created() {
    this.getList()
    this.getAdvertisingLinkList()
    this.getCouponsTypeList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listAd(this.listQuery).then(response => {
        this.list = response.data.data.items
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },

    getAdvertisingLinkList(){
      getDictionaryTypeList({
        groupCode: 'advertising_link'
      }).then(response => {
        this.advertisingLinkList = response.data.data
      }).catch(() => {
        this.advertisingLinkList = []
      })
    },
    
    getCouponsTypeList(){
      getDictionaryTypeList({
        groupCode: 'coupons_type'
      }).then(response => {
        this.couponsTypeList = response.data.data
      }).catch(() => {
        this.couponsTypeList = []
      })
    },

    onSelectedLinkFlag(val) {
      if(val!=""){
          if(val == 5){//优惠券类别
            this.discountVisible = true;
            this.nocodeVisible = false;
          }else{//编号类型
            this.discountVisible = false;
            this.nocodeVisible = true;
          }
      }else{
          this.discountVisible = false;
          this.nocodeVisible = false;
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
    resetForm() {
      this.dataForm = {
        id: undefined,
        name: undefined,
        content: undefined,
        url: undefined,
        link: undefined,
        position: 1,
        enabled: true
      }
    },
    handleCreate() {
      this.resetForm()
      this.discountVisible = false;
      this.nocodeVisible = false;
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    uploadUrl(item) {
      const formData = new FormData()
      formData.append('file', item.file)
      createStorage(formData).then(res => {
        this.dataForm.url = res.data.data.url
      }).catch(() => {
        this.$message.error('上传失败，请重新上传')
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createAd(this.dataForm).then(response => {
            this.list.unshift(response.data.data)
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
    handleUpdate(row) {//修改页面
      this.dataForm = Object.assign({}, row)
      if(this.dataForm.link == '5'){//优惠券类别
        this.discountVisible = true;
        this.nocodeVisible = false;
      }else{//编号类型
        this.discountVisible = false;
        this.nocodeVisible = true;
      }
      
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateAd(this.dataForm).then(() => {
            for (const v of this.list) {
              if (v.id === this.dataForm.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, this.dataForm)
                break
              }
            }
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
      deleteAd(row).then(response => {
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
        const index = this.list.indexOf(row)
        this.list.splice(index, 1)
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['广告ID', '广告标题', '广告内容', '广告图片', '广告位置', '活动链接', '是否启用']
        const filterVal = ['id', 'name', 'content', 'url', 'postion', 'link', 'enabled']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '广告信息')
        this.downloadLoading = false
      })
    },
    // 图片上传
    uploadBannerImg(file) {
      const isJPGs = file.type === 'image/jpeg'
      console.log(isJPGs)
    },
    handleAvatarSuccess(res) {
      this.dataForm.url = res.data.url
    },
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