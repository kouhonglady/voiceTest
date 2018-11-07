package com.hrg.lucene;


import java.io.*;
import java.util.Iterator;
import org.json.JSONException;
import com.hrg.lucene.IndexManager.PCpair;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
* 创建时间：创建时间：2018年4月27日 上午9:44:37
* 项目名称：voiceTest
* @author lingxue
* @version 1.0
* @since JDK 1.8
* 文件名称：IndexManagerHelper.java
* 类说明：这个类是用于Lucene检索的帮助类
*/



public class IndexManagerHelper {
	
	int num=1;
	
	//static String root_path = index_test.class.getClassLoader().getResource("").getFile().replaceAll("/voiceTest/WebContent/WEB-INF/classes/", "");
	
	/** 判断索引是否已经建立  */
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
	        //建索引的过程
		 	System.out.println("Start making index.\n");
	        IndexManager im = new IndexManager();
	        String dir = "E:/study/hrg_project/environment/dataset/WechatCBC.xls";
	        try{
	        	Workbook book = Workbook.getWorkbook(new File(dir));
	        	//getSheet（0）表示的是得到第一个表格的内容
	        	Sheet sheet =book.getSheet(0);
	        	int rows =sheet.getRows();
	        	int cols =sheet.getColumns();
	        	System.out.println(rows+","+cols);
	        	String colname1 =sheet.getCell(0, 0).getContents().trim();
	        	String colname2 =sheet.getCell(1, 0).getContents().trim();
	        	String colname3 =sheet.getCell(2, 0).getContents().trim();
	        	System.out.println(colname1+","+colname2+","+colname3);
	        	for (int z = 0; z <rows; z++)
	        	{
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
	        //单条查询，即输入一个检索字段进行检索。检索结果为一个列表，通过返回Iterator
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
                    qres = im.search(question,0,20);//这里的question 为检索的关键字，（0,20）表示返回检索结果的前20条
                    System.out.println("result:");
                    return qres;
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	   
	    System.out.println("down!");
		return qres;
	    }
	
	/**
	 * 
	 * 这里的main 主要用于在检测系统的检索准确程度
	 */
    public static void main(String[] arg) {
    	
    	 String dir = "E:/study/hrg_project/environment/dataset/precision_data/test_new1028.xls";
    	 
    	 
	        try{
	        	Workbook book = Workbook.getWorkbook(new File(dir));
	        	//0浠ｈ〃绗竴涓伐浣滆〃瀵硅薄
	        	
	        	WritableWorkbook book_write = book.createWorkbook(new File(dir),book);
	        	//WritableSheet sheet =book.getSheet(0);
	        	WritableSheet sheet = book_write.getSheet(0);
	        	int rows =sheet.getRows();
	        	int cols =sheet.getColumns();
	        	System.out.println(rows+","+cols);
	        	for (int z = 0; z <rows; z++)
	        	{//0浠ｈ〃鍒楁暟锛寊浠ｈ〃琛屾暟
	                String question1 =sheet.getCell(0, z).getContents();
	                String question2 =sheet.getCell(1, z).getContents();
	                
	                Iterator<IndexManager.PCpair> qres1 = IndexManagerHelper.first_search(question1);
	                Iterator<IndexManager.PCpair> qres2 = IndexManagerHelper.first_search(question2);
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
		String realPath = IndexManagerHelper.class.getClassLoader().getResource("").getFile();  		
		java.io.File file = new java.io.File(realPath);  		
		realPath = file.getAbsolutePath();	
		try {  			
			realPath = java.net.URLDecoder.decode(realPath, "utf-8");  		
			} 
		catch (Exception e) {  			
			e.printStackTrace();  		
			}  		
		return realPath;  
	}

}
