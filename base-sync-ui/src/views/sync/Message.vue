<template>
   <div class="message">
       <!-- 搜索区域 -->
       <el-form size="small" :inline="true" :model="searchForm" ref="searchForm">
           <el-form-item label="源系统" prop="srcProject">
               <el-select v-model="searchForm.srcProject" placeholder="源系统" class="w150">
                   <el-option v-for="item in srcProjectG" :label="srcProjectGName[item.code]" :value="item.code" :key="item.code"></el-option>
               </el-select>
           </el-form-item>

           <el-form-item label="目标系统" prop="destProject">
               <el-select v-model="searchForm.destProject" placeholder="目标系统" class="w150">
                   <el-option label="全部" value=""></el-option>
                   <el-option v-for="item in destProjectG" :label="destProjectGName[item.code]" :value="item.code" :key="item.code"></el-option>
               </el-select>
           </el-form-item>

           <el-form-item label="发送状态" prop="sendStatus">
               <el-select v-model="searchForm.sendStatus" placeholder="发送状态" class="w150">
                   <el-option label="全部" value=""></el-option>
                   <el-option v-for="item in sendStatusG" :label="sendStatusGName[item.code]" :value="item.code" :key="item.code"></el-option>
               </el-select>
           </el-form-item>

           <el-form-item label="处理状态" prop="dealStatus">
               <el-select v-model="searchForm.dealStatus" placeholder="处理状态" class="w150">
                   <el-option label="全部" value=""></el-option>
                   <el-option v-for="item in dealStatusG" :label="dealStatusGName[item.code]" :value="item.code" :key="item.code"></el-option>
               </el-select>
           </el-form-item>

           <el-form-item label="消息ID" prop="msgId">
               <el-input v-model="searchForm.msgId" placeholder="消息ID"></el-input>
           </el-form-item>

           <el-form-item label="MQ消息ID" prop="mid">
               <el-input v-model="searchForm.mid" placeholder="MQ消息ID"></el-input>
           </el-form-item>

           <br/>
           <el-form-item>
               <el-button type="primary"  @click="getData" icon="el-icon-search">查询</el-button>
               <el-button type="warning"  @click="reset" icon="el-icon-refresh">重置</el-button>
           </el-form-item>

       </el-form>


       <!-- 表格部分 -->
       <el-table
               border
               ref="messageTable"
               class="messageTable"
               size="mini"
               stripe
               v-loading="loading"
               :data="dataList"
               style="width: 100%">
           <el-table-column
                   prop="mid"
                   width="300"
                   align="center"
                   label="MQ消息">
           </el-table-column>
           <el-table-column
                   width="200px"
                   prop="msgContent"
                   align="center"
                   label="消息内容">
               <template slot-scope="scope">
                   <span @click="showMsgContent(scope.row.msgContent)" style="color: #3a8ee6;cursor: pointer">{{ scope.row.msgContent | ellipsis(20) }}</span>
               </template>
           </el-table-column>
           <el-table-column
                   prop="srcProject"
                   align="center"
                   label="源系统">
               <template slot-scope="scope">
                   {{srcProjectGName[scope.row.srcProject]}}
               </template>
           </el-table-column>
           <el-table-column
                   prop="destProject"
                   align="center"
                   label="目标系统">
               <template slot-scope="scope">
                   {{destProjectGName[scope.row.destProject]}}
               </template>
           </el-table-column>
           <el-table-column
                   prop="createTime"
                   align="center"
                   label="创建时间">
           </el-table-column>
           <el-table-column
                   prop="sendTime"
                   align="center"
                   label="发送时间">
           </el-table-column>
           <el-table-column
                   prop="ackTime"
                   align="center"
                   label="ACK时间">
           </el-table-column>
           <el-table-column
                   prop="sendStatus"
                   align="center"
                   label="发送状态">
               <template slot-scope="scope">
                   {{sendStatusGName[scope.row.sendStatus]}}
               </template>
           </el-table-column>
           <el-table-column
                   prop="dealStatus"
                   align="center"
                   label="处理状态">
               <template slot-scope="scope">
                   {{dealStatusGName[scope.row.dealStatus]}}
               </template>
           </el-table-column>
           <el-table-column
                   prop="dealCount"
                   align="center"
                   label="处理次数">
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

       <!-- 查看消息内容 -->
       <el-dialog
               title="消息内容"
               :visible.sync="msgVisible"
       >
           <div  class="msg_content_div">
               <pre v-if="msgVisible" v-highlightjs><code class="c++ showContent" v-text="msgContent"></code></pre>
           </div>
       </el-dialog>
   </div>
</template>

<script>
    import constant from "@/util/constant";

    export default {
        name: "Message.vue",
        data(){
            return {
                searchForm:{
                    srcProject: 'order',
                    sendStatus: '',
                    dealStatus:'',
                    destProject:'',
                    msgId: '',
                    mid: '',
                },
                loading: false,
                dataList: [],
                total: 0,
                pageSize: 16,
                currentPage: 1,
                msgContent: '',
                msgVisible: false,
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
        computed: {
            srcProjectG() {
                return constant.methods.getConst('src_project');
            },
            srcProjectGName() {
                return constant.methods.formatConst(this.srcProjectG);
            },
            destProjectG() {
                return constant.methods.getConst("dest_project");
            },
            destProjectGName() {
                return constant.methods.formatConst(this.destProjectG);
            },
            sendStatusG() {
                return constant.methods.getConst("send_status");
            },
            sendStatusGName() {
                return constant.methods.formatConst(this.sendStatusG);
            },
            dealStatusG() {
                return constant.methods.getConst("deal_status");
            },
            dealStatusGName() {
                return constant.methods.formatConst(this.dealStatusG);
            },
        },
        methods:{
            showMsgContent(content) {
                let ojb  = JSON.parse(content);
                this.msgContent = JSON.stringify(ojb,null,'\t');
                this.msgVisible = true;
            },
            reset() {
                this.currentPage = 1;
                this.$refs["searchForm"].resetFields();
                this.getData();
            },
            getData() {
                this.loading = true;
                const params = {};
                Object.keys(this.searchForm).forEach(key => {
                    params[key] = this.searchForm[key];
                })
                params.currentPage = this.currentPage;
                params.pageSize = this.pageSize;
                this.$sendAjax.doGet('/sync/message/page', {params}).then((data) => {
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

        },
        created(){
            this.getData();
        }
    }
</script>

<style scoped>
    .w150 {
        width: 150px;
    }

    .w500 {
        width: 300px;
    }
    .pagination {
        margin-top: 20px;
    }
    .messageTable{
        margin-top: 20px;
    }
    .msg_content_div{
        max-height: 400px;
        overflow: auto;
    }
    .showContent {
        font-family: "Ubuntu Mono", serif !important;
    }
</style>