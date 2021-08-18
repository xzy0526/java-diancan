<html>
<#include "../zujian/top.ftlh">

<body>
<div id="wrapper" class="toggled">
    <#--内容区域头部样式-->
    <h2 class="bg-info text-white p-3  text-center">统计信息页</h2>
    <#--左边栏-->
    <#include "../zujian/left.ftlh">
    <#--主要内容content-->
    <#if !isAdmin??>
        <h2 class="text-danger m-5">您不是管理员，没有管理权限</h2>
    <#else >
        <div id="page-content-wrapper" class="container">
            <div class="row clearfix">
                <#--每月收入-->
                <div class="col-md-10 offset-md-1 column">
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr class="table-info">
                            <th>月份</th>
                            <th>月度收入(元)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list totalMoneyList as money>
                            <tr>
                                <td>${money.time!''}</td>
                                <td>${money.totalMoney!''}</td>
                            </tr>
                        </#list>
                        <tr>
                            <th>年度总收入(元)</th>
                            <td>${yearMoney!''}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <#--需要补货商品-->
                <div class="col-md-10 offset-md-1 column mt-5">
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr class="table-danger">
                            <th colspan="2">需要补货的菜品</th>
                        </tr>
                        <tr class="table-info">
                            <th>菜品名</th>
                            <th>库存</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list foodList as food>
                            <tr>
                                <td>${food.foodName!''}</td>
                                <td>${food.foodStock!''}</td>
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
