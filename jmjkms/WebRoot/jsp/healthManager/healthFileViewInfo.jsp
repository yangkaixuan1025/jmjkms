<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head><meta name="viewport" content="width=device-width, initial-scale=1.0"><meta http-equiv="X-UA-Compatible" content="ie=edge">
<meta charset="UTF-8">
<c:choose><c:when test="${session.comHospital.isHead==0}"><title>中电科社区健康管理平台</title></c:when><c:when test="${session.communityHospitalGroup!=null}"><title>社区健康管理系统</title></c:when><c:otherwise><title>社区健康服务站</title></c:otherwise></c:choose>
<link rel="stylesheet" href="/jmjkms/css/cssreset.css">
<link rel="stylesheet" href="/jmjkms/css/main.css">
<link rel="stylesheet"
	href="/jmjkms/css/font-awesome-4.5.0/css/font-awesome.min.css">

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


			<h3 class="current-title">健康档案详情</h3>

			<!-- 结果容器 start -->
			<div class="table-content">
				<input name="yemianname" value="02202" type="hidden" />
				<form action="healthFileAction!updateHealthFile.action"
					method="post" enctype="multipart/form-data">
					<input type="hidden" name="healthFile.healthFileId" value="${healthFileCloudSingle.healthFileId}">
					<table class="table table-bordered" id="jqprint"
						style="word-break:break-all">
						<tbody>
							<tr>
								<td>身份证号</td>
								<td><c:out value="${healthFileCloudSingle.idNum}"></c:out></td>
								<td>居民档案编号<br></td>
								<td><c:out value="${healthFileCloudSingle.fileNum}"></c:out>
							</tr>
							<tr>
								<td>姓名</td>
								<td><c:out value="${healthFileCloudSingle.name}"></c:out>
								<td>本人电话</td>
								<td><c:out value="${healthFileCloudSingle.iPhone}"></c:out>
								<%-- <td>建档单位</td>
								<td><c:out value="${healthFileCloudSingle.hospital}"></c:out> --%>
							</tr>
							<tr>
								<td>责任医生</td>
								 <td><c:out value="${vstaffHosSu.name}"></c:out></td> 
								 <td>建档日期</td>
								 <td><c:out value="${healthFileCloudSingle.fileDate}"></c:out></td>
							 </tr>
							<tr>
								<td>乡镇（街道）名称</td>
								<td><c:out value="${healthFileCloudSingle.currentOffice}"></c:out></td>
								<td>建档人</td>
								<td><c:out value="${healthFileCloudSingle.filePerson}"></c:out></td>
							</tr>
							<tr>
								<td>村（居）委员名称</td>
								<td><c:out value="${healthFileCloudSingle.currentResidentCommittee}"></c:out>
								</td>
								<td>录入员</td>
								<td><c:out value="${healthFileCloudSingle.enterPeople}"></c:out></td>
							</tr>
							<tr>
								<td>户籍地址</td>
								<td colspan="3"><c:out value="${healthFileCloudSingle.otherProvince}"></c:out>
									<c:out value="${healthFileCloudSingle.otherCity}"></c:out> <c:out
										value="${healthFileCloudSingle.otherCounty}"></c:out> <c:out
										value="${healthFileCloudSingle.otherCommunity}"></c:out></td>
							</tr>


							<tr>
								<td>现住址</td>
								<td colspan="3"><c:out
										value="${healthFileCloudSingle.currentProvince}"></c:out> <c:out
										value="${healthFileCloudSingle.currentCity}"></c:out> <c:out
										value="${healthFileCloudSingle.currentCounty}"></c:out> <c:out
										value="${healthFileCloudSingle.currentCommunity}"></c:out></td>
							</tr>
							 <tr>
								<td>照片</td>
								<td colspan="3">
								
								<c:if test="${empty healthFileCloudSingle.pic}"><img id="second"
									src="/jmjkms/images/healthfile_default_pic.jpg" width="100px" height="100px"> <input
									type="file" name="upload" id="doc" style="display:none"
									onchange="javascript:setImagePreview();" /></c:if>
								<c:if test="${not empty healthFileCloudSingle.pic}">
								<img id="second"
									src="${healthFileCloudSingle.pic}" width="100px" height="100px"> <input
									type="file" name="upload" id="doc" style="display:none"
									onchange="javascript:setImagePreview();" />
									
									</c:if></td>
							</tr> 
							<tr>
								<td>性别</td>
								<td><c:if test="${healthFileCloudSingle.sex == false}">
										<c:out value="男"></c:out>
									</c:if> <c:if test="${healthFileCloudSingle.sex == true}">
										<c:out value="女"></c:out>
									</c:if></td>
								<td>出生日期</td>
								
								<td><c:out value="${healthFileCloudSingle.birthDate}"></c:out></td>
							</tr>
							<tr>
								<td>建档单位</td>
								<td><c:out value="${healthFileCloudSingle.hospital}"></c:out></td>
								<%-- <td>本人电话</td>
								<td><c:out value="${healthFileCloudSingle.IPhone}"></c:out> --%>
								<td>所在社区医院</td>
								<td><c:out value="${healthFileCloudSingle.hospital}"></c:out></td>
							</tr>
							<tr>
								<td>联系人姓名</td>
								<td><c:out value="${healthFileCloudSingle.otherName}"></c:out></td>
								<td>联系人电话</td>
								<td><c:out value="${healthFileCloudSingle.otherPhone}"></c:out></td>
							</tr>
							<tr>
								<td>常驻类型</td>
								<td><c:out value="${healthFileCloudSingle.permanentType}"></c:out></td>
								<td>民族</td>
								<td><c:out value="${healthFileCloudSingle.nation}"></c:out></td>
							</tr>
							<tr>
								<td>血型</td>
								<td><c:out value="${healthFileCloudSingle.bloodTypr}"></c:out></td>
								<td>RH阴性</td>
								<td><c:out value="${healthFileCloudSingle.rhNegative}"></c:out></td>
							</tr>
							<tr>
								<td>职业</td>
								<td colspan="3"><c:out value="${healthFileCloudSingle.job}"></c:out></td>
							</tr>
							<tr>
								<td>婚姻状况</td>
								<td><c:out value="${healthFileCloudSingle.maritalStatus}"></c:out></td>
								<td>文化程度</td>
								<td><c:out value="${healthFileCloudSingle.eduLevel}"></c:out></td>
							</tr>
							 <tr>
								<td rowspan="1">医疗付费方式</td>
								<td colspan="3"><s:iterator value="payMethodList"
										var="hh">
										<c:out value="${hh.name}"></c:out>
										<c:out value="${hh.cardNum}"></c:out>
										<br>
									</s:iterator></td>
							</tr>
							<tr>
								<td>药物过敏史</td>
								<td colspan="3"><c:out value="${choiceResultAllergyName}"></c:out>

								</td>
							</tr>
							<tr>
								<td>暴露史</td>
								<td colspan="3"><c:out value="${choiceResultExposeName}"></c:out>
									<c:out value="${healthFileCloudSingle.exposeNote}"></c:out></td>
							</tr>
							
							<tr>
								<td colspan="4" class="title-td">个人基本身体信息</td>
							</tr>
							<tr>
								<td>身高</td>
								<td><c:out value="${healthFileCloudSingle.height}cm"></c:out></td>
								<td>体重</td>
								<td><c:out value="${healthFileCloudSingle.weihth}kg"></c:out></td>
							</tr>
							<tr>
								<td>腰围</td>
								<td><c:out value="${healthFileCloudSingle.waistline}cm"></c:out></td>
								<td>臀围</td>
								<td><c:out value="${healthFileCloudSingle.hipline}cm"></c:out></td>
							</tr>
							<tr>
								<td>收缩压</td>
								<td><c:out value="${healthFileCloudSingle.systolicBloodPressure}mmgh"></c:out>
								</td>
								<td>舒张压</td>
								<td><c:out
										value="${healthFileCloudSingle.diastolicBloodPressure}mmgh"></c:out></td>
							</tr>
							<tr>
								<td>老人卡NFC编码</td>
								<td><c:out value="${healthFileCloudSingle.nfc}"></c:out>
								</td>
								<td></td>
								<td></td>
							</tr>

<tr>
                                     <td colspan="8" class="title-td">生活方式</td>
                                   </tr>
                                   <tr>
                                     <td>体育锻炼频率</td>
                                       <td>${lifestyle.exerciseFrequency}
                                       </td>
                                       <td>每次锻炼时间</td>
                                       <td> ${lifestyle.exerciseTime} min</td> 
                                        </tr>
                                   <tr>
                                       <td>坚持锻炼时间</td>
                                       <td>${lifestyle.allExerciseTime} min</td> 
                                       <td>锻炼方式</td>
                                       <td>
                                       ${lifestyle.exerciseType}
                                       </td> 
                                   </tr>
                                   <tr>
                                     <td>饮食习惯</td>
                                       <td> 
                                       ${lifestyle.dietaryHabit}
                                       </td> 
                                       <td>吸烟状况</td>
                                       <td>
                                       ${lifestyle.smokingStatus}
                                       </td>
                                        </tr>
                                   <tr>
                                       <td>日饮酒量平均</td>
                                       <td>${lifestyle.smokingOneday} </td> 
                                       <td>开始吸烟年龄</td>
                                       <td>${lifestyle.startsmokingAge} 岁</td>  
                                   </tr>
                                   <tr>
                                       <td>戒烟年龄</td>
                                       <td>${lifestyle.giveupSmokingAge} 岁</td>
                                        <td>饮酒频率</td>
                                       <td>${lifestyle.drinkingFrequency}
                                       </td>
                                        </tr>
                                   <tr>
                                       <td>日饮酒量</td>
                                       <td>${lifestyle.monthlyDrinking} </td>
                                        <td>是否戒酒</td>
                                       <td>
                                       ${lifestyle.abstinence}

                                       </td>
                                   </tr>
                                   <tr>
                                     <td>开始饮酒年龄</td>
                                       <td>${lifestyle.startDrinkingAge} 岁</td>
                                        <td>近一年内是否曾醉酒</td>
                                       <td colspan="1">
                                       <c:if test="${lifestyle.recentlyYearDrinking==true}"><c:out value="是"></c:out> </c:if>
									  <c:if test="${lifestyle.recentlyYearDrinking==false}"><c:out value="否"></c:out></c:if>
                                       <c:if test="${lifestyle.recentlyYearDrinking==null}"></c:if>
                                       </td>
                                        </tr>
                                   <tr>
                                        <td>饮酒种类</td>
                                      <td> ${lifestyle.drinkingType}
                                       </td>
                                 
                                        <td>职业病危害因素接触史</td>
                                       <td colspan="1">
                                       ${lifestyle.occupationalDisease}
                                       </td>
                                        </tr>
                                   <tr>
                                       <td>毒物种类</td>
                                       <td>
                                       ${lifestyle.dust}

                                       </td>
                                        <td>防护措施</td>
                                       <td colspan="1">
                                       ${lifestyle.dustFence}
                                       </td>
                                   </tr>
                                   <tr>
                                       <td>放射物质</td>
                                       <td>
                                       ${lifestyle.radiogen} 
                                       
                                       </td>
                                        <td>防护措施</td>
                                       <td>
                                       ${lifestyle.radiogenFence}
                                       </td>
                                        </tr>
                                   <tr>
                                       <td>物理因素</td>
                                       <td>${lifestyle.physicalFactor} </td>
                                        <td>防护措施</td>
                                       <td>
                                       ${lifestyle.physicalFactorFence}
                                       </td>
                                   </tr>
                                   <tr>
                                       <td>化学物质</td>
                                       <td>${lifestyle.chemicalSubstances}</td>
                                        <td>防护措施</td>
                                       <td> 
                                       ${lifestyle.chSubFence}
                                       </td>
                                        </tr>
                                   <tr>
                                       <td>其他</td>
                                       <td>
                                       ${lifestyle.other}
                                       </td>
                                        <td>防护措施</td>
                                       <td> ${lifestyle.otherFence}
                                       </td>
                                   </tr>
						 	<tr><td colspan="8" class="title-td">用药记录</td></tr>         
				             
				               <tr >
				                        <s:iterator value="MedicalRecordList"
										var="hh">
										<tr>
				                        <td>药物名称</td>
				                        <td>用法</td>
				                        <td>用量</td>
				                        <td>开始用药时间</td>
				             			</tr>
										<tr>
										<td><c:out value="${hh.drugName}"></c:out></td>
										<td><c:out value="${hh.usages}"></c:out></td>
										<td><c:out value="${hh.dosage}"></c:out></td>
										<td><c:out value="${hh.beginDate}"></c:out></td>
										</tr>
										<tr>
										<td>服药依从性</td>
				                        <td>不良反应</td>
				                        <td>药品来源</td>
				                        <td>操作</td>
				                        </tr>
				                        <tr>
										<td><c:out value="${hh.medicationAdherence}"></c:out></td>
										<td><c:out value="${hh.untowardEffect}"></c:out></td>
										<td><c:out value="${hh.drugSources}"></c:out></td>
										<td><c:out value="无"></c:out></td>
										</tr>
										</s:iterator>
				              </tr>          
				                    
							<tr>
								<td colspan="7" class="title-td">既往史</td>
							</tr>
							<!--  jjdjisdi -->
							<tr>
								<td rowspan="1">疾病</td>
								<td colspan="3"><s:iterator value="pastList"
										var="hh">
										<c:out value="${hh.pastName}"></c:out>
										<c:out value="${hh.pastDate}"></c:out>
										<br>
									</s:iterator></td>
							</tr>


							<tr>
								<td colspan="1">手术</td>
								<td colspan="3"><s:iterator value="pastShouShuList"
										var="hh">
										<c:out value="名称：${hh.pastName}"></c:out>
										<c:out value="时间：${hh.pastDate}"></c:out>
										<br>
									</s:iterator></td>
							</tr>

							<tr>
								<td colspan="1">外伤名称</td>

								<td colspan="3"><s:iterator value="pastWaiShangList"
										var="hh">
										<c:out value="名称：${hh.pastName}"></c:out>
										<c:out value="时间：${hh.pastDate}"></c:out>
										<br>
									</s:iterator></td>
							</tr>

							<tr>
								<td colspan="1">输血原因</td>

								<td colspan="3"><s:iterator value="pastShuXieList"
										var="hh">
										<c:out value="原因:${hh.pastName}"></c:out>
										<c:out value="时间:${hh.pastDate}"></c:out>
										<br>
									</s:iterator></td>
							</tr>

							<tr>
								<td colspan="4" class="title-td">家族史</td>
							</tr>
							<tr>
								<td>父亲</td>
								<td colspan="3"><c:out value="${choiceResultFather}"></c:out>
									<c:out value="${healthFileCloudSingle.one}"></c:out>
							</tr>
							<tr>
								<td>母亲</td>
								<td colspan="3"><c:out value="${choiceResultMother}"></c:out>
									<c:out value="${healthFileCloudSingle.two}"></c:out></td>
							</tr>
							<tr>
								<td>兄弟姐妹</td>
								<td colspan="3"><c:out value="${choiceResultBroSis}"></c:out>
									<c:out value="${healthFileCloudSingle.three}"></c:out></td>
							</tr>
							<tr>
								<td>子女</td>
								<td colspan="3"><c:out value="${choiceResultChild}"></c:out>
									<c:out value="${healthFileCloudSingle.four}"></c:out></td>
							</tr>
							<tr>
								<td>遗传病史</td>
								<c:out value="${healthFileCloudSingle.geneticHistory}"></c:out>

							</tr>
							<tr>
								<td>残疾情况</td>
								<td colspan="3"><c:out value="${choiceResultDisability}"></c:out>
									<c:out value="残疾证号:${healthFileCloudSingle.disabilitity}"></c:out></td>
							</tr>
							<tr>
								<td colspan="4" class="title-td">生活环境</td>
							</tr>
							<tr>
								<td>厨房排风措施</td>
								<td><c:out value="${healthFileCloudSingle.kitVenFac}"></c:out></td>
								<td>燃料类型</td>
								<td><c:out value="${healthFileCloudSingle.fuelType}"></c:out></td>
							</tr>
							<tr>
								<td>饮水</td>
								<td><c:out value="${healthFileCloudSingle.drinkWater}"></c:out></td>
								<td>厕所</td>
								<td><c:out value="${healthFileCloudSingle.toilet}"></c:out></td>
							</tr>
							<tr>
								<td>禽畜栏</td>
								<td colspan="3"><c:out value="${healthFileCloudSingle.livestockBar}"></c:out>

								</td>

							</tr>
							<tr>
								<td>人群分类</td>
								<td colspan="3"><c:if test="${healthFileCloudSingle.crowdClass eq 0}">
										<c:out value="健康人群"></c:out>
									</c:if> <c:if test="${healthFileCloudSingle.crowdClass eq 1}">
										<c:out value="慢病高危人群"></c:out>
									</c:if> <c:if test="${healthFileCloudSingle.crowdClass eq 2}">
										<c:out value="慢病患者人群"></c:out>
									</c:if></td>
							</tr> 
							
						</tbody>
					</table>
					<div class="btn-content">
						<a href="javascript:;" class="btn btn-print">打印</a> <a
							href="javascript:;" class="btn btn-back">返回</a>
					</div>
				</form>
			</div>

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
	<script type="text/javascript">
		//下面用于图片上传预览功能
		function setImagePreview(avalue) {
			var docObj = document.getElementById("doc");

			var imgObjPreview = document.getElementById("second");
			if (docObj.files && docObj.files[0]) {
				//火狐下，直接设img属性
				//imgObjPreview.style.display = 'block';
				imgObjPreview.style.width = '200px';
				imgObjPreview.style.height = '200px';
				//imgObjPreview.src = docObj.files[0].getAsDataURL();

				//火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式
				imgObjPreview.src = window.URL.createObjectURL(docObj.files[0]);
			} else {
				//IE下，使用滤镜
				docObj.select();
				var imgSrc = document.selection.createRange().text;
				var localImagId = document.getElementById("imgdiv");
				//必须设置初始大小
				localImagId.style.width = "200px";
				localImagId.style.height = "200px";
				//图片异常的捕捉，防止用户修改后缀来伪造图片
				try {
					localImagId.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
					localImagId.filters
							.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
				} catch (e) {
					alert("您上传的图片格式不正确，请重新选择!");
					return false;
				}
				imgObjPreview.style.display = 'none';
				document.selection.empty();
			}
			return true;
		}
	</script>
	<script type="text/javascript">
		window.onload = function() {

			var Oinput = document.getElementsByTagName("input");
			// alert(Oinput.length);
			for (var i = 0; i < Oinput.length; i++) {
				Oinput[i].disabled = true;
			}
			var Oselect = document.getElementsByTagName("select");
			for (var i = 0; i < Oselect.length; i++) {
				Oselect[i].disabled = true;
			}

			//疾病的回显

			$
					.ajax({
						type : "post",
						url : "healthFileAction!getDisease.action?healthFileId="
								+ ${healthFileCloudSingle.healthFileId},
						dataType : "json",
						success : function(data) {
							if (data.length == 0) {
								return;
							}
							for (var i = 0; i < data.length; ++i) {
								if (document.getElementById("wwwww").innerHTML == data[i].pastHistoryName) {
									document.getElementById("no01").checked = true;
									//////////
									for (var i = 0; i < document
											.getElementsByClassName("not").length; i++) {
										document.getElementsByClassName("not").disabled = true;
										document
												.getElementsByClassName("nocheck")[i].disabled = true;
										document
												.getElementsByClassName("nocheck")[i].checked = false;
										//Onot[i].placeholder="请输入确诊时间";
										document.getElementsByClassName("not")[i].style.display = "none";
										document.getElementsByClassName("not")[i].value = "";
									}

									//////////	

								}
								if (document.getElementById("guanxinbingName").innerHTML == data[i].pastHistoryName) {
									$("#guanxinbingTime").val(
											data[i].pastHistoryTime);
									//让复选框选中
									document.getElementById("guanxinbing").checked = true;
								}
								if (document.getElementById("gaoxueyaName").innerHTML == data[i].pastHistoryName) {
									$("#gaoxueyaTime").val(
											data[i].pastHistoryTime);
									//让复选框选中
									document.getElementById("gaoxueya").checked = true;
								}
								if (document.getElementById("tangniaobingName").innerHTML == data[i].pastHistoryName) {
									$("#tangniaobingTime").val(
											data[i].pastHistoryTime);
									//让复选框选中
									document.getElementById("tangniaobing").checked = true;
								}
								if (document.getElementById("mxzsxfjbName").innerHTML == data[i].pastHistoryName) {
									$("#mxzsxfjbTime").val(
											data[i].pastHistoryTime);
									//让复选框选中
									document.getElementById("mxzsxfjb").checked = true;
								}
								if (document.getElementById("exzlName").innerHTML == data[i].pastHistoryName) {
									$("#exzlTime").val(data[i].pastHistoryTime);
									//让复选框选中
									document.getElementById("exzl").checked = true;
								}
								if (document.getElementById("naocuzhongName").innerHTML == data[i].pastHistoryName) {
									$("#naocuzhongTime").val(
											data[i].pastHistoryTime);
									//让复选框选中
									document.getElementById("naocuzhong").checked = true;
								}
								if (document.getElementById("zxjsjbName").innerHTML == data[i].pastHistoryName) {
									$("#zxjsjbTime").val(
											data[i].pastHistoryTime);
									//让复选框选中
									document.getElementById("zxjsjb").checked = true;
								}
								if (document.getElementById("jiehebingName").innerHTML == data[i].pastHistoryName) {
									$("#jiehebingTime").val(
											data[i].pastHistoryTime);
									//让复选框选中
									document.getElementById("jiehebing").checked = true;
								}
								if (document.getElementById("zxjsjbName").innerHTML == data[i].pastHistoryName) {
									$("#zxjsjbTime").val(
											data[i].pastHistoryTime);
									//让复选框选中
									document.getElementById("zxjsjb").checked = true;
								}
								if (document.getElementById("ganyanName").innerHTML == data[i].pastHistoryName) {
									$("#ganyanTime").val(
											data[i].pastHistoryTime);
									//让复选框选中
									document.getElementById("ganyan").checked = true;
								}
								if (document.getElementById("qtfdcrbName").innerHTML == data[i].pastHistoryName) {
									$("#qtfdcrbTime").val(
											data[i].pastHistoryTime);
									//让复选框选中
									document.getElementById("qtfdcrb").checked = true;
								}
								if (document.getElementById("zhiyebingName").innerHTML == data[i].pastHistoryName) {
									$("#zhiyebingTime").val(
											data[i].pastHistoryTime);
									//让复选框选中
									document.getElementById("zhiyebing").checked = true;
								}
								if (document.getElementById("qita-02Name").innerHTML == data[i].pastHistoryName) {
									$("#qita-02Time").val(
											data[i].pastHistoryTime);
									//让复选框选中
									document.getElementById("qita-02").checked = true;
								}

							}

						}
					});

		}
	</script>
	<script type="text/javascript">
		//外伤、手术
		window.onload = function() {
			//让所在社区医院的输入框为不可修改
			var Oinput = document.getElementsByClassName("NoAlter");
			// alert(Oinput.length);
			for (var i = 0; i < Oinput.length; i++) {
				Oinput[i].disabled = true;
			}
		}
	</script>
</body>
</html>

