package com.finance.util.excel;

import com.finance.dao.FundNetDao;
import com.finance.model.dto.Config;
import com.finance.model.pojo.FundNet;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by zt on 2016/10/30.
 */
@Component
public final class ExportExcelUtil {

    private static Config config;
    @Autowired
    private Config configTemp;

    public static void exportBigDataExcel(List<String> headers, String fileName, FundNetDao fundNetDao)
            throws IOException {
        // 最重要的就是使用SXSSFWorkbook，表示流的方式进行操作
        // 在内存中保持1000行，超过1000行将被刷新到磁盘
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(1000);
        // 建立新的sheet对象
        Sheet sh = sxssfWorkbook.createSheet("Sheet0");
        // 创建第一行对象,定义表头
        Row row = sh.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            String head = headers.get(i);
            row.createCell(i).setCellValue(head);
        }
        // 数据库中存储的数据行
        int page_size = 10000;
        int listCount = fundNetDao.findFundNetCount();
        int exportTimes = listCount % page_size > 0 ? listCount / page_size
                + 1 : listCount / page_size;
        List<FundNet> list = null;
        for (int j = 0; j < exportTimes; j++) {
            list = fundNetDao.findFundNetPage(page_size, page_size * j);
            int len = list.size() < page_size ? list.size() : page_size;
            for (int i = 0; i < len; i++) {
                Row rowValue = sh.createRow(j * page_size + i + 1);
                rowValue.createCell(0).setCellValue(list.get(i).getId());
                rowValue.createCell(1).setCellValue(list.get(i).getCode());
                rowValue.createCell(2).setCellValue(list.get(i).getUnitNetValue());
            }
            list.clear();
        }
        output(sxssfWorkbook, fileName);
        sxssfWorkbook.dispose();
    }

    private static void output(SXSSFWorkbook sxssfWorkbook, String fileName) {
        String path = config.getFilePath() + fileName + ".xlsx";
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            sxssfWorkbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    private void init() {
        config = this.configTemp;
    }

    protected void generateExcel(Map<String, Object> beanParams, String templateExcelName)
            throws ParsePropertyException, IOException, InvalidFormatException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(templateExcelName), beanParams).write(os);
        InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        os.close();
    }
}
