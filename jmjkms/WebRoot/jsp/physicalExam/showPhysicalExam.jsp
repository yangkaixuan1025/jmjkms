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
<link rel="stylesheet" href="/jmjkms/css/font-awesome-4.5.0/css/font-awesome.min.css">
<head><meta name="viewport" content="width=device-width, initial-scale=1.0"><meta http-equiv="X-UA-Compatible" content="ie=edge">
  <meta charset="UTF-8">
  <c:choose><c:when test="${session.comHospital.isHead==0}"><title>中电科社区健康管理平台</title></c:when><c:when test="${session.communityHospitalGroup!=null}"><title>社区健康管理系统</title></c:when><c:otherwise><title>社区健康服务站</title></c:otherwise></c:choose>
  <link rel="stylesheet" href="/jmjkms/css/cssreset.css">
  <link rel="stylesheet" href="/jmjkms/css/main.css">
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
      <h3 class="current-title">体检详情</h3>
      
      <input name="yemianname" value="03100" type="hidden"/> <!-- //权限隐藏框 -->
     
      
              <!-- 表单表格 start --> 
              <div class="table-content">
              <form action=""> 
                          <table class="table table-bordered" id="jqprint" style="word-break:break-all">
                           <tbody>
                                  <tr> 
                                        <td>档案编号</td> 
                                        <%-- <td>${VExam.fileNum}</td>  --%>
                                        <td>${jiGouFileNum}</td>
                                        <td>姓名</td> 
                                        <td>${jiGouname}</td> 
                                        <td>性别</td> 
                                        <c:if test="${jiGousex==true}">
										<td colspan="3">男</td>
							         	</c:if>
							        	 <c:if test="${jiGousex==false}">
										<td colspan="3">女</td>
							         	</c:if>
                                        
                                   </tr>

                                   <tr>
                                        <td>体检类型：</td> 
                                         <td colspan="3"> ${VExam.physicalExamType}</td>
                                         <td>体检日期:</td> 
                                        <td colspan="3"> <fmt:formatDate value="${VExam.physicalExamDate}" pattern="yyyy-MM-dd"/>
                                         </td>
                                   </tr>
                                   <tr>
                                        <td>责任医生:</td> 
                                         <td colspan="3"> ${VExam.responsibleDoctor}</td>
                                         <td>录入员:</td>
                                         <td colspan="3"> ${VExam.operator}</td>
                                   </tr>
                                   <tr>
                                     <td colspan="8" class="title-td">症状、一般状况</td>
                                   </tr>
                                   <tr>
                                     <td>症状特点</td>  
                                     <td>
                                     ${VExam.characteristicSymptom}
                                     </td>
                                     
                                     <td>体温</td> 
                                      <td>
                                      ${VExam.temperature} ℃</td>
                                       <td>呼吸频率</td>
                                       <td>
                                       ${VExam.breathingRate} 次/分</td> 
                                    
                                     <td>舒张压</td> 
                                      <td>
                                      ${VExam.diastolicPressure} mmhg</td>
                                   </tr>
                                   <tr>
                                      <td>收缩压</td> 
                                      <td>
                                      ${VExam.systolicPressure}  mmhg</td>
                                       <td>脉搏</td>
                                       <td>${VExam.pulseRate} 次/分</td> 
                                        <td>身高</td>
                                       <td>${VExam.height} cm</td> 
                                        <td>体重</td>
                                       <td>${VExam.weight} kg</td> 
                                   </tr>
                                   <tr>
                                    <td>体质指数</td>
                                       <td id="tizhi"></td>
                                       <td>腰围</td>
                                       <td>${VExam.waistline} cm</td> 
                                        <td>臀围</td>
                                        <td>${VExam.hip} cm</td> 
                                        <td>腰臀围比例</td>
                                        <td id="ytbi"></td> 
                                   </tr>
                                  <tr>
                                     <td>老人健康状况自我评估</td>
                                       <td colspan="3">
                                       ${VExam.healthAssessment}
                                       </td>
                                       <td>老人生活自理能力自我评估</td>
                                       <td colspan="3"> ${VExam.selfAssessment}
                                       </td>
                                  </tr>
                                   <tr>
                                     <td>老年人认知功能</td>
                                       <td colspan="3">
                                       ${VExam.cognitiveFunction}

                                        </td>
                                       <td>老年人情感状况</td>
                                       <td colspan="3">
                                       ${VExam.emotion}
                                       </td>
                                   </tr>
                                   <tr>
                                     <td colspan="8" class="title-td">生活方式</td>
                                   </tr>
                                   <tr>
                                     <td>体育锻炼频率</td>
                                       <td>${VExam.exerciseFrequency}
                                       </td>
                                       <td>每次锻炼时间</td>
                                       <td> ${VExam.exerciseTime} min</td> 
                                       <td>坚持锻炼时间</td>
                                       <td>${VExam.allExerciseTime} min</td> 
                                       <td>锻炼方式</td>
                                       <td>
                                       ${VExam.exerciseType}
                                       </td> 
                                   </tr>
                                   <tr>
                                     <td>饮食习惯</td>
                                       <td> 
                                       ${VExam.dietaryHabit}
                                       </td> 
                                       <td>吸烟状况</td>
                                       <td>
                                       ${VExam.smokingStatus}
                                       </td>
                                       <td>日饮酒量平均</td>
                                       <td>${VExam.smokingOneday} 两</td> 
                                       <td>开始吸烟年龄</td>
                                       <td>${VExam.startsmokingAge} 岁</td>  
                                   </tr>
                                   <tr>
                                       <td>戒烟年龄</td>
                                       <td>${VExam.giveupSmokingAge} 岁</td>
                                        <td>饮酒频率</td>
                                       <td>${VExam.drinkingFrequency}
                                       </td>
                                       <td>日饮酒量</td>
                                       <td>${VExam.monthlyDrinking} 两</td>
                                        <td>是否戒酒</td>
                                       <td>
                                       ${VExam.abstinence}

                                       </td>
                                   </tr>
                                   <tr>
                                     <td>开始饮酒年龄</td>
                                       <td>${VExam.startDrinkingAge} 岁</td>
                                        <td>近一年内是否曾醉酒</td>
                                       <td colspan="2">
                                       <c:if test="${VExam.recentlyYearDrinking==true}"><c:out value="是"></c:out> </c:if>
									  <c:if test="${VExam.recentlyYearDrinking==false}"><c:out value="否"></c:out></c:if>
                                       <c:if test="${VExam.recentlyYearDrinking==null}"></c:if>
                                       </td>
                                        <td>饮酒种类</td>
                                      <td> ${VExam.drinkingType}
                                       </td>
                                   </tr>
                                   
                                   <tr>
                                        <td>职业病危害因素接触史</td>
                                       <td colspan="2">
                                       ${VExam.occupationalDisease}
                                       </td>
                                       <td>毒物种类</td>
                                       <td>
                                       ${VExam.dust}

                                       </td>
                                        <td>防护措施</td>
                                       <td colspan="2">
                                       ${VExam.dustFence}
                                       </td>
                                   </tr>
                                   <tr>
                                       <td>放射物质</td>
                                       <td>
                                       ${VExam.radiogen} 
                                       
                                       </td>
                                        <td>防护措施</td>
                                       <td>
                                       ${VExam.radiogenFence}
                                       </td>
                                       <td>物理因素</td>
                                       <td>${VExam.physicalFactor} </td>
                                        <td>防护措施</td>
                                       <td>
                                       ${VExam.physicalFactorFence}
                                       </td>
                                   </tr>
                                   <tr>
                                       <td>化学物质</td>
                                       <td>${VExam.chemicalSubstances}</td>
                                        <td>防护措施</td>
                                       <td> 
                                       ${VExam.chSubFence}
                                       </td>
                                       <td>其他</td>
                                       <td>
                                       ${VExam.other}
                                       </td>
                                        <td>防护措施</td>
                                       <td> ${VExam.otherFence}
                                       </td>
                                   </tr>
                                   <tr>
                                     <td colspan="8" class="title-td">脏器功能</td>
                                   </tr>
                                   <tr>
                                     <td>口腔口齿</td>
                                       <td> ${VExam.oralLip}
                                       </td>
                                       <td>齿列</td>
                                       <td> ${VExam.dentition}
                                       </td>
                                       <td>咽部</td>
                                       <td colspan="3" >${VExam.pharynx}
                                       </td>
                                   </tr>
                                   <tr>
                                      <td>视力左眼</td>
                                       <td>${VExam.visionLeftEye}</td>
                                        <td>视力右眼</td>
                                       <td>${VExam.visionRightEye}</td>
                                        <td>矫正视力(左眼)</td>
                                       <td>${VExam.correctedVisionLeftEye}</td>
                                        <td>矫正视力(右眼)</td>
                                       <td>${VExam.correctedVisionRightEye}</td>
                                   </tr>
                                    <tr>
                                     <td>听力</td>
                                       <td colspan="3">
                                       ${VExam.hearing}
                                       </td>
                                       <td>运动功能</td>
                                       <td colspan="3">
                                       ${VExam.motorFunction}
                                       </td>
                                   </tr>
                                   <tr>
                                     <td colspan="8" class="title-td">查体</td>
                                   </tr>
                                   <tr>
                                       <td>皮肤</td>
                                       <td> 
                                       ${VExam.skin}
                                       </td>
                                       <td>巩膜</td>
                                       <td> 
                                       ${VExam.sclera}
                                       </td>
                                       <td>淋巴结</td>
                                       <td > 
                                        ${VExam.lymphNode}
                                       </td>
                                       <td>肺：桶状胸</td>
                                       <td>
                                       ${VExam.barrelChest}
                                       </td>
                                    </tr>
                                    <tr>
                                       <td>呼吸音</td>
                                       <td> 
                                      ${VExam.breathSounds}
                                      </td>
                                        <td>罗音</td>
                                       <td> 
                                       ${VExam.rale}
                                       </td>
                                       <td>心率</td>
                                       <td> 
                                       ${VExam.heartRate} 次/分</td>
                                        <td>心律</td>
                                       <td> 
                                       ${VExam.rhythmOfTheHeart}
                                       </td>
                                    </tr>
                                    <tr>
                                       <td>心脏杂音</td>
                                       <td> 
                                       ${VExam.noise}
                                       </td>
                                        <td>腹部：压痛</td>
                                       <td> 
                                       ${VExam.tenderness}
                                       </td>
                                        <td>包块</td>
                                       <td> 
                                       ${VExam.masses}
                                       </td>
                                        <td>肝大</td>
                                       <td> 
                                       ${VExam.hepatomegaly}
                                      
                                       </td>
                                    </tr>
                                     <tr>
                                       <td>脾大</td>
                                       <td> 
                                       ${VExam.splenomegaly}

                                       </td>
                                        <td>移动性浊音</td>
                                       <td> 
                                       ${VExam.shiftingDullness}
                                       </td>
                                        <td>下肢水肿</td>
                                       <td> 
                                       ${VExam.lowerExtremityEdema}
                                       </td>
                                        <td>足背动脉搏动</td>
                                       <td> 
                                       ${VExam.dorsalisPedisPulse}
                                       </td>
                                    </tr>
                                    <tr>
                                       <td>肛门指诊</td>
                                       <td> 
                                       ${VExam.theAnusDre}
                                       </td>
                                        <td>眼底</td>
                                       <td> 
                                      ${VExam.fundus}
                                      </td>
                                        <td>乳腺</td>
                                       <td> 
                                       ${VExam.mammaryGland}
                                       </td>
                                        <td>妇科外阴</td>
                                       <td> 
                                       ${VExam.vulva}
                                       </td>
                                    </tr>
                                    
                                     <tr>
                                       <td>阴道</td>
                                       <td> 
                                       ${VExam.vagina}
                                       </td>
                                        <td>宫颈</td>
                                       <td> 
                                      ${VExam.cervix}
                                       </td>
                                        <td>宫体</td>
                                       <td> 
                                       ${VExam.corpus}
                                       </td>
                                        <td>附件</td>
                                       <td> 
                                       ${VExam.attachment}
                                       </td>
                                    </tr>
                                    <tr>
                                       <td>其他</td>
                                       <td colspan="7"> ${VExam.others} </td>
                                    </tr>
                                     <tr>
                                     <td colspan="8" class="title-td">辅助检查</td>
                                   </tr>
                                   <tr>
                                     <td>空腹血糖</td>
                                       <td> ${VExam.fbg} mmol/L</td>
                                       <td>糖化血红蛋白</td>
                                       <td> 
                                       ${VExam.glycatedHemoglobin} %</td>
                                       <td>乙型肝炎表面抗原</td>
                                       <td colspan="3"> 
                                       ${VExam.hbsag}
                                       </td>
                                   </tr>
                                   <tr>
                                     <td>血常规</td>
                                       <td colspan="7"> </td>
                                   </tr>
                                   <tr>
                                       <td>血红蛋白</td>
                                       <td> ${VExam.hemoglobin} g/L</td>
                                       <td>白细胞</td>
                                       <td> ${VExam.leukocyte} g/L</td>
                                       <td>血小板</td>
                                       <td> ${VExam.platelet} g/L</td>
                                   </tr>
                                   <tr>
                                     <td>其他</td>
                                       <td colspan="7"> ${VExam.auxiliaryExaminationOthersRb}</td>
                                   </tr>

                                    <tr>
                                     <td>尿常规</td>
                                       <td colspan="7"> 
                                       </td>
                                   </tr>
                                   <tr>
                                       <td>尿蛋白</td>
                                       <td> ${VExam.urineProtein}</td>
                                       <td>尿糖</td>
                                       <td> ${VExam.urineGlucose}</td>
                                       <td>尿酮体</td>
                                       <td> ${VExam.ketone}</td>
                                       <td>尿潜血</td>
                                       <td> ${VExam.ery} mg/dL</td>
                                   </tr>

                                    <tr>
                                     <td>其他</td>
                                        <td>${VExam.othersRu} </td>
                                   </tr>
                                   <tr>
                                   <td>尿微量白蛋白</td>
                                       <td colspan="3"> ${VExam.mau} mg/dL</td>
                                   
                                     <td>大便潜血</td>
                                       <td colspan="3"> 
                                       <c:if test="${ VExam.sedOccultBlood==false }"> 无</c:if>
                                       <c:if test="${ VExam.sedOccultBlood!=false }"> 有</c:if>
                                       </td> 
                                 </tr>
                                 <tr>
                                     <td> 肝功能</td>
                                       <td colspan="7"> </td>
                                   </tr>
                                   <tr>
                                     <td>血清谷丙转氨酸</td>
                                       <td> ${VExam.serumAlanineAminoAcid} U/L</td>
                                       <td>血清谷草转氨酸</td>
                                       <td> ${VExam.serumAspertateAminoAcid} U/L</td>
                                       <td>白蛋白</td> 
                                       <td>${VExam.albumin} g/L</td>
                                       <td>总胆红素</td>
                                       <td>${VExam.tbil} umol/L</td>
                                   </tr>
                                   <tr>
                                      <td>结合胆红素</td>
                                       <td>${VExam.dbil} umol/L</td>
                                   </tr>
                                   <tr>
                                   <td>肾功能</td>
                                       <td colspan="7"></td>
                                   </tr>
                                   <tr>
                                     <td>血清肌酐</td>
                                       <td>${VExam.cr} mmol/L</td>
                                       <td>血尿素氨</td>
                                       <td>${VExam.bun} mmol/L</td>
                                       <td>血钾浓度</td>
                                       <td>
                                       ${VExam.serumPotassiumLevel} mmol/L</td>
                                       <td>血钠浓度</td>
                                       <td>
                                       ${VExam.serumNatriumLevel} mmol/L</td>
                                   </tr>
                                   
                                    <tr>
                                   <td>血脂</td>
                                       <td colspan="7"> </td>
                                   </tr>
                                   <tr>
                                       <td>总胆固醇</td>
                                       <td>
                                       ${VExam.totalCholesterol} mmol/L</td>
                                       <td>血清低密度蛋白胆固醇</td>
                                       <td>
                                       ${VExam.ldlC} mmol/L</td>
                                       <td>甘油三酯</td>
                                       <td>
                                       ${VExam.triglyceride}
                                       mmol/L</td>
                                       <td>血清高密度脂蛋白胆固醇</td>
                                       <td>
                                       ${VExam.hdlC}
                                       mmol/L</td>
                                   </tr>
                                   <tr>
                                     <td>宫颈涂片</td>
                                       <td>
                                       ${VExam.papSmear}
                                       </td>
                                       <td>心电图</td>
                                       <td>
                                       ${VExam.ecg}
                                       </td>
                                       <td>胸部X线片</td>
                                       <td>
                                       ${VExam.chestXRay}
                                       </td>
                                       <td>B超</td>
                                       <td>
                                       ${VExam.BScanUltrasonography}
                                       </td>
                                   </tr>
                                
                                    <tr>
                                   <td>其他</td>
                                       <td colspan="7">${VExam.auxiliaryExaminationOthers}</td>
                                   </tr>
                                     <tr>
                                     <td colspan="8" class="title-td">中医体辨识</td>
                                   </tr>
                                  
                                   <tr>
                                     <td>1、平和质</td>
                                       <td colspan="3"> 
                                       ${VExam.deficiency}
                                       </td>
                                       <td>指导意见</td>
                                       <td colspan="3"> 
                                       ${VExam.deficiencyGuidance}

                                       </td>
                                   </tr>
                                   <tr>
                                     <td>2、气虚质</td>
                                       <td colspan="3"> 
                                       ${VExam.qiDeficiency}
                                       </td>
                                       <td>指导意见</td>
                                       <td colspan="3"> 
                                       ${VExam.qiDeficiencyGuidance}
                                       </td>
                                   </tr>
                                   <tr>
                                     <td>3、阳虚质</td>
                                       <td colspan="3"> 
                                       ${VExam.yangDeficiency}
                                       </td>
                                       <td>指导意见</td>
                                       <td colspan="3"> 
                                       ${VExam.yangDeficiencyGuidance}
                                       </td>
                                   </tr>
                                   <tr>
                                     <td>4、阴虚质</td>
                                       <td colspan="3">
                                       ${VExam.yingDecifiency}
                                       </td>
                                       <td>指导意见</td>
                                       <td colspan="3">
                                       ${VExam.yingDecifiencyGuidance}
                                       </td>
                                   </tr>
                                   <tr>
                                     <td>5、痰湿质</td>
                                       <td colspan="3"> 
                                       ${VExam.phlegmDampness}
                                       </td>
                                       <td>指导意见</td>
                                       <td colspan="3"> 
                                       ${VExam.phlegmDampnessGuidance}
                                       </td>
                                   </tr>
                                   <tr>
                                     <td>6、湿热质</td>
                                       <td colspan="3"> 
                                       ${VExam.dampHeatConstitution}
                                       </td>
                                       <td>指导意见</td>
                                       <td colspan="3"> 
                                       ${VExam.dampHeatConstitutionGuidance}
                                       </td>
                                   </tr>
                                   <tr>
                                     <td>7、血瘀质</td>
                                       <td colspan="3"> 
                                       ${VExam.bloodySputum}
                                       </td>
                                       <td>指导意见</td>
                                       <td colspan="3"> 
                                       ${VExam.bloodySputumGuidance}
                                       </td>
                                   </tr>
                                   <tr>
                                     <td>8、气郁质</td>
                                       <td colspan="3"> 
                                       ${VExam.qiStagnation}
                                       </td>
                                       <td>指导意见</td>
                                       <td colspan="3"> 
                                       ${VExam.qiStagnationGuidance}
                                       </td>
                                   </tr>
                                    <tr>
                                     <td>9、特禀质</td>
                                       <td colspan="3"> 
                                       ${VExam.grasp}
                                       </td>
                                       <td>指导意见</td>
                                       <td colspan="3">
                                       ${VExam.graspGuidance}
                                       </td>
                                   </tr>
                                   <tr>
                                       <td colspan="8" class="title-td">现存主要健康问题</td>
                                       </tr>
                                       <tr>
                                       <td>脑血管疾病</td>
                                       <td colspan="3"> 
                                       ${VExam.cerebrovascularDisease}
                                       </td>
                                       <td>眼部疾病</td>
                                       <td colspan="3"> 
                                       ${VExam.ophthalmicDiseases}
                                       </td>
                                   </tr>
                                    <tr>
                                         <td>肾脏疾病</td>
                                       <td colspan="3"> 
                                       ${VExam.kidneyDisease}
                                       </td>
                                       <td>神经系统疾病</td>
                                       <td colspan="3"> 
                                       ${VExam.nervousSystemDisease}
                                       </td>
                                   </tr>
                                    <tr>
                                         <td>心脏疾病</td>
                                       <td colspan="3"> 
                                       ${VExam.heartDiseases}
                                       </td>
                                       <td>其他系统疾病</td>
                                       <td colspan="3"> 
                                       ${VExam.otherNeurologicalDisorders}
                                       </td>
                                   </tr>
                                   <tr>
                                       <td colspan="8" class="title-td">住院治疗室史</td>
                                       </tr>
                                       <tr>
                                         <td>住院史</td>
                                         <td colspan="7"> </td>
                                       </tr>
                                             <tr>
                                        <td>入院时间</td>
                                        <td>出院时间</td>
                                        <td>原因</td>
                                        <td>医疗机构名称</td>
                                        <td>病案号</td>
                                        <td colspan="3"><!-- 操作 --></td>
                                      </tr>
                                      
                                     <s:iterator value="listTHospitalHistory" var="thh">
                                        <tr class="dyna-form">
                                     	<td> 
                                     	 <fmt:formatDate value="${thh.admitHosTime}" pattern="yyyy-MM-dd"/>
                                     	</td>
                                        <td>
                                        <fmt:formatDate value="${thh.leaveHosTime}" pattern="yyyy-MM-dd"/>
                                        </td>
                                        <td>
                                        ${thh.reason}
                                        </td>
                                        <td>${thh.medicalInstitution}
                                      </td>
                                        <td>${thh.medicalRecordNum}
                                     </td>
                                        <td colspan="3">
                                        </td>
                                      </tr>
                                      </s:iterator>
                                      


                                      <tr>
                                         <td>家庭病床史</td>
                                         <td colspan="7"> </td>
                                       </tr>

                                        <tr>
                                        <td>建床日期</td>
                                        <td>撤床日期</td>
                                        <td>原因</td>
                                        <td>医疗机构名称</td>
                                        <td>病案号</td>
                                        <td colspan="3"><!-- 操作 --></td>
                                      </tr>
                                      <s:iterator value="listHF" var="hf">
                                      <tr class="dyna-form">
                                        <td>
                                        <fmt:formatDate value="${hf.createBedTime}" pattern="yyyy-MM-dd"/>
                                        </td>
                                        <td>
                                        <fmt:formatDate value="${hf.revokeBedTime}" pattern="yyyy-MM-dd"/>
                                        </td>
                                        <td>${hf.reason}
                                        </td>
                                        <td>${hf.medicalInstitution}
                                        </td>
                                        <td>${hf.medicalRecordNum}
                                        </td>
                                        <td colspan="3">
                                        </td>
                                      </tr>
                                      </s:iterator>



                                   <tr>
                                        <td colspan="8" class="title-td">住院用药情况</td>
                                       </tr>
                                       <tr>
                                        <td>药物名称</td>
                                        <td>用法</td>
                                        <td>用量</td>
                                        <td>用药时间</td>
                                        <td>服药依从性</td>
                                        <td colspan="3"><!-- 操作 --></td>
                                      </tr>
                                      <s:iterator value="listTMedicalUse" var="mu">
                                      <tr class="dyna-form">
                                        <td>${mu.medicalName}
                                        </td>
                                        <td>${mu.useMethod}
                                        </td>
                                        <td>${mu.useAccount}
                                 	   </td>
                                        <td><fmt:formatDate value="${mu.useTime}" pattern="yyyy-MM-dd"/>
                                        
                                   </td>
                                        <td>${mu.medicalComl}
                                  </td>
                                        <td colspan="3">
                                        </td>
                                      </tr>
                                      </s:iterator>
                                      <tr>
                                         <td colspan="8" class="title-td">非免疫规划预防接种史</td>
                                       </tr>
                                       <tr>
                                        <td>疫苗名称</td>
                                        <td>接种日期</td>
                                        <td>接种机构</td>
                                       <!--  <td colspan="5">操作</td> -->
                                      </tr>
                                      <s:iterator value="listTDefendInocu" var="di">
                                       <tr class="dyna-form">
                                        <td>${di.vaccineName}
                                       <!--  <input name="listTDefendInocu[0].vaccineName" type="text" class="w40"> -->
                                        </td>
                                        <td><fmt:formatDate value="${di.vaccineTime}" pattern="yyyy-MM-dd"/>
<!--                                         <input type="text" name="listTDefendInocu[0].vaccineTime" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})">
 -->                                        </td>
                                        <td>${di.vaccineInstit}
<!--                                         <input name="listTDefendInocu[0].vaccineInstit" type="text" class="w40">
 -->                                        </td>
                                        <td colspan="5">
                                        	<!-- <a href="javascript:;" class="btn-del">删除</a>
                                        	<a href="javascript:;" class="btn-add">增加</a> -->
                                        </td>
                                      </tr>
                                        </s:iterator>
                                        
                                        <tr>
                                        <td>
                                          <label>健康评价：</label>
                                        </td>
                                        <td>
                                           ${VExam.TPhysicalHealthAssessment}
                                        </td>
                                        <td>
                                         <label>危险因素控制：</label>
                                        </td>
                                        <td>
                                             ${VExam.riskFactorControl}
                                        </td>
                                        <td>
                                          <label>健康指导：</label>
                                        </td>
                                        <td colspan="3">
                                           ${VExam.healthGuide}
                                        </td>
                                  </tr>

                             </tbody> 
                             </table>
                          <div class="btn-content">
                          	<c:if test="${VExam.belongSystem==1}">
                                   <a href="PhysicalExamAction!jumpEditPhysicalExam.action?physicalExamId=${VExam.physicalExamId}" class="btn btn-xiugai">编辑</a>
							</c:if>
                                 <a href="javascript:;" class="btn btn-print btn-dayin">打印</a>

                                   <a href="javascript:;" class="btn" onclick="history.go(-1);">返回</a>
                            </div>
                       </form>
            </div> <!-- 表单表格 start -->
      


    </div>
    <!-- 底栏 Start-->
    <s:include value="/include/footer.jsp" />
    <!-- 底栏 Start-->
  </div>

<script src="/jmjkms/js/jquery-1.4.4.min.js"></script>
<script src="/jmjkms/js/laydate/laydate.js"></script>
<script src="/jmjkms/js/base.js"></script>
	<script src="/jmjkms/js/jquery.jqprint-0.3.js"></script>
<%-- ${VExam.waistline}/${VExam.hip}
${VExam.weight}/(${VExam.height}*${VExam.height}/10000) --%>
<script>
	var yt=(Number(${VExam.waistline}/${VExam.hip}));
	var tizhi=(Number(${VExam.weight}/(${VExam.height}*${VExam.height}/10000)));
	$("#ytbi").html((Number(yt).toFixed(2)));
	$("#tizhi").html((Number(tizhi).toFixed(2)));
</script>
</body>
</html>
