package com.hrg.lucene;


import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;
import org.json.JSONException;
import com.hrg.lucene.IndexManager.PCpair;
import com.hrg.voice.synthesize.MyThread;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by lingxue
 * 
 * 
 */



public class index_test {
	//This is a test
	int num=1;
	
	//static String root_path = index_test.class.getClassLoader().getResource("").getFile().replaceAll("/voiceTest/WebContent/WEB-INF/classes/", "");
	
	/** 判断索引是否已经创建*/
 public static Boolean findIndex() {
	    	File file = new File("E:/study/hrg_project/environment/dataset/index/segments.gen");	    	
	    	 if (file.exists()) {
	    		 System.out.println("file exists");
	    		 return true;
	    		 }
	    	 System.out.println("file not exists");
	    	 return false;	
	    }
	public static void build_index_test() throws IOException {
	        //这个是建索引的过程
		 	System.out.println("Start making index.\n");
	        IndexManager im = new IndexManager();
	        String dir = "E:/study/hrg_project/environment/dataset/WechatCBC.xls";
	        try{
	        	Workbook book = Workbook.getWorkbook(new File(dir));
	        	//0代表第一个工作表对象
	        	Sheet sheet =book.getSheet(0);
	        	int rows =sheet.getRows();
	        	int cols =sheet.getColumns();
	        	System.out.println(rows+","+cols);
	        	String colname1 =sheet.getCell(0, 0).getContents().trim();
	        	String colname2 =sheet.getCell(1, 0).getContents().trim();
	        	String colname3 =sheet.getCell(2, 0).getContents().trim();
	        	System.out.println(colname1+","+colname2+","+colname3);
	        	for (int z = 0; z <rows; z++)
	        	{//0代表列数，z代表行数
	        		String id =sheet.getCell(0, z).getContents();
	                String question =sheet.getCell(1, z).getContents();
	                String answer =sheet.getCell(2, z).getContents();
	                im.addDocument(id,question,answer);
	               }
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	        System.out.println("Start to write down");
	        im.write();
	        System.out.println("Index down");
	    }

	public static void query_test(String tmpquery) throws Exception {
	        //单条查询，取前六，因为第一个肯定是原来的问题
	        IndexManager qim = new IndexManager();
	        qim.openIndex();
	        Iterator<IndexManager.PCpair> qres = qim.search(tmpquery,0,14);
	        while(qres.hasNext()){
	            IndexManager.PCpair pcpair = qres.next();
	            System.out.println(pcpair.getAnswer());
	        }
	    }
	public static Iterator<IndexManager.PCpair> first_search(String question) throws IOException, JSONException {
		
	        Iterator<IndexManager.PCpair> qres=null;
	        
	        IndexManager im = null;

	        try{
	            im = new IndexManager();
	            im.openIndex();        
	            System.out.println(question);
	        	
                    qres = im.search(question,0,20);
                    System.out.println("result:");
                    return qres;
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	   
	    System.out.println("down!");
		return qres;
	    }
    public static void main(String[] arg) {
    	
    	 String dir = "E:/study/hrg_project/environment/dataset/precision_data/test_new.xls";
    	 
    	 
	        try{
	        	Workbook book = Workbook.getWorkbook(new File(dir));
	        	//0代表第一个工作表对象
	        	
	        	WritableWorkbook book_write = book.createWorkbook(new File(dir),book);
	        	//WritableSheet sheet =book.getSheet(0);
	        	WritableSheet sheet = book_write.getSheet(0);
	        	int rows =sheet.getRows();
	        	int cols =sheet.getColumns();
	        	System.out.println(rows+","+cols);
	        	for (int z = 0; z <rows; z++)
	        	{//0代表列数，z代表行数
	                String question1 =sheet.getCell(0, z).getContents();
	                String question2 =sheet.getCell(1, z).getContents();
	                
	                Iterator<IndexManager.PCpair> qres1 = index_test.first_search(question1);
	                Iterator<IndexManager.PCpair> qres2 = index_test.first_search(question2);
	                String ans1 = "";
	                String ans2 = "";
	                String qres_ans1 = "";
	                String qres_ans2 = "";
	                
	                if(qres1 != null) {
		                while(qres1.hasNext()) {
		                	 PCpair  temp = qres1.next();
		                	qres_ans1 += temp.getAnswer()+"_";
		                	break;
		                }
	                	Label label1 = new Label(3, z, qres_ans1);
	                	sheet.addCell(label1);
		                System.out.println(ans1);
	                }
	                if(qres2 != null) {
		                while(qres2.hasNext()) {
		                	 PCpair  temp = qres2.next();
		                	ans2 += temp.getQuestion()+"_";
		                	qres_ans2 += temp.getAnswer()+"_";
		                }
		                Label label = new Label(2, z, ans2);
	                	sheet.addCell(label);
	                	Label label2 = new Label(4, z, qres_ans2);
	                	sheet.addCell(label2);
		                System.out.println(ans2);
	                }
	                
	              }
	        	book_write.write();
	        	book_write.close();
	        	book.close();
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
    }
    
	public static String getRealPath() {  		
		String realPath = index_test.class.getClassLoader().getResource("").getFile();  		
		java.io.File file = new java.io.File(realPath);  		
		realPath = file.getAbsolutePath();//去掉了最前面的斜杠/		
		try {  			
			realPath = java.net.URLDecoder.decode(realPath, "utf-8");  		
			} 
		catch (Exception e) {  			
			e.printStackTrace();  		
			}  		
		return realPath;  
	}

}
