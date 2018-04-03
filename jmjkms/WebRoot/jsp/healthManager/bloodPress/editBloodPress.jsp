<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head><meta name="viewport" content="width=device-width, initial-scale=1.0"><meta http-equiv="X-UA-Compatible" content="ie=edge">
  <meta charset="UTF-8">
  <c:choose><c:when test="${session.comHospital.isHead==0}"><title>中电科社区健康管理平台</title></c:when><c:when test="${session.communityHospitalGroup!=null}"><title>社区健康管理系统</title></c:when><c:otherwise><title>社区健康服务站</title></c:otherwise></c:choose>
  <link rel="stylesheet" href="/jmjkms/css/cssreset.css">
  <link rel="stylesheet" href="/jmjkms/css/main.css">
  <link type="text/css" href="/jmjkms/css/confirm.css">
</head>
<body>
  <!-- 顶栏 start -->
  <c:choose>
		<c:when test='${embeded }'>
		</c:when>
		<c:otherwise>
			<s:include value="/include/header.jsp" />
		</c:otherwise>
	</c:choose>
  <!-- 顶栏 end -->
  <div class="main-content clearfix">
    <!-- 主菜单 start -->
    <c:choose>
			<c:when test='${embeded }'>
			</c:when>
			<c:otherwise>
				<s:include value="/include/nav.jsp" />
			</c:otherwise>
		</c:choose>
    <!-- 主菜单 end -->
    <!-- 主容器 start -->
    <div class="container">
      <h3 class="current-title">血压档案 数据修改</h3>
      <input name="yemianname" value="02201" type="hidden"/>
      <!-- 表格容器 start -->
       <div class="search-content">
        <div class="search-item">
          <label>姓名：${localHealthFile.name}</label>
          &nbsp;&nbsp;&nbsp;&nbsp;<label>性别：<c:if test="${localHealthFile.sex==true }">男</c:if><c:if test="${localHealthFile.sex==false }">女</c:if></label>
          &nbsp;&nbsp;&nbsp;&nbsp;<label>电话号码：${localHealthFile.IPhone}</label>
        </div>
      </div>
      <!-- begin -->
       <h3 class="title-td">血压数据修改</h3>
       <div class="table-content">
       <!-- 血压表单表格 start -->
         <form id="registerform" action="HealthManagerAction!editBPCloud.action" method="post" >
           <table class="table table-bordered">
             <tbody>
               <tr>
                 <td><span style="color:red">*</span>测量条件
                          <input type="hidden"  value="${healthFileId}"  name="healthFileId" />
                          <input type="hidden"  value="${cloudId}"  name="cloudId" />
                 </td>
                 <td><select name="bloodPress.meaState"  id="" >
                      <option value="0"  <c:if test="${meaState==0 }">selected</c:if>>安静</option>
                      <option value="1" <c:if test="${meaState==1 }">selected</c:if>>饮酒后</option>
                      <option value="2" <c:if test="${meaState==2 }">selected</c:if>>运动后</option>
                   </select>
                   </td>
                 <td><span style="color:red">*</span>是否服药</td>
                 <td>
                   <select name="bloodPress.takeMed"  id="takeMedSelect">
                     <option value="0" <c:if test="${takeMed eq 0 }">selected</c:if>>未服药</option>
                     <option value="1" <c:if test="${takeMed eq 1 }">selected</c:if>>已服药</option>
                   </select>
                 </td>
                   <td><span style="color:red">*</span>测量时间</td>
                 <td> <input type="text" class="w150" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})" name="bloodPress.meaTime" value="${meaTime}"  onfocus="this.blur()">
                 </td>
                 <td>药物名称</td>
                 <td>
                 	  <input type="text" class="w150 temp" id="medName"   maxlength='20' name="bloodPress.medName" value = "${medName }"/>
                 </td>
               </tr>
                <tr>
                <td><span style="color:red">*</span>测量手臂</td>
                 <td><select name="bloodPress.arm"  id="">
                     <option value="0" <c:if test="${arm eq 0 }">selected</c:if>>左手</option>
                     <option value="1" <c:if test="${arm eq 1 }">selected</c:if>>右手</option>
                   </select></td>
                 <td><span style="color:red">*</span>脉率(次/min)</td>
                 <td><input type="text" id="ml" class="temp" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength='5' name="bloodPress.pulse" datatype="*" nullmsg="脉率不可为空"  value="${pulse }"></td>
                 <td><span style="color:red">*</span>收缩压(mmHg)</td>
                <td><input type="text" id="ssy" class="temp" onkeyup="Num(this)" maxlength='5' name="bloodPress.sysPre"  datatype="*" nullmsg="收缩压不可为空"  value="${sysPre}"></td>
                 <td><span style="color:red">*</span>舒张压(mmHg)</td>
                 <td> <input type="text" class="temp" id="szy" onkeyup="Num(this)" maxlength='5' name="bloodPress.diaPre"  datatype="*" nullmsg="舒张压不可为空" value="${diaPre }"></td>
               </tr>
               <tr>                 
           </tr>
             </tbody>
           </table>
           <div class="btn-content">
              <input type='hidden' id='embeded' name='embeded' value='${embeded}'/>
              <input type="submit" class="btn" value="保存">
              <c:choose>
						<c:when test='${embeded }'>
							<a class="btn btn-xianshi" onclick='editReturn();'>返回</a>
						</c:when>
						<c:otherwise>
							<button class="btn" type="button" onclick="history.go(-1);">返回</button>
						</c:otherwise>
					</c:choose>
              
            </div>
         </form>
         <!-- 血压表单表格 end -->
       </div>
       <!--end -->
    </div>
    <!-- 主容器 end -->
    <!-- 底栏 Start-->
   <c:choose>
			<c:when test='${embeded }'>
			</c:when>
			<c:otherwise>
				<s:include value="/include/footer.jsp" />
			</c:otherwise>
		</c:choose>
    <!-- 底栏 Start-->
 </div>
<script src="/jmjkms/js/jquery-2.1.1.min.js"></script>
<script src="/jmjkms/js/laydate/laydate.js"></script>
  <script src="js/sweetalert.min.js"></script> 
<script src="/jmjkms/js/base.js"></script>
<script src="/jmjkms/js/healthManager.js"></script>
<script type="text/javascript" src="/jmjkms/js/confirm/Validform_v5.3.2_min.js"></script>
<script src='/jmjkms/js/dkdhcommon.js'>
<script type="text/javascript">
$(function(){
      $("#registerform").Validform({
        showAllError:false,
        tiptype:function(msg){          
          alert(msg);
        }
      });
  });
</script>
<script>
	function Num(obj){
	obj.value = obj.value.replace(/[^\d.]/g,"");
	obj.value = obj.value.replace(/^\./g,""); 
	obj.value = obj.value.replace(/\.{2,}/g,"."); 
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
        }
        
    	$("form").submit(
			function() {
				if ($("#ml").val() > 999 || $("#szy").val() > 999
						|| $("ssy").val() > 999) {
					alert("数据值过大！")
					return false;
				}
			})    
</script>
</body>
</html>