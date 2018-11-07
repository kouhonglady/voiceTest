package com.hrg.lucene;

import java.io.IOException;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.QueryBuilder;
import org.apache.lucene.util.Version;

/**

* 创建时间：创建时间：2018年4月27日 上午10:47:22
* 项目名称：voiceTest
* @author lingxue
* @version 1.0
* @since JDK 1.8
* 文件名称：IndexManager.java
* 类说明：Lucene检索管理类，包括索引的创建，创建索引的关键字段，创建索引的方式等等内容
*/



public class IndexManager {
	
    private String indexPath = "E:/study/hrg_project/environment/dataset/index";  //索引路径

    private Analyzer analyzer = null;  //分析器，先定义数据类型，然后再初始化

    private RAMDirectory rDir = null;
    private FSDirectory fDir = null;

    private IndexWriterConfig config = null;
    private IndexWriter indexWriter = null;

    private Query query = null;
    private  QueryBuilder builder = null; 
    public IndexManager() throws IOException {  //初始化函数
    	
        analyzer = new SmartChineseAnalyzer();  // 创建一个处理中文的解析器

        rDir = new RAMDirectory();
        fDir = FSDirectory.open(new File(indexPath));  //打开索引
        openRam();

        builder = new QueryBuilder(analyzer);
    }

    public void exit() throws IOException {
        indexWriter.close();
        rDir.close();
        fDir.close();
    }

    /** 生成索引 **/
    public void openRam() throws IOException {
        config = new IndexWriterConfig(Version.LUCENE_4_10_4, analyzer);
        config.setOpenMode(OpenMode.CREATE_OR_APPEND);  //创建或者追加
        indexWriter = new IndexWriter(rDir, config);
    }
    public void closeRam() throws IOException {
        indexWriter.close();
    }

    public void openFS() throws IOException {
        config = new IndexWriterConfig(Version.LUCENE_4_10_4, analyzer);
        config.setOpenMode(OpenMode.CREATE_OR_APPEND);  //创建或者追加
        indexWriter = new IndexWriter(fDir,config);
    }
    public void closeFS() throws IOException {
        indexWriter.close();
    }

    /** document 域选项 **/
    private Document getDocument(String id,String question,String answer){
        Document doc = new Document();
        doc.add(new Field("id",id,TextField.TYPE_STORED));
        doc.add(new Field("question",question,TextField.TYPE_STORED));
        doc.add(new Field("answer",answer,TextField.TYPE_STORED));
        return doc;
    }

    public  void addDocument(String id,String question,String answer) throws IOException {
        indexWriter.addDocument(getDocument(id,question,answer));
    }

    public void write() throws IOException { //写索引，把内存中的内容写到硬盘里面，然后清空RAM里面的内容
        closeRam();
        openFS();
        indexWriter.addIndexes(new Directory[]{rDir});
        indexWriter.forceMerge(1);
        indexWriter.commit();
        closeFS();
        openRam();
        indexWriter.deleteAll();
    }

    /** search **/
    DirectoryReader indexreader;
    IndexSearcher indexsearcher;

    //相似度
    public void openIndex() throws IOException {
        indexreader = DirectoryReader.open(fDir);
        indexsearcher = new IndexSearcher(indexreader);
        indexsearcher.setSimilarity(new BM25Similarity());
    }

    public void closeIndex() throws IOException {
        indexreader.close();
    }

    public Iterator<PCpair> search(String input,int start,int end) throws Exception {
        //pos = 0;
        if (input == null || input.length() == 0){
            return null;
        }
        setQuery(input);
        TopDocs tDocs = indexsearcher.search(query, end);

        ScoreDoc[] scoreDocs = tDocs.scoreDocs;
        List<PCpair> docs = new LinkedList<PCpair>();
        for (int i = start; i < scoreDocs.length; i ++){
            Document d = indexsearcher.doc(scoreDocs[i].doc);
            System.out.println(d.getValues("id")[0]+"-->"+d.getValues("question")[0]);
            docs.add(new PCpair(d.getValues("id")[0],d.getValues("question")[0],d.getValues("answer")[0],scoreDocs[i].score));
        }
        return docs.iterator();
    }

    //设置查询，组合下什么内容
    private void setQuery(String input){
        query = builder.createBooleanQuery("question", input);
    }

    
    /**
     * 
     * PCpair 为 IndexManager类的内部类
     * 用于检索过程中最小单元的描述，每个最小单元为检索得到一个问句对
     * 包含四个属性，i(id)表示句子对的编号
     * q(question) 问句对中的问题
     * a(answer) 问句对中的答案
     * s(score) 检索得分
     */
    public static class PCpair{
        String i;
        String q;
        String a;
        float s;
        public PCpair( String id, String question,String answer, float score) throws Exception{
            i = id;
            q = question;
            a= answer;
            s = score;
        }
        public String getId(){
            return i;
        }
        public String getQuestion(){
            return q;
        }
        public String getAnswer(){
            return a;
        }
        public float getScore(){
            return s;
        }
    }

}
