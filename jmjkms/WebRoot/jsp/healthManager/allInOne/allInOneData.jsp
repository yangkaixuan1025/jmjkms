<%@page import="java.text.SimpleDateFormat"%>
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
      <h3 class="current-title">健康日常监测-健康一体机档案</h3>
	<input name="yemianname" value="02201" type="hidden"/>
<!-- 表单表格 start -->
   <h3 class="current-title">健康一体机档案</h3>
   <div class="table-content">
       <table class="table table-bordered">
         <tbody>
           <tr>
             <td>姓名</td>
             <td>监测日期</td>
             <td>体质指数</td>
             <td>健康报表</td>
             <td>健康指标分析</td>
           </tr>
             <s:iterator value="aios" var="ao"><tr>
             <td>${localHealthFile.name}</td>
             <td>${ao.meaTime}</td>
             <td>${ao.bmi}</td>
             <td> <a href="HealthManagerAction!getPhysicalExamByHealthFileId.action?healthFileId=${localHealthFile.healthFileId}&allInOne.appID=${ao.appID}&allInOne.id=${ao.id}&allInOne.appUserID=${ao.appUserID}&allInOne.meaTime=${ao.meaTime}&allInOne.temperature=${ao.temperature}&allInOne.fbs=${ao.fbs}&allInOne.pbs=${ao.pbs}&allInOne.rbs=${ao.rbs}&allInOne.sysPre=${ao.sysPre}&allInOne.diaPre=${ao.diaPre}&allInOne.height=${ao.height}&allInOne.weight=${ao.weight}&allInOne.oxygen=${ao.oxygen}&allInOne.bmi=${ao.bmi}&allInOne.pulse=${ao.pulse}&allInOne.fat=${ao.fat}&allInOne.muscle=${ao.muscle}&allInOne.jkzd=${ao.jkzd}&allInOne.jkpj=${ao.jkpj}&embeded=${embeded}">查看</a></td>
             <td> <a href="HealthManagerAction!getAllInOneFromCloud.action?healthFileId=${localHealthFile.healthFileId}&embeded=${embeded}">查看</a></td>
           </tr>
          </s:iterator>
         </tbody>
       </table>
        <div class="btn-content">
        <c:choose>
						<c:when test='${embeded }'>
							<a class="btn btn-xianshi" onclick='editReturn();'>返回</a>
						</c:when>
						<c:otherwise>
						<a href="HealthManagerAction!getDataFromCloud.action?healthFileId=${localHealthFile.healthFileId}" class="btn btn-xianshi">返回</a>
						</c:otherwise>
					</c:choose>
               
        </div>
   </div>
   <!-- 表单表格 end-->
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
<script src="/jmjkms/js/base.js"></script>
<script src='/jmjkms/js/dkdhcommon.js'></script>
</body>
</html>
