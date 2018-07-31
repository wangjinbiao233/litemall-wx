<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入角色名称" v-model="listQuery.roleName">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate" icon="el-icon-edit">添加</el-button>
      </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column align="center" width="100px" label="角色ID" prop="id" sortable>
      </el-table-column>
      <el-table-column align="center" min-width="100px" label="角色名称" prop="roleName">
      </el-table-column>
      <el-table-column align="center" label="操作" width="250" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row, false)">编辑</el-button>
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row, true)">权限</el-button>
          <el-button type="primary" size="mini" @click="handleRemove(scope.row)">删除</el-button>
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
        <template v-if="!dialogMain">
          <el-form-item label="角色名称" prop="roleName">
            <el-input v-model="dataForm.roleName"></el-input>
          </el-form-item>
        </template>
        <template v-else>
          <el-form-item label="角色权限" prop="password">
            <el-tree
              :data="router"
              show-checkbox
              node-key="name"
              ref="tree"
              @check-change="handleCheckChange"
              @node-click="handleRoleNodeClick"
              :props="defaultProps">
            </el-tree>
          </el-form-item>
        </template>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <template v-else>
          <el-button v-if="!dialogMain" type="primary" @click="updateRoleNames">确定</el-button>
          <el-button v-else type="primary" @click="updateRoleList">确定</el-button>
        </template>
      </div>
    </el-dialog>

  </div>
</template>
<script>
import { asyncRouterMap } from '@/router'
import { addRole, roleList, updateRoleName, getRoleMenu, updateRoleMenu, removeRole} from '@/api/role'
// import { listAdmin } from '@/api/admin'
import waves from '@/directive/waves' // 水波纹指令

export default {
  name: 'role',
  directives: {
    waves
  },
  data() {
    return {
      list: null,
      total: null,
      listLoading: true,
      dialogMain: false,
      router: asyncRouterMap,
      currentSlectRole:'',
      listQuery: {
        page: 1,
        limit: 20,
        roleName: ''
      },
      dataForm: {
        roleName: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        roleName: [
          { required: true, message: '角色名称不能为空', trigger: 'blur' }
        ]
      },
      downloadLoading: false,
      data2: [
        {
          id: 1,
          label: '一级 1',
          children: [
            {
              id: 4,
              label: '二级 1-1',
              children: [
                {
                  id: 9,
                  label: '三级 1-1-1'
                },
                {
                  id: 10,
                  label: '三级 1-1-2'
                }
              ]
            }
          ]
        }
      ],
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    }
  },
  created() {
    this.getList()
    //this.router = this.initRouter(this.router)
  },
  methods: {
    getList() {
      this.listLoading = true
      roleList(this.listQuery)
        .then(response => {
          this.list = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        })
        .catch(() => {
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
        role: undefined
      }
      if (this.$refs.tree) {
        this.$refs.tree.setCheckedKeys([])
      }
    },
    handleCreate() {
      this.resetForm()
      this.dialogStatus = 'create'
      this.dialogMain = false
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          addRole(this.dataForm).then(response => {
            this.getList()
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
      if (!type && row.roleName=='管理员'){
        this.$notify({
          title: '提示',
          message: '此角色的角色名称不可以修改',
          type: 'success',
          duration: 2000
        })
        return;
      }
      this.dialogMain = type
      this.dataForm = Object.assign({}, { roleName: row.roleName, id: row.id })      
      this.currentSlectRole = row.roleName
      if (type) {
        this.getRoleList()
        //每次修改一个角色，重新初始化树的初始化数据
        this.router = this.initRouter(this.router)
        
      }
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateRoleNames() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          updateRoleName(this.dataForm).then(() => {
            this.dialogFormVisible = false
            this.getList()
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
    updateRoleList() {
      var roleList = this.$refs.tree.getCheckedKeys().join(',')
      console.log("-=roleList-="+roleList)
      updateRoleMenu({
        roleId: this.dataForm.id,
        menuList: roleList
      }).then(() => {
        this.dialogFormVisible = false
        this.$notify({
          title: '成功',
          message: '更新成功',
          type: 'success',
          duration: 2000
        })
      })
    },
    initRouter(list) {
      var router = []
      list.map((obj) => {
        if (obj.meta) {
          obj.label = obj.meta.title
          //obj.disabled = false;
          if(this.currentSlectRole=="管理员"){        
            if(obj.name=="pad" || obj.name=="doctor"){
              obj.disabled = true          
            }
          }else{
            obj.disabled = false     
          }
          if (obj.children) this.initRouter(obj.children)
          router.push(obj)
        }
      })
      return router
    },
    handleCheckChange(data, checked, indeterminate) {      
           
    },
    handleRoleNodeClick(data){
      console.log(data);      
    },
    
    getRoleList() {
      getRoleMenu({ roleId: this.dataForm.id }).then(response => {
        this.$refs.tree.setCheckedKeys(response.data.data)
      })
    },
    handleRemove(row){
      this.dataForm = Object.assign({}, { roleName: row.roleName, id: row.id })
      removeRole({ roleId: this.dataForm.id }).then(() => {
        this.dialogFormVisible = false
        this.getList()
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
      })
    }
  }
}
</script>

