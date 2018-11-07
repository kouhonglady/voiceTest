package com.hrg.voice.util;

import java.io.FileOutputStream;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;

/**
 * 创建时间：2018年5月18日 下午16:41:11 
 * 项目名称：voiceTest
 * @author lingXue
 * @version 1.0
 * @since JDK 1.8 
 * 文件名称：SampleClient.java 
 * 类说明：语音服务功能核心类
 */

public class SampleClient {

	// 设置APPID/AK/SK
	public final String APP_ID = "11239226";
	public final String API_KEY = "mFvfdlIrS4W5syGAxRUEkh3R";
	public final String SECRET_KEY = "PGOOX3p8zezqGnmMIDmg0UPD9QDsQuGU";

	// 初始化一个FaceClient
	AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

	/**
	 * 语音识别
	 * 
	 * @param fileName
	 * @return
	 */
	public String getSynthesis(String filePathName) {

		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000); // 建立连接的超时时间（单位：毫秒）
		client.setSocketTimeoutInMillis(60000); // 通过打开的连接传输数据的超时时间（单位：毫秒）
		// 对本地语音文件进行识别G:\voice
		// String path = "F:\\testvoice\\"+fileName;
		JSONObject asrRes = client.asr(filePathName, "wav", 16000, null);
		String Rtext = asrRes.optString("result");
		System.out.println(asrRes);
		String[] ss = null;
		if (Rtext.contains("，")) {
			ss = Rtext.split("，");
		} else if (Rtext.contains("？")) {
			ss = Rtext.split("？");
		} else if (Rtext.contains("！")) {
			ss = Rtext.split("！");
		} else {
			return Rtext;
		}
		String b = "";
		for (int i = 0; i < ss.length - 1; i++) {
			String a = "";
			if (i == 0) {
				a = ss[i].substring(2);
				// System.out.println(ss[i].substring(2));
			} else {
				a = ss[i];
				// System.out.println(ss[i]);
			}
			b = b + a;
		}
		return b;
	}

	public String getVoiceBySdk(byte[] file) {

		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000); // 建立连接的超时时间（单位：毫秒）
		client.setSocketTimeoutInMillis(60000); // 通过打开的连接传输数据的超时时间（单位：毫秒）
		// 对本地语音文件进行识别G:\voice
		// String path = "F:\\testvoice\\"+fileName;
		JSONObject asrRes = client.asr(file, "wav", 16000, null);
		String Rtext = asrRes.optString("result");
		System.out.println(asrRes);
		System.out.println(Rtext);
		String[] ss = null;
		if (Rtext.contains("，")) {
			ss = Rtext.split("，");
		} else if (Rtext.contains("？")) {
			ss = Rtext.split("？");
		} else if (Rtext.contains("！")) {
			ss = Rtext.split("！");
		} else {
			return Rtext;
		}
		String b = "";
		for (int i = 0; i < ss.length - 1; i++) {
			String a = "";
			if (i == 0) {
				a = ss[i].substring(2);
				// System.out.println(ss[i].substring(2));
			} else {
				a = ss[i];
				// System.out.println(ss[i]);
			}
			b = b + a;
		}
		return b;
	}

	/**
	 * 语音合成
	 * 
	 * @param text
	 * @param path
	 */
	public String synthesis(String text, HttpServletRequest request) {
		// text ="我叫凌雪同学"; //文字 不能超过1024个字节
		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put("spd", "5");
		options.put("pit", "5");
		// 发音人选择, 0为女声，1为男声，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女
		options.put("per", "4");
		TtsResponse res = client.synthesis(text, "zh", 1, options);
		// System.out.println(res.getErrorCode());
		byte[] data = res.getData();
		System.out.println(data);
		try {
			// 项目路径
			String fPath = request.getSession().getServletContext().getRealPath("/") + "voice/";
			String fName = System.currentTimeMillis() + ".mp3";
			// 指定一个路径
			// String path = "C:/testvoice/bot.mp3";
			String file = fPath + fName;
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.close();
			return file;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("合成文件失败异常!!");
			return null;
		}
	}

}