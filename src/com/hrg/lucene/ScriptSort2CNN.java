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
 * ����ʱ�䣺����ʱ�䣺2018��11��10�� ����2:35:17
 * ��Ŀ���ƣ�voiceTest
 * @author lingxue
 * @version 1.0
 * @since JDK 1.8
 * �ļ����ƣ�ScriptSort2CNN.java
 * 
 * ��˵������������ڽ��������ͨ��cnn�����������򣬵��ǽ�����ʽΪ��Javaweb����python�ű��ķ�ʽ���С�
 * 
 * 
 * 
 */

public class ScriptSort2CNN {
	public static Iterator<IndexManager.PCpair> sort_by_cnn_script(String quesiton, Iterator<IndexManager.PCpair>  qres) {
		List<PCpair> result = new LinkedList<PCpair>();
		String qres_to_string ="";
		try {
            //�贫��Ĳ���
            if(qres != null) {
                while(qres.hasNext()) { 
                	qres_to_string +=  qres.next().toString() + "#";
                }
            }
//            System.out.println("------"+qres_to_string);
            //���������д������
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
		Iterator<IndexManager.PCpair> qres1 = IndexManagerHelper.first_search("���ÿ���������");
		Iterator<IndexManager.PCpair> result = sort_by_cnn_script("���ÿ���������", qres1);
		System.out.println("the final ans");
		if(result != null) {
            while(result.hasNext()) { 
            	System.out.println(result.next().getQuestion());
            }
        }
		
	}
}
