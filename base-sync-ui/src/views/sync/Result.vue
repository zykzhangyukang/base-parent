<template>
    <div class="result">

        <!-- 搜索区域 -->
        <el-form size="small" :inline="true" :model="searchForm" ref="searchForm">

            <el-form-item label="源系统" prop="srcProject">
                <el-select v-model="searchForm.srcProject" placeholder="源系统" class="w150">
                </el-select>
            </el-form-item>

            <el-form-item label="目标系统" prop="destProject">
                <el-select v-model="searchForm.destProject" placeholder="目标系统" class="w150">
                </el-select>
            </el-form-item>

            <el-form-item label="计划编号" prop="planCode">
                <el-input v-model="searchForm.planCode" placeholder="计划编号"></el-input>
            </el-form-item>
            <el-form-item label="创建时间">
                <el-date-picker
                        value-format="yyyy-MM-dd HH:mm:ss"
                        v-model="createTimeRange"
                        type="datetimerange"
                        :picker-options="pickerOptions"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        align="right">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="同步状态" prop="syncStatus">
                <el-select v-model="searchForm.syncStatus" placeholder="计划状态" class="w150">
                    <el-option label="全部" value=""></el-option>
                    <el-option label="成功" value="success"></el-option>
                    <el-option label="失败" value="fail"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="重试次数" prop="repeatCount">
                <el-select v-model="searchForm.repeatCount" class="w150" placeholder="重试次数">
                    <el-option label="全部" value=""></el-option>
                    <el-option label=">=1" value="1"></el-option>
                    <el-option label=">=2" value="2"></el-option>
                    <el-option label=">=3" value="3"></el-option>
                    <el-option label=">=4" value="4"></el-option>
                    <el-option label=">=5" value="5"></el-option>
                    <el-option label=">=6" value="6"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="消息来源" prop="msgSrc">
                <el-select v-model="searchForm.msgSrc" class="w150" placeholder="消息来源">
                    <el-option label="全部" value=""></el-option>
                    <el-option label="RocketMQ" value="rocket_mq"></el-option>
                    <el-option label="手动同步" value="handle"></el-option>
                    <el-option label="定时器同步" value="job"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="关键词" prop="keywords">
                <el-input v-model="searchForm.keywords" placeholder="关键词,消息内容,同步内容,备注" class="w500"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="getData">查询</el-button>
                <el-button type="warning" @click="reset" icon="el-icon-refresh">重置</el-button>
                <el-button type="primary" @click="signSuccess">标记成功</el-button>
                <el-button type="primary" @click="validResult">校验数据</el-button>
                <el-button type="primary" @click="repeatSync">重新同步</el-button>
            </el-form-item>
        </el-form>

        <!-- 表格部分 -->
        <el-table
                border
                ref="resultTable"
                class="resultTable"
                size="mini"
                stripe
                v-loading="loading"
                :data="dataList"
                highlight-current-row
                style="width: 100%">
            <el-table-column label="选择" width="50">
                <template slot-scope="{ row }">
                    <el-radio :label="row.uuid" v-model="uuid"><span></span></el-radio>
                </template>
            </el-table-column>
            <el-table-column
                    prop="planCode"
                    label="计划编号"
                    width="260px"
            >
                <template slot-scope="scope">
                    <span class="planCode">{{ scope.row.planCode | ellipsis(35) }}</span>
                </template>
            </el-table-column>
            <el-table-column
                    prop="planName"
                    label="计划名称"
                    width="150px"

            >
                <template slot-scope="scope">
                    <span>{{ scope.row.planName | ellipsis(20) }}</span>
                </template>
            </el-table-column>

            <el-table-column
                    prop="srcProject"
                    label="源系统"
                    width="120px"
            >
            </el-table-column>
            <el-table-column
                    prop="destProject"
                    label="目标系统"
                    width="120px"
            >
            </el-table-column>
            <el-table-column
                    prop="msgCreateTime"
                    width="150px"
                    label="创建时间">
            </el-table-column>
            <el-table-column
                    prop="syncTime"
                    width="150px"
                    label="同步时间">
            </el-table-column>

            <el-table-column
                    prop="status"
                    width="100px"
                    label="同步结果">
                <template slot-scope="scope">
                    <span v-if="scope.row.status==='success'" style="color:#67C23A">成功  ({{(new Date(scope.row.syncTime).getTime() - new Date(scope.row.msgCreateTime)) / 1000 }})</span>
                    <span v-else style="color: red;cursor: pointer">
                       <el-tooltip effect="light" popper-class="tooltip-width" :content="scope.row.errorMsg"
                                   placement="top">
                          <span>失败</span>
                        </el-tooltip>
                   </span>
                </template>
            </el-table-column>

            <el-table-column
                    prop="repeatCount"
                    width="80px"
                    label="重试次数">
            </el-table-column>

            <el-table-column
                    prop="msgSrc"
                    width="100px"
                    label="来源">
            </el-table-column>

            <el-table-column
                    width="150px"
                    prop="msgContent"
                    label="消息内容">
                <template slot-scope="scope">
                    <span @click="showMsgContent(scope.row.msgContent)" style="color: #3a8ee6;cursor: pointer">{{ scope.row.msgContent | ellipsis(15) }}</span>
                </template>
            </el-table-column>

            <el-table-column
                    width="150px"
                    prop="syncContent"
                    label="同步内容">
                <template slot-scope="scope">
                    <span @click="showSyncContent(scope.row.syncContent)" style="color: #3a8ee6;cursor: pointer">{{ scope.row.syncContent | ellipsis(15) }}</span>
                </template>
            </el-table-column>

            <el-table-column
                    width="100px"
                    prop="remark"
                    label="备注系统">
                <template slot-scope="scope">
                    <span style="color: #E6A23C;cursor: pointer">{{ scope.row.remark | ellipsis(5) }}</span>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <el-pagination
                class="pagination"
                @current-change="currentChange"
                :current-page.sync="currentPage"
                layout="total, prev, pager, next"
                :page-size="pageSize"
                :total="total">
        </el-pagination>

        <!-- 查看同步内容 -->
        <el-dialog
                title="同步内容"
                :visible.sync="syncVisible"
        >
            <pre v-if="syncVisible" v-highlightjs><code class="c++ showContent" v-text="syncContent"></code></pre>
        </el-dialog>

        <!-- 查看消息内容 -->
        <el-dialog
                title="消息内容"
                :visible.sync="msgVisible"
        >
            <pre v-if="msgVisible" v-highlightjs><code class="c++ showContent" v-text="msgContent"></code></pre>
        </el-dialog>

        <!-- 校验数据 -->
        <el-dialog
                title="校验数据"
                :visible.sync="validVisible"
        >

            <el-table size="mini" v-for="(item,index) in validTables"
                      :data="[item]"
                      :key="index"
                    style="width: 100%"
            >

                <el-table-column  align="center" :label="item.srcTable + '->' + item.destTable">
                    <el-table-column
                            align="center"
                            prop="province"
                            width="200px"
                            label="数据库字段"
                    >
                        <template slot-scope="{ row }">

                            <el-row :gutter="20">
                                <el-col :span="24">  <p  size="mini" v-for="(i,ix) in  item.srcColumnList" >{{i +'->' +item.destColumnList[ix] }}</p></el-col>
                            </el-row>

                        </template>

                    </el-table-column>
                    <el-table-column
                            align="center"
                            prop="city"
                            label="数据库值"
                    >
                        <template slot-scope="{ row }">

                            <el-col :span="24">   <p  size="mini" v-for="(i,ix) in  item.destResultList" >{{i + '->' + item.destResultList[ix]}}</p></el-col>

                        </template>

                    </el-table-column>
                </el-table-column>
            </el-table>

        </el-dialog>

    </div>
</template>

<script>

    export default {
        name: "Result.vue",
        data() {
            return {
                uuid: '',
                validTables: [],
                loading: true,
                syncVisible: false,
                syncContent: '',
                msgVisible: false,
                validVisible: false,
                msgContent: '',
                dataList: [],
                searchForm: {
                    syncStatus: '',
                    planCode: '',
                    keywords: '',
                    repeatCount: '',
                    msgSrc: '',
                    srcProject: '',
                    destProject: '',
                },
                createTimeRange: [],
                total: 0,
                pageSize: 16,
                currentPage: 1,
                pickerOptions: {
                    shortcuts: [{
                        text: '最近一天',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                },
            }
        },
        filters: {

            ellipsis(value, limit) {
                if (!value) return ''
                if (value.length > limit) {
                    return value.slice(0, limit) + '...'
                }
                return value
            },

        },
        methods: {
            showSyncContent(content) {
                let ojb  = JSON.parse(content);
                this.syncContent = JSON.stringify(ojb,null,'\t');
                this.syncVisible = true;
            },
            showMsgContent(content) {
                let ojb  = JSON.parse(content);
                this.msgContent = JSON.stringify(ojb,null,'\t');
                this.msgVisible = true;
            },
            reset() {
                this.$refs["searchForm"].resetFields();
                this.currentPage = 1;
                this.uuid = '';
                // this.createTimeRange = [new Date(new Date().toLocaleDateString()),new Date()];
                this.getData();
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
                params.startTime = this.createTimeRange[0];
                params.endTime = this.createTimeRange[1];

                this.$sendAjax.doGet('/sync/result/search', {params}).then((data) => {
                    this.dataList = data.result.dataList;
                    this.total = data.result.totalRow;
                }).finally(() => {
                    this.loading = false;
                });
            },
            signSuccess() {

                if (!this.uuid) {
                    return this.$message.warning("请选择要操作的记录");
                }

                this.$confirm('操作存在风险, 仅限开发人员操作!', '标记成功', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {

                    this.$sendAjax.doGet('/sync/result/sign/success?uuid=' + this.uuid).then(({data: res}) => {

                        this.addVisible = false;
                        this.$message.success({
                            showClose: true,
                            message: '标记同步记录成功'
                        });

                        this.getData();

                    }).finally(() => {

                        this.loading = false;
                    })

                }).catch((e) => {
                    console.log(e)
                    this.$message({
                        type: 'info',
                        message: '已取消标记'
                    });
                });

            },
            validResult() {

                if(!this.uuid){

                    return this.$message.warning("请选择一条记录进行操作");
                }

                let row = this.dataList.find(e=>e.uuid === this.uuid);

                this.$sendAjax.doPost('/sync/result/valid/data',{msgContent: row.msgContent},{ emulateJSON: true }).then(({result:res})=>{

                    console.log(res)
                    this.validVisible = true;
                    this.validTables = res;

                }).finally(()=>{

                    this.loading=false;
                })


            },
            repeatSync() {

                if (!this.uuid) {
                    return this.$message.warning("请选择要操作的记录");
                }

                this.$confirm('操作存在风险, 仅限开发人员操作!', '重新同步', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {

                    this.$sendAjax.doGet('/sync/result/repeat/sync?uuid=' + this.uuid).then(({data: res}) => {

                        this.addVisible = false;
                        this.$message.success({
                            showClose: true,
                            message: '重新同步成功'
                        });

                        this.getData();

                    }).finally(() => {

                        this.loading = false;
                    })

                }).catch((e) => {
                    console.log(e)
                    this.$message({
                        type: 'info',
                        message: '已取消重新同步'
                    });
                });
            },
            currentChange(current) {
                this.currentPage = current;
                this.getData();
            },
        },
        created() {
            this.getData();
        }
    }
</script>


<style>
    .planCode {
        font-family: Consolas, serif;
    }

    .pagination {
        margin-top: 20px;
    }

    .resultTable {
        margin-top: 20px;
    }

    .tooltip-width {
        max-width: 500px;
    }

    .w150 {
        width: 150px;
    }

    .w500 {
        width: 300px;
    }

    .showContent {
        font-family: "Ubuntu Mono", serif !important;
    }
</style>