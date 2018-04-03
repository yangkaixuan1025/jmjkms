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
  
</head>
<body>
  <!-- 顶栏 start -->
  <s:include value="/include/header.jsp" />
  <!-- 顶栏 end -->
  <div class="main-content clearfix">
    <s:include value="/include/nav.jsp" />
    <!-- 主菜单 end -->
    <!-- 主容器 start -->               <!-- 冠心病-->
    <div class="container">
      <h3 class="current-title">其他疾病管理</h3>     
       <!-- 主容器 start -->
    <div class="container">
     <input name="yemianname" value="02206" type="hidden"/>

				<div class="table-content">
					<table class="table table-bordered" id="jqprint">
						<tr>
							<td colspan="8" style="text-align:right;">姓名：<c:out
									value="${vchronic.fileName}"></c:out>&nbsp;&nbsp;&nbsp;&nbsp;档案编号：<c:out
									value="${vchronic.fileNum}"></c:out></td>
						</tr>
						<tbody>
							<tr>
								<td>冠心病类型</td>
								<td colspan="7"><label><c:out
											value="${vchronic.coronaryDiseaseType}"></c:out></label></td>
							</tr>
							<tr>
								<td>首诊信息</td>
								<td colspan="7"><label><c:out
											value="${vchronic.coronaryDiseaseFristDiagnose}"></c:out></label></td>
							</tr>
							<tr>
								<td>危险原因</td>
								<td colspan="3"><label><c:out
											value="${vchronic.coronaryDiseaseCause}"></c:out></label></td>
								<td>起病形式</td>
								<td colspan="3"><label><c:out
											value="${vchronic.coronaryDiseaseForm}"></c:out></label></td>
							</tr>
							<tr>
								<td>并发症</td>
								<td colspan="7"><label> <c:out
											value="${vchronic.coronaryDiseaseComplication}"></c:out></label></td>
							</tr>
							<tr>
								<td>用药情况</td>
								<td colspan="3"><label><c:out
											value="${vchronic.coronaryDiseaseUseDrug}"></c:out></label></td>
								<td>心电图</td>
								<td colspan="3"><label><c:out
											value="${vchronic.electrocardiogram}"></c:out></label></td>
							</tr>
							<tr>
								<td>脑电图</td>
								<td colspan="3"><label><c:out
											value="${vchronic.electroencephalogram}"></c:out></label></td>
								<td>X线</td>
								<td colspan="3"><label><c:out
											value="${vchronic.coronaryDiseaseX}"></c:out></label></td>
							</tr>
							<tr>
								<td>CT</td>
								<td colspan="3"><label><c:out
											value="${vchronic.coronaryDiseaseCt}"></c:out></label></td>
								<td>脑血管造影</td>
								<td colspan="3"><label><c:out
											value="${vchronic.coronaryDiseaseCerebralBloodVessel}"></c:out></label>
								</td>
							</tr>
							<tr>
								<td>备注</td>
								<td colspan="7"><label><c:out
											value="${vchronic.coronaryDiseaseRemarks}"></c:out> </label></td>
							</tr>
							<tr>
								<td>是否添加到慢病管理</td>
								<td colspan="7"><s:if test="vchronic.status==0">
										<label><c:out value="否"></c:out></label>
									</s:if> <s:if test="vchronic.status!=0">
										<label><c:out value="是"></c:out></label>
									</s:if></td>
							</tr>
							<tr>
								<td>被调查人</td>
								<td><label><c:out
											value="${vchronic.ivestingPerson}"></c:out> </label>
								</td>
								<td>建档人</td>
								<td><label><c:out
											value="${vchronic.chronicDiseaseFileName}"></c:out></label></td>
								<td>建档日期</td>
								<td   colspan="2"><label><fmt:formatDate
											value="${vchronic.chronicDiseaseFileDate}"
											pattern="yyyy-MM-dd" /></label></td>
							</tr>
						</tbody>
					</table>
					<div class="btn-content">
						<a href="javascript:;" class="btn btn-print"
							class="btn btn-xianshi">打印</a> <a
							href="javascript:history.go(-1)" class="btn">返回</a>
					</div>
				</div>
			</div>
    <!-- 主容器 end -->



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
