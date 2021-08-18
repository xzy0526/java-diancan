<html>
<#include "../zujian/top.ftlh">

<body>
<div id="wrapper" class="toggled">
    <#--内容区域头部样式-->
    <h4 class="bg-info text-white p-1  text-center">客户评价</h4>
    <#--左边栏-->
    <#include "../zujian/left.ftlh">
    <#--主要内容content-->
    <div id="page-content-wrapper" class="container-fluid">
        <div class="row clearfix">
            <div class="col-md-12 column">
                <table class="table  table-striped table-bordered table-sm">
                    <thead>
                    <tr class="table-info">
                        <th>评价id</th>
                        <th>用户名</th>
                        <th>评价内容</th>
                        <th>创建时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list commentPage.content as comment>
                        <tr>
                            <td>${comment.commentId}</td>
                            <td>${comment.name}</td>
                            <td>${comment.content}</td>
                            <td>${comment.createTime}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            <#--分页-->
            <div class="col-md-12 column">
                <ul class="pagination">
                    <#if currentPage lte 1>
                        <li class="page-item disabled"><a class="page-link" href="#">上一页</a></li>
                    <#else>
                        <li class="page-item"><a class="page-link"
                                                 href="/diancan/comment/list?page=${currentPage - 1}&size=${size}">上一页</a>
                        </li>
                    </#if>

                    <#list 1..commentPage.getTotalPages() as index>
                        <#if currentPage == index>
                            <li class="page-item disabled"><a class="page-link" href="#">${index}</a></li>
                        <#else>
                            <li class="page-item"><a class="page-link"
                                                     href="/diancan/comment/list?page=${index}&size=${size}">${index}</a>
                            </li>
                        </#if>
                    </#list>

                    <#if currentPage gte commentPage.getTotalPages()>
                        <li class="page-item disabled"><a class="page-link" href="#">下一页</a></li>
                    <#else>
                        <li class="page-item"><a class="page-link"
                                                 href="/diancan/comment/list?page=${currentPage + 1}&size=${size}">下一页</a>
                        </li>
                    </#if>
                </ul>
            </div>
        </div>
    </div>
</div>
</body>
</html>
