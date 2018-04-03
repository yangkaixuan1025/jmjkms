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
      <h3 class="current-title">健康一体机报表详情</h3>
      <input name="yemianname" value="02201" type="hidden"/>
      <!-- 表单表格 start -->
   <div class="table-content">
     <form action="HealthManagerAction!uploadAllInOne.action" method="post">
     <input type="hidden" name="healthFileId" value="${localHealthFile.healthFileId }">
      <input type="hidden" name="cloudId" value="${allInOne.id }">
      <input type='hidden' name='embeded' value='${embeded }'>
       <table class="table table-bordered">
         <tbody>
           <tr>
             <td>姓名</td>
             <td colspan="2">${localHealthFile.name}</td>
             <td>性别</td>
             <c:if test="${localHealthFile.sex == false}"><td>男</td></c:if>
             <c:if test="${localHealthFile.sex == true}"><td>女</td></c:if>
             <td>出生日期</td>
             <td colspan="2"><fmt:formatDate value="${localHealthFile.birthDate}" pattern="yyyy-MM-dd"/></td>
           </tr>
           <tr>
             <td>身份证号</td>
             <td colspan="2">${localHealthFile.idNum}</td>
             <td>责任医生</td>
             <td>${voldStaff.SName}</td>
             <td>联系方式</td>
             <td colspan="2">${localHealthFile.IPhone}</td>
           </tr>
           <tr>
             <td rowspan="10">基本指标</td>
             <td>体温</td>
             <td>${allInOne.temperature}℃</td>
             <td>脉率</td>
             <td >${allInOne.pulse}次/分钟</td>
            </tr>
           <tr>
             <td rowspan="3">血糖</td>
             <td>空腹血糖</td>
             <td colspan="4">${allInOne.fbs}mmol/L</td>
           </tr>
           <tr>
             <td>餐后血糖</td>
             <td colspan="5">${allInOne.pbs}mmol/L</td>
           </tr>
               <tr>
             <td>随机血糖</td>
             <td colspan="5">${allInOne.rbs}mmol/L</td>
           </tr>
           <tr>
             <td rowspan="2">血压</td>
             <td>收缩压</td>
             <td colspan="4">${allInOne.sysPre}mmHg</td>
           </tr>
           <tr>
             <td>舒张压</td>
             <td colspan="5">${allInOne.diaPre}mmHg</td>
           </tr>
           <tr>
             <td colspan="2">身高</td>
             <td>${allInOne.height}cm</td>
             <td>体重</td>
             <td colspan="4">${allInOne.weight}kg</td>
           </tr>
           <tr>
             <td colspan="2">血氧</td>
             <td>${allInOne.oxygen}%</td>
             <td>体质指数(BMI)</td>
             <td colspan="4">${allInOne.bmi} kg/m²</td>
           </tr>
           <tr>
             <td colspan="2">脂肪含量</td>
             <td >${allInOne.fat}</td>
             <td>测量时间</td>
             <td colspan="5">${allInOne.meaTime}</td>
           </tr>
           <tr>
           <tr>
             <td>健康评价</td>
             <td colspan="7">
               <textarea name="advice" class="textarea temp">${allInOne.jkpj}</textarea>
             </td>
           </tr>
           <tr>
             <td>健康指导</td>
             <td colspan="7">
               <textarea name="guide" class="textarea temp">${allInOne.jkzd}</textarea>
             </td>
           </tr>
         </tbody>
       </table>
        <div class="btn-content">
        <c:choose>
          <c:when test="${embeded }"></c:when>
          <c:otherwise>
            <a class="btn" href="HealthManagerAction!downloadHealthReport.action?healthFileId=${healthFile.healthFileId }&meaTime=${allInOne.meaTime}&temperature=${allInOne.temperature}&fbs=${allInOne.fbs}&pbs=${allInOne.pbs}&rbs=${allInOne.rbs}&diaPre=${allInOne.diaPre}&sysPre=${allInOne.sysPre}&height=${allInOne.height}&weight=${allInOne.weight}&oxygen=${allInOne.oxygen}&bmia=${allInOne.bmi}&pulse=${allInOne.pulse}&fat=${allInOne.fat}&muscle=${allInOne.muscle}&guide=${allInOne.jkzd}&advice=${allInOne.jkpj}&embeded=${embeded}">下载一体机报表</a>
			<%-- <a href="HealthManagerAction!download.action?healthFileId=${healthFile.healthFileId }&id=${allInOne.id}">下载报表详情</a> --%>
			<input type="submit" class="btn" value="保存">
          </c:otherwise>
        </c:choose>
        		<!-- <a class="btn" href="HealthManagerAction!download.action"></a> -->
        	
              <button class="btn" type="button" onclick="history.go(-1);">返回</button>
         </div>
     </form>
   </div>
   <!-- 表单表格 end -->
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
</body>
</html>
