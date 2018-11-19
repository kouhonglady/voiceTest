package com.hrg.lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONException;
import com.hrg.lucene.IndexManager.PCpair;


/**
 * 
 * 创建时间：创建时间：2018年11月10日 下午2:35:17
 * 项目名称：voiceTest
 * @author lingxue
 * @version 1.0
 * @since JDK 1.8
 * 文件名称：ScriptSort2CNN.java
 * 
 * 类说明：这个类用于将检索结果通过cnn进行重新排序，但是交互方式为，Javaweb调用python脚本的方式进行。
 * 
 * 
 * 
 */

public class ScriptSort2CNN {
	public static Iterator<IndexManager.PCpair> sort_by_cnn_script(String quesiton, Iterator<IndexManager.PCpair>  qres) {
		List<PCpair> result = new LinkedList<PCpair>();
		String qres_to_string ="";
		try {
            //需传入的参数
            if(qres != null) {
                while(qres.hasNext()) { 
                	qres_to_string +=  qres.next().toString() + "#";
                }
            }
//            System.out.println("------"+qres_to_string);
            //设置命令行传入参数
            String[] args = new String[] { "python", "E:\\study\\hrg_project\\bigDataCompetition\\bank\\sentence-similarity\\cnn-sentence-similarity\\temp.py",quesiton,qres_to_string};
            Process pr = Runtime.getRuntime().exec(args);

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
            	String[] sourceStrArray = line.split("_");
            	if(sourceStrArray.length == 2) {
            		IndexManager.PCpair pcpair = new IndexManager.PCpair("0",sourceStrArray[0],sourceStrArray[1],0);
                	result.add(pcpair);
//                    System.out.println(line);
            	}
            }
            in.close();
            pr.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return result.iterator();
	}
	
	public static void main(String[] args) throws JSONException, IOException {
		Iterator<IndexManager.PCpair> qres1 = IndexManagerHelper.first_search("信用卡密码忘了");
		Iterator<IndexManager.PCpair> result = sort_by_cnn_script("信用卡密码忘了", qres1);
		System.out.println("the final ans");
		if(result != null) {
            while(result.hasNext()) { 
            	System.out.println(result.next().getQuestion());
            }
        }
		
	}
}
