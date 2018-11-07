package com.hrg.lucene;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**

* 创建时间：创建时间：2018年4月27日 上午10:47:22

* 项目名称：voiceTest

* @author lingxue

* @version 1.0

* @since JDK 1.8

* 文件名称：Test3.java

* 类说明：这个类是用于Lucene检索的帮助类

*/

public class TestPrecision {
	 public static void main(String[] arg) {
		 try {
	           //String a=getPara("car").substring(1),b="D34567",c="LJeff34",d="iqngfao";
	           
			 //String[] args1=new String[]{ "python", "D:\\pyworkpeace\\9_30_1.py", a, b, c, d };
	           //Process pr=Runtime.getRuntime().exec(args1);
	           String url="http://blog.csdn.net/thorny_v/article/details/61417386";
	           System.out.println("start;"+url);
	           String[] args1 = new String[] { "python", "E:\\study\\hrg_project\\bigDataCompetition\\bank\\demo\\pattern_demo\\data\\sorted_by_pattern.py", url}; 
	           Process pr=Runtime.getRuntime().exec(args1);
	           BufferedReader in = new BufferedReader(new InputStreamReader(
	             pr.getInputStream()));
	           String line;
	           while ((line = in.readLine()) != null) {
	            System.out.println(line);
	           }
	           in.close();
	           pr.waitFor();
	           System.out.println("end");
	          } 
	        catch (Exception e) {
	           e.printStackTrace();
	          }
	 }
}
