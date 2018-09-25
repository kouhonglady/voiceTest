package com.hrg.voice.synthesize;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class VoiceSynthesize implements  Runnable
{
	
	   //设置APPID/AK/SK 
		 public  final static String APP_ID = "11239226";
	    public  final static String API_KEY = "mFvfdlIrS4W5syGAxRUEkh3R";
	    public  final static String SECRET_KEY = "PGOOX3p8zezqGnmMIDmg0UPD9QDsQuGU";
	    String filename = "";
        public void setFilename(String filename) {
			this.filename = filename;
		}
    	@Override
    	public void run() {
    		try {
				new Player(new BufferedInputStream(new FileInputStream(new File(filename)))).play();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JavaLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     }
//	        public static void main(String[] args) {
//	            // 初始化一个AipSpeech
//	            AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
//	            // 可选：设置网络连接参数
//	            client.setConnectionTimeoutInMillis(2000);
//	            client.setSocketTimeoutInMillis(60000);
//	            // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//	            //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//	            //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
//	            // 调用接口
//	            
//	         // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
//	            // 也可以直接通过jvm启动参数设置此环境变量
//	            System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
//	            
//	            HashMap<String, Object> options = new HashMap<String, Object>();
//	            options.put("spd", "5");
//	            options.put("pit", "5");
//	            //发音人选择, 0为女声，1为男声，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女
//	            options.put("per", "4");
//	            String string = "你好百度";
//	            // 调用接口
//	            TtsResponse res = client.synthesis(string, "zh", 1, options);
//	            
//	            //TtsResponse res = client.synthesis("你好百度", "zh", 1, null);
//	            byte[] data = res.getData();
//	            JSONObject res1 = res.getResult();
//	            if (data != null) {
//	                try {
//	                    Util.writeBytesToFileSystem(data, "output.mp3");
//	                } catch (IOException e) {
//	                    e.printStackTrace();
//	                }
//	            }
//	            if (res1 != null) {
//	               System.out.println(res1.toString(2));
//	            }
//	        }

}