package egovframework.covision.coviflow.migration;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

	public static void main(String[] args) {
		DataMigrationMain migration = new DataMigrationMain();
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
			
			System.out.println(dateFormat.format(new Date()) + " : Start Migration");
			migration.executeDataMigration();
			System.out.println(dateFormat.format(new Date()) + " : Migration complete");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
