package com.finance.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by zt on 2017/1/22.
 */
public class Indexer {

    /**
     * 写索引实例
     */
    private IndexWriter indexWriter;

    public Indexer(String indexDir) {
        try {
            Directory dir = FSDirectory.open(Paths.get(indexDir));
            // 标准分词器
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(dir, indexWriterConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭写索引
     */
    public void close() {
        try {
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 索引指定目录的所有文件
     */
    public int index(String dataDir) {
        File[] files = new File(dataDir).listFiles();
        for (File file : files) {
            indexFile(file);
        }
        return indexWriter.numDocs();
    }

    /**
     * 索引指定文件
     */
    private void indexFile(File file) {
        try {
            System.out.println("索引文件：" + file.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document document = getDocument(file);
        try {
            indexWriter.addDocument(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文档，文档里再设置每个字段
     */
    private Document getDocument(File file) {
        Document document = new Document();
        try {
            document.add(new TextField("contents", new FileReader(file)));
            document.add(new TextField("fileName", file.getName(), Field.Store.YES));
            document.add(new TextField("fullPath", file.getCanonicalPath(), Field.Store.YES));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return document;
    }

    public static void main(String[] args) {
        String indexDir = "G:\\workspace\\lucene";
        String dataDir = "G:\\workspace\\lucene\\data";
        Indexer indexer = new Indexer(indexDir);
        indexer.index(dataDir);
        int indexedNumber = indexer.index(dataDir);
        System.out.println(indexedNumber);
        indexer.close();
    }
}
