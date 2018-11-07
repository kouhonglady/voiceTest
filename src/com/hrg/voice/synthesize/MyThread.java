package com.hrg.voice.synthesize;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.filechooser.FileNameExtensionFilter;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
* 创建时间：创建时间：2018年5月26日 下午2:50:49
* 项目名称：voiceTest
* @author lingXue
* @version 1.0
* @since JDK 1.8
* 文件名称：MyThread.java
* 类说明：检索结果进行语音播报的时候开启一个新的线程，这个为开始播报的线程类
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
