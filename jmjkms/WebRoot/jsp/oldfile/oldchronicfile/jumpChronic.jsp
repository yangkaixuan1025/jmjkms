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
  <link rel="stylesheet" href="/jmjkms/css/font-awesome-4.5.0/css/font-awesome.min.css">
  <link type="text/css" href="/jmjkms/css/confirm.css">
  
</head>
<body>
  <!-- 顶栏 start -->
  <s:include value="/include/header.jsp" />
  <!-- 顶栏 end -->
  <div class="main-content clearfix">
  <input name="yemianname" value="01202" type="hidden"/>
    <!-- 主菜单 start -->
    <s:include value="/include/nav.jsp" />
    <!-- 主菜单 end -->
    <!-- 主容器 start -->
    <div class="container">
      <h3 class="current-title">慢性病档案</h3>
      
      <!-- 搜索容器 start -->
      <div class="search-content">
        <form id="registerform1" action="<%=basePath %>showOldChronicAction!searchOlder.action" method="post">
          <label>查询选项：</label>
            <select name="idOrfid">
              <option value="0" <c:if test="${'0' eq idOrfid}">selected</c:if>>姓名</option>
              <option value="2" <c:if test="${'2' eq idOrfid}">selected</c:if>>档案编号</option>
              <option value="1" <c:if test="${'1' eq idOrfid}">selected</c:if>>身份证号</option>
            </select>            
            <input id="searchText"  datatype="*"  nullmsg="请输入查询信息"  type="text" class="150 temp" name="idcardOrfild" maxlength="20"  value="${idcardOrfild}">
            <input type="submit" class="btn" value="查询">&nbsp;
            <a href="<%=basePath %>showOldChronicAction!firstShowChronic.action" class="btn">返回</a>
            <span class="required">${nulloldmessage}</span>&nbsp;&nbsp;
            <label>
            	姓名：${healthFile.name}&nbsp;&nbsp;&nbsp;&nbsp;档案编号：${healthFile.fileNum}&nbsp;&nbsp;&nbsp;&nbsp;
            	已有疾病：<s:iterator value="vclist" var="vl"><s:if test="status!=9">${vl.diseaseName}</s:if>&nbsp;&nbsp;&nbsp;&nbsp; </s:iterator>
            </label>
        </form>
         <div class="btn-content">
             <c:choose>
				<c:when test="${empty healthFile.healthFileId}"> 
				<a <s:iterator value="vclist" var="vl"><c:if test="${vl.diseaseName eq '高血压'}">style="display:none;"</c:if></s:iterator> href="<%=basePath %>showOldChronicAction!jumpOneHighBlood.action?helid=0"  class="btn">高血压</a>
	            <a <s:iterator value="vclist" var="vl"><c:if test="${vl.diseaseName eq '糖尿病'}">style="display:none;"</c:if></s:iterator> href="<%=basePath %>showOldChronicAction!jumpDiabete.action?helid=0"class="btn">糖尿病</a>
	            <a <s:iterator value="vclist" var="vl"><c:if test="${vl.diseaseName eq '重性精神疾病'}">style="display:none;"</c:if></s:iterator> href="<%=basePath %>showOldChronicAction!jumpInsanityform.action?helid=0" class="btn">重性精神疾病</a>
	            <a <s:iterator value="vclist" var="vl"><c:if test="${vl.diseaseName eq '冠心病'}">style="display:none;"</c:if></s:iterator> href="<%=basePath %>showOldChronicAction!jumpCoronary.action?helid=0" class="btn">冠心病</a>	            	
            	<a <s:iterator value="vclist" var="vl"><c:if test="${vl.diseaseName eq '慢性支气管病'}">style="display:none;"</c:if></s:iterator> href="<%=basePath %>showOldChronicAction!jumpBronchial.action?helid=0" class="btn">慢性支气管病</a>      
				<a <s:iterator value="vclist" var="vl"> <c:if test="${vl.diseaseName eq '肿瘤'}"> style="display:none;" </c:if> </s:iterator> href="<%=basePath %>showOldChronicAction!jumpTumour.action?helid=0" class="btn">肿瘤</a>
	            <a <s:iterator value="vclist" var="vl"><c:if test="${vl.diseaseName eq '心脑血管病'}">style="display:none;"</c:if></s:iterator> href="<%=basePath %>showOldChronicAction!jumpHcvd.action?helid=0" class="btn">心脑血管病</a>
	            <a <s:iterator value="vclist" var="vl"><c:if test="${vl.diseaseName eq '残疾障碍'}">style="display:none;"</c:if></s:iterator> href="<%=basePath %>showOldChronicAction!jumpDisability.action?helid=0" class="btn">残疾障碍</a>	          
				 </c:when>
				 <c:otherwise> 
				 <a <s:iterator value="vclist" var="vl"><c:if test="${vl.diseaseName eq '高血压'}">style="display:none;"</c:if></s:iterator> href="<%=basePath %>showOldChronicAction!jumpOneHighBlood.action?helid=${healthFile.healthFileId}"  class="btn">高血压</a>
		         <a <s:iterator value="vclist" var="vl"><c:if test="${vl.diseaseName eq '糖尿病'}">style="display:none;"</c:if></s:iterator> href="<%=basePath %>showOldChronicAction!jumpDiabete.action?helid=${healthFile.healthFileId}"class="btn">糖尿病</a>
		         <a <s:iterator value="vclist" var="vl"><c:if test="${vl.diseaseName eq '重性精神疾病'}">style="display:none;"</c:if></s:iterator> href="<%=basePath %>showOldChronicAction!jumpInsanityform.action?helid=${healthFile.healthFileId}" class="btn">重性精神疾病</a>
		         <a <s:iterator value="vclist" var="vl"><c:if test="${vl.diseaseName eq '冠心病'}">style="display:none;"</c:if></s:iterator> href="<%=basePath %>showOldChronicAction!jumpCoronary.action?helid=${healthFile.healthFileId}" class="btn">冠心病</a>
		         <a <s:iterator value="vclist" var="vl"><c:if test="${vl.diseaseName eq '慢性支气管病'}">style="display:none;"</c:if></s:iterator> href="<%=basePath %>showOldChronicAction!jumpBronchial.action?helid=${healthFile.healthFileId}" class="btn">慢性支气管病</a>      								 
	             <a <s:iterator value="vclist" var="vl"> <c:if test="${vl.diseaseName eq '肿瘤'}"> style="display:none;" </c:if> </s:iterator> href="<%=basePath %>showOldChronicAction!jumpTumour.action?helid=${healthFile.healthFileId}" class="btn">肿瘤</a>
	             <a <s:iterator value="vclist" var="vl"><c:if test="${vl.diseaseName eq '心脑血管病'}">style="display:none;"</c:if></s:iterator> href="<%=basePath %>showOldChronicAction!jumpHcvd.action?helid=${healthFile.healthFileId}" class="btn">心脑血管病</a>
	             <a <s:iterator value="vclist" var="vl"><c:if test="${vl.diseaseName eq '残疾障碍'}">style="display:none;"</c:if></s:iterator> href="<%=basePath %>showOldChronicAction!jumpDisability.action?helid=${healthFile.healthFileId}" class="btn">残疾障碍</a>          
				 </c:otherwise>
			</c:choose></div>
          
          <div id="showwindow" class="table-content"" id="button_alert_2">
 			<h3 class="current-title">查询结果</h3>
 			<table class="table table-bordered">
  			<thead>
			<tr>
				<th>姓名</th>
				<th>身份证号</th>
			</tr>
			</thead>
			<tbody>
				<s:iterator value="listolder" var="lo">
					<tr>
					<td><a href="<%=basePath %>showOldChronicAction!ReOlder.action?helid=${lo.healthFileId}" id="btn_write_off">${lo.name}</a></td>
					<td>${lo.idNum}</td>
				</s:iterator>
			</tbody>
			</table>
		</div>	   
         
         
          
      </div>
      		<%-- <div class="btn-content">
   				<a href="<%=basePath %>showOldChronicAction!firstShowChronic.action" class="btn">返回</a>
			</div> --%>
      <!-- 结果容器 end -->
    </div>
    
    <!-- 主容器 end -->
    <!-- 底栏 Start-->
    <s:include value="/include/footer.jsp" />
    <!-- 底栏 Start-->
  </div>
<input type="hidden" name="helid" value="${healthFile.healthFileId}">
<script src="/jmjkms/js/jquery-2.1.1.min.js"></script>
<script src="/jmjkms/js/laydate/laydate.js"></script>
<script src="/jmjkms/js/base.js"></script>
<script src="/jmjkms/js/sweetalert.min.js"></script>
<script type="text/javascript" src="/jmjkms/js/confirm/Validform_v5.3.2_min.js"></script>

<script type="text/javascript">
		$(function() {
			$("#registerform1").Validform({
				//btnSubmit:".btn_sub",
				showAllError : false,
				tiptype : function(msg) {
					alert(msg);
				}
			});
		});
	</script>
<script type="text/javascript">
	$(function(){
		var show=${showwindow};
		if(show){
			$("#showwindow").css("display","block");
		}else
		{
			$("#showwindow").css("display","none");
		}
	})

</script>

</body>
</html>
