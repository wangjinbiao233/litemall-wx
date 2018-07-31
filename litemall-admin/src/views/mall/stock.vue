<template xmlns:c="http://www.w3.org/1999/html">
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入商品Id" v-model="listQuery.goodsId">
      </el-input>

      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-download" @click="handleDownload" :loading="downloadLoading">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" min-width="100px" label="商品Id" prop="goodsId">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="商品名称" prop="goodsName">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="货品规格" prop="goodsSpec" :formatter="showGoodsSpec">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="货品数量" prop="goodsNumber">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="销售价格" prop="retailPrice">
      </el-table-column>

      <el-table-column align="center" label="操作" width="250" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" @click="handleUpdate(scope.row)">入库</el-button>

        </template>
      </el-table-column>

    </el-table>

    <!-- 入库对话框 -->
    <el-dialog title="入库" :visible.sync="stockUpdateDialog">
      <el-form ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>
        <el-form-item label="货品数量" prop="goodsNumber">
          <el-input v-model="dataForm.goodsNumber"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="stockUpdateDialog = false">取消</el-button>
        <el-button type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>

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
import { listProduct,  updateProduct} from '@/api/product'
import waves from '@/directive/waves' // 水波纹指令

export default {
  name: 'reserve',
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
        username: undefined,
        sort: '+id'
      },
      downloadLoading: false,
      dataForm: {
        id: undefined,
        goodsNumber: undefined
      },
      stockUpdateDialog: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listProduct(this.listQuery).then(response => {
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
    showGoodsSpec(data){
      return data.goodsSpec+ data.specification
    },
    resetForm(row) {
      this.dataForm.id = row.id
      this.dataForm.goodsNumber = row.goodsNumber
    },

    handleUpdate(row) {
      this.resetForm(row)
      this.stockUpdateDialog = true
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateProduct(this.dataForm).then(response => {
            const updateProduct = response.data.data
            for (const v of this.list) {
              if (v.id === updateProduct.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, updateProduct)
                break
              }
            }
            this.getList()
            this.stockUpdateDialog = false
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
