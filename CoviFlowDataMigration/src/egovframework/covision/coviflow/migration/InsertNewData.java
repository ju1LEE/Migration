package egovframework.covision.coviflow.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class InsertNewData {
	
	// insert FormInstance Data
	public String insertFormInstanceData(JSONObject paramJSON) throws Exception{
		String returnAutoID = "";
		
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = ConnectionFactoryMaria.getDatabaseConnection();
			String query = "INSERT INTO covi_approval4j_store.jwf_forminstance ("
					+ "ProcessID,"
					+ "SchemaID,"
					+ "FormID,"
					+ "Subject,"
					+ "InitiatorID,"
					+ "InitiatorName,"
					+ "InitiatorUnitID,"
					+ "InitiatorUnitName,"
					+ "InitiatedDate,"
					+ "CompletedDate,"
					+ "DeletedDate,"
					+ "LastModifiedDate,"
					+ "LastModifierID,"
					+ "EntCode,"
					+ "EntName,"
					+ "DocNo,"
					+ "DocLevel,"
					+ "DocClassID,"
					+ "DocClassName,"
					+ "DocSummary,"
					+ "IsPublic,"
					+ "SaveTerm,"
					+ "AttachFileInfo,"
					+ "AppliedDate,"
					+ "AppliedTerm,"
					+ "ReceiveNo,"
					+ "ReceiveNames,"
					+ "ReceiptList,"
					+ "BodyType,"
					+ "BodyContext,"
					+ "DocLinks,"
					+ "RuleItemInfo"
					+ ")"
					+ "VALUES ("
					+ isHasValue(paramJSON, "ProcessID") + ","
					+ isHasValue(paramJSON, "SchemaID") + ","
					+ isHasValue(paramJSON, "FormID") + ","
					+ isHasValue(paramJSON, "Subject") + ","
					+ isHasValue(paramJSON, "InitiatorID") + ","
					+ isHasValue(paramJSON, "InitiatorName") + ","
					+ isHasValue(paramJSON, "InitiatorUnitID") + ","
					+ isHasValue(paramJSON, "InitiatorUnitName") + ","
					+ isHasValue(paramJSON, "InitiatedDate") + ","
					+ isHasValue(paramJSON, "CompletedDate") + ","
					+ isHasValue(paramJSON, "DeletedDate") + ","
					+ isHasValue(paramJSON, "LastModifiedDate") + ","
					+ isHasValue(paramJSON, "LastModifierID") + ","
					+ isHasValue(paramJSON, "EntCode") + ","
					+ isHasValue(paramJSON, "EntName") + ","
					+ isHasValue(paramJSON, "DocNo") + ","
					+ isHasValue(paramJSON, "DocLevel") + ","
					+ isHasValue(paramJSON, "DocClassID") + ","
					+ isHasValue(paramJSON, "DocClassName") + ","
					+ isHasValue(paramJSON, "DocSummary") + ","
					+ isHasValue(paramJSON, "IsPublic") + ","
					+ isHasValue(paramJSON, "SaveTerm") + ","
					+ isHasValue(paramJSON, "AttachFileInfo") + ","
					+ isHasValue(paramJSON, "AppliedDate") + ","
					+ isHasValue(paramJSON, "AppliedTerm") + ","
					+ isHasValue(paramJSON, "ReceiveNo") + ","
					+ isHasValue(paramJSON, "ReceiveNames") + ","
					+ isHasValue(paramJSON, "ReceiptList") + ","
					+ isHasValue(paramJSON, "BodyType") + ","
					+ isHasValue(paramJSON, "BodyContext") + ","
					+ isHasValue(paramJSON, "DocLinks") + ","
					+ isHasValue(paramJSON, "RuleItemInfo")
					+ ");";
			
			ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setEscapeProcessing(true);
			ps.setQueryTimeout(100);
			
			//System.out.println("FormInstance Data : " + query);
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			returnAutoID = rs.getString(1);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			ps.close();
			conn.close();
		}

		return returnAutoID;
	}
	
	// insert Process Data
	public String insertProcessData(JSONObject paramJSON) throws Exception{
		String returnAutoID = "";
		
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = ConnectionFactoryMaria.getDatabaseConnection();
			String query = "INSERT INTO covi_approval4j_store.jwf_processarchive ("
					+ "ProcessArchiveID,"
					+ "ProcessKind,"
					+ "ParentProcessID,"
					+ "ProcessDescriptionArchiveID,"
					+ "ProcessName,"
					+ "DocSubject,"
					+ "BusinessState,"
					+ "InitiatorID,"
					+ "InitiatorName,"
					+ "InitiatorUnitID,"
					+ "InitiatorUnitName,"
					+ "FormInstID,"
					+ "ProcessState,"
					+ "InitiatorSIPAddress,"
					+ "StartDate,"
					+ "EndDate,"
					+ "DeleteDate,"
					+ "FormName,"
					+ "FormPrefix,"
					+ "DivisionKind,"
					+ "DocFolder,"
					+ "DocFolderName,"
					+ "OwnerUnitID,"
					+ "EntCode,"
					+ "Inserted,"
					+ "SaveTermExpired"
					+ ")"
					+ "VALUES ("
					+ isHasValue(paramJSON, "ProcessArchiveID") + ","
					+ isHasValue(paramJSON, "ProcessKind") + ","
					+ isHasValue(paramJSON, "ParentProcessID") + ","
					+ isHasValue(paramJSON, "ProcessDescriptionArchiveID") + ","
					+ (isHasValue(paramJSON, "ProcessName").equals("null") ? "''" : isHasValue(paramJSON, "ProcessName")) + ","
					+ isHasValue(paramJSON, "DocSubject") + ","
					+ isHasValue(paramJSON, "BusinessState") + ","
					+ isHasValue(paramJSON, "InitiatorID") + ","
					+ isHasValue(paramJSON, "InitiatorName") + ","
					+ isHasValue(paramJSON, "InitiatorUnitID") + ","
					+ isHasValue(paramJSON, "InitiatorUnitName") + ","
					+ isHasValue(paramJSON, "FormInstID") + ","
					+ isHasValue(paramJSON, "ProcessState") + ","
					+ (isHasValue(paramJSON, "InitiatorSIPAddress").equals("null")?"''" :  isHasValue(paramJSON, "InitiatorSIPAddress")) + ","
					+ isHasValue(paramJSON, "StartDate") + ","
					+ isHasValue(paramJSON, "EndDate") + ","
					+ isHasValue(paramJSON, "DeleteDate") + ","
					+ (isHasValue(paramJSON, "FormName").equals("null") ? "''" : isHasValue(paramJSON, "FormName")) + ","
					+ isHasValue(paramJSON, "FormPrefix") + ","
					+ isHasValue(paramJSON, "DivisionKind").replaceAll(" ", "") + ","
					+ isHasValue(paramJSON, "DocFolder") + ","
					+ isHasValue(paramJSON, "DocFolderName") + ","
					+ isHasValue(paramJSON, "OwnerUnitID") + ","
					+ isHasValue(paramJSON, "EntCode") + ","
					+ isHasValue(paramJSON, "Inserted") + ","
					+ isHasValue(paramJSON, "SaveTermExpired")
					+ ");";
			
			ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setEscapeProcessing(true);
			ps.setQueryTimeout(100);
			
			//System.out.println("Process Data : " + query);
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			returnAutoID = rs.getString(1);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			ps.close();
			conn.close();
		}

		return returnAutoID;
	}
	
	// insert Process Description Data
	public String insertProcessDescriptionData(JSONObject paramJSON) throws Exception{
		String returnAutoID = "";
		
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = ConnectionFactoryMaria.getDatabaseConnection();
			String query = "INSERT INTO covi_approval4j_store.jwf_processdescriptionarchive ("
					+ "FormInstID,"
					+ "FormID,"
					+ "FormName,"
					+ "FormSubject,"
					+ "IsSecureDoc,"
					+ "IsFile,"
					+ "FileExt,"
					+ "IsComment,"
					+ "DocNo,"
					+ "ApproverCode,"
					+ "ApproverName,"
					+ "ApprovalStep,"
					+ "ApproverSIPAddress,"
					+ "IsReserved,"
					+ "ReservedGubun,"
					+ "ReservedTime,"
					+ "Priority,"
					+ "IsModify,"
					+ "Reserved1,"
					+ "Reserved2,"
					+ "BusinessData1"
					+ ")"
					+ "VALUES ("
					+ isHasValue(paramJSON, "FormInstID") + ","
					+ isHasValue(paramJSON, "FormID") + ","
					+ ((isHasValue(paramJSON, "FormName").equals("null")) ? "' '" : isHasValue(paramJSON, "FormName")) + ","
					+ isHasValue(paramJSON, "DocSubject") + ","
					+ isHasValue(paramJSON, "IsSecureDoc") + ","
					+ isHasValue(paramJSON, "IsFile") + ","
					+ isHasValue(paramJSON, "FileExt") + ","
					+ isHasValue(paramJSON, "IsComment") + ","
					+ isHasValue(paramJSON, "DocNo") + ","
					+ ((isHasValue(paramJSON, "ApproverCode").equals("null")) ? "''" : isHasValue(paramJSON, "ApproverCode")) + ","
					+ ((isHasValue(paramJSON, "ApproverName").equals("null")) ? "''" : isHasValue(paramJSON, "ApproverName")) + ","
					+ ((isHasValue(paramJSON, "ApprovalStep").equals("null")) ? "''" : isHasValue(paramJSON, "ApprovalStep")) + ","
					+ ((isHasValue(paramJSON, "ApproverSIPAddress").equals("null")) ? "''" : isHasValue(paramJSON, "ApproverSIPAddress")) + ","
					+ isHasValue(paramJSON, "IsReserved") + ","
					+ isHasValue(paramJSON, "ReservedGubun") + ","
					+ isHasValue(paramJSON, "ReservedTime") + ","
					+ isHasValue(paramJSON, "Priority") + ","
					+ isHasValue(paramJSON, "IsModify") + ","
					+ isHasValue(paramJSON, "Reserved1") + ","
					+ isHasValue(paramJSON, "Reserved2") + ","
					+ isHasValue(paramJSON, "BusinessData1")
					+ ");";
			
			ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setEscapeProcessing(true);
			ps.setQueryTimeout(100);
			
			//System.out.println("Process Description Data : " + query);
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			returnAutoID = rs.getString(1);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			ps.close();
			conn.close();
		}

		return returnAutoID;
	}
	
	// insert Workitem Data
	public void insertWorkitemData(JSONObject paramJSON) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = ConnectionFactoryMaria.getDatabaseConnection();
			String query = "INSERT INTO covi_approval4j_store.jwf_workitemarchive ("
					+ "PerformerID,"
					+ "TaskID,"
					+ "ProcessArchiveID,"
					+ "WorkItemDescriptionArchiveID,"
					+ "UserCode,"
					+ "UserName,"
					+ "DeputyID,"
					+ "DeputyName,"
					+ "Created,"
					+ "FinishRequested,"
					+ "Finished,"
					+ "Deleted,"
					+ "ActualKind,"
					+ "SubKind,"
					+ "IsBatch,"
					+ "IsMobile,"
					+ "Charge,"
					+ "PIDeleted,"
					+ "PIBusinessState,"
					+ "PIFinished"
					+ ")"
					+ "VALUES ("
					+ isHasValue(paramJSON, "PerformerID") + ","
					+ isHasValue(paramJSON, "TaskID") + ","
					+ isHasValue(paramJSON, "ProcessArchiveID") + ","
					+ isHasValue(paramJSON, "WorkItemDescriptionArchiveID") + ","
					+ isHasValue(paramJSON, "UserCode") + ","
					+ isHasValue(paramJSON, "UserName") + ","
					+ isHasValue(paramJSON, "DeputyID") + ","
					+ isHasValue(paramJSON, "DeputyName") + ","
					+ isHasValue(paramJSON, "Created") + ","
					+ isHasValue(paramJSON, "FinishRequested") + ","
					+ isHasValue(paramJSON, "Finished") + ","
					+ isHasValue(paramJSON, "Deleted") + ","
					+ isHasValue(paramJSON, "ActualKind") + ","
					+ isHasValue(paramJSON, "SubKind") + ","
					+ isHasValue(paramJSON, "IsBatch") + ","
					+ isHasValue(paramJSON, "IsMobile") + ","
					+ isHasValue(paramJSON, "Charge") + ","
					+ isHasValue(paramJSON, "PIDeleted") + ","
					+ isHasValue(paramJSON, "PIBusinessState") + ","
					+ isHasValue(paramJSON, "PIFinished")
					+ ");";
			
			ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setEscapeProcessing(true);
			ps.setQueryTimeout(100);
			
			//System.out.println("Workitem Data : " + query);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			ps.close();
			conn.close();
		}
	}
	
	// insert Circulation Description Data
	public String insertCirculationDescriptionData(JSONObject paramJSON) throws Exception{
		String returnAutoID = "";
		
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = ConnectionFactoryMaria.getDatabaseConnection();
			String query = "INSERT INTO covi_approval4j_store.jwf_circulationboxdescription ("
					+ "FormInstID,"
					+ "FormID,"
					+ "FormPrefix,"
					+ "FormName,"
					+ "FormSubject,"
					+ "IsSecureDoc,"
					+ "IsFile,"
					+ "FileExt,"
					+ "IsComment,"
					+ "ApproverCode,"
					+ "ApproverName,"
					+ "ApprovalStep,"
					+ "ApproverSIPAddress,"
					+ "IsReserved,"
					+ "ReservedGubun,"
					+ "ReservedTime,"
					+ "Priority,"
					+ "IsModify"
					+ ")"
					+ "VALUES ("
					+ isHasValue(paramJSON, "FormInstID") + ","
					+ isHasValue(paramJSON, "FormID") + ","
					+ isHasValue(paramJSON, "FormPrefix") + ","
					+ isHasValue(paramJSON, "FormName") + ","
					+ isHasValue(paramJSON, "FormSubject") + ","
					+ isHasValue(paramJSON, "IsSecureDoc") + ","
					+ isHasValue(paramJSON, "IsFile") + ","
					+ isHasValue(paramJSON, "FileExt") + ","
					+ isHasValue(paramJSON, "IsComment") + ","
					+ isHasValue(paramJSON, "ApproverCode") + ","
					+ isHasValue(paramJSON, "ApproverName") + ","
					+ isHasValue(paramJSON, "ApprovalStep") + ","
					+ isHasValue(paramJSON, "ApproverSIPAddress") + ","
					+ isHasValue(paramJSON, "IsReserved") + ","
					+ isHasValue(paramJSON, "ReservedGubun") + ","
					+ isHasValue(paramJSON, "ReservedTime") + ","
					+ isHasValue(paramJSON, "Priority") + ","
					+ isHasValue(paramJSON, "IsModify")
					+ ");";
			
			ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setEscapeProcessing(true);
			ps.setQueryTimeout(100);
			
			//System.out.println("Circulation Description Data : " + query);
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			returnAutoID = rs.getString(1);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			ps.close();
			conn.close();
		}
		
		return returnAutoID;
	}
	
	// insert Circulation Data
	public String insertCirculationData(JSONObject paramJSON) throws Exception{
		String returnAutoID = "";
		
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = ConnectionFactoryMaria.getDatabaseConnection();
			String query = "INSERT INTO covi_approval4j_store.jwf_circulationbox ("
					+ "CirculationBoxID,"
					+ "ProcessID,"
					+ "CirculationBoxDescriptionID,"
					+ "FormInstID,"
					+ "ReceiptID,"
					+ "ReceiptType,"
					+ "ReceiptName,"
					+ "ReceiptDate,"
					+ "Kind,"
					+ "State,"
					+ "ReadDate,"
					+ "SenderID,"
					+ "SenderName,"
					+ "Subject,"
					+ "Comment,"
					+ "DeletedDate,"
					+ "DataState,"
					+ "RegID,"
					+ "RegDate,"
					+ "ModID,"
					+ "ModDate"
					+ ")"
					+ "VALUES ("
					+ isHasValue(paramJSON, "CirculationBoxID") + ","
					+ isHasValue(paramJSON, "ProcessID") + ","
					+ isHasValue(paramJSON, "CirculationBoxDescriptionID") + ","
					+ isHasValue(paramJSON, "FormInstID") + ","
					+ isHasValue(paramJSON, "ReceiptID") + ","
					+ isHasValue(paramJSON, "ReceiptType") + ","
					+ isHasValue(paramJSON, "ReceiptName") + ","
					+ isHasValue(paramJSON, "ReceiptDate") + ","
					+ isHasValue(paramJSON, "Kind") + ","
					+ isHasValue(paramJSON, "State") + ","
					+ isHasValue(paramJSON, "ReadDate") + ","
					+ isHasValue(paramJSON, "SenderID") + ","
					+ isHasValue(paramJSON, "SenderName") + ","
					+ isHasValue(paramJSON, "Subject") + ","
					+ isHasValue(paramJSON, "Comment") + ","
					+ isHasValue(paramJSON, "DeletedDate") + ","
					+ isHasValue(paramJSON, "DataState") + ","
					+ isHasValue(paramJSON, "RegID") + ","
					+ isHasValue(paramJSON, "RegDate") + ","
					+ isHasValue(paramJSON, "ModID") + ","
					+ isHasValue(paramJSON, "ModDate")
					+ ");";
			
			ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setEscapeProcessing(true);
			ps.setQueryTimeout(100);
			
			//System.out.println("Circulation Data : " + query);
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			returnAutoID = rs.getString(1);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			ps.close();
			conn.close();
		}
		
		return returnAutoID;
	}
	
	// insert ComFile Data
	public void insertComFileData(JSONObject comFileData) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = ConnectionFactoryMaria.getDatabaseConnection();
			String query = "INSERT INTO covi_smart4j.sys_file ("
					+ "StorageID,"
					+ "ServiceType,"
					+ "ObjectID,"
					+ "ObjectType,"
					+ "MessageID,"
					+ "Version,"
					+ "SaveType,"
					+ "LastSeq,"
					+ "Seq,"
					+ "FilePath,"
					+ "FileName,"
					+ "SavedName,"
					+ "Extention,"
					+ "Size,"
					+ "ThumWidth,"
					+ "ThumHeight,"
					+ "Description,"
					+ "RegistDate,"
					+ "RegisterCode"
					+ ")"
					+ "VALUES ("
					+ isHasValue(comFileData, "StorageID") + ","
					+ isHasValue(comFileData, "ServiceType") + ","
					+ isHasValue(comFileData, "ObjectID") + ","
					+ isHasValue(comFileData, "ObjectType") + ","
					+ isHasValue(comFileData, "MessageID") + ","
					+ isHasValue(comFileData, "Version") + ","
					+ isHasValue(comFileData, "SaveType") + ","
					+ isHasValue(comFileData, "LastSeq") + ","
					+ isHasValue(comFileData, "Seq") + ","
					+ isHasValue(comFileData, "FilePath") + ","
					+ isHasValue(comFileData, "FileName") + ","
					+ isHasValue(comFileData, "SavedName") + ","
					+ isHasValue(comFileData, "Extention") + ","
					+ isHasValue(comFileData, "Size") + ","
					+ isHasValue(comFileData, "ThumWidth") + ","
					+ isHasValue(comFileData, "ThumHeight") + ","
					+ isHasValue(comFileData, "Description") + ","
					+ "now(),"
					+ isHasValue(comFileData, "RegisterCode")
					+ ");";
			
			ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setEscapeProcessing(true);
			ps.setQueryTimeout(100);
			
			//System.out.println("ComFile Data : " + query);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			ps.close();
			conn.close();
		}
	}
	
	// insert DocLink Data
	public void insertDocLink(JSONObject docLinkData) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = ConnectionFactoryMaria.getDatabaseConnection();
			String query = "INSERT INTO covi_approval4j_store.migration_doclink ("
					+ "UNIQUEID,"
					+ "SEQ,"
					+ "REF_DocID,"
					+ "REF_Subject"
					+ ")"
					+ "VALUES ("
					+ isHasValue(docLinkData, "UNIQUEID") + ","
					+ isHasValue(docLinkData, "SEQ") + ","
					+ isHasValue(docLinkData, "REF_DocID") + ","
					+ isHasValue(docLinkData, "REF_Subject")
					+ ");";
			
			ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setEscapeProcessing(true);
			ps.setQueryTimeout(100);
			
			//System.out.println("ComFile Data : " + query);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			ps.close();
			conn.close();
		}
	}

	// update ProcessArchive ParentProcessID Data
	public void updateParentProcessID() throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = ConnectionFactoryMaria.getDatabaseConnection();
			
			String query = "UPDATE covi_approval4j_store.jwf_processarchive PA "
			+ "INNER JOIN covi_approval4j_store.jwf_processdescriptionarchive PDA ON PA.ProcessDescriptionArchiveID = PDA.ProcessDescriptionArchiveID "
			+ "INNER JOIN ( "
			+ "	SELECT PA.ProcessArchiveID AS TOBE_PIID, PDA.Reserved1 AS ASIS_PIID FROM covi_approval4j_store.jwf_processarchive PA "
			+ "	INNER JOIN covi_approval4j_store.jwf_processdescriptionarchive PDA ON PA.ProcessDescriptionArchiveID = PDA.ProcessDescriptionArchiveID "
			+ ") PM ON PDA.Reserved2 = PM.ASIS_PIID "
			+ "SET PA.ParentProcessID = PM.TOBE_PIID "
			+ "WHERE PDA.Reserved2 <> ''; ";
			
			ps = conn.prepareStatement(query);
			ps.setEscapeProcessing(true);
			ps.setQueryTimeout(100);
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ps.close();
			conn.close();
		}
	}
	
	// update FormInstance DocLink Data
	public void updateDocLink() throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = ConnectionFactoryMaria.getDatabaseConnection();
	
			String query = "UPDATE COVI_APPROVAL4J_STORE.JWF_FORMINSTANCE FI "
			+ "INNER JOIN ( " 
			+ "SELECT MDL.UNIQUEID " 
			+ "	, MDL.SEQ "
			+ "	, GROUP_CONCAT(CONCAT(PD.ProcessDescriptionArchiveID, '@@@WF_MIGRATION@@@', PD.FormSubject, '@@@', PD.FormInstID, '@@@true') SEPARATOR '^^^') AS DocLinks "
			+ "FROM covi_approval4j_store.MIGRATION_DOCLINK MDL "
			+ "INNER JOIN covi_approval4j_store.jwf_processdescriptionarchive PD ON MDL.REF_DocID = PD.Reserved1 "
			+ "GROUP BY MDL.UNIQUEID "
			+ ") MDL ON FI.FormInstID = MDL.UNIQUEID "
			+ "SET FI.DocLinks = MDL.DocLinks "
			+ "WHERE 1=1;";
			
			ps = conn.prepareStatement(query);
			ps.setEscapeProcessing(true);
			ps.setQueryTimeout(100);
			
			//System.out.println("FormInstance DocLink Data : " + query);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			ps.close();
			conn.close();
		}
	}
	
	public String isHasValue(JSONObject paramJSON, String key){
		if(paramJSON.has(key)){
			if(key.equals("FormSubject") || key.equals("AttachFileInfo"))
				return "'" + paramJSON.getString(key).replace("'", "\\'") + "'";
			else
				return "'" + paramJSON.getString(key) + "'";
		}else{
			return "null";
		}
	}
}
