<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入知识编号" v-model="listQuery.id">
      </el-input>
      <el-input clearable  class="filter-item" style="width: 200px;" placeholder="请输入标题" v-model="listQuery.title">
      </el-input>
      <el-select v-model="listQuery.knowledgeCls" placeholder="请选择" clearable  style = "top: -4px;">
          <el-option
            v-for="item in kCategoryList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
      </el-select>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-download" @click="handleDownload" :loading="downloadLoading">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <!--<el-table-column align="center" width="100px" label="知识编号" prop="id" sortable>
      </el-table-column>-->
      <el-table-column type="index" label="序号" header-align="center" align="center">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="标题" prop="title">
      </el-table-column>

      <!--<el-table-column align="center" min-width="120px" label="首页横幅" prop="bannerUrl">-->
        <!--<template slot-scope="scope" v-if ="scope.row.bannerUrl" >-->
          <!--<img :src="scope.row.bannerUrl" style="width: 90px;height: 90px">-->
        <!--</template>-->
      <!--</el-table-column>-->

      <el-table-column align="center" min-width="300px" label="分类页横幅" prop="titlePicUrl">
        <template slot-scope="scope">
          <img  :src="scope.row.titlePicUrl" alt="" style="width: 270px;height: 90px">
        </template>
      </el-table-column>

      <el-table-column v-if="false" align="center" min-width="200px" label="介绍" prop="introduction">
      </el-table-column>

      <el-table-column v-if="false" align="center" min-width="400px" label="内容" prop="content">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="分类" prop="knowledgeCls" >
        <template slot-scope="scope">
          {{kCategoryMap[scope.row.knowledgeCls]}}
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="80px" label="展示" prop="isShow">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isShow ? 'success' : 'error' ">{{scope.row.isShow ? '展示' : '不展示'}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="访问数量" prop="visitCount">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="评价数量" prop="commentCount">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="点赞数量" prop="praiseCount">
      </el-table-column>

      <el-table-column align="center" label="操作" width="250" class-name="small-padding fixed-width">
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
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width = "800px">
      <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>
        <el-form-item label="知识标题" prop="title">
          <el-input v-model="dataForm.title" ></el-input>
        </el-form-item>

        <el-form-item label="知识分类" prop="knowledgeCls">
          <el-select v-model="dataForm.knowledgeCls" placeholder="请选择">
              <el-option
                v-for="item in kCategoryList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="知识介绍" prop="introduction">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入内容"
            v-model="dataForm.introduction">
          </el-input>
        </el-form-item>

        <el-form-item label="展示" prop="isShow">
          <el-select v-model="dataForm.isShow" placeholder="请选择">
              <el-option
                v-for="item in isShowList"
                :key="item.value"
                :label="item.name"
                :value="item.value">
              </el-option>
          </el-select>
        </el-form-item>

        <!--
        <el-form-item label="首页横幅" prop = "bannerUrl" >
          <el-tooltip content="建议图片宽高250*250，或宽高比:1" placement="top-start" style= "width:178px">
            <el-upload v-model="dataForm.bannerUrl"
              class="k-avatar-uploader"
              :action="fileImgUrl"
              :show-file-list="false"
              :on-success="handleBannerUrlSuccess"
              :before-upload="beforeAvatarUpload">
              <template v-if="dataForm.bannerUrl">
                <img :src="dataForm.bannerUrl" class="k-avatar" width="178px" height="178px">
              </template>
              <i v-else class="el-icon-plus k-avatar-uploader-icon"></i>
            </el-upload>
          </el-tooltip>
        </el-form-item>-->

        <el-form-item label="分类页横幅" prop = "titlePicUrl" >
          <el-tooltip content="建议图片宽高345*150 图片宽度扩大两倍，高度等比例扩大" placement="top-start" style= "width:178px">
            <el-upload v-model="dataForm.titlePicUrl"
              class="k-avatar-uploader"
              :action="fileImgUrl"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <template v-if="dataForm.titlePicUrl">
                <img :src="dataForm.titlePicUrl" class="k-avatar" width="300px" height="100px">
              </template>
              <i v-else class="el-icon-plus k-avatar-uploader-icon"></i>
            </el-upload>
          </el-tooltip>
        </el-form-item>

        <el-form-item label="视频" prop="video">
          <el-upload
            class="k-avatar-uploader el-upload--text"
            action="/admin/storage/create"
            :show-file-list="false"
            :on-success="handleVideoSuccess"
            :before-upload="beforeUploadVideo"
            :on-progress="uploadVideoProcess">
            <video v-if="dataForm.video !='' && videoFlag == false" :src="dataForm.video" class="k-avatar" width="300px" height="200px"
                   controls="controls">您的浏览器不支持视频播放</video>
            <i v-else-if="dataForm.video =='' && videoFlag == false" class="el-icon-plus k-avatar-uploader-icon"></i>
            <el-progress v-if="videoFlag == true" type="circle" :percentage="videoUploadPercent" style="margin-top:30px;"></el-progress>
          </el-upload>
          <P class="text">请保证视频格式正确，且不超过100M</P>
        </el-form-item>

        <el-form-item style="width: 700px;" label="知识内容" prop="content" >
          <tinymce v-model="dataForm.content" ref = "tinymce"></tinymce>
        </el-form-item>

        <el-form-item label="关联商品" prop="goodsId">
          <el-select v-model="dataForm.goodsId" multiple filterable placeholder="请选择" style="width: 350px;">
            <el-option v-for="item in goodsList" :label="item.name" :value="item.id">
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
  import { listKnowledge, createKnowledge, updateKnowledge,
    deleteKnowledge, getKCategory ,selectGoodSn, listKnowledgeGoods} from '@/api/knowledge'
  import waves from '@/directive/waves' // 水波纹指令
  import BackToTop from '@/components/BackToTop'
  import Tinymce from '@/components/Tinymce'

  export default {
    name: 'Knowledge',
    components: { BackToTop, Tinymce },
    directives: { waves },
    data() {
      return {
        list: undefined,
        total: undefined,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          title: undefined,
          knowledgeCls: undefined,
          id: undefined,
          sort: '+id'
        },
        dataForm: {
          id: undefined,
          title: undefined,
          introduction: undefined,
          bannerUrl: undefined,
          content: undefined,
          visitCount: undefined,
          commentCount: undefined,
          praiseCount: undefined,
          isShow: undefined,
          knowledgeCls: undefined,
          titlePicUrl: undefined,
          goodsId: [],
          titlePicUrl: undefined,
          video: undefined
        },
        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: '创建'
        },
        rules: {
          title: [{ required: true, message: '知识标题不能为空', trigger: 'blur' }],
          titlePicUrl: [{ required: true, message: '分类页横幅不能为空', trigger: 'blur' }],
          isShow: [{ required: true, message: '展示标识不能为空', trigger: 'blur' }],
          knowledgeCls: [{ required: true, message: '知识分类不能为空', trigger: 'blur' }],
          content: [{ required: true, message: '知识内容不能为空', trigger: 'blur' }]
        },
        goodsList: [],
        downloadLoading: false,
        videoFlag: false,
        fileImgUrl: process.env.BASE_API + '/storage/uploadPic',
        kCategoryList: undefined,
        kCategoryMap: undefined,
        isShowList: [{ 'value': true, 'name': '是' }, { 'value': false, 'name': '否' }]
      }
    },
    created() {
      this.getList()
      this.getKCategoryList()

      selectGoodSn().then(response => {
        if (response.data.errno === 0) {
          this.goodsList = response.data.data.items
          console.log(this.goodsList)
        } else {
          this.goodsList = []
        }

      }).catch(() => {
        this.goodsList = []
      })
    },
    methods: {
      getList() {
        this.listLoading = true
        listKnowledge(this.listQuery).then(response => {
          this.list = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.list = []
          this.total = 0
          this.listLoading = false
        })
      },
      getKCategoryList() {
        getKCategory().then(response => {
          this.kCategoryList = response.data.data.kCategory
          this.kCategoryMap = response.data.data.kCategoryMap
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
      resetForm() {
        this.dataForm = {
          id: undefined,
          title: undefined,
          content: undefined,
          introduction: undefined,
          bannerUrl: undefined,
          visitCount: undefined,
          commentCount: undefined,
          praiseCount: undefined,
          isShow: undefined,
          knowledgeCls: undefined,
          titlePicUrl: undefined,
          goodsId: [],
          video: undefined
        }
      },
      handleCreate() {
        this.resetForm()
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs.tinymce.setContent('')
          this.$refs['dataForm'].clearValidate()
        })
      },

      createData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            createKnowledge(this.dataForm).then(response => {
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
      handleUpdate(row) {
        this.dataForm = Object.assign({}, row)
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs.tinymce.setContent(row.content)
          this.$refs['dataForm'].clearValidate()
        })

        listKnowledgeGoods({ knowleId : this.dataForm.id}).then(response => {
          const items = response.data.data.items
          console.log(items)
          debugger
          this.dataForm.goodsId = items.map((item)=>{
            return item.id
          })
        }).catch(() => {

        })
      },
      updateData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            updateKnowledge(this.dataForm).then(() => {
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
        deleteKnowledge(row).then(response => {
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
          const tHeader = ['知识ID', '标题', '知识内容', '分类', '是否展示', '访问数量', '评价数量', '点赞数量']
          const filterVal = ['id', 'title', 'content', 'knowledgeCls', 'isShow', 'visitCount', 'commentCount', 'praiseCount']
          excel.export_json_to_excel2(tHeader, this.list, filterVal, '知识信息')
          this.downloadLoading = false
        })
      },
      // 图片上传
      uploadBannerImg(file) {
        const isJPGs = file.type === 'image/jpeg'
        console.log(isJPGs)
      },
      handleAvatarSuccess(res) {
        this.dataForm.titlePicUrl = res.data.url
      },
      handleBannerUrlSuccess(res) {
        this.dataForm.bannerUrl = res.data.url
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
      },
      // 视频上传
      handleVideoSuccess(res, file) { // 获取上传图片地址
        debugger
        this.videoFlag = false
        this.videoUploadPercent = 0
        if (res.status === 200) {
          this.dataForm.id = res.data.id
          this.dataForm.Video = res.data.uploadUrl
        } else {
          this.$message.error('视频上传失败，请重新上传！')
        }
      },
      beforeUploadVideo(file) {
        const isLt100M = file.size / 1024 / 1024 < 100
        if (['video/mp4', 'video/ogg', 'video/flv', 'video/avi', 'video/wmv', 'video/rmvb'].indexOf(file.type) === -1) {
          this.$message.error('请上传正确的视频格式')
          return false
        }
        if (!isLt100M) {
          this.$message.error('上传视频大小不能超过100MB哦!')
          return false
        }
      },
      uploadVideoProcess(event, file, fileList) {
        this.videoFlag = true
        this.videoUploadPercent = file.percentage.toFixed(0)
      }
    }
  }
</script>
<style>
  .k-avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .k-avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .k-avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .k-avatar {
    display: block;
  }
</style>
