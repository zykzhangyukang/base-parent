<template>
    <div id="welcome">
        <h1>可靠消息服务系统</h1>
        <el-col :sm="12" :lg="24">
            <el-result icon="success" title="成功提示" subTitle="鲁迅说过: 一个成熟的系统,应该具备可以用性和数据一致性!">
                <template slot="extra">
                    <el-button type="primary" size="medium">系统介绍</el-button>
                </template>
            </el-result>
        </el-col>
    </div>
</template>

<script>
    export default {
        name: "welcome",
        data() {
            return {

            }
        },
        methods:{

        }
    }
</script>

<style scoped>
#welcome{
    margin: 200px auto;
    text-align: center;
    font-size: 25px;
}
</style>