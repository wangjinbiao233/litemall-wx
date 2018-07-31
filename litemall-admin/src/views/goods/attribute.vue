<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入商品ID" v-model="listQuery.goodsId">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate" icon="el-icon-edit">添加</el-button>
      <el-button class="filter-item" type="primary" :loading="downloadLoading" v-waves icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" :span-method="objectSpanMethod" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" min-width="100px" label="商品ID" prop="goodsId">
      </el-table-column>
      <!-- add by pengxb @2018-05-07 10:57 -->
      <el-table-column align="center" min-width="100px" label="商品名称" prop="goodsName">
      </el-table-column>
      <!--
      <el-table-column align="center" width="150px" label="商品参数ID" prop="id" sortable>
      </el-table-column>
      -->

      <el-table-column align="center" min-width="100px" label="商品参数名称" prop="attribute">
      </el-table-column>

      <el-table-column align="center" min-width="200px" label="商品参数值" prop="value">
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
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>
        <el-form-item label="商品ID" prop="goodsId">
          <el-input :style="dialogStatus == 'update' ? 'width: 100%' : 'width: 84%'" v-model="dataForm.goodsId" :disabled="dialogStatus == 'update' ? true : false"></el-input>
          <el-button v-if="dialogStatus=='create'" size="mini" icon="el-icon-edit" circle @click="handleSelectGoods"></el-button>
        </el-form-item>
        <el-form-item label="商品名称" prop="goodsName">
          <el-input v-model="dataForm.goodsName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="商品参数名" prop="attribute">
          <el-input v-model="dataForm.attribute"></el-input>
        </el-form-item>
        <el-form-item label="商品参数值" prop="value">
          <el-input v-model="dataForm.value"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>

    <!-- 商品选择对话框 -->
    <el-dialog :title="textMap['selectGoods']" :visible.sync="dialogGoodsSelectVisible" width="60%">
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

  </div>
</template>

<script>
import { listGoodsAttribute, createGoodsAttribute, updateGoodsAttribute, deleteGoodsAttribute } from '@/api/goods-attribute'
import { listGoods } from '@/api/goods'
import waves from '@/directive/waves' // 水波纹指令

export default {
  name: 'GoodsAttribute',
  directives: {
    waves
  },
  data() {
    return {
      list: null,
      total: null,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        goodsId: undefined,
        sort: '+id'
      },
      dataForm: {
        id: undefined,
        goodsId: undefined,
        attribute: undefined,
        value: undefined,
        goodsName: undefined  // add by pengxb @2018-05-07 10:57
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建',
        selectGoods: '请选择商品' // add by pengxb @2018-05-07 17:38
      },
      rules: {
        goodsId: [{ required: true, message: '商品ID不能为空', trigger: 'blur' }],
        attribute: [{ required: true, message: '商品参数名称不能为空', trigger: 'blur' }],
        value: [{ required: true, message: '商品参数值不能为空', trigger: 'blur' }]
      },
      downloadLoading: false,
      dialogGoodsSelectVisible: false, // add by pengxb @2018-05-07 17:38
      spanInfo: undefined, // add by pengxb @2018-05-07 16:15
      goodsList: null, // add by pengxb @2018-05-07 17:38
      goodsTotal: null, // add by pengxb @2018-05-07 17:38
      goodsListQuery:{  // add by pengxb @2018-05-07 17:38
        page: 1,
        limit: 10,
        goodsSn: undefined,
        name: undefined,
        sort: '+id'
      }
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
      listGoodsAttribute(this.listQuery).then(response => {
        this.list = response.data.data.items
        this.total = response.data.data.total
        this.spanInfo = response.data.data.spanInfo;
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
        attribute: undefined,
        value: undefined
      }
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
          createGoodsAttribute(this.dataForm).then(response => {
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
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateGoodsAttribute(this.dataForm).then(() => {
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
      deleteGoodsAttribute(row).then(response => {
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
        const tHeader = ['商品参数ID', '商品ID', '商品参数名称', '商品参数值']
        const filterVal = ['id', 'goodsId', 'attribute', 'value']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '商品参数信息')
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
      this.dataForm.goodsId = row.id
      this.dataForm.goodsName = row.name

      this.dialogGoodsSelectVisible = false
    }

  }
}
</script>
