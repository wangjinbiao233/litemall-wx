<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入用户名称" v-model="listQuery.username">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate" icon="el-icon-edit">添加</el-button>
      <el-button class="filter-item" type="primary" :loading="downloadLoading" v-waves icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column align="center" width="100px" label="用户ID" prop="id" sortable>
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="用户名称" prop="username">
      </el-table-column>

      <el-table-column align="center" min-width="100px" label="用户头像" prop="avatar">
      </el-table-column>

      <el-table-column align="center" label="操作" width="450px" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row, false)">编辑</el-button>
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row, true)">角色</el-button>
          <el-button type="danger" size="mini"  @click="handleDelete(scope.row)">删除</el-button>
          <el-button type="primary" size="mini" style="padding-left: 7px"  @click="setStore(scope.row)">添加门店</el-button>
          <el-button type="primary" size="mini" style="padding-left: 6px"  @click="editStore(scope.row)">编辑门店</el-button>
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
      <template v-if="!dialogFlag">
        <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>
          <el-form-item label="用户名称" prop="username">
            <el-input v-model="dataForm.username"></el-input>
          </el-form-item>
          <el-form-item label="用户密码" prop="password">
            <el-input type="password" v-model="dataForm.password"  auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkPassword">
            <el-input type="password" v-model="dataForm.checkPassword" auto-complete="off"></el-input>
          </el-form-item>
          <!--<el-form-item label="用户头像" prop="avatar">-->
          <!--<el-input v-model="dataForm.avatar"></el-input>-->
          <!--<el-upload action="#" list-type="picture" :show-file-list="false" :limit="1" :http-request="uploadAvatar">-->
          <!--<el-button size="small" type="primary">点击上传</el-button>-->
          <!--</el-upload>-->
          <!--</el-form-item>-->
          <el-form-item label="用户头像" prop="avatar">
            <el-upload
              class="avatar-uploader"
              action="/admin/storage/create"
              :show-file-list="false"
              :data="typeDate"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <img v-if="imageUrl" :src="imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
        </el-form>
      </template>
      <template v-else>
        <el-form status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>
          <el-form-item label="用户角色">
            <el-select v-model="role" multiple placeholder="请选择">
              <el-option
                v-for="item in roleList"
                :key="item.id"
                :label="item.roleName"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </template>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <template v-if="!dialogFlag">
          <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
          <el-button v-else type="primary" @click="updateData">确定</el-button>
        </template>
        <template v-if="dialogFlag">
          <el-button  type="primary" @click="updateUserRoles">确定</el-button>
        </template>
      </div>
    </el-dialog>

    <!--分配门店-->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogStoreVisible">

      <div class="filter-container">
        <el-input clearable class="filter-item" style="width: 200px;" placeholder="请填写门店名称" v-model="storelistQuery.storeName">
        </el-input>

        <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleStoreFilter">查找</el-button>

      </div>

      <!-- 查询结果 -->
      <el-table size="small" :data="storeList"
                v-loading="listLoading"
                ref="multipleTable"
                @selection-change="handleDocSelectionChange"
                element-loading-text="正在查询中。。。" border fit highlight-current-row>

        <el-table-column type="selection" width="55">
        </el-table-column>

        <el-table-column align="center" width="100px" label="门店ID" prop="id" sortable v-if='show' >
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="门店名称" prop="storeName">
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="门店地址" prop="storeAddress">
        </el-table-column>



      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination background @size-change="handleStoreSizeChange" @current-change="handleStoreCurrentChange" :current-page="storelistQuery.page"
                       :page-sizes="[10,20,30,50]" :page-size="storelistQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="storeTotal">
        </el-pagination>
      </div>


      <div  slot="footer" class="dialog-footer">
        <el-button @click="dialogStoreVisible = false">取消</el-button>
        <el-button  type="primary" @click="handleStore">添加</el-button>
      </div>
    </el-dialog>

    <!--编辑门店-->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogStoreEditVisible">

      <div class="filter-container">
        <el-input clearable class="filter-item" style="width: 200px;" placeholder="请填写门店名称" v-model="storelistQuery.storeName">
        </el-input>

        <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleStoreFilter">查找</el-button>

      </div>

      <!-- 查询结果 -->
      <el-table size="small" :data="storeList"
                v-loading="listLoading"
                ref="multipleTable"
                @selection-change="handleDocSelectionChange"
                element-loading-text="正在查询中。。。" border fit highlight-current-row>

        <el-table-column type="selection" width="55">
        </el-table-column>

        <el-table-column align="center" width="100px" label="门店ID" prop="id" sortable v-if='show' >
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="门店名称" prop="storeName">
        </el-table-column>

        <el-table-column align="center" min-width="100px" label="门店地址" prop="storeAddress">
      </el-table-column>

        <el-table-column align="center" min-width="100px" label="门店地址" prop="storeAddress">
        </el-table-column>

        <el-table-column align="center" label="操作" width="80" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button type="danger" size="mini"  @click="handleDeleteStore(scope.row)">删除</el-button>
          </template>
        </el-table-column>



      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination background @size-change="handleStoreSizeChange" @current-change="handleStoreCurrentChange" :current-page="storelistQuery.page"
                       :page-sizes="[10,20,30,50]" :page-size="storelistQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="storeTotal">
        </el-pagination>
      </div>


      <div  slot="footer" class="dialog-footer">
        <el-button @click="dialogStoreEditVisible = false">取消</el-button>
      </div>
    </el-dialog>


  </div>
</template>

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


<script>
  import { listAdmin, createAdmin, updateAdmin, deleteAdmin } from '@/api/admin'
  import { roleList, selectAdminRole, addAdminRole } from '@/api/role'
  import { createStorage } from '@/api/storage'
  import { listAdminStore, addStore, deleteStoreDoctor} from '@/api/store'
  import waves from '@/directive/waves' // 水波纹指令

  export default {
    name: 'Admin',
    directives: {
      waves
    },
    data() {
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'))
        } else {
          if (this.dataForm.checkPassword !== '') {
            this.$refs.dataForm.validateField('checkPassword')
          }
          callback()
        }
      }
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'))
        } else if (value !== this.dataForm.password) {
          callback(new Error('两次输入密码不一致!'))
        } else {
          callback()
        }
      }
      return {
        list: null,
        storeList: null,
        role: undefined,
        total: null,
        storeTotal: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          username: undefined,
          sort: '+id'
        },
        storeForm:  {
          storeId:  undefined,
          userId: undefined,
          flag:'0'  //'0'代表门用户

        },
        storelistQuery: {
          page: 1,
          limit: 20,
          storename: undefined,
          sflag: undefined,
          userId: '',
          storeId: '',
          userStoreFlag:'1',//用于用户界面筛选出未分配员工的门店
          sort: '+id'
        },
        multipleSelectionStore: [],
        dataForm: {
          id: undefined,
          username: undefined,
          password: undefined,
          checkPassword: undefined,
          avatar: undefined
        },
        typeDate: {
          imgBelongs:"3"
        },
        imageUrl:'',
        dialogFormVisible: false,
        dialogStoreVisible: false,
        dialogStoreEditVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: '创建',
          setStore: '添加门店',
          editStore: '编辑门店'
        },
        rules: {
          username: [{ required: true, message: '用户名称不能为空', trigger: 'blur' }],
          password: [
            { required: true, message: '密码不能为空', trigger: 'blur' },
            { validator: validatePass, trigger: 'blur' }
          ],
          checkPassword: [
            { required: true, message: '密码不能为空', trigger: 'blur' },
            { validator: validatePass2, trigger: 'blur' }
          ]
        },
        downloadLoading: false,
        roleList: [
          {
            value: 1,
            label: 'admin'
          }
        ],
        dialogFlag: false
      }
    },
    created() {
      this.getList()
      this.getRoleList()
    },
    methods: {
      getList() {
        this.listLoading = true
        listAdmin(this.listQuery).then(response => {
        this.list = response.data.data.items
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
          this.list = []
        this.total = 0
        this.listLoading = false
      })
      },

      handleDocSelectionChange(val) {
        this.multipleSelectionStore = ''
        this.multipleSelectionStore = val
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
      handleStoreFilter(){
        this.storelistQuery.page = 1
        this.getStoreList()
      },
      handleStoreSizeChange(val) {
        this.storelistQuery.page = 1
        this.getStoreList()
      },
      handleStoreCurrentChange(val) {
        this.storelistQuery.page = 1
        this.getStoreList()
      },

      resetForm() {
        this.dataForm = {
          id: undefined,
          username: undefined,
          password: undefined,
          checkPassword: undefined,
          avatar: undefined
        }
        this.imageUrl=''
        this.role = []
      },
      uploadAvatar(item) {
        const formData = new FormData()
        formData.append('file', item.file)
        createStorage(formData).then(res => {
          this.dataForm.avatar = res.data.data.url
      }).catch(() => {
          this.$message.error('上传失败，请重新上传')
      })
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
            createAdmin(this.dataForm).then(response => {
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
      handleUpdate(row, type) {
        this.dialogFlag = type
        this.dataForm = Object.assign({}, row)
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
        if (!type) {
          this.$nextTick(() => {
            this.$refs['dataForm'].clearValidate()
        })
        } else {
          this.getUserRoles()
        }
      },
      updateData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            updateAdmin(this.dataForm).then(() => {
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
        deleteAdmin(row).then(response => {
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
          const tHeader = ['用户ID', '用户名称', '用户头像']
          const filterVal = ['id', 'username', 'avatar']
          excel.export_json_to_excel2(tHeader, this.list, filterVal, '用户信息')
        this.downloadLoading = false
      })
      },
      getRoleList() {
        roleList({ page: 1, limit: 100 })
          .then(response => {
          this.roleList = response.data.data.items
      })
      },
      getUserRoles() {
        selectAdminRole({ userId: this.dataForm.id })
          .then(response => {
          var role = response.data.data
          if (role) {
            role = role.map( function(val) {
              return val - 0
            })
          } else {
            role = []
          }
          this.role = role
      })
      },
      handleAvatarSuccess(res, file){
        debugger;
        //this.imageUrl = URL.createObjectURL(file.raw.url);
        this.dataForm.avatar = res.data.url;
        this.imageUrl = res.data.url;
      },
      beforeAvatarUpload(file){
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      },
      //-------------------edit by lb start
      //编辑门店弹出框
      editStore(row) {
        this.resetForm()
        this.storeForm.userId = row.id
        this.storelistQuery.userId = row.id
        this.storelistQuery.sflag = '1'
        this.dialogStatus = 'editStore'
        this.dialogStoreEditVisible = true
        this.getStoreList()
      },


      // 添加门店弹出框
      setStore(row) {
        this.resetForm()
        this.storeForm.userId = row.id
        this.storelistQuery.userId = row.id
        this.storelistQuery.sflag = '0'
        this.dialogStatus = 'setStore'
        this.dialogStoreVisible = true
        this.getStoreList()
      },
      //删除门店医生或者店长
      handleDeleteStore(row) {
        this.storelistQuery.sflag = '1'
        this.storelistQuery.storeId = row.id
        deleteStoreDoctor(this.storelistQuery).then(response => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.handleStoreFilter()
        })
      },

      //查询门店列表
      getStoreList() {
        this.listLoading = true
        listAdminStore(this.storelistQuery).then(response => {
          this.storeList = response.data.data.items
          this.storeTotal = response.data.data.total
          this.listLoading = false
        }).catch(() => {
          this.storeList = []
          this.storeTotal = 0
          this.listLoading = false
        })
      },
      //用户关联门店 获取所有已经勾选的门店
      handleStore() {
        this.storeForm.storeId = ''
        this.multipleSelectionStore.forEach(row =>{
          this.storeForm.storeId = this.storeForm.storeId + row.id + ','
        })
        //请求后台插入数据
        addStore(this.storeForm).then(response => {
          //重新查询list 开始======
     /*     this.listQuery.page = 1
          this.getList()*/
          //重新查询list 结束======
          this.dialogStoreVisible = false
          this.$notify({
            title: '成功',
            message: '创建成功',
            type: 'success',
            duration: 2000
          })
        })
      },

      //--------------------------edit by lb end

      updateUserRoles() {
        addAdminRole({ roleList: this.role.join(','), userId: this.dataForm.id })
          .then(response => {
          this.dialogFormVisible = false
        this.$notify({
          title: '成功',
          message: '更新成功',
          type: 'success',
          duration: 2000
        })
      })
      }
    }
  }
</script>
