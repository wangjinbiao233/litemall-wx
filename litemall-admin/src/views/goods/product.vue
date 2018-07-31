<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入商品ID" v-model="listQuery.goodsId">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-download" @click="handleDownload" :loading="downloadLoading">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" :span-method="objectSpanMethod" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <!--
      <el-table-column type="expand">
        <template slot-scope="props">
        </template>
      </el-table-column>
      -->
      <!--
      <el-table-column align="center" width="100px" label="货品ID" prop="id" sortable>
      </el-table-column>
      -->

      <el-table-column align="center" min-width="100px" label="商品ID" prop="goodsId" :disabled="true">
      </el-table-column>
      <!-- add by pengxb @2018-05-07 14:10 -->
      <el-table-column align="center" min-width="100px" label="商品名称" prop="goodsName" :disabled="true">
      </el-table-column>

      <!--
      <el-table-column align="center" min-width="150px" label="商品规格ID列表" prop="goodsSpecificationIds">
        <template slot-scope="scope">
          {{ scope.row.goodsSpecificationIds.join(',') }}
        </template>
      </el-table-column>
      -->

      <el-table-column align="left" min-width="150px" label="商品规格" prop="goodsSpecificationIds">
        <template slot-scope="scope">
          <el-tag size="mini" v-for="spec in scope.row.goodsSpec.split(',')">{{spec}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="货品数量" prop="goodsNumber">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="货品价格" prop="retailPrice">
      </el-table-column>

      <el-table-column align="center" min-width="200px" label="货品图片" prop="url">
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

    <!-- 添加货品输入框 -->
    <el-dialog title="添加货品" :visible.sync="createDialogFormVisible">
      <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>
        <el-form-item label="商品ID" prop="goodsId">
          <el-input style="width: 84%;" v-model="dataForm.goodsId" :disabled="true"></el-input>
          <el-button size="mini" icon="el-icon-edit" circle @click="handleSelectGoods"></el-button>
        </el-form-item>

        <el-form-item label="商品名称" prop="goodsName">
          <el-input v-model="dataForm.goodsName" :disabled="true"></el-input>
        </el-form-item>

        <el-form-item label="已有规格">
          <el-tag style="margin-left: 5px;" size="mini" v-for="spec in selectProductSpecName">{{spec}}</el-tag>
        </el-form-item>

        <el-form-item label="规格" prop="goodsSpecificationIds">
          <el-select style="width: 100%;" v-model="dataForm.goodsSpecificationIds" multiple filterable placeholder="请选择" @change="goodsSpecSelectChange">
            <el-option-group
              v-for="group in goodsSpecOptions"
              :key="group.label"
              :label="group.label">
              <el-option
                v-for="item in group.options"
                :key="item.value"
                :label="item.label"
                :disabled="item.disabled"
                :value="item.value">
              </el-option>
            </el-option-group>
          </el-select>
        </el-form-item>

        <el-form-item label="货品数量" prop="goodsNumber">
          <el-input v-model="dataForm.goodsNumber" :disabled="disabled.goodsName"></el-input>
        </el-form-item>
        <el-form-item label="货品价格" prop="retailPrice">
          <el-input v-model="dataForm.retailPrice" :disabled="disabled.retailPrice"></el-input>
        </el-form-item>
        <el-form-item label="货品图片" prop="url">
          <el-upload
            class="avatar-uploader"
            action="/admin/storage/create"
            :show-file-list="false"
            :on-success="handlePrimaryPicSuccess"
            :before-upload="beforePrimaryPicUpload">
            <img v-if="dataForm.url" :src="dataForm.url" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="createData" :disabled="disabled.btnCreateData">确定</el-button>
      </div>
    </el-dialog>

    <!-- 商品选择对话框 -->
    <el-dialog title="请选择商品" :visible.sync="dialogGoodsSelectVisible" width="60%">
      <div class="filter-container">
        <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入商品编号" v-model="goodsListQuery.goodsSn">
        </el-input>
        <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入商品名称" v-model="goodsListQuery.name">
        </el-input>
        <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleGoodsFilter">查找</el-button>
      </div>
      <!-- 查询结果 -->
      <el-table size="small" :data="goodsList" v-loading="listLoading" element-loading-text="正在查询中。。。" @row-dblclick="selectGoods">
        <el-table-column align="center" width="100px" label="商品ID" prop="id" sortable></el-table-column>
        <el-table-column align="center" min-width="250px" label="名称" prop="name"></el-table-column>
        <el-table-column align="center" label="操作" width="200px" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="selectGoods(scope.row)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination background @size-change="handleGoodsSizeChange" @current-change="handleGoodsCurrentChange" :current-page="goodsListQuery.page"
                       :page-sizes="[10,20,30,50]" :page-size="goodsListQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="goodsTotal">
        </el-pagination>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogGoodsSelectVisible = false">取消</el-button>
        <!-- <el-button v-if="dialogStatus=='create'" type="primary" @click="selectGoods">确定</el-button> -->
      </div>
    </el-dialog>

    <!-- 修改对话框 -->
    <el-dialog title="修改货品" :visible.sync="editDialogFormVisible">
      <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>
        <el-form-item label="商品ID" prop="goodsId">
          <el-input v-model="dataForm.goodsId" :disabled="true"></el-input>
        </el-form-item>

        <el-form-item label="商品名称" prop="goodsId">
          <el-input v-model="dataForm.goodsName" :disabled="true"></el-input>
        </el-form-item>


        <el-form-item label="商品规格ID" prop="goodsSpecificationIds">
          <el-input v-model="dataForm.goodsSpecificationIds.join(',')" :disabled="true"></el-input>
        </el-form-item>


        <el-form-item label="商品规格" prop="goodsSpec">
          <el-tag style="margin-left: 5px;" size="mini" v-for="spec in dataForm.goodsSpec.split(',')">{{spec}}</el-tag>
        </el-form-item>

        <el-form-item label="货品数量" prop="goodsNumber">
          <el-input v-model="dataForm.goodsNumber"></el-input>
        </el-form-item>
        <el-form-item label="货品价格" prop="retailPrice">
          <el-input v-model="dataForm.retailPrice"></el-input>
        </el-form-item>
        <el-form-item label="货品图片" prop="url">
          <!--
          <el-input v-model="dataForm.url"></el-input>
          <el-upload action="#" list-type="picture" :show-file-list="false" :limit="1" :http-request="uploadUrl">
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
          -->
          <el-upload
            class="avatar-uploader"
            action="/admin/storage/create"
            :show-file-list="false"
            :on-success="handlePrimaryPicSuccess"
            :before-upload="beforePrimaryPicUpload">
            <img v-if="dataForm.url" :src="dataForm.url" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="updateData">确定</el-button>
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
import { listProduct, createProduct, updateProduct, deleteProduct, listSpecGroup, listProductSpec } from '@/api/product'
import { createStorage } from '@/api/storage'
import { listGoods } from '@/api/goods'
import waves from '@/directive/waves' // 水波纹指令

export default {
  name: 'Product',
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
        goodsId: undefined,
        sort: '+id'
      },
      createDialogFormVisible: false,
      editDialogFormVisible: false,
      dataForm: {
        id: undefined,
        goodsId: undefined,
        goodsSpecificationIds: [],
        goodsNumber: 0,
        retailPrice: 0,
        url: undefined,
        goodsName: undefined, // add by pengxb @2018-05-07 14:11
        goodsSpec: ""  // add by pengxb @2018-05-07 15:32
      },
      rules: {
        goodsId: [{ required: true, message: '商品ID不能为空', trigger: 'blur' }],
        goodsSpecificationIds: [{ required: true, message: '商品规格ID列表不能为空', trigger: 'blur' }]
      },
      downloadLoading: false,
      spanInfo: undefined, // add by pengxb @2018-05-07 14:11
      dialogGoodsSelectVisible: false, // add by pengxb @2018-05-07 17:38
      goodsList: null, // add by pengxb @2018-05-07 17:38
      goodsTotal: null, // add by pengxb @2018-05-07 17:38
      goodsListQuery:{  // add by pengxb @2018-05-07 17:38
        page: 1,
        limit: 10,
        goodsSn: undefined,
        name: undefined,
        sort: '+id'
      },
      goodsSpecOptions : [], // add by pengxb @2018-05-10 14:00
      disabled: {
        goodsName: true,
        retailPrice: true,
        btnCreateData: true
      },
      selectGoodsProduct: [], // 商品已经存在的产品信息 add by pengxb
      selectProductSpecName: [] // 已存在商品的规格名称 add by pengxb
    }
  },
  created() {
    this.getList()
  },
  methods: {

    objectSpanMethod({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 0 || columnIndex === 1) {
        return this.spanInfo[rowIndex];
      }
    },
    getList() {
      this.listLoading = true
      listProduct(this.listQuery).then(response => {
        this.list = response.data.data.items
        this.total = response.data.data.total
        this.spanInfo = response.data.data.spanInfo
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
        goodsId: undefined,
        goodsSpecificationIds: [],
        goodsNumber: 0,
        retailPrice: 0,
        url: undefined,
        goodsName: undefined, // add by pengxb @2018-05-07 14:11
        goodsSpec: ""  // add by pengxb @2018-05-07 15:32
      }
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
    handleCreate() {
      this.resetForm()
      this.goodsSpecOptions = []
      this.selectProductSpecName = []
      this.selectGoodsProduct = []

      this.createDialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createProduct(this.dataForm).then(response => {
            this.getList()
            this.createDialogFormVisible = false
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
      this.editDialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateProduct(this.dataForm).then(() => {
            for (const v of this.list) {
              if (v.id === this.dataForm.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, this.dataForm)
                break
              }
            }
            this.editDialogFormVisible = false
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
      deleteProduct(row).then(response => {
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
        const index = this.list.indexOf(row)
        this.spanInfo.splice(index, 1) // 删除合并的行信息 add by pengxb @2018-05-15 11:24
        this.list.splice(index, 1)
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['货品ID', '商品ID', '商品规格ID列表', '货品数量', '货品价格', '货品图片']
        const filterVal = ['id', 'goodsId', 'goodsSpecificationIds', 'goodsNumber', 'retailPrice', 'url']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '货品信息')
        this.downloadLoading = false
      })
    },
    // add by pengxb @2018-05-07 17:41
    getGoodsList() {
      this.listLoading = true
      listGoods(this.goodsListQuery).then(response => {
        this.goodsList = response.data.data.items
        this.goodsTotal = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.goodsList = []
        this.goodsTotal = 0
        this.listLoading = false
      })
    },
    // add by pengxb @2018-05-08 17:40
    handleGoodsFilter() {
      this.goodsListQuery.page = 1
      this.getGoodsList()
    },
    // add by pengxb @2018-05-08 17:40
    handleGoodsSizeChange(val) {
      this.goodsListQuery.limit = val
      this.getGoodsList()
    },
    // add by pengxb @2018-05-08 17:40
    handleGoodsCurrentChange(val) {
      this.goodsListQuery.page = val
      this.getGoodsList()
    },
    // add by pengxb @2018-05-08 17:40
    handleSelectGoods() {
      this.getGoodsList()
      this.dialogGoodsSelectVisible = true
    },
    // add by pengxb @2018-05-08 17:40
    selectGoods(row){
      //var data = Object.assign({}, row);
      //this.resetForm()

      this.dataForm.goodsId = row.id
      this.dataForm.goodsName = row.name
      this.dataForm.goodsNumber = 0
      this.dataForm.retailPrice = 0

      this.dialogGoodsSelectVisible = false
      this.disabled.retailPrice = true
      this.disabled.goodsName = true
      this.disabled.btnCreateData = true

      // 获取规格列表
      this.goodsSpecOptions = []
      let goodsId = this.dataForm.goodsId
      listSpecGroup({goodsId : goodsId}).then(response => {
        this.goodsSpecOptions = response.data.data.items
      }).catch(() => {
        this.goodsSpecOptions = []
      })

      // 获取已存在货品的规格列表
      this.selectGoodsProduct = []
      this.selectProductSpecName = []
      listProductSpec({goodsId: goodsId}).then(response => {
        if(response.data.data.items){
          this.selectGoodsProduct = response.data.data.items
          this.selectProductSpecName = response.data.data.itemsName
        }
      }).catch(() => {
        this.selectGoodsProduct = []
      })
    },
    // add by pengxb
    handlePrimaryPicSuccess(res, file) {
      //let url = file.response.data.url; // 获取 response 返回的图片url
      //this.dataForm.picUrl = URL.createObjectURL(file.raw)
      this.dataForm.picUrl = res.data.url;
    },
    // add by pengxb
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
    // add by pengxb
    goodsSpecSelectChange(value) {
      // this.dataForm.goodsSpecificationIds = Array.sort(value);
      /** 把当选中项所在分组的其他选项禁用 */
      for(let i = 0; i < this.goodsSpecOptions.length; i++) {

        let isDisabled = false
        let options = this.goodsSpecOptions[i].options
        let v /** 选择项的值,所在分组除了该值选项外其他项都禁用 */

        for(let j = 0; j < options.length; j++) {
          let item = options[j];

          if(!isDisabled){
            for(let k = 0; k < value.length; k++){
              if(item.value == value[k]) {
                j = -1
                v = value[k]
                isDisabled = true
                break
              }
            }
          }

          if(isDisabled == true) {
            if(item.value != v){
              item.disabled = true
            } else {
              item.disabled = false
            }

            continue
          } else {
            item.disabled = false
          }
        }
      }
      /** end */

      // 判断是否存在该规格的货品
      if(value.length === 0) {
        this.disabled.retailPrice = true
        this.disabled.goodsName = true
        this.disabled.btnCreateData = true

        return ;
      }

      for(let i = 0; i < this.selectGoodsProduct.length; i++){
        if(this.selectGoodsProduct[i].equals(Array.sort(value))){
          this.$message.error('已存在该规格货品,请更换规格项')
          this.disabled.retailPrice = true
          this.disabled.goodsName = true
          this.disabled.btnCreateData = true
          return ;
        }
      }

      this.disabled.retailPrice = false
      this.disabled.goodsName = false
      this.disabled.btnCreateData = false
    }
  }
}

/**
 * 添加数组比较是否相等功能 [].equals([]) : true 表示相等, false 表示不相等, 比较与元素的次序无关
 * add by pengxb
 */
// Warn if overriding existing method
if (Array.prototype.equals)
       console.warn("Overriding existing Array.prototype.equals. Possible causes: New API defines the method, there's a framework conflict or you've got double inclusions in your code.");
// attach the .equals method to Array's prototype to call it on any array
Array.prototype.equals = function (array) {
  // if the other array is a falsy value, return
  if (!array)
    return false;
  // compare lengths - can save a lot of time
  if (this.length != array.length)
    return false;

  for (var i = 0, l = this.length; i < l; i++) {
    // Check if we have nested arrays
    if (this[i] instanceof Array && array[i] instanceof Array) {
      // recurse into the nested arrays
      if (!this[i].equals(array[i]))
        return false;
    } else if (this[i] != array[i]) {
      // Warning - two different object instances will never be equal: {x:20} != {x:20}
      return false;
    }
  }
  return true;
}
// Hide method from for-in loops
Object.defineProperty(Array.prototype, "equals", {enumerable: false});
/** end 数据比较功能 **/


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
