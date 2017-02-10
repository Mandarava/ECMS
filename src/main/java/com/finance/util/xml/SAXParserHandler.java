package com.finance.util.xml;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zt on 2017/2/6.
 */
public class SAXParserHandler extends DefaultHandler {

    private String value;

    private Book book = new Book();

    private List<Book> bookList = new ArrayList<>();

    public List<Book> getBookList() {
        return bookList;
    }

    /**
     * 用来遍历xml文件的开始标签
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("book")) {
            // 已知属性名称的情况
            // String value = attributes.getValue("id");
            // 未知的情况
            for (int i = 0; i < attributes.getLength(); i++) {
                String attr = attributes.getQName(i);
                String attrValue = attributes.getValue(i);
                book.setId(attrValue);
                System.out.println("key : " + attr + " value: " + attrValue);
            }
        }
    }

    /**
     * 用来遍历xml文件的结束标签
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equals("book")) {
            bookList.add(book);
            book = new Book();
        } else if (qName.equals("name")) {
            book.setName(value);
        } else if (qName.equals("author")) {
            book.setAuthor(value);
        } else if (qName.equals("year")) {
            book.setYear(value);
        } else if (qName.equals("price")) {
            book.setPrice(value);
        } else if (qName.equals("language")) {
            book.setLanguage(value);
        }
    }

    /**
     * 用来标识解析开始
     */
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    /**
     * 用来标识解析结束
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        value = new String(ch, start, length);
        if (!StringUtils.isEmpty(value.trim())) {
            System.out.println("节点值是:" + value);
        }
    }
}
