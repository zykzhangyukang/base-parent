<template>

    <div class="plan">
        <!-- 搜索区域 -->
        <el-form size="small" :inline="true" :model="searchForm" ref="searchForm">

            <el-form-item label="计划编号" prop="planCode">
                <el-input v-model="searchForm.planCode" placeholder="计划编号"></el-input>
            </el-form-item>

            <el-form-item label="计划状态" prop="status">
                <el-select v-model="searchForm.status" placeholder="计划状态" class="w150">
                    <el-option label="全部" value=""></el-option>
                    <el-option v-for="item in planStatusG" :label="planStatusGName[item.code]" :value="item.code" :key="item.code">
                    </el-option>
                </el-select>
            </el-form-item>

            <br/>
            <el-form-item>
                <el-button type="primary"  @click="getData" icon="el-icon-search">查询</el-button>
                <el-button type="warning"  @click="reset" icon="el-icon-refresh">重置</el-button>
                <el-button type="success"  @click="addVisible = true" icon="el-icon-plus">新增</el-button>
                <el-button type="danger"  @click="updateStatus">更新状态</el-button>
            </el-form-item>
        </el-form>


        <!-- 表格部分 -->
        <el-table
                border
                ref="planTable"
                class="planTable"
                size="mini"
                stripe
                v-loading="loading"
                :data="dataList"
                style="width: 100%">
            <el-table-column label="选择" width="50">
                <template slot-scope="{ row }">
                    <el-radio :label="row.uuid" v-model="uuid"><span></span></el-radio>
                </template>
            </el-table-column>
            <el-table-column
                    prop="planCode"
                    label="计划编号"
                    width="300px"
                    align="center"
                   >
                <template slot-scope="scope">
                    <span class="planCode" :underline="false" type="primary"
                             @click="lookContent(scope.row.uuid)">{{scope.row.planCode}}
                    </span>
                </template>
            </el-table-column>
            <el-table-column
                    prop="description"
                    label="计划名称"
                    align="center"
                    >
            </el-table-column>
            <el-table-column
                    prop="srcProject"
                    label="源系统"
                    align="center"
            >
                <template slot-scope="scope">
                    {{srcProjectGName[scope.row.srcProject]}}
                </template>
            </el-table-column>
            <el-table-column
                    prop="destProject"
                    label="目标系统"
                    align="center"
            >
                <template slot-scope="scope">
                    {{destProjectGName[scope.row.destProject]}}
                </template>
            </el-table-column>
            <el-table-column
                    prop="srcDb"
                    label="源数据库"
                    align="center"
                    >
            </el-table-column>
            <el-table-column
                    prop="destDb"
                    label="目标数据库"
                    align="center"
                    >
            </el-table-column>
            <el-table-column
                    width="80"
                    align="center"
                    label="启用状态">
                <template slot-scope="scope">
                    <span v-if="scope.row.status==='normal'"  style="color: #67C23A">{{planStatusGName[scope.row.status]}}</span>
                    <span v-else type="danger" size="small" style="color: #f40">{{planStatusGName[scope.row.status]}}</span>
                </template>
            </el-table-column>
            <el-table-column
                    width="150"
                    prop="createTime"
                    align="center"
                    label="创建时间">
            </el-table-column>
            <el-table-column
                    width="150"
                    prop="updateTime"
                    align="center"
                    label="更新时间">
            </el-table-column>
            <el-table-column
                    fixed="right"
                    label="操作"
                    width="150">
                <template slot-scope="scope">
                    <el-button @click="del(scope.row.uuid)" type="text" size="mini"><i class="el-icon-delete"></i>&nbsp;删除</el-button>
                    <el-button @click="openEdit(scope.row.uuid)" type="text" size="mini"><i class="el-icon-edit"></i>&nbsp;编辑</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <el-pagination
                class="pagination"
                @current-change="currentChange"
                :current-page.sync="currentPage"
                :page-size="pageSize"
                layout="total, prev, pager, next"
                :total="total">
        </el-pagination>

        <!-- 新增 -->
        <el-dialog
                :close-on-click-modal="false"
                title="新增计划"
                :visible.sync="addVisible"
        >
            <el-form :model="addForm" :rules="addRules" ref="addForm">
                <el-form-item prop="planContent">
                    <el-input
                            resize="none"
                            type="textarea"
                            :rows="15"
                            placeholder="请输入内容"
                            v-model="addForm.planContent">
                    </el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addCancel">取 消</el-button>
                <el-button :loading="loading" type="primary" @click="add">保 存</el-button>
            </span>
        </el-dialog>

        <!-- 新增 -->
        <el-dialog
                :close-on-click-modal="false"
                title="编辑计划"
                :visible.sync="editVisible"
        >
            <el-form :model="editForm" :rules="addRules" ref="editForm">
                <el-form-item prop="planContent">
                    <el-input
                            resize="none"
                            type="textarea"
                            :rows="15"
                            placeholder="请输入内容"
                            v-model="editForm.planContent">
                    </el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editCancel">取 消</el-button>
                <el-button :loading="loading" type="primary" @click="edit">更新</el-button>
            </span>
        </el-dialog>

        <!-- 查看 -->
        <el-dialog
                title="同步计划"
                :visible.sync="showVisible"
        >
            <pre v-if="showVisible" v-highlightjs><code class="javascript showContent" v-text="showContent"></code></pre>
        </el-dialog>
    </div>

</template>

<script>
    import constant from "@/util/constant";
    export default {
        name: "Plan.vue",
        data() {
            return {
                uuid: '',
                loading: true,
                searchForm: {
                    status: '',
                    planCode: '',
                },
                addForm: {
                    planContent: '',
                },
                editForm: {
                    planContent: '',
                },
                addVisible: false,
                showVisible: false,
                editVisible: false,
                showContent: '',
                dataList: [],
                total: 0,
                pageSize: 20,
                currentPage: 1,
                addRules: {
                    planContent: [
                        {required: true, message: '请填写同步计划内容', trigger: 'blur'}
                    ]
                }
            }
        },
        computed: {
            planStatusG() {
                return constant.methods.getConst('plan_status');
            },
            planStatusGName(){
                return constant.methods.formatConst(this.planStatusG);
            },
            srcProjectG() {
                return constant.methods.getConst('src_project');
            },
            srcProjectGName(){
                return constant.methods.formatConst(this.srcProjectG);
            },
            destProjectG() {
                return constant.methods.getConst("dest_project");
            },
            destProjectGName(){
                return constant.methods.formatConst(this.destProjectG);
            },
        },
        methods: {
            reset() {
                this.$refs["searchForm"].resetFields();
                this.getData();
            },
            updateStatus(){

                if(!this.uuid){

                    return this.$message.warning("请选择一条记录进行操作");
                }

                let row = this.dataList.find(e=>e.uuid === this.uuid);

                let tips  = row.status === 'normal' ? '禁用' : '启用';

                this.$confirm('此操作将更新状态, 是否'+tips+'?', '提示', {
                    confirmButtonText: tips,
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {

                    this.$sendAjax.doGet('/sync/plan/status?uuid='+this.uuid).then(({data:res})=>{

                        this.addVisible = false;
                        this.$message({
                            type: 'success',
                            message: tips + '成功!'
                        });

                        this.getData();

                    }).finally(()=>{

                        this.loading=false;
                    })

                }).catch((e) => {
                    console.log(e)
                });

            },
            openEdit(uuid){
                this.$sendAjax.doGet('/sync/plan/content?uuid='+uuid).then((data) => {

                    this.editForm.planContent = data.result;
                    this.editForm.uuid = uuid;
                    this.editVisible = true;
                }).finally(() => {

                });
            },
            del(uuid){

                this.$confirm('此操作将永久删除, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {

                    this.$sendAjax.doGet('/sync/plan/delete?uuid='+uuid).then(({data:res})=>{

                        this.addVisible = false;
                        this.$message.success({
                            showClose: true,
                            message: '删除同步计划成功'
                        });

                        this.getData();

                    }).finally(()=>{

                        this.loading=false;
                    })

                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });

            },
            getData() {
                this.uuid = '';
                this.loading = true;
                const params = {};
                Object.keys(this.searchForm).forEach(key => {
                    params[key] = this.searchForm[key];
                })
                params.page = this.currentPage;
                params.limit = this.pageSize;
                this.$sendAjax.doGet('/sync/plan/page', {params}).then((data) => {
                    this.dataList = data.result.dataList;
                    this.total = data.result.totalRow;
                }).finally(() => {
                    this.loading = false;
                });
            },
            currentChange(current) {
                this.currentPage = current;
                this.getData();
            },
            add() {
                this.$refs['addForm'].validate((valid) => {
                    if (valid) {
                        this.loading = true;

                        this.$sendAjax.doPost('/sync/plan/save',this.addForm , { emulateJSON: false }).then(({data:res})=>{

                            this.addVisible = false;
                            this.$message.success({
                                showClose: true,
                                message: '新增同步计划成功'
                            });
                            this.$refs["addForm"].resetFields();
                            this.getData();

                        }).finally(()=>{

                            this.loading=false;
                        })


                    } else {
                        return false;
                    }
                });

            },
            editCancel() {
                this.$refs["editForm"].resetFields();
                this.editVisible = false;
            },
            edit() {
                this.$refs['editForm'].validate((valid) => {
                    if (valid) {
                        this.loading = true;

                        this.$sendAjax.doPost('/sync/plan/update',this.editForm , { emulateJSON: false }).then(({data:res})=>{

                            this.$refs["editForm"].resetFields();
                            this.editVisible = false;
                            this.getData();
                            this.$message.success({
                                showClose: true,
                                message: '更新同步计划成功'
                            });

                        }).finally(()=>{

                            this.loading=false;
                        })


                    } else {
                        return false;
                    }
                });

            },
            addCancel() {
                this.$refs["addForm"].resetFields();
                this.addVisible = false;
            },
            lookContent(uuid) {

                this.$sendAjax.doGet('/sync/plan/content?uuid='+uuid).then((data) => {

                    this.showContent = data.result;
                    this.showVisible = true;
                }).finally(() => {

                });
            }
        },
        mounted() {
            this.getData();
        }
    }
</script>

<style scoped>
    .planCode {
        font-family: Consolas, serif;
        color: #409eff;
        cursor: pointer;
    }

    .pagination {
        margin-top: 20px;
    }
    .showContent{
        font-family: "Ubuntu Mono",serif!important;
    }
    .planTable{
        margin-top: 20px;
    }
</style>