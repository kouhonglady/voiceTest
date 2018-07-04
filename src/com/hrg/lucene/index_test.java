package com.hrg.lucene;


import java.io.*;
import java.util.Iterator;

import org.json.JSONException;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by hyz.
 */
public class index_test {
	//This is a test
	int num=1;
	 /** 判断索引是否已经创建*/
 public static Boolean findIndex() {
	    	File file = new File("D:/project/environment/dataset/index/segments.gen");	    	
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
	        String dir = "D:/project/environment/dataset/WechatCBC.xls";
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
	        	
                    qres = im.search(question,0,10);
                    System.out.println("result:");
                    return qres;
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	   
	    System.out.println("down!");
		return qres;
	    }
}
