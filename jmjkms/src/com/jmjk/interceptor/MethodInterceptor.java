package com.jmjk.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jmjk.entity.TCommunityHospital;
import com.jmjk.entity.TCommunityHospitalGroup;
import com.jmjk.entity.TStaff;
import com.jmjk.enums.Admin_IsHead;
import com.jmjk.enums.RightsValue;
import com.jmjk.iservice.IRightsService;
import com.jmjk.utils.WebXMLParameter;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 登录用户权限拦截器
 * 
 * @author lilicong
 *
 */
@Scope("prototype")
@Component
public class MethodInterceptor extends MethodFilterInterceptor {

	@Autowired
	IRightsService rightsService;

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {

		// 获取访问的action
		String action = invocation.getAction().getClass().getName();
		// 获取访问的method
		String method = invocation.getProxy().getMethod();
		// 获取session
		Map session = invocation.getInvocationContext().getSession();

		// 登录页不拦截,甲方访问接口不拦截
		if (action.contains("com.jmjk.action.LoginoutAction")
				|| action.contains("WebServicePort")) {
			return invocation.invoke();
		}
		if (session == null) {
			HttpServletRequest request = ServletActionContext.getRequest();
			String user = "";
			// 取cookie
			Cookie[] cookies = request.getCookies();// 读取Cookie,一般都会有多个
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					if (cookie.getName().equalsIgnoreCase("user")) {
						user = cookie.getValue(); // 得到cookie的用户名
					}
				}
			}
			if ("adminLogin".equals(user)) {
				return "adminLogin";
			}
			return "onLogin";
		}

		// 根据web.xml配置文件获取栏目编码,one_one代表一级第一个子菜单,根据页面依次类推
		// 档案管理:1、健康档案；2、慢病管理
		String one = WebXMLParameter.getColumnCod("one");
		String one_one = WebXMLParameter.getColumnCod("one_one");
		String one_two = WebXMLParameter.getColumnCod("one_two");

		// 健康管理：1、数据录入；2、日常监测；3、预警管理；4、血压管理；5、血糖管理；6、其他慢病；7、中医治未病
		String two = WebXMLParameter.getColumnCod("two");
		String two_one = WebXMLParameter.getColumnCod("two_one");
		String two_two = WebXMLParameter.getColumnCod("two_two");
		String two_three = WebXMLParameter.getColumnCod("two_three");
		String two_four = WebXMLParameter.getColumnCod("two_four");
		String two_five = WebXMLParameter.getColumnCod("two_five");
		String two_six = WebXMLParameter.getColumnCod("two_six");
		String two_seven = WebXMLParameter.getColumnCod("two_seven");

		// 健康体检
		String three = WebXMLParameter.getColumnCod("three");

		// 健康理疗：1、康复方案；2、康复计划；3、统计分析
		String four = WebXMLParameter.getColumnCod("four");
		String four_one = WebXMLParameter.getColumnCod("four_one");
		String four_two = WebXMLParameter.getColumnCod("four_two");
		String four_three = WebXMLParameter.getColumnCod("four_three");

		// 健康关怀：1、健康教育；2、上门随访；3、知识讲座；4、社区义诊
		String five = WebXMLParameter.getColumnCod("five");
		String five_one = WebXMLParameter.getColumnCod("five_one");
		String five_two = WebXMLParameter.getColumnCod("five_two");
		String five_three = WebXMLParameter.getColumnCod("five_three");
		String five_four = WebXMLParameter.getColumnCod("five_four");

		// 门诊记录：1、随诊；2、接诊；3、会诊；4、转诊；5、用药记录；6、家庭病床;7、住院记录
		String six = WebXMLParameter.getColumnCod("six");
		String six_one = WebXMLParameter.getColumnCod("six_one");
		String six_two = WebXMLParameter.getColumnCod("six_two");
		String six_three = WebXMLParameter.getColumnCod("six_three");
		String six_four = WebXMLParameter.getColumnCod("six_four");
		String six_five = WebXMLParameter.getColumnCod("six_five");
		String six_six = WebXMLParameter.getColumnCod("six_six");
		String six_seven = WebXMLParameter.getColumnCod("six_seven");

		// 员工管理：1、员工信息；2、员工请假；3、奖惩；4、操作员
		String seven = WebXMLParameter.getColumnCod("seven");
		String seven_one = WebXMLParameter.getColumnCod("seven_one");
		String seven_two = WebXMLParameter.getColumnCod("seven_two");
		String seven_three = WebXMLParameter.getColumnCod("seven_three");
		String seven_four = WebXMLParameter.getColumnCod("seven_four");

		// 统计分析：1、医院统计；2、设备统计；3、医院疾病
		String eight = WebXMLParameter.getColumnCod("eight");
		String eight_one = WebXMLParameter.getColumnCod("eight_one");
		String eight_two = WebXMLParameter.getColumnCod("eight_two");
		String eight_three = WebXMLParameter.getColumnCod("eight_three");

		// 设备管理
		String nine = WebXMLParameter.getColumnCod("nine");

		// 系统管理：1、医院管理；2、专家康复方案；3、数据字典；4、职务权限
		String ten = WebXMLParameter.getColumnCod("ten");
		String ten_one = WebXMLParameter.getColumnCod("ten_one");
		String ten_two = WebXMLParameter.getColumnCod("ten_two");
		String ten_three = WebXMLParameter.getColumnCod("ten_three");
		String ten_four = WebXMLParameter.getColumnCod("ten_four");
		String ten_five = WebXMLParameter.getColumnCod("ten_five");

		// 得到员工和社区负责人,集团负责人,平台admin对象
		TStaff tstaff = (TStaff) session.get("tstaff");
		TCommunityHospital comHospital = (TCommunityHospital) session
				.get("comHospital");
		TCommunityHospitalGroup group = (TCommunityHospitalGroup) session
				.get("communityHospitalGroup");

		// 登录用户为社区负责人或者平台admin/////////////////////////////////////////////////////////////////////
		if (comHospital != null) {

			// 社区负责人开始,拦截不能访问的action或方法
			if (comHospital.getIsHead() == Admin_IsHead.shequfuzeren.getValue()) {

				// 社区负责人权限判断,无需判断角色和权限,拦截一些action方法即可
				List<String> comHosAdminNOPowerAction = new ArrayList<String>();
				// 此处写社区负责人不能访问的action
				comHosAdminNOPowerAction
					    .add("com.jmjk.action.CommunityHospitalAction");
				comHosAdminNOPowerAction
                        .add("com.jmjk.action.CommunityHospitalGroupAction");
				comHosAdminNOPowerAction
						.add("com.jmjk.action.MaintainaleAction");
				comHosAdminNOPowerAction
						.add("com.jmjk.action.MaintainableEntryAction");

				// 社区负责人不能访问的method
				List<String> comHosAdminNOPowerMethod = new ArrayList<String>();
				comHosAdminNOPowerMethod.add("jumpAddExpertScheme");
				comHosAdminNOPowerMethod.add("expertSchemeDetails");
				comHosAdminNOPowerMethod.add("deleteExpertSchemeById");
				comHosAdminNOPowerMethod.add("jumpEditExpertScheme");
				comHosAdminNOPowerMethod.add("editExpertRecoveryScheme");
				comHosAdminNOPowerMethod.add("getExpertScheme");
				comHosAdminNOPowerMethod.add("addExpertScheme");
				// 如果社区负责人访问非法action,method,error
				if (comHosAdminNOPowerAction.contains(action)
						|| comHosAdminNOPowerMethod.contains(method)) {
					return "error";
				} else {
					return invocation.invoke();
				}
			}// 社区负责人结束

			// //平台管理员开始,允许能够访问的action和方法
			if (comHospital.getIsHead() == Admin_IsHead.guanliyuan.getValue()) {
				// 管理员能访问Action,但是不能新增\删除\修改
				List<String> systemAdminActionList = new ArrayList<String>();
				// one_one模块 健康档案
				systemAdminActionList.add("com.jmjk.action.HealthFileAction");
				// one_two模块 慢病档案
				systemAdminActionList
						.add("com.jmjk.action.OldChronicFollowAction");
				systemAdminActionList
						.add("com.jmjk.action.ShowOldChronicAction");
				// two_one模块 数据采集
				systemAdminActionList
						.add("com.jmjk.action.HealthManagerAction");
				systemAdminActionList
				.add("com.jmjk.action.UrineAction");
				systemAdminActionList
				.add("com.jmjk.action.EcgAction");
				// two_two模块 日常监测
				systemAdminActionList
						.add("com.jmjk.action.HealthDailyCheckAction");
				systemAdminActionList
						.add("com.jmjk.action.HealthManagerAction");
				systemAdminActionList
				.add("com.jmjk.action.UrineAction");
				systemAdminActionList
				.add("com.jmjk.action.EcgAction");
				// two_three模块 健康预警
				systemAdminActionList.add("com.jmjk.action.HealthAlarmAction");

				// two_four/five/six模块 血压\血糖\其他慢病管理
				systemAdminActionList
						.add("com.jmjk.action.ChronicManagerAction");
				systemAdminActionList
						.add("com.jmjk.action.DoctorDiagnosisAction");
				//two_seven中医治未病
				systemAdminActionList
				.add("com.jmjk.action.XKHealthMachineAction");
				// three模块 健康体检
				systemAdminActionList.add("com.jmjk.action.PhysicalExamAction");
				// four_one/two/three模块 康复方案\计划\统计
				systemAdminActionList
						.add("com.jmjk.action.HealthPhysiotherapyAction");
				// five_one模块 健康教育
				systemAdminActionList
						.add("com.jmjk.action.HealthEducationAction");
				// five_two模块 上门随访
				systemAdminActionList.add("com.jmjk.action.DoorKnockingAction");
				// five_three模块 知识讲座
				systemAdminActionList
						.add("com.jmjk.action.KnowledgeLectureAction");
				// five_four模块 社区义诊
				systemAdminActionList
						.add("com.jmjk.action.ComminityClinicAction");
				// six_one模块 随诊记录
				systemAdminActionList.add("com.jmjk.action.FollowRecordAction");
				// six_two模块 接诊记录
				systemAdminActionList
						.add("com.jmjk.action.IncompleteRecordAction");
				systemAdminActionList
				.add("com.jmjk.action.IncompleteRecordAction2");
				// six_three模块 会诊
				systemAdminActionList
						.add("com.jmjk.action.ConsultationRecordAction");
				// six_four模块 转诊
				systemAdminActionList
						.add("com.jmjk.action.DualReferralRecordAction");
				// six_five模块 用药记录
				systemAdminActionList
						.add("com.jmjk.action.MedicalRecordAction");
				// six_six模块 家庭病床
				systemAdminActionList
						.add("com.jmjk.action.FamilyBedHistoryAction");
				// six_seven模块 住院记录
				systemAdminActionList
						.add("com.jmjk.action.InHospitalHistoryAction");
				// seven_one/two 员工信息\请假
				systemAdminActionList.add("com.jmjk.action.StaffAction");
				// seven_three 员工奖惩
				systemAdminActionList
						.add("com.jmjk.action.StaffRewardPunishAction");
				// seven_four 操作员管理
				systemAdminActionList.add("com.jmjk.action.OperatorAction");
				// eight 统计分析
				systemAdminActionList.add("com.jmjk.action.StatisticsAction");
				// nine 设备管理
				systemAdminActionList
						.add("com.jmjk.action.EquipmentManageAction");
				// ten 系统管理
				/*systemAdminActionList
						.add("com.jmjk.action.CommunityHospitalAction");*/
				systemAdminActionList.add("com.jmjk.action.MaintainableAction");
				systemAdminActionList.add("com.jmjk.action.MaintainableAction");

				systemAdminActionList
						.add("com.jmjk.action.MaintainableEntryAction");
				systemAdminActionList.add("com.jmjk.action.CommunityHospitalGroupAction");

				// 向平台管理员添加能访问的方法
				List<String> systemAdminNoMethodList = new ArrayList<String>();
				// one_one
				systemAdminNoMethodList.add("showHealthFile");
				systemAdminNoMethodList.add("turnToHealthFileView");
				systemAdminNoMethodList.add("getDisease");
				systemAdminNoMethodList.add("searchHealthFile");
				// one_two
				systemAdminNoMethodList.add("firstShowChronic");
				systemAdminNoMethodList.add("jumpShowChronic");
				systemAdminNoMethodList.add("showOneAllFollow");
				systemAdminNoMethodList.add("jumpShowFollow");
				systemAdminNoMethodList.add("searchChronicPeople");
				systemAdminNoMethodList.add("showSlowChronic");

				// two_one
				systemAdminNoMethodList.add("getHealthFileList");
				systemAdminNoMethodList.add("getHealthFileListBySearchBean");
				systemAdminNoMethodList.add("toUploadData");
				systemAdminNoMethodList.add("getBloodPressFromCloud");
				systemAdminNoMethodList.add("bloodPressDataAnalysis");
				systemAdminNoMethodList.add("bloodPressTendAnalysis");
				systemAdminNoMethodList.add("bloodPressDataDetail");
				systemAdminNoMethodList.add("searchBloodPressRecoverPlan");
				systemAdminNoMethodList.add("getDataFromCloud");
				
				systemAdminNoMethodList.add("getRecoverSchema");
				systemAdminNoMethodList.add("getBloodSugerFromCloud");
				systemAdminNoMethodList.add("bloodSugerDataAnalysis");
				systemAdminNoMethodList.add("bloodSugerTendAnalysis");
				systemAdminNoMethodList.add("bloodSugerDataDetail");
				systemAdminNoMethodList.add("searchBloodSugerRecoverPlan");
				systemAdminNoMethodList.add("getAllInOneData");
				systemAdminNoMethodList.add("getPhysicalExamByHealthFileId");
				systemAdminNoMethodList.add("getBloodOxygenFromCloud");
				systemAdminNoMethodList.add("bloodOxygenDataAnalysis");
				systemAdminNoMethodList.add("bloodOxygenTendAnalysis");
				systemAdminNoMethodList.add("getAllInOneFromCloud");
				systemAdminNoMethodList.add("bloodOxygenDataDetail");

				// 查询
				systemAdminNoMethodList.add("allInOneDataAnalysis");
				systemAdminNoMethodList.add("allInOneTendAnalysis");
				systemAdminNoMethodList.add("allInOneDataDetail");
				
				
				/*// two_two
				systemAdminNoMethodList.add("getHealthFileList");
				systemAdminNoMethodList.add("getHealthFileListBySearchBean");
				systemAdminNoMethodList.add("getBloodPressFromCloud");
				systemAdminNoMethodList.add("bloodPressDataAnalysis");
				systemAdminNoMethodList.add("bloodPressTendAnalysis");
				systemAdminNoMethodList.add("bloodPressDataDetail");
				systemAdminNoMethodList.add("searchBloodPressRecoverPlan");
				systemAdminNoMethodList.add("getDataFromCloud");
				systemAdminNoMethodList.add("getRecoverSchema");
				systemAdminNoMethodList.add("getBloodSugerFromCloud");
				systemAdminNoMethodList.add("bloodSugerDataAnalysis");
				systemAdminNoMethodList.add("bloodSugerTendAnalysis");
				systemAdminNoMethodList.add("bloodSugerDataDetail");
				systemAdminNoMethodList.add("searchBloodSugerRecoverPlan");
				systemAdminNoMethodList.add("getAllInOneData");
				systemAdminNoMethodList.add("getPhysicalExamByHealthFileId");
				systemAdminNoMethodList.add("getBloodOxygenFromCloud");
				systemAdminNoMethodList.add("bloodOxygenDataAnalysis");
				systemAdminNoMethodList.add("bloodOxygenTendAnalysis");
				systemAdminNoMethodList.add("getAllInOneFromCloud");
				systemAdminNoMethodList.add("bloodOxygenDataDetail");

				// 查询
				systemAdminNoMethodList.add("allInOneDataAnalysis");
				systemAdminNoMethodList.add("allInOneTendAnalysis");
				systemAdminNoMethodList.add("allInOneDataDetail");*/

				// two_three
				systemAdminNoMethodList.add("toAlarmManager");
				systemAdminNoMethodList.add("getAlarmData");
				systemAdminNoMethodList.add("hanglehealthAlarm");
				systemAdminNoMethodList.add("getBloodPressureDetail");
				systemAdminNoMethodList.add("getBloodSugarDetail");
				// two_four
				systemAdminNoMethodList.add("searchChronicPeople");
				systemAdminNoMethodList.add("getAllHighBloodManager");
				systemAdminNoMethodList.add("infoChronic");
				systemAdminNoMethodList.add("showOldPlan");
				systemAdminNoMethodList.add("searchRecoveryPlan");
				systemAdminNoMethodList.add("jumpInfoRecoveryPlan");
				systemAdminNoMethodList.add("getBloodPressFromCloud");
				systemAdminNoMethodList.add("bloodPressDataAnalysis");
				systemAdminNoMethodList.add("bloodPressTendAnalysis");
				systemAdminNoMethodList.add("bloodPressDataDetail");
				systemAdminNoMethodList.add("showDoctorDiagnosis");
				systemAdminNoMethodList.add("searchDoctorDiagnosis");
				systemAdminNoMethodList.add("infoDoctorDiagnosis");

				// two_five
				systemAdminNoMethodList.add("searchChronicPeople");
				systemAdminNoMethodList.add("getAllDiabeteManager");
				systemAdminNoMethodList.add("infoChronic");
				systemAdminNoMethodList.add("showOldPlan");
				systemAdminNoMethodList.add("searchRecoveryPlan");
				systemAdminNoMethodList.add("jumpInfoRecoveryPlan");
				systemAdminNoMethodList.add("showDoctorDiagnosis");
				systemAdminNoMethodList.add("searchDoctorDiagnosis");
				systemAdminNoMethodList.add("infoDoctorDiagnosis");

				systemAdminNoMethodList.add("getBloodSugerFromCloud");
				systemAdminNoMethodList.add("bloodSugerDataAnalysis");
				systemAdminNoMethodList.add("bloodSugerTendAnalysis");
				systemAdminNoMethodList.add("bloodSugerDataDetail");
				// two_six
				systemAdminNoMethodList.add("searchChronicPeople");
				systemAdminNoMethodList.add("getAllOtherManager");
				systemAdminNoMethodList.add("infoChronic");
				systemAdminNoMethodList.add("showOldPlan");
				systemAdminNoMethodList.add("searchRecoveryPlan");
				systemAdminNoMethodList.add("jumpInfoRecoveryPlan");
				systemAdminNoMethodList.add("showDoctorDiagnosis");
				systemAdminNoMethodList.add("searchDoctorDiagnosis");
				systemAdminNoMethodList.add("infoDoctorDiagnosis");
				// two_seven
				systemAdminNoMethodList.add("getList");
				systemAdminNoMethodList.add("getListByCX");
				systemAdminNoMethodList.add("showDetail");

				// three
				systemAdminNoMethodList.add("addsearchPhysicalExam");
				systemAdminNoMethodList.add("showPhysicalExam");
				systemAdminNoMethodList.add("firstIndexsearch");
				systemAdminNoMethodList.add("searchMore");
				systemAdminNoMethodList.add("showPhysicalExamListBypersonal");
				systemAdminNoMethodList.add("fuction");
				// four_one
				systemAdminNoMethodList.add("error");
				systemAdminNoMethodList.add("getSchemes");
				systemAdminNoMethodList.add("recoverySchemeDetails");
				systemAdminNoMethodList.add("execute");
				// four_two
				systemAdminNoMethodList.add("getAllPlan");
				systemAdminNoMethodList.add("getRecoveryPlan");
				systemAdminNoMethodList.add("getPlanById");
				systemAdminNoMethodList.add("errorplan");
				// four_three

				systemAdminNoMethodList.add("getSearchRecovery");
				systemAdminNoMethodList.add("getRecovery");
				systemAdminNoMethodList.add("getRocoveryById");
				systemAdminNoMethodList.add("errorStatistical");

				// five_one
				systemAdminNoMethodList.add("getHealthEducationBySearch");
				systemAdminNoMethodList.add("getHealthEducationList");
				systemAdminNoMethodList.add("getHealthEducationDetail");

				// five_two
				systemAdminNoMethodList.add("doorKnockingDetails");
				systemAdminNoMethodList.add("getDoorKnockingListById");
				systemAdminNoMethodList.add("getDoorknockingBySearch");
				// five_three
				systemAdminNoMethodList.add("knowledgeLectureDetails");
				systemAdminNoMethodList.add("getKnowledgeLectureListById");
				systemAdminNoMethodList.add("getKnowledgeLectureBySearch");

				// five_four

				systemAdminNoMethodList.add("comminityClinicDetails");
				systemAdminNoMethodList.add("getComminityClinicListById");
				systemAdminNoMethodList.add("getComminityClinicBySearch");

				// six_one

				systemAdminNoMethodList.add("searchFollowRecord");
				systemAdminNoMethodList.add("showFollowRecord");
				systemAdminNoMethodList.add("findFollowRecord");
				systemAdminNoMethodList.add("turnToDetailFollowRecord");
				// six_two
				systemAdminNoMethodList.add("searchIncompleteRecord");
				systemAdminNoMethodList.add("showIncompleteRecord");
				systemAdminNoMethodList.add("findIncompleteRecord");
				systemAdminNoMethodList.add("turnToDetailIncompleteRecord");
//				systemAdminNoMethodList.add("turnToDetailIncompleteRecordNew");
				// six_three
				systemAdminNoMethodList.add("searchConsultationRecord");
				systemAdminNoMethodList.add("showConsultationRecord");
				systemAdminNoMethodList.add("findConsultationRecord");
				systemAdminNoMethodList.add("turnToDetailConsultationRecord");
				// six_four
				systemAdminNoMethodList.add("searchDualReferralRecord");
				systemAdminNoMethodList.add("showDualReferralRecord");
				systemAdminNoMethodList.add("findDualReferralRecord");
				systemAdminNoMethodList.add("turnToDetailDualReferralRecord");

				// six_five
				systemAdminNoMethodList.add("searchMedicalRecord");
				systemAdminNoMethodList.add("showMedicalRecord");
				systemAdminNoMethodList.add("findMedicalRecord");
				systemAdminNoMethodList.add("turnToDetailMedicalRecord");
				// six_six
				systemAdminNoMethodList.add("searchFamilyBedHistory");
				systemAdminNoMethodList.add("showFamilyBedHistory");
				systemAdminNoMethodList.add("findFamilyBedHistory");
				systemAdminNoMethodList.add("turnToDetailFamilyBedHistory");
				// six_seven
				systemAdminNoMethodList.add("searchInHospitalHistory");
				systemAdminNoMethodList.add("showInHospitalHistory");
				systemAdminNoMethodList.add("findInHospitalHistory");
				systemAdminNoMethodList.add("turnToDetailInHospitalHistory");

				// seven_one
				systemAdminNoMethodList.add("check");
				systemAdminNoMethodList.add("search");
				systemAdminNoMethodList.add("execute");
				systemAdminNoMethodList.add("showStaffInfo");
				systemAdminNoMethodList.add("updateStaff");
				systemAdminNoMethodList.add("getAllStaffsBySQId");
				// seven_two
				systemAdminNoMethodList.add("selectStaffLeaves");
				systemAdminNoMethodList.add("getAllStaffLeaves");
				systemAdminNoMethodList.add("updateStaffLeaveById");
				systemAdminNoMethodList.add("showStaffLeave");

				// seven_three
				systemAdminNoMethodList.add("search");
				systemAdminNoMethodList.add("getAllStaffRewardPunish");
				systemAdminNoMethodList.add("staffRewardPunishDetail");

				// seven_four
				systemAdminNoMethodList.add("getRoleList");
				systemAdminNoMethodList.add("search");
				systemAdminNoMethodList.add("getAllOperators");
				systemAdminNoMethodList.add("changeUperatorState");
				systemAdminNoMethodList.add("recoverPassword");
				systemAdminNoMethodList.add("getAllStaffExceptOperator");
				systemAdminNoMethodList.add("getUpdateOperatorById");
				
				// eight_one
				systemAdminNoMethodList.add("staticticsList");
				systemAdminNoMethodList.add("getStatisticsListBySuperAdmin");
				systemAdminNoMethodList.add("getStatisticsList");
				systemAdminNoMethodList.add("jumpCurrentCount");
				systemAdminNoMethodList.add("getComHosDiseaseAnalyse");
				// eight_two
				systemAdminNoMethodList.add("jumpDeviceStatistics");
				systemAdminNoMethodList.add("deviceStatisticsAD");
				systemAdminNoMethodList.add("jumpOldStatistics");
				systemAdminNoMethodList.add("oldStatisticsByAD");
				// eight_three
				systemAdminNoMethodList.add("jumpALLCurrentCount");
				systemAdminNoMethodList.add("getAllGroupDiseaseAnalyse");
				systemAdminNoMethodList.add("getComHosDiseaseAnalyseByAD");

				// nine
				systemAdminNoMethodList.add("getEquipmentManageDetails");
				systemAdminNoMethodList.add("getEquipmentManageListById");
				systemAdminNoMethodList.add("getEquipmentManageBySearch");
				systemAdminNoMethodList.add("getMaintenanceListById");

				/*// ten_one
				systemAdminNoMethodList.add("jumpAddHospital");
				systemAdminNoMethodList.add("addHospital");
				systemAdminNoMethodList.add("deleteHospital");
				systemAdminNoMethodList.add("jumpEditHospital");
				systemAdminNoMethodList.add("editHospital");
				systemAdminNoMethodList.add("changePassword");
				systemAdminNoMethodList.add("changeState");
				systemAdminNoMethodList.add("getAllHospitals");
				systemAdminNoMethodList.add("getHospitalList");
				systemAdminNoMethodList.add("ifHaveLogin");
				systemAdminNoMethodList.add("ifHaveComHosName");*/

				// ten_two
				systemAdminNoMethodList.add("jumpAddExpertScheme");
				systemAdminNoMethodList.add("addExpertScheme");
				systemAdminNoMethodList.add("checkScheme");
				systemAdminNoMethodList.add("expertSchemeDetails");
				systemAdminNoMethodList.add("deleteExpertSchemeById");
				systemAdminNoMethodList.add("jumpEditExpertScheme");
				systemAdminNoMethodList.add("editExpertRecoveryScheme");
				systemAdminNoMethodList.add("getExpertScheme");
				systemAdminNoMethodList.add("jsonChuan");
				systemAdminNoMethodList.add("expertExport");
				systemAdminNoMethodList.add("errorExpertScheme");
				// ten_three
				systemAdminNoMethodList.add("getMaintainableByPage");
				systemAdminNoMethodList.add("getMaintainableEntry");
				systemAdminNoMethodList.add("updateMaintainableName");
				systemAdminNoMethodList.add("MaintainableEntryAction");
				systemAdminNoMethodList.add("addEntry");
				systemAdminNoMethodList.add("deleteEntry");
				systemAdminNoMethodList.add("updateEntry");
				systemAdminNoMethodList.add("getMaintainableEntry");
				// ten_four
				/*
				 * systemAdminNoMethodList.add("getRoleList");
				 * systemAdminNoMethodList.add("findRoleInfo");
				 * systemAdminNoMethodList.add("jumpFindRoleInfo");
				 * systemAdminNoMethodList.add("getPowerlist");
				 * systemAdminNoMethodList.add("getMenuList");
				 * systemAdminNoMethodList.add("getButtonlist");
				 * systemAdminNoMethodList.add("addRole");
				 * systemAdminNoMethodList.add("jumpAddRole");
				 * systemAdminNoMethodList.add("deleteRole");
				 * systemAdminNoMethodList.add("jumpEditRole");
				 * systemAdminNoMethodList.add("editRole");
				 * systemAdminNoMethodList.add("ifHaveRole");
				 */
				// ten_five
				systemAdminNoMethodList.add("getHosGroupList");
				systemAdminNoMethodList.add("jumpAddHospitalGroup");
				systemAdminNoMethodList.add("addHospitalGroup");
				systemAdminNoMethodList.add("jumpEditHospitalGroup");
				systemAdminNoMethodList.add("editHospitalGroup");
				systemAdminNoMethodList.add("changePassword");
				systemAdminNoMethodList.add("deleteHosGroup");
				systemAdminNoMethodList.add("changeState");
				systemAdminNoMethodList.add("getHosInfo");
				systemAdminNoMethodList.add("ifHaveGroupName");
				systemAdminNoMethodList.add("ifHaveLogin");
				systemAdminNoMethodList.add("ifHavePhonenumg");

				// 平台管理员在以上action中只具有查看,打印权限权限
				if (systemAdminActionList.contains(action)
						&& systemAdminNoMethodList.contains(method)) {

					return invocation.invoke();
				} else {
					return "error";
				}

			}

		}// admin拦截结束

		// 集团负责人拦截开始////////////
		// 集团负责人开始,允许能够访问的action和方法
		if (group!=null) {
			// 集团负责人能访问Action,但是不能新增\删除\修改
			List<String> groupAdminActionList = new ArrayList<String>();
			// one_one模块 健康档案
			groupAdminActionList.add("com.jmjk.action.HealthFileAction");
			// one_two模块 慢病档案
			groupAdminActionList.add("com.jmjk.action.OldChronicFollowAction");
			groupAdminActionList.add("com.jmjk.action.ShowOldChronicAction");
			// two_one模块 数据采集
			groupAdminActionList.add("com.jmjk.action.HealthManagerAction");
			groupAdminActionList.add("com.jmjk.action.UrineAction");
			groupAdminActionList.add("com.jmjk.action.EcgAction");
			// two_two模块 日常监测
			groupAdminActionList.add("com.jmjk.action.HealthDailyCheckAction");
			groupAdminActionList.add("com.jmjk.action.HealthManagerAction");
			groupAdminActionList.add("com.jmjk.action.UrineAction");
			groupAdminActionList.add("com.jmjk.action.EcgAction");
			// two_three模块 健康预警
			groupAdminActionList.add("com.jmjk.action.HealthAlarmAction");

			// two_four/five/six模块 血压\血糖\其他慢病管理
			groupAdminActionList.add("com.jmjk.action.ChronicManagerAction");
			groupAdminActionList.add("com.jmjk.action.DoctorDiagnosisAction");
			//two_seven模块 中医治未病
			groupAdminActionList.add("com.jmjk.action.XKHealthMachineAction");
			// three模块 健康体检
			groupAdminActionList.add("com.jmjk.action.PhysicalExamAction");
			// four_one/two/three模块 康复方案\计划\统计
			groupAdminActionList
					.add("com.jmjk.action.HealthPhysiotherapyAction");
			// five_one模块 健康教育
			groupAdminActionList.add("com.jmjk.action.HealthEducationAction");
			// five_two模块 上门随访
			groupAdminActionList.add("com.jmjk.action.DoorKnockingAction");
			// five_three模块 知识讲座
			groupAdminActionList.add("com.jmjk.action.KnowledgeLectureAction");
			// five_four模块 社区义诊
			groupAdminActionList.add("com.jmjk.action.ComminityClinicAction");
			// six_one模块 随诊记录
			groupAdminActionList.add("com.jmjk.action.FollowRecordAction");
			// six_two模块 接诊记录
			groupAdminActionList.add("com.jmjk.action.IncompleteRecordAction");
			groupAdminActionList.add("com.jmjk.action.IncompleteRecordAction2");
			// six_three模块 会诊
			groupAdminActionList
					.add("com.jmjk.action.ConsultationRecordAction");
			// six_four模块 转诊
			groupAdminActionList
					.add("com.jmjk.action.DualReferralRecordAction");
			// six_five模块 用药记录
			groupAdminActionList.add("com.jmjk.action.MedicalRecordAction");
			// six_six模块 家庭病床
			groupAdminActionList.add("com.jmjk.action.FamilyBedHistoryAction");
			// six_seven模块 住院记录
			groupAdminActionList
					.add("com.jmjk.action.InHospitalHistoryAction");
			// seven_one/two 员工信息\请假
			groupAdminActionList.add("com.jmjk.action.StaffAction");
			// seven_three 员工奖惩
			groupAdminActionList
					.add("com.jmjk.action.StaffRewardPunishAction");
			// seven_four 操作员管理
			groupAdminActionList.add("com.jmjk.action.OperatorAction");
			// eight 统计分析
			groupAdminActionList.add("com.jmjk.action.StatisticsAction");
			// nine 设备管理
			groupAdminActionList.add("com.jmjk.action.EquipmentManageAction");
			// ten 系统管理
			groupAdminActionList
					.add("com.jmjk.action.CommunityHospitalAction");
			groupAdminActionList.add("com.jmjk.action.CommunityHospitalGroupAction");
			/*groupAdminActionList.add("com.jmjk.action.MaintainableAction");
			groupAdminActionList.add("com.jmjk.action.MaintainableAction");

			groupAdminActionList
					.add("com.jmjk.action.MaintainableEntryAction");*/

			// 向集团负责人添加能访问的方法
			List<String> groupAdminNoMethodList = new ArrayList<String>();
			// one_one
			groupAdminNoMethodList.add("showHealthFile");
			groupAdminNoMethodList.add("turnToHealthFileView");
			groupAdminNoMethodList.add("getDisease");
			groupAdminNoMethodList.add("searchHealthFile");
			// one_two
			groupAdminNoMethodList.add("firstShowChronic");
			groupAdminNoMethodList.add("jumpShowChronic");
			groupAdminNoMethodList.add("showOneAllFollow");
			groupAdminNoMethodList.add("jumpShowFollow");
			groupAdminNoMethodList.add("searchChronicPeople");
			groupAdminNoMethodList.add("showSlowChronic");

			// two_one
			groupAdminNoMethodList.add("getHealthFileList");
			groupAdminNoMethodList.add("getHealthFileListBySearchBean");
			groupAdminNoMethodList.add("toUploadData");

			// two_two
			groupAdminNoMethodList.add("getHealthFileList");
			groupAdminNoMethodList.add("getHealthFileListBySearchBean");
			groupAdminNoMethodList.add("getBloodPressFromCloud");
			groupAdminNoMethodList.add("bloodPressDataAnalysis");
			groupAdminNoMethodList.add("bloodPressTendAnalysis");
			groupAdminNoMethodList.add("bloodPressDataDetail");
			groupAdminNoMethodList.add("searchBloodPressRecoverPlan");
			groupAdminNoMethodList.add("getDataFromCloud");
			groupAdminNoMethodList.add("getRecoverSchema");
			groupAdminNoMethodList.add("getBloodSugerFromCloud");
			groupAdminNoMethodList.add("bloodSugerDataAnalysis");
			groupAdminNoMethodList.add("bloodSugerTendAnalysis");
			groupAdminNoMethodList.add("bloodSugerDataDetail");
			groupAdminNoMethodList.add("searchBloodSugerRecoverPlan");
			groupAdminNoMethodList.add("getAllInOneData");
			groupAdminNoMethodList.add("getPhysicalExamByHealthFileId");
			groupAdminNoMethodList.add("getBloodOxygenFromCloud");
			groupAdminNoMethodList.add("bloodOxygenDataAnalysis");
			groupAdminNoMethodList.add("bloodOxygenTendAnalysis");
			groupAdminNoMethodList.add("getAllInOneFromCloud");
			groupAdminNoMethodList.add("bloodOxygenDataDetail");

			// 查询
			groupAdminNoMethodList.add("allInOneDataAnalysis");
			groupAdminNoMethodList.add("allInOneTendAnalysis");
			groupAdminNoMethodList.add("allInOneDataDetail");

			// two_three
			groupAdminNoMethodList.add("toAlarmManager");
			groupAdminNoMethodList.add("getAlarmData");
			groupAdminNoMethodList.add("hanglehealthAlarm");
			groupAdminNoMethodList.add("getBloodPressureDetail");
			groupAdminNoMethodList.add("getBloodSugarDetail");
			// two_four
			groupAdminNoMethodList.add("searchChronicPeople");
			groupAdminNoMethodList.add("getAllHighBloodManager");
			groupAdminNoMethodList.add("infoChronic");
			groupAdminNoMethodList.add("showOldPlan");
			groupAdminNoMethodList.add("searchRecoveryPlan");
			groupAdminNoMethodList.add("jumpInfoRecoveryPlan");
			groupAdminNoMethodList.add("getBloodPressFromCloud");
			groupAdminNoMethodList.add("bloodPressDataAnalysis");
			groupAdminNoMethodList.add("bloodPressTendAnalysis");
			groupAdminNoMethodList.add("bloodPressDataDetail");
			groupAdminNoMethodList.add("showDoctorDiagnosis");
			groupAdminNoMethodList.add("searchDoctorDiagnosis");
			groupAdminNoMethodList.add("infoDoctorDiagnosis");

			// two_five
			groupAdminNoMethodList.add("searchChronicPeople");
			groupAdminNoMethodList.add("getAllDiabeteManager");
			groupAdminNoMethodList.add("infoChronic");
			groupAdminNoMethodList.add("showOldPlan");
			groupAdminNoMethodList.add("searchRecoveryPlan");
			groupAdminNoMethodList.add("jumpInfoRecoveryPlan");
			groupAdminNoMethodList.add("showDoctorDiagnosis");
			groupAdminNoMethodList.add("searchDoctorDiagnosis");
			groupAdminNoMethodList.add("infoDoctorDiagnosis");

			groupAdminNoMethodList.add("getBloodSugerFromCloud");
			groupAdminNoMethodList.add("bloodSugerDataAnalysis");
			groupAdminNoMethodList.add("bloodSugerTendAnalysis");
			groupAdminNoMethodList.add("bloodSugerDataDetail");
			// two_six
			groupAdminNoMethodList.add("searchChronicPeople");
			groupAdminNoMethodList.add("getAllOtherManager");
			groupAdminNoMethodList.add("infoChronic");
			groupAdminNoMethodList.add("showOldPlan");
			groupAdminNoMethodList.add("searchRecoveryPlan");
			groupAdminNoMethodList.add("jumpInfoRecoveryPlan");
			groupAdminNoMethodList.add("showDoctorDiagnosis");
			groupAdminNoMethodList.add("searchDoctorDiagnosis");
			groupAdminNoMethodList.add("infoDoctorDiagnosis");
			// two_seven
			groupAdminNoMethodList.add("getList");
			groupAdminNoMethodList.add("getListByCX");
			groupAdminNoMethodList.add("showDetail");
			
			// three
			groupAdminNoMethodList.add("fuction");
			groupAdminNoMethodList.add("addsearchPhysicalExam");
			groupAdminNoMethodList.add("showPhysicalExam");
			groupAdminNoMethodList.add("firstIndexsearch");
			groupAdminNoMethodList.add("searchMore");
			groupAdminNoMethodList.add("showPhysicalExamListBypersonal");
			// four_one
			groupAdminNoMethodList.add("error");
			groupAdminNoMethodList.add("getSchemes");
			groupAdminNoMethodList.add("recoverySchemeDetails");
			groupAdminNoMethodList.add("execute");
			// four_two
			groupAdminNoMethodList.add("getAllPlan");
			groupAdminNoMethodList.add("getRecoveryPlan");
			groupAdminNoMethodList.add("getPlanById");
			groupAdminNoMethodList.add("errorplan");
			// four_three

			groupAdminNoMethodList.add("getSearchRecovery");
			groupAdminNoMethodList.add("getRecovery");
			groupAdminNoMethodList.add("getRocoveryById");
			groupAdminNoMethodList.add("errorStatistical");

			// five_one
			groupAdminNoMethodList.add("getHealthEducationBySearch");
			groupAdminNoMethodList.add("getHealthEducationList");
			groupAdminNoMethodList.add("getHealthEducationDetail");

			// five_two
			groupAdminNoMethodList.add("doorKnockingDetails");
			groupAdminNoMethodList.add("getDoorKnockingListById");
			groupAdminNoMethodList.add("getDoorknockingBySearch");
			// five_three
			groupAdminNoMethodList.add("knowledgeLectureDetails");
			groupAdminNoMethodList.add("getKnowledgeLectureListById");
			groupAdminNoMethodList.add("getKnowledgeLectureBySearch");

			// five_four

			groupAdminNoMethodList.add("comminityClinicDetails");
			groupAdminNoMethodList.add("getComminityClinicListById");
			groupAdminNoMethodList.add("getComminityClinicBySearch");

			// six_one

			groupAdminNoMethodList.add("searchFollowRecord");
			groupAdminNoMethodList.add("showFollowRecord");
			groupAdminNoMethodList.add("findFollowRecord");
			groupAdminNoMethodList.add("turnToDetailFollowRecord");
			groupAdminNoMethodList.add("ReOlder");
			// six_two
			groupAdminNoMethodList.add("searchIncompleteRecord");
			groupAdminNoMethodList.add("showIncompleteRecord");
			groupAdminNoMethodList.add("findIncompleteRecord");
			groupAdminNoMethodList.add("turnToDetailIncompleteRecord");
			groupAdminNoMethodList.add("ReOlder");
			// six_three
			groupAdminNoMethodList.add("searchConsultationRecord");
			groupAdminNoMethodList.add("showConsultationRecord");
			groupAdminNoMethodList.add("findConsultationRecord");
			groupAdminNoMethodList.add("turnToDetailConsultationRecord");
			groupAdminNoMethodList.add("ReOlder");
			// six_four
			groupAdminNoMethodList.add("searchDualReferralRecord");
			groupAdminNoMethodList.add("showDualReferralRecord");
			groupAdminNoMethodList.add("findDualReferralRecord");
			groupAdminNoMethodList.add("turnToDetailDualReferralRecord");
			groupAdminNoMethodList.add("ReOlder");

			// six_five
			groupAdminNoMethodList.add("searchMedicalRecord");
			groupAdminNoMethodList.add("showMedicalRecord");
			groupAdminNoMethodList.add("findMedicalRecord");
			groupAdminNoMethodList.add("turnToDetailMedicalRecord");
			groupAdminNoMethodList.add("ReOlder");
			// six_six
			groupAdminNoMethodList.add("searchFamilyBedHistory");
			groupAdminNoMethodList.add("showFamilyBedHistory");
			groupAdminNoMethodList.add("findFamilyBedHistory");
			groupAdminNoMethodList.add("turnToDetailFamilyBedHistory");
			groupAdminNoMethodList.add("ReOlder");
			// six_seven
			groupAdminNoMethodList.add("searchInHospitalHistory");
			groupAdminNoMethodList.add("showInHospitalHistory");
			groupAdminNoMethodList.add("findInHospitalHistory");
			groupAdminNoMethodList.add("turnToDetailInHospitalHistory");
			groupAdminNoMethodList.add("ReOlder");

			// seven_one
			groupAdminNoMethodList.add("check");
			groupAdminNoMethodList.add("search");
			groupAdminNoMethodList.add("execute");
			groupAdminNoMethodList.add("showStaffInfo");
			groupAdminNoMethodList.add("updateStaff");
			groupAdminNoMethodList.add("getAllStaffsBySQId");
			// seven_two
			groupAdminNoMethodList.add("selectStaffLeaves");
			groupAdminNoMethodList.add("getAllStaffLeaves");
			groupAdminNoMethodList.add("updateStaffLeaveById");
			groupAdminNoMethodList.add("showStaffLeave");

			// seven_three
			groupAdminNoMethodList.add("search");
			groupAdminNoMethodList.add("getAllStaffRewardPunish");
			groupAdminNoMethodList.add("staffRewardPunishDetail");

			// seven_four
			groupAdminNoMethodList.add("getRoleList");
			groupAdminNoMethodList.add("search");
			groupAdminNoMethodList.add("getAllOperators");
			groupAdminNoMethodList.add("changeUperatorState");
			groupAdminNoMethodList.add("recoverPassword");
			groupAdminNoMethodList.add("getAllStaffExceptOperator");
			groupAdminNoMethodList.add("getUpdateOperatorById");
			
			// eight_one
			groupAdminNoMethodList.add("staticticsList");
			groupAdminNoMethodList.add("getStatisticsList");
			groupAdminNoMethodList.add("jumpCurrentCount");
			groupAdminNoMethodList.add("getComHosDiseaseAnalyse");
			// eight_two
			groupAdminNoMethodList.add("jumpDeviceStatistics");
			groupAdminNoMethodList.add("deviceStatistics");
			groupAdminNoMethodList.add("jumpOldStatistics");
			groupAdminNoMethodList.add("oldStatistics");
			// eight_three
			groupAdminNoMethodList.add("jumpALLCurrentCount");
			groupAdminNoMethodList.add("getAllComHosDiseaseAnalyse");
			groupAdminNoMethodList.add("getComHosDiseaseAnalyse");
		
			// nine
			groupAdminNoMethodList.add("getEquipmentManageDetails");
			groupAdminNoMethodList.add("getEquipmentManageListById");
			groupAdminNoMethodList.add("getEquipmentManageBySearch");
			groupAdminNoMethodList.add("getMaintenanceListById");

			// ten_one
			groupAdminNoMethodList.add("jumpAddHospital");
			groupAdminNoMethodList.add("addHospital");
			groupAdminNoMethodList.add("deleteHospital");
			groupAdminNoMethodList.add("jumpEditHospital");
			groupAdminNoMethodList.add("editHospital");
			groupAdminNoMethodList.add("changePassword");
			groupAdminNoMethodList.add("changeState");
			groupAdminNoMethodList.add("getAllHospitals");
			groupAdminNoMethodList.add("getHospitalList");
			groupAdminNoMethodList.add("ifHaveLogin");
			groupAdminNoMethodList.add("ifHaveComHosName");
			groupAdminNoMethodList.add("jumpCopyGroup");
			groupAdminNoMethodList.add("copyGroup");
			groupAdminNoMethodList.add("ifMaxHosNum");
			groupAdminNoMethodList.add("ifHavePhonenum");
			groupAdminNoMethodList.add("ifHavePhonenumg");
			groupAdminNoMethodList.add("ifHaveCode");

			// ten_two
			groupAdminNoMethodList.add("jumpAddExpertScheme");
			groupAdminNoMethodList.add("addExpertScheme");
			groupAdminNoMethodList.add("checkScheme");
			groupAdminNoMethodList.add("expertSchemeDetails");
			groupAdminNoMethodList.add("deleteExpertSchemeById");
			groupAdminNoMethodList.add("jumpEditExpertScheme");
			groupAdminNoMethodList.add("editExpertRecoveryScheme");
			groupAdminNoMethodList.add("getExpertScheme");
			groupAdminNoMethodList.add("jsonChuan");
			groupAdminNoMethodList.add("expertExport");
			groupAdminNoMethodList.add("errorExpertScheme");
			/*// ten_three
			groupAdminNoMethodList.add("getMaintainableByPage");
			groupAdminNoMethodList.add("getMaintainableEntry");
			groupAdminNoMethodList.add("updateMaintainableName");
			groupAdminNoMethodList.add("MaintainableEntryAction");
			groupAdminNoMethodList.add("addEntry");
			groupAdminNoMethodList.add("deleteEntry");
			groupAdminNoMethodList.add("updateEntry");
			groupAdminNoMethodList.add("getMaintainableEntry");*/
			// ten_four
			/*
			 * systemAdminNoMethodList.add("getRoleList");
			 * systemAdminNoMethodList.add("findRoleInfo");
			 * systemAdminNoMethodList.add("jumpFindRoleInfo");
			 * systemAdminNoMethodList.add("getPowerlist");
			 * systemAdminNoMethodList.add("getMenuList");
			 * systemAdminNoMethodList.add("getButtonlist");
			 * systemAdminNoMethodList.add("addRole");
			 * systemAdminNoMethodList.add("jumpAddRole");
			 * systemAdminNoMethodList.add("deleteRole");
			 * systemAdminNoMethodList.add("jumpEditRole");
			 * systemAdminNoMethodList.add("editRole");
			 * systemAdminNoMethodList.add("ifHaveRole");
			 */

			// 平台管理员在以上action中只具有查看,打印权限权限
			if (groupAdminActionList.contains(action)
					&& groupAdminNoMethodList.contains(method)) {

				return invocation.invoke();
			} else {
				return "error";
			}

		}// 集团负责人拦截结束

		// 员工拦截开始///////////////////////////////////////////////////////////////////////////////////

		// 如果员工不为空,判断action,授权方法
		if (tstaff != null) {

		    int special=0;//0代表不是责任医生,1代表是责任医生
			// 根据员工id获得权限列表
			List<String> rightList = rightsService
					.getAllRightValueByStaffId(tstaff.getStaffId());
			if(tstaff.getIsSpecialDoctor()){
			    special=1;
			    rightList.add(two_two);
			}
			// 员工访问Action
			// one_one模块 健康档案
			String one_one_Action = "com.jmjk.action.HealthFileAction";
			// one_two模块 慢病档案
			List<String> one_two_Action = new ArrayList<String>();
			one_two_Action.add("com.jmjk.action.OldChronicFollowAction");
			one_two_Action.add("com.jmjk.action.ShowOldChronicAction");
			// two_one模块 健康数据采集
			String two_one_Action = "com.jmjk.action.HealthManagerAction";
			// two_two模块 健康日常监测
			List<String> two_two_Action = new ArrayList<String>();
			// String two_two_Action = "com.jmjk.action.HealthManagerAction";
			two_two_Action.add("com.jmjk.action.HealthDailyCheckAction");
			two_two_Action.add("com.jmjk.action.HealthManagerAction");
			two_two_Action.add("com.jmjk.action.UrineAction");
			two_two_Action.add("com.jmjk.action.EcgAction");
//			two_two_Action.add("com.jmjk.action.UrineAction2");
			// two_three模块 健康预警
			String two_three_Action = "com.jmjk.action.HealthAlarmAction";
			// two_four/five/six模块 血压\血糖\其他慢病管理
			List<String> two_four_five_six_Action = new ArrayList<String>();
			two_four_five_six_Action
					.add("com.jmjk.action.ChronicManagerAction");
			two_four_five_six_Action
					.add("com.jmjk.action.DoctorDiagnosisAction");
			//two_seven模块 中医治未病
			List<String> two_seven_Action = new ArrayList<String>();
			two_seven_Action.add("com.jmjk.action.XKHealthMachineAction");
			// three模块 健康体检
			String three_Action = "com.jmjk.action.PhysicalExamAction";
			// four_one/two/three模块 康复方案\计划\统计
			String four_one_two_three_Action = "com.jmjk.action.HealthPhysiotherapyAction";
			// five_one模块 健康教育
			String five_one_Action = "com.jmjk.action.HealthEducationAction";
			// five_two模块 上门随访
			String five_two_Action = "com.jmjk.action.DoorKnockingAction";
			// five_three模块 知识讲座
			String five_three_Action = "com.jmjk.action.KnowledgeLectureAction";
			// five_four模块 社区义诊
			String five_four_Action = "com.jmjk.action.ComminityClinicAction";
			// six_one模块 随诊记录
			String six_one_Action = "com.jmjk.action.FollowRecordAction";
			// six_two模块 接诊记录
			String six_two_Action = "com.jmjk.action.IncompleteRecordAction";
			String six_two_Action2 = "com.jmjk.action.IncompleteRecordAction2";
			// six_three模块 会诊
			String six_three_Action = "com.jmjk.action.ConsultationRecordAction";
			// six_four模块 转诊
			String six_four_Action = "com.jmjk.action.DualReferralRecordAction";
			// six_five模块 用药记录
			String six_five_Action = "com.jmjk.action.MedicalRecordAction";
			// six_six模块 家庭病床
			String six_six_Action = "com.jmjk.action.FamilyBedHistoryAction";
			// six_seven模块 住院记录
			String six_seven_Action = "com.jmjk.action.InHospitalHistoryAction";
			// seven_one/two 员工信息\请假
			String seven_one_two_Action = "com.jmjk.action.StaffAction";
			// seven_three 员工奖惩
			String seven_three_Action = "com.jmjk.action.StaffRewardPunishAction";
			// seven_four 操作员管理
			String seven_four_Action = "com.jmjk.action.OperatorAction";
			// eight 设备管理
			String nine_Action = "com.jmjk.action.EquipmentManageAction";

			// 如果员工权限包含栏目编码,同时访问的action是该栏目链接的action...go

			// one_one 健康档案
			if (rightList.contains(one_one) && one_one_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), one_one);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("searchHealthFile");
					staffMethodList.add("showHealthFile");
					staffMethodList.add("turnToHealthFileView");
					staffMethodList.add("searchHealthFile");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {
					staffMethodList.add("checkPic");
					staffMethodList.add("checkIdNum");
					staffMethodList.add("checkFileNum");
					staffMethodList.add("getFileNumAuto");
					staffMethodList.add("turnToAddHealthFile");
					staffMethodList.add("register");
					staffMethodList.add("registerHealthMachine");
					staffMethodList.add("addHealthFile");
					staffMethodList.add("input");
					staffMethodList.add("getBirthDate");
					staffMethodList.add("checkIphone");
					staffMethodList.add("isIdCard");

				}

				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("turnToHealthFileDetil");
					staffMethodList.add("getDisease");
					staffMethodList.add("updateHealthFile");
					staffMethodList.add("checkIdNum");
					staffMethodList.add("checkFileNum");
					staffMethodList.add("getFileNumAuto");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("cancelHealthFile");
					staffMethodList.add("deleteHealthFile");
					staffMethodList.add("deleteMoreHealthFile");
					staffMethodList.add("killHealthFile");
					staffMethodList.add("killMoreHealthFile");
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("fuction");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				} else {
					return "error";
				}
			}

			// one_two 慢病档案
			if (rightList.contains(one_two) && one_two_Action.contains(action)) {

				// 向员工中添加能够访问的方法
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), one_two);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					// 添加页面显示的方法
					staffMethodList.add("firstShowChronic");
					staffMethodList.add("jumpShowChronic");
					staffMethodList.add("showOneAllFollow");
					staffMethodList.add("jumpShowFollow");
					staffMethodList.add("searchChronicPeople");
					staffMethodList.add("showSlowChronic");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {
					staffMethodList.add("jumpOneHighBlood");
					staffMethodList.add("jumpadd");
					staffMethodList.add("jumpDiabete");
					staffMethodList.add("jumpInsanityform");
					staffMethodList.add("jumpCoronary");
					staffMethodList.add("jumpBronchial");
					staffMethodList.add("jumpTumour");
					staffMethodList.add("jumpHcvd");
					staffMethodList.add("jumpDisability");
					staffMethodList.add("searchOlder");
					staffMethodList.add("addDiabete");
					staffMethodList.add("addOneHighBlood");
					staffMethodList.add("addInsanity");
					staffMethodList.add("addCoronary");
					staffMethodList.add("addBronchial");
					staffMethodList.add("addTumour");
					staffMethodList.add("addHcvd");
					staffMethodList.add("addDisability");
					staffMethodList.add("jumpAddFollow");
					staffMethodList.add("addHighFollow");
					staffMethodList.add("addDiabets");
					staffMethodList.add("addMental");
					staffMethodList.add("addCoronary");
					staffMethodList.add("addCarddiovascular");
					staffMethodList.add("addOtherIll");
					staffMethodList.add("onePeopleJump");
				}
				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("jumpEditChronic");
					staffMethodList.add("editCoronnary");
					staffMethodList.add("editBronchial");
					staffMethodList.add("editDiabete");
					staffMethodList.add("editMental");
					staffMethodList.add("editTumour");
					staffMethodList.add("editDisability");
					staffMethodList.add("editHighBlood");
					staffMethodList.add("editHcvd");
					staffMethodList.add("jumpEditFollow");
					staffMethodList.add("editCardFollow");
					staffMethodList.add("editCornaryFollow");
					staffMethodList.add("editHighBloodFollow");
					staffMethodList.add("editDiabetsFollow");
					staffMethodList.add("editMentalFollow");
					staffMethodList.add("editOtherIllFollow");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("chooseDelete");
					staffMethodList.add("fdeleteChronic");
					staffMethodList.add("deleteChronic");
					staffMethodList.add("deleteFollow");
				}

				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("fuction");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// two_one 健康管理-数据录入
			if (rightList.contains(two_one) && two_one_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), two_one);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					/*staffMethodList.add("getHealthFileList");
					staffMethodList.add("getHealthFileListBySearchBean");
					staffMethodList.add("getDataFromCloud");*/
					 staffMethodList.add("getSuggests");
						staffMethodList.add("searchBloodPressRecoverPlan");
						staffMethodList.add("searchBloodPressRecoverPlan");
						staffMethodList.add("getAllInOneData");
						staffMethodList.add("getHealthFileList");
						staffMethodList.add("getPhysicalExamByHealthFileId");
						staffMethodList.add("getDataFromCloud");
						staffMethodList.add("getBloodPressFromCloud");
						staffMethodList.add("bloodPressDataAnalysis");
						staffMethodList.add("bloodPressTendAnalysis");
						staffMethodList.add("bloodPressDataDetail");
						staffMethodList.add("getRecoverSchema");
						staffMethodList.add("getBloodSugerFromCloud");
						staffMethodList.add("bloodSugerTendAnalysis");
						staffMethodList.add("bloodSugerDataAnalysis");
						staffMethodList.add("bloodSugerDataDetail");
						staffMethodList.add("getBloodOxygenFromCloud");
						staffMethodList.add("bloodOxygenDataAnalysis");
						staffMethodList.add("bloodOxygenDataDetail");
						staffMethodList.add("bloodOxygenTendAnalysis");
						staffMethodList.add("getHealthFileListBySearchBean");
						staffMethodList.add("jumpInfoRecoveryPlan");
						staffMethodList.add("getAllInOneFromCloud");
						staffMethodList.add("getPhysicExemByHealthFileId");
						staffMethodList.add("getPhysicExemById");
						staffMethodList.add("allInOneDataAnalysis");
						staffMethodList.add("allInOneTendAnalysis");
						staffMethodList.add("allInOneDataDetail");
						staffMethodList.add("getList");
						staffMethodList.add("getListByCX");
						staffMethodList.add("showDetail");
						staffMethodList.add("getListFromCloud");
						staffMethodList.add("getListByCXFromCloud");
						staffMethodList.add("showDetailFromCloud");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {
					staffMethodList.add("toUploadData");
					staffMethodList.add("uploadBloodOxygen");
					staffMethodList.add("uploadBloodPressData");
					staffMethodList.add("uploadBloodSugerData");
				}
				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					// 下载健康一体机报告
					staffMethodList.add("downloadHealthReport");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// two_two 健康管理-健康建议
			if (rightList.contains(two_two) && two_two_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue;
				if(special==1)
				{
				    columnValue = 63;
				}
				else{
				    columnValue = rightsService.getStaffValueByColumnCod(
	                        tstaff.getStaffId(), two_two);
				}
				

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
				    staffMethodList.add("getSuggests");
				    staffMethodList.add("jumpHealthAdviceInfo");
				    staffMethodList.add("jumpAddHealthAdvice");
					staffMethodList.add("searchBloodPressRecoverPlan");
					staffMethodList.add("searchBloodPressRecoverPlan");
					staffMethodList.add("getAllInOneData");
					staffMethodList.add("getHealthFileList");
					staffMethodList.add("getPhysicalExamByHealthFileId");
					staffMethodList.add("getDataFromCloud");
					staffMethodList.add("getBloodPressFromCloud");
					staffMethodList.add("bloodPressDataAnalysis");
					staffMethodList.add("bloodPressTendAnalysis");
					staffMethodList.add("bloodPressDataDetail");
					staffMethodList.add("getRecoverSchema");
					staffMethodList.add("getBloodSugerFromCloud");
					staffMethodList.add("bloodSugerTendAnalysis");
					staffMethodList.add("bloodSugerDataAnalysis");
					staffMethodList.add("bloodSugerDataDetail");
					staffMethodList.add("getBloodOxygenFromCloud");
					staffMethodList.add("bloodOxygenDataAnalysis");
					staffMethodList.add("bloodOxygenDataDetail");
					staffMethodList.add("bloodOxygenTendAnalysis");
					staffMethodList.add("getHealthFileListBySearchBean");
					staffMethodList.add("jumpInfoRecoveryPlan");
					staffMethodList.add("getAllInOneFromCloud");
					staffMethodList.add("getPhysicExemByHealthFileId");
					staffMethodList.add("getPhysicExemById");
					staffMethodList.add("allInOneDataAnalysis");
					staffMethodList.add("allInOneTendAnalysis");
					staffMethodList.add("allInOneDataDetail");
					staffMethodList.add("getList");
					staffMethodList.add("getListByCX");
					staffMethodList.add("showDetail");
					staffMethodList.add("getListFromCloud");
					staffMethodList.add("getListByCXFromCloud");
					staffMethodList.add("showDetailFromCloud");

				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {
					
					staffMethodList.add("uploadAllInOne");
					staffMethodList.add("addBPRecoverPlan");
					staffMethodList.add("toAddBPRecoverPlan");
					staffMethodList.add("addSuggestByHealthFileId");

				}
				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("jumpEditHealthAdvice");
				    staffMethodList.add("editSuggest");
					staffMethodList.add("toEditBSCloud");
					staffMethodList.add("editBSCloud");
					staffMethodList.add("toEditBPCloud");
					staffMethodList.add("editBPCloud");
					staffMethodList.add("toEditBOCloud");
					staffMethodList.add("editBOCloud");
					staffMethodList.add("changePlanStatus");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteBloodPressDataCloud");
					staffMethodList.add("deleteSuggests");
					staffMethodList.add("deleteBloodSugerDataCloud");
					staffMethodList.add("deletePlanById");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					// 下载健康一体机报告
					staffMethodList.add("downloadHealthReport");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// two_three 健康管理-健康预警
			if (rightList.contains(two_three)
					&& two_three_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), two_three);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("getAlarmData");
					staffMethodList.add("toAlarmManager");
					staffMethodList.add("getHealthFileFromCloudByHos");
					staffMethodList.add("getHealthFileFromCloudByDoctor");
					staffMethodList.add("getBloodPressureDetail");
					staffMethodList.add("getBloodSugarDetail");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {
				}
				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("hanglehealthAlarm");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}
			// two_four 健康管理_血压管理
			if (rightList.contains(two_four)
					& two_four_five_six_Action.contains(action)) {
				// 向员工中添加能够访问的方法
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), two_four);
				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("searchChronicPeople");
					staffMethodList.add("getAllHighBloodManager");
					staffMethodList.add("infoChronic");
					staffMethodList.add("showOldPlan");
					staffMethodList.add("searchRecoveryPlan");
					staffMethodList.add("jumpInfoRecoveryPlan");
					staffMethodList.add("getBloodPressFromCloud");
					staffMethodList.add("bloodPressDataAnalysis");
					staffMethodList.add("bloodPressTendAnalysis");
					staffMethodList.add("bloodPressDataDetail");
					staffMethodList.add("showDoctorDiagnosis");
					staffMethodList.add("searchDoctorDiagnosis");
					staffMethodList.add("infoDoctorDiagnosis");
				}
				// "添加"权限判断
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {
					staffMethodList.add("addHighManager");
					staffMethodList.add("addHighConfirm");
					staffMethodList.add("addRecoveryPlan");
					staffMethodList.add("addPlan");
					staffMethodList.add("jsonChuan");
					staffMethodList.add("checkPlan");
					staffMethodList.add("addDoctorDiagnosis");
					staffMethodList.add("addDoctorDiagnosisAction");
				}
				// "修改"权限判断
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("jumpEditPlan");
					staffMethodList.add("editRecoveryPlan");
					staffMethodList.add("changePlanStatus");
					staffMethodList.add("toEditBPCloud");
					staffMethodList.add("editBPCloud");
					staffMethodList.add("editDoctorDiagnosis");
					staffMethodList.add("editDoctorDiagnosisAction");
				}
				// "删除"权限判断
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteHighManager");
					staffMethodList.add("deletePlanById");
					staffMethodList.add("deleteBloodPressDataCloud");
					staffMethodList.add("deleteDoctorDiagnosis");
				}
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("function");

				}
				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// two_five 健康管理_血糖管理
			if (rightList.contains(two_five)
					& two_four_five_six_Action.contains(action)) {
				// 向员工中添加能够访问的方法
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), two_five);
				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("searchChronicPeople");
					staffMethodList.add("getAllDiabeteManager");
					staffMethodList.add("infoChronic");
					staffMethodList.add("showOldPlan");
					staffMethodList.add("searchRecoveryPlan");
					staffMethodList.add("jumpInfoRecoveryPlan");
					staffMethodList.add("showDoctorDiagnosis");
					staffMethodList.add("searchDoctorDiagnosis");
					staffMethodList.add("infoDoctorDiagnosis");

					staffMethodList.add("getBloodSugerFromCloud");
					staffMethodList.add("bloodSugerDataAnalysis");
					staffMethodList.add("bloodSugerTendAnalysis");
					staffMethodList.add("bloodSugerDataDetail");
				}
				// "添加"权限判断
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {
					staffMethodList.add("addDiabeteManager");
					staffMethodList.add("addDiabeteConfirm");
					staffMethodList.add("addRecoveryPlan");
					staffMethodList.add("addPlan");
					staffMethodList.add("jsonChuan");
					staffMethodList.add("checkPlan");
					staffMethodList.add("addDoctorDiagnosis");
					staffMethodList.add("addDoctorDiagnosisAction");
				}
				// "修改"权限判断
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("toEditBSCloud");
					staffMethodList.add("editBSCloud");
					staffMethodList.add("changePlanStatus");
					staffMethodList.add("jumpEditPlan");
					staffMethodList.add("editRecoveryPlan");
					staffMethodList.add("editDoctorDiagnosis");
					staffMethodList.add("editDoctorDiagnosisAction");
				}
				// "删除"权限判断
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteDiabeteManager");
					staffMethodList.add("deletePlanById");
					staffMethodList.add("deleteDoctorDiagnosis");
					staffMethodList.add("deleteBloodSugerDataCloud");
				}
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("diabeteExport");
				}
				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}
			// /two_six 健康管理_其他慢病
			if (rightList.contains(two_six)
					& two_four_five_six_Action.contains(action)) {
				// 向员工中添加能够访问的方法
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), two_six);
				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("searchChronicPeople");
					staffMethodList.add("getAllOtherManager");
					staffMethodList.add("infoChronic");
					staffMethodList.add("showOldPlan");
					staffMethodList.add("searchRecoveryPlan");
					staffMethodList.add("jumpInfoRecoveryPlan");
					staffMethodList.add("showDoctorDiagnosis");
					staffMethodList.add("searchDoctorDiagnosis");
					staffMethodList.add("infoDoctorDiagnosis");

				}
				// "添加"权限判断
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {
					staffMethodList.add("addOtherManager");
					staffMethodList.add("addOtherConfirm");
					staffMethodList.add("addRecoveryPlan");
					staffMethodList.add("addPlan");
					staffMethodList.add("jsonChuan");
					staffMethodList.add("checkPlan");
					staffMethodList.add("addDoctorDiagnosis");
					staffMethodList.add("addDoctorDiagnosisAction");
				}
				// "修改"权限判断
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("jumpEditPlan");
					staffMethodList.add("editRecoveryPlan");
					staffMethodList.add("changePlanStatus");
					staffMethodList.add("editDoctorDiagnosis");
					staffMethodList.add("editDoctorDiagnosisAction");
				}
				// "删除"权限判断
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteOtherManager");
					staffMethodList.add("deletePlanById");
					staffMethodList.add("deleteDoctorDiagnosis");
				}
				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}
			// /two_seven 健康管理_中医治未病
			if (rightList.contains(two_seven)
					& two_seven_Action.contains(action)) {
				// 向员工中添加能够访问的方法
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), two_seven);
				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("getList");
					staffMethodList.add("getListByCX");
					staffMethodList.add("showDetail");

				}
//				// "添加"权限判断
//				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
//						.getValue()) {
//					staffMethodList.add("addOtherManager");
//					staffMethodList.add("addOtherConfirm");
//					staffMethodList.add("addRecoveryPlan");
//					staffMethodList.add("addPlan");
//					staffMethodList.add("jsonChuan");
//					staffMethodList.add("checkPlan");
//					staffMethodList.add("addDoctorDiagnosis");
//					staffMethodList.add("addDoctorDiagnosisAction");
//				}
//				// "修改"权限判断
//				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
//						.getValue()) {
//					staffMethodList.add("jumpEditPlan");
//					staffMethodList.add("editRecoveryPlan");
//					staffMethodList.add("changePlanStatus");
//					staffMethodList.add("editDoctorDiagnosis");
//					staffMethodList.add("editDoctorDiagnosisAction");
//				}
//				// "删除"权限判断
//				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
//						.getValue()) {
//					staffMethodList.add("deleteOtherManager");
//					staffMethodList.add("deletePlanById");
//					staffMethodList.add("deleteDoctorDiagnosis");
//				}
				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}
			// three 健康体检
			if (rightList.contains(three) & three_Action.contains(action)) {
				// 向员工中添加能够访问的方法
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), three);
				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("addsearchPhysicalExam");
					staffMethodList.add("showPhysicalExam");
					staffMethodList.add("firstIndexsearch");
					staffMethodList.add("allDoctor");
					staffMethodList.add("autoAction");
					staffMethodList.add("showPhysicalExamListBypersonal");
				}
				// "添加"权限判断
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {
					staffMethodList.add("addPhysicalExam");
					staffMethodList.add("jumpAddPhysicalExam");
				}
				// "修改"权限判断
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("jumpEditPhysicalExam");
					staffMethodList.add("editPhysicalExam");
				}
				// "删除"权限判断
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deletePhysicalExam");
				}
				// "导出"权限判断
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("searchMore");
					staffMethodList.add("fuction");
				}
				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// four_one 健康理疗-康复方案
			if (rightList.contains(four_one)
					&& four_one_two_three_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), four_one);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("getSchemes");
					staffMethodList.add("execute");
					staffMethodList.add("recoverySchemeDetails");
					staffMethodList.add("error");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {

					staffMethodList.add("addRecoveryScheme");
					staffMethodList.add("jumpAddScheme");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteScheme");
					staffMethodList.add("checkScheme");
					staffMethodList.add("deleteSchemeById");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
					staffMethodList.add("test");
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("test");

				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// four_two 健康理疗-康复计划
			if (rightList.contains(four_two)
					&& four_one_two_three_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), four_two);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("getAllPlan");
					staffMethodList.add("getRecoveryPlan");
					staffMethodList.add("getPlanById");
					staffMethodList.add("errorplan");
					staffMethodList.add("getAddOld");
					staffMethodList.add("getEditAddOld");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {

					staffMethodList.add("jumpAddPlan");
					staffMethodList.add("addPlan");
					staffMethodList.add("getAddOld");
					staffMethodList.add("getEditAddOld");
					staffMethodList.add("checkPlan");
					staffMethodList.add("jsonChuan");

				}
				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("changePlanStatus");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deletePlanId");
					staffMethodList.add("deletePlanById");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
					staffMethodList.add("function");
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("function");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// four_three 统计分析
			if (rightList.contains(four_three)
					&& four_one_two_three_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), four_three);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("getRecovery");
					staffMethodList.add("getRocoveryById");
					staffMethodList.add("getSearchRecovery");
					staffMethodList.add("errorStatistical");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deletestStisticalById");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("exportAnalysis");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// five_one 健康关怀-健康教育
			if (rightList.contains(five_one)
					&& five_one_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), five_one);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {

					staffMethodList.add("getHealthEducationBySearch");
					staffMethodList.add("getHealthEducationList");
					staffMethodList.add("getHealthEducationDetail");
					staffMethodList.add("downLoadFile");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {

					staffMethodList.add("saveHealthEducation");
					staffMethodList.add("addHealthEducation");
				}
				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {

					staffMethodList.add("updateHealthEducation");
					staffMethodList.add("editHealthEducation");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {

					staffMethodList.add("deleteHealthEducation");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
				    staffMethodList.add("test");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}

			}

			// five_two 健康关怀-上门随访
			if (rightList.contains(five_two)
					&& five_two_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), five_two);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("doorKnockingDetails");
					staffMethodList.add("getDoorKnockingListById");
					staffMethodList.add("getDoorknockingBySearch");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {

					staffMethodList.add("addDoorKnocking");
					staffMethodList.add("saveDoorKnocking");
				}

				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {

					staffMethodList.add("editDoorKnocking");
					staffMethodList.add("updateDoorKnocking");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteDoorKnocking");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
				    staffMethodList.add("test");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}

			}

			// five_three 健康关怀-知识讲座
			if (rightList.contains(five_three)
					&& five_three_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), five_three);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("knowledgeLectureDetails");
					staffMethodList.add("getKnowledgeLectureListById");
					staffMethodList.add("getKnowledgeLectureBySearch");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {

					staffMethodList.add("addKnowledgeLecture");
					staffMethodList.add("saveKnowledgeLecture");
				}

				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("editKnowledgeLecture");
					staffMethodList.add("updateKnowledgeLecture");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteKnowledgeLecture");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
				    staffMethodList.add("test");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}

			}

			// five_four 健康关怀-社区义诊
			if (rightList.contains(five_four)
					&& five_four_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), five_four);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("comminityClinicDetails");
					staffMethodList.add("getComminityClinicListById");
					staffMethodList.add("getComminityClinicBySearch");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {

					staffMethodList.add("addComminityClinic");
					staffMethodList.add("saveComminityClinic");
				}

				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("editComminityClinic");
					staffMethodList.add("updateComminityClinic");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteComminityClinic");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
				    staffMethodList.add("test");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// six_one 门诊记录-随诊记录
			if (rightList.contains(six_one) && six_one_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), six_one);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("searchFollowRecord");
					staffMethodList.add("showFollowRecord");
					staffMethodList.add("findFollowRecord");
					staffMethodList.add("turnToDetailFollowRecord");
					staffMethodList.add("ReOlder");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {

					staffMethodList.add("findAndAddFollowRecord");
					staffMethodList.add("addFollowRecord");
					staffMethodList.add("turnToAddPersonFollowRecord");
				}
				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("updateFollowRecord");
					staffMethodList.add("turnToUpdateFollowRecord");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteFollowRecord");
					staffMethodList.add("deleteMoreFollowRecord");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("fuction");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// six_two 门诊记录-接诊记录
			if (rightList.contains(six_two) && six_two_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), six_two);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("searchIncompleteRecord");
					staffMethodList.add("showIncompleteRecord");
					staffMethodList.add("findIncompleteRecord");
					staffMethodList.add("turnToDetailIncompleteRecord");
					staffMethodList.add("ReOlder");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {
					staffMethodList.add("findAndAddIncompleteRecord");
					staffMethodList.add("addIncompleteRecord");
					staffMethodList.add("turnToAddPersonIncompleteRecord");
				}

				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("updateIncompleteRecord");
					staffMethodList.add("turnToUpdateIncompleteRecord");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteIncompleteRecord");
					staffMethodList.add("deleteMoreIncompleteRecord");
				}

				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("fuction");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}

			}

			// six_two 门诊记录-接诊记录2
						if (rightList.contains(six_two) && six_two_Action2.contains(action)) {
							List<String> staffMethodList = new ArrayList<String>();
							// 根据员工Id和栏目编码获得权限值
							int columnValue = rightsService.getStaffValueByColumnCod(
									tstaff.getStaffId(), six_two);

							// 根据权限值判断能够访问的方法
							// "显示"权限判断
							if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
									.getValue()) {
								staffMethodList.add("searchIncompleteRecord");
								staffMethodList.add("showIncompleteRecord");
								staffMethodList.add("findIncompleteRecord");
								staffMethodList.add("turnToDetailIncompleteRecord");
								staffMethodList.add("ReOlder");
							}

							// 新增的权限
							if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
									.getValue()) {
								staffMethodList.add("findAndAddIncompleteRecord");
								staffMethodList.add("addIncompleteRecord");
								staffMethodList.add("turnToAddPersonIncompleteRecord");
							}

							// 修改的权限
							if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
									.getValue()) {
								staffMethodList.add("updateIncompleteRecord");
								staffMethodList.add("turnToUpdateIncompleteRecord");
							}

							// 删除的权限
							if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
									.getValue()) {
								staffMethodList.add("deleteIncompleteRecord");
								staffMethodList.add("deleteMoreIncompleteRecord");
							}

							// 打印的权限
							if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
									.getValue()) {
							}

							// 导出的权限
							if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
									.getValue()) {
								staffMethodList.add("fuction");
							}

							// 如果员工能够访问拦截的方法....go
							if (staffMethodList.contains(method)) {
								return invocation.invoke();
							}

						}
						// six_two 门诊记录-接诊记录
						if (rightList.contains(six_two) && six_two_Action.contains(action)) {
							List<String> staffMethodList = new ArrayList<String>();
							// 根据员工Id和栏目编码获得权限值
							int columnValue = rightsService.getStaffValueByColumnCod(
									tstaff.getStaffId(), six_two);

							// 根据权限值判断能够访问的方法
							// "显示"权限判断
							if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
									.getValue()) {
								staffMethodList.add("searchIncompleteRecord");
								staffMethodList.add("showIncompleteRecord");
								staffMethodList.add("findIncompleteRecord");
								staffMethodList.add("turnToDetailIncompleteRecord");
								staffMethodList.add("ReOlder");
							}

							// 新增的权限
							if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
									.getValue()) {
								staffMethodList.add("findAndAddIncompleteRecord");
								staffMethodList.add("addIncompleteRecord");
								staffMethodList.add("turnToAddPersonIncompleteRecord");
							}

							// 修改的权限
							if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
									.getValue()) {
								staffMethodList.add("updateIncompleteRecord");
								staffMethodList.add("turnToUpdateIncompleteRecord");
							}

							// 删除的权限
							if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
									.getValue()) {
								staffMethodList.add("deleteIncompleteRecord");
								staffMethodList.add("deleteMoreIncompleteRecord");
							}

							// 打印的权限
							if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
									.getValue()) {
							}

							// 导出的权限
							if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
									.getValue()) {
								staffMethodList.add("fuction");
							}

							// 如果员工能够访问拦截的方法....go
							if (staffMethodList.contains(method)) {
								return invocation.invoke();
							}

						}

			// six_three 门诊记录-会诊记录
			if (rightList.contains(six_three)
					&& six_three_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), six_three);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("searchConsultationRecord");
					staffMethodList.add("showConsultationRecord");
					staffMethodList.add("findConsultationRecord");
					staffMethodList.add("turnToDetailConsultationRecord");
					staffMethodList.add("ReOlder");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {

					staffMethodList.add("findAndAddConsultationRecord");
					staffMethodList.add("addConsultationRecord");
					staffMethodList.add("turnToAddPersonConsultationRecord");
				}

				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("updateConsultationRecord");
					staffMethodList.add("turnToUpdateConsultationRecord");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteConsultationRecord");
					staffMethodList.add("deleteMoreConsultationRecord");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("fuction");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// six_four 门诊记录-转诊记录
			if (rightList.contains(six_four)
					&& six_four_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), six_four);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("searchDualReferralRecord");
					staffMethodList.add("showDualReferralRecord");
					staffMethodList.add("findDualReferralRecord");
					staffMethodList.add("turnToDetailDualReferralRecord");
					staffMethodList.add("ReOlder");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {

					staffMethodList.add("findAndAddDualReferralRecord");
					staffMethodList.add("addDualReferralRecord");
					staffMethodList.add("turnToAddPersonDualReferralRecord");
				}
				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("updateDualReferralRecord");
					staffMethodList.add("turnToUpdateDualReferralRecord");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteDualReferralRecord");
					staffMethodList.add("deleteMoreDualReferralRecord");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("fuction");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}

			}

			// six_five 门诊记录-用药
			if (rightList.contains(six_five)
					&& six_five_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), six_five);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {

					staffMethodList.add("searchMedicalRecord");
					staffMethodList.add("showMedicalRecord");
					staffMethodList.add("findMedicalRecord");
					staffMethodList.add("turnToDetailMedicalRecord");
					staffMethodList.add("ReOlder");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {

					staffMethodList.add("findAndAddMedicalRecord");
					staffMethodList.add("addMedicalRecord");
					staffMethodList.add("turnToAddPersonMedicalRecord");
				}
				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("updateMedicalRecord");
					staffMethodList.add("turnToUpdateMedicalRecord");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteMedicalRecord");
					staffMethodList.add("deleteMoreMedicalRecord");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("fuction");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}

			}

			// six_six 门诊记录-家庭病床
			if (rightList.contains(six_six) && six_six_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), six_six);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {

					staffMethodList.add("searchFamilyBedHistory");
					staffMethodList.add("showFamilyBedHistory");
					staffMethodList.add("findFamilyBedHistory");
					staffMethodList.add("turnToDetailFamilyBedHistory");
					staffMethodList.add("ReOlder");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {

					staffMethodList.add("findAndAddFamilyBedHistory");
					staffMethodList.add("addFamilyBedHistory");
					staffMethodList.add("turnToAddPersonFamilyBedHistory");
				}
				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("updateFamilyBedHistory");
					staffMethodList.add("turnToUpdateFamilyBedHistory");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteFamilyBedHistory");
					staffMethodList.add("deleteMoreFamilyBedHistory");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("fuction");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// six_seven 门诊记录-住院记录
			if (rightList.contains(six_seven)
					&& six_seven_Action.contains(action)) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), six_seven);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("searchInHospitalHistory");
					staffMethodList.add("showInHospitalHistory");
					staffMethodList.add("findInHospitalHistory");
					staffMethodList.add("turnToDetailInHospitalHistory");
					staffMethodList.add("ReOlder");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {

					staffMethodList.add("findAndAddInHospitalHistory");
					staffMethodList.add("addInHospitalHistory");
					staffMethodList.add("turnToAddPersonInHospitalHistory");
				}
				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("updateInHospitalHistory");
					staffMethodList.add("turnToUpdateInHospitalHistory");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteInHospitalHistory");
					staffMethodList.add("deleteMoreInHospitalHistory");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("fuction");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}

			}

			// seven_one 员工信息管理
			if (rightList.contains(seven_one)
					& seven_one_two_Action.contains(action)) {
				// 向员工中添加能够访问的方法
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), seven_one);
				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("check");
					staffMethodList.add("search");
					staffMethodList.add("execute");
					staffMethodList.add("showStaffInfo");
					staffMethodList.add("updateStaff");
					staffMethodList.add("getAllStaffsBySQId");
				}
				// "添加"权限判断
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {
					staffMethodList.add("addStaff");
					staffMethodList.add("getKWHZD");
					staffMethodList.add("toAdd");
					staffMethodList.add("checkIdNum");
				}
				// "修改"权限判断
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("updateSave");
					staffMethodList.add("getKWHZDUpdate");
					staffMethodList.add("checkIdNum");
				}
				// "删除"权限判断
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteStaff");
				}

				// "导出"权限判断
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("test");
				}

				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// seven_two 员工请假管理
			if (rightList.contains(seven_two)
					& seven_one_two_Action.contains(action)) {
				// 向员工中添加能够访问的方法
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), seven_two);
				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("selectStaffLeaves");
					staffMethodList.add("getAllStaffLeaves");
					staffMethodList.add("updateStaffLeaveById");
					staffMethodList.add("showStaffLeave");
				}
				// "添加"权限判断
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {
					staffMethodList.add("addStaffLeave");
				}
				// "修改"权限判断
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("comeBack");
					staffMethodList.add("updateStaffLeaveSave");
				}
				// "删除"权限判断
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteStaffLeave");
				}
				// "导出"权限判断
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("function");
				}
				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// seven_three 员工奖惩管理
			if (rightList.contains(seven_three)
					& seven_three_Action.contains(action)) {
				// 向员工中添加能够访问的方法
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), seven_three);
				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("search");
					staffMethodList.add("staffRewardPunishDetail");
					staffMethodList.add("getAllStaffRewardPunish");
				}
				// "添加"权限判断
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {
					staffMethodList.add("toAdd");
					staffMethodList.add("getKWHZD");
					staffMethodList.add("addRewardPunish");
				}
				// "修改"权限判断
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("jumpUpdateStaffRewardPunish");
					staffMethodList.add("editstaffRewardPunish");
				}
				// "删除"权限判断
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deletestaffRewardPunish");
				}
				// "导出"权限判断
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
					staffMethodList.add("function");
				}
				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// seven_four 操作员管理
			if (rightList.contains(seven_four)
					& seven_four_Action.contains(action)) {
				// 向员工中添加能够访问的方法
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), seven_four);
				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("search");
					staffMethodList.add("getAllOperators");
					staffMethodList.add("getAllStaffExceptOperator");
					staffMethodList.add("getUpdateOperatorById");
					staffMethodList.add("getRoleList");
				}
				// "添加"权限判断
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {
					staffMethodList.add("addOperator");
					staffMethodList.add("getAllStaffExceptOperator");
					staffMethodList.add("getAllStaffExceptOperator2");
					
				}
				// "修改"权限判断
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("changeUperatorState");
					staffMethodList.add("recoverPassword");
					staffMethodList.add("updateOperatorSave");
				}
				// "删除"权限判断
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteOperator");
				}
				// 如果员工能够访问拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			}

			// nine 设备管理
			if ((rightList.contains(nine) && nine_Action.contains(action))) {
				List<String> staffMethodList = new ArrayList<String>();
				// 根据员工Id和栏目编码获得权限值
				int columnValue = rightsService.getStaffValueByColumnCod(
						tstaff.getStaffId(), nine);

				// 根据权限值判断能够访问的方法
				// "显示"权限判断
				if ((RightsValue.xianshi.getValue() & columnValue) == RightsValue.xianshi
						.getValue()) {
					staffMethodList.add("getEquipmentManageDetails");
					staffMethodList.add("getEquipmentManageListById");
					staffMethodList.add("getEquipmentManageBySearch");
					staffMethodList.add("getMaintenanceListById");
				}

				// 新增的权限
				if ((RightsValue.tianjia.getValue() & columnValue) == RightsValue.tianjia
						.getValue()) {

					staffMethodList.add("addEquipmentManage");
					staffMethodList.add("saveEquipmentManage");
					staffMethodList.add("addMaintence");
					staffMethodList.add("saveMaintence");
				}
				// 修改的权限
				if ((RightsValue.xiugai.getValue() & columnValue) == RightsValue.xiugai
						.getValue()) {
					staffMethodList.add("editEquipmentManage");
					staffMethodList.add("updateEquipmentManage");
					staffMethodList.add("updateMaintenance");
					staffMethodList.add("editMaintenance");
				}

				// 删除的权限
				if ((RightsValue.shanchu.getValue() & columnValue) == RightsValue.shanchu
						.getValue()) {
					staffMethodList.add("deleteEquipmentManage");
					staffMethodList.add("deletMaintenance");
				}
				// 打印的权限
				if ((RightsValue.dayin.getValue() & columnValue) == RightsValue.dayin
						.getValue()) {
				}

				// 导出的权限
				if ((RightsValue.daochu.getValue() & columnValue) == RightsValue.daochu
						.getValue()) {
				}

				// 如果员工能够访拦截的方法....go
				if (staffMethodList.contains(method)) {
					return invocation.invoke();
				}
			} else {
				// action或方法没有经过授权,被拦截
				return "error";
			}

		} // 员工拦截结束

		HttpServletRequest request = ServletActionContext.getRequest();
		String user = "";
		// 取cookie
		Cookie[] cookies = request.getCookies();// 读取Cookie,一般都会有多个
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equalsIgnoreCase("user")) {
					user = cookie.getValue(); // 得到cookie的用户名
				}
			}
		}
		if ("adminLogin".equals(user)) {
			return "adminLogin";
		}
		return "onLogin";

	}// 拦截方法结束
}
