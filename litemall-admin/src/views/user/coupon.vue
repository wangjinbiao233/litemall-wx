<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入优惠券名称" v-model="listQuery.discountName">
      </el-input>
      <el-select clearable class="filter-item" v-model="listQuery.discountType" placeholder="请选择优惠券类别" >
        <el-option
          v-for="item in couponsTypeList"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate" icon="el-icon-edit">添加</el-button>
      <el-button class="filter-item" type="primary" :loading="downloadLoading" v-waves icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column align="center" width="200px" label="优惠券key" prop="key" sortable>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="名称" prop="discountName">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="优惠券类别" prop="discountTypeStr">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="剩余/总数" prop="">
        <template slot-scope="scope">
          {{ scope.row.discountCount - scope.row.getCount }}/{{ scope.row.discountCount }}
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="开始时间" prop="startTimeStr">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="结束时间" prop="endTimeStr">
      </el-table-column>

      <el-table-column align="center" label="操作" width="250" class-name="small-padding fixed-width">
      <template slot-scope="scope">
      <el-button type="primary" @click="handleUpdate(scope.row)">编辑</el-button>
      <el-button type="primary" @click="downloadImg(scope.row)">下载二维码</el-button>
      <!--<el-button type="danger" size="mini"  @click="handleDelete(scope.row)">删除</el-button>-->
      </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.page"
                     :page-sizes="[10,20,30,50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 添加对话框 -->
    <el-dialog title="添加优惠券信息" :visible.sync="createDialogVisible">
      <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>
        <el-form-item label="优惠券名称" prop="">
          <el-input v-model="dataForm.discountName"></el-input>
        </el-form-item>
        <el-form-item label="优惠券类别" prop="">
          <el-select clearable v-model="dataForm.discountType" placeholder="请选择优惠券类别" style = "top: -4px;">
            <el-option
              v-for="item in couponsTypeList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="优惠金额" prop="" >
          <el-input-number v-model="dataForm.discountsPrice" controls-position="right" :min="0" :max="10000"></el-input-number>
          <!--<el-input v-model="dataForm.discountsPrice"></el-input>-->
        </el-form-item>
        <el-form-item label="限制金额" prop="">
          <el-input-number v-model="dataForm.limitPrice" controls-position="right" :min="0" :max="10000"></el-input-number>
          <!--<el-input v-model="dataForm.limitPrice"></el-input>-->
        </el-form-item>
        <el-form-item label="开始时间" prop="">
          <el-date-picker v-model="dataForm.startTimeStr" type="date" placeholder="选择日期" value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="">
          <el-date-picker v-model="dataForm.endTimeStr" type="date" placeholder="选择日期" value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="优惠券数量" prop="">
          <el-input-number v-model="dataForm.discountCount" controls-position="right" :min="1" :max="10000"></el-input-number>
          <!--<el-input v-model="dataForm.discountCount"></el-input>-->
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="createData":disabled="isDisable">确定</el-button>
      </div>
    </el-dialog>

    <!-- 修改对话框 -->
    <el-dialog title="修改优惠券信息" :visible.sync="updateDialogVisible">
      <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>
        <el-form-item label="名称" prop="">
          <el-input v-model="dataForm.discountName" readonly></el-input>
        </el-form-item>
        <el-form-item label="优惠券Key" prop="">
          <el-input v-model="dataForm.key" readonly></el-input>
        </el-form-item>
        <el-form-item label="开始时间" prop="">
          <el-date-picker v-model="dataForm.startTimeStr" type="date" placeholder="选择日期" value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="">
          <el-date-picker v-model="dataForm.endTimeStr" type="date" placeholder="选择日期" value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import { couponList ,CreateCoupon , updateCoupon , getDictionaryTypeList } from '@/api/coupon'
  import waves from '@/directive/waves' // 水波纹指令
  import axios from 'axios'

  export default {
    name: 'Storage',
    directives: {
      waves
    },
    data() {
      return {
        list: null,
        total: null,
        listLoading: true,
        isDisable:false,
        listQuery: {
          page: 1,
          limit: 20,
          key: undefined,
          name: undefined,
          discountType: undefined,
          sort: '+id'
        },
        createDialogVisible: false,
        dataForm: {
          id: undefined,
          key: undefined,
          discountName: undefined,
          discountsPrice: undefined,
          limitPrice: undefined,
          startTimeStr: undefined,
          endTimeStr: undefined,
          discountCount: undefined,
          discountType: undefined
        },
        updateDialogVisible: false,
        rules: {
          name: [{ required: true, message: '对象名称不能为空', trigger: 'blur' }],
          discountsPrice: [{ required: true,type: 'number', message: '年龄必须为数字值' }],
          limitPrice: [{ required: true,type: 'number', message: '年龄必须为数字值' }]
        },
        downloadLoading: false,
        couponsTypeList: undefined
      }
    },
    created() {
      this.getList()
      this.getCouponsTypeList()
    },
    methods: {
      getList() {
        this.listLoading = true
        couponList(this.listQuery).then(response => {
          this.list = response.data.data.items
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
          this.list = []
        this.total = 0
        this.listLoading = false
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
          key: undefined,
          discountName: undefined,
          discountsPrice: undefined,
          limitPrice: undefined,
          startTimeStr: undefined,
          endTimeStr: undefined,
          discountCount: undefined
        }
      },
      handleCreate() {
        this.resetForm();
        this.listQuery.page = 1
        this.getList();
        this.createDialogVisible = true
      },
      handleUpload(item) {
        const formData = new FormData()
        formData.append('file', item.file)
        createStorage(formData).then(response => {
          this.list.unshift(response.data.data)
        this.createDialogVisible = false
        this.$notify({
          title: '成功',
          message: '创建成功',
          type: 'success',
          duration: 2000
        })
      }).catch(() => {
          this.$message.error('失败')
      })
      },
      //修改
      handleUpdate(row) {
        this.dataForm = Object.assign({}, row)
        this.updateDialogVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
      })
      },
      //修改保存
      updateData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            if(this.dataForm.endTimeStr < this.dataForm.startTimeStr){
              this.$message.error('结束时间不能小于开始时间')
              return;
            }

            updateCoupon(this.dataForm).then(() => {
              for (const v of this.list) {
              if (v.id === this.dataForm.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, this.dataForm)
                break
              }
            }
            //重新查询list 开始======
            this.getList()
            //重新查询list 结束======
            this.updateDialogVisible = false
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
      createData() {
        this.isDisable = true
           setTimeout(() => {
               this.isDisable = false
           }, 1000)
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {

            if(this.dataForm.endTimeStr < this.dataForm.startTimeStr){
              this.$message.error('结束时间不能小于开始时间')
              return;
            }
            CreateCoupon(this.dataForm).then(response => {
              //this.list.unshift(response.data.data)
              this.createDialogVisible = false
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
      handleDelete(row) {
        deleteStorage({
          id:row.id
        }).then(response => {
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
          const tHeader = ['ID', '对象KEY', '对象名称', '对象类型', '对象大小', '访问链接']
          const filterVal = ['id', 'key', 'oldName', 'type', 'size', 'url']
          excel.export_json_to_excel2(tHeader, this.list, filterVal, '对象存储信息')
        this.downloadLoading = false
      })
      },

      downloadImg(row){
        //alert(process.env.BASE_API)
        let baseurl =  process.env.BASE_API
        let url = baseurl+'/discount/downloadCodeImg?id=&keys='+row.key;
        window.location.href = url

      }
    }
  }
</script>
