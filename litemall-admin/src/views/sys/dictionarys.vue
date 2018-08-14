<template>
  <div class="app-container calendar-list-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input clearable class="filter-item" style="width: 200px;" placeholder="请输入字典名称" v-model="listQuery.name">
      </el-input>
      <el-select clearable class="filter-item"  v-model="listQuery.groupCode" placeholder="请选择组名称"  >
        <el-option
          v-for="infos in dictionaryGoupList"
          :key="infos.groupCode"
          :label="infos.groupName"
          :value="infos.groupCode">
        </el-option>
      </el-select>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">查找</el-button>
      <!--
      <router-link ref='tag' :to="{path:'/sys/dictionarysCreate'}">
        <el-button class="filter-item" type="primary" icon="el-icon-edit">添加</el-button>  
      </router-link>
      -->
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table size="small" :data="list" v-loading="listLoading" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column align="center" width="100px" label="字典ID" prop="id" sortable>
      </el-table-column>

      <el-table-column align="center" min-width="180px" label="组编号" prop="groupCode">
      </el-table-column>
      <el-table-column align="center" min-width="180px" label="组名称" prop="groupName">
      </el-table-column>

      <el-table-column align="center" min-width="200px" label="字典名称" prop="name">
      </el-table-column>

      <el-table-column align="center" min-width="260px" label="字典值" prop="value">
      </el-table-column>      

      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
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
        <el-form-item label="组编号" prop="groupCode">
          <el-input v-model="dataForm.groupCode" ></el-input>
        </el-form-item>
        <el-form-item label="组名称" prop="groupName">
          <el-input v-model="dataForm.groupName" ></el-input>
        </el-form-item>        
        <el-form-item label="字典名称" prop="name">
          <el-input v-model="dataForm.name"></el-input>
        </el-form-item>
        <el-form-item label="字典值" prop="value">
          <el-input v-model="dataForm.value" type="textarea" :rows="1" ></el-input>
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
import { listDictionary, getDictionaryGoupList, deleteDictionary, updateDictionary, createDictionary } from '@/api/dictionarys'
import waves from '@/directive/waves' // 水波纹指令

export default {
  name: 'Dictionary',
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
        limit: 20,
        name: undefined,
        groupCode: undefined,
        sort: '+id'
      },
      dictionaryGoupList: undefined,

      dataForm: {
        id: undefined,
        groupCode : undefined,
        groupName: undefined,
        name: undefined,
        value: undefined,
        seqNo: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        groupCode: [{ required: true, message: '组编号不能为空', trigger: 'blur' }],
        groupName: [{ required: true, message: '组名称不能为空', trigger: 'blur' }],
        name: [{ required: true, message: '字典名称不能为空', trigger: 'blur' }],
        value: [{ required: true, message: '字典值不能为空', trigger: 'blur' }]
      }
      
    }
  },
  created() {
    this.getList()
    this.getDictionaryGoupList()
  },
  methods: {

    getList() {
      listDictionary(this.listQuery).then(response => {
        this.list = response.data.data.items
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },
    getDictionaryGoupList() {
      getDictionaryGoupList().then(response => {
        this.dictionaryGoupList = response.data.data.items        
      }).catch(() => {
        this.dictionaryGoupList = []       
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
        groupCode : undefined,
        groupName: undefined,
        name: undefined,
        value: undefined,
        seqNo: undefined
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
          createDictionary(this.dataForm).then(response => {
            //this.list.unshift(response.data.data)
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
            this.getDictionaryGoupList()
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
          updateDictionary(this.dataForm).then(() => {
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
            //this.getList()
            this.getDictionaryGoupList()
          })
        }
      })
    },
    handleDelete(row) {
      deleteDictionary(row).then(response => {
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
        const index = this.list.indexOf(row)
        this.list.splice(index, 1)
        this.getDictionaryGoupList()
      })
    }

  }
}
</script>
