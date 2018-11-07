package com.hrg.voice.synthesize;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.baidu.aip.speech.AipSpeech;


/**
 * 创建时间：2018年5月10日 上午11:28:31 
 * 项目名称：voiceTest
 * @author lingXue
 * @version 1.0
 * @since JDK 1.8 
 * 文件名称：Test.java 
 * 类说明：这个类主要是用于语音识别过程测试类
 */
public class Test{
	
	 public  final static String APP_ID = "11239226";
	    public  final static String API_KEY = "mFvfdlIrS4W5syGAxRUEkh3R";
	    public  final static String SECRET_KEY = "PGOOX3p8zezqGnmMIDmg0UPD9QDsQuGU";
	    
	   
	 public static void main(String[] args) throws IOException {
		 AipSpeech aipSpeech = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
		 HashMap<String, Object> options = new HashMap<String, Object>();
		    options.put("spd", "5");
		    options.put("pit", "5");
		    options.put("per", "4");
		 com.baidu.aip.speech.TtsResponse ttsResponse = aipSpeech.synthesis("浣犲ソ灏忓竻", "zh", 1, options);
		 byte[] aa = ttsResponse.getData();
		 getFile(aa, "D:/", "2.mp3");
		 //System.out.println(JSON.toJSONString(ttsResponse));
	 }
	 public static void getFile(byte[] bfile, String filePath, String fileName) {
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;
			File file = null;
			try {
				File dir = new File(filePath);
				if (!dir.exists() && dir.isDirectory()) {// 鍒ゆ柇鏂囦欢鐩綍鏄惁瀛樺湪
					dir.mkdirs();
				}
				file = new File(filePath + "\\" + fileName);
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos);
				bos.write(bfile);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bos != null) {
					try {
						bos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
}
