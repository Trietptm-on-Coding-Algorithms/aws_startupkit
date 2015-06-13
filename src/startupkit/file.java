package startupkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
//import com.amazonaws.services.s3.model.Bucket;
//import com.amazonaws.services.s3.model.ListObjectsRequest;
//import com.amazonaws.services.s3.model.ObjectListing;
//import com.amazonaws.services.s3.model.S3ObjectSummary;
//import com.amazonaws.services.s3.model.PutObjectResult;
/**
 * 
 * @author Charles Chong
 *
 */
public class file {
	private static Region usWest2 = Region.getRegion(Regions.US_WEST_2);
	private static AWSCredentials credentials = new ProfileCredentialsProvider("charlesc").getCredentials();
	private static AmazonS3 s3 = new AmazonS3Client(credentials);
	public file() {
	}
	/**
	 * Pushes selected file to the AWS S3 bucket
	 * @param bucketName name of S3 bucket container
	 * @param key the directory containing media content
	 * @param fileLocation the directory containing file on local system
	 * @throws IOException checks for errors, throws exception to continue if error found
	 * @return boolean confirms method works
	 */
	public static boolean uploadFile(String bucketName, String key, String fileLocation)throws IOException {
		try {
		s3.setRegion(usWest2);
		File file = new File(fileLocation);
		s3.putObject(new PutObjectRequest(bucketName, key, file));
		
	}  catch (AmazonServiceException ase) {
        System.out.println("Caught an AmazonServiceException, which means your request made it "
                + "to Amazon S3, but was rejected with an error response for some reason.");
        System.out.println("Error Message:    " + ase.getMessage());
        System.out.println("HTTP Status Code: " + ase.getStatusCode());
        System.out.println("AWS Error Code:   " + ase.getErrorCode());
        System.out.println("Error Type:       " + ase.getErrorType());
        System.out.println("Request ID:       " + ase.getRequestId());
    } catch (AmazonClientException ace) {
        System.out.println("Caught an AmazonClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with S3, "
                + "such as not being able to access the network.");
        System.out.println("Error Message: " + ace.getMessage());
    } 
		return true;
	}

	/**
	 * Downloads file from S3 Bucket
	 * @param bucketName name of S3 bucket container
	 * @param key the directory containing media content
	 * @return boolean confirms method works
	 * @throws IOException checks for errors, throws exception to continue if error found
	 */
	public static boolean downloadFile(String bucketName, String key) throws IOException {
		s3.setRegion(usWest2);             
		S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
		displayTextInputStream(object.getObjectContent());
		return true;
	}
	/**
	 * Delete file in S3 Bucket
	 * @param bucketName name of S3 bucket container
	 * @param key the directory containing media content
	 * @return deletes file in selected S3 Bucket
	 */
	public static boolean deleteFile(String bucketName, String key) {
		s3.setRegion(usWest2);
		s3.deleteObject(bucketName, key);
		return true;
	}
	
	/**
	 * @param input displays output of read buffer
	 * @throws IOException checks for errors, throws exception to continue if error found
	 */
	private static void displayTextInputStream(InputStream input)throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;
			System.out.println("    " + line);
		}
		System.out.println();
	}

}
