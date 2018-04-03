package com.jmjk.action;

import com.jmjk.entity.SearchClass;
import com.jmjk.enums.AddPhysicalExamSearch;
import com.jmjk.enums.Admin_IsHead;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jmjk.action.base.BaseAction;
import com.jmjk.entity.TAuxiliaryExamination;
import com.jmjk.entity.TCheck;
import com.jmjk.entity.TChinaMedicine;
import com.jmjk.entity.TCommunityHospital;
import com.jmjk.entity.TCommunityHospitalGroup;
import com.jmjk.entity.TDefendInocu;
import com.jmjk.entity.TGeneralCondition;
import com.jmjk.entity.THF;
import com.jmjk.entity.THealthExtingProblem;
import com.jmjk.entity.THealthFile;
import com.jmjk.entity.THospitalHistory;
import com.jmjk.entity.TLifeStyle;
import com.jmjk.entity.TMaintainableEntry;
import com.jmjk.entity.TMedicalUse;
import com.jmjk.entity.TOldStaffHos;
import com.jmjk.entity.TOrganFunction;
import com.jmjk.entity.TPhysicalExam;
import com.jmjk.entity.TRole;
import com.jmjk.entity.TStaff;
import com.jmjk.entity.view.VExam;
import com.jmjk.entity.view.VHealthStaff;
import com.jmjk.entity.view.VStaffHos;
import com.jmjk.entity.view.VStaffHosSu;
import com.jmjk.service.CommunityHospitalGroupService;
import com.jmjk.service.CommunityHospitalService;
import com.jmjk.service.HealthFileService;
import com.jmjk.service.HealthManagerService;
import com.jmjk.service.MaintainableAttributeService;
import com.jmjk.service.OldStaffHosService;
import com.jmjk.service.PhysicalExamService;
import com.jmjk.service.RoleService;
import com.jmjk.service.StaffService;
import com.jmjk.enums.*;
import com.jmjk.export.PhysicalExport;
import com.jmjk.export.StaffRewardPunishExport;
import com.jmjk.pojo.HealthFileCloud;
import com.jmjk.pojo.PhysicalExamBypersonal;
import com.jmjk.pojo.PhysicalExamBypersonalPageResult;
import com.jmjk.pojo.HealthFileCloud.Page.HealthFile;
import com.jmjk.utils.GetSessionPerson;
import com.jmjk.utils.JxlExcel;
import com.jmjk.utils.Page;
import com.jmjk.utils.Reflex;
import com.jmjk.utils.Resubmit;
import com.jmjk.utils.SearchBeanSU;
import com.jmjk.utils.SearchMulti;
import com.jmjk.utils.TokenProccessor;
import com.opensymphony.xwork2.ActionContext;


@Component
@Scope("prototype")
@Action(value="PhysicalExamAction",results={

		@Result(name="index",location="PhysicalExamAction!firstIndexsearch.action",type="redirectAction"),
		@Result(name="firstIndexsearch",location="/jsp/physicalExam/physicalExamIndex.jsp"),
		@Result(name="searchMore",location="/jsp/physicalExam/physicalExamIndex.jsp"),
		@Result(name="deletePhysicalExam",location="PhysicalExamAction!firstIndexsearch.action",type="redirectAction"),
		@Result(name="showPhysicalExam",location="/jsp/physicalExam/showPhysicalExam.jsp"),
		@Result(name="showYearChecked",location="/jsp/oldfile/oldchronicfile/showYearChecked.jsp"),  //李卓远
		@Result(name="editPhysicalExam",location="PhysicalExamAction!firstIndexsearch.action",type="redirectAction"),
		@Result(name="jumpEditPhysicalExam",location="/jsp/physicalExam/editPhysicalExam.jsp"),
		@Result(name="jumpAddPhysicalExam",location="/jsp/physicalExam/addPhysicalExam.jsp"),
		@Result(name="allDoctor",location="/jsp/physicalExam/addPhysicalExam.jsp"),
		@Result(name="nosearch",location="/jsp/physicalExam/addPhysicalExam.jsp"),
		@Result(name="error",location="/error.jsp"),
		@Result(name="TestError",type="redirect",location="/jsp/physicalExam/addPhysicalExam.jsp"),
		@Result(name="autoAction",location="/login.jsp"),
		@Result(name="showPhysicalExamInHealthAdice",location="/jsp/healthManager/healthPhysicalExamTable.jsp"),
		@Result(name="showPhysicalExamListForHealthAdvice",location="/jsp/healthManager/healthPhysicExm.jsp"),
		
		
		@Result(name="showPhysicalExamListBypersonal",location="/jsp/physicalExam/personalPhysicalExam.jsp"),
		@Result(name="DocDRedir",type="redirectAction",location="PhysicalExamAction!firstIndexsearch.action"),
		@Result(name="addsearchPhysicalExam",location="/jsp/physicalExam/addPhysicalExam.jsp"),
		
		@Result(name = "searchHealthFileKill", location = "/jsp/physicalExam/healthFileMainPlus.jsp"),
		@Result(name = "searchHealthFile", location = "/jsp/physicalExam/healthFileMain.jsp"),
		
		@Result(name = "getHealthFileList", location = "/jsp/healthManager/healthFilelist.jsp"),
		
		@Result(name = "searchHealthFileKillInfo", location = "/jsp/physicalExam/healthFileMainPlusInfo.jsp"),
		@Result(name = "searchHealthFileInfo", location = "/jsp/physicalExam/healthFileMainInfo.jsp"),
		
		@Result(name = "showHealthFile", location = "/jsp/physicalExam/healthFileMain.jsp"),
		@Result(name = "showHealthFileInfo", location = "/jsp/physicalExam/healthFileMainInfo.jsp"),
})
public class PhysicalExamAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	@Autowired
	private PhysicalExamService physicalExamService;
	@Autowired
	private HealthFileService healthFileService;
	@Autowired
	private CommunityHospitalService  communityHospitalService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private OldStaffHosService oldStaffHosService;
	@Autowired
	private MaintainableAttributeService maintainableAttributeService;
	@Autowired
	private CommunityHospitalGroupService communityHospitalGroupService;
	@Autowired
	private RoleService roleService;
	
	
	
	private VExam VExam; //体检视图实体 
	
	private String token;
	private String addToken;
	
	private THealthFile thealthFile; //健康档案表实体
	private List<VHealthStaff> thealthFileList; //健康档案表实体
	private TPhysicalExam tphysicalexam;   		//体检总表
	
	private TLifeStyle tlifestyle;    // 生活方式表 
	private TOrganFunction torganfunction;//脏器功能表
	private TCheck tcheck;					//查体表
	private TChinaMedicine tchinamedicine;	//中医体质辨识表
	private THealthExtingProblem thealthexting; //现存主要问题表
	private TGeneralCondition tgeneralcondition; //一般状况表 
	// generalconditionId
	private TAuxiliaryExamination tauxiliaryexamination; //辅助检查表
	private Integer healthFileId;//健康档案ID
	private List<VExam> vExamsList;
	private Integer physicalExamId;//体检总表ID
	private Integer organfunctionId;//脏器功能表ID
	private Integer checkId;//查体表ID
	private Integer chinaMedicineId;//中医体质辨识表ID
	private Integer healthExistingProblemsId;//现存主要问题表ID
	private Integer generalconditionId;//一般状况表ID
	private Integer lifeId;//生活方式表 ID
	private int auxiliaryExaminationId;//辅助检查表ID
	
	
	private String characteristicSymptom; //症状特点
	
	private int searchMethod; //前台 添加页面 的精确查询 方式 0位档案编号 1为身份证号
	private String searchNum; //上面 那个 的   查询 条件 
	private TCommunityHospital tcommunityHospital; //社区医院实体
	
	private int hosId;  //社区医院ID
	private int healthFileType; //档案类型
	private int cp=1;
	private int cp1 = 1;
	private String pageHtml;
	private List<VExam> vexamList; 

	private String hosName;  //社区医院名称
	private String fileNum;//健康档案编号
	
	private List<THospitalHistory> listTHospitalHistory;  //住院治疗室史
	private List<THF> listHF;   //家庭病床史
	private List<TMedicalUse> listTMedicalUse; //住院用药情况
	private List<TDefendInocu> listTDefendInocu;  //非免疫规划预防接种史
	private List<TStaff> doctorList;
	private List<TMaintainableEntry> zztdList;
	private List<TMaintainableEntry> lnrjkList;
	private List<TMaintainableEntry> lnrzlList;
	private List<TMaintainableEntry> lnrrzList;
	private List<TMaintainableEntry> lnrqgList;
	private List<TMaintainableEntry> kqkcList;
	private List<TMaintainableEntry> clList;
	private List<TMaintainableEntry> ybList;
	private List<TMaintainableEntry> tlList;
	private List<TMaintainableEntry> ydgnList;
	private List<TMaintainableEntry> pfList;
	private List<TMaintainableEntry> gmList;
	private List<TMaintainableEntry> lbjList;
	private List<TMaintainableEntry> lyList;
	private List<TMaintainableEntry> xzszList;
	private List<TMaintainableEntry> zbdmbdList;
	private List<TMaintainableEntry> gmzzList;
	private List<TMaintainableEntry> lxList;
	private List<TMaintainableEntry> nxgjbList;
	private List<TMaintainableEntry> szjbList;
	private List<TMaintainableEntry> xzjbList;
	private List<TMaintainableEntry> xgjbList;
	private List<TMaintainableEntry> ybjbList;
	private List<TMaintainableEntry> jkzdList;
	private List<TMaintainableEntry> wxysList;
	private List<TMaintainableEntry> phzList;
	private List<TMaintainableEntry> qxzList;
	private List<TMaintainableEntry> yangxzList;
	private List<TMaintainableEntry> yinxzList;
	private List<TMaintainableEntry> tszList;
	private List<TMaintainableEntry> srzList;
	private List<TMaintainableEntry> xyzList;
	private List<TMaintainableEntry> qyzList;
	private List<TMaintainableEntry> tbzList;
	private boolean isyear = false;//是否是慢病年检的状态量   false不是慢病年检 true  是
	private String conditionsNum;
	private int conditions;
	private SearchClass searchClass;
	private boolean issetsearch; 
	private int[] ids;
	private int conFlag;
	private String str;
	private VHealthStaff vhealthstaff;

	private int yiyuanflag;
	private int zhuangtai;
	private List<TMaintainableEntry> tydlplList;
	private List<TMaintainableEntry> dlfsList;
	private List<TMaintainableEntry> ysxgList;
	private List<TMaintainableEntry> xyzkList;
	private List<TMaintainableEntry> yjplList;
	private List<TMaintainableEntry> yjzlList;
	private List<TCommunityHospital> hospitalsList;
	private TCommunityHospital admin;
	private boolean issq;
	private int responsibility;
	private List<TStaff> specialDoctor;
	private boolean showwindow;
	private int sq = 2;// 判断是否是平台管理员的状态量，用于是否显示社区医院搜索医院选项
	private List<TCommunityHospital> hospitals; // 社区医院list
	private List<VHealthStaff> vhealthStaffList;
	private int state = 2;// 档案状态
	private List<THealthFile> healthFileList;
	private boolean flagPingTai = false;
	private TCommunityHospital communityHospital;
	private List<TCommunityHospitalGroup> groups;
	private List<VStaffHos> allDoctor;
	private List<VStaffHos> allEnterPeople;
	private List<TStaff> staffs;
	private TStaff tstaff;
	private boolean flag = false;
	private Page<VHealthStaff> pagePlus;
	private SearchBeanSU searchbean;// （多条件查询）
	private THealthFile healthFile;
	private int belongSystem;
	private PhysicalExamBypersonal physicalExamBypersonal;
	private int indexFlag=0;
	private List<HealthFile> healthFileCloudList;
	@Autowired
	HealthManagerService healthManagerService;
	
	private String flagSearch;//健康建议，首页多条件查询调同一个查询方法标记
	private boolean chaojiGuanLiYuanFlag;
	private String jiGouFileNum;
	private String jiGouname;
	private boolean jiGousex;
	
	//用于判断是否嵌入到门诊接诊记录页面 false:没有嵌入，true:嵌入
		private boolean embeded = false;

	

	//导出
	public String fuction() throws Exception{
		if(conFlag==1){
		List<PhysicalExport> physicalExportsList=new ArrayList<PhysicalExport>();
		if(ids!=null){
			HttpSession session = ServletActionContext.getRequest().getSession();
			TCommunityHospitalGroup group=(TCommunityHospitalGroup) session.getAttribute("communityHospitalGroup");
			if(group!=null){
				for(int i=0;i<ids.length;i++){
				VExam=physicalExamService.searchVExamViewByphysicalexamId(ids[i]);
					if(VExam!=null){
						PhysicalExport physicalExport=new PhysicalExport(VExam);
						physicalExportsList.add(physicalExport);
					}
				}
			}else{
			int a=GetSessionPerson.getPerson();	
				for(int i=0;i<ids.length;i++){
					if(a<0){
						VExam=physicalExamService.getOneTPhysicalExamBy54(ids[i]);
					}else if(a>0){
						VExam=physicalExamService.getOneTPhysicalExamBy54(ids[i]);
					}else{
					VExam=physicalExamService.searchVExamViewByphysicalexamId(ids[i]);
					}
						if(VExam!=null){
							PhysicalExport physicalExport=new PhysicalExport(VExam);
							physicalExportsList.add(physicalExport);
						}
				}
			}
			
		}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("content-disposition", "attachment;filename="+"Physical"+".xls");
			ServletOutputStream output = response.getOutputStream();
			PhysicalExport physicalExport=new PhysicalExport();
			String [] titles = physicalExport.getTitles();
    		JxlExcel he = new JxlExcel(titles);
    		he.write(physicalExportsList, output);
    		response.flushBuffer();
		return null;
		}
		if(conFlag==2){
			List<PhysicalExport> physicalExportsList=new ArrayList<PhysicalExport>();
			String[] titles=(new PhysicalExport()).getTitles();
			vExamsList = fuctionGetAllPhysilcal();
			for(VExam VExam : vExamsList){
				if(VExam!=null){
					PhysicalExport physicalExport=new PhysicalExport(VExam);
					physicalExportsList.add(physicalExport);
				}
			}
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setHeader("content-disposition", "attachment;filename="+"Physical"+".xls");
				ServletOutputStream output = response.getOutputStream();
	    		JxlExcel he = new JxlExcel(titles);   
	    		he.write(physicalExportsList, output);
	    		response.flushBuffer();
	    		return null;
			}
		return  "fuction";
	}
	
	
	 public List<VExam> fuctionGetAllPhysilcal(){
		 HttpSession session = ServletActionContext.getRequest().getSession();
			TCommunityHospitalGroup group=(TCommunityHospitalGroup) session.getAttribute("communityHospitalGroup");
			if(group!=null){//集团管理员导出本地全部
				 vexamList = physicalExamService.searchVExamView(healthFileId);
				 return vexamList;
				}
			int a=GetSessionPerson.getPerson();	
			if(a==0){              //系统管理员导出本地全部
				 vexamList = physicalExamService.searchVExamView(healthFileId);
				 return vexamList;
				}
			if(a<0){	//说明是社区管理员 云端读取数据
				PhysicalExamBypersonal pb=physicalExamService.getPhysicalExamBypersonalBy54(healthFileId,Belong_System.shequ.getValue());
				for(PhysicalExamBypersonalPageResult p:pb.getPage().getResult()){
					VExam ve=physicalExamService.getOneTPhysicalExamBy54(p.getPhysicalexamid());
					List<VExam> vList=new ArrayList<VExam>();	
					vList.add(ve);
					vexamList=vList;
				}
				return vexamList;
			}
			if(a>0){	//这里是员工！  有责任医生和  非责任医生之分   非责任医生享受   社区负责人权限
				PhysicalExamBypersonal pb=physicalExamService.getPhysicalExamBypersonalBy54(healthFileId,Belong_System.shequ.getValue());
				for(PhysicalExamBypersonalPageResult p:pb.getPage().getResult()){
					VExam ve=physicalExamService.getOneTPhysicalExamBy54(p.getPhysicalexamid());
					List<VExam> vList=new ArrayList<VExam>();	
					vList.add(ve);
					vexamList=vList;
				}
				return vexamList;
			}
			return vexamList;
	 }
	
	public String addPhysicalExam(){
		HttpServletRequest request=ServletActionContext.getRequest();
		boolean b = Resubmit.isRepeatSubmit(request,addToken,"addToken");//判断用户是否是重复提交
		if(b==true){
			return "DocDRedir";  //重定向回首页
		}
		request.getSession().removeAttribute("addToken");//移除session中的token
		//
		tphysicalexam.setBelongSystem(String.valueOf(Belong_System.shequ.getValue()));//设置belongsystem
		
		tphysicalexam.setTOrganFunction(new TOrganFunction(physicalExamService.saveTOrganFunction(torganfunction)));
		tphysicalexam.setTCheck(new TCheck(physicalExamService.saveTCheck(tcheck)));
		tphysicalexam.setTChinaMedicine(new TChinaMedicine(physicalExamService.saveTChinaMedicine(tchinamedicine)));
		tphysicalexam.setTLifeStyle(new TLifeStyle(physicalExamService.saveTLifeStyle(tlifestyle)));
		tphysicalexam.setTHealthExtingProblem(new THealthExtingProblem(physicalExamService.saveTHealthExtingProblem(thealthexting)));
		tphysicalexam.setTGeneralCondition(new TGeneralCondition(physicalExamService.saveTGeneralCondition(tgeneralcondition)));
		tphysicalexam.setTAuxiliaryExamination(new TAuxiliaryExamination(physicalExamService.saveTAuxiliaryExamination(tauxiliaryexamination)));
		tphysicalexam.setTHealthFile(thealthFile);
		  
		
		if(thealthFile.getHealthFileId()==null || thealthFile.getHealthFileId()==0){
			 return "error";
		}
		if(tphysicalexam.getCharacteristicSymptom()==null || tphysicalexam.getPhysicalExamType()==null || tphysicalexam.getPhysicalExamDate()==null || tphysicalexam.getResponsibleDoctor()==null || tphysicalexam.getOperator()==null){
			 return "error";
		}
	try{
		physicalExamService.saveTPhysicalExam(tphysicalexam); 
		
		int a=GetSessionPerson.getPerson();	
		if(a<0){//说明是社区管理员
			tphysicalexam.setOne(String.valueOf(tphysicalexam.getPhysicalExamId()));
				boolean re=physicalExamService.saveTPhysicalExamBy54(tphysicalexam, tauxiliaryexamination, tcheck, 
						tchinamedicine, tgeneralcondition, thealthexting, tlifestyle, torganfunction);
					if(re==false){
						return "error";
					}
		}
		if(a>0){ //这里是员工！  有责任医生和  非责任医生之分   非责任医生享受   社区负责人权限
			tphysicalexam.setOne(String.valueOf(tphysicalexam.getPhysicalExamId()));
			boolean re=physicalExamService.saveTPhysicalExamBy54(tphysicalexam, tauxiliaryexamination, tcheck, 
					tchinamedicine, tgeneralcondition, thealthexting, tlifestyle, torganfunction);
				if(re==false){
					return "error";
				}
		}
		}  catch (SecurityException e) {
      return "TestError";
		} catch (IllegalArgumentException e) {
			return "TestError";
		}
	
	
	//添加 体检 4史List表
		//删除 List中 空元素
		listTHospitalHistory.removeAll(Collections.singleton(null));
		//定义操作计数器
		int i=0;
		//循环添加外键
		for(THospitalHistory s:listTHospitalHistory){
			//判断 病案号 是否为空，若为空  则是 一条空字段。不对其操作
			i=0;
			if(s.getMedicalRecordNum().getBytes().length!=0){
			s.setTPhysicalExam(tphysicalexam);
			  i++;
			}
		}
					//起码操作一次 之后 才给 save
					if(i!=0){
					physicalExamService.saveTHospitalHistory(listTHospitalHistory);
					}
				
		listHF.removeAll(Collections.singleton(null));
		for(THF f:listHF){
			i=0;
			if(f.getMedicalRecordNum().getBytes().length!=0){
			f.setTPhysicalExam(tphysicalexam);
				i++;
			}		
		}   		if(i!=0){
						physicalExamService.saveTHF(listHF);
						}
						
		listTMedicalUse.removeAll(Collections.singleton(null));
		for(TMedicalUse m:listTMedicalUse){
			i=0;
			if(m.getMedicalName().getBytes().length!=0){
			m.setTPhysicalExam(tphysicalexam);
			i++;
			}
		}
					if(i!=0){
						physicalExamService.saveTMedicalUse(listTMedicalUse);
					}
		listTDefendInocu.removeAll(Collections.singleton(null));
		for(TDefendInocu d:listTDefendInocu){
			i=0;
			if(d.getVaccineName().getBytes().length!=0){
			d.setTPhysicalExam(tphysicalexam);
			i++;
			}
		}
					if(i!=0){
						physicalExamService.saveTDefendInocu(listTDefendInocu);
					}
		return "index";
	}
	
	
	public String showPhysicalExamListBypersonal(){
		
		String jiankangjianyi=""; //朱德江健康建议页面使用！！！！！！
		if(healthFileId<0){
			healthFileId=Math.abs(healthFileId);
			jiankangjianyi="showPhysicalExamListForHealthAdvice";
		}
		
		HttpSession session = ServletActionContext.getRequest().getSession();
		TCommunityHospitalGroup group=(TCommunityHospitalGroup) session.getAttribute("communityHospitalGroup");
		if(group!=null){
			Page<VExam> page=new Page<VExam>();
	        page.setCurrentPage(cp);
	        page.setTotalNum(physicalExamService.getPhysicalExamListByHealthFileId(healthFileId));
	        vexamList=physicalExamService.getPhysicalExamListByHealthFileId(healthFileId,page);
	        pageHtml = page.getPage("PhysicalExamAction!showPhysicalExamListBypersonal.action?healthFileId="+healthFileId+"&embeded="+embeded); 
	        chaojiGuanLiYuanFlag=true;
	        return"showPhysicalExamListBypersonal";
		}
		
		int a=GetSessionPerson.getPerson();	
		if(a<0){				//说明是社区管理员
				physicalExamBypersonal=physicalExamService.getPhysicalExamBypersonalBy54(healthFileId, belongSystem,cp);
			 	Page<PhysicalExamBypersonal> page=new Page<PhysicalExamBypersonal>();
	            page.setCurrentPage(cp);
	            page.setTotalNum(physicalExamBypersonal.getPage().getTotal());
	            pageHtml = page.getPage("PhysicalExamAction!showPhysicalExamListBypersonal.action?healthFileId="+healthFileId+"&belongSystem="+belongSystem+"&embeded="+embeded);
	            
			if(jiankangjianyi.equals("")){
	            return"showPhysicalExamListBypersonal";
			}else{
				return jiankangjianyi;
			}
		}
		if(a>0){ //这里是员工！  有责任医生和  非责任医生之分   非责任医生享受   社区负责人权限
			/*if(healthFileService.getOldStaffHosByStaffId(a)){//判断是否责任医生
*/					physicalExamBypersonal=physicalExamService.getPhysicalExamBypersonalBy54(healthFileId, belongSystem,cp);
					Page<PhysicalExamBypersonal> page=new Page<PhysicalExamBypersonal>();
		            page.setCurrentPage(cp);
		            page.setTotalNum(physicalExamBypersonal.getPage().getTotal());
		            pageHtml = page.getPage("PhysicalExamAction!showPhysicalExamListBypersonal.action?healthFileId="+healthFileId+"&belongSystem="+belongSystem+"&embeded="+embeded); 
		            
		            if(jiankangjianyi.equals("")){
			            return"showPhysicalExamListBypersonal";
					}else{
						return jiankangjianyi;
					}
			/*}*/
		}
		Page<VExam> page=new Page<VExam>();
        page.setCurrentPage(cp);
        page.setTotalNum(physicalExamService.getPhysicalExamListByHealthFileId(healthFileId));
        vexamList=physicalExamService.getPhysicalExamListByHealthFileId(healthFileId,page);
        pageHtml = page.getPage("PhysicalExamAction!showPhysicalExamListBypersonal.action?healthFileId="+healthFileId+"&embeded="+embeded); 
        chaojiGuanLiYuanFlag=true;
        return"showPhysicalExamListBypersonal";
	 }
	
		/* 用于添加页面内 的精确搜索  */
		public String addsearchPhysicalExam(){
			int a=GetSessionPerson.getPerson();	
					if(searchNum == null || searchNum.trim().equals("")){ //当搜索 条件为空时  
						thealthFile=null;
						str="请输入信息再查询";
						return "nosearch";
					}
				//使用姓名查询
				if(searchMethod==AddPhysicalExamSearch.name.getvalue()){	
					if(a<0){//说明是社区管理员
						hosId=Math.abs(a);
						//thealthFileList=healthFileService.getVHealthStaffByOldNameForNormal(hosId, searchNum);
						thealthFileList=physicalExamService.getVHealthStaffBy54(searchNum,null,null,1,hosId);
					}
					if(a>0){
							if(healthFileService.getOldStaffHosByStaffId(a)){//判断是否责任医生
							/*}else{*/ //一般员工 享受社区负责人权限  所以取消
								//hosId=staffService.getCommHospitalByStaffId(a);
								//thealthFileList=healthFileService.getVHealthStaffByOldNameForNormal(hosId, searchNum);
								thealthFileList=physicalExamService.getVHealthStaffBy54(searchNum,null,null,2,a);
							}
					}
					if(a==0){              //说明是平台管理员 不能进行添加操作
			 			return "error";
			 		} 
					if(thealthFileList==null){
						//thealthFileList.clear();
						thealthFile=null;
						str="未找到该老人信息";
						return "nosearch";
					}
					if(thealthFileList.size()==1){
						thealthFile=healthFileService.getHealthFileById(thealthFileList.get(0).getHealthFileId());
						thealthFileList.clear();
						return "addsearchPhysicalExam";
					}
					if(thealthFileList.size()>1){
						showwindow=true;
						return "addsearchPhysicalExam";
					}
				}
				if(searchMethod==AddPhysicalExamSearch.idNum.getvalue()||searchMethod==AddPhysicalExamSearch.healthFileId.getvalue())
				{
					if(searchMethod==AddPhysicalExamSearch.idNum.getvalue()){ 	//使用身份证进行搜索
						if(a<0){ //说明是社区管理员
							hosId=Math.abs(a);
							thealthFile=physicalExamService.getThealthFileBy54(null,searchNum,null,1,hosId);
							if(thealthFile!=null){
								return "addsearchPhysicalExam";
							}else{
								thealthFile=null;
								str="未找到该老人信息";
								return "nosearch";
							}     
							/*if(thealthFile!=null){ //通过用 身份证查出的健康档案实体  获取其ID  
							TOldStaffHos oldStaffHos=healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, thealthFile.getHealthFileId());//根据医院ID和健康档案Id得到 OldStaffHos实体,
								if(oldStaffHos==null){
									thealthFile=null;
									str="未找到该老人信息";
									return "nosearch";
								}
								if(oldStaffHos.getTCommunityHospital().getCommunityHospitalId()==hosId){ //判断当前搜索出来的 健康关系表 的 社区医院 是不是当前社区负责人所在社区医院
									thealthFile=physicalExamService.getThealthFileBy54(null,searchNum,null);
								return "addsearchPhysicalExam";
									}else{
										thealthFile=null;
										str="未找到该老人信息";
									return "nosearch";
								     }
							}else{	
								thealthFile=null;
								str="未找到该老人信息";
								return "nosearch";
							 }*/
							}else if(a>0){ //说明员工
										hosId=staffService.getCommHospitalByStaffId(a);
										thealthFile=physicalExamService.getThealthFileBy54(null,searchNum,null,1,hosId);
										if(thealthFile!=null){
											return "addsearchPhysicalExam";
										}else{
											thealthFile=null;
											str="未找到该老人信息";
											return "nosearch";
										}
										/*if(healthFileService.getOldStaffHosByStaffId(a)){
										thealthFile=healthFileService.getHealthFileByIdNum(searchNum);
										if(thealthFile!=null){
											TOldStaffHos oldStaffHos =healthFileService.getTOldStaffHosByhealthId(thealthFile.getHealthFileId());
											if(oldStaffHos==null || oldStaffHos.getTStaff()==null || oldStaffHos.getTStaff().getStaffId()==null || oldStaffHos.getTStaff().getStaffId()==0){
												thealthFile=null;
												str="未找到该老人信息";
												return "nosearch";
											}
											int staffId=oldStaffHos.getTStaff().getStaffId();
											if(a==staffId){    //说明是 被搜索 人的 责任医生
												thealthFile=healthFileService.getHealthFileByIdNum(searchNum);
												return"addsearchPhysicalExam";
											}else{
												thealthFile=null;
												str="未找到该老人信息";
												return "nosearch";
											   }
										  }else{
											  thealthFile=null;
											  str="未找到该老人信息";
												return "nosearch";
											  }
										}else{ //说明是员工 不是责任医生 享有社区负责人权限
											hosId=staffService.getCommHospitalByStaffId(a);
											thealthFile=healthFileService.getHealthFileByIdNum(searchNum);
											if(thealthFile!=null){ 	//通过用 身份证查出的健康档案实体  获取其ID 判断是否存在
											TOldStaffHos oldStaffHos=healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, thealthFile.getHealthFileId());//根据医院ID和健康档案Id得到 OldStaffHos实体,
												if(oldStaffHos!=null){
												thealthFile=healthFileService.getHealthFileByIdNum(searchNum);
														return "addsearchPhysicalExam";
												}else{
													thealthFile=null;
													str="未找到该老人信息";
													return "nosearch";
												  }
											}else{
												thealthFile=null;
												str="未找到该老人信息";
												return "nosearch";
											}
										}*/
									}if(a==0){//说明是平台 管理员查本地
										thealthFile=healthFileService.getHealthFileByIdNum(searchNum);
										return "addsearchPhysicalExam";
									}
			}else{	//说明当前使用档案编号 进行 搜索 
						if(a<0){ //说明是社区管理员
							hosId=Math.abs(a);
							thealthFile=physicalExamService.getThealthFileBy54(null,null,searchNum,1,hosId);
							if(thealthFile!=null){
								return "addsearchPhysicalExam";
							}else{
								thealthFile=null;
								str="未找到该老人信息";
								return "nosearch";
							}
							
							
							/*hosId=Math.abs(a);
							thealthFile=healthFileService.getHealthFileByFileNum(searchNum);
							if(thealthFile!=null){ //通过用 身份证查出的健康档案实体  获取其ID  存不存在 
							TOldStaffHos oldStaffHos=healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, thealthFile.getHealthFileId());//根据医院ID和健康档案Id得到 OldStaffHos实体,
								if(oldStaffHos!=null){
									thealthFile=healthFileService.getHealthFileByFileNum(searchNum);
									return "addsearchPhysicalExam";
								 }else{
									 str="未找到该老人信息";
									 thealthFile=null;
										return "nosearch";
									}
							 }else{
								 str="未找到该老人信息";
								 thealthFile=null;
								 return"nosearch";
							   }*/
						 }else if(a>0){//说明是员工
							    hosId=staffService.getCommHospitalByStaffId(a);
							 	thealthFile=physicalExamService.getThealthFileBy54(null,null,searchNum,1,hosId);
								if(thealthFile!=null){
									return "addsearchPhysicalExam";
								}else{
									thealthFile=null;
									str="未找到该老人信息";
									return "nosearch";
								}
							 		
								/*if(healthFileService.getOldStaffHosByStaffId(a)){ 	//说明是责任医生
							 			thealthFile=healthFileService.getHealthFileByFileNum(searchNum);
							 			if(thealthFile!=null){     //根据输入的 档案编号   是否能搜索到 档案实体
							 			//	int b=thealthFile.getHealthFileId();  档案不为null时得到 档案ID
							 				TOldStaffHos oldStaffHos=healthFileService.getTOldStaffHosByhealthId(thealthFile.getHealthFileId()); //通过健康档案ID 得到 关系表实体
							 				
							 				if(oldStaffHos==null || oldStaffHos.getTStaff()==null || oldStaffHos.getTStaff().getStaffId()==null || oldStaffHos.getTStaff().getStaffId()==0){ //如果这个关系表 里的员工 没有 即没有责任医生
							 					str="未找到该老人信息";
								 				thealthFile=null;
							 					return "nosearch";
							 				}
							 				int staffId=oldStaffHos.getTStaff().getStaffId();
							 				  if(a==staffId){    //说明是 被搜索 人的 责任医生
												thealthFile=healthFileService.getHealthFileByFileNum(searchNum);
												return"addsearchPhysicalExam";
											      }else{
											    	  str="未找到该老人信息";
											    	  thealthFile=null;
											     	return "nosearch";
											     	}
							 			 }else{	
							 				str="未找到该老人信息";
							 				thealthFile=null;
							 				return "nosearch";  
							 			  }
							 		}else { //说明是员工 不是责任医生 享有社区负责人权限
							 			hosId=staffService.getCommHospitalByStaffId(a);
							 			thealthFile=healthFileService.getHealthFileByFileNum(searchNum);
							 			if(thealthFile!=null){ //通过用 身份证查出的健康档案实体  获取其ID 查询存不存在
												TOldStaffHos oldStaffHos=healthFileService.getOldStaffHosByHosIdAndHeathFileId(hosId, thealthFile.getHealthFileId());//根据医院ID和健康档案Id得到 OldStaffHos实体,
												if(oldStaffHos!=null){
													thealthFile=healthFileService.getHealthFileByFileNum(searchNum);
													return "addsearchPhysicalExam";
													}else{
														str="未找到该老人信息";
														thealthFile=null;
														return "nosearch";
													  }
									 	  }else{
									 		 str="未找到该老人信息";
									 		thealthFile=null;
									 		 return "nosearch";
									 		 }
							 		}*/
							 
						 		}if(a==0){              //说明是平台管理员 不能进行添加操作
						 			return "index";
						 		}else{
						 			return"error";
						 		}
						}
				
				}
				str="未找到该老人信息";
				thealthFile=null; 
				return "nosearch"; //搜索条件 不是 档案编号  或者身份证号的 时候
		
		}
		
//=============================================以下是体检 首页 采用健康档案首页 源代码！！！！！！！！！！！！！！===========================		
		public HealthFileCloud getHealthFilePageInfoByDoctor(int doctorId){
			String resultJson = healthManagerService.getHealthFileFromCloudByDoctor(doctorId, 1, 10);
			return JSON.parseObject(resultJson,HealthFileCloud.class);
		}
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
		
		
		
		/*首页自动触发  的展示   当前社区  档案状态正常的  体检 档案*/
		public String firstIndexsearch(){
			if(roleService.getRoleByHosIdForSu(0)==null){
				TRole role=new TRole();
				role.setCommunityId(0);
				role.setRoleName("11");
				roleService.addRoleForSu(role);
			};
			
			Page<THealthFile> page = new Page<THealthFile>();
			page.setCurrentPage(cp);
			/*healthFileCloudList=getHealthFileListInfo(page);
			this.pageHtml = page.getPage("healthFileAction!showHealthFile.action");
			return "showHealthFileInfo";*/
			HttpSession session = ServletActionContext.getRequest().getSession();
			TCommunityHospitalGroup group=(TCommunityHospitalGroup) session.getAttribute("communityHospitalGroup");
			if(group!=null){//集团负责人登陆
				sq = 1;//0是平台管理员登陆，1是集团登陆，2是其他登陆
				hospitals = communityHospitalGroupService.getComHosListByGroup(group);
				page.setTotalNum(healthFileService.getHealthFileCountForGroup(group.getGId()));
				this.pageHtml = page.getPage("PhysicalExamAction!firstIndexsearch.action");
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
					sq = 0;//0是平台管理员登陆，1是集团登陆，2是其他登陆
					page.setTotalNum(healthFileService.getHealthFileCount());
					this.pageHtml = page.getPage("PhysicalExamAction!firstIndexsearch.action");
					healthFileList = healthFileService.getAllHealthFile(page, state);
					allDoctor = healthFileService.getAllDoctorByHosId();
					allEnterPeople = staffService.getAllOperator();
					staffs = staffService.getStaffByCommunityHospitalId(communityHospital.getCommunityHospitalId());
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
					staffs = staffService.getStaffByCommunityHospitalId(communityHospital.getCommunityHospitalId());
					/*return "showHealthFile";*/
					//服务器接口--
					healthFileCloudList=getHealthFileListInfo(page);
					this.pageHtml = page.getPage("PhysicalExamAction!firstIndexsearch.action");
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
					staffs = staffService.getStaffByCommunityHospitalId(hosId);
					indexFlag=1;
					flag = true;
					//服务器接口--
					healthFileCloudList=getHealthFileListInfo(page);
					this.pageHtml = page.getPage("PhysicalExamAction!firstIndexsearch.action");
					return "showHealthFileInfo";
					//--服务器接口
				} else {
					page.setTotalNum(healthFileService.getHealthFileCount(communityHospital.getCommunityHospital()));
					String hosname=communityHospital.getCommunityHospital();
					healthFileList = healthFileService.getAllHealthFile(page, state,hosname);
					allDoctor = healthFileService.getAllDoctorByHosId(hosId);
					allEnterPeople = staffService.getAllOperator(hosId);
					specialDoctor =healthFileService.getSpeciallDoctor(hosId);
					staffs = staffService.getStaffByCommunityHospitalId(hosId);
				}

				this.pageHtml = page.getPage("PhysicalExamAction!firstIndexsearch.action");
				return "showHealthFile";
			} else {
				return "error";
			}
			
		}
//===================================================首页结束========================================================	
		public List<VExam> vexamOneself(){
			 int a=GetSessionPerson.getPerson(); 
			 if(a<0){
			hosId=Math.abs(a);    			//获取当前社区负责人 所在医院的Id
			vexamList = physicalExamService.searchVExamByHosId(hosId);
			}		 
			 else if(a>0){  	//说明是员工 
				if(healthFileService.getOldStaffHosByStaffId(a)){  //再判断是否是责任医生
				//当前是责任医生  只能查看 当前的 自己所管辖的 档案
					vexamList = physicalExamService.searchVExamViewByStaffId(a);
				}else if(!healthFileService.getOldStaffHosByStaffId(a)){ //是员工  不是责任医生   享有社区负责人一样的   查看权限
						hosId=staffService.getCommHospitalByStaffId(a);	 //通过员工ID 得到所在社区医院ID 	
						vexamList = physicalExamService.searchVExamByHosId(hosId);
						}
			 	}else {//即 a=0 的时候  表示的是 平台管理员登录
				vexamList = physicalExamService.getAllVExamView();
			} 
			return vexamList;
		}
		
		
		public void getVexamBya(){
			healthFileType=HealthFileCheck.zhengchang.getValue();  		//档案状态=2 正常
			Page<VExam> page= new Page<VExam>();
			page.setCurrentPage(cp);
			page.setTotalNum(physicalExamService.searchVExamByFileTypeHosId(healthFileType, hosId).size());
			this.pageHtml = page.getPage("PhysicalExamAction!firstIndexsearch.action");
			vexamList = physicalExamService.searchVExamByFileTypeHosId(healthFileType, hosId, page);
		}
		
		//用于 添加 健康体检JSP页面   获取 当前医院 所有员工
		public String allDoctor(){
			int a =GetSessionPerson.getPerson();
			if(a<0){
				int b=Math.abs(a); //a<0 表示当前 登录时社区负责人   b表示社区负责人 所在的社区ID
				 doctorList=staffService.getStaffByCommunityHospitalId(b);
			}else if(a>0){
				int b=staffService.getCommHospitalByStaffId(a);	
				 doctorList=staffService.getStaffByCommunityHospitalId(b);
			}if(a==0){
				//doctorList = staffService.getStaffByCommunityHospitalId(admin.getCommunityHospitalId());
				return"index";
			}
			return "allDoctor";
		}
		
		/*  根据 前台 传 回来的  体检ID 和  社区医院 ID（校验）  查看某一  体检记录 详情*/
		public String showPhysicalExam(){
//================================朱德江开始，郑富良写的=========================================			
			String jiankangjianyi="";
			if(physicalExamId<0){	
			 physicalExamId=Math.abs(physicalExamId);
			 jiankangjianyi="showPhysicalExamInHealthAdice";
		}
			
//------------------------------------------↓李卓远↓---------------------------------------------------------------------------------------
			HttpSession session = ServletActionContext.getRequest().getSession();
			TCommunityHospitalGroup group = (TCommunityHospitalGroup) session
					.getAttribute("communityHospitalGroup");
			if(group != null){
				int gid = group.getGId();
				//VExam=physicalExamService.searchVExamViewByphysicalexamId(physicalExamId);//被zfl屏蔽
				//VExam=physicalExamService.getOneTPhysicalExamBy54();   //zfl添加
				VExam=physicalExamService.searchVExamViewByphysicalexamId(physicalExamId);//zfl添加
				getList();
				int hid = VExam.getHealthFileId();
				vhealthstaff = healthFileService.getGroup(hid);
				if(gid!=vhealthstaff.getGId()){
					return "error";
				}else{
					if(isyear == true){
						return "showYearChecked";
					}
						return"showPhysicalExam";
				}
			}else{//李卓远  2017-1-16所加else开始
				
//-------------------------------------------↑李卓远↑--------------------------------------------------------------------------------------			
			
			int a=GetSessionPerson.getPerson();
			//小于0 说明当前是 社区负责人   
			if(a<0){
			hosId=Math.abs(a);  //获取社区ID 绝对值 
			if(isyear == true){//李卓远
				VExam=physicalExamService.searchVExamViewByphysicalexamId(physicalExamId);//zfl添加
				getList();
				return "showYearChecked";//李卓远
			}//李卓远
			
			VExam=physicalExamService.getOneTPhysicalExamBy54(physicalExamId);
			
			if(VExam!=null){ // VExam 存在 且 没有被删除！！
					if(jiankangjianyi.equals("")){//朱德江 功能！！
						return"showPhysicalExam";
					}else{
						return jiankangjianyi;
					}
			}else{
				return "error";
			   }
			}else if(a>0){  //说明是员工
				if(healthFileService.getOldStaffHosByStaffId(a)){ //说明不仅是 员工还是责任医生   责任医生只能查看自己 老人的 VExam
					/*VExam=physicalExamService.getVExamByStaffIdPhysicalId(a, physicalExamId); 
					if(VExam!=null  && VExam.getStatus()==null){ // VExam 存在 且 没有被删除！！*/
					
					if(isyear == true){//李卓远
						VExam=physicalExamService.searchVExamViewByphysicalexamId(physicalExamId);//zfl添加
						getList();
						return "showYearChecked";//李卓远
					}//李卓远
					
					VExam=physicalExamService.getOneTPhysicalExamBy54(physicalExamId);  //从54服务器获取数据
					if(VExam!=null){
					/*getList();*/
							if(jiankangjianyi.equals("")){
								return"showPhysicalExam";
							}else{
								return jiankangjianyi;
							}
					}else{
						return "error";
					}
				}else{		      		//说明是一般员工  不是责任医生  享有社区负责人权限 
					hosId=staffService.getCommHospitalByStaffId(a);
					if(isyear == true){//李卓远
						VExam=physicalExamService.searchVExamViewByphysicalexamId(physicalExamId);//zfl添加
						getList();
						return "showYearChecked";//李卓远
					}//李卓远
					VExam=physicalExamService.getOneTPhysicalExamBy54(physicalExamId);  //从54服务器获取数据
					
					if(VExam!=null){
						/*getList();*/
								if(jiankangjianyi.equals("")){
									return"showPhysicalExam";
								}else{
									return jiankangjianyi;
								}
						}else{
							return "error";
						}
				}
			}else if(a==0){  //当a=0时 说明是 平台管理员 登录  查看时   不校验
				//VExam=physicalExamService.getOneTPhysicalExamBy54(physicalExamId);
				VExam=physicalExamService.searchVExamViewByphysicalexamId(physicalExamId);
				
				getList();
						if(isyear == true){//李卓远
							return "showYearChecked";//李卓远
						}//李卓远
				return"showPhysicalExam";
			}else{
				return"error";
			}
			}//李卓远  2017-1-16所加else结束括号
		}
		//通过体检ID 得到 4史表 List
		public void getList(){
			listTHospitalHistory=physicalExamService.searchtHospitalHistoryByPhysicalExamId(physicalExamId);  //住院治疗室史
			listHF=physicalExamService.searchTHFListByPhysicalExamId(physicalExamId);   //家庭病床史
			listTMedicalUse=physicalExamService.searchTMedicalUseListByPhysicalExamId(physicalExamId); //住院用药情况
			listTDefendInocu=physicalExamService.searchTDefendInocusListByPhysicalExamId(physicalExamId);  //非免疫规划预防接种史
		}
		public String jumpEditPhysicalExam(){
			 token = TokenProccessor.getInstance().makeToken();//创建令牌
				HttpServletRequest request=ServletActionContext.getRequest();
				request.getSession().setAttribute("token", token);  //在服务器使用session保存token(令牌)
				
				
				HttpSession session = ServletActionContext.getRequest().getSession();
				TCommunityHospitalGroup group = (TCommunityHospitalGroup) session
						.getAttribute("communityHospitalGroup");
				if(group != null){
					VExam=physicalExamService.getVExamByGidAndtphysicalexamid(group.getGId(), physicalExamId);
					if(VExam!=null && VExam.getHealthFileType().equals(String.valueOf(HealthFileCheck.zhengchang.getValue()))){
						getList();
						return"jumpEditPhysicalExam";
						}else{
							return"error";
						  }
				}
				
			int a=GetSessionPerson.getPerson();
			
			//小于0 说明当前是 社区负责人   
			if(a<0){
			hosId=Math.abs(a);  			// 取社区ID 绝对值 
			/*VExam=physicalExamService.searchVExamViewByphysicalExamIdHosId(physicalExamId, hosId);*/
			VExam=physicalExamService.getOneTPhysicalExamBy54(physicalExamId);
			if(VExam!=null){
				/*getList();*/
				return"jumpEditPhysicalExam";
				}else{
					return"error";
				  }
				}else if(a>0){  				//说明是员工   
					if(healthFileService.getOldStaffHosByStaffId(a)){ //说明不仅是 员工还是责任医生
						VExam=physicalExamService.getOneTPhysicalExamBy54(physicalExamId);
						if(VExam!=null){
						/*getList();*/
						return"jumpEditPhysicalExam";
						}else{
							return"error";
						}
					}else{		      		 	//说明是一般员工  不是责任医生 
						hosId=staffService.getCommHospitalByStaffId(a);
/*						VExam=physicalExamService.searchVExamViewByphysicalExamIdHosId(physicalExamId, hosId);*/
						VExam=physicalExamService.getOneTPhysicalExamBy54(physicalExamId);
							if(VExam!=null){
							/*getList();*/
							return"jumpEditPhysicalExam";
							}else{
							return"error";
						}
					}
				}else if(a==0){  
			//当a=0时 说明是 平台管理员 登录  不能编辑
			//VExam=physicalExamService.searchVExamViewByphysicalexamId(physicalExamId);
			//	getList();
			//		return"jumpEditPhysicalExam";
			//	}else{
					return"error";
				}
			return"error";
			}
		
		
		 
		public String editPhysicalExam(){
			
			HttpServletRequest request=ServletActionContext.getRequest();
			boolean b = Resubmit.isRepeatSubmit(request,token);//判断用户是否是重复提交
			if(b==true){
				return "DocDRedir";  //重定向回首页
			}
			request.getSession().removeAttribute("token");//移除session中的token
			
			if(thealthFile.getHealthFileId() ==null || thealthFile.getHealthFileId()==0){
				 return "error";
			}
			if(tphysicalexam.getCharacteristicSymptom().trim().equals( "") || tphysicalexam.getPhysicalExamType().trim().equals("") || tphysicalexam.getPhysicalExamDate().equals("") || tphysicalexam.getResponsibleDoctor().trim().equals("") || tphysicalexam.getOperator().trim().equals("")){
				 return "error";
			}
			//这里下面的一大段全局变量  都看做54服务器得到！！
			
			//首先 对总表 进行反射 取值
			TPhysicalExam tp=new TPhysicalExam();
			tp=Reflex.getNewT(tp, tphysicalexam);
			
			/*	反射无法 对  外键赋值 ！！！！*/
			TOrganFunction to=new TOrganFunction();
			to=Reflex.getNewT(to, torganfunction);
			//set 总表 外键
			tphysicalexam.setTOrganFunction(to);
			
			TCheck tc=new TCheck();
			tc=Reflex.getNewT(tc, tcheck);
			tphysicalexam.setTCheck(tc);
			
			TChinaMedicine tcm=new TChinaMedicine();
			tcm=Reflex.getNewT(tcm, tchinamedicine);
			tphysicalexam.setTChinaMedicine(tcm);
			
			TLifeStyle tl=new TLifeStyle();
			tl=Reflex.getNewT(tl, tlifestyle);
			tphysicalexam.setTLifeStyle(tl);
			 
			THealthExtingProblem th=new THealthExtingProblem();
			th=Reflex.getNewT(th, thealthexting);
			tphysicalexam.setTHealthExtingProblem(th);
			
			TGeneralCondition tg=new TGeneralCondition();
			tg=Reflex.getNewT(tg,tgeneralcondition);
			tphysicalexam.setTGeneralCondition(tg);
			
			TAuxiliaryExamination ta=new TAuxiliaryExamination();
			ta=Reflex.getNewT(ta,tauxiliaryexamination);
			tphysicalexam.setTAuxiliaryExamination(ta);
			
			//set 健康档案实体
			tphysicalexam.setTHealthFile(thealthFile);
			
			int a=GetSessionPerson.getPerson();	
					
			if(a<0){
					try{
						VExam ve=physicalExamService.searchVExamViewByphysicalexamId(Integer.valueOf(tphysicalexam.getOne()));
						tp.setBelongSystem(String.valueOf(Belong_System.shequ.getValue()));
		    			tp.setPhysicalExamId(Integer.valueOf(tphysicalexam.getOne()));
		    			tp.setTAuxiliaryExamination(new TAuxiliaryExamination(ve.getAuxiliaryExaminationId()));
		    			tp.setTCheck(new TCheck(ve.getCheckId()));
		    			tp.setTChinaMedicine(new TChinaMedicine(ve.getChinaMedicineId()));
		    			tp.setTOrganFunction(new TOrganFunction(ve.getOrganFunctionId()));
		    			tp.setTGeneralCondition(new TGeneralCondition(ve.getGeneralConditionId()));
		    			tp.setTHealthExtingProblem(new THealthExtingProblem(ve.getHealthExistingProblemsId()));
		    			tp.setTHealthFile(new THealthFile(ve.getHealthFileId()));
		    			tp.setTLifeStyle(new TLifeStyle(ve.getLifeId()));
		    			
		    			ta.setAuxiliaryExaminationId(ve.getAuxiliaryExaminationId());
		    			physicalExamService.editTAuxiliaryExamination(ta);
		    			tg.setGeneralConditionId(ve.getGeneralConditionId());
		    			physicalExamService.editTGeneralCondition(tg);
		    			th.setHealthExistingProblemsId(ve.getHealthExistingProblemsId());
		    			physicalExamService.editTHealthExtingProblem(th);
		    			tl.setLifeId(ve.getLifeId());
		    			physicalExamService.editTLifeStyle(tl);
		    			tcm.setChinaMedicineId(ve.getChinaMedicineId());
		    			physicalExamService.editTChinaMedicine(tcm);
		    			tc.setCheckId(ve.getCheckId());
		    			physicalExamService.editTCheck(tc);
		    			to.setOrganFunctionId(ve.getOrganFunctionId());
		    			physicalExamService.editTOrganFunction(to);
		    			
		    			physicalExamService.editTPhysicalExam(tp);//本地 编辑！！！！！
		    			
		    		
		    			
		    			boolean re=physicalExamService.editTPhysicalExamBy54(tphysicalexam, tauxiliaryexamination, tcheck, 
		    					tchinamedicine, tgeneralcondition, thealthexting, 
		                        tlifestyle, torganfunction);
		                 if(re==false){ //数据库保存失败作此操作
		                	 return"error";
		                 }else{
		                	 return"editPhysicalExam"; 
		                 }
					}catch (Exception e) {
					}
			}
			if(a>0){ //这里是员工！  有责任医生和  非责任医生之分   非责任医生享受   社区负责人权限
					try{
						VExam ve=physicalExamService.searchVExamViewByphysicalexamId(Integer.valueOf(tphysicalexam.getOne()));
						tp.setBelongSystem(String.valueOf(Belong_System.shequ.getValue()));
		    			tp.setPhysicalExamId(Integer.valueOf(tphysicalexam.getOne()));
		    			tp.setTAuxiliaryExamination(new TAuxiliaryExamination(ve.getAuxiliaryExaminationId()));
		    			tp.setTCheck(new TCheck(ve.getCheckId()));
		    			tp.setTChinaMedicine(new TChinaMedicine(ve.getChinaMedicineId()));
		    			tp.setTOrganFunction(new TOrganFunction(ve.getOrganFunctionId()));
		    			tp.setTGeneralCondition(new TGeneralCondition(ve.getGeneralConditionId()));
		    			tp.setTHealthExtingProblem(new THealthExtingProblem(ve.getHealthExistingProblemsId()));
		    			tp.setTHealthFile(new THealthFile(ve.getHealthFileId()));
		    			tp.setTLifeStyle(new TLifeStyle(ve.getLifeId()));
		    			
		    			ta.setAuxiliaryExaminationId(ve.getAuxiliaryExaminationId());
		    			physicalExamService.editTAuxiliaryExamination(ta);
		    			tg.setGeneralConditionId(ve.getGeneralConditionId());
		    			physicalExamService.editTGeneralCondition(tg);
		    			th.setHealthExistingProblemsId(ve.getHealthExistingProblemsId());
		    			physicalExamService.editTHealthExtingProblem(th);
		    			tl.setLifeId(ve.getLifeId());
		    			physicalExamService.editTLifeStyle(tl);
		    			tcm.setChinaMedicineId(ve.getChinaMedicineId());
		    			physicalExamService.editTChinaMedicine(tcm);
		    			tc.setCheckId(ve.getCheckId());
		    			physicalExamService.editTCheck(tc);
		    			to.setOrganFunctionId(ve.getOrganFunctionId());
		    			physicalExamService.editTOrganFunction(to);
		    			
		    			physicalExamService.editTPhysicalExam(tp);//本地 编辑！！！！！
		    			boolean re=physicalExamService.editTPhysicalExamBy54(tphysicalexam, tauxiliaryexamination, tcheck, 
		    					tchinamedicine, tgeneralcondition, thealthexting, 
		                        tlifestyle, torganfunction);
		                 if(re==false){ //数据库保存失败作此操作
		                	 return"error";
		                 }else{
		                	 return"editPhysicalExam"; 
		                 }
					}catch (Exception e) {
					}
			}
			physicalExamService.editTAuxiliaryExamination(ta);
			physicalExamService.editTGeneralCondition(tg);
			physicalExamService.editTHealthExtingProblem(th);
			physicalExamService.editTLifeStyle(tl);
			physicalExamService.editTChinaMedicine(tcm);
			physicalExamService.editTCheck(tc);
			physicalExamService.editTOrganFunction(to);
			physicalExamService.editTPhysicalExam(tphysicalexam);//本地 编辑！！！！！
			//删除 List中 空元素
			listTHospitalHistory.removeAll(Collections.singleton(null));
			//定义操作计数器
			int i=0;
			//循环添加外键
			for(THospitalHistory s:listTHospitalHistory){
				//判断 病案号 是否为空，若为空  则是 一条空字段。不对其操作
				i=0;
				if(s.getMedicalRecordNum().getBytes().length!=0){
				s.setTPhysicalExam(tphysicalexam);
				  i++;
				}
			}
						//起码操作一次 之后 才给 save
						if(i!=0){
						physicalExamService.editTHospitalHistory(listTHospitalHistory);
						}
			if(listHF!=null){
			listHF.removeAll(Collections.singleton(null));
				for(THF f:listHF){
					i=0;
					if(f.getMedicalRecordNum().getBytes().length!=0){
					f.setTPhysicalExam(tphysicalexam);
						i++;
					}		
				}  
			}
			if(i!=0){
							physicalExamService.editTHF(listHF);
							}
			if(listTMedicalUse!=null){		
			listTMedicalUse.removeAll(Collections.singleton(null));
			
				for(TMedicalUse m:listTMedicalUse){
					i=0;
					if(m.getMedicalName().getBytes().length!=0){
					m.setTPhysicalExam(tphysicalexam);
					i++;
					}
				}
			}			if(i!=0){
							physicalExamService.editTMedicalUse(listTMedicalUse);
						}
			if(listTDefendInocu!=null){
			listTDefendInocu.removeAll(Collections.singleton(null));
				for(TDefendInocu d:listTDefendInocu){
					i=0;
					if(d.getVaccineName().getBytes().length!=0){
					d.setTPhysicalExam(tphysicalexam);
					i++;
					}
				}
			}
						if(i!=0){
							physicalExamService.editTDefendInocu(listTDefendInocu);
						}
		 return"editPhysicalExam";
		}
		public String jumpAddPhysicalExam(){
			
			int a=GetSessionPerson.getPerson();
			if(a<0){ //社区负责人
			hosId=Math.abs(a);  //获取社区ID 绝对值 	
			THealthFile thealthFile2=healthFileService.getHealthFileByFileNum(fileNum); //thealthFile2 局部健康档案变量
				if(thealthFile2==null || !thealthFile2.getHealthFileType().equals(String.valueOf(HealthFileCheck.zhengchang.getValue()))){
					 return"error";
				}
					if(oldStaffHosService.getOldStaffHosByHealthIdAndHosId(thealthFile2.getHealthFileId(), hosId)==true)
					 {
						thealthFile=thealthFile2;
						return "jumpAddPhysicalExam";
						}else {
						 return"error";
					}
				}else if(a>0){
					if(healthFileService.getOldStaffHosByStaffId(a)){//说明不仅是 员工还是责任医生
						THealthFile thealthFile2=healthFileService.getHealthFileByFileNum(fileNum);//thealthFile2 局部健康档案变量 根据 编号  得到  档案实体
						if(thealthFile2==null){
							 return"error";
						}
					VStaffHosSu vo=healthFileService.getVStaffHosSuByHeathFileId(thealthFile2.getHealthFileId(),HealthFileCheck.zhengchang.getValue()); 
						if(vo==null ){
							  return"error";
						}
						  if(vo.getStaffId()==a){ //通过档案得到 档案视图实体  获得责任医生ID。。判断与当前登录用户ID 是否相同
							  thealthFile=thealthFile2;
							  return"jumpAddPhysicalExam";
						  }else{
							  return"error";
						  }
					}else{ //是员工 但是不是责任医生的时候！
						thealthFile=healthFileService.getHealthFileByFileNum(fileNum);
						return"jumpAddPhysicalExam";
					}
				}
				if(a==0){
					thealthFile=healthFileService.getHealthFileByFileNum(fileNum);
					if(healthFileService.getHealthFileByFileNum(fileNum)==null){//根据 编号  得到  档案实体
						 return"error";
					}
					return"jumpAddPhysicalExam";
				}
				return"error";
			}
		/*根据 体检 ID 社区医院ID  假删一条 体检 记录 实体*/
		public String deletePhysicalExam(){
			
			HttpSession session = ServletActionContext.getRequest().getSession();
			TCommunityHospitalGroup group = (TCommunityHospitalGroup) session
					.getAttribute("communityHospitalGroup");
			if(group != null){
				VExam ve=physicalExamService.getVExamByGidAndtphysicalexamid(group.getGId(), physicalExamId);
				if(ve!=null){
					tphysicalexam=physicalExamService.searchTPhysicalExam(physicalExamId);
					if(tphysicalexam.getTHealthFile().getHealthFileType().equals(String.valueOf(HealthFileCheck.zhengchang.getValue()))){ //档案状态为正常才能删除
						tphysicalexam.setStatus(String.valueOf(PhysicalExamStatus.delete.getvalue()));
						boolean result=physicalExamService.deleteTPhysicalExamBy54(physicalExamId);
						if(result==false){
							return"error";
						}
						physicalExamService.editTPhysicalExam(tphysicalexam);
						return"deletePhysicalExam";
						}else{
							return"error";
						}
					}else{
						return"error";
					}
			}
			int a=GetSessionPerson.getPerson();
			if(a<0){ //社区管理员
				hosId=Math.abs(a);  //获取社区ID 绝对值 	
				if(physicalExamService.searchVExamViewByphysicalExamIdHosId(physicalExamId, hosId)==null)
				{
					healthFileId = 0;
				}
				else{
					healthFileId= physicalExamService.searchVExamViewByphysicalExamIdHosId(physicalExamId, hosId).getHealthFileId();
				}
				if(healthFileId!=0){
					tphysicalexam=physicalExamService.searchTPhysicalExam(physicalExamId, healthFileId);
						if(tphysicalexam.getTHealthFile().getHealthFileType().equals(String.valueOf(HealthFileCheck.zhengchang.getValue()))){ //档案状态为正常才能删除
						tphysicalexam.setStatus(String.valueOf(PhysicalExamStatus.delete.getvalue()));
						boolean result=physicalExamService.deleteTPhysicalExamBy54(physicalExamId);
						if(result==false){
							return"error";
						}
						physicalExamService.editTPhysicalExam(tphysicalexam);
						return"deletePhysicalExam";
						}else{
							return"error";
						}
				}else{
					return"error";
				}
			}else if(a>0){
				if(healthFileService.getOldStaffHosByStaffId(a)){//说明不仅是 员工还是责任医生
					VExam=physicalExamService.getVExamByStaffIdPhysicalId(a, physicalExamId);//通过员工ID和体检ID 差一条体检视图 判断有没有乱输地址栏
					if(VExam!=null){
							tphysicalexam=physicalExamService.searchTPhysicalExamByPhysicalExamId(physicalExamId);//通过体检ID 得到 体检总表
							if(tphysicalexam.getTHealthFile().getHealthFileType().equals(String.valueOf(HealthFileCheck.zhengchang.getValue()))){ //档案状态为正常才能删除
							tphysicalexam.setStatus(String.valueOf(PhysicalExamStatus.delete.getvalue()));
						boolean result=physicalExamService.deleteTPhysicalExamBy54(physicalExamId);
							if(result==false){
								return"error";
							}
							physicalExamService.editTPhysicalExam(tphysicalexam);
							return"deletePhysicalExam";
							}else{
								return"error";		
							}
						}else{
							return"error";							
							}
						
					}else{  //说明是一般员工  不是责任医生   享有社区管理员权限
						hosId=staffService.getCommHospitalByStaffId(a);//根据员工ID 获取社区ID
						healthFileId=physicalExamService.searchVExamViewByphysicalExamIdHosId(physicalExamId, hosId).getHealthFileId();//进行校验
						if(healthFileId!=0 || healthFileId!=null){
							tphysicalexam=physicalExamService.searchTPhysicalExam(physicalExamId, healthFileId);
							if(tphysicalexam.getTHealthFile().getHealthFileType().equals(String.valueOf(HealthFileCheck.zhengchang.getValue()))){ //档案状态为正常才能删除
							tphysicalexam.setStatus(String.valueOf(PhysicalExamStatus.delete.getvalue()));
							physicalExamService.deleteTPhysicalExamBy54(physicalExamId);
							
							physicalExamService.editTPhysicalExam(tphysicalexam);
							return"deletePhysicalExam";
							}else{
								return"error";
							}
						}else{
							return"error";
							}
						}
					}
			if(a==0){  //说明是平台负责人 不能删除
				/*tphysicalexam=physicalExamService.searchTPhysicalExamByPhysicalExamId(physicalExamId);
				tphysicalexam.setStatus(String.valueOf(PhysicalExamStatus.delete.getvalue()));
				physicalExamService.editTPhysicalExam(tphysicalexam);
				return"deletePhysicalExam";*/
			return null;
			}
			return"error";
		}
//====================================多条件查询 全部使用健康体检源代码==================================================
		public String searchMore(){
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
				sq = 1;//0是平台管理员登陆，1是集团登陆，2是其他登陆 
				pagePlus.setTotalNum(healthFileService.findChronicPeopleCount(searchbean));
				this.pageHtml = pagePlus.getPageForChronic("entity");
				vhealthStaffList = healthFileService.findChronicPeople(searchbean, pagePlus);
				healthFileList = new ArrayList<THealthFile>();
				if (vhealthStaffList.size() > 0) {
					for (int i = 0; i < vhealthStaffList.size(); i++) {
						healthFileList.add(healthFileService.getHealthFileById(vhealthStaffList.get(i).getHealthFileId()));
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
					sq = 0; //0是平台管理员登陆，1是集团登陆，2是其他登陆 
					pagePlus.setTotalNum(healthFileService.findChronicPeopleCount(searchbean));
					this.pageHtml = pagePlus.getPageForChronic("entity");
					vhealthStaffList = healthFileService.findChronicPeople(searchbean, pagePlus);
					healthFileList = new ArrayList<THealthFile>();
					if (vhealthStaffList.size() > 0) {
						for (int i = 0; i < vhealthStaffList.size(); i++) {
							healthFileList.add(healthFileService.getHealthFileById(vhealthStaffList.get(i).getHealthFileId()));
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
					this.pageHtml = pagePlus.getPageForChronic("entity");
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
					staffs = staffService.getStaffByCommunityHospitalId(communityHospital.getCommunityHospitalId());
					//以下是从服务器读取数据
					healthFileCloudList= getHealthFileListInfoForMany(pagePlus, searchbean);
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
					staffs = staffService.getStaffByCommunityHospitalId(hosId);
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
					staffs = staffService.getStaffByCommunityHospitalId(hosId);
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
		 * 查询默认的页面的老人档案列表，【多条件查询健康档案】
		 * 
		 * @return
		 */
		public List<HealthFile> getHealthFileListInfoForMany(Page pagePlus,SearchBeanSU searchbean) {
			
			int userId = 1;
			//责任医生
			TStaff tStaff = (TStaff) ActionContext.getContext().getSession().get("tstaff");
			//社区负责人
			TCommunityHospital tCommunityHospital = (TCommunityHospital) ActionContext.getContext().getSession().get("comHospital");
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
				HealthFileCloud healthFileCloud = (HealthFileCloud) ActionContext.getContext().getSession().get("healthFileCloudHosForMany");
				if(healthFileCloud == null){
					healthFileCloud = getHealthPageForManyForAll(searchbean , hosId);
					if(healthFileCloud.getPage() != null){
						ActionContext.getContext().getSession().put("healthFileCloudHosForMany", healthFileCloud);
					}
				}
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
		
		public HealthFileCloud getHealthPageForManyForSpecial(SearchBeanSU searchbean ,int docId){
			String resultJson = healthFileService.getHealthFileCloudForManySpecial(searchbean,docId, 1, 10);
			return JSON.parseObject(resultJson,HealthFileCloud.class);
		}
		
		public HealthFileCloud getHealthPageForManyForAll(SearchBeanSU searchbean ,int hosId){
			String resultJson = healthFileService.getHealthFileCloudForMany(searchbean,hosId, 1, 10);
			return JSON.parseObject(resultJson,HealthFileCloud.class);
		}
//=============================多条件查询结束=========================================		  
		
		
		
		public String autoAction(){
		     zztdList=maintainableAttributeService.getmEntriesByCode("15001");
			 lnrjkList =maintainableAttributeService.getmEntriesByCode("15002");
			 lnrzlList =maintainableAttributeService.getmEntriesByCode("15003");
			 lnrrzList =maintainableAttributeService.getmEntriesByCode("15004");
			 lnrqgList =maintainableAttributeService.getmEntriesByCode("15005");
			 kqkcList =maintainableAttributeService.getmEntriesByCode("15006");
			 clList =maintainableAttributeService.getmEntriesByCode("15007");
			 ybList =maintainableAttributeService.getmEntriesByCode("15008");
			 tlList =maintainableAttributeService.getmEntriesByCode("15009");
			 ydgnList =maintainableAttributeService.getmEntriesByCode("15010");
			 pfList =maintainableAttributeService.getmEntriesByCode("15011");
			 gmList =maintainableAttributeService.getmEntriesByCode("15012");
			 lbjList =maintainableAttributeService.getmEntriesByCode("15013");
			 lyList =maintainableAttributeService.getmEntriesByCode("15014");
			 xzszList =maintainableAttributeService.getmEntriesByCode("15015");
			 zbdmbdList =maintainableAttributeService.getmEntriesByCode("15016");
			 gmzzList =maintainableAttributeService.getmEntriesByCode("15017");
			 lxList =maintainableAttributeService.getmEntriesByCode("15018");
			 nxgjbList =maintainableAttributeService.getmEntriesByCode("15019");
			 szjbList =maintainableAttributeService.getmEntriesByCode("15020");
			 xzjbList =maintainableAttributeService.getmEntriesByCode("15021");
			 xgjbList =maintainableAttributeService.getmEntriesByCode("15022");
			 ybjbList =maintainableAttributeService.getmEntriesByCode("15023");
			 jkzdList =maintainableAttributeService.getmEntriesByCode("15024");
			 wxysList =maintainableAttributeService.getmEntriesByCode("15025");
			 phzList =maintainableAttributeService.getmEntriesByCode("15026");
			 qxzList =maintainableAttributeService.getmEntriesByCode("15027");
			 yangxzList =maintainableAttributeService.getmEntriesByCode("15028");
			 yinxzList =maintainableAttributeService.getmEntriesByCode("15029");
			 tszList =maintainableAttributeService.getmEntriesByCode("15030");
			 srzList =maintainableAttributeService.getmEntriesByCode("15031");
			 xyzList =maintainableAttributeService.getmEntriesByCode("15032");
			 qyzList =maintainableAttributeService.getmEntriesByCode("15033");
			 tbzList =maintainableAttributeService.getmEntriesByCode("15034");
			 
			 tydlplList=maintainableAttributeService.getmEntriesByCode("02010");
			 dlfsList=maintainableAttributeService.getmEntriesByCode("02011");
			 ysxgList=maintainableAttributeService.getmEntriesByCode("02008");
			 xyzkList=maintainableAttributeService.getmEntriesByCode("02013");
			 yjplList=maintainableAttributeService.getmEntriesByCode("02012");
			 yjzlList=maintainableAttributeService.getmEntriesByCode("02014");
			
			 addToken = TokenProccessor.getInstance().makeToken();//创建令牌
				HttpServletRequest request=ServletActionContext.getRequest();
				request.getSession().setAttribute("addToken", addToken);  //在服务器使用session保存token(令牌)
			 
			return"autoAction";
		}
	 
		
//================================================		
		
		
		
		
		
		
		
	
	
	
//==========================get&set============================================================	
	
	
	public PhysicalExamService getPhysicalExamService() {
		return physicalExamService;
	}
	public int getResponsibility() {
		return responsibility;
	}




	public void setResponsibility(int responsibility) {
		this.responsibility = responsibility;
	}




	public TCommunityHospital getAdmin() {
		return admin;
	}




	public void setAdmin(TCommunityHospital admin) {
		this.admin = admin;
	}




	public List<TCommunityHospital> getHospitalsList() {
		return hospitalsList;
	}




	public void setHospitalsList(List<TCommunityHospital> hospitalsList) {
		this.hospitalsList = hospitalsList;
	}




	public List<VExam> getvExamsList() {
		return vExamsList;
	}




	public void setvExamsList(List<VExam> vExamsList) {
		this.vExamsList = vExamsList;
	}




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




	public boolean isIssq() {
		return issq;
	}




	public void setIssq(boolean issq) {
		this.issq = issq;
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




	public SearchClass getSearchClass() {
		return searchClass;
	}
	public void setSearchClass(SearchClass searchClass) {
		this.searchClass = searchClass;
	}
	public String getConditionsNum() {
		return conditionsNum;
	}
	public void setConditionsNum(String conditionsNum) {
		this.conditionsNum = conditionsNum;
	}
	public int getConditions() {
		return conditions;
	}
	public void setConditions(int conditions) {
		this.conditions = conditions;
	}
	public StaffService getStaffService() {
		return staffService;
	}
	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}
	public OldStaffHosService getOldStaffHosService() {
		return oldStaffHosService;
	}
	public void setOldStaffHosService(OldStaffHosService oldStaffHosService) {
		this.oldStaffHosService = oldStaffHosService;
	}
	public MaintainableAttributeService getMaintainableAttributeService() {
		return maintainableAttributeService;
	}
	public void setMaintainableAttributeService(
			MaintainableAttributeService maintainableAttributeService) {
		this.maintainableAttributeService = maintainableAttributeService;
	}
	public List<TMaintainableEntry> getLnrzlList() {
		return lnrzlList;
	}
	public void setLnrzlList(List<TMaintainableEntry> lnrzlList) {
		this.lnrzlList = lnrzlList;
	}
	public List<TMaintainableEntry> getLnrrzList() {
		return lnrrzList;
	}
	public void setLnrrzList(List<TMaintainableEntry> lnrrzList) {
		this.lnrrzList = lnrrzList;
	}
	public List<TMaintainableEntry> getLnrqgList() {
		return lnrqgList;
	}
	public void setLnrqgList(List<TMaintainableEntry> lnrqgList) {
		this.lnrqgList = lnrqgList;
	}
	public List<TMaintainableEntry> getKqkcList() {
		return kqkcList;
	}
	public void setKqkcList(List<TMaintainableEntry> kqkcList) {
		this.kqkcList = kqkcList;
	}
	public List<TMaintainableEntry> getClList() {
		return clList;
	}
	public void setClList(List<TMaintainableEntry> clList) {
		this.clList = clList;
	}
	public List<TMaintainableEntry> getYbList() {
		return ybList;
	}
	public void setYbList(List<TMaintainableEntry> ybList) {
		this.ybList = ybList;
	}
	public List<TMaintainableEntry> getTlList() {
		return tlList;
	}
	public void setTlList(List<TMaintainableEntry> tlList) {
		this.tlList = tlList;
	}
	public List<TMaintainableEntry> getYdgnList() {
		return ydgnList;
	}
	public void setYdgnList(List<TMaintainableEntry> ydgnList) {
		this.ydgnList = ydgnList;
	}
	public List<TMaintainableEntry> getPfList() {
		return pfList;
	}
	public void setPfList(List<TMaintainableEntry> pfList) {
		this.pfList = pfList;
	}
	public List<TMaintainableEntry> getGmList() {
		return gmList;
	}
	public void setGmList(List<TMaintainableEntry> gmList) {
		this.gmList = gmList;
	}
	public List<TMaintainableEntry> getLbjList() {
		return lbjList;
	}
	public void setLbjList(List<TMaintainableEntry> lbjList) {
		this.lbjList = lbjList;
	}
	public List<TMaintainableEntry> getLyList() {
		return lyList;
	}
	public void setLyList(List<TMaintainableEntry> lyList) {
		this.lyList = lyList;
	}
	public List<TMaintainableEntry> getXzszList() {
		return xzszList;
	}
	public void setXzszList(List<TMaintainableEntry> xzszList) {
		this.xzszList = xzszList;
	}
	public List<TMaintainableEntry> getZbdmbdList() {
		return zbdmbdList;
	}
	public void setZbdmbdList(List<TMaintainableEntry> zbdmbdList) {
		this.zbdmbdList = zbdmbdList;
	}
	public List<TMaintainableEntry> getGmzzList() {
		return gmzzList;
	}
	public void setGmzzList(List<TMaintainableEntry> gmzzList) {
		this.gmzzList = gmzzList;
	}
	public List<TMaintainableEntry> getLxList() {
		return lxList;
	}
	public void setLxList(List<TMaintainableEntry> lxList) {
		this.lxList = lxList;
	}
	public List<TMaintainableEntry> getNxgjbList() {
		return nxgjbList;
	}
	public void setNxgjbList(List<TMaintainableEntry> nxgjbList) {
		this.nxgjbList = nxgjbList;
	}
	public List<TMaintainableEntry> getSzjbList() {
		return szjbList;
	}
	public void setSzjbList(List<TMaintainableEntry> szjbList) {
		this.szjbList = szjbList;
	}
	public List<TMaintainableEntry> getXzjbList() {
		return xzjbList;
	}
	public void setXzjbList(List<TMaintainableEntry> xzjbList) {
		this.xzjbList = xzjbList;
	}
	public List<TMaintainableEntry> getXgjbList() {
		return xgjbList;
	}
	public void setXgjbList(List<TMaintainableEntry> xgjbList) {
		this.xgjbList = xgjbList;
	}
	public List<TMaintainableEntry> getYbjbList() {
		return ybjbList;
	}
	public void setYbjbList(List<TMaintainableEntry> ybjbList) {
		this.ybjbList = ybjbList;
	}
	public List<TMaintainableEntry> getJkzdList() {
		return jkzdList;
	}
	public void setJkzdList(List<TMaintainableEntry> jkzdList) {
		this.jkzdList = jkzdList;
	}
	public List<TMaintainableEntry> getWxysList() {
		return wxysList;
	}
	public void setWxysList(List<TMaintainableEntry> wxysList) {
		this.wxysList = wxysList;
	}
	public List<TMaintainableEntry> getPhzList() {
		return phzList;
	}
	public void setPhzList(List<TMaintainableEntry> phzList) {
		this.phzList = phzList;
	}
	public List<TMaintainableEntry> getQxzList() {
		return qxzList;
	}
	public void setQxzList(List<TMaintainableEntry> qxzList) {
		this.qxzList = qxzList;
	}
	public List<TMaintainableEntry> getYangxzList() {
		return yangxzList;
	}
	public void setYangxzList(List<TMaintainableEntry> yangxzList) {
		this.yangxzList = yangxzList;
	}
	public List<TMaintainableEntry> getYinxzList() {
		return yinxzList;
	}
	public void setYinxzList(List<TMaintainableEntry> yinxzList) {
		this.yinxzList = yinxzList;
	}
	public List<TMaintainableEntry> getTszList() {
		return tszList;
	}
	public void setTszList(List<TMaintainableEntry> tszList) {
		this.tszList = tszList;
	}
	public List<TMaintainableEntry> getSrzList() {
		return srzList;
	}
	public void setSrzList(List<TMaintainableEntry> srzList) {
		this.srzList = srzList;
	}
	public List<TMaintainableEntry> getXyzList() {
		return xyzList;
	}
	public void setXyzList(List<TMaintainableEntry> xyzList) {
		this.xyzList = xyzList;
	}
	public List<TMaintainableEntry> getQyzList() {
		return qyzList;
	}
	public void setQyzList(List<TMaintainableEntry> qyzList) {
		this.qyzList = qyzList;
	}
	public List<TMaintainableEntry> getTbzList() {
		return tbzList;
	}
	public void setTbzList(List<TMaintainableEntry> tbzList) {
		this.tbzList = tbzList;
	}
	public List<TMaintainableEntry> getZztdList() {
		return zztdList;
	}
	public void setZztdList(List<TMaintainableEntry> zztdList) {
		this.zztdList = zztdList;
	}
	 
	public List<TMaintainableEntry> getLnrjkList() {
		return lnrjkList;
	}
	public void setLnrjkList(List<TMaintainableEntry> lnrjkList) {
		this.lnrjkList = lnrjkList;
	}
	public Integer getGeneralconditionId() {
		return generalconditionId;
	}
	public void setGeneralconditionId(Integer generalconditionId) {
		this.generalconditionId = generalconditionId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void setPhysicalExamId(Integer physicalExamId) {
		this.physicalExamId = physicalExamId;
	}
	public String getFileNum() {
		return fileNum;
	}
	public void setFileNum(String fileNum) {
		this.fileNum = fileNum;
	}
	public CommunityHospitalService getCommunityHospitalService() {
		return communityHospitalService;
	}
	public void setCommunityHospitalService(
			CommunityHospitalService communityHospitalService) {
		this.communityHospitalService = communityHospitalService;
	}
	public VExam getVExam() {
		return VExam;
	}
	public void setVExam(VExam vExam) {
		VExam = vExam;
	}
	
	public String getHosName() {
		return hosName;
	}
	public void setHosName(String hosName) {
		this.hosName = hosName;
	}
	
	public int getPhysicalExamId() {
		return physicalExamId;
	}
	public void setPhysicalExamId(int physicalExamId) {
		this.physicalExamId = physicalExamId;
	}
	public int getHosId() {
		return hosId;
	}
	public void setHosId(int hosId) {
		this.hosId = hosId;
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
	public List<VExam> getVexamList() {
		return vexamList;
	}
	public void setVexamList(List<VExam> vexamList) {
		this.vexamList = vexamList;
	}
	
	public int getHealthFileType() {
		return healthFileType;
	}
	public void setHealthFileType(int healthFileType) {
		this.healthFileType = healthFileType;
	}
	public void setHealthFileType(char healthFileType) {
		this.healthFileType = healthFileType;
	}
	public TCommunityHospital getTcommunityHospital() {
		return tcommunityHospital;
	}
	public void setTcommunityHospital(TCommunityHospital tcommunityHospital) {
		this.tcommunityHospital = tcommunityHospital;
	}
	public int gethosId() {
		return hosId;
	}
	public void sethosId(int hosId) {
		this.hosId = hosId;
	}
	public HealthFileService getHealthFileService() {
		return healthFileService;
	}

	public void setHealthFileService(HealthFileService healthFileService) {
		this.healthFileService = healthFileService;
	}

	public THealthFile getThealthFile() {
		return thealthFile;
	}

	public void setThealthFile(THealthFile thealthFile) {
		this.thealthFile = thealthFile;
	}

	public int getSearchMethod() {
		return searchMethod;
	}

	public void setSearchMethod(int searchMethod) {
		this.searchMethod = searchMethod;
	}


	public Integer getHealthFileId() {
		return healthFileId;
	}

	

	public String getSearchNum() {
		return searchNum;
	}
	public void setSearchNum(String searchNum) {
		this.searchNum = searchNum;
	}
	public void setHealthFileId(Integer healthFileId) {
		this.healthFileId = healthFileId;
	}



	public String getAddToken() {
		return addToken;
	}

	public void setAddToken(String addToken) {
		this.addToken = addToken;
	}

	public void setPhysicalExamService(PhysicalExamService physicalExamService) {
		this.physicalExamService = physicalExamService;
	}


	public TPhysicalExam getTphysicalexam() {
		return tphysicalexam;
	}


	public void setTphysicalexam(TPhysicalExam tphysicalexam) {
		this.tphysicalexam = tphysicalexam;
	}


	public TLifeStyle getTlifestyle() {
		return tlifestyle;
	}


	public void setTlifestyle(TLifeStyle tlifestyle) {
		this.tlifestyle = tlifestyle;
	}


	public TOrganFunction getTorganfunction() {
		return torganfunction;
	}


	public void setTorganfunction(TOrganFunction torganfunction) {
		this.torganfunction = torganfunction;
	}


	public TCheck getTcheck() {
		return tcheck;
	}


	public void setTcheck(TCheck tcheck) {
		this.tcheck = tcheck;
	}


	public TChinaMedicine getTchinamedicine() {
		return tchinamedicine;
	}


	public void setTchinamedicine(TChinaMedicine tchinamedicine) {
		this.tchinamedicine = tchinamedicine;
	}


	public THealthExtingProblem getThealthexting() {
		return thealthexting;
	}


	public void setThealthexting(THealthExtingProblem thealthexting) {
		this.thealthexting = thealthexting;
	}


	public TGeneralCondition getTgeneralcondition() {
		return tgeneralcondition;
	}


	public void setTgeneralcondition(TGeneralCondition tgeneralcondition) {
		this.tgeneralcondition = tgeneralcondition;
	}


	public TAuxiliaryExamination getTauxiliaryexamination() {
		return tauxiliaryexamination;
	}


	public void setTauxiliaryexamination(TAuxiliaryExamination tauxiliaryexamination) {
		this.tauxiliaryexamination = tauxiliaryexamination;
	}

	public String getCharacteristicSymptom() {
		return characteristicSymptom;
	}


	public void setCharacteristicSymptom(String characteristicSymptom) {
		this.characteristicSymptom = characteristicSymptom;
	}
	public Integer getOrganfunctionId() {
		return organfunctionId;
	}
	public void setOrganfunctionId(Integer organfunctionId) {
		this.organfunctionId = organfunctionId;
	}
	public Integer getCheckId() {
		return checkId;
	}
	public void setCheckId(Integer checkId) {
		this.checkId = checkId;
	}
	public Integer getChinaMedicineId() {
		return chinaMedicineId;
	}
	public void setChinaMedicineId(Integer chinaMedicineId) {
		this.chinaMedicineId = chinaMedicineId;
	}
	public Integer getHealthExistingProblemsId() {
		return healthExistingProblemsId;
	}
	public void setHealthExistingProblemsId(Integer healthExistingProblemsId) {
		this.healthExistingProblemsId = healthExistingProblemsId;
	}
	public Integer getLifeId() {
		return lifeId;
	}
	public void setLifeId(Integer lifeId) {
		this.lifeId = lifeId;
	}
	public int getAuxiliaryExaminationId() {
		return auxiliaryExaminationId;
	}
	public void setAuxiliaryExaminationId(int auxiliaryExaminationId) {
		this.auxiliaryExaminationId = auxiliaryExaminationId;
	}
	public List<TMedicalUse> getListTMedicalUse() {
		return listTMedicalUse;
	}
	public void setListTMedicalUse(List<TMedicalUse> listTMedicalUse) {
		this.listTMedicalUse = listTMedicalUse;
	}
	public List<THospitalHistory> getListTHospitalHistory() {
		return listTHospitalHistory;
	}
	public void setListTHospitalHistory(List<THospitalHistory> listTHospitalHistory) {
		this.listTHospitalHistory = listTHospitalHistory;
	}
	public List<THF> getListHF() {
		return listHF;
	}
	public void setListHF(List<THF> listHF) {
		this.listHF = listHF;
	}
	public List<TDefendInocu> getListTDefendInocu() {
		return listTDefendInocu;
	}
	public void setListTDefendInocu(List<TDefendInocu> listTDefendInocu) {
		this.listTDefendInocu = listTDefendInocu;
	}
	 
	public List<TStaff> getDoctorList() {
		return doctorList;
	}
	public void setDoctorList(List<TStaff> doctorList) {
		this.doctorList = doctorList;
	}
	public boolean isIssetsearch() {
		return issetsearch;
	}
	public void setIssetsearch(boolean issetsearch) {
		this.issetsearch = issetsearch;
	}

	public boolean isIsyear() {
		return isyear;
	}
	public void setIsyear(boolean isyear) {
		this.isyear = isyear;
	}

	public int[] getIds() {
		return ids;
	}

	public void setIds(int[] ids) {
		this.ids = ids;
	}

	public int getConFlag() {
		return conFlag;
	}

	public void setConFlag(int conFlag) {
		this.conFlag = conFlag;
	}

	public int getZhuangtai() {
		return zhuangtai;
	}

	public void setZhuangtai(int zhuangtai) {
		this.zhuangtai = zhuangtai;
	}

	public List<TStaff> getSpecialDoctor() {
		return specialDoctor;
	}
	public void setSpecialDoctor(List<TStaff> specialDoctor) {
		this.specialDoctor = specialDoctor;
	}
	public String getToken() {
		return token;
	}
	public boolean isChaojiGuanLiYuanFlag() {
		return chaojiGuanLiYuanFlag;
	}


	public void setChaojiGuanLiYuanFlag(boolean chaojiGuanLiYuanFlag) {
		this.chaojiGuanLiYuanFlag = chaojiGuanLiYuanFlag;
	}


	public void setToken(String token) {
		this.token = token;
	}




	public CommunityHospitalGroupService getCommunityHospitalGroupService() {
		return communityHospitalGroupService;
	}


	public void setCommunityHospitalGroupService(
			CommunityHospitalGroupService communityHospitalGroupService) {
		this.communityHospitalGroupService = communityHospitalGroupService;
	}


	public VHealthStaff getVhealthstaff() {
		return vhealthstaff;
	}


	public void setVhealthstaff(VHealthStaff vhealthstaff) {
		this.vhealthstaff = vhealthstaff;
	}


	public int getSq() {
		return sq;
	}


	public void setSq(int sq) {
		this.sq = sq;
	}


	public List<TCommunityHospital> getHospitals() {
		return hospitals;
	}


	public void setHospitals(List<TCommunityHospital> hospitals) {
		this.hospitals = hospitals;
	}


	public List<VHealthStaff> getVhealthStaffList() {
		return vhealthStaffList;
	}


	public void setVhealthStaffList(List<VHealthStaff> vhealthStaffList) {
		this.vhealthStaffList = vhealthStaffList;
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}


	public List<THealthFile> getHealthFileList() {
		return healthFileList;
	}


	public void setHealthFileList(List<THealthFile> healthFileList) {
		this.healthFileList = healthFileList;
	}


	public boolean isFlagPingTai() {
		return flagPingTai;
	}


	public void setFlagPingTai(boolean flagPingTai) {
		this.flagPingTai = flagPingTai;
	}


	public TCommunityHospital getCommunityHospital() {
		return communityHospital;
	}


	public void setCommunityHospital(TCommunityHospital communityHospital) {
		this.communityHospital = communityHospital;
	}


	public List<TCommunityHospitalGroup> getGroups() {
		return groups;
	}


	public void setGroups(List<TCommunityHospitalGroup> groups) {
		this.groups = groups;
	}


	public List<VStaffHos> getAllDoctor() {
		return allDoctor;
	}


	public void setAllDoctor(List<VStaffHos> allDoctor) {
		this.allDoctor = allDoctor;
	}


	public List<VStaffHos> getAllEnterPeople() {
		return allEnterPeople;
	}


	public void setAllEnterPeople(List<VStaffHos> allEnterPeople) {
		this.allEnterPeople = allEnterPeople;
	}


	public List<TStaff> getStaffs() {
		return staffs;
	}


	public void setStaffs(List<TStaff> staffs) {
		this.staffs = staffs;
	}


	public TStaff getTstaff() {
		return tstaff;
	}


	public void setTstaff(TStaff tstaff) {
		this.tstaff = tstaff;
	}


	public boolean isFlag() {
		return flag;
	}

	
	public String getJiGouFileNum() {
		return jiGouFileNum;
	}


	public void setJiGouFileNum(String jiGouFileNum) {
		this.jiGouFileNum = jiGouFileNum;
	}


	public String getJiGouname() {
		return jiGouname;
	}


	public void setJiGouname(String jiGouname) {
		this.jiGouname = jiGouname;
	}


	public boolean isJiGousex() {
		return jiGousex;
	}


	public void setJiGousex(boolean jiGousex) {
		this.jiGousex = jiGousex;
	}


	public void setFlag(boolean flag) {
		this.flag = flag;
	}


	public int getCp1() {
		return cp1;
	}


	
	public int getYiyuanflag() {
		return yiyuanflag;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public void setYiyuanflag(int yiyuanflag) {
		this.yiyuanflag = yiyuanflag;
	}


	public void setCp1(int cp1) {
		this.cp1 = cp1;
	}


	public boolean isShowwindow() {
		return showwindow;
	}


	public Page<VHealthStaff> getPagePlus() {
		return pagePlus;
	}


	public void setPagePlus(Page<VHealthStaff> pagePlus) {
		this.pagePlus = pagePlus;
	}


	public SearchBeanSU getSearchbean() {
		return searchbean;
	}


	public void setSearchbean(SearchBeanSU searchbean) {
		this.searchbean = searchbean;
	}


	public THealthFile getHealthFile() {
		return healthFile;
	}


	public void setHealthFile(THealthFile healthFile) {
		this.healthFile = healthFile;
	}


	public void setShowwindow(boolean showwindow) {
		this.showwindow = showwindow;
	}


	public List<VHealthStaff> getThealthFileList() {
		return thealthFileList;
	}


	public void setThealthFileList(List<VHealthStaff> thealthFileList) {
		this.thealthFileList = thealthFileList;
	}


	public int getBelongSystem() {
		return belongSystem;
	}


	public void setBelongSystem(int belongSystem) {
		this.belongSystem = belongSystem;
	}


	public PhysicalExamBypersonal getPhysicalExamBypersonal() {
		return physicalExamBypersonal;
	}


	public void setPhysicalExamBypersonal(PhysicalExamBypersonal physicalExamBypersonal) {
		this.physicalExamBypersonal = physicalExamBypersonal;
	}


	public int getIndexFlag() {
		return indexFlag;
	}


	public void setIndexFlag(int indexFlag) {
		this.indexFlag = indexFlag;
	}


	public List<HealthFile> getHealthFileCloudList() {
		return healthFileCloudList;
	}


	public void setHealthFileCloudList(List<HealthFile> healthFileCloudList) {
		this.healthFileCloudList = healthFileCloudList;
	}


	public boolean isEmbeded() {
		return embeded;
	}


	public void setEmbeded(boolean embeded) {
		this.embeded = embeded;
	}


	 
	

}
