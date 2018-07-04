package com.hrg.voice.synthesize;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.filechooser.FileNameExtensionFilter;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

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
