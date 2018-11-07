package com.hrg.voice.synthesize;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.filechooser.FileNameExtensionFilter;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
* ����ʱ�䣺����ʱ�䣺2018��5��26�� ����2:50:49
* ��Ŀ���ƣ�voiceTest
* @author lingXue
* @version 1.0
* @since JDK 1.8
* �ļ����ƣ�MyThread.java
* ��˵�������������������������ʱ����һ���µ��̣߳����Ϊ��ʼ�������߳���
*/


public class MyThread extends Thread{
	String filename;
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
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

}
