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
  <title>血糖健康管理</title>
  <link rel="stylesheet" href="/jmjkms/css/confirm.css">
  <link rel="stylesheet" href="/jmjkms/css/cssreset.css">
  <link rel="stylesheet" href="/jmjkms/css/main.css">
  <link rel="stylesheet" href="/jmjkms/css/font-awesome-4.5.0/css/font-awesome.min.css">
  <link rel="stylesheet" href="/jmjkms/select2/css/select2.css">
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
        <!-- 主容器 start -->
    <div class="container">
      <h3 class="current-title">高血糖健康管理</h3>     
             <!-- 搜索容器 start -->
     <input name="yemianname" value="02205" type="hidden"/>
 	 <form id="entity" action="<%=basePath %>showOldChronicAction!searchChronicPeople.action"  method="post">
 			<div class="search-item">
			       <input id="cp" type="hidden" name="cp" value="${cp}">
			       
			       <s:if test="issq==0">
       				<label>社区医院：</label>
       				<select name="searchbean.sqid">
       				<option value="">全部</option>
       	 			<s:iterator value="hospitals" var="h">
       				<option value="${h.communityHospitalId}" <c:if test="${h.communityHospitalId eq searchbean.sqid}">selected</c:if>>${h.communityHospital}</option>
       	 			</s:iterator>
       				</select>
       				</s:if>
       				
       				<s:if test="issq==1">
       				<label>集团：</label>
       				<select name="searchbean.sqid">
       				<option value="">全部</option>
       	 			<s:iterator value="groups" var="h">
       				<option value="${h.GId}" <c:if test="${h.GId eq searchbean.sqid}">selected</c:if>>${h.groupName}</option>
       	 			</s:iterator>
       				</select>
       				</s:if>
       				
			       <label>查询选项：<input type="hidden" name="issetsearch" value="ture"></input></label>
			       <select name="searchbean.selectFlag" >
			         <%-- <option value="0" <c:if test="${'0' eq searchbean.selectFlag}">selected</c:if>>请选择</option> --%>
			         <option value="1" <c:if test="${'1' eq searchbean.selectFlag}">selected</c:if>>姓名</option>
			         <option value="2" <c:if test="${'2' eq searchbean.selectFlag}">selected</c:if>>身份证号</option>
			         <option value="3" <c:if test="${'3' eq searchbean.selectFlag}">selected</c:if>>档案编号</option>
			       </select>
			       <input name="searchbean.selectValue" maxlength='20' type="text" class="w150 temp" maxlength='20' value="${searchbean.selectValue}">
			       <label>年龄：</label>
			       <input name="searchbean.lowAge" maxlength='3' type="text" class="w40 temp" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" value="<c:if test="${searchbean.lowAge!=0}">${searchbean.lowAge}</c:if>">至<input  name="searchbean.highAge" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength='3' value="<c:if test="${searchbean.highAge!=0}">${searchbean.highAge}</c:if>" type="text" class="w40">岁
     		</div>
			    <%--  <div class="search-item">
			       <label>出生日期：</label>
			       <input class="startTime" onfocus="this.blur()"
			        name="searchbean.lowBrithday" type="text" class="w150" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})" value="<fmt:formatDate value="${searchbean.lowBrithday}"  pattern="yyyy-MM-dd"/>">
			       至
			       <input class="endTime" onfocus="this.blur()"
			        name="searchbean.highBrithday" type="text" class="w150" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})" value="<fmt:formatDate value="${searchbean.highBrithday}"  pattern="yyyy-MM-dd"/>">
			     </div> --%>
			     <div class="search-item">
			       <label>建档日期：</label>
			       <input class="startTime" onfocus="this.blur()"
			        name="searchbean.lowFileDate" type="text" class="w150" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})" value="<fmt:formatDate value="${searchbean.lowFileDate}"  pattern="yyyy-MM-dd"/>">
			       至
			       <input class="endTime" onfocus="this.blur()"
			        name="searchbean.highFileDate" type="text" class="w150" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})" value="<fmt:formatDate value="${searchbean.highFileDate}"  pattern="yyyy-MM-dd"/>">
			     </div>
			         <s:if test="issq==2">
     <div class="search-item">
     <s:if test="flag==false">
       <label>责任医生：</label>
       <%-- <select name="searchbean.name">
       	 <option value="">请选择</option>
         <s:iterator value="staffs" var="s">
         <option value="${s.name}" <c:if test="${s.name eq searchbean.name}">selected</c:if>>${s.name}</option>
         </s:iterator>
       </select> --%>
       		<select name="searchbean.name" class="js-example-basic-single" class="select2" style="width: 170px;" >
			 <option value="">请选择</option>
         	 <s:iterator value="specialDoctor" var="s">
         	 	<option value="${s.name}" <c:if test="${s.name eq searchbean.name}">selected</c:if>>${s.name}</option>
			 </s:iterator>
			</select>
    
       <label>建档人：</label>
       <%-- <select name="searchbean.filePeople" class="js-example-basic-single" class="select2" style="width: 170px;">
         <option value="">请选择</option>
         <c:forEach  items="${staffs}" var="s">
         <option value="${s.name}" <c:if test="${s.name eq searchbean.filePeople}">selected</c:if>>${s.name}</option>
         </c:forEach>
       </select> --%>
       <select name="searchbean.filePeople" class="js-example-basic-single" class="select2" style="width: 170px;">
			<option value="">请选择</option>
         	<s:iterator value="staffs" var="s">
         	 	<option value="${s.name}" <c:if test="${s.name eq searchbean.filePeople}">selected</c:if>>${s.name}</option>
			</s:iterator>
	   </select>
       <label>录入人：</label>
       <%-- <select name="searchbean.enterPeople">
         <option value="">请选择</option>
         <c:forEach  items="${staffs}" var="s">
         <option value="${s.name}" <c:if test="${s.name eq searchbean.enterPeople}">selected</c:if>>${s.name}</option>
         </c:forEach>
       </select> --%>
       <select name="searchbean.enterPeople" class="js-example-basic-single" class="select2" style="width: 170px;">
			 <option value="">请选择</option>
         	 <s:iterator value="staffs" var="s">
         	 	<option value="${s.name}" <c:if test="${s.name eq searchbean.enterPeople}">selected</c:if>>${s.name}</option>
			 </s:iterator>
	   </select>
	   </s:if>
     </div>
      
     </s:if>
			     <div class="search-item">
			       <label>性别查询：</label>
			       <input id="all-sex" type="radio" name="searchbean.sex" value="0" <c:if test="${searchbean.sex==null||searchbean.sex==0}"> checked</c:if> ><label for="all-sex">全部</label>
			       <input id="man" type="radio"  name="searchbean.sex" value="2" <c:if test="${searchbean.sex==2 }"> checked</c:if>><label for="man">男</label>
			       <input id="women" type="radio"  name="searchbean.sex" value="1" <c:if test="${searchbean.sex==1 }"> checked</c:if>><label for="women">女</label>
			     </div>	
			      <input name="searchbean.renqun" value="2" type="hidden"/>
			      <input name="flagsear" value="flagDiabete" type="hidden"/>	  	
			      <div class="search-item">
      				 <label>档案状态：</label>
       				 <input id="zhengchang" type="radio" name="searchbean.danganzhuangtai" value="0" <c:if test="${searchbean.danganzhuangtai==null||searchbean.danganzhuangtai==0}"> checked</c:if>><label for="zhengchang">正常</label>
      				 <input id="zhuxiao" type="radio"  name="searchbean.danganzhuangtai" value="1" <c:if test="${searchbean.danganzhuangtai==1}"> checked</c:if>><label for="zhuxiao">注销</label>
      				 <input id="siwang" type="radio"  name="searchbean.danganzhuangtai" value="2" <c:if test="${searchbean.danganzhuangtai==2}"> checked</c:if>><label for="siwang">死亡</label>
     			</div>
			     <div class="btn-content">
			       <input type="submit" value="查询" id="" name="" class="btn">
			       <a id="news" href="javascript:;" class="btn">重置</a>
			   		<a href="chronicManagerAction!addDiabeteManager.action" class="btn btn-xinzeng" >添加老人</a>
			     </div>
		</form>
	 
			 <!-- 搜索容器 end --> 
        <form action="<%=basePath %>chronicManagerAction!diabeteExport.action" method="post">
        <h3 class="current-title">查询结果</h3>
        <div class="table-content">
          <table class="table table-bordered">
           <thead>
            <tr>
               <th>姓名</th>
               <th>性别</th>
              <th>居民健康档案编号</th>
               <th>联系方式</th>
               <th>现住址</th>
               <th>责任医生</th>
              <th>血糖详情</th>
              <th>医生诊断</th>
              <th>康复计划</th>
              <th>慢病操作</th>
            </tr>
          </thead>
          <tbody>
          <s:iterator value="vclist" var="vc" >
           <input type="hidden" name="ids" id="id" value="${vc.chronicDiseaseId}">
            <tr>
              <td>${vc.fileName}</td>
              <td>
                 <c:if test="${!sex}">男</c:if>
                 <c:if test="${sex}">女</c:if>
              </td>
              <td>${vc.fileNum}</td>
              <td>${vc.IPhone}</td>
              <td>${vc.currentProvince}</td>
              <td>${vc.name}</td>              
              <td> <a class="btn-xianshi" href="<%=basePath %>chronicManagerAction!getBloodSugerFromCloud.action?healthFileId=${vc.healthFileId }">详情</a></td>
              <td> <a class="btn-xianshi" href="<%=basePath %>doctorDiagnosisAction!showDoctorDiagnosis.action?chronicName=${vc.diseaseName}&daId=${vc.healthFileId}">详情</a></td>
              <td> <a class="btn-xianshi" href="chronicManagerAction!searchRecoveryPlan.action?chronicId=${vc.chronicDiseaseId}">详情</a></td>
              <td>
                <a class="btn-xianshi" href="chronicManagerAction!infoChronic.action?chronicId=${vc.chronicDiseaseId}">查看</a>
                <a class="btn-shanchu" href="chronicManagerAction!deleteDiabeteManager.action?daId=${vc.healthFileId }&chronicName=${vc.diseaseName}" onclick="return confirm('是否删除？')">删除</a>
              </td>
            </tr>
            </s:iterator>
            
          </tbody>
        </table>
        <input type="hidden" name="exportFlag"  id="act" value=""/>
       		 <div class="sum-btn">
	       		 <div class="page">
					${pageHtml}
		    	</div>
		    	
		        <div class="btn-content2">
		          <input class="btn btn-daochu" type="submit" class="btn"  value="导出本页"  onclick="run01()"></input>
		          <input class="btn btn-daochu" type="submit" class="btn"  value="导出全部"  onclick="run02()"></input>
		        </div> 
	        </div>
	    	
      </div>
      <!-- 表格容器 end -->
    </form>
  </div> 
 <!-- 搜索容器 end -->
 <s:include value="/include/footer.jsp" />
   </div>
 




<script type="text/javascript" src="/jmjkms/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/jmjkms/js/base.js"></script> 
<script src="/jmjkms/js/laydate/laydate.js"></script><!-- 这个是时间插件的js -->
<!-- 3级联动 -->
<script type="text/javascript" src="/jmjkms/js/threecity/jq.js"></script>  
<script type="text/javascript" src="/jmjkms/js/threecity/jquery.cityselect.js"></script>
<script type="text/javascript" src="/jmjkms/js/threecity/location.js"></script>
<!-- 3级联动 -->
<!-- 验证的js -->
<script  type="text/javascript" src="/jmjkms/js/confirm/Validform_v5.3.2_min.js"></script>
<script src="/jmjkms/select2/js/select2.full.js"></script>
<script type="text/javascript">
    $(function(){
      $("#registerform").Validform({
        showAllError:false,
        tiptype:function(msg){          
          alert(msg);
        }
      });
  });
  $(function(){
  		$(".js-example-basic-single").select2();
  });
</script>
<!-- 验证的js -->
<script type="text/javascript">
    var Obtn01=document.getElementById("btn01");
    var Obtn02=document.getElementById("btn02");
    var Oact  =document.getElementById("act");
    function run01(){
        Oact.value=1;
    }
    function run02(){
        Oact.value=2;
    }
    function run03(){
        Oact.value=3;
    }
</script>
<script type="text/javascript">
 $("form").submit(function(){
    	var startTime0=$(".startTime").eq(0).val();
    	var startTime1=$(".startTime").eq(1).val();
    	var endTime0=$(".endTime").eq(0).val();
    	var endTime1=$(".endTime").eq(1).val();
    	if(startTime0 != ""&&endTime0 != ""){
    	if(startTime0>endTime0){
      	 	$(".startTime").eq(0).val("");
       		$(".endTime").eq(0).val("");
       		alert("查询时间顺序输入错误");
       		return false;
       	}
       	}
       	if(startTime1 != ""&&endTime1 != ""){
    	if(startTime1>endTime1){
      	 	$(".startTime").eq(1).val("");
       		$(".endTime").eq(1).val("");
       		alert("查询时间顺序输入错误");
       		return false;
       	}
       	}
    })
</script>
<script type="text/javascript">
		$("#news").bind("click", function () {
               	$(":radio").eq(0).attr("checked", true);
                $(":radio").eq(3).attr("checked", true);
                $(":text").val("");
    			$("textarea").val("");
    			$("select").val("");
            });
</script>
<script>
function sumit(a){
		
			//document.getElementById("entity").submit();
			var g = /^[1-9]*[1-9][0-9]*$/;
            if(g.test(a)){
            		$("#cp").val(a);
            		$("#entity").submit();
            }
            else{
            	if($("#go").val()!=''){
            			if(g.test($("#go").val())){
								$("#cp").val($("#go").val());
								$("#entity").submit();
							}
					}
           	 }
}
</script>
<script type="text/javascript">
	$(function(){
		$(".js-example-basic-single").select2();
	})
</script>

</body>
</html>
