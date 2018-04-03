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
      <h3 class="current-title">尿常规检查详情</h3>
      <input name="yemianname" value="02201" type="hidden"/>
      <!-- 表单表格 start -->
   <div class="table-content">
     <form action="#" method="post">
     <input type="hidden" name="healthFileId" value="${localHealthFile.healthFileId }">
      <input type="hidden" name="urineId" value="${urine.id }">
       <table class="table table-bordered">
         <tbody>
           <tr>
     <th width='10%'>检查时间</th>
     <td colspan='5'><c:out value="${urine.meaTime }"></c:out></td>
   </tr>
   <tr>
     <th>姓名</th>
     <td colspan='2'><c:out value="${name }"></c:out></td>
     <th width='10%'>身份证号</th>
     <td colspan='2'><c:out value="${urine.idCard }"></c:out></td>
   </tr>
   <tr style='border-bottom:3px solid black;'>
     <th>检查序号</th>
     <td colspan='2'><c:out value="${urine.id }"></c:out></td>
     <th>设备编号</th>
     <td colspan='2'><c:out value="${urine.devID }"></c:out></td>
   </tr>
   <tr>
     <th>检查项</th>
     <th>测量结果</th>
     <th>正常参考值</th>
     <th>检查项</th>
     <th>测量结果</th>
     <th>正常参考值</th>
   </tr>
   <tr>
      <th>白细胞(WBC)</th>
      <td width='15%'><c:out value="${urine.wbc }"></c:out></td>
      <td>参考值：阴性（&lt;15个白细胞/μL）</td>
      <th>亚硝酸盐(NIT)</th>
     <td width='15%'><c:out value="${urine.nit }"></c:out></td>
     <td>参考值：阴性</td>
   </tr>
   <tr>
      <th>酮体(KET)</th>
     <td><c:out value="${urine.ket }"></c:out></td>
     <td>参考值：阴性</td>
    <th>胆红素(BIL)</th>
     <td><c:out value="${urine.bil }"></c:out></td>
     <td>参考值：阴性</td>
   </tr>
   <tr>
    <th>尿胆原(URO)</th>
     <td><c:out value="${urine.uro }"></c:out></td>
     <td>参考值：阴性或弱阳性</td>
     <th>葡萄糖(GLU)</th>
     <td><c:out value="${urine.glu }"></c:out></td>
     <td>参考值：阴性（&lt;2mmol/L）</td>
   </tr>
   <tr>
     <th>蛋白质(PRO)</th>
     <td><c:out value="${urine.pro }"></c:out></td>
     <td>参考值：阴性（&lt;0.1g/L）</td>
     <th>隐血(BLD)</th>
     <td><c:out value="${urine.bld }"></c:out></td>
     <td>参考值：阴性（&lt;10个红细胞/μL）</td>
   </tr>
   <tr>
     <th>尿比重(SG)</th>
     <td><c:out value="${urine.sg }"></c:out></td>
     <td>参考值：1.015~1.025</td>
     <th>维生素C（VC）</th>
     <td ><c:out value="${urine.vc }"></c:out></td>
     <td>参考值：阴性（&lt;0.570mmol/L）</td>
   </tr>
   <tr>
     <th>酸碱度（PH）</th>
     <td><c:out value="${urine.ph }"></c:out></td>
     <td colspan='4'>参考值：5~7</td>
   </tr>
   
   
         </tbody>
       </table>
        <div class="btn-content">
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
