package com.hrg.voice.synthesize;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.baidu.aip.speech.AipSpeech;


/**
 * ����ʱ�䣺2018��5��10�� ����11:28:31 
 * ��Ŀ���ƣ�voiceTest
 * @author lingXue
 * @version 1.0
 * @since JDK 1.8 
 * �ļ����ƣ�Test.java 
 * ��˵�����������Ҫ����������ʶ����̲�����
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
		 com.baidu.aip.speech.TtsResponse ttsResponse = aipSpeech.synthesis("你好小帅", "zh", 1, options);
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
				if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
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
