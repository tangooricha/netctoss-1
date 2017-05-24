<%@page pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>达内－NetCTOSS</title>
        <link type="text/css" rel="stylesheet" media="all" href="styles/global.css" />
        <link type="text/css" rel="stylesheet" media="all" href="styles/global_color.css" />
        <script src="js/jquery-1.11.1.js"></script>
        <script>
        	$(function(){
        		$(":password").blur(function(e){
            		var pwd = e.target;
            		var val = pwd.value;
            		var reg = /^[\w\d_]{1,30}$/;
            		if(!reg.test(val)){
            			$(pwd).siblings().filter("div").eq(0).css("color","red");
            		}else{
            			$(pwd).siblings().filter("div").eq(0).css("color","black");
            		}
            	});
        	});
        	
        	function showResult(){
        		var pwds = $("#main :password");
        		var oldPwd = pwds[0].value;
        		var newPwd = pwds[1].value;
        		var repeatPwd = pwds[2].value;
        		$.ajax({
        			url:'modifyPwd.do',
        			type:'POST',
        			data:{'oldPwd':oldPwd,'newPwd':newPwd,'repeatPwd':repeatPwd},
        			dataType:'text',
        			success:function(text){
        				var result = text;
        				if (result==1){
        					console.log(1);
        					setTimer('保存成功！');
        				} else if(result==-1){
        					setTimer('未找到账号！');
        				} else if(result==-2){
        					setTimer('旧密码错误！');
        				} else if(result==-3){
        					setTimer('两次输入密码不一致！');
        				} 
        			},
        			error:function(){
        				alert('Ajax请求失败');
        			}
        		});
        	}
        	
        	function setTimer(msg){
        		var show = $("#save_result_info");
        		show.html(msg);
        		show.css('display','block');
        		setTimeout(function(){
        			show.css('display','none');
        		}, 3000);
        	}
        </script>        
    </head>
    <body>
        <!--Logo区域开始-->
        <div id="header">
            <%@include file="../logo.jsp" %>         
        </div>
        <!--Logo区域结束-->
        <!--导航区域开始-->
        <div id="navi">
           <%@include file="../menu.jsp" %>
        </div>
        <!--导航区域结束-->
        <div id="main">      
            <!--保存操作后的提示信息：成功或者失败-->      
            <div id="save_result_info" class="save_success">保存成功！</div><!--保存失败，旧密码错误！-->
            <form action="" method="post" class="main_form">
                <div class="text_info clearfix"><span>旧密码：</span></div>
                <div class="input_info">
                    <input type="password" name="oldPwd" class="width200"  /><span class="required">*</span>
                    <div class="validate_msg_medium">30长度以内的字母、数字和下划线的组合</div>
                </div>
                <div class="text_info clearfix"><span>新密码：</span></div>
                <div class="input_info">
                    <input type="password" name="newPwd"  class="width200" /><span class="required">*</span>
                    <div class="validate_msg_medium">30长度以内的字母、数字和下划线的组合</div>
                </div>
                <div class="text_info clearfix"><span>重复新密码：</span></div>
                <div class="input_info">
                    <input type="password" name="repeatPwd" class="width200"  /><span class="required">*</span>
                    <div class="validate_msg_medium">两次新密码必须相同</div>
                </div>
                <div class="button_info clearfix">
                    <input type="button" value="保存" class="btn_save" onclick="showResult();" />
                    <input type="button" value="取消" class="btn_save" onclick="location.href='toIndex.do';"/>
                </div>
            </form>  
        </div>
        <!--主要区域结束-->
        <div id="footer">
            <p>[源自北美的技术，最优秀的师资，最真实的企业环境，最适用的实战项目]</p>
            <p>版权所有(C)加拿大达内IT培训集团公司 </p>
        </div>
    </body>
</html>
