package com.hrg.voice.util;

import java.io.FileOutputStream;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;

/**
 * ����ʱ�䣺2018��5��18�� ����16:41:11 
 * ��Ŀ���ƣ�voiceTest
 * @author lingXue
 * @version 1.0
 * @since JDK 1.8 
 * �ļ����ƣ�SampleClient.java 
 * ��˵�������������ܺ�����
 */

public class SampleClient {

	// ����APPID/AK/SK
	public final String APP_ID = "11239226";
	public final String API_KEY = "mFvfdlIrS4W5syGAxRUEkh3R";
	public final String SECRET_KEY = "PGOOX3p8zezqGnmMIDmg0UPD9QDsQuGU";

	// ��ʼ��һ��FaceClient
	AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

	/**
	 * ����ʶ��
	 * 
	 * @param fileName
	 * @return
	 */
	public String getSynthesis(String filePathName) {

		// ��ѡ�������������Ӳ���
		client.setConnectionTimeoutInMillis(2000); // �������ӵĳ�ʱʱ�䣨��λ�����룩
		client.setSocketTimeoutInMillis(60000); // ͨ���򿪵����Ӵ������ݵĳ�ʱʱ�䣨��λ�����룩
		// �Ա��������ļ�����ʶ��G:\voice
		// String path = "F:\\testvoice\\"+fileName;
		JSONObject asrRes = client.asr(filePathName, "wav", 16000, null);
		String Rtext = asrRes.optString("result");
		System.out.println(asrRes);
		String[] ss = null;
		if (Rtext.contains("��")) {
			ss = Rtext.split("��");
		} else if (Rtext.contains("��")) {
			ss = Rtext.split("��");
		} else if (Rtext.contains("��")) {
			ss = Rtext.split("��");
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

		// ��ѡ�������������Ӳ���
		client.setConnectionTimeoutInMillis(2000); // �������ӵĳ�ʱʱ�䣨��λ�����룩
		client.setSocketTimeoutInMillis(60000); // ͨ���򿪵����Ӵ������ݵĳ�ʱʱ�䣨��λ�����룩
		// �Ա��������ļ�����ʶ��G:\voice
		// String path = "F:\\testvoice\\"+fileName;
		JSONObject asrRes = client.asr(file, "wav", 16000, null);
		String Rtext = asrRes.optString("result");
		System.out.println(asrRes);
		System.out.println(Rtext);
		String[] ss = null;
		if (Rtext.contains("��")) {
			ss = Rtext.split("��");
		} else if (Rtext.contains("��")) {
			ss = Rtext.split("��");
		} else if (Rtext.contains("��")) {
			ss = Rtext.split("��");
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
	 * �����ϳ�
	 * 
	 * @param text
	 * @param path
	 */
	public String synthesis(String text, HttpServletRequest request) {
		// text ="�ҽ���ѩͬѧ"; //���� ���ܳ���1024���ֽ�
		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put("spd", "5");
		options.put("pit", "5");
		// ������ѡ��, 0ΪŮ����1Ϊ������3Ϊ��кϳ�-����ң��4Ϊ��кϳ�-��ѾѾ��Ĭ��Ϊ��ͨŮ
		options.put("per", "4");
		TtsResponse res = client.synthesis(text, "zh", 1, options);
		// System.out.println(res.getErrorCode());
		byte[] data = res.getData();
		System.out.println(data);
		try {
			// ��Ŀ·��
			String fPath = request.getSession().getServletContext().getRealPath("/") + "voice/";
			String fName = System.currentTimeMillis() + ".mp3";
			// ָ��һ��·��
			// String path = "C:/testvoice/bot.mp3";
			String file = fPath + fName;
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.close();
			return file;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("�ϳ��ļ�ʧ���쳣!!");
			return null;
		}
	}

}