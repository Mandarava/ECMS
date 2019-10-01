package com.finance.util.excel;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 生成Excel报表
 */
public class Export {

    /**
     * 生成XLSX为后缀的Excel模板文件（Excel 2007/2010） eg:Export.Template3("model.Employee", "员工数据", al,
     * "F:/Template/n2.xlsx");
     *
     * @param object 保存数据的bean类的对象
     * @param title  报表的标题
     * @param list   保存每一列字段名的list（其中的顺序要和bean类定义字段的顺序一样）
     * @param path   生成Excel模板的路径
     */
    public static void Template1(Object object, String title, List list, String path) {
        Class objectClass = object.getClass();
        Field[] field = objectClass.getDeclaredFields();
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Sheet0");
        XSSFRow[] row = new XSSFRow[6];
        row[0] = sheet.createRow(0);
        Cell cell = row[0].createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(titleFormat(wb));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, field.length));
        row[2] = sheet.createRow(2);
        XSSFCell[] headerCell12 = new XSSFCell[list.size()];
        for (int i = 0; i < list.size(); i++) {
            headerCell12[i] = row[2].createCell(i);
            headerCell12[i].setCellValue(list.get(i).toString());
        }
        row[3] = sheet.createRow(3);
        Cell tagBegin = row[3].createCell(0);
        tagBegin.setCellValue("<jx:forEach items=\"${list}\" var=\"item\">");
        row[4] = sheet.createRow(4);
        XSSFCell[] headerCell14 = new XSSFCell[field.length];
        for (int i = 0; i < field.length; i++) {
            headerCell14[i] = row[4].createCell(i);
            headerCell14[i].setCellValue(new XSSFRichTextString(
                    "${item." + getField(field[i].toString()) + "}"));
        }
        row[5] = sheet.createRow(5);
        Cell tagEnd = row[5].createCell(0);
        tagEnd.setCellValue("</jx:forEach>");
        Output(wb, path);
    }

    /**
     * 生成XLSX为后缀的Excel模板文件
     *
     * @param objectClass 保存数据的bean的类类型
     * @param title       报表的标题
     * @param list        保存每一列字段名的list（其中的顺序要和bean类定义字段的顺序一样）
     * @param path        生成Excel模板的路径
     */
    public static void Template2(Class objectClass, String title, List list, String path) {
        Field[] field = objectClass.getDeclaredFields();
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Sheet0");
        XSSFRow[] row = new XSSFRow[6];
        row[0] = sheet.createRow(0);
        Cell cell = row[0].createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(titleFormat(wb));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, field.length));
        row[2] = sheet.createRow(2);
        XSSFCell[] headerCell12 = new XSSFCell[list.size()];
        for (int i = 0; i < list.size(); i++) {
            headerCell12[i] = row[2].createCell(i);
            headerCell12[i].setCellValue(list.get(i).toString());
        }
        row[3] = sheet.createRow(3);
        Cell tagBegin = row[3].createCell(0);
        tagBegin.setCellValue("<jx:forEach items=\"${list}\" var=\"item\">");
        row[4] = sheet.createRow(4);
        XSSFCell[] headerCell14 = new XSSFCell[field.length];
        for (int i = 0; i < field.length; i++) {
            headerCell14[i] = row[4].createCell(i);
            headerCell14[i].setCellValue(new XSSFRichTextString(
                    "${item." + getField(field[i].toString()) + "}"));
        }
        row[5] = sheet.createRow(5);
        Cell tagEnd = row[5].createCell(0);
        tagEnd.setCellValue("</jx:forEach>");
        Output(wb, path);
    }

    /**
     * 生成XLSX为后缀的Excel模板文件
     *
     * @param cPath 所需类的完全限定名
     * @param title 报表的标题
     * @param list  保存每一列字段名的list（其中的顺序要和bean类定义字段的顺序一样）
     * @param path  生成Excel模板的路径
     */
    public static void Template3(String cPath, String title, List list, String path) throws ClassNotFoundException {
        Class objectClass = Class.forName(cPath);
        Field[] field = objectClass.getDeclaredFields();
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Sheet0");
        XSSFRow[] row = new XSSFRow[6];
        row[0] = sheet.createRow(0);
        Cell cell = row[0].createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(titleFormat(wb));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, field.length));
        row[2] = sheet.createRow(2);
        XSSFCell[] headerCell12 = new XSSFCell[list.size()];
        for (int i = 0; i < list.size(); i++) {
            headerCell12[i] = row[2].createCell(i);
            headerCell12[i].setCellValue(list.get(i).toString());
        }
        row[3] = sheet.createRow(3);
        Cell tagBegin = row[3].createCell(0);
        tagBegin.setCellValue("<jx:forEach items=\"${list}\" var=\"item\">");
        row[4] = sheet.createRow(4);
        XSSFCell[] headerCell14 = new XSSFCell[field.length];
        for (int i = 0; i < field.length; i++) {
            headerCell14[i] = row[4].createCell(i);
            headerCell14[i].setCellValue(new XSSFRichTextString(
                    "${item." + getField(field[i].toString()) + "}"));
            if (field[i].toString().indexOf("java.util.Date") != -1) {
                headerCell14[i].setCellStyle(dateFormat(wb));
            }

        }
        row[5] = sheet.createRow(5);
        Cell tagEnd = row[5].createCell(0);
        tagEnd.setCellValue("</jx:forEach>");
        Output(wb, path);
    }

    /**
     * 生成Excel报表
     *
     * @param temPath Excel模板的路径
     * @param list    保存数据的bean的列表
     * @param desPath 生成Excel报表的路径
     */
    public static void Generate_reports(String temPath, List list, String desPath) {
        Map beans = new HashMap();
        beans.put("list", list);
        XLSTransformer transformer = new XLSTransformer();
        try {
            transformer.transformXLS(temPath, beans, desPath);
        } catch (ParsePropertyException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置单元格格式
     *
     * @return 返回设置的单元格格式
     */
    private static CellStyle titleFormat(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 20);
        font.setFontName("宋体");
        font.setColor((short) 10);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;

    }

    /**
     * 设置单元格日期格式
     */
    private static CellStyle dateFormat(XSSFWorkbook wb) {
        CreationHelper createHelper = wb.getCreationHelper();
        CellStyle dateStyle = wb.createCellStyle();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
        return dateStyle;
    }


    /**
     * 将生成的Excel模板输出的制定的路径
     */
    private static void Output(XSSFWorkbook wb, String path) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            wb.write(out);
        } catch (IOException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得类字段名的简写
     */
    private static String getField(String str) {
        int i = str.lastIndexOf(".");
        return str.substring(i + 1);
    }
}