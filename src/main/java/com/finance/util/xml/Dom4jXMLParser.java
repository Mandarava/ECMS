package com.finance.util.xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zt on 2017/2/6.
 */
public class Dom4jXMLParser<T> {

    public static void main(String[] args) {
        Dom4jXMLParser xmlUtil = new Dom4jXMLParser<Book>();
        Book book = new Book();
        List<Book> books = xmlUtil.parseXML("src/main/java/com/finance/util/xml/books.xml",book);
    }

    private List<T> parseXML(String fileURL, T t) {
        List<T> result = new ArrayList<>();
        Field[] fields = t.getClass().getDeclaredFields();
        Map<String,Field> fieldMap = new HashMap<>();
        for (int i = 0; i <fields.length; i++) {
            fieldMap.put(fields[i].getName(),fields[i]);
        }
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new File(fileURL));
            Element rootElement = document.getRootElement();
            Iterator<Element> iterator = rootElement.elementIterator(t.getClass().getSimpleName());
            while (iterator.hasNext()) {
                t = (T) t.getClass().newInstance();
                Element element = iterator.next();
                List<Attribute> attributes = element.attributes();
                for (Attribute attribute : attributes) {
                    if(fieldMap.containsKey(attribute.getName())) {
                        Field field = fieldMap.get(attribute.getName());
                        Method setMethod = t.getClass().getMethod(
                                "set"
                                        + field.getName().substring(0, 1)
                                        .toUpperCase()
                                        + field.getName().substring(1),field.getType());
                        setMethod.invoke(t,attribute.getValue());
                    }
                }
                Iterator<Element> iterator1 = element.elementIterator();
                while (iterator1.hasNext()) {
                    Element node = iterator1.next();
                    if(fieldMap.containsKey(node.getName())) {
                        Field field = fieldMap.get(node.getName());
                        Method setMethod = t.getClass().getMethod(
                                "set"
                                        + field.getName().substring(0, 1)
                                        .toUpperCase()
                                        + field.getName().substring(1),field.getType());
                        setMethod.invoke(t,node.getStringValue());
                    }
                }
                result.add(t);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void parseXML(String fileURL) {
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new File(fileURL));
            Element rootElement = document.getRootElement();
            Iterator<Element> iterator = rootElement.elementIterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();
                List<Attribute> attributes = element.attributes();
                for (Attribute attribute : attributes) {
                    String name = attribute.getName();
                    String value = attribute.getValue();
                    System.out.println(name + ":" + value);
                }
                Iterator<Element> iterator1 = element.elementIterator();
                while (iterator1.hasNext()) {
                    Element node = iterator1.next();
                    String name = node.getName();
                    String value = node.getStringValue();
                    System.out.println(name + ":" + value);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public List<Node> xmlElements(String xmlDoc) {
        List<Node> result;
        try {
            Document document = DocumentHelper.parseText(xmlDoc);
            result = treeWalk(document.getRootElement());
            if (result != null && result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                    Node node = result.get(i);
                    System.out.println(node.getParent().getName() + ":" + node.getText());
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Node> treeWalk(Element element) {
        List<Node> result = new ArrayList<Node>();
        for (int i = 0, size = element.nodeCount(); i < size; i++) {
            Node node = element.node(i);
            if (node instanceof Element) {
                result.addAll(treeWalk((Element) node));
            } else {
                result.add(node);
            }
        }
        return result;
    }

}
