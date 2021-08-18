<html>
<#include "../zujian/top.ftlh">

<body>
<div id="wrapper" class="toggled">
    <#--内容区域头部样式-->
    <h2 class="bg-info text-white p-3  text-center">菜品类目管理</h2>
    <#--左边栏-->
    <#include "../zujian/left.ftlh">
    <#--主要内容content-->
    <#if !isAdmin??>
        <h2 class="text-danger m-5">您不是管理员，没有管理权限</h2>
    <#else >
        <div id="page-content-wrapper" class="container">
            <#--添加类目-->
            <button class="btn btn-warning text-white my-3"
                    onclick="window.parent.location.href='/diancan/leimu/detail';">
                添加单个类目
            </button>
            <#--添加类目-->
            <button class="btn btn-info text-white my-3" onclick="window.parent.location.href='/diancan/leimu/excel';">
                批量导入类目
            </button>
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <table class="table table-bordered ">
                        <thead>
                        <tr class="table-info">
                            <th>类目Id</th>
                            <th>名字</th>
                            <th>类目Type</th>
                            <th>创建时间</th>
                            <th>修改时间</th>
                            <th colspan="2">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list leimuList as leimu>
                            <tr>
                                <td>${leimu.leimuId}</td>
                                <td>${leimu.leimuName}</td>
                                <td>${leimu.leimuType}</td>
                                <td>${leimu.createTime!''}</td>
                                <td>${leimu.updateTime!''}</td>
                                <td><a href="/diancan/leimu/detail?leimuId=${leimu.leimuId}">修改</a>
                                <td><a href="/diancan/leimu/remove?leimuId=${leimu.leimuId}">删除</a>
                                </td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </#if>
</div>
</body>
</html>
