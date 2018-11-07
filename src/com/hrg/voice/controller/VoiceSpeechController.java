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
* ����ʱ�䣺2018��5��23�� ����3:30:27
* ��Ŀ���ƣ�voiceTest
* @author lingxue
* @version 1.0
* @since JDK 1.8
* �ļ����ƣ�VoiceSpeechController.java
* ��˵����H5 ¼��ʵ��
*/


@Controller
@RequestMapping(value = "/voiceSpeech")
public class VoiceSpeechController {

	SampleClient sc = new SampleClient(); //����ʶ�������

	/**
	 * ����¼��ҳ��
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
	 * ����¼��
	 * @date 2018��5��18�� 
	 * @param audioData
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save.do")
	@ResponseBody
	public Map<String, Object> save(MultipartFile audioData,HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		try {
//			//����¼��
//			String filePathName =saveAudio(audioData);//������Ƶ·������
//			//ʶ��¼��
//			String Rtext = "δʶ�����";
//			if ((filePathName)!="") {
//				System.out.println(filePathName);
//				Rtext = sc.getSynthesis(filePathName);
//				modelMap.put("success", true);
//			}
//			//��������ʶ������ �ϳɻظ�����
////			String res = "";
////			if("���".equals(Rtext)){
////	    		 res = "��֪����Ҳ�ܺ�ѽ";
////	    	}else{
////	    		 res = "�Բ����Ҳ�̫����";
////	    	}
//			String name = sc.synthesis("�ҽ���ѩͬѧ",request);//�ϳ���Ҫ���ŵ�¼��
//			System.out.println(name);
//			System.out.println("����ʶ������ݣ�" + Rtext);
//			modelMap.put("Rtext", Rtext);
////			modelMap.put("res", res);
////			modelMap.put("name", name);
			
			//��Ƶ�ļ� ת��Ϊ byte[]
			InputStream content = audioData.getInputStream();
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] buff = new byte[100];
			int rc = 0;
			while ((rc = content.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			// ��ö���������
			byte[] byte1 = swapStream.toByteArray();
			String Rtext = sc.getVoiceBySdk(byte1);
			System.out.println("����ʶ������:"+Rtext);
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
	 * ������Ƶ�ļ�
	 * @param audioData
	 * @return
	 */
	private String saveAudio(MultipartFile audioData){
		try {
			String filePathName = null;
			String fName = System.currentTimeMillis() + ".wav";
	        String fPath = "C:/testvoice";
	        //��¼�����ļ���ŵ�F���������ļ�����  
	        File filePath = new File(fPath);  
	        if(!filePath.exists())  
	        {//����ļ������ڣ��򴴽���Ŀ¼  
	            filePath.mkdir();  
	        }  
	        filePathName = fPath+"/"+fName;
			FileOutputStream os = new FileOutputStream(filePathName); 
			System.out.println("·����"+filePathName);
	        InputStream in = audioData.getInputStream();  
	        int b = 0;  
	        while((b=in.read())!=-1){ //��ȡ�ļ�   
	            os.write(b);  
	        }  
	        os.flush(); //�ر���   
	        in.close();  
	        os.close();  
	        return filePathName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}