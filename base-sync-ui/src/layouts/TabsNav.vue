<template>
    <el-tabs class="tabs"
             v-model="editableTabsValue"
             type="border-card"
             @tab-click="clickTab"
             @tab-remove="removeTab"
    >
        <el-tab-pane class="tab-pane"
                     v-for="item in editableTabs"
                     :closable="item.close"
                     :key="item.name"
                     :label="item.title"
                     :name="item.name">
            <span slot="label"><i :class="item.icon"></i> {{item.title}}</span>
            <keep-alive>
                <transition name="slide-fade">
                    <RouterView v-if="$route.path===item.name"></RouterView>
                </transition>
            </keep-alive>
        </el-tab-pane>
    </el-tabs>
</template>

<script>
    import store from '@/store/index'
    import router from '@/router/index'

    export default {
        name: "TabsNav.vue",
        data() {
            return {
                editableTabsValue: store.state.editableTabsValue,
                editableTabs: store.state.editableTabs,
            }
        },
        methods: {
            clickTab(tab) {
                let name = JSON.stringify(tab.paneName).replace('"', '').replace('"', '')
                store.commit('changeTabs', name)
                router.push(name)
            },
            removeTab(targetName) {
                let tabs = this.editableTabs;
                let activeName = this.editableTabsValue;
                if (activeName === targetName) {
                    tabs.forEach((tab, index) => {
                        if (tab.name === targetName) {
                            let nextTab = tabs[index + 1] || tabs[index - 1];
                            if (nextTab) {
                                activeName = nextTab.name;
                            }
                        }
                    });
                }
                store.state.editableTabsValue = activeName;
                store.state.editableTabs = tabs.filter(tab => tab.name !== targetName);
                router.push(activeName)
            },
            refreshTabs(){
                this.editableTabsValue=store.state.editableTabsValue;
                this.editableTabs=store.state.editableTabs;
            },
        },
        watch: {
            "$store.state": {
                deep: true,
                handler: function () {
                    this.refreshTabs();
                }
            },
        },
        created(){

        }
    }
</script>

<style scoped>
    .fade-enter-active, .fade-leave-active {
        transition: opacity .5s;
    }
    .fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
        opacity: 0;
    }

    /* 可以设置不同的进入和离开动画 */
    /* 设置持续时间和动画函数 */
    .slide-fade-enter-active {
        transition: all .3s ease;
    }
    .slide-fade-leave-active {
        transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
    }
    .slide-fade-enter, .slide-fade-leave-to
        /* .slide-fade-leave-active for below version 2.1.8 */ {
        transform: translateX(10px);
        opacity: 0;
    }
</style>