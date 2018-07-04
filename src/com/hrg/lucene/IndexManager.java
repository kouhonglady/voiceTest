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
 * Created by hyz
 */
public class IndexManager {
	/**init**/
    private String indexPath = "D:/project/environment/dataset/index";  //索引路径

    private Analyzer analyzer = null;  //分析器，先开始定义这些数据类型，后来再初始化

    private RAMDirectory rDir = null;
    private FSDirectory fDir = null;

    private IndexWriterConfig config = null;
    private IndexWriter indexWriter = null;

    private Query query = null;
    private  QueryBuilder builder = null;  //这些是用来干什么的
    public IndexManager() throws IOException {  //初始化函数
    	indexPath = "D:/project/environment/dataset/index";  //索引路径
        analyzer = new SmartChineseAnalyzer();  //处理中文的分析器,这里更之前的有些不同。

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

    /** build index **/
    public void openRam() throws IOException {
        config = new IndexWriterConfig(Version.LUCENE_4_10_4, analyzer);
        config.setOpenMode(OpenMode.CREATE_OR_APPEND);  //创建或者追加模式
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

    /** document 域选项！！ **/
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

    public void write() throws IOException { //写，就是把内存里的写到路径盘上，然后清空RAM里的！？
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

    //设置查询，组合下 什么的
    private void setQuery(String input){
        query = builder.createBooleanQuery("question", input);
    }

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
