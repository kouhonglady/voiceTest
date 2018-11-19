package com.hrg.lucene;
//
//public class CNN {
//	public static void main(String[] args) {
//	}
//}


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import com.hrg.lucene.IndexManager.PCpair;

import jxl.write.Label;

public class CNN {
	public static Iterator<IndexManager.PCpair> sort_by_cnn(String quesiton, Iterator<IndexManager.PCpair>  qres) {
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
		Iterator<IndexManager.PCpair> result = sort_by_cnn("信用卡密码忘了", qres1);
		System.out.println("the final ans");
		if(result != null) {
            while(result.hasNext()) { 
            	System.out.println(result.next().getQuestion());
            }
        }
		
	}
}
