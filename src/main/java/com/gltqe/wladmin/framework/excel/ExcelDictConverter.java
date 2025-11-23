package com.gltqe.wladmin.framework.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.gltqe.wladmin.commons.utils.DictUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author gltqe
 * @date 2025/2/19 16:17
 */
public class ExcelDictConverter implements Converter<Object> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return Converter.super.supportJavaTypeKey();
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return Converter.super.supportExcelTypeKey();
    }

    @Override
    public Object convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return Converter.super.convertToJavaData(cellData, contentProperty, globalConfiguration);
    }

    @Override
    public WriteCellData<?> convertToExcelData(Object value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (value == null) {
           return new WriteCellData<>("");
        }
        Dict annotation = contentProperty.getField().getAnnotation(Dict.class);
        String dictText = DictUtil.getDictText(annotation, value);
        if (StringUtils.isBlank(dictText)) {
            return new WriteCellData<>(String.valueOf(value));
        } else {
            return new WriteCellData<>(dictText);
        }
    }
}
