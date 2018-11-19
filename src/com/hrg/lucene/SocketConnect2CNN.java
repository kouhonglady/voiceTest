
package com.hrg.lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import com.hrg.lucene.IndexManager.PCpair;

/**
 * 
 * 创建时间：创建时间：2018年11月15日 上午10:51:19
 * 项目名称：voiceTest
 * @author lingxue
 * @version 1.0
 * @since JDK 1.8
 * 文件名称：SocketConnect2CNN.java
 * 
 * 类说明：这个类用于Javaweb 进程同python算法进程通讯
 * 
 * 
 */

public class SocketConnect2CNN {
	static Iterator<PCpair> sort_by_cnn_socket(String question, Iterator<IndexManager.PCpair> qres) throws Exception {
		//result 用于储存最后牌好序之后的结果
		List<PCpair> result = new LinkedList<PCpair>();
		String qres_to_string = "";
		
		//每个备选结果之间通过 ‘#’ 来分割，
		if (qres != null) {
			while (qres.hasNext()) {
				qres_to_string += qres.next().toString() + "#";
			}
		}

		// System.out.println(qres_to_string);
		//在进程通信中，检索的问题以及检索的备选n条结果，通过字符串的方式进行传播
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("question", question);
		jsonObject.put("answers", qres_to_string);
		String str = jsonObject.toString();
		// System.out.println(str);

		// 访问服务进程的套接字
		Socket socket = null;

		//得到本机的ip地址，用于和python进程进行通信
		String HOST = getLocalIp();
		//端口号是Javaweb 项目和python算法共同决定的，在通用的接口里顺便选择一个
		int PORT = 12345;
		
		System.out.println("调用算法进行排序:host=>" + HOST + ",port=>" + PORT);
		try {
			// 初始化套接字，设置访问服务的主机和进程端口号，HOST是访问python进程的主机名称，可以是IP地址或者域名，PORT是python进程绑定的端口号
			socket = new Socket(HOST, PORT);
			// 获取输出流对象
			OutputStream os = socket.getOutputStream();
			PrintStream out = new PrintStream(os);
			// 发送内容
			out.print(str);
			// 告诉服务进程，内容发送完毕，可以开始处理
			out.print("over");
			// 获取服务进程的输入流

			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String tmp = null;
			//sb 用于检测我的检索结果是否成功
			//StringBuilder sb = new StringBuilder();
			// 读取内容
			while ((tmp = br.readLine()) != null) {
				//sb.append(tmp).append('\n');
				String[] sourceStrArray = tmp.split("_");
				if (sourceStrArray.length == 2) {
					IndexManager.PCpair pcpair = new IndexManager.PCpair("0", sourceStrArray[0], sourceStrArray[1], 0);
					result.add(pcpair);
				}
			}
			// 解析结果

			//System.out.println(sb);
			return result.iterator();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
			}
			System.out.println("算法调用结束.");
		}
		return result.iterator();
	}


	// 得到本机的ip地址，因为此处我们的cnn算法程序和我们的javaweb 项目运行在同一个服务器上，所以他们通信的IP地址是一样的。
	public static final String getLocalIp() throws Exception {
		String ipString = "";
		Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
			Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = (InetAddress) addresses.nextElement();
				if (ip != null && ip instanceof Inet4Address && !ip.getHostAddress().equals("127.0.0.1")) {
					return ip.getHostAddress();
				}
			}
		}
		return ipString;
	}
	
	//用于测试
	public static void main(String[] arg) throws Exception {
		sort_by_cnn_socket("this is a test", null);
	}
}
