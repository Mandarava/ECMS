package com.finance.util.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by zt on 2017/2/6.
 */
public class XMLParser {

    public static void main(String[] args) {
        XMLParser xmlUtil = new XMLParser();
        xmlUtil.parseXML("src/main/java/com/finance/util/xml/books.xml");
    }

    /**
     *  一次性加载到内存生成DOM树
     * @param fileURI
     */
    public void parseXML(String fileURI) {
        // 创建一个DocumentBuilderFactory的对象
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            //创建一个DocumentBuilder的对象
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileURI);
            // 所有节点的集合
            NodeList nodeList = document.getElementsByTagName("book");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                // 获得节点下的所有属性
                NamedNodeMap namedNodeMap = node.getAttributes();
                for (int j = 0; j < namedNodeMap.getLength(); j++) {
                    Node attribute = namedNodeMap.item(j);
                    // 获取属性名
                    String nodeName = attribute.getNodeName();
                    //获取属性值
                    String nodeValue = attribute.getNodeValue();
                    System.out.println("属性名：" + nodeName + "  属性值： " + nodeValue);
                }

                /*// 已知节点下有且只有一个id属性------------
                Element element = (Element) nodeList.item(i);
                String attrValue = element.getAttribute("id");
                System.out.println("id属性的属性值为:" + attrValue);
                // -------------------*/
            }

            // 解析子节点
            NodeList childNodes = document.getChildNodes();
            System.out.println("共有 " + childNodes.getLength() + "个子节点");
            // 遍历childNodes 获取每个节点的节点名和节点值
            for (int i = 0; i < childNodes.getLength(); i++) {
                // 区软text类型的node以及element类型的node
                if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    // 获取element类型节点的节点名
                    String nodeName = childNodes.item(i).getNodeName();
                    //获取节点值  如该节点下还有节点,返回空  firstChild是element类型的value是空
                    String nodeValue = childNodes.item(i).getFirstChild().getNodeValue();
                    // 另一种获取节点值的方式，如该节点下还有节点 那么会获得多个值 eg: <name><a>abc</a>d</name>  returns abcd
                    nodeValue = childNodes.item(i).getTextContent();
                    System.out.println(nodeName);
                }
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
