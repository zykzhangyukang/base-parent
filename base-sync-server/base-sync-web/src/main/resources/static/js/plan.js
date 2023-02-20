layui.use('table', function () {

    let table = layui.table;

    //第一个实例
    table.render({
        elem: '#sync-plan-table'
        , height: 650
        , url: '/sync/plan/page'
        , parseData: function (res) {
            if (res.code === 200) {
                return {
                    "code": "0"
                    , "msg": "success"
                    , "data": res.result.dataList
                };
            }
        }
        , page: true
        , cols: [[
            {field: 'planCode', title: '同步计划编号', width: 300, align:'center'}
            , {field: 'srcProject', title: '源系统', width: 200, align:'center'}
            , {field: 'destProject', title: '目标系统', width: 150, align:'center'}
            , {field: 'srcDb', title: '源数据库', width: 150, align:'center'}
            , {field: 'destDb', title: '目标数据库', width: 150, align:'center'}
            , {field: 'status', title: '状态', width: 150, align:'center'}
            , {field: 'createTime', title: '创建时间', width: 200, align:'center'}
            , {field: 'updateTime', title: '更新时间', width: 200, align:'center'},
            {field: 'op', title: '操作', width:150,  fixed: 'right', align:'center',toolbar: '#toolbar'}
        ]]
    });

});