package xyz.soulspace.restore.mybatis;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;

public class GeneratorGit {
    public static void main(String[] args) {
        String property = System.getProperty("user.dir");
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/TimeDB?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTime=Asia/shanghai&serverTimezone=UTC",
                        "root",
                        "")
                .globalConfig(builder -> {
                    builder.author("soulspace")
                            .dateType(DateType.TIME_PACK)
                            .commentDate("yyyy-MM-dd hh:mm:ss")
                            .outputDir(property + "/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("xyz.soulspace") // 设置父包名
                            .moduleName("cinemaline") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, property + "/src/main/resources/mapper"))
                    ;
                })
                .strategyConfig(builder -> {
                    builder.addInclude(
                                    "fms_film_tag",
                                    "fms_tag_film_relation",
                                    "fms_area_info"
//                                    "img_info"
//                                    "fms_member",
//                                    "fms_film_member_relation",
//                                    "main_cinema"
//                                    "main_film"
//                                    "sms_order",
//                                    "ums_comment",
//                                    "ums_user",
//                                    "cms_city"
                            )
                            .addTablePrefix("cms_", "ums_", "sms_", "fms_", "main_")
                            .addTableSuffix("_table")
                            .entityBuilder()
                            .fileOverride()
                            .disableSerialVersionUID()
                            .enableLombok()
                            .enableRemoveIsPrefix()
                            .enableTableFieldAnnotation()
                            .enableActiveRecord()
                            .addTableFills(new Column("create_time", FieldFill.INSERT))
                            .addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE))
                            .mapperBuilder()
                            .superClass(BaseMapper.class)
                            .enableMapperAnnotation()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .serviceBuilder()
                            .superServiceClass(IService.class)
                            .superServiceImplClass(ServiceImpl.class)
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImp")
                    ;
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}
