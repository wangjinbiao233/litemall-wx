<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入商品编号" v-model="listQuery.goodsSn">
      </el-input>
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入商品名称" v-model="listQuery.name">
      </el-input>
      <el-select clearable class="filter-item" style="width: 200px;" v-model="listQuery.categoryId" filterable placeholder="请选择商品分类">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <router-link ref='tag' :to="{path:'/goods/goodsCreate'}">
      <el-button class="filter-item" type="primary" icon="el-icon-edit">添加</el-button>
      </router-link>
      <el-button class="filter-item" type="primary" :loading="downloadLoading" v-waves icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" class="demo-table-expand">
            <el-form-item label="首页主图">
              <span>{{ props.row.listPicUrl }}</span>
            </el-form-item>
            <el-form-item label="宣传画廊">
              <span>{{ props.row.gallery }}</span>
            </el-form-item>
            <el-form-item label="商品介绍">
              <span>{{ props.row.goodsBrief }}</span>
            </el-form-item>
            <el-form-item label="商品详细介绍">
              <span>{{ props.row.goodsDesc }}</span>
            </el-form-item>
            <el-form-item label="商品主图">
              <span>{{ props.row.primaryPicUrl }}</span>
            </el-form-item>
            <el-form-item label="商品单位">
              <span>{{ props.row.goodsUnit }}</span>
            </el-form-item>
            <el-form-item label="关键字">
              <span>{{ props.row.keyword }}</span>
            </el-form-item>
            <el-form-item label="类目ID">
              <span>{{ props.row.categoryId }}</span>
            </el-form-item>
            <el-form-item label="品牌商ID">
              <span>{{ props.row.brandId }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>

      <!--
      <el-table-column align="center" width="100px" label="商品ID" prop="id" sortable>
      </el-table-column>
       -->     
      
      <el-table-column align="center" min-width="100px" label="商品编号" prop="goodsSn" sortable>
      </el-table-column>
         

      <el-table-column align="center" min-width="150px" label="名称" prop="name">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="专柜价格" prop="counterPrice">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="当前价格" prop="retailPrice">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="是否新品" prop="isNew">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isNew ? 'success' : 'error' ">{{scope.row.isNew ? '新品' : '非新品'}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="是否热品" prop="isHot">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isHot ? 'success' : 'error' ">{{scope.row.isHot ? '热品' : '非热品'}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="是否在售" prop="isOnSale">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isOnSale ? 'success' : 'error' ">{{scope.row.isOnSale ? '在售' : '未售'}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="300px" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <router-link ref='tag' :to="{path:'/goods/goodsDetail',query: {id: scope.row.id, name: scope.row.name}}">
            <el-button style="width: inherit;" type="primary" size="mini" icon="el-icon-search">详情</el-button>
          </router-link>
          <!-- <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button> -->

          <el-popover
            placement="top"
            width="160"
            v-model="scope.row.visible2">
            <p>确定删除这个商品吗？</p>
            <div style="text-align: right; margin: 0">
              <el-button size="mini" type="primary" @click="scope.row.visible2 = false">取消</el-button>
              <el-button type="primary" size="mini" @click="handleDelete(scope.row)">确定</el-button>
            </div>
            <el-button slot="reference" type="danger" size="mini"  @click="scope.row.visible2 = true">删除</el-button>
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

    <el-tooltip placement="top" content="返回顶部">
      <back-to-top :visibilityHeight="100" ></back-to-top>
    </el-tooltip>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>
        <el-form-item label="商品编号" prop="goodsSn">
          <el-input v-model="dataForm.goodsSn"></el-input>
        </el-form-item>
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="dataForm.name"></el-input>
        </el-form-item>
        <el-form-item label="专柜价格" prop="counterPrice">
          <el-input v-model="dataForm.counterPrice"></el-input>
        </el-form-item>
        <el-form-item label="当前价格" prop="retailPrice">
          <el-input v-model="dataForm.retailPrice"></el-input>
        </el-form-item>
        <el-form-item label="是否新品" prop="isNew">
          <el-select v-model="dataForm.isNew" placeholder="请选择">
            <el-option label="是" :value="true">
            </el-option>
            <el-option label="否" :value="false">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否热品" prop="isHot">
          <el-select v-model="dataForm.isHot" placeholder="请选择">
            <el-option label="热品" :value="true">
            </el-option>
            <el-option label="非热品" :value="false">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否在售" prop="isOnSale">
          <el-select v-model="dataForm.isOnSale" placeholder="请选择">
            <el-option label="在售" :value="true">
            </el-option>
            <el-option label="未售" :value="false">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品归属" prop="flag">
          <el-select v-model="dataForm.flag" placeholder="请选择" @change="onSelectedFlag($event, item)">
            <el-option label="实物商品" :key="'1'" :value="'1'">
            </el-option>
            <el-option label="服务类商品" :key="'2'" :value="'2'">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="疗程数" :visible.sync="treatmentNumVisible">
          <el-input v-model="dataForm.treatmentNum"></el-input>
        </el-form-item>

        <el-form-item label="商品单位">
          <el-input v-model="dataForm.goodsUnit"></el-input>
        </el-form-item>

        <el-form-item label="关键字">
          <el-input v-model="dataForm.keyword"></el-input>
        </el-form-item>
        <!--
        <el-form-item label="类目ID">
          <el-input v-model="dataForm.categoryId"></el-input>
        </el-form-item>
        -->
        <el-form-item label="类目">
          <el-select v-model="dataForm.categoryId" filterable placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <!--
        <el-form-item label="品牌商ID">
          <el-input v-model="dataForm.brandId"></el-input>
        </el-form-item>
        -->
        <el-form-item label="品牌">
          <el-select v-model="dataForm.brandId" filterable placeholder="请选择">
            <el-option
              v-for="item in brandOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="首页主图">
          <el-upload
            class="avatar-uploader"
            action="/admin/storage/create"
            :show-file-list="false"
            :on-success="handlePrimaryPicSuccess"
            :before-upload="beforePrimaryPicUpload">
            <img v-if="dataForm.listPicUrl" :src="dataForm.listPicUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="商品主图">
          <el-upload
            class="avatar-uploader"
            action="/admin/storage/create"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <img v-if="imageUrl" :src="imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item style="width: 800px;" label="宣传画廊">
          <el-upload
            action="/admin/storage/create"
            list-type="picture-card"
            multiple
            :limit="5"
            :data="typeDate"
            :file-list="typeDate.fileList"
            :on-success="handleGallerySucess"
            :on-exceed="handleExceed"
            :before-upload="uploadBannerImg"
            :on-remove="handleRemove">
            <i class="el-icon-plus"></i>
          </el-upload>
        </el-form-item>
        <!--
        <el-form-item label="首页主图">
          <el-input v-model="dataForm.listPicUrl"></el-input>
        </el-form-item>
        -->
        <!--
        <el-form-item label="宣传画廊">
          <el-input v-model="dataForm.gallery"></el-input>
        </el-form-item>
        -->
        <!--
        <el-form-item label="商品主图">
          <el-input v-model="dataForm.primaryPicUrl"></el-input>
        </el-form-item>
        -->

        <el-form-item label="商品介绍">
          <el-input v-model="dataForm.goodsBrief"></el-input>
        </el-form-item>

        <el-form-item style="width: 700px;" label="商品详细介绍">
          <tinymce v-model="dataForm.goodsDesc"></tinymce>
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
  .el-dialog {
    width: 800px;
  }

</style>

<script>
import { listGoods, createGoods, updateGoods, deleteGoods, readGoods } from '@/api/goods'
import { listCategoryBySubId } from '@/api/category'
import { listBrand } from '@/api/brand'
import waves from '@/directive/waves' // 水波纹指令
import BackToTop from '@/components/BackToTop'
import Tinymce from '@/components/Tinymce'

export default {
  name: 'Goods',
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
        goodsSn: undefined,
        name: undefined,
        categoryId: undefined,
        sort: '+id'
      },
      readGoodsQuery: {
        id: 0
      },
      listCategoryQuery: {
        subId :'1005000',
        sort: '+id'
      },
      listBrandQuery: {
        limit: 1000,
        sort: '+id'
      },
      typeDate: {
        imgBelongs:"2",
        fileList:[]
      },
      dataForm: {
        id: undefined,
        goodsSn: undefined,
        name: undefined,
        counterPrice: undefined,
        retailPrice: undefined,
        isHot: false,
        isNew: true,
        isOnSale: true,
        listPicUrl: undefined,
        primaryPicUrl: undefined,
        goodsBrief: undefined,
        goodsDesc: undefined,
        keywords: undefined,
        gallery: [],
        categoryId: undefined,
        brandId: undefined,
        treatmentNum:0,
        flag: '1'
      },
      treatmentNumVisible:false,
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        goodsSn: [{ required: true, message: '商品编号不能为空', trigger: 'blur' }],
        name: [{ required: true, message: '商品名称不能为空', trigger: 'blur' }]
      },
      downloadLoading: false,
      options : [],
      brandOptions : [],
      imageUrl: ''
    }
  },
  created() {
    this.getList()

    // 获取品类列表
    this.listCategoryQuery.subId = this.dataForm.categoryId
    listCategoryBySubId(this.listCategoryQuery).then(response => {
      this.options = response.data.data.items.map((item) => {
        return {value : item.id, label : item.name}
      })
    }).catch(() => {
      this.options = []
    })

    // 获取品牌信息
    listBrand(this.listBrandQuery).then(response => {
      this.brandOptions = response.data.data.items.map((item) => {
        return {value : item.id, label : item.name}
      });
    }).catch(() => {
      this.brandOptions = [];
    })
  },
  methods: {
    getList() {
      this.listLoading = true
      listGoods(this.listQuery).then(response => {
        this.list = response.data.data.items
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
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
        goodsSn: undefined,
        name: undefined,
        counterPrice: undefined,
        retailPrice: undefined,
        isHot: false,
        isNew: true,
        isOnSale: true,
        listPicUrl: undefined,
        primaryPicUrl: undefined,
        goodsBrief: undefined,
        goodsDesc: undefined,
        keywords: undefined,
        gallery: [],
        categoryId: undefined,
        brandId: undefined,
        treatmentNum:0,
        flag: '1'
      }
    },
    filterLevel(value, row) {
      return row.level === value
    },
    handleCreate() {
      this.resetForm()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createGoods(this.dataForm).then(response => {
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

      // 构建画廊图片列表数据
      this.typeDate.fileList = []
      if(this.dataForm.gallery && this.dataForm.gallery.length) {
          for(let i in this.dataForm.gallery) {
            let item = this.dataForm.gallery[i]
            let name = 'file_' + i
            let url = this.dataForm.gallery[i]

            this.typeDate.fileList.push({name : name, url : url, response : {errno : '0', data : {url : url}}})
          }
      }
      // 设置商品图片数据
      this.imageUrl = this.dataForm.listPicUrl

      // 获取商品介绍描述
      this.readGoodsQuery.id = this.dataForm.id
      readGoods(this.readGoodsQuery).then(response => {
        this.dataForm.goodsDesc = response.data.data.goodsDesc
      }).catch(() => {

      })

      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {

      //console.log("------------------>" + this.dataForm.gallery.length)

      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateGoods(this.dataForm).then(() => {
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
      deleteGoods(row).then(response => {
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
        const tHeader = ['商品ID', '商品编号', '名称', '专柜价格', '当前价格', '是否新品', '是否热品', '是否在售', '首页主图', '宣传画廊', '商品介绍', '详细介绍', '商品主图', '商品单位', '关键字', '类目ID', '品牌商ID']
        const filterVal = ['id', 'goodsSn', 'name', 'counterPrice', 'retailPrice', 'isNew', 'isHot', 'isOnSale', 'listPicUrl', 'gallery', 'goodsBrief', 'goodsDesc', 'primaryPicUrl', 'goodsUnit', 'keywords', 'categoryId', 'brandId']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '商品信息')
        this.downloadLoading = false
      })
    },
    handleRemove(file, fileList) {
      console.log(file, fileList)
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 5 个文件，本次选择了 ${files.length} 个文件！`)
    },
    uploadBannerImg(file) {
      const isJPGs = file.type === 'image/jpeg'
      console.log(isJPGs)
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw)
      this.dataForm.primaryPicUrl = res.data.url
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
    handlePrimaryPicSuccess(res, file) {
      //this.dataForm.primaryPicUrl = URL.createObjectURL(file.raw)
      this.dataForm.listPicUrl = res.data.url;
    },
    beforePrimaryPicUpload(file) {
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
    handleGallerySucess(res, file, fileList) {

      this.dataForm.gallery = []; // 清空画廊图片数组

      for(let i in fileList) {

        let response = fileList[i].response

        if(response.errno && response.errno != '0') {

          this.$message.error('该图片上传失败,已被移除,请重新上传!')
          // 上传失败移除该 file 对象
          fileList.splice(i, 1)

        } else {

          let url = response.data.url
          this.dataForm.gallery.push(url)
        }
      }
    }
  }
}
</script>
<style>
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
</style>
