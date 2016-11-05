package com.finance.util.excel;

import com.finance.dao.FundNetDao;
import com.finance.model.pojo.FundNet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zt on 2016/10/30.
 */
public final class ExportExcelUtil {

    public static void exportBigDataExcel(FundNetDao fundNetDao)
            throws IOException {
        String path = "G:/test.xlsx";
        // 最重要的就是使用SXSSFWorkbook，表示流的方式进行操作
        // 在内存中保持1000行，超过1000行将被刷新到磁盘
        SXSSFWorkbook wb = new SXSSFWorkbook(1000);
        // 建立新的sheet对象
        Sheet sh = wb.createSheet();
        // 创建第一行对象
        Row row = sh.createRow(0);
        // -----------定义表头-----------
        Cell cel0 = row.createCell(0);
        cel0.setCellValue("id");
        Cell cel2 = row.createCell(1);
        cel2.setCellValue("code");
        Cell cel3 = row.createCell(2);
        cel3.setCellValue("aaa");

        // ---------------------------
        List<FundNet> list = new ArrayList<>();
        // 数据库中存储的数据行
        int page_size = 10000;
        // 求数据库中待导出数据的行数
        int list_count = fundNetDao.findFundNetCount();
        list_count = 1000000;
        // 根据行数求数据提取次数
        int export_times = list_count % page_size > 0 ? list_count / page_size
                + 1 : list_count / page_size;
        // 按次数将数据写入文件
        for (int j = 0; j < export_times; j++) {
            list = fundNetDao.findFundNetPage(page_size, page_size * j);
            int len = list.size() < page_size ? list.size() : page_size;
            for (int i = 0; i < len; i++) {
                Row row_value = sh.createRow(j * page_size + i + 1);

                Cell cel0_value = row_value.createCell(0);
                cel0_value.setCellValue(list.get(i).getId());
                Cell cel2_value = row_value.createCell(1);
                cel2_value.setCellValue(list.get(i).getCode());
                Cell cel3_value = row_value.createCell(2);
                cel3_value.setCellValue(list.get(i).getUnitNetValue());

            }
            list.clear(); // 每次存储len行，用完了将内容清空，以便内存可重复利用
        }
        FileOutputStream fileOut = new FileOutputStream(path);
        wb.write(fileOut);
        fileOut.close();
        wb.dispose();
    }
}
