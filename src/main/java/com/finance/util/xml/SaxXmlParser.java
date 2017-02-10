package com.finance.util.xml;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by zt on 2017/2/6.
 */
public class SaxXmlParser {

    public static void main(String[] args) {
        SaxXmlParser xmlUtil = new SaxXmlParser();
        xmlUtil.parseXML("src/main/java/com/finance/util/xml/books.xml");
    }

    /**
     * 采用事件驱动模式，每走到一个标签触发事件
     */
    public void parseXML(String fileURI) {
        // 获取SAXParserFactory的实例
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            // 获取SAXParser的实例
            SAXParser saxParser = saxParserFactory.newSAXParser();
            SAXParserHandler saxParserHandler = new SAXParserHandler();
            saxParser.parse(fileURI, saxParserHandler);
            System.out.println("~~~~" + saxParserHandler.getBookList().size());
            for (Book book : saxParserHandler.getBookList()) {
                System.out.println("id : " + book.getId());
                System.out.println("name : " + book.getName());
                System.out.println("author : " + book.getAuthor());
                System.out.println("year : " + book.getYear());
                System.out.println("price : " + book.getPrice());
                System.out.println("language : " + book.getLanguage());
                System.out.println("=====finish =====");
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
