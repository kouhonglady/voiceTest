<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">	
<title>智能语音交互</title>
 <script src="${pageContext.request.contextPath}/js/jquery-1.8.3.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${pageContext.request.contextPath}/js/HZRecorder.js"></script>
<script type="text/javascript">
		/* 音频 */
		var recorder;
		var audio = document.querySelector('audio');
		//开始录音
		function startRecording() {
			HZRecorder.get(function(rec) {
				recorder = rec;
				recorder.start();
			});
		}
			
		//保存录音
		function uploadRecord(){
			//上传录音地址
			recorder.upload("${pageContext.request.contextPath}/voiceSpeech/save.do", function (state, e) {
				switch (state) {
	                    case 'uploading':
	                        break;
	                    case 'ok':       	
	                    	var map = JSON.parse(e.target.response);
	                        if (map["success"]) {
 	            	   			alert("上传成功");
	            	   		} else {
	            	   			alert("保存失败");
	            	   		} 
	                        break;
	                    case 'error':
	                        alert("上传失败");
	                        break;
	                    case 'cancel':
	                        alert("上传被取消");
	                        break;
	                }
	         });
		}	
	</script>
</head>
<body>
<div>
<h1>H5语音识别测试</h1>
<input type="button" value="开始录音" onclick="startRecording()" />
<input type="button" value="保存录音" onclick="uploadRecord()" />
<br><br>
 </div>
</body>
</html>