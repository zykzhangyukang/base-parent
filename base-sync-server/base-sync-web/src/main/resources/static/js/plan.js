layui.use('table', function () {

    let table = layui.table;
    let $ = layui.jquery;

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
                    , "count": res.result.totalRow
                    , "data": res.result.dataList
                };
            }
        }
        , page: true
        , limit: 30
        , limits:[5,10,15,30,60,80,100]
        , cols: [[
            {
                field: 'planCode', title: '同步计划编号', width: 300, align: 'center', templet: function (row) {
                    return `<span lay-event="view" style="color: #2d8cf0;cursor: pointer;font-family: Consolas,serif">${row.planCode}</span>`;
                }
            }
            , {field: 'srcProject', title: '源系统', width: 150, align: 'center'}
            , {field: 'destProject', title: '目标系统', width: 150, align: 'center'}
            , {field: 'srcDb', title: '源数据库', width: 150, align: 'center'}
            , {field: 'destDb', title: '目标数据库', width: 150, align: 'center'}
            , {field: 'status', title: '状态', width: 150, align: 'center'}
            , {field: 'createTime', title: '创建时间', width: 180, align: 'center'}
            , {field: 'updateTime', title: '更新时间', width: 180, align: 'center'},
            {field: 'op', title: '操作', width: 200, fixed: 'right', align: 'center', toolbar: '#toolbar'}
        ]]
    });

    table.on('tool(table)', function (obj) {

            let row = obj.data;

            switch (obj.event) {
                case 'view':
                    view(row);
                    break;
                case 'edit':
                    layer.msg('编辑');
                    break;
                case 'del':
                    layer.msg('删除');
                    break;
            }
        }
    );

    /**
     * 查看同步计划内容
     *
     * @param row
     */
    function view(row) {
        let content = '<textarea disabled style="cursor:pointer;color: #314659;width: 100%;height: 100%;padding: 2px"' +
            ' class="layui-textarea plan-content"></textarea>'
        layer.open({
            title: '同步计划:' + row.planCode,
            type: 1,
            area: ['750px', '450px'],
            content: content,
            shadeClose: true,
            btn: ['确定']
        });
        $(".plan-content").text(row.planContent);

    }

    /**
     * 点击按钮搜索
     */
    $("#searchBtn").click(function () {
        table.reload('sync-plan-table', {
            where: {planCode: $("#planCode").val()}
        })
    })

    /**
     * 点击按钮搜索
     */
    $("#resetBtn").click(function () {
        let planCode = $("#planCode");

        planCode.val('');
        table.reload('sync-plan-table', {
            where: {planCode: planCode.val()}
        })
    })

    $("#addBtn").click(function () {

        layer.open({
            type: 1,
            title: '新增',
            content: '<textarea id="add-plan-content" style="width: 100%;height: 95%" name="desc" placeholder="请输入内容" class="layui-textarea"></textarea>',
            area: ['750px', '450px'],
            btn: ['提交', '取消'],
            yes: function (index) {
                let content = $("#add-plan-content").val();
                let params = {planContent: content};

                $.ajax({
                    url: "/sync/plan/save",
                    data: JSON.stringify(params),
                    dataType: "json",
                    contentType: "application/json;charset=utf-8",
                    type: "POST",
                    success: function (res) {
                        if (res.code === 200) {

                            table.reload('sync-plan-table');
                            layer.close(index);
                        } else {

                            layer.msg(res.msg);
                        }
                    }
                });
            }
        });

    })
});