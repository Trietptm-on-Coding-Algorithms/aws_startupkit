package test;
import java.io.IOException;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import startupkit.database;
import startupkit.file;


/**
 * 
 * @author Charles Chong
 *
 */
public class Unit_Test_1 {

	private static AmazonSimpleDB sdb = new AmazonSimpleDBClient (new ClasspathPropertiesFileCredentialsProvider()); 
	private static database db = new database();//putting into place the objects I wish to test
	@SuppressWarnings("static-access")
	
	public static void main(String[] args) throws IOException 
	{		
		file f = new file(); //bucketName, key, directory path of file
		if(f.uploadFile("bucket-test-now","MyObjectKeyNew", "./src/startupkit/TestFileName.txt") == true) 
		{
			System.out.println("File Upload OK \n");
			if(f.downloadFile("bucket-test-now", "MyObjectKeyNew") == true)
			{
				System.out.println("File Download OK \n");
				if((f.deleteFile("bucket-test-now", "MyObjectKeyNew")) == true)
				{
					System.out.println("Delete Download OK \n");
				}
			}
		}
		else //To signal that a failure took place
		{
			System.out.println("Looks like the file op failed");
		}

		//Creating DB & Listing Current DB Test
		if(db.createDB("TestSimpleDB") == true) //boolean result
		{
			System.out.println("Database creation OK \n");
			for (String domainName : sdb.listDomains().getDomainNames()) //FETCH A LIST OF ALL MY DATABASE DOMAINS
			{
				System.out.println("--" + domainName);
			}
			//Reading Test
			if(db.read("MyStore", "select * from MyStore") == true)
			{
				System.out.println("Database Item Read OK \n");
				if(db.write("MyStore", "ItemNameTest", "MyStore") == true){
					System.out.println("Database write OK \n");
					System.out.println("Showing Written Item");
					System.out.println("--------------------------------------------");
					db.read("MyStore", "select * from MyStore");
					if (db.update("MyStore","ItemNameTest","Small") == true){
						if (db.delete("MyStore", "ItemNameTest") == true);
						System.out.println("Database Item Update OK \n");
						System.out.println("--------------------------------------------");
						db.read("MyStore", "select * from MyStore");
						System.out.println("Database Item Delete OK \n");
					}
				}
			}
			
		}
	
		
		
	}

}
