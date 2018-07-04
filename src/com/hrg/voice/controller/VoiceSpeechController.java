package com.hrg.voice.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hrg.voice.util.SampleClient;
/**
 * H5 录音实现
 * 
 *
 */
@Controller
@RequestMapping(value = "/voiceSpeech")
public class VoiceSpeechController {

	SampleClient sc = new SampleClient(); //语音识别核心类

	/**
	 * 访问录音页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/voice.do")
	public ModelAndView queryVoice() throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/voice/voiceIndex");
		return modelAndView;
	}
	
	/**
	 * 保存录音
	 * @date 2018年5月18日 
	 * @param audioData
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save.do")
	@ResponseBody
	public Map<String, Object> save(MultipartFile audioData,HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		try {
//			//保存录音
//			String filePathName =saveAudio(audioData);//返回音频路径名称
//			//识别录音
//			String Rtext = "未识别清楚";
//			if ((filePathName)!="") {
//				System.out.println(filePathName);
//				Rtext = sc.getSynthesis(filePathName);
//				modelMap.put("success", true);
//			}
//			//根据语音识别内容 合成回复语音
////			String res = "";
////			if("你好".equals(Rtext)){
////	    		 res = "我知道你也很好呀";
////	    	}else{
////	    		 res = "对不起，我不太明白";
////	    	}
//			String name = sc.synthesis("我叫凌雪同学",request);//合成需要播放的录音
//			System.out.println(name);
//			System.out.println("语音识别的内容：" + Rtext);
//			modelMap.put("Rtext", Rtext);
////			modelMap.put("res", res);
////			modelMap.put("name", name);
			
			//音频文件 转化为 byte[]
			InputStream content = audioData.getInputStream();
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] buff = new byte[100];
			int rc = 0;
			while ((rc = content.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			// 获得二进制数组
			byte[] byte1 = swapStream.toByteArray();
			String Rtext = sc.getVoiceBySdk(byte1);
			System.out.println("语音识别内容:"+Rtext);
			modelMap.put("success", true);
			modelMap.put("Rtext", Rtext);
			
        } catch (Exception e) {
        	System.out.println("VoiceSpeechController-catche");
            e.printStackTrace();
            modelMap.put("success", false);
			modelMap.put("data", e.getMessage());
        }
		return modelMap;
	}
	
	/**
	 * 保存音频文件
	 * @param audioData
	 * @return
	 */
	private String saveAudio(MultipartFile audioData){
		try {
			String filePathName = null;
			String fName = System.currentTimeMillis() + ".wav";
	        String fPath = "C:/testvoice";
	        //将录音的文件存放到F盘下语音文件夹下  
	        File filePath = new File(fPath);  
	        if(!filePath.exists())  
	        {//如果文件不存在，则创建该目录  
	            filePath.mkdir();  
	        }  
	        filePathName = fPath+"/"+fName;
			FileOutputStream os = new FileOutputStream(filePathName); 
			System.out.println("路径："+filePathName);
	        InputStream in = audioData.getInputStream();  
	        int b = 0;  
	        while((b=in.read())!=-1){ //读取文件   
	            os.write(b);  
	        }  
	        os.flush(); //关闭流   
	        in.close();  
	        os.close();  
	        return filePathName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
