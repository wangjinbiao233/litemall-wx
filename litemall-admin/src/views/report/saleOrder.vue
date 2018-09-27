<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-select v-model="value8" filterable placeholder="请选择产品名称">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
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


      <el-table-column align="center" width="100px" label="门店名称" prop="id" sortable>
      </el-table-column>

      <el-table-column align="center" min-width="50px" label="日期" prop="position">
      </el-table-column>

      <el-table-column align="center" min-width="50px" label="订单号" prop="position">
      </el-table-column>

      <el-table-column align="center" min-width="50px" label="订单状态" prop="position">
      </el-table-column>

      <el-table-column align="center" min-width="50px" label="商品名称" prop="position">
      </el-table-column>

      <el-table-column align="center" min-width="50px" label="数量" prop="position">
      </el-table-column>

      <el-table-column align="center" min-width="50px" label="单价" prop="position">
      </el-table-column>

      <el-table-column align="center" min-width="50px" label="金额" prop="position">
      </el-table-column>

      <el-table-column align="center" min-width="50px" label="疗程数" prop="position">
      </el-table-column>

      <el-table-column align="center" min-width="50px" label="商品归属" prop="position">
      </el-table-column>

      <el-table-column align="center" min-width="50px" label="疗程总数" prop="position">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="会员编号" prop="name">
      </el-table-column>

      <el-table-column align="center" min-width="180px" label="会员名称" prop="content">
      </el-table-column>

      <el-table-column align="center" min-width="180px" label="订单金额" prop="content">
      </el-table-column>

      <el-table-column align="center" min-width="180px" label="券抵扣" prop="content">
      </el-table-column>

      <el-table-column align="center" min-width="180px" label="折扣" prop="content">
      </el-table-column>

      <el-table-column align="center" min-width="180px" label="实付金额" prop="content">
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
        // 产品下拉选项
        goodsList: [],
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
      this.getGoodsList()
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

      getAdvertisingLinkList() {
        getDictionaryTypeList({
          groupCode: 'advertising_link'
        }).then(response => {
          this.advertisingLinkList = response.data.data
        }).catch(() => {
          this.advertisingLinkList = []
        })
      },

      getGoodsList() {
        getDictionaryTypeList({
          groupCode: 'coupons_type'
        }).then(response => {
          this.couponsTypeList = response.data.data
        }).catch(() => {
          this.couponsTypeList = []
        })
      },

      onSelectedLinkFlag(val) {
        if (val != '') {
          if (val == 5) { // 优惠券类别
            this.discountVisible = true
            this.nocodeVisible = false
          } else { // 编号类型
            this.discountVisible = false
            this.nocodeVisible = true
          }
        } else {
          this.discountVisible = false
          this.nocodeVisible = false
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
        this.discountVisible = false
        this.nocodeVisible = false
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
      handleUpdate(row) { // 修改页面
        this.dataForm = Object.assign({}, row)
        if (this.dataForm.link == '5') { // 优惠券类别
          this.discountVisible = true
          this.nocodeVisible = false
        } else { // 编号类型
          this.discountVisible = false
          this.nocodeVisible = true
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
