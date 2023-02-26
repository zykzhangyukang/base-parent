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
                <RouterView v-if="$route.path===item.name"></RouterView>
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
        }
    }
</script>

<style scoped>

</style>