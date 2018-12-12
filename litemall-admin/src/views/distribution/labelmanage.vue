<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="userDetail">
      <div class="user-info">
        <div class="user-face">
          <img :src="userDataForm.avatar" style="width: 50px;height: 50px;border-radius: 50%"/>
        </div>
        <div class="user-msg">
          <p>
            <span class="user-msg-item">{{userDataForm.username}}</span>
          </p>
        </div>
      </div>
    </div>
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入标签名称" v-model="listQuery.labelName">
      </el-input>

      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate" icon="el-icon-edit">添加</el-button>
      <el-button class="filter-item" type="primary" :loading="downloadLoading" v-waves icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="labelList" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <!--<el-table-column align="center" width="50px" label="ID" prop="id" >
      </el-table-column>-->
      <el-table-column type="index" label="序号" header-align="center" align="center">
      </el-table-column>

      <el-table-column align="center" min-width="200px" label="二维码" prop="qrcodeUrl">
        <template slot-scope="scope" v-if="scope.row.qrcodeUrl">
          <a :href="scope.row.qrcodeUrl" target="_blank" class="buttonText">查看二维码</a>
          <a :href="scope.row.qrcodeUrl" :download="scope.row.qrcodeUrl" >下载二维码</a>
        </template>
      </el-table-column>

      <el-table-column align="center" min-width="190px" label="标签名称" prop="labelName">
      </el-table-column>

      <el-table-column align="center" min-width="200px" label="标签描述" prop="labelDesc">
      </el-table-column>

      <el-table-column align="center" min-width="190px" label="创建时间" prop="createTime">
      </el-table-column>

      <el-table-column align="left" label="操作" width="300px" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button type="danger"  size="small" @click="handleDelete(scope.row)">删除</el-button>
          <el-button type="success" size="small" v-if="scope.row.qrcodeUrl== null "  @click="handleQrcode(scope.row)">生成二维码</el-button>
          <!--<el-button type="primary" size="small" @click="showDistriUser(scope.row)">分销用户</el-button>-->
          <!--<router-link ref='tag' v-if="scope.row.qrcodeUrl" :to="{path:'/user/userDetail',query: {id: scope.row.userId}}">
            <el-button type="primary" size="small" style="width: inherit;">分销客户</el-button>
          </router-link>-->
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
        <el-form-item label="标签名称" prop="labelName">
          <el-input v-model="dataForm.labelName"></el-input>
        </el-form-item>
        <el-form-item label="标签描述" prop="labelDesc" >
          <el-input v-model="dataForm.labelDesc"></el-input>
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

<script>
  import { fetchList, createLabel, updateLabel, removeLabel, createQrcode} from '@/api/label'
  import { readUser } from '@/api/user'
  import waves from '@/directive/waves' // 水波纹指令
  export default {
    name: 'labelmanage',
    directives: {
      waves
    },
    data() {
      return {
        id:undefined,
        labelList: undefined,
        list: undefined,
        total: undefined,
        listLoading: true,
        listQuery: {
          userId:undefined,
          labelName:undefined,
          page: 1,
          limit: 20,
          sort: '+id'
        },
        dialogFormVisible: false,
        dialogStatus: '',
        textMap:{
          update: '编辑标签',
          create: '创建标签'

        },
        dataForm: {
          id:undefined,
          userId: undefined,
          labelName: undefined,
          labelDesc: undefined
        },
        userDataForm: {
          id: undefined,
          username: '',
        },
        rules: {
          labelName: [{ required: true, message: '标签内容不能为空', trigger: 'blur' }]
        },
        downloadLoading: false,
      }
    },

    methods: {
      getList() {
        this.listLoading = true
        fetchList(this.listQuery).then(response => {
          this.labelList = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.labelList = []
          this.total = 0
          this.listLoading = false
        })
      },

      handleFilter() {
        this.listQuery.page = 1
        this.getList()
      },

      handleSizeChange(val) {
        this.userlistQuery.limit = val
        this.getList()
      },
      handleCurrentChange(val) {
        this.listQuery.page = val
        this.getList()
      },

      resetForm() {
        this.dataForm = {
          id:undefined,
          labelName: undefined,
          labelDesc: undefined
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
        if(this.id){
          this.dataForm.userId = this.id
          this.$refs['dataForm'].validate((valid) => {
            if (valid) {
              createLabel(this.dataForm).then(response => {
                this.listQuery.page = 1
                this.dialogFormVisible = false
                this.getList()
                this.$notify({
                  title: '成功',
                  message: '创建成功',
                  type: 'success',
                  duration: 2000
                })
              }).catch(() => {
                this.$notify.error({
                  title: '失败',
                  message: '创建失败',
                  duration: 2000
                })
              })
            }
          })
        } else {
          this.dialogFormVisible = false
          this.$notify.error({
            title: '创建失败',
            message: '请关闭该页面重新打开',
            duration: 2000
          })
        }
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
            updateLabel(this.dataForm).then(response => {
            this.dialogFormVisible = false
              this.getList()
              this.$notify({
                title: '成功',
                message: '修改成功',
                type: 'success',
                duration: 2000
              })
          })
          }
        })
      },

      handleDelete(row) {
        this.$confirm('确认删除吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {

          removeLabel(row).then(response => {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            const index = this.labelList.indexOf(row)
            this.labelList.splice(index, 1)
          })
        }).catch(() => {

        })
      },

      //查看二维码
      handleQrcode(row){

        createQrcode(row).then(response => {
          debugger
          this.getList()
          this.$notify({
            title: '成功',
            message: '创建成功',
            type: 'success',
            duration: 2000
          })
        }).catch(() => {
          this.$notify.error({
            title: '失败',
            message: '创建失败',
            duration: 2000
          })
        })
      },

      handleDownload() {
        this.downloadLoading = true
        import('@/vendor/Export2Excel').then(excel => {
          const tHeader = ['ID', '标签名称', '标签描述', '二维码','创建时间']
          const filterVal = ['id', 'labelName', 'labelDesc', 'qrcodeUrl','createTime']
          excel.export_json_to_excel2(tHeader, this.labelList, filterVal, '标签信息')
          this.downloadLoading = false
        })
      }
    },
    watch: {
      '$route'(to, from) {
        var id = this.$route.query.id
        debugger
        if (id) {
          this.id = id
          this.listQuery.userId = id
          fetchList(this.listQuery).then(response => {
            this.labelList = response.data.data.items
            this.total = response.data.data.total
            this.listLoading = false
          }).catch(() => {
            this.labelList = []
            this.total = 0
            this.listLoading = false
          })

          readUser({ 'id': id }).then(response => {
            this.userDataForm = response.data.data.items
            this.$nextTick(() => {
              this.$refs['userDataForm'].clearValidate()
            })
          })

        }
      }
    },
    created() {
      var id = this.$route.query.id
      if (id) {
        this.id = id
        this.listQuery.userId = id
        this.getList()

        readUser({ 'id': id }).then(response => {
          this.userDataForm = response.data.data.items
          this.$nextTick(() => {
            this.$refs['userDataForm'].clearValidate()
          })
        })
      }
    }
  }
</script>
<style>
  .userDetail .user-info{
    display: flex;
    align-items: center;

  }
  .userDetail .user-msg{
    padding-left: 10px;
    font-size: 14px;

  }
  .userDetail .user-msg-item{
    font-size: 16px;
    color: #409eff;
    display: inline-block;
  }
</style>
