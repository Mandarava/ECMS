package com.finance.util.xml;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by zt on 2017/2/6.
 */
public class JdomParser {

    public static void main(String[] args) {
        JdomParser xmlUtil = new JdomParser();
        xmlUtil.parseXML("src/main/java/com/finance/util/xml/books.xml");
    }

    public void parseXML(String fileURI) {
        SAXBuilder saxBuilder = new SAXBuilder();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileURI), "UTF-8");
            Document document = saxBuilder.build(inputStreamReader);
            Element rootElement = document.getRootElement();
            List<Element> elements = rootElement.getChildren();
            for (Element element : elements) {
                List<Attribute> attributes = element.getAttributes();
                // 知道节点下属性名称
                // element.getAttributeValue("id");
                for (Attribute attribute : attributes) {
                    // 属性名
                    String name = attribute.getName();
                    // 属性值
                    String value = attribute.getValue();
                    System.out.println(name + ":" + value);
                }
                List<Element> nodeList = element.getChildren();
                for (Element node : nodeList) {
                    System.out.println(node.getName() + ":" + node.getValue());
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
