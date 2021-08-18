<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        /*.fm {*/
        /*    font-size: 40px;*/
        /*    color: green;*/
        /*}*/
    </style>
</head>
<body>
<div class="fm">
    <#include "top.html"/>
    <#--    你好啊。。。。 ${name}-->
    <#--    <#list list as item>-->
    <#--        ${item}<br>-->
    <#--    </#list>-->
    <#--    <h3>if语句</h3>-->
    <#--    <#if (age>=18)>-->
    <#--        成年了-->
    <#--    </#if>-->
    <#--    <h3>if else 语句</h3>-->
    <#--    <#if (age>=18)>-->
    <#--        if成立了，你成年了-->
    <#--    <#else >-->
    <#--        else成立了，你还未成年-->
    <#--    </#if>-->
    <#if (age>=12)>
        中学生
    <#elseif (age>6)>
        小学生
    <#else >
        幼儿园
    </#if>
    <#include "foot.ftlh"/>

</div>

</body>
</html>