package com.hrg.servlet;

import java.util.*;
import java.util.Date;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.Thread;

import com.hrg.lucene.*;
import com.hrg.voice.synthesize.MyThread;
import com.hrg.voice.synthesize.VoiceSynthesize;
import com.hrg.voice.util.SampleClient;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

//用户登录处理Servlet 系统登录主页处理表单
public class HomePageAction extends HttpServlet {
	
	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SampleClient sc = new SampleClient(); //语音识别核心类
	//初始化方法，取得数据库连接对象
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		System.out.println("++++++++++++++++++");
		try {
			if(!index_test.findIndex())
			index_test.build_index_test();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//处理GET请求方法
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		
		System.out.println("++++++++this is doGet++++++++++");

		//查询数据库和跳转到登录主界面
		try {
			//查询数据库操作
			//跳转到主界面

	        String input = request.getParameter("inputMessage");
	        String input_ques = new String(input.getBytes("ISO-8859-1"),"utf-8");
	        if(input_ques==null||input_ques.trim().length()==0) {  
	        	response.sendRedirect("index.jsp"); 
	        	
	        }else{
	        	Date begintime = new Date();
	        	System.out.println("start to fetch!");
	        	Iterator<IndexManager.PCpair> qres = index_test.first_search(input_ques);
	        	 int i=0 ;
	        	
	        	if(qres != null) {
	        		request.setAttribute("newsNum",""+10);
	                while(qres.hasNext()){
	                IndexManager.PCpair pcpair = qres.next();
	                System.out.println(pcpair.getId()+"===="+pcpair.getQuestion());
	                request.setAttribute("newsTitle"+i, pcpair.getQuestion()+"?");
				    //request.setAttribute("newsUrl"+i, pcpair.getQuestion());
				    request.setAttribute("newsBody"+i, pcpair.getAnswer());
				    request.setAttribute("newsDate"+i,new Date());
				    if(i == 0) {
				    	String name = sc.synthesis( pcpair.getAnswer(),request);
				    	System.out.println("the path is :"+name);
				    	MyThread myThread = new MyThread();
				    	myThread.setFilename(name);
				    	myThread.start();
				    }
				     i++;
	                }
					Date endtime = new Date();
					long timeOfsearch = endtime.getTime()-begintime.getTime();
					request.setAttribute("searchTime",""+timeOfsearch);
					
					request.setAttribute("question_asked", input_ques);
				        
			        	        	        	   
			        RequestDispatcher dispatcher = request.getRequestDispatcher("result.jsp");
			        dispatcher.forward(request, response);	                
	        	}else {
	        		System.out.println("no result");
					request.setAttribute("noResult","没有找到相关内容");
					RequestDispatcher dispatcher = request.getRequestDispatcher("noresult.jsp");
			        dispatcher.forward(request, response);
	        	}
	        }
		} catch(Exception e) {
			System.out.println("错误："+e.getMessage());
			e.printStackTrace();
			response.sendRedirect("index.jsp");
		}
	}
	
	//处理POST请求方法
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
		doGet(request,response);
	}
	//销毁方法
	public void destroy() {
		super.destroy();
		try {
			//cn.close();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}		
}