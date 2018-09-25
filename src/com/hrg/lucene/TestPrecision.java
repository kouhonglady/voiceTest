package com.hrg.lucene;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
