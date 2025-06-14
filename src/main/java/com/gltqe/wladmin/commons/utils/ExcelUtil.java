package com.gltqe.wladmin.commons.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.gltqe.wladmin.commons.base.BaseDto;
import com.gltqe.wladmin.commons.common.Constant;
import jakarta.servlet.http.HttpServletResponse;


import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author gltqe
 * @date 2024/5/13 16:49
 */
public class ExcelUtil {

    public static<T> void writeExcel(List<T> data, String fileName, String sheetName, HttpServletResponse response, Class<T> clazz){
        try {
            OutputStream outputStream = getOutputStream(fileName, response);
            EasyExcel.write(outputStream, clazz)
                    .excelType(ExcelTypeEnum.XLSX).sheet(sheetName)
                    .doWrite(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 设置请求
     */
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment;filename=" +  fileName + ".xlsx" );
//        response.setHeader("Content-Disposition",  "attachment;filename*=utf-8'zh_cn'"+fileName+"."+ ExcelTypeEnum.XLSX);
        return response.getOutputStream();
    }

}
