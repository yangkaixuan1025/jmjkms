package com.jmjk.action;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.dialect.Ingres10Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleIfStatement.Else;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.jmjk.action.base.BaseAction;
import com.jmjk.entity.TCommunityHospital;
import com.jmjk.entity.TCommunityHospitalGroup;
import com.jmjk.entity.TFileLifeStyle;
import com.jmjk.entity.TFileMedicalRecord;
import com.jmjk.entity.THealthFile;
import com.jmjk.entity.TMaintainableEntry;
import com.jmjk.entity.TMedicalPayMethod;
import com.jmjk.entity.TOldStaffHos;
import com.jmjk.entity.TPastHistory;
import com.jmjk.entity.TRole;
import com.jmjk.entity.TStaff;
import com.jmjk.entity.view.VHealthStaff;
import com.jmjk.entity.view.VStaffHos;
import com.jmjk.entity.view.VStaffHosSu;
import com.jmjk.enums.Admin_IsHead;
import com.jmjk.export.healthFileExport;
import com.jmjk.pojo.HealthFileCloud;
import com.jmjk.pojo.HealthFileCloud.Page.HealthFile;
import com.jmjk.pojo.HealthFileCloudForDetail;
import com.jmjk.pojo.HealthFileCloudForDetail.Item;
import com.jmjk.pojo.HealthFileCloudForDetail.Item.FileLifeStyle;
import com.jmjk.pojo.HealthFileCloudForDetail.Item.FileMedicalRecord;
import com.jmjk.pojo.HealthFileCloudForDetail.Item.MedicalPayMethod;
import com.jmjk.pojo.HealthFileCloudForDetail.Item.PastHistory;
import com.jmjk.service.CommunityHospitalGroupService;
import com.jmjk.service.CommunityHospitalService;
import com.jmjk.service.FileLifeStyleService;
import com.jmjk.service.FileMedicalRecordService;
import com.jmjk.service.HealthFileService;
import com.jmjk.service.HealthManagerService;
import com.jmjk.service.MaintainableAttributeService;
import com.jmjk.service.MedicalPayMethodService;
import com.jmjk.service.PastHistoryService;
import com.jmjk.service.RoleService;
import com.jmjk.service.StaffService;
import com.jmjk.utils.AllInfoBean;
import com.jmjk.utils.HttpRequest;
import com.jmjk.utils.IDCardVerificationUtil;
import com.jmjk.utils.JxlExcel;
import com.jmjk.utils.MD5;
import com.jmjk.utils.Page;
import com.jmjk.utils.Resubmit;
import com.jmjk.utils.SearchBeanSU;
import com.jmjk.utils.TokenProccessor;
import com.jmjk.utils.TypeConverter;
import com.jmjk.utils.UploadAndDownload;
import com.opensymphony.xwork2.ActionContext;

@Component
@Scope("prototype")

@Action(value = "healthFileAction", results = { @Result(name = "success", location = "/success.jsp"),
		@Result(name = "showHealthFile", location = "/jsp/oldfile/healthFile/healthFileMain.jsp"),
		@Result(name = "showHealthFileInfo", location = "/jsp/oldfile/healthFile/healthFileMainInfo.jsp"),
		@Result(name = "searchHealthFile", location = "/jsp/oldfile/healthFile/healthFileMain.jsp"),
		@Result(name = "searchHealthFileInfo", location = "/jsp/oldfile/healthFile/healthFileMainInfo.jsp"),
		@Result(name = "searchHealthFileKill", location = "/jsp/oldfile/healthFile/healthFileMainPlus.jsp"),
		@Result(name = "searchHealthFileKillInfo", location = "/jsp/oldfile/healthFile/healthFileMainPlusInfo.jsp"),
		@Result(name = "error", location = "/error.jsp"),
		@Result(name = "testBack", location = "/jsp/oldfile/healthFile/test.jsp"),
		@Result(name = "turnToAddHealthFile", location = "/jsp/oldfile/healthFile/healthFileAdd.jsp"),
		@Result(name = "turnToAddHealthFileForSpecial", location = "/jsp/oldfile/healthFile/healthFileForSpecial.jsp"),
		@Result(name = "addHealthFile", type = "redirectAction", location = "healthFileAction!showHealthFile.action"),
		@Result(name = "turnToHealthFileDetil", location = "/jsp/oldfile/healthFile/healthFileDetil.jsp"),
		@Result(name = "turnToHealthFileDetilInfo", location = "/jsp/oldfile/healthFile/healthFileDetilInfo.jsp"),
		@Result(name = "turnToHealthFileView", location = "/jsp/oldfile/healthFile/healthFileView.jsp",params = {"embeded","${embeded}"}),
		@Result(name = "turnToHealthFileViewInfo", location = "/jsp/oldfile/healthFile/healthFileViewInfo.jsp"),
		@Result(name = "updateHealthFile", type = "redirectAction", location = "healthFileAction!showHealthFile.action"),
		@Result(name = "deleteHealthFile", type = "redirectAction", location = "healthFileAction!showHealthFile.action"),
		@Result(name = "DocDRedir", type = "redirectAction", location = "healthFileAction!showHealthFile.action"),
		@Result(name = "cancelHealthFile", type = "redirectAction", location = "healthFileAction!showHealthFile.action"),
		@Result(name = "input", type = "redirectAction", location = "healthFileAction!showHealthFile.action", params = {
				"X", "${X}", "Y", "${Y}", "egg", "${egg}" }),
		@Result(name = "killHealthFile", type = "redirectAction", location = "healthFileAction!showHealthFile.action"),
		
		@Result(name = "errorAddHealthFile", type = "redirectAction", location = "healthFileAction!turnToAddHealthFile.action" ),
		@Result(name = "showHealthFileHealthAdvice", location = "/jsp/healthManager/healthFileViewInfo.jsp"),
		@Result(name = "getHealthFileList", location = "/jsp/healthManager/healthFilelist.jsp")})


public class HealthFileAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	FileMedicalRecordService fileMedicalRecordService;
	@Autowired
	HealthManagerService healthManagerService;
	@Autowired
	FileLifeStyleService  fileLifeStyleService;
	@Autowired
	HealthFileService healthFileService;
	@Autowired
	CommunityHospitalService communityHospitalService;
	@Autowired
	MedicalPayMethodService medicalPayMethodService;
	@Autowired
	StaffService staffService;
	@Autowired
	PastHistoryService pastHistoryService;
	@Autowired
	MaintainableAttributeService maintainableAttributeService;
	@Autowired
	CommunityHospitalGroupService communityHospitalGroupService;
	@Autowired
	RoleService roleService;
	
	private String yongLiangCiShu;
	private String meiCiYongLiang;
	private int belongSystemQianDuan;
	private String token;
	private List<TStaff> staffs;
	private List<VHealthStaff> vhealthStaffList;
	// private SearchBean searchbean;//（多条件查询）
	private SearchBeanSU searchbean;// （多条件查询）
	private boolean flag = false;
	private boolean flagPingTai = false;
	private List<TPastHistory> pastHistoryList;
	private List<PastHistory> pastList;
	private List<THealthFile> healthFileList;
	private TPastHistory pasthistory;
	Page<THealthFile> page;
	Page<VHealthStaff> pagePlus;
	int hosId;// =4;//医院Id
	int state = 2;// 档案状态
	private String peopleString;
	private String yemianname;
	private TCommunityHospital communityHospital;
	private int cp = 1;
	private int cp1 = 1;//用于多条件查询
	private int egg = -1;
	private String pageHtml;
	private String hosName;
	private List<Object> choice;
	private List<String> choiceBack;
	private THealthFile healthFile;
	private THealthFile healthFile1;
	private String result;
	private List<String> beginList;
	private List<String> disabilityNameList;// 残疾名称
	private List<String> allergyNameList;// 过敏名称
	private List<String> exposeNameList;// 暴露名称
	private List<Object> choiceExposeName;
	private List<Object> choiceDisabilityName;
	private List<Object> choiceAllergyName;
	private List<Object> choiceFather;
	private String choiceResultAllergyName;
	private String choiceResultExposeName;
	private String choiceResultDisability;
	private String choiceResultFather;
	private String choiceResultMother;
	private String choiceResultBroSis;
	private String choiceResultChild;
	private List<Object> choiceMother;
	private List<Object> choiceBroSis;
	private List<Object> choiceChild;
	private List<Object> disabilityName;
	private List<VStaffHos> allDoctor;
	private List<VStaffHos> allEnterPeople;
	private VStaffHos vstaffHos;
	private VStaffHosSu vstaffHosSu;
	private int DoctorId;
	private int intId;
	private List<TCommunityHospital> allHos;
	private String fileNum;
	private String idNum;
	private String iphone;
	private String doctorName;
	private int autoId;
	private TMedicalPayMethod medicalPayMethod;
	private List<TMedicalPayMethod> medicalPayMethodList;
	private List<MedicalPayMethod> payMethodList;
	private List<String> medicalPayMethodName;
	private List<String> medicalPayMethodCardNum;
	private int healthFileId;
	private int indexFlag=0;
	private TOldStaffHos oldStaffHos;
	private TStaff tstaff;
	private List<TStaff> specialDoctor;
	private List<String> medicalPayMethodNameList;
	private List<String> medicalPayMethodCardNumList;
	private String str;
	private List<TPastHistory> pastHistoryShouShuList;
	private List<TPastHistory> pastHistoryWaiShangList;
	private List<TPastHistory> pastHistoryShuXieList;
	private List<PastHistory> pastShouShuList;
	private List<PastHistory> pastWaiShangList;
	private List<PastHistory> pastShuXieList;
	private List<PastHistory> pastAllList;
	private List<Timestamp> pastHistoryShouShuTimeList;
	private List<String> pastHistoryShouShuNameList;
	private List<Timestamp> pastHistoryWaiShangTimeList;
	private List<String> pastHistoryWaiShangNameList;
	private List<Timestamp> pastHistoryShuXieTimeList;
	private List<String> pastHistoryShuXieNameList;
	private List<Timestamp> diseaseTime;
	private List<String> diseaseName;
	private List<TOldStaffHos> wocao;
	private int conFlag;
	private int choice1[];
	private String picStr;
	private int issq = 2;// 判断是否是平台管理员的状态量，用于是否显示社区医院搜索医院选项
	private List<TCommunityHospital> hospitals; // 社区医院list
	private List<TCommunityHospitalGroup> groups;
	

	// 上传照片
	private File upload;
	private String uploadFileName;
	private String savaPartPath = "/images/";
	// 导入健康档案
	private List<AllInfoBean> allInfoBeanList;
	private int X;
	private int Y;
	private int xy[] = new int[2];
	
	//一体机注册校验
	private String uname;
	private String rname;
	private String password;
	private String idcard;
	private String nfc;
	private boolean sex;
	private String height;
	private String weight;
	private Date birthday;
	private String yaowei;
	private String tunwei;
	private String results;
	//增加的子表：生活方式
	private TFileLifeStyle tlifestyle;
	private List<TFileLifeStyle>  tlifestyleList;
	private List<HealthFile> healthFileCloudList;
	private List<FileLifeStyle>  lifestyleList;
	private FileLifeStyle lifestyle;
	//增加的子表：用药记录
	private List<TFileMedicalRecord>  fileMedicalRecordList;
	private List<TFileMedicalRecord>  fileMedicalList;
	private List<FileMedicalRecord> MedicalRecordList;
	private List<String>  fileMedicalListName;
	private List<String>  fileMedicalListUsages;
	private List<String>  fileMedicalListDosage;
	private List<String>  fileMedicalListMedicationAdherence;
	private List<String>  fileMedicalListUntowardEffect;
	private List<String>  fileMedicalListDrugSources;
	private List<Timestamp>  fileMedicalListBegin;
	private List<Timestamp>  fileMedicalListStop;
	private TFileMedicalRecord fileMedicalRecord;
	//从服务器拿健康档案
	private int healthCloudId;
	private Item healthFileCloudSingle;
	private String flagSearch;//健康建议，首页多条件查询调同一个查询方法标记
	
	//用于判断是否嵌入到门诊接诊记录页面 false:没有嵌入，true:嵌入
	private boolean embeded = false;
	
	// 导入健康档案
	public String input() throws Exception {
		// 得到医院id
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session.getAttribute("comHospital") != null) {
			communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
			hosId = communityHospital.getCommunityHospitalId();
		} else if (session.getAttribute("tstaff") != null) {
			tstaff = (TStaff) session.getAttribute("tstaff");
			hosId = staffService.getCommHospitalByStaffId(tstaff.getStaffId());
			communityHospital = communityHospitalService.getCommunityHospitalById(hosId);

		}
		// 检查是否是excel格式
		if(uploadFileName==null){
			egg = -4;//没有选择导入文件
			return "input";
		}
		if(!uploadFileName.contains(".")){
			egg = -2;
			return "input";
		}
		String fileType = uploadFileName.substring(uploadFileName.lastIndexOf("."));
		List<String> allowTypesList = new ArrayList<String>();
		allowTypesList.add(".xls");
		allowTypesList.add(".xlsx");
		if (!allowTypesList.contains(fileType)) {
			egg = -2;
			return "input";
		}
		// 检测医院内是否有员工编号为0的员工，若没有，执行下面语句，添加员工编号为0 的员工，使其称为导入老人的责任医生
		if (healthFileService.checkStaff0(hosId)) {
			TStaff staff = new TStaff();
			TOldStaffHos oldStaffHos = new TOldStaffHos();
			staff.setStaffNumber("0");
			staff.setState("2");
			staff.setName("0");
			staff.setIdCardNum("0");
			staff.setDepartment("0");
			staff.setJob("0");
			staff.setPhone("0");
			oldStaffHos.setTCommunityHospital(communityHospital);
			oldStaffHos.setTStaff(staff);
			// 添加员工，并且添加到oldStaffHos关系表
			healthFileService.addStaffDefault(staff, oldStaffHos);
		}

		JxlExcel jxl = new JxlExcel();
		if (upload != null) {
			if(jxl.getExcelByClient(upload)==null){
				egg = -3;
				return "input";
			}
			allInfoBeanList = jxl.getExcelByClient(upload);
		} else {
			return "input";
		}
		if(allInfoBeanList.size()==0){
			egg = -3;
			return "input";
		}
		if (allInfoBeanList.get(allInfoBeanList.size() - 1).getX() != -1
				|| allInfoBeanList.get(allInfoBeanList.size() - 1).getY() != -1) {
			X = allInfoBeanList.get(allInfoBeanList.size() - 1).getX();
			Y = allInfoBeanList.get(allInfoBeanList.size() - 1).getY();
			xy[0] = X;
			xy[1] = Y;
			return "input";
		}
		for (int i = 0; i < allInfoBeanList.size(); i++) {
			THealthFile healthFile = new THealthFile();
			healthFile = allInfoBeanList.get(i).getHeathFile();
			healthFile.setWorkUnit(communityHospital.getCommunityHospital());
			healthFile.setHospital(communityHospital.getCommunityHospital());
			healthFile.setCurrentOffice("未知");
			healthFile.setFilePerson("未知");
			healthFile.setIdNum("123");
			healthFile.setHealthFileType("2");
			if (healthFile.getFileNum().equals("")) {
				healthFile.setFileNum(healthFileService.getFileNum(hosId));
			}
			healthFileId = healthFileService.getAutoIdBySaveHealthFile(healthFile);// 添加健康档案
			healthFile = healthFileService.getHealthFileById(healthFileId);// 得到刚才保存的健康档案
			// 把导入的健康档案添加到oldStaffHos关系表里
			oldStaffHos = healthFileService.getOldStaffHos0(hosId);//返回该医院员工编号为0的TOldStaffHos实体
			TStaff staff = new TStaff();
			staff = oldStaffHos.getTStaff();//导入的老人的责任医生默认为员工编号为0的医生
			TOldStaffHos oldStaffHos11 = new TOldStaffHos();
			oldStaffHos11.setTHealthFile(healthFile);// !!!
			oldStaffHos11.setTCommunityHospital(communityHospital);
			oldStaffHos11.setTStaff(staff);
			healthFileService.addOldStaffHos0(oldStaffHos11);

			medicalPayMethodList = new ArrayList<TMedicalPayMethod>();
			medicalPayMethod = new TMedicalPayMethod();
			medicalPayMethod = allInfoBeanList.get(i).getMedicalPay();
			medicalPayMethod.setTHealthFile(healthFile);
			medicalPayMethodList.add(medicalPayMethod);
			medicalPayMethodService.saveMedicalPayMethod(medicalPayMethodList);

			pasthistory = new TPastHistory();
			List<TPastHistory> pastHistoryList = allInfoBeanList.get(i).getPasts();
			for (int j = 0; j < pastHistoryList.size(); j++) {
				pastHistoryList.get(j).setTHealthFile(healthFile);
				pastHistoryService.savePastHistory(pastHistoryList.get(j));
			}

		}
		egg = 0;// 表示成功导入
		return "input";
	}

	public String fuction() throws Exception {
		if (conFlag == 1) {// 导出本页
			List<healthFileExport> conList = new ArrayList<healthFileExport>();
			if (choice1 != null) {
				for (int i = 0; i < choice1.length; i++) {
					healthFile = healthFileService.getHealthFileById(choice1[i]);
					if (healthFile != null) {
						healthFileExport ct = new healthFileExport(healthFile);
						conList.add(ct);
					}
				}
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("content-disposition", "attachment;filename=" + 4 + ".xls");
			ServletOutputStream output = response.getOutputStream();
			healthFileExport ct = new healthFileExport();
			String[] titles = ct.getTiles();
			JxlExcel he = new JxlExcel(titles);
			he.write(conList, output);
			response.flushBuffer();
		}

		if (conFlag == 2) {// 导出全部
			List<healthFileExport> conList = new ArrayList<healthFileExport>();
			String[] titles = (new healthFileExport()).getTiles();
			HttpSession session = ServletActionContext.getRequest().getSession();
			if (session.getAttribute("comHospital") != null) {
				communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
				if (communityHospital.getIsHead() == Admin_IsHead.guanliyuan.getValue()) {
					healthFileList = healthFileService.getAllHealthFile(state);
				} else {
					healthFileList = healthFileService.getAllHealthFile(state,
							communityHospital.getCommunityHospital());
				}
			} else if (session.getAttribute("tstaff") != null) {
				tstaff = (TStaff) session.getAttribute("tstaff");
				hosId = staffService.getCommHospitalByStaffId(tstaff.getStaffId());
				if (healthFileService.getOldStaffHosByStaffId(tstaff.getStaffId())) {// 判断员工是否为责任医生，若是，则只能看到所对应的老人
					healthFileList = healthFileService.getHealthFileListByStaffIdForSpeciall(state,
							tstaff.getStaffId());
				} else {
					healthFileList = healthFileService.getAllHealthFile(state,
							communityHospital.getCommunityHospital());
				}
			}
			for (THealthFile healthFile : healthFileList) {
				if (healthFile != null) {
					healthFileExport cExport = new healthFileExport(healthFile);// *
					conList.add(cExport);
				}
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("content-disposition", "attachment;filename=" + 4 + ".xls");
			ServletOutputStream output = response.getOutputStream();
			JxlExcel he = new JxlExcel(titles);
			he.write(conList, output);
			response.flushBuffer();
		}

		System.out.println("ceshji");
		return null;
	}
	/**
	 * 获取责任医生可以[责任医生，多条件查询]查询的健康档案的分页信息
	 * @param searchbean
	 * @param hosId 医院id
	 * @return
	 */
	public HealthFileCloud getHealthPageForManyForSpecial(SearchBeanSU searchbean ,int docId){
		String resultJson = healthFileService.getHealthFileCloudForManySpecial(searchbean,docId, 1, 10);
		return JSON.parseObject(resultJson,HealthFileCloud.class);
	}
	/**
	 * 获取责任医生可以[社区负责任，多条件查询]查询的健康档案的分页信息
	 * @param searchbean
	 * @param hosId 医院id
	 * @return
	 */
	public HealthFileCloud getHealthPageForManyForAll(SearchBeanSU searchbean ,int hosId){
		String resultJson = healthFileService.getHealthFileCloudForMany(searchbean,hosId, 1, 10);
		return JSON.parseObject(resultJson,HealthFileCloud.class);
	}
	/**
	 * 查询默认的页面的老人档案列表，【多条件查询健康档案】
	 * 
	 * @return
	 */
	public List<HealthFile> getHealthFileListInfoForMany(Page pagePlus,SearchBeanSU searchbean) {
		
		int userId = 1;
		HttpSession session = ServletActionContext.getRequest().getSession();
		//责任医生
		TStaff tStaff = (TStaff) session.getAttribute("tstaff");
		//TStaff tStaff = (TStaff) ActionContext.getContext().getSession().get("tstaff");
		//社区负责人
		TCommunityHospital tCommunityHospital =(TCommunityHospital) session.getAttribute("comHospital");
		//TCommunityHospital tCommunityHospital = (TCommunityHospital) ActionContext.getContext().getSession().get("comHospital");
		if(tStaff != null){
			userId = tStaff.getStaffId();
			//userId = 1; //测试专用，运行阶段删除
			//HealthFileCloud healthFileCloud = (HealthFileCloud) ActionContext.getContext().getSession().get("healthFileCloudDocForMany");
			//if(healthFileCloud == null){
			HealthFileCloud healthFileCloud = getHealthPageForManyForSpecial(searchbean ,userId);
			//	if(healthFileCloud.getPage() != null){
			//		ActionContext.getContext().getSession().put("healthFileCloudDocForMany", healthFileCloud);
			//	}
			//}
			if(healthFileCloud.getPage() != null){
				pagePlus.setPageSize(healthFileCloud.getPage().getPageSize());
				pagePlus.setTotal(healthFileCloud.getPage().getTotal());
				pagePlus.setTotalNum(healthFileCloud.getPage().getTotal());
				String resultJson = healthFileService.getHealthFileCloudForManySpecial(searchbean , userId,pagePlus.getCurrentPage(),pagePlus.getPageSize());
				healthFileCloud = JSON.parseObject(resultJson,HealthFileCloud.class);
				healthFileCloudList = healthFileCloud.getPage().getResult();
			}
			}else if(tCommunityHospital != null){
			//userId = tCommunityHospital.getCommunityHospitalId();--运行时撤销注释
			hosId=tCommunityHospital.getCommunityHospitalId();
			HealthFileCloud healthFileCloud = getHealthPageForManyForAll(searchbean , hosId);
			//HealthFileCloud healthFileCloud = (HealthFileCloud) ActionContext.getContext().getSession().get("healthFileCloudHosForMany");
			/*if(healthFileCloud == null){
				healthFileCloud = getHealthPageForManyForAll(searchbean , hosId);
				if(healthFileCloud.getPage() != null){
					ActionContext.getContext().getSession().put("healthFileCloudHosForMany", healthFileCloud);
				}
			}*/
			if(healthFileCloud.getPage() != null){
				pagePlus.setPageSize(healthFileCloud.getPage().getPageSize());
				pagePlus.setTotal(healthFileCloud.getPage().getTotal());
				pagePlus.setTotalNum(healthFileCloud.getPage().getTotal());
				String resultJson = healthFileService.getHealthFileCloudForMany( searchbean , hosId,pagePlus.getCurrentPage(),pagePlus.getPageSize());
				healthFileCloud = JSON.parseObject(resultJson,HealthFileCloud.class);
				healthFileCloudList = healthFileCloud.getPage().getResult();
			}
		}
		return healthFileCloudList;
	}
	/**
	 * 多条件查询
	 * @return
	 */
	public String searchHealthFile() {
		pagePlus = new Page<VHealthStaff>();
		pagePlus.setCurrentPage(cp1);
		HttpSession session = ServletActionContext.getRequest().getSession();
		TCommunityHospitalGroup group=(TCommunityHospitalGroup) session.getAttribute("communityHospitalGroup");
		if(group!=null){//集团负责人登陆
			hospitals = communityHospitalGroupService.getComHosListByGroup(group);
			searchbean.setShenfen(0);
			if(searchbean.getSqid()==0){
				searchbean.setShenfen(4);//假定为平台管理员登陆，查自己的集团
				searchbean.setSqid(group.getGId());
			}
			searchbean.setSqid(searchbean.getSqid());
			issq = 1;//0是平台管理员登陆，1是集团登陆，2是其他登陆 
			pagePlus.setTotalNum(healthFileService.findChronicPeopleCount(searchbean));
			this.pageHtml = pagePlus.getPageForChronic("entity");
			vhealthStaffList = healthFileService.findChronicPeople(searchbean, pagePlus);
			healthFileList = new ArrayList<THealthFile>();
			if (vhealthStaffList.size() > 0) {
				for (int i = 0; i < vhealthStaffList.size(); i++) {
					healthFileList
							.add(healthFileService.getHealthFileById(vhealthStaffList.get(i).getHealthFileId()));
				}
			}
			flagPingTai=true;//当flagPingTai==true时，会隐藏多条件查询里的录入人、责任医生等选项
			indexFlag=1;
			if(searchbean.getDanganzhuangtai()!=0){
				return "searchHealthFileKill";
			}
			else{
				return "searchHealthFile";
			}
		}
		if (session.getAttribute("comHospital") != null) {
			communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
			// communityHospital=communityHospitalService.getCommunityHospitalById(hosId);

			if (communityHospital.getIsHead() == Admin_IsHead.guanliyuan.getValue()) {//系统管理员
				groups = communityHospitalGroupService.getAllGroups();
				searchbean.setShenfen(4);
				issq = 0; //0是平台管理员登陆，1是集团登陆，2是其他登陆 
				pagePlus.setTotalNum(healthFileService.findChronicPeopleCount(searchbean));
				this.pageHtml = pagePlus.getPageForChronic("entity");
				vhealthStaffList = healthFileService.findChronicPeople(searchbean, pagePlus);
				healthFileList = new ArrayList<THealthFile>();
				if (vhealthStaffList.size() > 0) {
					for (int i = 0; i < vhealthStaffList.size(); i++) {
						healthFileList
								.add(healthFileService.getHealthFileById(vhealthStaffList.get(i).getHealthFileId()));
					}
				}
				allDoctor = healthFileService.getAllDoctorByHosId();
				allEnterPeople = staffService.getAllOperator();
				flagPingTai = true;
				indexFlag=1;
				if (searchbean.getDanganzhuangtai() != 0) {
					return "searchHealthFileKill";
				} else {
					return "searchHealthFile";
				}

			} else {//社区负责人
				hosName = communityHospital.getCommunityHospital();
				state=(int) healthFileService.findChronicPeopleCount(searchbean, hosName);
				
				//不要删出以下注释，以下是从本地数据库读取数据
				/*pagePlus.setTotalNum(healthFileService.findChronicPeopleCount(searchbean, hosName));
				vhealthStaffList = healthFileService.findChronicPeople(searchbean, pagePlus, hosName);
				healthFileList = new ArrayList<THealthFile>();
				if (vhealthStaffList.size() > 0) {
					for (int i = 0; i < vhealthStaffList.size(); i++) {
						int j = vhealthStaffList.get(i).getHealthFileId();
						healthFile = healthFileService.getHealthFileById(vhealthStaffList.get(i).getHealthFileId());
						healthFileList
								.add(healthFileService.getHealthFileById(vhealthStaffList.get(i).getHealthFileId()));
					}
				}*/
				allDoctor = healthFileService.getAllDoctorByHosId(communityHospital.getCommunityHospitalId());
				allEnterPeople = staffService.getAllOperator(communityHospital.getCommunityHospitalId());
				specialDoctor =healthFileService.getSpeciallDoctor(communityHospital.getCommunityHospitalId());
				//staffs = staffService.getStaffByCommunityHospitalId(communityHospital.getCommunityHospitalId());
				staffs=healthFileService.getStaffs(communityHospital.getCommunityHospitalId());
				//以下是从服务器读取数据
				healthFileCloudList= getHealthFileListInfoForMany(pagePlus, searchbean);
				this.pageHtml = pagePlus.getPageForChronic("entity");
				if(flagSearch!=null){
					return "getHealthFileList";
				}
				if (searchbean.getDanganzhuangtai() != 2) {//对于从服务器拿数据来说，2是正常状态
					return "searchHealthFileKillInfo";
				} else {
					return "searchHealthFileInfo";
				}
			}
		} else if (session.getAttribute("tstaff") != null) {
			tstaff = (TStaff) session.getAttribute("tstaff");
			hosId = staffService.getCommHospitalByStaffId(tstaff.getStaffId());
			communityHospital = communityHospitalService.getCommunityHospitalById(hosId);
			// 判断员工是否为责任医生，若是，则只能看到所对应的老人
			if (healthFileService.getOldStaffHosByStaffId(tstaff.getStaffId())) {//是责任医生
				//不要删出以下注释，以下是从本地数据库读取数据
				/*pagePlus.setTotalNum(healthFileService.findChronicPeopleCount(searchbean, tstaff.getStaffId()));
				vhealthStaffList = healthFileService.findChronicPeople(searchbean, pagePlus, tstaff.getStaffId());
				healthFileList = new ArrayList<THealthFile>();
				if (vhealthStaffList.size() > 0) {
					for (int i = 0; i < vhealthStaffList.size(); i++) {
						healthFileList
								.add(healthFileService.getHealthFileById(vhealthStaffList.get(i).getHealthFileId()));
					}
				}*/
				//以下是从服务器读取数据
				healthFileCloudList= getHealthFileListInfoForMany(pagePlus, searchbean);
				
				allDoctor = healthFileService.getAllDoctorByHosId(hosId);
				allEnterPeople = staffService.getAllOperator(hosId);
				staffs = healthFileService.getStaffs(hosId);
				flag = true;//隐藏多条件查询里的责任医生、录入人等选项（责任医生登陆）
				indexFlag=1;
				this.pageHtml = pagePlus.getPageForChronic("entity");
				if(flagSearch!=null){
					return "getHealthFileList";
				}
				if (searchbean.getDanganzhuangtai() != 2) {//对于从服务器拿数据来说，2是正常状态
					return "searchHealthFileKillInfo";
				} else {
					return "searchHealthFileInfo";
				}
			} else {
				hosName = communityHospital.getCommunityHospital();
				pagePlus.setTotalNum(healthFileService.findChronicPeopleCount(searchbean, hosName));
				vhealthStaffList = healthFileService.findChronicPeople(searchbean, pagePlus, hosName);
				healthFileList = new ArrayList<THealthFile>();
				if (vhealthStaffList.size() > 0) {
					for (int i = 0; i < vhealthStaffList.size(); i++) {
						healthFileList
								.add(healthFileService.getHealthFileById(vhealthStaffList.get(i).getHealthFileId()));
					}
				}
				allDoctor = healthFileService.getAllDoctorByHosId(hosId);
				specialDoctor =healthFileService.getSpeciallDoctor(hosId);
				allEnterPeople = staffService.getAllOperator(hosId);
				staffs =healthFileService.getStaffs(hosId);
				this.pageHtml = pagePlus.getPageForChronic("entity");
				if (searchbean.getDanganzhuangtai() != 0) {
					return "searchHealthFileKill";
				} else {
					return "searchHealthFile";
				}
			}

			
		} else {
			return "error";
		}
	}
	

	/**
	 * 获取责任医生可以查询的健康档案的分页信息
	 * @param doctorId
	 * @return
	 */
	public HealthFileCloud getHealthFilePageInfoByDoctor(int doctorId){
		
		String resultJson = healthManagerService.getHealthFileFromCloudByDoctor(doctorId, 1, 10);
		return JSON.parseObject(resultJson,HealthFileCloud.class);
	}
	/**
	 * 获取社区医院可以查询的健康档案的分页信息
	 * @param hosId
	 * @return
	 */
	public HealthFileCloud getHealthFilePageInfoByHos(int hosId){
		String resultJson = healthManagerService.getHealthFileFromCloudByHos(hosId, 1, 10);
		return JSON.parseObject(resultJson,HealthFileCloud.class);
	}
	/**
	 * 查询默认的页面的老人档案列表，【进入健康采集首页】
	 * 
	 * @return
	 */
	public List<HealthFile> getHealthFileListInfo(Page page) {
		
		int userId = 1;
		//责任医生
		TStaff tStaff = (TStaff) ActionContext.getContext().getSession().get("tstaff");
		//社区负责人
		TCommunityHospital tCommunityHospital = (TCommunityHospital) ActionContext.getContext().getSession().get("comHospital");
		if(tStaff != null){
			userId = tStaff.getStaffId();
			//userId = 1; //测试专用，运行阶段删除
			//HealthFileCloud healthFileCloud = (HealthFileCloud) ActionContext.getContext().getSession().get("healthFileCloudDoc");
			//if(healthFileCloud == null){
			HealthFileCloud healthFileCloud = getHealthFilePageInfoByDoctor(userId);
			//	if(healthFileCloud.getPage() != null){
			//		ActionContext.getContext().getSession().put("healthFileCloudDoc", healthFileCloud);
			//	}
			//}
			if(healthFileCloud.getPage() != null){
				page.setPageSize(healthFileCloud.getPage().getPageSize());
				page.setTotal(healthFileCloud.getPage().getTotal());
				page.setTotalNum(healthFileCloud.getPage().getTotal());
				String resultJson = healthManagerService.getHealthFileFromCloudByDoctor(userId, page.getCurrentPage(), page.getPageSize());
				healthFileCloud = JSON.parseObject(resultJson,HealthFileCloud.class);
				healthFileCloudList = healthFileCloud.getPage().getResult();
			}
		}else if(tCommunityHospital != null){
			//userId = tCommunityHospital.getCommunityHospitalId();--运行时撤销注释
			hosId=tCommunityHospital.getCommunityHospitalId();
			//HealthFileCloud healthFileCloud = (HealthFileCloud) ActionContext.getContext().getSession().get("healthFileCloudHos");
			//if(healthFileCloud == null){
				HealthFileCloud healthFileCloud = getHealthFilePageInfoByHos(hosId);
			//	if(healthFileCloud.getPage() != null){
			//		ActionContext.getContext().getSession().put("healthFileCloudHos", healthFileCloud);
			//	}
			//}
			if(healthFileCloud.getPage() != null){
				page.setPageSize(healthFileCloud.getPage().getPageSize());
				page.setTotal(healthFileCloud.getPage().getTotal());
				page.setTotalNum(healthFileCloud.getPage().getTotal());
				String resultJson = healthManagerService.getHealthFileFromCloudByHos(hosId, page.getCurrentPage(), page.getPageSize());
				healthFileCloud = JSON.parseObject(resultJson,HealthFileCloud.class);
				healthFileCloudList = healthFileCloud.getPage().getResult();
			}
		}
		return healthFileCloudList;
	}
	public String showHealthFile() {
		if(roleService.getRoleByHosIdForSu(0)==null){
			TRole role=new TRole();
			role.setCommunityId(0);
			role.setRoleName("11");
			roleService.addRoleForSu(role);
		};
		page = new Page<THealthFile>();
		page.setCurrentPage(cp);
		/*healthFileCloudList=getHealthFileListInfo(page);
		this.pageHtml = page.getPage("healthFileAction!showHealthFile.action");
		return "showHealthFileInfo";*/
		HttpSession session = ServletActionContext.getRequest().getSession();
		TCommunityHospitalGroup group=(TCommunityHospitalGroup) session.getAttribute("communityHospitalGroup");
		if(group!=null){//集团负责人登陆
			issq = 1;//0是平台管理员登陆，1是集团登陆，2是其他登陆
			hospitals = communityHospitalGroupService.getComHosListByGroup(group);
			page.setTotalNum(healthFileService.getHealthFileCountForGroup(group.getGId()));
			this.pageHtml = page.getPage("healthFileAction!showHealthFile.action");
			vhealthStaffList=healthFileService.getAllHealthFileForGroup(page, group.getGId(), state);
			healthFileList = new ArrayList<THealthFile>();
			if (vhealthStaffList.size() > 0) {
				for (int i = 0; i < vhealthStaffList.size(); i++) {
					healthFileList
							.add(healthFileService.getHealthFileById(vhealthStaffList.get(i).getHealthFileId()));
				}
			}
			flagPingTai = true;
			indexFlag=1;
			return "showHealthFile";
		}
		if (session.getAttribute("comHospital") != null) {
			communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
			// communityHospital=communityHospitalService.getCommunityHospitalById(hosId);

			if (communityHospital.getIsHead() == Admin_IsHead.guanliyuan.getValue()) {
				groups = communityHospitalGroupService.getAllGroups();
				//hospitals = communityHospitalService.getAllHospitals();
				issq = 0;//0是平台管理员登陆，1是集团登陆，2是其他登陆
				page.setTotalNum(healthFileService.getHealthFileCount());
				this.pageHtml = page.getPage("healthFileAction!showHealthFile.action");
				healthFileList = healthFileService.getAllHealthFile(page, state);
				allDoctor = healthFileService.getAllDoctorByHosId();
				allEnterPeople = staffService.getAllOperator();
				staffs = healthFileService.getStaffs(communityHospital.getCommunityHospitalId());
				flagPingTai = true;
				indexFlag=1;
				return "showHealthFile";
			} else {//社区负责人

				str = communityHospital.getName();
				hosName = communityHospital.getCommunityHospital();
				//注释的部分是因为改成了从服务器获取
				/*page.setTotalNum(healthFileService.getHealthFileCount(hosName));
				this.pageHtml = page.getPage("healthFileAction!showHealthFile.action");
				healthFileList = healthFileService.getAllHealthFile(page, state, hosName);*/
				allDoctor = healthFileService.getAllDoctorByHosId(communityHospital.getCommunityHospitalId());
				specialDoctor =healthFileService.getSpeciallDoctor(communityHospital.getCommunityHospitalId());
				allEnterPeople = staffService.getAllOperator(communityHospital.getCommunityHospitalId());
				staffs = healthFileService.getStaffs(communityHospital.getCommunityHospitalId());
				/*return "showHealthFile";*/
				//服务器接口--
				healthFileCloudList=getHealthFileListInfo(page);
				this.pageHtml = page.getPage("healthFileAction!showHealthFile.action");
				return "showHealthFileInfo";
				//--服务器接口
			}
		} else if (session.getAttribute("tstaff") != null) {
			tstaff = (TStaff) session.getAttribute("tstaff");
			str = tstaff.getName();
			hosId = staffService.getCommHospitalByStaffId(tstaff.getStaffId());
			communityHospital = communityHospitalService.getCommunityHospitalById(hosId);
			// 判断员工是否为责任医生，若是，则只能看到所对应的老人
			if (healthFileService.getOldStaffHosByStaffId(tstaff.getStaffId())) {//是责任医生
				//page.setTotalNum(healthFileService.getHealthFileCountForSpecial(tstaff.getStaffId(), state));
				healthFileList = healthFileService.getHealthFileListByStaffIdForSpeciall(page, state,
						tstaff.getStaffId());
				allDoctor = healthFileService.getAllDoctorByHosId(hosId);
				allEnterPeople = staffService.getAllOperator(hosId);
				staffs = healthFileService.getStaffs(hosId);
				indexFlag=1;
				flag = true;
				//服务器接口--
				healthFileCloudList=getHealthFileListInfo(page);
				this.pageHtml = page.getPage("healthFileAction!showHealthFile.action");
				return "showHealthFileInfo";
				//--服务器接口
			} else {
				page.setTotalNum(healthFileService.getHealthFileCount(communityHospital.getCommunityHospital()));
				String hosname=communityHospital.getCommunityHospital();
				healthFileList = healthFileService.getAllHealthFile(page, state,hosname);
				allDoctor = healthFileService.getAllDoctorByHosId(hosId);
				allEnterPeople = staffService.getAllOperator(hosId);
				specialDoctor =healthFileService.getSpeciallDoctor(hosId);
				staffs = healthFileService.getStaffs(hosId);
			}

			this.pageHtml = page.getPage("healthFileAction!showHealthFile.action");
			return "showHealthFile";
		} else {
			return "error";
		}

		// page.setTotalNum(healthFileService.getHealthFileCount(hosName));
		// this.pageHtml=page.getPage("healthFileAction!showHealthFile.action");
		// healthFileList=healthFileService.getAllHealthFile(page, state,
		// hosName);
		// return "showHealthFile";
	}

	public String turnToAddHealthFile() {
		token = TokenProccessor.getInstance().makeToken();//创建令牌
		HttpServletRequest request=ServletActionContext.getRequest();
		request.getSession().setAttribute("token", token);  //在服务器使用session保存token(令牌)
		// /////////////////////////////////////////
		whcd = maintainableAttributeService.getmEntriesByCode("01001");// 文化程度
		zy = maintainableAttributeService.getmEntriesByCode("01002");// 职业
		hyzk = maintainableAttributeService.getmEntriesByCode("01003");// 婚姻状况
		cfpfcs = maintainableAttributeService.getmEntriesByCode("01004");// 厨房排风措施
		rllx = maintainableAttributeService.getmEntriesByCode("01005");// 燃料类型
		ys = maintainableAttributeService.getmEntriesByCode("01006");// 饮水
		qcl = maintainableAttributeService.getmEntriesByCode("01007");// 禽畜栏
		cs = maintainableAttributeService.getmEntriesByCode("01008");// 厕所
		mz = maintainableAttributeService.getmEntriesByCode("01009");// 民族
		//生活方式（可维护字段）
		 tydlplList=maintainableAttributeService.getmEntriesByCode("02010");
		 dlfsList=maintainableAttributeService.getmEntriesByCode("02011");
		 ysxgList=maintainableAttributeService.getmEntriesByCode("02008");
		 xyzkList=maintainableAttributeService.getmEntriesByCode("02013");
		 yjplList=maintainableAttributeService.getmEntriesByCode("02012");
		 yjzlList=maintainableAttributeService.getmEntriesByCode("02014");
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session.getAttribute("comHospital") != null) {
			communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
			str = communityHospital.getName();
			hosId = communityHospital.getCommunityHospitalId();
			specialDoctor =healthFileService.getSpeciallDoctor(hosId);
			allEnterPeople = staffService.getAllOperator(hosId);
			allDoctor = healthFileService.getAllDoctorByHosId(hosId);
			staffs = healthFileService.getStaffs(hosId);
		} else if (session.getAttribute("tstaff") != null) {
			tstaff = (TStaff) session.getAttribute("tstaff");
			str = tstaff.getName();
			intId=tstaff.getStaffId();
			hosId = staffService.getCommHospitalByStaffId(tstaff.getStaffId());
			specialDoctor =healthFileService.getSpeciallDoctor(hosId);
			allEnterPeople = staffService.getAllOperator(hosId);
			communityHospital = communityHospitalService.getCommunityHospitalById(hosId);
			allDoctor = healthFileService.getAllDoctorByHosId(hosId);
			staffs = healthFileService.getStaffs(hosId);
			if (healthFileService.getOldStaffHosByStaffId(tstaff.getStaffId())) {// 判断员工是否为责任医生，若是
				//flag=true;
				return "turnToAddHealthFileForSpecial";
			}
		} else {
			return "error";
		}

		return "turnToAddHealthFile";
	}

	public String addHealthFile() {
		
	HttpServletRequest request=ServletActionContext.getRequest();
	boolean b = Resubmit.isRepeatSubmit(request,token);//判断用户是否是重复提交
	if(b==true){
		return "DocDRedir";  //重定向回首页
	}
	request.getSession().removeAttribute("token");//移除session中的token
		// 添加一体机注册 add by wangkan
		String sexStr = healthFile.getSex() == true ? "0" : "1";
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String strBirth = df.format(healthFile.getBirthDate());
		String ret = this.registerHealthMachine(healthFile.getIPhone(), healthFile.getName(), "123456",
				healthFile.getIdNum(), healthFile.getNfc(), sexStr, healthFile.getHeight() + "",
				healthFile.getWeihth() + "", strBirth, healthFile.getWaistline() + "", healthFile.getHipline() + "");
		if (!(ret.equals("0") || ret.equals("1"))) {
			return "errorAddHealthFile";
		}
		// 后台校验身份证号是否重复
		if (healthFileService.getHealthFileByIdNum(healthFile.getIdNum()) != null) {
			return "errorAddHealthFile";
		}
		// 以下是上传照片
		if (upload != null) {
			str = UploadAndDownload.uploadImage(upload, uploadFileName, savaPartPath);
			if (str != null) {
				healthFile.setPic(str);
			}

		}
		// 以下是算出年龄
		String datetime = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
		int a = Integer.parseInt(datetime);
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String birth = sdf.format(healthFile.getBirthDate());
		int c = Integer.parseInt(birth.substring(0, 4));
		healthFile.setAge(a - c);
		//健康档案来自哪个系统：1社区医院；2养老机构；3居家养老
		healthFile.setBelongSystem("1");
		/**
		 * 判断角色
		 */
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session.getAttribute("comHospital") != null) {
			communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
			if (communityHospital.getIsHead() == Admin_IsHead.guanliyuan.getValue()) {
				hosId = 2;
			} else {
				hosId = communityHospital.getCommunityHospitalId();
			}
		} else if (session.getAttribute("tstaff") != null) {
			tstaff = (TStaff) session.getAttribute("tstaff");
			hosId = staffService.getCommHospitalByStaffId(tstaff.getStaffId());
		}
		/**
		 * 若没有输入档案编号，则自动生成编号
		 */
		str = healthFile.getFileNum();
		/*
		 * if(str!=null){ for (int i = 0; i < str.length(); i++){ if
		 * (!Character.isDigit(str.charAt(i))){
		 * healthFile.setFileNum(healthFileService.getFileNum(hosId)); } }
		 */

		if (healthFile.getFileNum().equals("") || healthFile.getFileNum() == null) {
			healthFile.setFileNum(healthFileService.getFileNum(hosId));
		}
		/**
		 * 若前台没有添加建档日期，将系统当前时间赋值给建档案时间
		 */
		if(healthFile.getFileDate()==null){
			Date now = new Date();
			Timestamp nowTime = TypeConverter.date2Timestamp(now);
			healthFile.setFileDate(nowTime);
		}
		

		//healthFile.setEnterPeople(peopleString);
		healthFile.setHealthFileType("2");
		healthFile.setBelongSystem("1");
		String str = TypeConverter.list2FormatString(choiceFather, ",");
		healthFile.setFatherDisease(str);// 添加父亲家族史
		healthFile.setMotherDisease(TypeConverter.list2FormatString(choiceMother, ","));// 添加母亲家族史
		healthFile.setBorSisDisease(TypeConverter.list2FormatString(choiceBroSis, ","));// 添加兄妹家族史
		healthFile.setChildDisease(TypeConverter.list2FormatString(choiceChild, ","));// 添加子女家族史
		healthFile.setDisabilityName(TypeConverter.list2FormatString(choiceDisabilityName, ","));// 添加残疾情况
		healthFile.setAllergyName(TypeConverter.list2FormatString(choiceAllergyName, ","));
		healthFile.setExposeName(TypeConverter.list2FormatString(choiceExposeName, ","));
		healthFileId = healthFileService.getAutoIdBySaveHealthFile(healthFile);// 添加健康档案
		healthFile = healthFileService.getHealthFileById(healthFileId);// 得到刚才保存的健康档案
		medicalPayMethodList = new ArrayList<TMedicalPayMethod>();
		if (medicalPayMethodName != null) {
			for (int i = 0; i < medicalPayMethodName.size(); i++) {
				medicalPayMethod = new TMedicalPayMethod();
				medicalPayMethod.setName(medicalPayMethodName.get(i));
				if (medicalPayMethodCardNum.get(i) != null) {
					medicalPayMethod.setCardNum(medicalPayMethodCardNum.get(i));
				}
				medicalPayMethod.setTHealthFile(healthFile);
				medicalPayMethodList.add(medicalPayMethod);
			}
		}
		medicalPayMethodService.saveMedicalPayMethod(medicalPayMethodList);// 医疗付费方式完毕
		tstaff = staffService.getStaffById(intId);
		communityHospital = communityHospitalService.getCommunityHospitalById(hosId);
		oldStaffHos = new TOldStaffHos();
		oldStaffHos.setTHealthFile(healthFile);
		oldStaffHos.setTCommunityHospital(communityHospital);
		oldStaffHos.setTStaff(tstaff);
		healthFileService.saveOldStaffHos(oldStaffHos);// 添加员工医院关系表
		if (pastHistoryShouShuNameList != null) {
			for (int i = 0; i < pastHistoryShouShuNameList.size(); i++) {
				pasthistory = new TPastHistory();
				pasthistory.setPastType("手术");
				pasthistory.setTHealthFile(healthFile);
				pasthistory.setPastName(pastHistoryShouShuNameList.get(i));
				pasthistory.setPastDate(pastHistoryShouShuTimeList.get(i));
				pastHistoryService.savePastHistory(pasthistory);
			}

		}
		if (pastHistoryWaiShangNameList != null) {
			for (int i = 0; i < pastHistoryWaiShangNameList.size(); i++) {
				pasthistory = new TPastHistory();
				pasthistory.setPastType("外伤");
				pasthistory.setTHealthFile(healthFile);
				pasthistory.setPastName(pastHistoryWaiShangNameList.get(i));
				pasthistory.setPastDate(pastHistoryWaiShangTimeList.get(i));
				pastHistoryService.savePastHistory(pasthistory);
			}

		}
		if (pastHistoryShuXieNameList != null) {
			for (int i = 0; i < pastHistoryShuXieNameList.size(); i++) {
				pasthistory = new TPastHistory();
				pasthistory.setPastType("输血");
				pasthistory.setTHealthFile(healthFile);
				pasthistory.setPastName(pastHistoryShuXieNameList.get(i));
				pasthistory.setPastDate(pastHistoryShuXieTimeList.get(i));
				pastHistoryService.savePastHistory(pasthistory);
			}
		}
		if (diseaseName != null) {
			for (int i = 0; i < diseaseName.size(); i++) {
				pasthistory = new TPastHistory();
				pasthistory.setPastType("疾病");
				pasthistory.setTHealthFile(healthFile);
				pasthistory.setPastName(diseaseName.get(i));
				if (diseaseTime != null) {
					// list去除null
					diseaseTime.removeAll(Collections.singleton(null));
					if(diseaseTime.size()>0){
						//if(diseaseTime.size()>i){
							try {
								pasthistory.setPastDate(diseaseTime.get(i));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						//}
					}
				}
				pastHistoryService.savePastHistory(pasthistory);
			}
		}
		//添加生活方式
		tlifestyle.setTHealthFile(healthFile);
		fileLifeStyleService.saveFileLifeStyle(tlifestyle);
		tlifestyleList=fileLifeStyleService.getFileLifeStyleListByhealthID(healthFile.getHealthFileId());
		//添加用药记录
		if(fileMedicalListName!=null){
			for(int i=0;i<fileMedicalListName.size();i++){
				fileMedicalRecord=new TFileMedicalRecord();
				fileMedicalRecord.setTHealthFile(healthFile);
				fileMedicalRecord.setDrugName(fileMedicalListName.get(i));
				fileMedicalRecord.setUsages(fileMedicalListUsages.get(i));
				fileMedicalRecord.setDosage(fileMedicalListDosage.get(i));
				fileMedicalRecord.setBeginDate(fileMedicalListBegin.get(i));
				fileMedicalRecord.setMedicationAdherence(fileMedicalListMedicationAdherence.get(i));
				fileMedicalRecord.setUntowardEffect(fileMedicalListUntowardEffect.get(i));
				fileMedicalRecord.setDrugSources(fileMedicalListDrugSources.get(i));
				fileMedicalRecordService.saveFileMedicalRecord(fileMedicalRecord);
			}
		}
		
		fileMedicalList=fileMedicalRecordService.getFileMedicalRecordList(healthFile.getHealthFileId());
		//同步到服务器
		medicalPayMethodList=medicalPayMethodService.getMedicalPayMethodByHFileId(healthFile.getHealthFileId());
		pastHistoryList=pastHistoryService.getPastHistory(healthFile.getHealthFileId());
		boolean aa=healthFileService.addHealthFileToCloulds(healthFile,pastHistoryList,medicalPayMethodList,tlifestyleList,fileMedicalList,intId);//intid是责任医生id
		return "addHealthFile";
	}

	public String turnToHealthFileDetil() {
		token = TokenProccessor.getInstance().makeToken();//创建令牌
		HttpServletRequest request=ServletActionContext.getRequest();
		request.getSession().setAttribute("token", token);  //在服务器使用session保存token(令牌)
		
		// /////////////////////////////////////////
		whcd = maintainableAttributeService.getmEntriesByCode("01001");// 文化程度
		zy = maintainableAttributeService.getmEntriesByCode("01002");// 职业
		hyzk = maintainableAttributeService.getmEntriesByCode("01003");// 婚姻状况
		cfpfcs = maintainableAttributeService.getmEntriesByCode("01004");// 厨房排风措施
		rllx = maintainableAttributeService.getmEntriesByCode("01005");// 燃料类型
		ys = maintainableAttributeService.getmEntriesByCode("01006");// 饮水
		qcl = maintainableAttributeService.getmEntriesByCode("01007");// 禽畜栏
		cs = maintainableAttributeService.getmEntriesByCode("01008");// 厕所
		mz = maintainableAttributeService.getmEntriesByCode("01009");// 民族
		// ///////////////////////////////////
		//生活方式（可维护字段）
		 tydlplList=maintainableAttributeService.getmEntriesByCode("02010");
		 dlfsList=maintainableAttributeService.getmEntriesByCode("02011");
		 ysxgList=maintainableAttributeService.getmEntriesByCode("02008");
		 xyzkList=maintainableAttributeService.getmEntriesByCode("02013");
		 yjplList=maintainableAttributeService.getmEntriesByCode("02012");
		 yjzlList=maintainableAttributeService.getmEntriesByCode("02014");
		 ///////////////
		HttpSession session = ServletActionContext.getRequest().getSession();
		//当前用户是社区负责人或者责任医生时
				communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
				tstaff = (TStaff) session.getAttribute("tstaff");
				if ((communityHospital!= null&&communityHospital.getIsHead() != Admin_IsHead.guanliyuan.getValue())||(tstaff!=null&&healthFileService.getOldStaffHosByStaffId(tstaff.getStaffId()))) {
					if (session.getAttribute("comHospital") != null) {
						hosId = communityHospital.getCommunityHospitalId();
					}
					else if (session.getAttribute("tstaff") != null) {
						if (healthFileService.getOldStaffHosByStaffId(tstaff.getStaffId())) {// 判断员工是否为责任医生，若是，则只能看到所对应的老人
							flag=true;
						}
						hosId = staffService.getCommHospitalByStaffId(tstaff.getStaffId());
						communityHospital = communityHospitalService.getCommunityHospitalById(hosId);
					}
					specialDoctor =healthFileService.getSpeciallDoctor(hosId);
					staffs = healthFileService.getStaffs(hosId);
					String resultJson=healthFileService.getHealthFileCloudForDetail(healthCloudId);
					HealthFileCloudForDetail healthSingle=JSON.parseObject(resultJson,HealthFileCloudForDetail.class);
					healthFileCloudSingle=healthSingle.getItem();
					String re1[];
					re1=healthFileCloudSingle.getBirthDate().split(" ");
					healthFileCloudSingle.setBirthDate(re1[0]);
					vstaffHosSu = healthFileService.getVStaffHosSuByHeathFileId(healthFileCloudSingle.getHealthFileId(), state);//用来显示责任医生
					choiceResultFather = healthFileCloudSingle.getFatherDisease();//家族史
					choiceResultMother = healthFileCloudSingle.getMotherDisease();
					choiceResultBroSis = healthFileCloudSingle.getBorSisDisease();
					choiceResultChild = healthFileCloudSingle.getChildDisease();
					choiceResultDisability = healthFileCloudSingle.getDisabilityName();//残疾状况
					choiceResultExposeName = healthFileCloudSingle.getExposeName();//暴露史
					choiceResultAllergyName = healthFileCloudSingle.getAllergyName();//药物过敏史
					//医疗付费方式子表
					payMethodList=healthFileCloudSingle.getMpmList();
					medicalPayMethodNameList = new ArrayList<String>();
					medicalPayMethodCardNumList = new ArrayList<String>();
					if (payMethodList != null&&payMethodList.size()!=0) {
						for (int i = 0; i < payMethodList.size(); i++) {
							medicalPayMethodNameList.add(payMethodList.get(i).getName());
							if (payMethodList.get(i).getCardNum() != null) {
								medicalPayMethodCardNumList.add(payMethodList.get(i).getCardNum());
							}
						}
					}
					if (medicalPayMethodNameList.size() > 0) {
						str = TypeConverter.list3FormatString(medicalPayMethodNameList, ",");
					} else {
						str = "ee";
					}
					//生活方式子表
					lifestyleList=healthFileCloudSingle.getFlsList();
					if(lifestyleList!=null&&lifestyleList.size()!=0){
						lifestyle=lifestyleList.get(0);
					}
					//用药记录子表
					//这里面的时间也不是字符串，而是一个类，以后也得改
					MedicalRecordList=healthFileCloudSingle.getFmrList();
					//既往史子表
					pastAllList=healthFileCloudSingle.getFphList();
					pastList=new ArrayList<PastHistory>();//初始化list
					pastWaiShangList=new ArrayList<PastHistory>();//初始化list
					pastShouShuList=new ArrayList<PastHistory>();//初始化list
					pastShuXieList=new ArrayList<PastHistory>();//初始化list
					//因为fphl中date不是string类型所以无法完成赋值
					if(pastAllList!=null&&pastAllList.size()!=0){
						for(int i=0;i<pastAllList.size();i++){
							if(pastAllList.get(i).getPastType().equals("疾病")){
								pastList.add(pastAllList.get(i));
							}
							else if(pastAllList.get(i).getPastType().equals("手术")){
								pastShouShuList.add(pastAllList.get(i));
							}
							else if(pastAllList.get(i).getPastType().equals("外伤")){
								pastWaiShangList.add(pastAllList.get(i));
							}
							else if(pastAllList.get(i).getPastType().equals("输血")){
								pastShuXieList.add(pastAllList.get(i));
							}
						}
					}
					return "turnToHealthFileDetilInfo";
				}
				else{
					
				}
		//当前用户不是社区负责人或者责任医生，是其他角色时
		if (session.getAttribute("comHospital") != null) {
			communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
			if (communityHospital.getIsHead() != Admin_IsHead.guanliyuan.getValue()) {
				hosId = communityHospital.getCommunityHospitalId();
				if (healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, healthFileId) == null) {
					return "error";
				}
			}
		} else if (session.getAttribute("tstaff") != null) {
			tstaff = (TStaff) session.getAttribute("tstaff");
			if (healthFileService.getOldStaffHosByStaffId(tstaff.getStaffId())) {// 判断员工是否为责任医生，若是，则只能看到所对应的老人
				flag=true;
				if (healthFileService.getOldStaffHosByStaffIdAndHeathFileId(tstaff.getStaffId(),
						healthFileId) == null) {
					return "error";
				}
			}
			hosId = staffService.getCommHospitalByStaffId(tstaff.getStaffId());
			if (healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, healthFileId) == null) {
				return "error";
			}
		}
		if (healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, healthFileId) == null) {

			return "error";
		}
		

		medicalPayMethodList = medicalPayMethodService.getMedicalPayMethodByHFileId(healthFileId);
		medicalPayMethodNameList = new ArrayList<String>();
		medicalPayMethodCardNumList = new ArrayList<String>();
		if (medicalPayMethodList != null) {
			for (int i = 0; i < medicalPayMethodList.size(); i++) {
				medicalPayMethodNameList.add(medicalPayMethodList.get(i).getName());
				if (medicalPayMethodList.get(i).getCardNum() != null) {
					medicalPayMethodCardNumList.add(medicalPayMethodList.get(i).getCardNum());
				}
			}
		}
		if (medicalPayMethodNameList.size() > 0) {
			str = TypeConverter.list3FormatString(medicalPayMethodNameList, ",");
		} else {
			str = "ee";
		}
		specialDoctor =healthFileService.getSpeciallDoctor(hosId);
		for(int k=0;k<specialDoctor.size();k++){
			System.out.println(specialDoctor.get(k).getStaffId()  );
		}
		allDoctor = healthFileService.getAllDoctorByHosId(hosId);// hosId
		staffs = healthFileService.getStaffs(hosId);
		communityHospital = communityHospitalService.getCommunityHospitalById(hosId);
		// allHos=communityHospitalService.getAllHospitals();
		vstaffHosSu = healthFileService.getVStaffHosSuByHeathFileId(healthFileId, state);
		healthFile = healthFileService.getHealthFileById(healthFileId);
		//boolean a=healthFileService.editHealthFileToCloulds(healthFile);
		choiceResultFather = healthFile.getFatherDisease();
		choiceResultMother = healthFile.getMotherDisease();
		choiceResultBroSis = healthFile.getBorSisDisease();
		choiceResultChild = healthFile.getChildDisease();
		choiceResultDisability = healthFile.getDisabilityName();
		choiceResultExposeName = healthFile.getExposeName();
		choiceResultAllergyName = healthFile.getAllergyName();
		fileMedicalRecordList=fileMedicalRecordService.getFileMedicalRecordList(healthFileId);
		pastHistoryShouShuList = pastHistoryService.getPastHistory(healthFileId, "手术");// 得到既往史
		pastHistoryWaiShangList = pastHistoryService.getPastHistory(healthFileId, "外伤");
		pastHistoryShuXieList = pastHistoryService.getPastHistory(healthFileId, "输血");
		tlifestyle=fileLifeStyleService.getFileLifeStyleByhealthID(healthFileId);
		return "turnToHealthFileDetil";
	}

	public String turnToHealthFileView() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		//当前用户是社区负责人或者责任医生时
				communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
				tstaff = (TStaff) session.getAttribute("tstaff");
				if ((communityHospital!= null&&communityHospital.getIsHead() != Admin_IsHead.guanliyuan.getValue())||(tstaff!=null&&healthFileService.getOldStaffHosByStaffId(tstaff.getStaffId()))) {
					//德江方法 健康档案详情
					String jiankangjianyi=""; //朱德江健康建议页面使用！！！！！！
					if(healthCloudId<0){
						healthCloudId=Math.abs(healthCloudId);
						jiankangjianyi="showHealthFileHealthAdvice";
					}
					
					String resultJson=healthFileService.getHealthFileCloudForDetail(healthCloudId);
					HealthFileCloudForDetail healthSingle=JSON.parseObject(resultJson,HealthFileCloudForDetail.class);
					healthFileCloudSingle=healthSingle.getItem();
					String re1[];
					re1=healthFileCloudSingle.getBirthDate().split(" ");
					healthFileCloudSingle.setBirthDate(re1[0]);
					doctorName=staffService.getStaffById(healthFileCloudSingle.getDoctorID()).getName();  
					vstaffHosSu = healthFileService.getVStaffHosSuByHeathFileId(healthFileCloudSingle.getHealthFileId(), state);//用来显示责任医生
					choiceResultFather = healthFileCloudSingle.getFatherDisease();//家族史
					choiceResultMother = healthFileCloudSingle.getMotherDisease();
					choiceResultBroSis = healthFileCloudSingle.getBorSisDisease();
					choiceResultChild = healthFileCloudSingle.getChildDisease();
					choiceResultDisability = healthFileCloudSingle.getDisabilityName();//残疾状况
					choiceResultExposeName = healthFileCloudSingle.getExposeName();//暴露史
					choiceResultAllergyName = healthFileCloudSingle.getAllergyName();//药物过敏史
					//医疗付费方式子表
					payMethodList=healthFileCloudSingle.getMpmList();
					//生活方式子表
					lifestyleList=healthFileCloudSingle.getFlsList();
					if(lifestyleList!=null&&lifestyleList.size()!=0){
						lifestyle=lifestyleList.get(0);
					}
					//用药记录子表
					//这里面的时间也不是字符串，而是一个类，以后也得改
					MedicalRecordList=healthFileCloudSingle.getFmrList();
					if(belongSystemQianDuan==2&&MedicalRecordList!=null){//老人来自养老机构
						for(int i=0;i<MedicalRecordList.size();i++){
							String[] re = MedicalRecordList.get(i).getDosage().split(" ");
							yongLiangCiShu=re[1];
							meiCiYongLiang=re[2];
							MedicalRecordList.get(i).setDosage("每日"+yongLiangCiShu+"次，每次"+meiCiYongLiang+"毫克");
						}
					}
					//既往史子表
					pastAllList=healthFileCloudSingle.getFphList();
					pastList=new ArrayList<PastHistory>();//初始化list
					pastWaiShangList=new ArrayList<PastHistory>();//初始化list
					pastShouShuList=new ArrayList<PastHistory>();//初始化list
					pastShuXieList=new ArrayList<PastHistory>();//初始化list
					//因为fphl中date不是string类型所以无法完成赋值
					if(pastAllList!=null&&pastAllList.size()!=0){
						for(int i=0;i<pastAllList.size();i++){
							if(pastAllList.get(i).getPastType().equals("疾病")){
								try {
									pastList.add(pastAllList.get(i));
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							else if(pastAllList.get(i).getPastType().equals("手术")){
								pastShouShuList.add(pastAllList.get(i));
							}
							else if(pastAllList.get(i).getPastType().equals("外伤")){
								pastWaiShangList.add(pastAllList.get(i));
							}
							else if(pastAllList.get(i).getPastType().equals("输血")){
								pastShuXieList.add(pastAllList.get(i));
							}
						}
					}
					
					//德江方法健康建议查看健康档案
					if(jiankangjianyi.equals("")){
						return "turnToHealthFileViewInfo";
					}else{
						return jiankangjianyi;
					}
				}
		//当前用户不是社区负责人或者责任医生，是其他角色时
		if (session.getAttribute("comHospital") != null) {
			communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
			if (communityHospital.getIsHead() != Admin_IsHead.guanliyuan.getValue()) {
				hosId = communityHospital.getCommunityHospitalId();
				if (healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, healthFileId) == null) {
					return "error";
				}
			}

		} else if (session.getAttribute("tstaff") != null) {
			tstaff = (TStaff) session.getAttribute("tstaff");
			if (healthFileService.getOldStaffHosByStaffId(tstaff.getStaffId())) {// 判断员工是否为责任医生，若是，则只能看到所对应的老人
				if (healthFileService.getOldStaffHosByStaffIdAndHeathFileId(tstaff.getStaffId(),
						healthFileId) == null) {
					return "error";
				}
			}
			hosId = staffService.getCommHospitalByStaffId(tstaff.getStaffId());
			if (healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, healthFileId) == null) {

				return "error";
			}
		}
		
		medicalPayMethodList = medicalPayMethodService.getMedicalPayMethodByHFileId(healthFileId);
		medicalPayMethodNameList = new ArrayList<String>();
		medicalPayMethodCardNumList = new ArrayList<String>();
		if (medicalPayMethodList != null) {
			for (int i = 0; i < medicalPayMethodList.size(); i++) {
				medicalPayMethodNameList.add(medicalPayMethodList.get(i).getName());
				if (medicalPayMethodList.get(i).getCardNum() != null) {
					medicalPayMethodCardNumList.add(medicalPayMethodList.get(i).getCardNum());
				}
			}
			if (medicalPayMethodNameList.size() > 0) {
				str = TypeConverter.list3FormatString(medicalPayMethodNameList, ",");
			} else {
				str = "ee";
			}
		}

		//allHos = communityHospitalService.getAllHospitals();123
		healthFile = healthFileService.getHealthFileById(healthFileId);
		//boolean a=healthFileService.editHealthFileToCloulds(healthFile);
		// vstaffHos=healthFileService.getVStaffHosByHeathFileId(healthFileId);//参数是healthFileId，默认为1
		vstaffHosSu = healthFileService.getVStaffHosSuByHeathFileId(healthFileId, state);//用来显示责任医生
		choiceResultFather = healthFile.getFatherDisease();//家族史
		choiceResultMother = healthFile.getMotherDisease();
		choiceResultBroSis = healthFile.getBorSisDisease();
		choiceResultChild = healthFile.getChildDisease();
		choiceResultDisability = healthFile.getDisabilityName();//残疾状况
		choiceResultExposeName = healthFile.getExposeName();//暴露史
		choiceResultAllergyName = healthFile.getAllergyName();//药物过敏史
		fileMedicalRecordList=fileMedicalRecordService.getFileMedicalRecordList(healthFileId);
		pastHistoryList = pastHistoryService.getPastHistory(healthFileId, "疾病");
		pastHistoryShouShuList = pastHistoryService.getPastHistory(healthFileId, "手术");// 得到既往史
		pastHistoryWaiShangList = pastHistoryService.getPastHistory(healthFileId, "外伤");
		pastHistoryShuXieList = pastHistoryService.getPastHistory(healthFileId, "输血");
		tlifestyle=fileLifeStyleService.getFileLifeStyleByhealthID(healthFileId);
		return "turnToHealthFileView";
	}

	public List<MedicalPayMethod> getPayMethodList() {
		return payMethodList;
	}

	public void setPayMethodList(List<MedicalPayMethod> payMethodList) {
		this.payMethodList = payMethodList;
	}

	public String egg() {
		egg++;
		return "egg";
	}

	public String checkPic() {
		String json = null;
		List<String> allowTypesList = new ArrayList<String>();
		allowTypesList.add(".jpeg");
		allowTypesList.add(".gif");
		allowTypesList.add(".pjpeg");
		allowTypesList.add(".jpg");
		allowTypesList.add(".png");
		allowTypesList.add(".bmp");
		String fileType = picStr.substring(picStr.lastIndexOf("."));
		if (!allowTypesList.contains(fileType)) {
			json = "{\"checkResult\":\"" + "no" + "\"}";
		} else {
			json = "{\"checkResult\":\"" + "ok" + "\"}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
//判断身份证号是否重复
	public String checkIdNum() {
		String json = null;
		if (healthFileService.getHealthFileByIdNum(idNum) != null) {
			json = "{\"checkResult\":\"" + "no" + "\"}";
		} else if(IDCardVerificationUtil.verification(idNum) == false){
			json = "{\"checkResult\":\"" + "false" + "\"}";
		}
		else{
			json = "{\"checkResult\":\"" + "ok" + "\"}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	//判断15/18位身份证号是否格式正确
	public String isIdCard(){
		String json = null;
		if (IDCardVerificationUtil.verification(idNum) == true) {
			json = "{\"checkResult\":\"" + "ok" + "\"}";
		} else {
			json = "{\"checkResult\":\"" + "no" + "\"}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String checkIphone() {
		String json = null;
		if (healthFileService.getHealthFileByIphone(iphone) == null) {
			json = "{\"checkResult\":\"" + "ok" + "\"}";
		} else {
			json = "{\"checkResult\":\"" + "no" + "\"}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
//	public String getBirthDate() {
//		String json = null;
//		String a = idNum.substring(6, 10);
//		String b = idNum.substring(10, 12);
//		String c = idNum.substring(12, 14);
//
//		json = "{\"checkResult\":\"" + a + "-" + b + "-" + c + "\"}";
//		/* +a+"-"+b+"-"+c+ */
//		HttpServletResponse response = ServletActionContext.getResponse();
//		response.setContentType("text");
//		response.setCharacterEncoding("UTF-8");
//		try {
//			response.getWriter().write(json);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	public String checkFileNum() {
		String json = null;
		boolean bool = true;// 默认是true
		if (healthFileService.getHealthFileByFileNum(fileNum) == null) {
			if (fileNum.length() == 17) {
				for (int i = fileNum.length(); --i >= 0;) {
					if (!Character.isDigit(str.charAt(i))) {
						bool = false;// 如果抛出异常，返回False}
					}
				}
				if (bool) {
					json = "{\"checkResult\":\"" + "ok" + "\"}";
				} else {
					json = "{\"checkResult\":\"" + "geshi" + "\"}";
				}
			} else {
				json = "{\"checkResult\":\"" + "geshi" + "\"}";
			}

		} else {
			json = "{\"checkResult\":\"" + "no" + "\"}";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ajax 自动生成健康档案编号
	 * 
	 * @return
	 */
	public String getFileNumAuto() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session.getAttribute("comHospital") != null) {
			communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
			hosId = communityHospital.getCommunityHospitalId();
		} else if (session.getAttribute("tstaff") != null) {
			tstaff = (TStaff) session.getAttribute("tstaff");
			hosId = staffService.getCommHospitalByStaffId(tstaff.getStaffId());
		}
		String json = "{\"fileNumAuto\":\"" + healthFileService.getFileNum(hosId) + "\"}";
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ajax 获取既往史中疾病的名称与确诊时间
	 * 
	 * @return
	 */
	public String getDisease() {
		
		String json = "[";
		HttpSession session = ServletActionContext.getRequest().getSession();
		//当前用户是社区负责人或者责任医生时
		communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
		tstaff = (TStaff) session.getAttribute("tstaff");
		if ((communityHospital!= null&&communityHospital.getIsHead() != Admin_IsHead.guanliyuan.getValue())||(tstaff!=null&&healthFileService.getOldStaffHosByStaffId(tstaff.getStaffId()))) {
			String resultJson=healthFileService.getHealthFileCloudForDetail(healthCloudId);
			HealthFileCloudForDetail healthSingle=JSON.parseObject(resultJson,HealthFileCloudForDetail.class);
			healthFileCloudSingle=healthSingle.getItem();
			pastAllList=healthFileCloudSingle.getFphList();//得到既往史
			pastList=new ArrayList<PastHistory>();//初始化list
			if(pastAllList!=null&&pastAllList.size()!=0){
				for(int i=0;i<pastAllList.size();i++){
					if(pastAllList.get(i).getPastType().equals("疾病")){
						pastList.add(pastAllList.get(i));
					}
				}
			}
			for (PastHistory pastHistoryCloud : pastList) {
				json += "{\"pastHistoryName\":\"" + pastHistoryCloud.getPastName() + "\"," + "\"pastHistoryTime\":\""
						+ pastHistoryCloud.getPastDate() + "\"},";
			}
		}
		//不从服务器那数据
		else{
			List<TPastHistory> pastHistoryList = pastHistoryService.getPastHistory(healthFileId, "疾病");
			for (TPastHistory pastHistory : pastHistoryList) {
				json += "{\"pastHistoryName\":\"" + pastHistory.getPastName() + "\"," + "\"pastHistoryTime\":\""
						+ pastHistory.getPastDate() + "\"},";
			}
		}
		
		
		json = json.substring(0, json.length() - 1);
		json = json + "]";
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String testHealthFile() {
		healthFile = new THealthFile();
		String str = TypeConverter.list2FormatString(choiceFather, ",");
		healthFile.setFatherDisease(str);
		// healthFileService.saveHealthFile(healthFile);
		return "showHealthFile";
	}

	public String test() {
		/*
		 * choiceBack=new ArrayList<String>(){ { add("冠心病");
		 * 
		 * } };
		 */
		/*
		 * result="高血压"; choice=TypeConverter.stringToList(result);
		 */
		beginList = new ArrayList<String>() {
			{
				add("高血压");
				add("冠心病");
				add("糖尿病");
			}
		};
		return "test";
	}
	/**
	 * 修改健康档案
	 * @return
	 */
	public String updateHealthFile() {
		HttpServletRequest request=ServletActionContext.getRequest();
		boolean b = Resubmit.isRepeatSubmit(request,token);//判断用户是否是重复提交
		if(b==true){
			return "DocDRedir";  //重定向回首页
		}
		request.getSession().removeAttribute("token");//移除session中的token
		
		// 添加一体机注册 add by wangkan
		String sexStr = healthFile.getSex() == true ? "0" : "1";
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String strBirth = df.format(healthFile.getBirthDate());
		String ret = this.registerHealthMachine(healthFile.getIPhone(), healthFile.getName(), "123456",
				healthFile.getIdNum(), healthFile.getNfc(), sexStr, healthFile.getHeight() + "",
				healthFile.getWeihth() + "", strBirth, healthFile.getWaistline() + "", healthFile.getHipline() + "");
		this.updateHealthMachine(healthFile.getIPhone(), healthFile.getName(), "123456", healthFile.getIdNum(),
				healthFile.getNfc(), sexStr, healthFile.getHeight() + "", healthFile.getWeihth() + "", strBirth,
				healthFile.getWaistline() + "", healthFile.getHipline() + "");
		/*
		 * if(!(ret.equals("0")||ret.equals("1"))){ return "error"; }
		 */
		
		healthFileId = healthFile.getHealthFileId();
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session.getAttribute("comHospital") != null) {
			communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
			if (communityHospital.getIsHead() != Admin_IsHead.guanliyuan.getValue()) {
				hosId = communityHospital.getCommunityHospitalId();
				if (healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, healthFileId) == null) {
					return "error";
				}
			}
		} else if (session.getAttribute("tstaff") != null) {
			tstaff = (TStaff) session.getAttribute("tstaff");
			hosId = staffService.getCommHospitalByStaffId(tstaff.getStaffId());
			if (healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, healthFileId) == null) {
				return "error";
			}
		}

		medicalPayMethodService.dedleteMedicalPayMethod(healthFileId);
		pastHistoryService.deletePastHistory(healthFileId);
		healthFileService.deleteOldStaffHos(healthFileId);
		fileLifeStyleService.deleteFileLifeStyle(healthFileId);
		fileMedicalRecordService.deleteFileMedicalRecord(healthFileId);
		if (upload == null) {

			healthFile.setPic(healthFileService.getHealthFileById(healthFileId).getPic());
		} else {
			healthFile.setPic(UploadAndDownload.upload(upload, uploadFileName, savaPartPath));

		}
		// 以下是算出年龄
		String datetime = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
		if (healthFile.getFileNum().equals("") || healthFile.getFileNum() == null) {
			healthFile.setFileNum(healthFileService.getFileNum(hosId));
		}
		int a = Integer.parseInt(datetime);
		int c= Integer.parseInt(healthFile.getBirthDate().toLocaleString().substring(0, 4));
		healthFile.setAge(a - c);
		healthFile.setHealthFileType("2");
		healthFile.setBelongSystem("1");
		String str = TypeConverter.list2FormatString(choiceFather, ",");
		healthFile.setFatherDisease(str);// 添加父亲家族史
		healthFile.setMotherDisease(TypeConverter.list2FormatString(choiceMother, ","));// 添加母亲家族史
		healthFile.setBorSisDisease(TypeConverter.list2FormatString(choiceBroSis, ","));// 添加兄妹家族史
		healthFile.setChildDisease(TypeConverter.list2FormatString(choiceChild, ","));// 添加子女家族史
		healthFile.setDisabilityName(TypeConverter.list2FormatString(choiceDisabilityName, ","));// 添加残疾情况
		healthFile.setAllergyName(TypeConverter.list2FormatString(choiceAllergyName, ","));
		healthFile.setExposeName(TypeConverter.list2FormatString(choiceExposeName, ","));
	//	healthFileService.editHealthFileToCloulds(healthFile);
		healthFileService.updateHealthFile(healthFile);
		healthFile1 = healthFileService.getHealthFileById(healthFileId);// 得到刚才保存的健康档案
		medicalPayMethodList = new ArrayList<TMedicalPayMethod>();
		if (medicalPayMethodName != null) {
			for (int i = 0; i < medicalPayMethodName.size(); i++) {
				medicalPayMethod = new TMedicalPayMethod();
				medicalPayMethod.setName(medicalPayMethodName.get(i));
				if (medicalPayMethodCardNum.get(i) != null) {
					medicalPayMethod.setCardNum(medicalPayMethodCardNum.get(i));
				}
				medicalPayMethod.setTHealthFile(healthFile1);
				medicalPayMethodList.add(medicalPayMethod);
			}
		}
		medicalPayMethodService.saveMedicalPayMethod(medicalPayMethodList);// 医疗付费方式完毕
		tstaff = staffService.getStaffById(intId);
		communityHospital = communityHospitalService.getCommunityHospitalById(hosId);
		oldStaffHos = new TOldStaffHos();
		oldStaffHos.setTHealthFile(healthFile1);
		oldStaffHos.setTCommunityHospital(communityHospital);
		oldStaffHos.setTStaff(tstaff);
		healthFileService.saveOldStaffHos(oldStaffHos);// 添加员工医院关系表
		if (pastHistoryShouShuNameList != null) {//以下是既往史表
			for (int i = 0; i < pastHistoryShouShuNameList.size(); i++) {
				pasthistory = new TPastHistory();
				pasthistory.setPastType("手术");
				pasthistory.setTHealthFile(healthFile1);
				pasthistory.setPastName(pastHistoryShouShuNameList.get(i));
				pasthistory.setPastDate(pastHistoryShouShuTimeList.get(i));
				pastHistoryService.savePastHistory(pasthistory);
			}

		}
		if (pastHistoryWaiShangNameList != null) {
			for (int i = 0; i < pastHistoryWaiShangNameList.size(); i++) {
				pasthistory = new TPastHistory();
				pasthistory.setPastType("外伤");
				pasthistory.setTHealthFile(healthFile1);
				pasthistory.setPastName(pastHistoryWaiShangNameList.get(i));
				pasthistory.setPastDate(pastHistoryWaiShangTimeList.get(i));
				pastHistoryService.savePastHistory(pasthistory);
			}

		}
		if (pastHistoryShuXieNameList != null) {
			for (int i = 0; i < pastHistoryShuXieNameList.size(); i++) {
				pasthistory = new TPastHistory();
				pasthistory.setPastType("输血");
				pasthistory.setTHealthFile(healthFile1);
				pasthistory.setPastName(pastHistoryShuXieNameList.get(i));
				pasthistory.setPastDate(pastHistoryShuXieTimeList.get(i));
				pastHistoryService.savePastHistory(pasthistory);
			}

		}
		if (diseaseName != null) {
			for (int i = 0; i < diseaseName.size(); i++) {
				pasthistory = new TPastHistory();
				pasthistory.setPastType("疾病");
				pasthistory.setTHealthFile(healthFile1);
				pasthistory.setPastName(diseaseName.get(i));
				if (diseaseTime != null) {
					// list去除null
					diseaseTime.removeAll(Collections.singleton(null));
					if(diseaseTime.size()>0){
						pasthistory.setPastDate(diseaseTime.get(i));
					}
					
				}
				pastHistoryService.savePastHistory(pasthistory);
			}
		}
		//保存生活方式子表
		tlifestyle.setTHealthFile(healthFile1);
		fileLifeStyleService.saveFileLifeStyle(tlifestyle);
		tlifestyleList=fileLifeStyleService.getFileLifeStyleListByhealthID(healthFile.getHealthFileId());
		//添加用药记录
			if(fileMedicalListName!=null){
				for(int i=0;i<fileMedicalListName.size();i++){
					fileMedicalRecord=new TFileMedicalRecord();
					fileMedicalRecord.setTHealthFile(healthFile);
					fileMedicalRecord.setDrugName(fileMedicalListName.get(i));
					fileMedicalRecord.setUsages(fileMedicalListUsages.get(i));
					fileMedicalRecord.setDosage(fileMedicalListDosage.get(i));
					fileMedicalRecord.setBeginDate(fileMedicalListBegin.get(i));
					//fileMedicalRecord.setStopDate(fileMedicalListStop.get(i));
					fileMedicalRecord.setMedicationAdherence(fileMedicalListMedicationAdherence.get(i));
					fileMedicalRecord.setUntowardEffect(fileMedicalListUntowardEffect.get(i));
					fileMedicalRecord.setDrugSources(fileMedicalListDrugSources.get(i));
					fileMedicalRecordService.saveFileMedicalRecord(fileMedicalRecord);
				}
			}
			
			fileMedicalList=fileMedicalRecordService.getFileMedicalRecordList(healthFile.getHealthFileId());
		//同步到服务器
				medicalPayMethodList=medicalPayMethodService.getMedicalPayMethodByHFileId(healthFile.getHealthFileId());
				pastHistoryList=pastHistoryService.getPastHistory(healthFile.getHealthFileId());
				boolean aa=healthFileService.editHealthFileToCloulds(healthFile,pastHistoryList,medicalPayMethodList,tlifestyleList,fileMedicalList,intId);//intid是责任医生id
		return "updateHealthFile";
	}

	public String deleteHealthFile() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session.getAttribute("comHospital") != null) {
			communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
			if (communityHospital.getIsHead() != Admin_IsHead.guanliyuan.getValue()) {
				hosId = communityHospital.getCommunityHospitalId();
				if (healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, healthFileId) == null) {
					return "error";
				}
			}
		} else if (session.getAttribute("tstaff") != null) {
			tstaff = (TStaff) session.getAttribute("tstaff");
			hosId = staffService.getCommHospitalByStaffId(tstaff.getStaffId());
			if (healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, healthFileId) == null) {
				return "error";
			}
		}
		//sTHealthFile healthFile;
		//healthFile=healthFileService.getHealthFileById(healthFileId);
		//healthFileService.editHealthFileToCloulds(healthFile);
		healthFileService.deleteHealthFile(healthFileId);
		if(healthCloudId!=0){
		healthFileService.deleteHealthFileToCloulds(healthCloudId,1);//删除健康档案同步到服务器;删除是1
		}
		return "deleteHealthFile";
	}

	public String killHealthFile() {//死亡登记健康档案
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session.getAttribute("comHospital") != null) {
			communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
			if (communityHospital.getIsHead() != Admin_IsHead.guanliyuan.getValue()) {
				hosId = communityHospital.getCommunityHospitalId();
				if (healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, healthFileId) == null) {
					return "error";
				}
			}
		} else if (session.getAttribute("tstaff") != null) {
			tstaff = (TStaff) session.getAttribute("tstaff");
			hosId = staffService.getCommHospitalByStaffId(tstaff.getStaffId());
			if (healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, healthFileId) == null) {
				return "error";
			}
		}
		healthFileService.killHealthFile(healthFileId);
		if(healthCloudId!=0){
		healthFileService.deleteHealthFileToCloulds(healthCloudId,3);//死亡健康档案同步到服务器;死亡是3
		}
		return "killHealthFile";
	}

	public String cancelHealthFile() {//注销健康档案
		HttpSession session = ServletActionContext.getRequest().getSession();
		if (session.getAttribute("comHospital") != null) {
			communityHospital = (TCommunityHospital) session.getAttribute("comHospital");
			if (communityHospital.getIsHead() != Admin_IsHead.guanliyuan.getValue()) {
				hosId = communityHospital.getCommunityHospitalId();
				if (healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, healthFileId) == null) {
					return "error";
				}
			}
		} else if (session.getAttribute("tstaff") != null) {
			tstaff = (TStaff) session.getAttribute("tstaff");
			hosId = staffService.getCommHospitalByStaffId(tstaff.getStaffId());
			if (healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, healthFileId) == null) {
				return "error";
			}
		}
		healthFileService.cancelHealthFile(healthFileId);
		if(healthCloudId!=0){
			healthFileService.deleteHealthFileToCloulds(healthCloudId,2);//注销健康档案同步到服务器;注销是2
		}
		
		return "cancelHealthFile";
	}

	public List<THealthFile> getHealthFileList() {
		return healthFileList;
	}

	public void setHealthFileList(List<THealthFile> healthFileList) {
		this.healthFileList = healthFileList;
	}

	public String getUname() {
		return uname;
	}

	public boolean isSex() {
		return sex;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getNfc() {
		return nfc;
	}

	public void setNfc(String nfc) {
		this.nfc = nfc;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getYaowei() {
		return yaowei;
	}

	public void setYaowei(String yaowei) {
		this.yaowei = yaowei;
	}

	public String getTunwei() {
		return tunwei;
	}

	public void setTunwei(String tunwei) {
		this.tunwei = tunwei;
	}

	public Page<THealthFile> getPage() {
		return page;
	}

	public void setPage(Page<THealthFile> page) {
		this.page = page;
	}

	public int getHosId() {
		return hosId;
	}

	public void setHosId(int hosId) {
		this.hosId = hosId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public TCommunityHospital getCommunityHospital() {
		return communityHospital;
	}

	public void setCommunityHospital(TCommunityHospital communityHospital) {
		this.communityHospital = communityHospital;
	}

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}

	public String getPageHtml() {
		return pageHtml;
	}

	public void setPageHtml(String pageHtml) {
		this.pageHtml = pageHtml;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public THealthFile getHealthFile() {
		return healthFile;
	}

	public void setHealthFile(THealthFile healthFile) {
		this.healthFile = healthFile;
	}

	public List<Object> getChoice() {
		return choice;
	}

	public void setChoice(List<Object> choice) {
		this.choice = choice;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<String> getChoiceBack() {
		return choiceBack;
	}

	public void setChoiceBack(List<String> choiceBack) {
		this.choiceBack = choiceBack;
	}

	public List<String> getBeginList() {
		return beginList;
	}

	public void setBeginList(List<String> beginList) {
		this.beginList = beginList;
	}

	public List<Object> getChoiceFather() {
		return choiceFather;
	}

	public void setChoiceFather(List<Object> choiceFather) {
		this.choiceFather = choiceFather;
	}

	public List<Object> getChoiceMother() {
		return choiceMother;
	}

	public void setChoiceMother(List<Object> choiceMother) {
		this.choiceMother = choiceMother;
	}

	public List<Object> getChoiceBroSis() {
		return choiceBroSis;
	}

	public void setChoiceBroSis(List<Object> choiceBroSis) {
		this.choiceBroSis = choiceBroSis;
	}

	public List<Object> getChoiceChild() {
		return choiceChild;
	}

	public void setChoiceChild(List<Object> choiceChild) {
		this.choiceChild = choiceChild;
	}

	public List<VStaffHos> getAllDoctor() {
		return allDoctor;
	}

	public void setAllDoctor(List<VStaffHos> allDoctor) {
		this.allDoctor = allDoctor;
	}

	public int getDoctorId() {
		return DoctorId;
	}

	public void setDoctorId(int doctorId) {
		DoctorId = doctorId;
	}

	public List<TCommunityHospital> getAllHos() {
		return allHos;
	}

	public void setAllHos(List<TCommunityHospital> allHos) {
		this.allHos = allHos;
	}

	public String getFileNum() {
		return fileNum;
	}

	public void setFileNum(String fileNum) {
		this.fileNum = fileNum;
	}

	public int getAutoId() {
		return autoId;
	}

	public void setAutoId(int autoId) {
		this.autoId = autoId;
	}

	public TMedicalPayMethod getMedicalPayMethod() {
		return medicalPayMethod;
	}

	public void setMedicalPayMethod(TMedicalPayMethod medicalPayMethod) {
		this.medicalPayMethod = medicalPayMethod;
	}

	public List<TMedicalPayMethod> getMedicalPayMethodList() {
		return medicalPayMethodList;
	}

	public void setMedicalPayMethodList(List<TMedicalPayMethod> medicalPayMethodList) {
		this.medicalPayMethodList = medicalPayMethodList;
	}

	public List<String> getMedicalPayMethodName() {
		return medicalPayMethodName;
	}

	public void setMedicalPayMethodName(List<String> medicalPayMethodName) {
		this.medicalPayMethodName = medicalPayMethodName;
	}

	public List<String> getMedicalPayMethodCardNum() {
		return medicalPayMethodCardNum;
	}

	public void setMedicalPayMethodCardNum(List<String> medicalPayMethodCardNum) {
		this.medicalPayMethodCardNum = medicalPayMethodCardNum;
	}

	public int getHealthFileId() {
		return healthFileId;
	}

	public void setHealthFileId(int healthFileId) {
		this.healthFileId = healthFileId;
	}

	public List<String> getDisabilityNameList() {
		return disabilityNameList;
	}

	public void setDisabilityNameList(List<String> disabilityNameList) {
		this.disabilityNameList = disabilityNameList;
	}

	public List<String> getAllergyNameList() {
		return allergyNameList;
	}

	public void setAllergyNameList(List<String> allergyNameList) {
		this.allergyNameList = allergyNameList;
	}

	public List<Object> getChoiceDisabilityName() {
		return choiceDisabilityName;
	}

	public void setChoiceDisabilityName(List<Object> choiceDisabilityName) {
		this.choiceDisabilityName = choiceDisabilityName;
	}

	public List<Object> getChoiceAllergyName() {
		return choiceAllergyName;
	}

	public void setChoiceAllergyName(List<Object> choiceAllergyName) {
		this.choiceAllergyName = choiceAllergyName;
	}

	public List<Object> getDisabilityName() {
		return disabilityName;
	}

	public void setDisabilityName(List<Object> disabilityName) {
		this.disabilityName = disabilityName;
	}

	public List<String> getExposeNameList() {
		return exposeNameList;
	}

	public void setExposeNameList(List<String> exposeNameList) {
		this.exposeNameList = exposeNameList;
	}

	public List<Object> getChoiceExposeName() {
		return choiceExposeName;
	}

	public void setChoiceExposeName(List<Object> choiceExposeName) {
		this.choiceExposeName = choiceExposeName;
	}

	public StaffService getStaffService() {
		return staffService;
	}

	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}

	public TOldStaffHos getOldStaffHos() {
		return oldStaffHos;
	}

	public void setOldStaffHos(TOldStaffHos oldStaffHos) {
		this.oldStaffHos = oldStaffHos;
	}

	public TStaff getTstaff() {
		return tstaff;
	}

	public void setTstaff(TStaff tstaff) {
		this.tstaff = tstaff;
	}

	public List<String> getMedicalPayMethodNameList() {
		return medicalPayMethodNameList;
	}

	public void setMedicalPayMethodNameList(List<String> medicalPayMethodNameList) {
		this.medicalPayMethodNameList = medicalPayMethodNameList;
	}

	public List<TPastHistory> getPastHistoryShouShuList() {
		return pastHistoryShouShuList;
	}

	public void setPastHistoryShouShuList(List<TPastHistory> pastHistoryShouShuList) {
		this.pastHistoryShouShuList = pastHistoryShouShuList;
	}

	public List<TPastHistory> getPastHistoryWaiShangList() {
		return pastHistoryWaiShangList;
	}

	public void setPastHistoryWaiShangList(List<TPastHistory> pastHistoryWaiShangList) {
		this.pastHistoryWaiShangList = pastHistoryWaiShangList;
	}

	public List<TPastHistory> getPastHistoryShuXieList() {
		return pastHistoryShuXieList;
	}

	public void setPastHistoryShuXieList(List<TPastHistory> pastHistoryShuXieList) {
		this.pastHistoryShuXieList = pastHistoryShuXieList;
	}

	public List<String> getMedicalPayMethodCardNumList() {
		return medicalPayMethodCardNumList;
	}

	public void setMedicalPayMethodCardNumList(List<String> medicalPayMethodCardNumList) {
		this.medicalPayMethodCardNumList = medicalPayMethodCardNumList;
	}

	public int getEgg() {
		return egg;
	}

	public void setEgg(int egg) {
		this.egg = egg;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getSavaPartPath() {
		return savaPartPath;
	}

	public void setSavaPartPath(String savaPartPath) {
		this.savaPartPath = savaPartPath;
	}

	public VStaffHos getVstaffHos() {
		return vstaffHos;
	}

	public void setVstaffHos(VStaffHos vstaffHos) {
		this.vstaffHos = vstaffHos;
	}

	public String getChoiceResultFather() {
		return choiceResultFather;
	}

	public void setChoiceResultFather(String choiceResultFather) {
		this.choiceResultFather = choiceResultFather;
	}

	public String getChoiceResultMother() {
		return choiceResultMother;
	}

	public void setChoiceResultMother(String choiceResultMother) {
		this.choiceResultMother = choiceResultMother;
	}

	public String getChoiceResultBroSis() {
		return choiceResultBroSis;
	}

	public void setChoiceResultBroSis(String choiceResultBroSis) {
		this.choiceResultBroSis = choiceResultBroSis;
	}

	public String getChoiceResultChild() {
		return choiceResultChild;
	}

	public void setChoiceResultChild(String choiceResultChild) {
		this.choiceResultChild = choiceResultChild;
	}

	public String getChoiceResultDisability() {
		return choiceResultDisability;
	}

	public void setChoiceResultDisability(String choiceResultDisability) {
		this.choiceResultDisability = choiceResultDisability;
	}

	public String getChoiceResultAllergyName() {
		return choiceResultAllergyName;
	}

	public void setChoiceResultAllergyName(String choiceResultAllergyName) {
		this.choiceResultAllergyName = choiceResultAllergyName;
	}

	public String getChoiceResultExposeName() {
		return choiceResultExposeName;
	}

	public void setChoiceResultExposeName(String choiceResultExposeName) {
		this.choiceResultExposeName = choiceResultExposeName;
	}

	public List<Timestamp> getPastHistoryShouShuTimeList() {
		return pastHistoryShouShuTimeList;
	}

	public void setPastHistoryShouShuTimeList(List<Timestamp> pastHistoryShouShuTimeList) {
		this.pastHistoryShouShuTimeList = pastHistoryShouShuTimeList;
	}

	public List<String> getPastHistoryShouShuNameList() {
		return pastHistoryShouShuNameList;
	}

	public void setPastHistoryShouShuNameList(List<String> pastHistoryShouShuNameList) {
		this.pastHistoryShouShuNameList = pastHistoryShouShuNameList;
	}

	public List<Timestamp> getPastHistoryWaiShangTimeList() {
		return pastHistoryWaiShangTimeList;
	}

	public void setPastHistoryWaiShangTimeList(List<Timestamp> pastHistoryWaiShangTimeList) {
		this.pastHistoryWaiShangTimeList = pastHistoryWaiShangTimeList;
	}

	public List<String> getPastHistoryWaiShangNameList() {
		return pastHistoryWaiShangNameList;
	}

	public void setPastHistoryWaiShangNameList(List<String> pastHistoryWaiShangNameList) {
		this.pastHistoryWaiShangNameList = pastHistoryWaiShangNameList;
	}

	public List<Timestamp> getPastHistoryShuXieTimeList() {
		return pastHistoryShuXieTimeList;
	}

	public void setPastHistoryShuXieTimeList(List<Timestamp> pastHistoryShuXieTimeList) {
		this.pastHistoryShuXieTimeList = pastHistoryShuXieTimeList;
	}

	public List<String> getPastHistoryShuXieNameList() {
		return pastHistoryShuXieNameList;
	}

	public void setPastHistoryShuXieNameList(List<String> pastHistoryShuXieNameList) {
		this.pastHistoryShuXieNameList = pastHistoryShuXieNameList;
	}

	public TPastHistory getPasthistory() {
		return pasthistory;
	}

	public void setPasthistory(TPastHistory pasthistory) {
		this.pasthistory = pasthistory;
	}

	public List<Timestamp> getDiseaseTime() {
		return diseaseTime;
	}

	public void setDiseaseTime(List<Timestamp> diseaseTime) {
		this.diseaseTime = diseaseTime;
	}

	public List<String> getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(List<String> diseaseName) {
		this.diseaseName = diseaseName;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public Date getBirthday() {
		return birthday;
	}

	public VStaffHosSu getVstaffHosSu() {
		return vstaffHosSu;
	}

	public void setVstaffHosSu(VStaffHosSu vstaffHosSu) {
		this.vstaffHosSu = vstaffHosSu;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	private List<TMaintainableEntry> whcd;// 文化程度
	private List<TMaintainableEntry> zy;// 职业
	private List<TMaintainableEntry> hyzk;// 婚姻状况
	private List<TMaintainableEntry> cfpfcs;// 厨房排风措施
	private List<TMaintainableEntry> rllx;// 燃料类型
	private List<TMaintainableEntry> ys;// 饮水
	private List<TMaintainableEntry> qcl;// 禽畜栏
	private List<TMaintainableEntry> cs;// 厕所
	private List<TMaintainableEntry> mz;// 民族

	// //////////////////////////////////////////////////////////////////////////////////////////////
	public List<TMaintainableEntry> getWhcd() {
		return whcd;
	}

	public void setWhcd(List<TMaintainableEntry> whcd) {
		this.whcd = whcd;
	}

	public List<TMaintainableEntry> getZy() {
		return zy;
	}

	public void setZy(List<TMaintainableEntry> zy) {
		this.zy = zy;
	}

	public List<TMaintainableEntry> getHyzk() {
		return hyzk;
	}

	public void setHyzk(List<TMaintainableEntry> hyzk) {
		this.hyzk = hyzk;
	}

	public List<TMaintainableEntry> getCfpfcs() {
		return cfpfcs;
	}

	public void setCfpfcs(List<TMaintainableEntry> cfpfcs) {
		this.cfpfcs = cfpfcs;
	}

	public List<TMaintainableEntry> getRllx() {
		return rllx;
	}

	public void setRllx(List<TMaintainableEntry> rllx) {
		this.rllx = rllx;
	}

	public List<TMaintainableEntry> getYs() {
		return ys;
	}

	public void setYs(List<TMaintainableEntry> ys) {
		this.ys = ys;
	}

	public List<TMaintainableEntry> getQcl() {
		return qcl;
	}

	public void setQcl(List<TMaintainableEntry> qcl) {
		this.qcl = qcl;
	}

	public List<TMaintainableEntry> getCs() {
		return cs;
	}

	public void setCs(List<TMaintainableEntry> cs) {
		this.cs = cs;
	}

	public List<VStaffHos> getAllEnterPeople() {
		return allEnterPeople;
	}

	public void setAllEnterPeople(List<VStaffHos> allEnterPeople) {
		this.allEnterPeople = allEnterPeople;
	}

	public List<TPastHistory> getPastHistoryList() {
		return pastHistoryList;
	}

	public void setPastHistoryList(List<TPastHistory> pastHistoryList) {
		this.pastHistoryList = pastHistoryList;
	}

	public SearchBeanSU getSearchbean() {
		return searchbean;
	}

	public void setSearchbean(SearchBeanSU searchbean) {
		this.searchbean = searchbean;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public List<VHealthStaff> getVhealthStaffList() {
		return vhealthStaffList;
	}

	public void setVhealthStaffList(List<VHealthStaff> vhealthStaffList) {
		this.vhealthStaffList = vhealthStaffList;
	}

	public Page<VHealthStaff> getPagePlus() {
		return pagePlus;
	}

	public void setPagePlus(Page<VHealthStaff> pagePlus) {
		this.pagePlus = pagePlus;
	}

	public int getConFlag() {
		return conFlag;
	}

	public void setConFlag(int conFlag) {
		this.conFlag = conFlag;
	}

	public int[] getChoice1() {
		return choice1;
	}

	public void setChoice1(int[] choice1) {
		this.choice1 = choice1;
	}

	public String getPicStr() {
		return picStr;
	}

	public void setPicStr(String picStr) {
		this.picStr = picStr;
	}

	public boolean isFlagPingTai() {
		return flagPingTai;
	}

	public void setFlagPingTai(boolean flagPingTai) {
		this.flagPingTai = flagPingTai;
	}

	public int getIssq() {
		return issq;
	}

	public void setIssq(int issq) {
		this.issq = issq;
	}

	public List<TCommunityHospital> getHospitals() {
		return hospitals;
	}

	public void setHospitals(List<TCommunityHospital> hospitals) {
		this.hospitals = hospitals;
	}

	public List<TMaintainableEntry> getMz() {
		return mz;
	}

	public void setMz(List<TMaintainableEntry> mz) {
		this.mz = mz;
	}

	public List<TStaff> getStaffs() {
		return staffs;
	}

	public void setStaffs(List<TStaff> staffs) {
		this.staffs = staffs;
	}

	public List<AllInfoBean> getAllInfoBeanList() {
		return allInfoBeanList;
	}

	public void setAllInfoBeanList(List<AllInfoBean> allInfoBeanList) {
		this.allInfoBeanList = allInfoBeanList;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public int[] getXy() {
		return xy;
	}

	public void setXy(int[] xy) {
		this.xy = xy;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public String getIphone() {
		return iphone;
	}

	public void setIphone(String iphone) {
		this.iphone = iphone;
	}
	
	public List<TStaff> getSpecialDoctor() {
		return specialDoctor;
	}

	public void setSpecialDoctor(List<TStaff> specialDoctor) {
		this.specialDoctor = specialDoctor;
	}

	public String getPeopleString() {
		return peopleString;
	}

	public void setPeopleString(String peopleString) {
		this.peopleString = peopleString;
	}

	public int getCp1() {
		return cp1;
	}

	public void setCp1(int cp1) {
		this.cp1 = cp1;
	}

	public void register(){
		password="123456";
		/*String sexstring= sex == true ? "0" : "1";
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String strBirth = df.format(birthday);*/
		results="1";
		///////////////////////////////1
		//results=this.registerHealthMachine(uname, rname, password, idcard, nfc, sexstring, height, weight, strBirth, yaowei, tunwei);
		System.out.println("###"+results);
		this.writeJson(results);
	}
	//注册一体机
	public String registerHealthMachine(String uname, String rname, String password, String idcard, String nfc,
			String sex, String height, String weight, String birthday, String yaowei, String tunwei) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sdf.format(new Date());
        String validKey = MD5.getMd5("cetcnav" + sdf.format(new Date()));

        String strHeight;
		if(height=="null"||height.equals("null")){
			strHeight = "";
		}else{
			strHeight=Double.parseDouble(height)/100+"";
		}
		
		password = MD5.getMd5("123456");
		String para = "json={\"method\":\"register\",\"uname\":\"" + uname +"\",\"key\":\""+validKey
				+ "\",\"rname\":\"" + rname + "\",\"password\":\"" + password
				+ "\",\"idcard\":\"" + idcard + "\",\"sex\":\"" + sex + "\","
				+ "\"height\":\"" + strHeight + "\",\"weight\":\"" + weight
				+ "\"," + "\"birthday\":\"" + birthday + "\",\"yaowei\":\""
				+ yaowei + "\"," + "\"tunwei\":\"" + tunwei + "\",\"nfc\":\""
				+ nfc + "\"}";

		String url = "http://mdapi.siheal.com:8086/SihealDocking/cetcnav";
		System.out.println("url:" + url + "---" + para);
		String ret = HttpRequest.sendPost(url, para);
		System.out.println("ret:" + ret);
		if (ret.equals("")) {
			return "";
		}
		JSONObject jsonObject = JSONObject.parseObject(ret);
		return jsonObject.get("flag").toString();
	}
	
	//更新一体机
	public String updateHealthMachine(String uname, String rname,
			String password, String idcard, String nfc, String sex,
			String height, String weight, String birthday, String yaowei,
			String tunwei) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sdf.format(new Date());
        String validKey = MD5.getMd5("cetcnav" + sdf.format(new Date()));
        
		password = MD5.getMd5("123456");
		
		String strHeight;
		if(height=="null"||height.equals("null")){
			strHeight = "";
		}else{
			strHeight=Double.parseDouble(height)/100+"";
		}
		
		String para ="json={\"method\":\"update\",\"uname\":\""+uname+"\",\"key\":\""+validKey+
				"\",\"rname\":\""+rname+"\",\"password\":\""+password+
				"\",\"sex\":\""+sex+"\","
				+ "\"height\":\""+strHeight+"\",\"weight\":\""+weight+"\","
				+ "\"birthday\":\""+birthday+"\",\"yaowei\":\""+yaowei+"\","
				+ "\"tunwei\":\""+tunwei+"\",\"nfc\":\""+nfc+"\"}";
		
		String url = "http://mdapi.siheal.com:8086/SihealDocking/cetcnav";
		System.out.println("url:" + url + "---" + para);
		String ret = HttpRequest.sendPost(url, para);
		System.out.println("ret:" + ret);
		if(ret.equals("")){
			return "";
		}
		JSONObject jsonObject = JSONObject.parseObject(ret);
		return jsonObject.get("flag").toString();
	}

	public int getIntId() {
		return intId;
	}

	public void setIntId(int intId) {
		this.intId = intId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<TCommunityHospitalGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<TCommunityHospitalGroup> groups) {
		this.groups = groups;
	}

	public THealthFile getHealthFile1() {
		return healthFile1;
	}

	public void setHealthFile1(THealthFile healthFile1) {
		this.healthFile1 = healthFile1;
	}

	public TFileLifeStyle getTlifestyle() {
		return tlifestyle;
	}

	public void setTlifestyle(TFileLifeStyle tlifestyle) {
		this.tlifestyle = tlifestyle;
	}
//生活方式（可维护字段）
	private List<TMaintainableEntry> tydlplList;
	private List<TMaintainableEntry> dlfsList;
	private List<TMaintainableEntry> ysxgList;
	private List<TMaintainableEntry> xyzkList;
	private List<TMaintainableEntry> yjplList;
	private List<TMaintainableEntry> yjzlList;

	public List<TMaintainableEntry> getTydlplList() {
		return tydlplList;
	}

	public void setTydlplList(List<TMaintainableEntry> tydlplList) {
		this.tydlplList = tydlplList;
	}

	public List<TMaintainableEntry> getDlfsList() {
		return dlfsList;
	}

	public void setDlfsList(List<TMaintainableEntry> dlfsList) {
		this.dlfsList = dlfsList;
	}

	public List<TMaintainableEntry> getYsxgList() {
		return ysxgList;
	}

	public void setYsxgList(List<TMaintainableEntry> ysxgList) {
		this.ysxgList = ysxgList;
	}

	public List<TMaintainableEntry> getXyzkList() {
		return xyzkList;
	}

	public void setXyzkList(List<TMaintainableEntry> xyzkList) {
		this.xyzkList = xyzkList;
	}

	public List<TMaintainableEntry> getYjplList() {
		return yjplList;
	}

	public void setYjplList(List<TMaintainableEntry> yjplList) {
		this.yjplList = yjplList;
	}

	public List<TMaintainableEntry> getYjzlList() {
		return yjzlList;
	}

	public void setYjzlList(List<TMaintainableEntry> yjzlList) {
		this.yjzlList = yjzlList;
	}

	public List<TFileLifeStyle> getTlifestyleList() {
		return tlifestyleList;
	}

	public void setTlifestyleList(List<TFileLifeStyle> tlifestyleList) {
		this.tlifestyleList = tlifestyleList;
	}

	public List<HealthFile> getHealthFileCloudList() {
		return healthFileCloudList;
	}

	public void setHealthFileCloudList(List<HealthFile> healthFileCloudList) {
		this.healthFileCloudList = healthFileCloudList;
	}

	public int getHealthCloudId() {
		return healthCloudId;
	}

	public void setHealthCloudId(int healthCloudId) {
		this.healthCloudId = healthCloudId;
	}

	public Item getHealthFileCloudSingle() {
		return healthFileCloudSingle;
	}

	public void setHealthFileCloudSingle(Item healthFileCloudSingle) {
		this.healthFileCloudSingle = healthFileCloudSingle;
	}

	public List<PastHistory> getPastList() {
		return pastList;
	}

	public void setPastList(List<PastHistory> pastList) {
		this.pastList = pastList;
	}

	public List<PastHistory> getPastShouShuList() {
		return pastShouShuList;
	}

	public void setPastShouShuList(List<PastHistory> pastShouShuList) {
		this.pastShouShuList = pastShouShuList;
	}

	public List<PastHistory> getPastWaiShangList() {
		return pastWaiShangList;
	}

	public void setPastWaiShangList(List<PastHistory> pastWaiShangList) {
		this.pastWaiShangList = pastWaiShangList;
	}

	public List<PastHistory> getPastShuXieList() {
		return pastShuXieList;
	}

	public void setPastShuXieList(List<PastHistory> pastShuXieList) {
		this.pastShuXieList = pastShuXieList;
	}

	public List<PastHistory> getPastAllList() {
		return pastAllList;
	}

	public void setPastAllList(List<PastHistory> pastAllList) {
		this.pastAllList = pastAllList;
	}

	public List<FileLifeStyle> getLifestyleList() {
		return lifestyleList;
	}

	public void setLifestyleList(List<FileLifeStyle> lifestyleList) {
		this.lifestyleList = lifestyleList;
	}

	public FileLifeStyle getLifestyle() {
		return lifestyle;
	}

	public void setLifestyle(FileLifeStyle lifestyle) {
		this.lifestyle = lifestyle;
	}

	public TFileMedicalRecord getFileMedicalRecord() {
		return fileMedicalRecord;
	}

	public void setFileMedicalRecord(TFileMedicalRecord fileMedicalRecord) {
		this.fileMedicalRecord = fileMedicalRecord;
	}

	public List<TFileMedicalRecord> getFileMedicalRecordList() {
		return fileMedicalRecordList;
	}

	public void setFileMedicalRecordList(List<TFileMedicalRecord> fileMedicalRecordList) {
		this.fileMedicalRecordList = fileMedicalRecordList;
	}

	public List<String> getFileMedicalListName() {
		return fileMedicalListName;
	}

	public void setFileMedicalListName(List<String> fileMedicalListName) {
		this.fileMedicalListName = fileMedicalListName;
	}

	public List<Timestamp> getFileMedicalListBegin() {
		return fileMedicalListBegin;
	}

	public void setFileMedicalListBegin(List<Timestamp> fileMedicalListBegin) {
		this.fileMedicalListBegin = fileMedicalListBegin;
	}

	public List<Timestamp> getFileMedicalListStop() {
		return fileMedicalListStop;
	}

	public void setFileMedicalListStop(List<Timestamp> fileMedicalListStop) {
		this.fileMedicalListStop = fileMedicalListStop;
	}

	public List<FileMedicalRecord> getMedicalRecordList() {
		return MedicalRecordList;
	}

	public void setMedicalRecordList(List<FileMedicalRecord> medicalRecordList) {
		MedicalRecordList = medicalRecordList;
	}

	public int getIndexFlag() {
		return indexFlag;
	}

	public void setIndexFlag(int indexFlag) {
		this.indexFlag = indexFlag;
	}
	public String getFlagSearch() {
		return flagSearch;
	}

	public void setFlagSearch(String flagSearch) {
		this.flagSearch = flagSearch;
	}

	public List<TFileMedicalRecord> getFileMedicalList() {
		return fileMedicalList;
	}

	public void setFileMedicalList(List<TFileMedicalRecord> fileMedicalList) {
		this.fileMedicalList = fileMedicalList;
	}

	public List<String> getFileMedicalListUsages() {
		return fileMedicalListUsages;
	}

	public void setFileMedicalListUsages(List<String> fileMedicalListUsages) {
		this.fileMedicalListUsages = fileMedicalListUsages;
	}

	public List<String> getFileMedicalListDosage() {
		return fileMedicalListDosage;
	}

	public void setFileMedicalListDosage(List<String> fileMedicalListDosage) {
		this.fileMedicalListDosage = fileMedicalListDosage;
	}

	public List<String> getFileMedicalListMedicationAdherence() {
		return fileMedicalListMedicationAdherence;
	}

	public void setFileMedicalListMedicationAdherence(List<String> fileMedicalListMedicationAdherence) {
		this.fileMedicalListMedicationAdherence = fileMedicalListMedicationAdherence;
	}

	public List<String> getFileMedicalListUntowardEffect() {
		return fileMedicalListUntowardEffect;
	}

	public void setFileMedicalListUntowardEffect(List<String> fileMedicalListUntowardEffect) {
		this.fileMedicalListUntowardEffect = fileMedicalListUntowardEffect;
	}

	public List<String> getFileMedicalListDrugSources() {
		return fileMedicalListDrugSources;
	}

	public void setFileMedicalListDrugSources(List<String> fileMedicalListDrugSources) {
		this.fileMedicalListDrugSources = fileMedicalListDrugSources;
	}

	public String getYemianname() {
		return yemianname;
	}

	public void setYemianname(String yemianname) {
		this.yemianname = yemianname;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public int getBelongSystemQianDuan() {
		return belongSystemQianDuan;
	}

	public void setBelongSystemQianDuan(int belongSystemQianDuan) {
		this.belongSystemQianDuan = belongSystemQianDuan;
	}

	public String getYongLiangCiShu() {
		return yongLiangCiShu;
	}

	public void setYongLiangCiShu(String yongLiangCiShu) {
		this.yongLiangCiShu = yongLiangCiShu;
	}

	public String getMeiCiYongLiang() {
		return meiCiYongLiang;
	}

	public void setMeiCiYongLiang(String meiCiYongLiang) {
		this.meiCiYongLiang = meiCiYongLiang;
	}

	public boolean isEmbeded() {
		return embeded;
	}

	public void setEmbeded(boolean embeded) {
		this.embeded = embeded;
	}
	
	

}
