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
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
public class Generator {
    public static void main(String[] args) {
        String property = System.getProperty("user.dir");
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/ImageRestoreDB?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTime=Asia/shanghai&serverTimezone=UTC",
                        "root",
                        "MYSQLSoulSpace1114!")
                .globalConfig(builder -> {
                    builder.author("soulspace")
                            .dateType(DateType.TIME_PACK)
                            .commentDate("yyyy-MM-dd hh:mm:ss")
                            .outputDir(property + "/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("xyz.soulspace") // 设置父包名
                            .moduleName("restore") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, property + "/src/main/resources/mapper"))
                    ;
                })
                .strategyConfig(builder -> {
                    builder.addInclude(
                            "im_i_origin_small_relation"
                            )
                            .addTablePrefix("main_", "um_t_", "im_i_")
                            .addTableSuffix("_table")
                            .addFieldPrefix("is_")
                            .entityBuilder()
                            .enableFileOverride()
                            .disableSerialVersionUID()
                            .enableLombok()
                            .enableRemoveIsPrefix()
                            .enableTableFieldAnnotation()
                            .enableActiveRecord()
                            .addTableFills(new Column("create_time", FieldFill.INSERT))
                            .addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE))
                            .mapperBuilder()
                            .enableFileOverride()
                            .mapperAnnotation(org.apache.ibatis.annotations.Mapper.class)
                            .superClass(BaseMapper.class)
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .serviceBuilder()
                            .superServiceClass(IService.class)
                            .superServiceImplClass(ServiceImpl.class)
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImp")
                            .enableFileOverride()
                            .controllerBuilder()
                            .enableFileOverride()
                    ;
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}
