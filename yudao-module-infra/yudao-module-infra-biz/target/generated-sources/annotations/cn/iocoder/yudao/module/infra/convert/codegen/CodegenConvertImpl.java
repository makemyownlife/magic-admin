package cn.iocoder.yudao.module.infra.convert.codegen;

import cn.iocoder.yudao.module.infra.dal.dataobject.codegen.CodegenColumnDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.codegen.CodegenTableDO;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.ibatis.type.JdbcType;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-08T14:24:37+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.4 (Oracle Corporation)"
)
public class CodegenConvertImpl implements CodegenConvert {

    @Override
    public CodegenTableDO convert(TableInfo bean) {
        if ( bean == null ) {
            return null;
        }

        CodegenTableDO codegenTableDO = new CodegenTableDO();

        codegenTableDO.setTableName( bean.getName() );
        codegenTableDO.setTableComment( bean.getComment() );

        return codegenTableDO;
    }

    @Override
    public List<CodegenColumnDO> convertList(List<TableField> list) {
        if ( list == null ) {
            return null;
        }

        List<CodegenColumnDO> list1 = new ArrayList<CodegenColumnDO>( list.size() );
        for ( TableField tableField : list ) {
            list1.add( convert( tableField ) );
        }

        return list1;
    }

    @Override
    public CodegenColumnDO convert(TableField bean) {
        if ( bean == null ) {
            return null;
        }

        CodegenColumnDO codegenColumnDO = new CodegenColumnDO();

        codegenColumnDO.setColumnName( bean.getName() );
        codegenColumnDO.setDataType( getDataType( beanMetaInfoJdbcType( bean ) ) );
        codegenColumnDO.setColumnComment( bean.getComment() );
        codegenColumnDO.setNullable( beanMetaInfoNullable( bean ) );
        codegenColumnDO.setPrimaryKey( bean.isKeyFlag() );
        codegenColumnDO.setJavaType( beanColumnTypeType( bean ) );
        codegenColumnDO.setJavaField( bean.getPropertyName() );

        return codegenColumnDO;
    }

    private JdbcType beanMetaInfoJdbcType(TableField tableField) {
        TableField.MetaInfo metaInfo = tableField.getMetaInfo();
        if ( metaInfo == null ) {
            return null;
        }
        return metaInfo.getJdbcType();
    }

    private Boolean beanMetaInfoNullable(TableField tableField) {
        TableField.MetaInfo metaInfo = tableField.getMetaInfo();
        if ( metaInfo == null ) {
            return null;
        }
        return metaInfo.isNullable();
    }

    private String beanColumnTypeType(TableField tableField) {
        IColumnType columnType = tableField.getColumnType();
        if ( columnType == null ) {
            return null;
        }
        return columnType.getType();
    }
}
