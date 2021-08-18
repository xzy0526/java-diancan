<html>
<#include "../zujian/top.ftlh">

<body>
<div id="wrapper" class="toggled">
    <#--内容区域头部样式-->
    <h2 class="bg-info text-white p-3  text-center">排号等位管理</h2>
    <#--左边栏-->
    <#include "../zujian/left.ftlh">


    <#--主要内容content-->
    <div id="page-content-wrapper">
        <button class="btn btn-warning text-white my-3 offset-md-1">
            小桌排号管理
        </button>
        <div class="row clearfix">
            <div class="col-md-10 offset-md-1 column">
                <table class="table table-bordered table-hover">
                    <thead>
                    <tr class="table-info">
                        <th>客户排号</th>
                        <th>排号开始时间</th>
                        <th colspan="1">操作</th>
                    </tr>
                    </thead>
                    <tbody>

                    <#list listSmall as category>
                        <tr>
                            <td>${category.num}</td>
                            <td>${category.createTime}</td>
                            <td><a href="/diancan/adminPaihao/ruzhuo?id=${category.id}">可入座</a></td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
        <button class="btn btn-warning text-white my-3 offset-md-1">
            大桌排号管理
        </button>
        <div class="row clearfix">
            <div class="col-md-10 offset-md-1 column">
                <table class="table table-bordered table-hover">
                    <thead>
                    <tr class="table-info">
                        <th>客户排号</th>
                        <th>排号开始时间</th>
                        <th colspan="1">操作</th>
                    </tr>
                    </thead>
                    <tbody>

                    <#list listBig as category>
                        <tr>
                            <td>${category.num}</td>
                            <td>${category.createTime}</td>
                            <td><a href="/diancan/adminPaihao/ruzhuo?id=${category.id}">可入座</a></td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div>
</body>
</html>