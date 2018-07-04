<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=emulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Searcher</title>
	<link href="css/style.css" rel="stylesheet" type="text/css" />
	<link href="css/index.css" rel="stylesheet" type="text/css" />
	<title>这是提示框</title>
    <style>
    /*这里修改提示框的字体样式*/
    .bdSug_wpr{color: black;}
    .bdsug_copy{display: none;}
    </style>
     <style type='text/css'>
    ul { list-style: none; }
    #recordingslist audio { display: block; margin-bottom: 10px; }
  </style>
</head>
<body>
	<div id="container">
		<div id="bd">
	        <div id="main">
	        	<h1 class="title">
	            	<div class="logo large"></div>
	            </h1>
	            <form id = "formid" action = "loginAction.done">
		            <div class="inputArea">
		                <input id="record_button" type = "button"   class="soutu-btn" value=""  onclick="startRecording()" ></input>
		            	<input  id ="upload_button" type = "button"   class="soutu-btn" value="" style="display:none" onclick="uploadRecord()" ></input>
		            	<input id="inputMe"  baiduSug=1 type="text" class="searchInput"  name="inputMessage"/>
		                <input id = "search_sub"  type="submit" class="searchButton" value=""/>	            
		            </div>
	            </form>
	            
	        </div><!-- End of main -->
	    </div><!--End of bd-->
	    <!DOCTYPE html>
	    

	    	    
	    <div class="foot">
	    	<div class="wrap">
	            <div class="copyright">Copyright 2018哈工大机器人义乌研究院&copy;</div>
	        </div>
	    </div>
	</div>
</body>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script charset="gbk" src="http://www.baidu.com/js/opensug.js"></script>
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
			 var record_button = document.getElementById('record_button');
			 var upload_button = document.getElementById('upload_button');
			 record_button.style.display = "none";
			 upload_button.style.display = "block";
			 
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
	                        	document.getElementById('inputMe').value=map["Rtext"];
 	            	   			//alert("上传成功");
	                        	document.getElementById("formid").submit();
	            	   		} else {
	            	   			//alert("保存失败");
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
			
			 var record_button = document.getElementById('record_button');
			 var upload_button = document.getElementById('upload_button');
			 record_button.style.display = "block";
			 upload_button.style.display = "none";
			 
			 
		}
	</script>
</html>