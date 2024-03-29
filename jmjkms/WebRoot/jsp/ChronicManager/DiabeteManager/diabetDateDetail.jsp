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
     <link rel="stylesheet" type="text/css" href="css/sweetalert.css"> 
     <link type="text/css" href="/jmjkms/css/confirm.css">
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
       <h3 class="current-title">血糖详情</h3>
       <div class="search-content">
        <input name="yemianname" value="02205" type="hidden"/>
          <div class="btn-content">
            <a href="chronicManagerAction!getBloodSugerFromCloud.action?bloodForward=0&healthFileId=${healthFile.healthFileId }" class="btn btn-default">数据分析</a>
            <a href="chronicManagerAction!getBloodSugerFromCloud.action?bloodForward=1&healthFileId=${healthFile.healthFileId } " class="btn btn-default">趋势分析</a>
            <a href="chronicManagerAction!getBloodSugerFromCloud.action?bloodForward=2&healthFileId=${healthFile.healthFileId }" class="btn ">详细数据</a>
             </div>
      </div>
   <form id="registerform" action="chronicManagerAction!bloodSugerDataDetail.action" method="post">
           <div class="search-item">
           <s:radio name="searchType" cssClass="type" list="#{'0':'按时间查询','1':'按数量查询'}"  label="请选择查询类型" ></s:radio>
            <label><input type="hidden" name="healthFileId" value="${healthFile.healthFileId }" /></label>
             </div>
               <div class="search-item">
               <div id="time" style="display: inline-block;">
                 <label>时间:<input type="text" class="w150" id="startTime" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})" value="<fmt:formatDate value="${startTime }" pattern="yyyy-MM-dd HH:mm:ss"/>" name="startTime" onfocus="this.blur()">
                                                            至
                  <input type="text" class="w150" id="endTime" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})" value="<fmt:formatDate value="${endTime }" pattern="yyyy-MM-dd HH:mm:ss"/>" name="endTime" onfocus="this.blur()">
               </label>
               </div>
                  <div  id="size" style="display:none;">
                  <label >最近信息条数：</label>
                  <input type="text" class="w40" name="totalSize" id="num" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength='10' datatype="n" value="${ totalSize}">
                  </div>
                  <input type="submit" id="sub" class="btn" value="查询"></input>
                </div>
             </form>
       <!-- 表单表格 start -->
   <div class="table-content">
       <table class="table table-bordered">
         <tbody>
           <tr>
             <td colspan="10" class="title-td">详细数据</td>
           </tr>
           <tr>
             <td>姓名</td>
             <td>测量时间</td>
             <td>测量条件</td>
             <td>服用药物</td>
             <td>血糖值(%)</td>
             <td>血糖分析结果</td>
             <td class="caozuo">操作</td>
           </tr>
         <s:iterator value="bloodSugerList" var="bsl">
           <tr>
             <td>${healthFile.name }</td>
             <td>${bsl.meaTime}</td>
             <c:if test="${bsl.meaState == 0}"><td>随机血糖</td></c:if>
             <c:if test="${bsl.meaState == 1}"><td>空腹血糖</td></c:if>
             <c:if test="${bsl.meaState == 2}"><td>餐后血糖</td></c:if>
             <c:if test="${bsl.takeMed != 1}"><td>未服</td></c:if>
             <c:if test="${bsl.takeMed == 1}"><td>${bsl.medName}</td></c:if>
             <td>${bsl.bloodSugar}</td>
             <td>${bsl.analysisResult}</td>
             <td class="caozuo">
               <a class="btn-xiugai" href="chronicManagerAction!toEditBSCloud.action?healthFileId=${healthFileId }&cloudId=${bsl.id}&bloodSugar=${ bsl.bloodSugar}&meaState=${bsl.meaState }&takeMed=${bsl.takeMed }&medName=${bsl.medName }&abnAlarm=${bsl.abnAlarm }&meaTime=${bsl.meaTime}&analysisResult=${bsl.analysisResult}">修改</a>
               <a class="btn-shanchu" href="chronicManagerAction!deleteBloodSugerDataCloud.action?healthFileId=${healthFileId }&cloudId=${bsl.id}" onclick="return confirm('确认删除')">删除</a>
             </td>
           </tr>
         </s:iterator>
         </tbody>
       </table>
      <div class="page">
				${pageHtml}
	  </div>
	  <div class="btn-content">
             <a href="chronicManagerAction!getAllDiabeteManager.action" class="btn btn-xianshi">返回</a>
        </div>
    
   </div>
   <!-- 表单表格 start -->
    </div>
    <!-- 主容器 end -->
    <!-- 底栏 Start-->
    <s:include value="/include/footer.jsp" />
    <!-- 底栏 Start-->
  </div>

<script src="/jmjkms/js/jquery-2.1.1.min.js"></script>
<script src="/jmjkms/js/laydate/laydate.js"></script>
  <script src="js/sweetalert.min.js"></script> 
<script src="/jmjkms/js/base.js"></script>
<script src="/jmjkms/js/healthManager.js"></script>
<script type="text/javascript" src="/jmjkms/js/confirm/Validform_v5.3.2_min.js"></script>
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
<script type="text/javascript">
	$(function(){
		var searchType=${searchType};
		if(searchType==1){
		 document.getElementsByName('searchType')[1].checked=true; 
	 }	
	 else{
	 	 document.getElementsByName('searchType')[0].checked=true;
	 } 
	})
</script>
<script type="text/javascript">
 $("form").submit(function(){
    	var startTime0=$("#startTime").val();
    	var endTime0=$("#endTime").val();
    	if(startTime0 != ""||endTime0 != ""){
    	if(startTime0>endTime0){
      	 	$("#startTime").val("");
       		$("#endTime").val("");
       		alert("查询时间输入错误");
       		return false;
       	}
       	}
       	 	if(startTime0=="" && endTime0=="" && $("#num").val()==""){
       			alert("请输入查询条件!")
       			return false;
       		}
    })
</script>
<script type="text/javascript">
      $(function(){
           var value = ${searchType}+"";
           hiddenSearch(value);
      }); 
		$(".type").change(function(){hiddenSearch($(this).val())});
		function hiddenSearch(value){
			if(value==="0"){
			    $("#time").css("display","inline-block");
			    $("#size").css("display","none");
			}
		    if(value==="1"){
			     $("#time").css("display","none");
			     $("#size").css("display","inline-block"); 
			}
		}; 
</script>
<script type="text/javascript">
	$(function(){
	var person=${person};
	 if(person=='0'){
		$('.caozuo').remove();
	  }
	});
	
</script>
<script>
	function Num(obj){
	obj.value = obj.value.replace(/[^\d.]/g,"");
	obj.value = obj.value.replace(/^\./g,""); 
	obj.value = obj.value.replace(/\.{2,}/g,"."); 
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
        }
</script>
</body>
</html>
