package egovframework.covision.coviflow.migration;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.XML;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DataMigrationMain {

	private SelectOldData oldDataSvc;
	private InsertNewData newDataSvc;
	
	// 테이블 ID
	private String newFormID = "265";
	private String newSchemaID = "1";
	private String newFormInstanceID = "";
	
	private String newProcessDescID = "";
	private String oldProcessID = "";
	private String newProcessID = "";
	
	private String newCirculationDescID = "";
	
	private JSONArray FormInstArr = new JSONArray();
	
	public void executeDataMigration() throws Exception {
		
		oldDataSvc = new SelectOldData();
		newDataSvc = new InsertNewData();
		
		int allCount = oldDataSvc.selectOldFormInstanceDataCount();
		SimpleDateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		
		System.out.println("Total Number : " + allCount);
		
		for(int i=1; i<=(allCount/1000)*1000+1; i+=1000){
			int startID = 0, endID = 0;
			
			startID = i;
			if(i == ((allCount/1000)*1000+1))
				endID = allCount;
			else
				endID = i+999;
			
			FormInstArr = oldDataSvc.selectOldFormInstanceData(startID, endID);
			
			for(Object obj : FormInstArr){
				try{
					// Form Instance (하위테이블, com_file 포함)
					// WF_FORM_INSTANCE -> JWF_FormInstance
					JSONObject formInstData = new JSONObject();
					formInstData = (JSONObject) obj;
					String oldFormInstanceID = formInstData.getString("FormInstID");
					
					migrationFormInstance(formInstData, oldFormInstanceID);							// 완료된 양식만. FormInstance 개수만큼 반복
					
					// Process (Process Description, Workitem, DomainData 포함)
					// WF_E_PROCESS -> JWF_ProcessArchive
					migrationProcessArchive(oldFormInstanceID);
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			System.out.println(dateFormat.format(new Date()) + " : " + endID + " completed.");
		}
		
		newDataSvc.updateParentProcessID();
		newDataSvc.updateDocLink();
	}
	
	// FormInstance 데이터 마이그레이션
	private void migrationFormInstance(JSONObject formInstData, String oldFormInstanceID) throws Exception{
		try {
			// 가공이 필요한 데이터
			formInstData.put("ProcessID", "0");
			formInstData.put("SchemaID", newSchemaID);
			formInstData.put("FormID", newFormID);
			formInstData.put("DocClassID", formInstData.has("DocClassID") ? formInstData.getString("DocClassID") : "");			// 매칭되는 DocClass ID 세팅
			formInstData.put("BodyType", "LINK");
			formInstData.put("BodyContext", "");
			formInstData.put("ReceiveNames", formInstData.has("ReceiveNames") ? formInstData.getString("ReceiveNames").equals("@@") ? ";;" : formInstData.getString("ReceiveNames") : "");
			formInstData.put("RuleItemInfo", "");
			
			// FormInstance Insert
			newFormInstanceID = newDataSvc.insertFormInstanceData(formInstData);
			
			if(formInstData.has("AttachFileInfo")) {
				migrationComFile(formInstData.getString("AttachFileInfo"), formInstData.getString("InitiatorID"));
			}
			
			if(formInstData.has("DocLinks")) {
				migrationDocLink(formInstData.getString("DocLinks"));
			}
		} catch (Exception e) {
			throw new Exception("FormInstID : " + oldFormInstanceID + "\r\n"+ e.getMessage());
		}
	}
	
	// 첨부파일 데이터 마이그레이션
	private void migrationComFile(String strAttachFileInfo, String strInitiatorID) throws Exception{
		if(!strAttachFileInfo.equals("")){
			JSONObject AttachFileInfoObj = new JSONObject();
			org.json.JSONObject AttachFileInfoObjTemp = new org.json.JSONObject();
				
			AttachFileInfoObjTemp = XML.toJSONObject(strAttachFileInfo);
			AttachFileInfoObj = JSONObject.fromObject(AttachFileInfoObjTemp.toString());
				
			JSONArray AttachFileInfoArr = new JSONArray();
			if(AttachFileInfoObj.get("fileinfos") instanceof JSONObject && AttachFileInfoObj.getJSONObject("fileinfos").has("fileinfo") && AttachFileInfoObj.getJSONObject("fileinfos").getJSONObject("fileinfo").has("file")){
				if(AttachFileInfoObj.getJSONObject("fileinfos").getJSONObject("fileinfo").get("file") instanceof JSONObject)
					AttachFileInfoArr.add(AttachFileInfoObj.getJSONObject("fileinfos").getJSONObject("fileinfo").getJSONObject("file"));
				else
					AttachFileInfoArr = AttachFileInfoObj.getJSONObject("fileinfos").getJSONObject("fileinfo").getJSONArray("file");
			}
			
			String length = String.valueOf(AttachFileInfoArr.size()-1);

			int cnt = 0;
			for(Object obj : AttachFileInfoArr){
				JSONObject fileInfoObj = (JSONObject) obj;
				JSONObject param = new JSONObject();
				
				String location = fileInfoObj.getString("location").replaceAll("/GWStorage/e-sign/Approval/Attach/", "");
				String filePath = location.split("/")[0]+"/";
				String fileName = fileInfoObj.getString("name").replace("'", "\\'");
				String savedName = location.split("/")[1].replace("'", "\\'");
				String extention = savedName.split("\\.")[savedName.split("\\.").length-1];
				String size = fileInfoObj.getString("size");
				
				param.put("StorageID", "0");
				param.put("ServiceType", "APPROVALMIG");
				param.put("ObjectID", "0");
				param.put("ObjectType", "DEPT");
				param.put("MessageID", newFormInstanceID);
				param.put("Version", "1");
				param.put("SaveType", "File");
				param.put("LastSeq", length);
				param.put("Seq", cnt);
				param.put("FilePath", filePath);
				param.put("FileName", fileName);
				param.put("SavedName", savedName);
				param.put("Extention", extention);
				param.put("Size", size);
				param.put("ThumWidth", "0");
				param.put("ThumHeight", "0");
				param.put("Description", null);
				param.put("RegisterCode", strInitiatorID);
				
				newDataSvc.insertComFileData(param);
				++cnt;
			}
		}
	}
	
	// 연결문서 데이터 마이그레이션
	private void migrationDocLink(String strDocLinks) throws Exception{
		if(!strDocLinks.equals("")){
			
			String[] DocLinkArr = strDocLinks.replace("^^^", "&&&").split("&&&");
			for(int i = 0; i < DocLinkArr.length; i++){
				JSONObject param = new JSONObject();
				String[] DocLinkInfo = DocLinkArr[i].split("@@@");
				
				if(DocLinkInfo.length > 2) {
					param.put("UNIQUEID", newFormInstanceID);
					param.put("SEQ", i);
					param.put("REF_DocID", DocLinkInfo[0].toUpperCase());
					param.put("REF_Subject", DocLinkInfo[2]);
					
					newDataSvc.insertDocLink(param);
				}
			}
		}
	}

	// Process Description 데이터 마이그레이션
	private void migrationProcessDesc(JSONObject processData) throws Exception{
		if(processData.has("DSCR")) {
			String dscrData = processData.getString("DSCR");
			
			processData.put("IsFile", getDSCRString(dscrData, "isfile").equals("1") ? "Y" : "N" );
			processData.put("IsComment", getDSCRString(dscrData, "comment").equals("1") ? "Y" : "N" );
			processData.put("IsReserved", getDSCRString(dscrData, "reserved").equals("1") ? "Y" : "N" );
			processData.put("ReservedGubun", getDSCRString(dscrData, "reserved_gubun"));
			processData.put("ReservedTime", getDSCRString(dscrData, "reserved_time"));
		} else {
			processData.put("IsFile", "N");
			processData.put("IsComment", "N");
			processData.put("IsReserved", "N");
			processData.put("ReservedGubun", "");
			processData.put("ReservedTime", "");
		}
		
		processData.put("FormInstID", isHasValue(processData, "FormInstID"));
		processData.put("FormID", newFormID);
		processData.put("FormName", isHasValue(processData, "FormName"));
		processData.put("IsSecureDoc", isHasValue(processData, "IsSecureDoc").equals("1") ? "Y" : "N" );
		processData.put("FileExt", isHasValue(processData, "FileExt"));
		processData.put("DocNo", isHasValue(processData, "DocNo"));
		processData.put("ApproverCode", isHasValue(processData, "ApproverCode"));
		processData.put("ApproverName", isHasValue(processData, "ApproverName"));
		processData.put("ApprovalStep", isHasValue(processData, "ApprovalStep"));
		processData.put("ApproverSIPAddress", isHasValue(processData, "ApproverSIPAddress"));
		processData.put("Priority", isHasValue(processData, "Priority"));
		processData.put("IsModify", isHasValue(processData, "IsModify"));
		processData.put("Reserved1", isHasValue(processData, "Reserved1"));
		processData.put("Reserved2", isHasValue(processData, "Reserved2"));
		processData.put("BusinessData1", isHasValue(processData, "BusinessData1"));
		
		newProcessDescID = newDataSvc.insertProcessDescriptionData(processData);
	}
	
	// Process 데이터 마이그레이션
	private void migrationProcessArchive(String oldFormInstanceID) throws Exception{
		try {
			JSONArray ProcessArr = oldDataSvc.selectOldProcessData(oldFormInstanceID);
			
			 for(Object obj : ProcessArr){
				 JSONObject processData = new JSONObject();
				 processData = (JSONObject) obj;

				 oldProcessID = processData.getString("ProcessArchiveID");
				 
				 processData.put("FormInstID", newFormInstanceID);
				 
				 migrationProcessDesc(processData);

				 processData.put("ProcessArchiveID", newProcessDescID);
				 processData.put("ProcessDescriptionArchiveID", newProcessDescID);
				 processData.put("ParentProcessID", "0");

				 newProcessID = newDataSvc.insertProcessData(processData);
				 
				 migrationWorkitemArchive();
				 
				 // CirculationBox (CirculationBox Description, CirculationRead 포함)
				 // WF_CIRCULATION -> JWF_CirculationBox
				 migrationCirculationBox();
			 }
		} catch (Exception e) {
			throw new Exception("FormInstID : " + oldFormInstanceID + "\r\n"+ e.getMessage());
		}
	}
	
	// Workitem 데이터 마이그레이션
	private void migrationWorkitemArchive() throws Exception{
		JSONArray workitemArr = oldDataSvc.selectOldWorkitemData(oldProcessID);
		
		for(Object obj : workitemArr){
			JSONObject workitemData = new JSONObject();
			workitemData = (JSONObject) obj;
			
			workitemData.put("ProcessArchiveID", newProcessID);
			workitemData.put("IsBatch", workitemData.getString("IsBatch").equals("1") ? "Y" : "N");
			workitemData.put("IsMobile", workitemData.getString("IsMobile").equals("1") ? "Y" : "N");
			
			newDataSvc.insertWorkitemData(workitemData);
		}
	}
	
	// CirculationBox Description 데이터 마이그레이션
	private void migrationCirculationBoxDesc(JSONObject circulationData) throws Exception{
		JSONObject circulationDescData = new JSONObject();
		String dscrData = circulationData.getString("LINK_URL");
		
		circulationDescData.put("FormInstID", newFormInstanceID);
		circulationDescData.put("FormID", newFormID);
		circulationDescData.put("FormPrefix", "WF_MIGRATION");
		circulationDescData.put("FormName", getDSCRString(dscrData, "name"));
		circulationDescData.put("FormSubject", getDSCRString(dscrData, "subject"));
		circulationDescData.put("IsSecureDoc", getDSCRString(dscrData, "secure_doc").equals("1") ? "Y" : "N" );
		circulationDescData.put("IsFile", getDSCRString(dscrData, "isfile").equals("1") ? "Y" : "N" );
		circulationDescData.put("FileExt", getDSCRString(dscrData, "fileext"));
		circulationDescData.put("IsComment", getDSCRString(dscrData, "comment").equals("1") ? "Y" : "N" );
		circulationDescData.put("ApproverCode", getDSCRString(dscrData, "approvercode"));
		circulationDescData.put("ApproverName", getDSCRString(dscrData, "approvername"));
		circulationDescData.put("ApprovalStep", "");
		circulationDescData.put("ApproverSIPAddress", getDSCRString(dscrData, "approversipaddress"));
		circulationDescData.put("IsReserved", getDSCRString(dscrData, "reserved").equals("1") ? "Y" : "N" );
		circulationDescData.put("ReservedGubun", getDSCRString(dscrData, "reserved_gubun"));
		circulationDescData.put("ReservedTime", getDSCRString(dscrData, "reserved_time"));
		circulationDescData.put("Priority", "3");
		circulationDescData.put("IsModify", "N");
		
		newCirculationDescID = newDataSvc.insertCirculationDescriptionData(circulationDescData);
	}
	
	// CirculationBox 데이터 마이그레이션
	private void migrationCirculationBox() throws Exception{
		JSONArray circulationArr = oldDataSvc.selectOldCirculationBoxData(oldProcessID);
		
		for(Object obj : circulationArr){
			JSONObject circulationData = new JSONObject();
			circulationData = (JSONObject) obj;
			
			migrationCirculationBoxDesc(circulationData);
			
			circulationData.put("ProcessID", newProcessID);
			circulationData.put("FormInstID", newFormInstanceID);
			circulationData.put("CirculationBoxID", newCirculationDescID);
			circulationData.put("CirculationBoxDescriptionID", newCirculationDescID);
			
			// CirculationBox Insert
			newDataSvc.insertCirculationData(circulationData);
		}
	}
	
	// DSCR Data를 xml에서 json으로 변환
	public String getDSCRString(String DSCR, String key) throws Exception{
		String returnData = "";
		JSONObject dscrObj = new JSONObject();
		org.json.JSONObject dscrObjTemp = new org.json.JSONObject();
		
		dscrObjTemp = XML.toJSONObject(DSCR);
		dscrObj = JSONObject.fromObject(dscrObjTemp.toString());
		dscrObj = dscrObj.getJSONObject("ClientAppInfo").getJSONObject("App").getJSONObject("forminfos").getJSONObject("forminfo");
		
		if(dscrObj.has(key)){
			returnData = dscrObj.getString(key);
		}else{
			if(key.equals("reserved_time")){
				returnData = null;
			}
		}
		
		return returnData;
	}

	private String isHasValue(JSONObject paramJSON, String key){
		if(paramJSON.has(key)){
			return paramJSON.getString(key);
		}else{
			return "";
		}
	}
	
}
