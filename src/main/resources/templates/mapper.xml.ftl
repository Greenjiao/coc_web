<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

    <#if enableCache>
    <!-- 开启二级缓存 -->
    <cache type="${cacheClassName}"/>

    </#if>
    <#if baseResultMap>
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
        <#list table.fields as field>
            <#if field.keyFlag><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
            </#if>
        </#list>
        <#list table.commonFields as field><#--生成公共字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
        </#list>
        <#list table.fields as field>
            <#if !field.keyFlag><#--生成普通字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
            </#if>
        </#list>
    </resultMap>

    </#if>
    <#if baseColumnList>
    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
        <#list table.fields as field>`${field.columnName}`<#if field_has_next=false><#break></#if>, <#if field_index%4=0>${"\n\t\t"}</#if></#list>
        <#--            ${table.fieldNames}-->
    </sql>

    <sql id="Join_Column_List">
        <#list table.fields as field>${r"${table}"}`${field.columnName}`<#if field_has_next=false><#break></#if>, <#if field_index%4=0>${"\n\t\t"}</#if></#list>
    </sql>
    <sql id="Batch_Insert_Column_List">
        <#list table.fields as field>${r"#{item."}${toCamelCase(field.columnName)}}<#if field_has_next=false><#break></#if>,<#if field_index%4=0>${"\n\t\t"}</#if></#list>
    </sql>
    </#if>
</mapper>

<#function toCamelCase columnName>
    <#list columnName?split('_') as word>
        <#if word?index == 0>
            <#assign result = word?lower_case>
        <#else>
            <#assign result = result + word?cap_first>
        </#if>
    </#list>
    <#return result>
</#function>
