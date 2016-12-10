package com.finance.util.excel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * 从Excel文件中读取数据的类
 *
 * @author yefh07219
 */
public class ReadDataFromExcel {
    private static DocumentBuilderFactory dbf;
    private List<String> sharedStrings;

    public ReadDataFromExcel() {
        dbf = DocumentBuilderFactory.newInstance();
    }

    public static void main(String args[]) {
        try {
            ReadDataFromExcel rd = new ReadDataFromExcel();
            List<List<String>> list = rd.readFile("F:/4.xlsx");
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).size(); j++) {
                    System.out.print(list.get(i).get(j) + " ");
                }
                System.out.println();
            }
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取数据
     *
     * @param f 要读取的Excel文件路径
     */
    public List<List<String>> readFile(String f) throws IOException, SAXException, ParserConfigurationException {
        ZipFile xlsxFile = new ZipFile(new File(f));
        ZipEntry workbookXML = xlsxFile.getEntry("xl/workbook.xml");
        InputStream workbookXMLIS = xlsxFile.getInputStream(workbookXML);
        Document doc = dbf.newDocumentBuilder().parse(workbookXMLIS);
        NodeList nl = doc.getElementsByTagName("sheet");
        sharedStrings = readXml("F:/tem/0.xlsx");
        List<List<String>> data = new ArrayList<>();
        try {
            // 将node转化为element，用来得到每个节点的属性
            Element element = (Element) nl.item(0);
            ZipEntry sheetXML = xlsxFile.getEntry("xl/worksheets/sheet"
                    + element.getAttribute("sheetId").toLowerCase()
                    + ".xml");
            // 接着就要到解压文件夹里找到对应的name值的xml文件，比如在workbook.xml中有<sheet name="Sheet1" sheetId="1" r:id="rId1" />
            // 节点
            InputStream sheetXMLIS = xlsxFile.getInputStream(sheetXML);
            // 那么就可以在解压文件夹里的xl/worksheets下找到sheet1.xml,这个xml文件夹里就是包含的表格的内容
            Document sheetdoc = dbf.newDocumentBuilder().parse(sheetXMLIS);
            NodeList rowdata = sheetdoc.getElementsByTagName("row");
            for (int j = 0; j < rowdata.getLength(); j++) {
                // 得到每个行的格式
                Element row = (Element) rowdata.item(j);
                // 根据行得到每个行中的列
                NodeList columndata = row.getElementsByTagName("c");
                List<String> dataList = new ArrayList<>();
                for (int k = 0; k < columndata.getLength(); k++) {
                    Element column = (Element) columndata.item(k);
                    NodeList values = column.getElementsByTagName("v");
                    if (values.getLength() != 0) {
                        Element value = (Element) values.item(0);
                        if (column.getAttribute("t") != null
                                & column.getAttribute("t").equals("s")) {
                            // 如果是共享字符串则在sharedstring.xml里查找该列的值
                            dataList.add(sharedStrings.get(Integer
                                    .parseInt(value.getTextContent())));
                        } else {
                            dataList.add("".equals(value.getTextContent().trim()) ? " " : value.getTextContent().trim());
                        }
                    } else {
                        values = column.getElementsByTagName("is");
                        Element value1 = (Element) values.item(0);
                        NodeList value2 = value1.getElementsByTagName("t");
                        Element value = (Element) value2.item(0);
                        dataList.add("".equals(value.getTextContent().trim()) ? " " : value.getTextContent().trim());
                    }
                }
                data.add(dataList);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 读取Excel中的共享字符串
     *
     * @param file 要读取的Excel路径
     */
    public List<String> readXml(String file) throws SAXException,
            IOException, ParserConfigurationException {
        List<String> sharedStrings = null;
        ZipFile xlsxFile1 = new ZipFile(new File(file));
        ZipEntry sharedStringXML = xlsxFile1.getEntry("xl/sharedStrings.xml");
        InputStream sharedStringXMLIS = xlsxFile1.getInputStream(sharedStringXML);
        Document sharedString = dbf.newDocumentBuilder().parse(
                sharedStringXMLIS);
        NodeList str = sharedString.getElementsByTagName("t");
        if (str.getLength() > 0) {
            sharedStrings = new ArrayList<>();
            for (int n = 0; n < str.getLength(); n++) {
                Element element = (Element) str.item(n);
                sharedStrings.add(element.getTextContent());
            }
            return sharedStrings;
        } else {
            return null;
        }
    }

}
