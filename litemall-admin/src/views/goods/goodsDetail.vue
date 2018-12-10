<template>
    <div class="goodsDetail">
        <p>{{name}} - 商品详情</p>
      <el-tabs type="border-card" @tab-click="handleClick">

        <el-tab-pane label="基本信息" name="0" >
          <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>
            <el-form-item label="商品编号" prop="goodsSn">
              <el-input v-model="dataForm.goodsSn" :disabled="true"></el-input>
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

            <el-form-item label="疗程数" v-show="treatmentNumVisible">
              <el-input v-model="dataForm.treatmentNum"></el-input>
            </el-form-item>

            <el-form-item label="商品单位">
              <el-input v-model="dataForm.goodsUnit"></el-input>
            </el-form-item>

            <el-form-item label="关键字">
              <el-input v-model="dataForm.keywords"></el-input>
            </el-form-item>

            <el-form-item label="类目">
              <el-select clearable v-model="dataForm.categoryId" filterable placeholder="请选择">
                <el-option
                  v-for="item in categoryOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>

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

            <el-form-item label="一级分销" prop="firstProfit">
              <el-input-number :precision="2" :step="0.01" :min="0" :max="1" v-model="dataForm.firstProfit"></el-input-number>
            </el-form-item>

            <el-form-item label="二级分销" prop="secondProfit">
              <el-input-number :precision="2" :step="0.01" :min="0" :max="1" v-model="dataForm.secondProfit"></el-input-number>
            </el-form-item>

            <el-form-item label="所属门店"  v-show="isservicegoods">
              <div v-for='(store, index) in storeList' :key="index">
                <!--判断storeIds是否包含当前store，dataForm.storeIds.indexOf(store.id)>=0返回包含store的id, 若不包含则返回-1-->
                <input type='checkbox' :checked="ischeckedStore(store.id)" @click='checkedAgStore(store.id)'> {{store.storeName}}
              </div>
            </el-form-item>

          </el-form>
        </el-tab-pane>

        <el-tab-pane label="商品图片" name="1" >
          <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='margin-left:50px;'>
            <el-form-item label="首页主图">
              <el-upload
                class="avatar-uploader"
                action="/admin/storage/create"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload">
                <img v-if="dataForm.primaryPicUrl" :src="dataForm.primaryPicUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <span class="imgspefi">上传图片规格：165*192 图片宽度扩大两倍，高度等比例扩大 </span>
              <span class="imgspefi">说明：首页中的商品图片</span>
            </el-form-item>

            <el-form-item label="商品主图">
              <el-upload
                class="avatar-uploader"
                action="/admin/storage/create"
                :show-file-list="false"
                :on-success="handlePrimaryPicSuccess"
                :before-upload="beforePrimaryPicUpload">
                <img v-if="dataForm.listPicUrl" :src="dataForm.listPicUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <span class="imgspefi">上传图片规格：165*192 图片宽度扩大两倍，高度等比例扩大 </span>
              <span class="imgspefi">说明：商城中的商品图片</span>
            </el-form-item>


            <el-form-item style="width: 800px;" label="宣传画廊">
              <el-upload
                class="avatar-uploader"
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
              <span class="imgspefi">上传图片规格：375*225 图片宽度扩大两倍，高度等比例扩大 </span>
            </el-form-item>

          </el-form>
        </el-tab-pane>

        <el-tab-pane label="商品介绍" name="2" >
          <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='width: 400px; margin-left:50px;'>
            <el-form-item label="商品介绍">
              <el-input v-model="dataForm.goodsBrief" style="width: 800px;"></el-input>
            </el-form-item>

            <el-form-item style="width: 900px;" label="商品详细介绍">
              <tinymce v-model="dataForm.goodsDesc" ></tinymce>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="商品参数" name="3" >
          <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style=' margin-left:50px;'>

            <el-row :gutter="20" v-for="(item, index) in goodsAttributes">
              <el-col :span="10">
                <el-form-item label="商品参数名" prop="attribute">
                  <el-input v-model="item.attribute"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item label="商品参数值" prop="value">
                  <el-input v-model="item.value"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4" style="padding-top: 5px;">
                <el-button class="btn-op" icon="el-icon-circle-plus-outline" @click="handleAddGoodsAboutItem(index, '1')" circle></el-button>
                <el-button class="btn-op" type="danger" icon="el-icon-remove-outline" @click="" circle @click="handleDelGoodsAboutItem(index, '1')"></el-button>
              </el-col>
            </el-row>

          </el-form>
        </el-tab-pane>

        <el-tab-pane label="商品规格" name="4" >

          <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style='margin-left:50px;'>

            <el-row :gutter="20" v-for="(item, index) in goodsSpecifications">
              <el-col :span="10">
                <el-form-item label="商品规格名" prop="specification">
                  <el-input v-model="item.specification"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item label="商品规格值" prop="value">
                  <el-input v-model="item.value"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4" style="padding-top: 5px;">
                <el-button class="btn-op" icon="el-icon-circle-plus-outline" circle @click="handleAddGoodsAboutItem(index, '2')"></el-button>
                <el-button class="btn-op" type="danger" icon="el-icon-remove-outline" circle @click="handleDelGoodsAboutItem(index, '2')"></el-button>
              </el-col>
            </el-row>

          </el-form>
        </el-tab-pane>

        <el-tab-pane label="销售价格" name="5" >
          <el-form :rules="rules" ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="80px" style=' margin-left:50px;'>

            <el-row :gutter="5" v-for="(productItem, index) in goodsProducts">
              <el-col :span="6">
                <el-form-item label="规格" label-width="60px" prop="goodsSpecificationIds">
                  <el-select style="width: 100%;" v-model="productItem.goodsSpecificationIds" multiple filterable placeholder="请选择" @click.native="showData()"  @change="goodsSpecSelectChange">
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
              </el-col>

              <el-col :span="5">
                <el-form-item label="货品数量" prop="goodsNumber">
                  <el-input v-model="productItem.goodsNumber"></el-input>
                </el-form-item>
              </el-col>

              <el-col :span="6">
                <el-form-item label="销售价格" prop="retailPrice">
                  <el-input v-model="productItem.retailPrice"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="是否下架" prop="retailPrice">
                  <el-select v-model="productItem.soldout" placeholder="请选择">
                    <el-option label="否" :value="0">
                    </el-option>
                    <el-option label="是" :value="1">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>

              <el-col :span="3" style="padding-top: 5px;">
                <el-button class="btn-op" icon="el-icon-circle-plus-outline" circle @click="handleAddGoodsAboutItem(index, '3')"></el-button>
                <el-button class="btn-op" type="danger" icon="el-icon-remove-outline" circle @click="handleDelGoodsAboutItem(index, '3')"></el-button>
              </el-col>

            </el-row>

          </el-form>
        </el-tab-pane>

        <!-- 谨慎保存 -->
            <el-dialog title="请谨慎保存数据" :visible.sync="reserveTimeUpdateDialog" width="30%" center>

              <div>
                <span class="dialogClass">请谨慎保存数据！ </span>
              </div>

              <div slot="footer" class="dialog-footer">
                <el-button @click="reserveTimeUpdateDialog = false">取消</el-button>
                <el-button type="primary" @click="handleSave()">确定</el-button>
              </div>
            </el-dialog>

        <div style="text-align: right;">
          <el-button type="primary" @click="handleDialog()">保存</el-button>
        </div>
      </el-tabs>
    </div>
</template>

<script>
  import { updateGoods, readGoods } from '@/api/goods'
  import { listCategoryBySubId } from '@/api/category'
  import { listGoodsAttribute, saveGoodsAttribute } from '@/api/goods-attribute'
  import { listGoodsSpecification, saveGoodsSpecification } from '@/api/goods-specification'
  import { listProduct, listSpecGroup, saveProduct } from '@/api/product'
  import { listBrand } from '@/api/brand'
  import { storeDataList } from '@/api/store'
  import waves from '@/directive/waves' // 水波纹指令
  import Tinymce from '@/components/Tinymce'

  export default {
    name: "goodsDetail",
    components: { Tinymce },
    directives: { waves },
    data() {
      return {
        id: undefined,
        name: undefined,
        activeTab: undefined,
        listCategoryQuery: {
          subId :'1005000',
          sort: '+id'
        },
        listBrandQuery: {
          limit: 1000,
          sort: '+id'
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
          goodsUnit: undefined,
          flag: '1',
          firstProfit: undefined,
          secondProfit: undefined,
          storeIds:[]
        },
        typeDate: {
          imgBelongs:"2",
          fileList:[]
        },
        rules: {
          goodsSn: [{ required: true, message: '商品编号不能为空', trigger: 'blur' }],
          name: [{ required: true, message: '商品名称不能为空', trigger: 'blur' }]
        },
        imageUrl: '',
        goodsAttributes: [{
          id: undefined,
          goodsId: undefined,
          attribute: undefined,
          value: undefined
        }],
        goodsSpecifications: [{
          id: undefined,
          goodsId: undefined,
          specification: undefined,
          value: undefined
        }],
        goodsProducts: [{
          id: undefined,
          goodsId: undefined,
          goodsSpecificationIds: [],
          goodsNumber: 0,
          retailPrice: 0
        }],
        categoryOptions : [],
        brandOptions : [],
        goodsSpecOptions : [], // add by pengxb @2018-05-10 14:00
        storeList:[],
        isservicegoods: false,
        treatmentNumVisible: false,
        reserveTimeUpdateDialog: false
      }
    },

    created() {
      let id = this.$route.query.id
      let name = this.$route.query.name

      if(id && name){
        this.id = id
        this.name = name
      }
      this.getGoods()

      // 获取品类列表
      //this.listCategoryQuery.subId = '0'
      listCategoryBySubId(this.listCategoryQuery).then(response => {
        this.categoryOptions = response.data.data.items.map((item) => {
          return {value : item.id, label : item.name}
        })
        this.categoryOptions.push({value: 0, label : '全部'});
      }).catch(() => {
        this.categoryOptions = []
      })

      // 获取品牌信息
      listBrand(this.listBrandQuery).then(response => {
        this.brandOptions = response.data.data.items.map((item) => {
          return {value : item.id, label : item.name}
        })
      }).catch(() => {
        this.brandOptions = []
      })

      // 获取规格列表
      this.goodsSpecOptions = []
      listSpecGroup({goodsId : id}).then(response => {
        this.goodsSpecOptions = response.data.data.items
      }).catch(() => {
        this.goodsSpecOptions = []
      })

      this.getAllStoreList();//查询所有的门店信息

      this.getGoodsAttribute()
      this.getGoodsSpecification()
      this.getGoodsProduct()
    },

    watch: {
      '$route'(to, from) {
        let id = this.$route.query.id
        let name = this.$route.query.name

        if(id && name){
          this.id = id
          this.name = name

          this.getGoods()

          // 获取品类列表
          //this.listCategoryQuery.subId = '0'
          listCategoryBySubId(this.listCategoryQuery).then(response => {
            this.categoryOptions = response.data.data.items.map((item) => {
              return {value : item.id, label : item.name}
            })
            this.categoryOptions.push({value: 0, label : '全部'});
          }).catch(() => {
            this.categoryOptions = []
          })

          // 获取品牌信息
          listBrand(this.listBrandQuery).then(response => {
            this.brandOptions = response.data.data.items.map((item) => {
              return {value : item.id, label : item.name}
            })
          }).catch(() => {
            this.brandOptions = []
          })

          // 获取规格列表
          this.goodsSpecOptions = []
          listSpecGroup({goodsId : id}).then(response => {
            this.goodsSpecOptions = response.data.data.items
          }).catch(() => {
            this.goodsSpecOptions = []
          })

          this.getAllStoreList();//查询所有的门店信息

          this.getGoodsAttribute()
          this.getGoodsSpecification()
          this.getGoodsProduct()
        }
      }
    },
    methods: {

      getGoods() {
        let goodId = this.id

        readGoods({id : goodId}).then(response => {
          this.dataForm = response.data.data

          if(response.data.data.flag == 1){
            this.treatmentNumVisible = false;
            this.isservicegoods = false;
          }else{
            this.treatmentNumVisible = true;
            this.isservicegoods = true;
          }

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

        }).catch(() => {

        })
      },

      //获取所有的门店信息
      getAllStoreList(){
        let goodsId = this.id
        storeDataList({goodsId: goodsId}).then(response => {
          this.storeList = response.data.data.allStoreList.map((item) => {
            return { id: item.id+"", storeName : item.storeName}
          })
          let listdatas = response.data.data.goodStoreList
          this.dataForm.storeIds = listdatas.map((item) => {
              return item.storeId+""
          })

        }).catch(() => {
          this.storeList = []
        })
      },

      getGoodsAttribute() {
        let goodsId = this.id
        listGoodsAttribute({goodsId: goodsId, page: 1, limit: 1000}).then(response => {
          let items = response.data.data.items
          if(items.length > 0) {
            this.goodsAttributes = items
          }else{
            this.goodsAttributes =  [{
              id: undefined,
              goodsId: undefined,
              attribute: undefined,
              value: undefined
            }]

          }
        }).catch(() => {

        })
      },

      getGoodsSpecification() {
        let goodsId = this.id
        listGoodsSpecification({goodsId: goodsId, page: 1, limit: 1000}).then(response => {
          let items = response.data.data.items
          if(items.length > 0) {
            this.goodsSpecifications = items
          }else{
            this.goodsSpecifications =  [{
              id: undefined,
              goodsId: undefined,
              specification: undefined,
              value: undefined
            }]

          }
        }).catch(() => {

        })
      },

      getGoodsProduct() {
        let goodsId = this.id
        listProduct({goodsId: goodsId, page: 1, limit: 1000 }).then(response => {
          let items = response.data.data.items
          if(items.length > 0) {
            this.goodsProducts = items
          }else{
            this.goodsProducts =  [{
              id: undefined,
              goodsId: undefined,
              goodsSpecificationIds: [],
              goodsNumber: 0,
              retailPrice: 0
            }]
          }


        }).catch(() => {

        })
      },

      handleClick(tab, e) {
        this.activeTab = tab.index
        if (this.activeTab === '5'){
            // 获取规格列表
          this.goodsSpecOptions = []
          listSpecGroup({goodsId : this.id}).then(response => {
            this.goodsSpecOptions = response.data.data.items
          }).catch(() => {
            this.goodsSpecOptions = []
          })
        }

      },
      showData(){
        this.getGoodsSpecification()
        this.getGoodsProduct()
      },

      handleSave() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            updateGoods(this.dataForm).then(() => {
              let goodsId = this.dataForm.id
              saveGoodsAttribute({goodsId: goodsId, goodsAttributes: this.goodsAttributes}).then(() => {
                this.$notify({
                  title: '成功',
                  message: '商品参数更新成功啦😀',
                  type: 'success',
                  duration: 2000
                })
              })

              saveGoodsSpecification({goodsId: goodsId, goodsSpecifications: this.goodsSpecifications}).then(() => {
                this.getGoodsSpecification()
                this.$notify({
                  title: '成功',
                  message: '商品规格更新成功啦😘',
                  type: 'success',
                  duration: 2000
                })
              })

              saveProduct({goodsId: goodsId, products: this.goodsProducts}).then(() => {
                this.getGoodsProduct()
                this.$notify({
                  title: '成功',
                  message: '货品价格更新成功啦😍',
                  type: 'success',
                  duration: 2000
                })
              })

              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
            })
          }
        })
        this.reserveTimeUpdateDialog = false

      },

      handleDialog(){
        this.reserveTimeUpdateDialog = true
      },

      handleAddGoodsAboutItem(index, type) {

        if(type == '1') { // 操作商品参数
          this.goodsAttributes.splice((index + 1), 0, {
            id: undefined,
            goodsId: undefined,
            attribute: undefined,
            value: undefined
          })
        } else if (type == '2') { // 操作商品规格
          this.goodsSpecifications.splice((index + 1), 0, {
            id: undefined,
            goodsId: undefined,
            specification: undefined,
            value: undefined
          })
        } else if (type == '3') { // 操作货品
          this.goodsProducts.splice((index + 1), 0, {
            id: undefined,
            goodsId: undefined,
            goodsSpecificationIds: [],
            goodsNumber: 0,
            retailPrice: 0,
            soldout: 0
          })
        }
      },

      handleDelGoodsAboutItem(index, type) {

        if(type == '1') { // 操作商品参数
          if(this.goodsAttributes.length === 1) {
            this.$message.error('可以不必填写,但至少保留一项作为操作!')
            return ;
          }
          this.goodsAttributes.splice(index , 1)
        } else if (type == '2') { // 操作商品规格
          if(this.goodsSpecifications.length === 1) {
            this.$message.error('可以不必填写,但至少保留一项作为操作!')
            return ;
          }
          this.goodsSpecifications.splice(index, 1)
        } else if (type == '3') { // 操作货品
          if(this.goodsProducts.length === 1) {
            this.$message.error('可以不必填写,但至少保留一项作为操作!')
            return ;
          }
          this.goodsProducts.splice(index, 1)
        }
      },
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
        // if(value.length === 0) {
        //   this.disabled.retailPrice = true
        //   this.disabled.goodsName = true
        //   this.disabled.btnCreateData = true
        //
        //   return ;
        // }
        //
        // for(let i = 0; i < this.selectGoodsProduct.length; i++){
        //   if(this.selectGoodsProduct[i].equals(Array.sort(value))){
        //     this.$message.error('已存在该规格货品,请更换规格项')
        //     this.disabled.retailPrice = true
        //     this.disabled.goodsName = true
        //     this.disabled.btnCreateData = true
        //     return ;
        //   }
        // }
        //
        // this.disabled.retailPrice = false
        // this.disabled.goodsName = false
        // this.disabled.btnCreateData = false
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
      ,

      // 服务和商品下拉触发事件
      onSelectedFlag(val) {
        if(val == 1){
          this.treatmentNumVisible = false;
        }else{
          this.treatmentNumVisible = true;
        }
      },

      ischeckedStore(storeid){
        let sid = storeid+"";
        var _this = this;
        if(_this.dataForm.storeIds!=undefined){
          let idIndex = _this.dataForm.storeIds.indexOf(sid);
          if(idIndex>=0){
            return true;
          }else{
            return false;
          }
        }else{
           return false;
        }
      },

      //服务类商品选择门店的CheckBox事件
      checkedAgStore(storeid){
        var _this = this;
        let storeidval = storeid+"";
        let index = _this.dataForm.storeIds.indexOf(storeidval);
        if(index>=0){
          //若已经包含了该id，则去除
          _this.dataForm.storeIds.splice(index,1);
        }else{
          //选中该CheckBox
          _this.dataForm.storeIds.push(storeidval);
        }
        //alert("get storeIds =="+_this.dataForm.storeIds);
      }

    }
  }
</script>

<style scoped>
  .goodsDetail{
    padding: 20px 30px;
  }

  .btn-op {
    font-size: 25px;
    padding: 0px;
    border: 2px;
    line-height: 26px;
    border-radius: 100%;
    height: 25px;
  }

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

  .imgspefi{
    color:#F00;
    vertical-align: top;
    line-height: 50px;
    margin-left: 20px;
  }
  .goodsDetail .avatar-uploader{
    display: inline-block;
  }

  .dialogClass{
      vertical-align: top;
      font-size:40px;
      margin-left: 35px;
    }

</style>
