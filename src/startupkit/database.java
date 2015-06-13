package startupkit;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.amazonaws.services.simpledb.model.SelectRequest;
//import com.amazonaws.AmazonClientException;
//import com.amazonaws.AmazonServiceException;
//import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
//import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
/**
 * @author Charles Chong
 */

public class database {
	
	private static AmazonSimpleDB sdb = new AmazonSimpleDBClient (new ClasspathPropertiesFileCredentialsProvider());
	private static Region usWest2 = Region.getRegion(Regions.US_WEST_2);
	
	/**
	 * Prints out Attributes for selected Item
	 * @param domainName the name of the domain created by method
	 */
	
	public static void getAttributesForItem(String domainName)
	{
		sdb.setRegion(usWest2);
		
		String selectExpression = "select * from `" + domainName + "` where Category = 'Clothes'";
		SelectRequest selectRequest = new SelectRequest(selectExpression);
		for(Item item : sdb.select(selectRequest).getItems()){
			System.out.println("  Item");
			System.out.println("   Name: " + item.getName());	
			for(Attribute attribute : item.getAttributes())
			{
				System.out.println("    Attribute");  //SDB stores and returns key/value pairs
				System.out.println("    Name: " + attribute.getName()); //JSON
				System.out.println("    Value: " + attribute.getValue());
			}
		}
	}
	/**
	 * Item Sample data with attributes to add in a Domain
	 * @return boolean confirms method works
	 */
	@SuppressWarnings("unused")
	private static List<ReplaceableItem> createSampleData() 
	{
		List<ReplaceableItem> sampleData = new ArrayList<ReplaceableItem>(); 
		
		sampleData.add(new ReplaceableItem("Item_01").withAttributes(
				new ReplaceableAttribute("Category", "Clothes", true),
				new ReplaceableAttribute("Subcategory", "Sweater", true),
				new ReplaceableAttribute("Name", "Cat Hair Sweater", true),
				new ReplaceableAttribute("Color", "Siamese", true),
				new ReplaceableAttribute("Size", "Small", true),
				new ReplaceableAttribute("Size", "Medium", true),
				new ReplaceableAttribute("Size", "Large", true)
				));
		
		sampleData.add(new ReplaceableItem("Item_02").withAttributes(
				new ReplaceableAttribute("Category", "Clothes", true),
				new ReplaceableAttribute("Subcategory", "Pants", true),
				new ReplaceableAttribute("Name", "Designer Jeans", true),
				new ReplaceableAttribute("Color", "Paisley Acid Wash", true),
				new ReplaceableAttribute("Size", "30x32", true),
				new ReplaceableAttribute("Size", "32x34", true),
				new ReplaceableAttribute("Size", "48x40", true)
				));
		
		sampleData.add(new ReplaceableItem("Item_03").withAttributes(
				new ReplaceableAttribute("Category", "Clothes", true),
				new ReplaceableAttribute("Subcategory", "Pants", true),
				new ReplaceableAttribute("Name", "Sweatpants", true),
				new ReplaceableAttribute("Color", "Pink", true),
				new ReplaceableAttribute("Color", "Red", true),
				new ReplaceableAttribute("Color", "Chartreuse", true),
				new ReplaceableAttribute("Size", "Small", true),
				new ReplaceableAttribute("Year", "2006", true),
				new ReplaceableAttribute("Year", "2007", true)
				));
		
		return sampleData;
	}
	
	/**
	 * Item New data with attributes to add in a Domain
	 * @return boolean confirms method works
	 */
	private static List<ReplaceableItem> createData(String newItemName)
	{
		List<ReplaceableItem> newData = new ArrayList<ReplaceableItem>();
		newData.add(new ReplaceableItem(newItemName).withAttributes(
				new ReplaceableAttribute("Category", "Tools", true),
				new ReplaceableAttribute("Subcategory", "Auto", true),		
				new ReplaceableAttribute("Name", "Wrench", true),		
				new ReplaceableAttribute("Color", "Silver", true),		
				new ReplaceableAttribute("Color", "Blue", true),		
				new ReplaceableAttribute("Color", "Red", true),			
				new ReplaceableAttribute("Size", "Large", true),		
				new ReplaceableAttribute("Year", "2006", true),		
				new ReplaceableAttribute("Year", "2007", true)
				));
		return newData;
	}
	/**
	 * Creates a Domain in Amazon SimpleDB
	 * Makes domain a variable to use for other methods 
	 * @param domainName the name of the domain created by method
	 * @return boolean confirms method works
	 */
	public static boolean createDB(String domainName)
	{
			//CREATE A DOMAIN TO USE, TO PUSH DATA
			//String myDomain = "Hump"; //a sample domain, for test purposes
			sdb.createDomain(new CreateDomainRequest(domainName));	
			return true;
		}
	
	/**
	 * Executes query to Database and returns results
	 * @param domainName the name of the domain created by method
	 * @param query the statement that will be executed to the database
	 * @return boolean confirms method works
	 */
	public static boolean read(String domainName,String query)
	{
		sdb.setRegion(usWest2);
		String selectExpression = query; 
		SelectRequest selectRequest = new SelectRequest(selectExpression);
		for (Item item : sdb.select(selectRequest).getItems())
		{
			System.out.println("  Item");
			System.out.println("   Name: " + item.getName());
			for( Attribute attribute : item.getAttributes())
			{
				System.out.println("      Attribute"); //SDB JSON ARRIVES IN NAME/VALUE PAIRS
				System.out.println("       Name:  " + attribute.getName());
				System.out.println("        Value: " + attribute.getValue());
			}
		}		
		return true;
	}
	
	/**
	 * Writes data into selected Domain of an item 
	 * @param domainName the database table
	 * @param itemName the name of the item
	 * @param itemAttribWrite written attribute to item
	 * @return boolean confirms method works
	 */
	public static boolean write(String domainName, String itemName, String itemAttribWrite)
	{
		sdb.setRegion(usWest2);
		sdb.batchPutAttributes(new BatchPutAttributesRequest(domainName, createData(itemName)));
		return true;
	}
	/**
	 * Updates attribute information of a Item in the selected Domain
	 * @param domainName the name of the domain created by method
	 * @param itemName the name of the item 
	 * @param itemAttribUpdate updated attribute properties
	 * @return boolean confirms method works
	 */
	public static boolean update(String domainName, String itemName, String itemAttribUpdate)
	{
		sdb.setRegion(usWest2);
		List<ReplaceableAttribute> replaceableAttributes = new ArrayList<ReplaceableAttribute>();
		replaceableAttributes.add(new ReplaceableAttribute("Size", itemAttribUpdate, true));
		sdb.putAttributes(new PutAttributesRequest(domainName, itemName, replaceableAttributes));
		return true;
	}	
	
	/**
	 * Deletes attribute information of a Item in the selected Domain
	 * @param domainName the name of the domain created by method
	 * @param itemName the name of the item 
	 * @return boolean confirms method works
	 */
	public static boolean delete(String domainName, String itemName)
	{
		sdb.setRegion(usWest2);
		sdb.deleteAttributes(new DeleteAttributesRequest(domainName, itemName));
		return true;
	}	
	
}//end class
