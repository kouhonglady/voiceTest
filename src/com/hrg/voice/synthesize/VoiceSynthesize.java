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

/**
 * ����ʱ�䣺2018��5��10�� ����11:41:12 
 * ��Ŀ���ƣ�voiceTest
 * @author lingXue
 * @version 1.0
 * @since JDK 1.8 
 * �ļ����ƣ�VoiceSynthesize.java 
 * ��˵�����������Ҫ��ͨ�� runnable �ķ�ʽʵ������ʶ�������
 */

public class VoiceSynthesize implements Runnable {

	// ����APPID/AK/SK
	public final static String APP_ID = "11239226";
	public final static String API_KEY = "mFvfdlIrS4W5syGAxRUEkh3R";
	public final static String SECRET_KEY = "PGOOX3p8zezqGnmMIDmg0UPD9QDsQuGU";
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

	public static void main(String[] args) {
		// ��ʼ��һ��AipSpeech
		AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
		// ��ѡ�������������Ӳ���
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
		// ��ѡ�����ô����������ַ, http��socket��ѡһ�����߾�������
		// client.setHttpProxy("proxy_host", proxy_port); // ����http����
		// client.setSocketProxy("proxy_host", proxy_port); // ����socket����
		// ���ýӿ�

		// ��ѡ������log4j��־�����ʽ���������ã���ʹ��Ĭ������
		// Ҳ����ֱ��ͨ��jvm�����������ô˻�������
		System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put("spd", "5");
		options.put("pit", "5");
		// ������ѡ��, 0ΪŮ����1Ϊ������3Ϊ��кϳ�-����ң��4Ϊ��кϳ�-��ѾѾ��Ĭ��Ϊ��ͨŮ
		options.put("per", "4");
		String string = "��ðٶ�";
		// ���ýӿ�
		TtsResponse res = client.synthesis(string, "zh", 1, options);

		// TtsResponse res = client.synthesis("��ðٶ�", "zh", 1, null);
		byte[] data = res.getData();
		JSONObject res1 = res.getResult();
		if (data != null) {
			try {
				Util.writeBytesToFileSystem(data, "output.mp3");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (res1 != null) {
			System.out.println(res1.toString(2));
		}
	}

}