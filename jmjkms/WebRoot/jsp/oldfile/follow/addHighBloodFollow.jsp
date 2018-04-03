<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta name="viewport" content="width=device-width, initial-scale=1.0"><meta http-equiv="X-UA-Compatible" content="ie=edge">
  <meta charset="UTF-8">
  <c:choose><c:when test="${session.comHospital.isHead==0}"><title>中电科社区健康管理平台</title></c:when><c:when test="${session.communityHospitalGroup!=null}"><title>社区健康管理系统</title></c:when><c:otherwise><title>社区健康服务站</title></c:otherwise></c:choose>
  <link rel="stylesheet" href="/jmjkms/css/cssreset.css">
  <link rel="stylesheet" href="/jmjkms/css/main.css">
  <link rel="stylesheet" href="/jmjkms/css/font-awesome-4.5.0/css/font-awesome.min.css">
  <link type="text/css" href="/jmjkms/css/confirm.css">
  <style type="text/css">
  .jia{
    width: 150px;
    height: 30px;
    /*background: #ccc;*/
    position: relative;
   
  }
  .di{
    width: 100px;
    height: 30px;
    position: absolute;
    top: 0;
    left: 0;
  }
  input[type="text"].gai{
    width: 70px;
    height: 22px;
    display: block;
    position: absolute;
    top: 0;
    left: 0;
  }
  </style>
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
     <h3 class="current-title">慢性病随访记录</h3>
      <div class="search-content">
         <div class="search-item">
           	当前人：<c:out value="${healthFile.name}"></c:out>&nbsp;&nbsp;&nbsp;&nbsp;档案编号：<c:out value="${healthFile.fileNum}"></c:out>&nbsp;&nbsp;&nbsp;&nbsp;
          <div class="btn-content"> 
             <lable class="btn">  
               		${chronicDisease.diseaseName}随访记录
               		<input type = "hidden" name="chronicFollow.diseasrName"  value= "${chronicDisease.diseaseName}" >
               		<input type = "hidden" name="cid"  value= "${chronicDisease.chronicDiseaseId}">
               		<input type = "hidden" name="hid"  value= "${healthFile.healthFileType}">
             </lable>
           </div>
          </div>
         
      </div>
      <form action="<%=basePath %>oldChronicFollowAction!addHighFollow.action?cid=${chronicDisease.chronicDiseaseId}&hid=${healthFile.healthFileId}" onsubmit="return dosubmit()" method="post" id="registerform">
      <div class="table-content">
        <table class="table table-bordered">
          <tbody>
           <tr>
             <td colspan="8" class="title-td">基本信息</td>
           </tr>
           <tr>
             <td><span class="required">*</span>随访日期</td>
               <td> 
               <input type="hidden" name="token" value="${token }" />
               <input type="text" id="time" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})" onfocus="this.blur()" name="chronicFollow.followDate" value="${currentTime}"></td>
             <td>随访方式</td>
             <td >
               <select name="thypertensiveFollow.way">
                 <option value="">请选择</option>
                 <s:iterator value="gxysffs" var="g">
                 <option value="${g.name}">${g.name}</option>
                 </s:iterator>
               </select>
             </td>
             <td><span class="required">*</span>随访医生</td>
             <td colspan="3">
             <input id="sfys" class="temp" type="text" name="chronicFollow.followPeople" maxlength="10" datatype="*" nullmsg="随访医生不可为空">
             </td>
           </tr>
           <tr>
             <td>主要症状</td>
             <td colspan="7">
               <select name="thypertensiveFollow.mainSymptom">
                 <option value="">请选择</option>
                 <s:iterator value="mqzyzz" var="g">
                 <option value="${g.name}">${g.name}</option>
                 </s:iterator>
               </select>
             </td>
           </tr>
           <tr>
             <td colspan="8" class="title-td">体征</td>
           </tr>
           <tr>
             <td>体重（kg）</td>
             <td >
               <input onkeyup="Num(this)" type="text" name="sign.wight" maxlength="10" >
             </td>
              <td>身高（cm）</td>
             <td >
               <input onkeyup="Num(this)" type="text" name="sign.height" maxlength="10" >
             </td>
             <td>体质指数</td>
             <td >
               <input onkeyup="Num(this)" type="text" name="sign.bmi" maxlength="10" >
             </td>
             <td>舒张压（mmhg）</td>
             <td><input onkeyup="Num(this)" type="text" class="w150" name="sign.bloodPressureH" maxlength="10" ></td>
           </tr>
           <tr>
             <td>心率(次/分)</td>
             <td><input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" type="text" class="w40" name="sign.heartRate" maxlength="10" ></td>
             <td>其他</td>
             <td colspan="3"><input   type="text" class="w150 temp" name="sign.others" maxlength="80"></td>
             <td>收缩压（mmhg）</td>
             <td><input onkeyup="Num(this)" type="text" class="w150" name="sign.bloodPressureL" maxlength="10" ></td>
             </tr>
             <tr>
             <td colspan="8" class="title-td">生活方式指导</td>
           </tr>
           <tr>
             <td>日吸烟量平均（支）</td>
             <td colspan="2" ><input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" type="text" class="w40" name="lifestyleGuide.dailyCigaretteSmoking" maxlength="10"></td>
             <td> 日饮酒量平均（两）</td>
             <td colspan="2"><input onkeyup="Num(this)" type="text" class="w40" name="lifestyleGuide.dailyDrinking" maxlength="10"></td>
               <td>遵医行为</td>
                <td >
               <select name="lifestyleGuide.treatmentCompliance">
                 <s:iterator value="zyxw" var="g">
                 <option value="${g.name}">${g.name}</option>
                 </s:iterator>
               </select>
               </td>
            </tr>
             <tr>
             <td>摄盐情况</td>
             <td colspan="2" >
               <select name="lifestyleGuide.takenSaltSituation">
                 <option value="">请选择</option>
                 <s:iterator value="gxyfsyqk" var="g">
                 <option value="${g.name}">${g.name}</option>
                 </s:iterator>
               </select>
             </td>
             
               <td>运动</td>
                <td colspan="2"><input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" type="text" class="w40" name="lifestyleGuide.timesSports" maxlength="10">次/周<input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" type="text" class="w40" name="lifestyleGuide.exerciseDuration" maxlength="10">分钟/次</td>
                <td>心理调整</td>
                <td >
               <select name="lifestyleGuide.psychologicalAdjustment">
                 <option value="">请选择</option>
                 <s:iterator value="gxyfxl" var="g">
                 <option value="${g.name}">${g.name}</option>
                 </s:iterator>
               </select>
               </td>
              
           <tr>
             <td colspan="8" class="title-td">其他</td>
           </tr>
            <tr>
            <td>辅助检查</td>
             <td colspan="7"><input type="text" class="w150 temp" name="thypertensiveFollow.supplementaryExamination" maxlength="80"></td>
             </tr>
             <tr>
              <td>服药依从性</td>
                <td >
               <select name="thypertensiveFollow.medicationCompliance">
               	 <option value="">请选择</option>
                 <s:iterator value="gxyfyycx" var="g">
                 <option value="${g.name}">${g.name}</option>
                 </s:iterator>
               </select>
               </td>
                <td>药物不良反应</td>
                <td colspan="2">
               <select name="thypertensiveFollow.adverseDrugReactions">
                 <option value="无">无</option>
                 <option value="有">有</option>
               </select>
               </td>
                <td>此次随访分类</td>
                <td colspan="2">
               <select name="thypertensiveFollow.followType">
                 <option value="">请选择</option>
                 <s:iterator value="gxysflx" var="g">
                 <option value="${g.name}">${g.name}</option>
                 </s:iterator>
               </select>
               </td>
              </tr>
        <!--  <div class="table-content"> -->
                <tr>
                  <td colspan="8">用药情况</td>
                </tr> 
               
                  <tr>
             <td colspan="2" class="title-td">药品名称</td>
             <td  class="title-td">每天服药次数</td>
             <td colspan="2" class="title-td">每天剂量（mg）</td>
             <td colspan="2" class="title-td">不良反应</td>
             <td class="title-td">操作</td>
                </tr>
            <tr class="dyna-form">
                         <td colspan="2"> <input class="temp"  type="text" name="listdrug[0].drugName" maxlength="20"> </td>
                         <td> <input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" type="text" name="listdrug[0].timesTakeMedicineDaily" maxlength="10"></td>
                         <td colspan="2"> <input onkeyup="Num(this)" type="text" name="listdrug[0].dailyDose" maxlength="10"></td>
                         <td colspan="2"> <input class="temp"  type="text" name="listdrug[0].adverseReactions" maxlength="50"></td>
                         <td>
                         	<a href="javascript:;" class="btn-del">删除</a>
                            <a href="javascript:;" class="btn-add">添加</a>
                         </td>
                         
                      </tr>
                <tr>
                <td>转诊</td>
                <td colspan="3">
                <div class="jia">
               <select id="di" class="di">
                 <option style="display:none"></option>
                 <option value="">请选择</option>
                 <option value="无">无</option>
                 <option value="有：（转诊原因：              机构及科别：             ）">有：（转诊原因：              机构及科别：             ）</option>
               </select>
               <input type="text" id="gai" class="gai"   name="thypertensiveFollow.heartReferral" placeholder="请选择">
               </div>
               </td>
                <td>下次随访日期</td>
                <td colspan="3">
                  <input type="text" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})" onfocus="this.blur()" name="thypertensiveFollow.laterDate">
               </td>
               </tr>
                 <tr rowspan="2">
                <td>备注</td>
                <td colspan="7"><textarea   id=""  class="textarea temp" name="thypertensiveFollow.dailyTip"></textarea></td>
                </tr>

          </tbody>
        </table>
        <div class="btn-content">
          <input type="submit" id="submit" class="btn" value="保存">
          <a href="<%=basePath %>showOldChronicAction!firstShowChronic.action" class="btn">返回</a>
          <!-- <a href="javascript:;" class="btn btn-back">返回</a> -->
        </div>
      </div>
      </form>
      <!-- 删除此行注释，在此添加对应的div -->

   
   </div>
    <!-- 主容器 end -->
    <!-- 底栏 Start-->
    <s:include value="/include/footer.jsp" />
    <!-- 底栏 Start-->
  </div>

<script src="/jmjkms/js/jquery-2.1.1.min.js"></script>
<script src="/jmjkms/js/laydate/laydate.js"></script>
<script src="/jmjkms/js/base.js"></script>
<script type="text/javascript" src="/jmjkms/js/yang/jquery-1.9.1.min.js"></script>
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
<script>
	$("form").submit(function(){
		if($("#time").val()==""){
			alert("随访日期不能为空");
			return false;
		}
	})
</script>
<script>
	function Num(obj){
	obj.value = obj.value.replace(/[^\d.]/g,"");
	obj.value = obj.value.replace(/^\./g,""); 
	obj.value = obj.value.replace(/\.{2,}/g,"."); 
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
}
</script>
<script type="text/javascript">
	
		window.onload = function(){


					var di = document.getElementsByClassName("di");
					var gai = document.getElementsByClassName("gai");

					for (var i = 0; i < di.length; i++) {
						
							di[i].di = i;
								 (function(j){
								  	gai[j].value = "请选择"; 
								 di[j].onchange = function(){
							gai[j].value = this.options[this.options.selectedIndex].value;
					
								 };	
								

							}(i));
							
					}
					}
</script>

<script type="text/javascript">
		var isCommitted = false;//表单是否已经提交标识，默认为false
		function dosubmit() {
			/* 此处还需要判断各个必填项校验是否通过*/
			//alert("1" + isCommitted);
			if (isCommitted == false
					&& document.getElementById('sfys').value != ""
					&& document.getElementById('time').value != "") {
				//提交表单后，将表单是否已经提交标识设置为true
				isCommitted = true;

			//获取表单提交按钮
		    var btnSubmit = document.getElementById("submit");
   			//将表单提交按钮设置为不可用，这样就可以避免用户再次点击提交按钮
   			btnSubmit.disabled= "disabled";
			return true;//返回true让表单正常提交
			} else {
				return false;//返回false那么表单将不提交
			}
		}
	</script>

</body>
</html>
