<template>
    <el-container class="wrapper" :class="{'folded':folded}">
        <el-aside class="left" width="200px">
            <div style="text-align: center;margin: 10px 0">
                <el-switch
                        v-model="folded"
                >
                </el-switch>
            </div>
            <el-divider></el-divider>
            <el-menu
                    :open="1"
                    :collapse="folded"
                    :default-active="activeName"
                    background-color="#191a23"
                    text-color="#fff"
                    active-text-color="#2d8cf0"
            >
                <el-submenu index="1">
                    <template slot="title">
                        <i class="el-icon-folder-opened"></i>
                        <span slot="title">同步系统</span>
                    </template>
                    <el-menu-item-group>
                        <el-menu-item v-for="item in menus" :key="item.path" :index="item.path" @click="openTab(item)">
                            <i :class="item.icon"></i>
                            <span slot="title">{{item.name}}</span>
                        </el-menu-item>
                    </el-menu-item-group>
                </el-submenu>


            </el-menu>
            <div class="folded-btn" @click="folded=!folded"><span
                    :class="[folded?'el-icon-s-unfold':'el-icon-s-fold']"></span></div>
        </el-aside>
        <el-container class="right">
            <el-header class="header">
            </el-header>
            <tabs-nav></tabs-nav>
        </el-container>
    </el-container>
</template>

<script>
    import store from '@/store/index'
    import tabsNav from "@/layouts/TabsNav";
    import router from '@/router/index'

    export default {
        components: {
            tabsNav
        },
        name: "Home.vue",
        data() {
            return {
                folded: false,
                activeName: this.$route.path,
                menus:[
                    {
                        name: '系统首页',
                        icon: 'el-icon-s-home',
                        path: '/sync/index',
                    },
                    {
                        name: '同步计划',
                        icon: 'el-icon-s-grid',
                        path: '/sync/plan',
                    },
                    {
                        name: '同步记录',
                        icon: 'el-icon-s-marketing',
                        path: '/sync/result',
                    },
                    {
                        name: '消息列表',
                        icon: 'el-icon-tickets',
                        path: '/sync/message',
                    },
                    {
                        name: '回调列表',
                        icon: 'el-icon-timer',
                        path: '/sync/callback',
                    }
                ]
            }
        },
        methods: {
            openTab(item) {
                store.commit('addTabs', item);
                router.push(item.path);
            }
        },
        watch: {
            "$store.state": {
                deep: true,
                handler: function () {
                    this.activeName = store.state.editableTabsValue;
                }
            },
        },
    }
</script>

<style>
    .el-tabs__content {
        background-color: #ffffff;
        height: calc(100vh - 72px);
    }

    .header {
        /*background-color: #ffffff;*/
        /*box-shadow: 1px 1px 1px #999999;*/
        z-index: 1000;
        height: 0 !important;
    }

    .left {
        transition: all 200ms;
        /*border-right: 1px solid #eeeeee;*/
        background-color: #191a23;
    }

    .el-menu {
        border-right-width: 0 !important;
    }

    .main {
        background-color: #f8f8f9;
    }

    .wrapper {
        height: 100vh;
    }

    .folded-btn {
        background: #424449;
        color: #ffffff;
        text-align: center;
        position: fixed;
        bottom: 0;
        width: 200px;
        transition: all 200ms;
        cursor: pointer;
    }

    /** 折叠后的效果 */
    .folded .left {
        width: 60px !important;
    }

    .folded .el-menu span {
        display: none;
    }

    .folded .folded-btn {
        width: 60px
    }

    .el-menu-item-group__title{
        display: none!important;
    }
    .el-divider--horizontal{
        margin: 20px 0 0 1px !important;
        background-color: #666666;
    }
</style>