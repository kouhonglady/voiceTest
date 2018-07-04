<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=emulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Searcher</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/result.css" rel="stylesheet" type="text/css" />
<style>
/*这里修改提示框的字体样式*/
.bdSug_wpr{color: black;}
.bdsug_copy{display: none;}
</style>
</head>
<body>
<div id="container">
	<div id="hd" class="ue-clear">
    	<div class="logo"></div>
    	<form id = "formid" action = "loginAction.done">
        <div class="inputArea"> 
            <input id="record_button" type = "button"   class="soutu-btn" value=""  onclick="startRecording()" ></input>
		    <input  id ="upload_button" type = "button"   class="soutu-btn" value="" style="display:none" onclick="uploadRecord()" ></input>   
        	<input  id="inputMe"  baiduSug=1 value= <%=request.getAttribute("question_asked") %> type="text"   class="searchInput" name="inputMessage" autocomplete="off"/>
            <input type="submit" class="searchButton" value=""/>
        </div>
        </form>
    </div>

	<div id="bd" class="ue-clear">
        <div id="main">
            <div class="resultArea">
            	<p class="resultTotal">
                	<span class="info">找到约&nbsp;<span class="totalResult"><%=request.getAttribute("newsNum") %></span>&nbsp;条结果(用时<span class="time"><%=request.getAttribute("searchTime") %></span>毫秒)</span>
                </p>
				
				
				
                <div class="resultList">
                    
                    <%
                    for(int i = 0;i<Integer.parseInt(request.getAttribute("newsNum").toString());i++){
                    	
                    	out.println("<div class=\"resultItem\">");
                    	
                    	out.println("<div class=\"itemHead\">");
                    	out.println("<a href=\""+request.getAttribute("newsUrl"+i)+"\""+"  target=\"_blank\" class=\"title\">");
                    	//out.println("<span class=\"keyWord\">");
                    	out.println(request.getAttribute("newsTitle"+i));
                    	out.println("<br>");
                    	//out.println("</span>");
                    	out.println("</a>");
                    	out.println("</div>");
                    	
                    	out.println("<div class=\"itemBody\">");
                    	String newsbody = request.getAttribute("newsBody"+i).toString();
                    	if(newsbody.length()>256)
                    		newsbody = newsbody.substring(0, 256);
                    	out.println(newsbody);
                    	out.println("<br>");
                    	out.println("</div>");
                    	
                    	out.println("<div class=\"itemFoot\">");
                    	//out.println("<span class=\"info\">");
                    	out.println("<label>发布时间：</label>");
                    	//out.println("<span class=\"value\">");
                    	out.println(request.getAttribute("newsDate"+i));
                    	//out.println("<br>");
                    	//out.println("<br>");
                    	//out.println("</span>");
                    	//out.println("</span>");
                    	out.println("</div>");
                    	
                    	out.println(" </div>");
                    }
                    %>
                    	
                   
                </div>
 					
            </div>
        </div><!-- End of main -->
    </div><!--End of bd-->
</div>

<div id="foot">Copyright 2018哈工大机器人义乌研究院 &copy;</div>
</body>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript" src="js/pagination.js"></script>
<script charset="gbk" src="http://www.baidu.com/js/opensug.js"></script>
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