package com.greenjiao.coc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * mp代码生成器
 */
@SuppressWarnings("all")
public class Generator {
    private static final String url = "jdbc:mysql://localhost:3306/coc?serverTimezone=Asia/Shanghai";
    private static final String username = "root";
    private static final String password = "2662334834";
    private static final String parentName = "com.greenjiao";
    private static final String moduleName = "coc";
    private static final String author = "yan";
    private static final String outputDir = "C:\\Users\\Yan\\Desktop\\ry\\COC\\src\\main\\java";
    private static final List<String> tables = new ArrayList<>(Arrays.asList("tb_user"));

    public static void main(String[] args) {
        FastAutoGenerator.create(url, username, password)
                /*
                全局配置
                 */
                .globalConfig(builder -> {
                    builder.author(author)
                            // 输出目录
                            .outputDir(outputDir)
                            .disableOpenDir()
                            // 时间策略
                            .dateType(DateType.TIME_PACK)
                            // 注释日期格式
                            .commentDate("yyyy-MM-dd");
                })
                /*
                包配置
                 */
                .packageConfig(builder -> {
                    builder.parent(parentName) // 父包名
                            .moduleName(moduleName)      // 模块名
                            .entity("bean")         // 实体类包名
                            .service("service")     // service包名
                            .serviceImpl("service.impl")
                            .mapper("mapper")
                            .xml("mapper.xml")
                            .controller("controller");
                })
                /*
                模板配置
                 */
                .templateEngine(new FreemarkerTemplateEngine()) //设置模板引擎，默认的是velocity
                .templateConfig(builder -> {
                    builder
                            .entity("/templates/entity.java")
                            .xml("/templates/mapper.xml");
                })
                /*
                策略配置
                 */
                .strategyConfig(builder -> {
                    builder.enableSkipView()
                            .disableSqlFilter()
                            // 添加匹配表名，等于这个名称才会自动生成代码，如果需要直接生成一个库中所有表可以不要匹配规则
                            // 当然还有其他匹配规则，请前往官网查看
                            .addInclude(tables);
                })
                /*
                实体类策略配置
                 */
                .strategyConfig(builder -> {
                    builder.entityBuilder()
//                            .enableFileOverride()
                            .disableSerialVersionUID()              //禁用生成 serialVersionUID
                            .enableLombok()                         //开启 lombok
                            .enableTableFieldAnnotation()           //开启生成字段注解
                            //数据表映射到实体命名策略，下划线转驼峰命名
                            .naming(NamingStrategy.underline_to_camel)
                            //字段映射命名策略
                            .columnNaming(NamingStrategy.underline_to_camel)
                            //主键策略，为空则雪花算法
                            .idType(IdType.ASSIGN_ID);
                })
                /*
                Controller策略配置
                 */
                .strategyConfig(builder -> {
                    builder.controllerBuilder()
//                            .enableFileOverride()
                            .enableHyphenStyle()
                            .enableRestStyle()
                            .formatFileName("%sController");
                })
                /*
                Service策略配置
                 */
                .strategyConfig(builder -> {
                    builder.serviceBuilder()
//                            .enableFileOverride()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl");
                })
                /*
                Mapper策略配置
                 */
                .strategyConfig(builder -> {
                    builder.mapperBuilder()
                            .enableFileOverride()
                            .mapperAnnotation(Mapper.class) // enableMapperAnnotation 将弃用，改为mapperAnnotation
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sMapper");
                })
                .execute();
    }
}
