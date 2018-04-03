<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head><meta name="viewport" content="width=device-width, initial-scale=1.0"><meta http-equiv="X-UA-Compatible" content="ie=edge">
  <meta charset="UTF-8">
  <c:choose><c:when test="${session.comHospital.isHead==0}"><title>中电科社区健康管理平台</title></c:when><c:when test="${session.communityHospitalGroup!=null}"><title>社区健康管理系统</title></c:when><c:otherwise><title>社区健康服务站</title></c:otherwise></c:choose>
  <link rel="stylesheet" href="/jmjkms/css/cssreset.css">
  <link rel="stylesheet" href="/jmjkms/css/main.css">
    <link rel="stylesheet" href="/jmjkms/css/font-awesome-4.5.0/css/font-awesome.min.css">
</head>
<body>
  <!-- 顶栏 start -->
  <s:include value="/include/header.jsp" />
  <!-- 顶栏 end -->
  <div class="main-content clearfix">
    <!-- 主菜单 start -->
    <s:include value="/include/nav.jsp" />
    <!-- 主菜单 end -->
    <!-- 主容器 start -->
    <div class="container">
      <h3 class="current-title">员工请假详情</h3>
      <input name="yemianname" value=07202 type="hidden"/>
      <!--///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////-->
      <!-- 结果容器 start -->
      <div class="table-content">
        <table class="table table-bordered" id="jqprint" style="word-break:break-all" >
          <tbody>
      <!--以下是一整行-->
      
      <tr><td colspan="4" class="title-td">详细信息</td></tr>
      <!--**********************少可输可选框************************-->
      <tr>
        <td>员工姓名：</td>
        <td> <font color="red"><c:out value="${staffLeave.TStaff.name }" ></c:out></font>           
         </td>    
         <td>员工编码：</td>
        <td> <font color="red"><c:out value="${staffLeave.TStaff.staffNumber }"></c:out></font>
         </td>  
      </tr>
        <!--*******************************************************-->
        
         <tr>
              <td>请假日期：</td>
              <td>
            <fmt:formatDate value='${staffLeave.leaveDate}' pattern='yyyy-MM-dd'/>
               </td>
           
              <td>销假日期：</td>
              <td>
             <fmt:formatDate value='${staffLeave.comebackDate}' pattern='yyyy-MM-dd'/>
               </td>
           
         </tr>
		  <tr>
            
             <td>请假天数：</td><td >
             <c:out value="${staffLeave.days}"></c:out>
             </td>
			  <td>实际天数：</td><td >
            <c:out value="${staffLeave.actualDays}"></c:out>
             </td>
            </tr>
          <tr>
            
             <td>请假原因：</td><td  colspan="3">
    			<c:out value="${ staffLeave.leaveReason}"></c:out>
             </td>
            </tr>
          <tr >
          <td>说明：</td>
          <td colspan="3" >
         	<c:out value="${staffLeave.note }"></c:out>
          </td></tr>

 
          </tbody>
        </table>
        <div class="btn-content">
         <a href="javascript:;" class="btn btn-print btn-dayin">打印</a>
          <a href="javascript:history.go(-1)" class="btn">返回</a>
        </div>
      </div>
      <!-- 结果容器 end -->
    </div>
    <!-- 主容器 end -->
    <!-- 底栏 Start-->
    <s:include value="/include/footer.jsp" />
    <!-- 底栏 Start-->
  </div>

<script src="/jmjkms/js/jquery-1.4.4.min.js"></script>
<script src="/jmjkms/js/laydate/laydate.js"></script>
<script src="/jmjkms/js/base.js"></script>
<script src="/jmjkms/js/jquery.jqprint-0.3.js"></script>
</body>
</html>
