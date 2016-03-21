/* Author: Aastha Agrawal
 * Provides three apis 
 * 1. To get the update file
 * 2. To get the MD5 digest file
 * 3. To check whether update is present or not
 */

package com.server.UpdateTry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/updateProcess")
public class api {
	
	@GET
	@Path("/getUpdateFile")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getUpdateFile() {
		
		File file = new File("E:/Project/update_file.txt"); 
		return Response.ok(file,MediaType.APPLICATION_OCTET_STREAM).header("File transfer", file.getName()).build();
	
	}
	
	@GET
	@Path("/getDigestFile")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getDigestFile(){

		Utilities ut = new Utilities();

		//check if the update file is modified since the time digest file is created
		//creating new digest file as update_file has been modified
		File file = new File("E:/Project/MD5Digest.txt");
		if(!file.exists()){
			try {
				System.out.println("***********");
				ut.createDigestFile();
			} catch (NoSuchAlgorithmException | IOException e) {
				e.printStackTrace();
				return Response.serverError().build();
			}
		} else{
			try {
				if(ut.isUpdateFileModified())
					System.out.println("Update file has been modified");
					ut.createDigestFile();
					//check if it is required to explicitly set the lastModified value 
			} catch (ParseException | NullPointerException | NoSuchAlgorithmException | IOException e) {
				e.printStackTrace();
				return Response.serverError().build();
			}	
		}
		//Sending the digest_file
		System.out.println("*********");
		File f = new File("E:/Project/MD5Digest.txt");
		return Response.ok(f,MediaType.APPLICATION_OCTET_STREAM).header("File transfer", f.getName()).build();
	}
	
	

	@GET
	@Path("/checkUpdate/{apiLevelOnDevice}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response isUpdateAvailable(@PathParam("apiLevelOnDevice") Integer apiLevelOnDevice){
		if(apiLevelOnDevice.intValue()<=0)
			return Response.status(400).entity("404 BAD REQUEST: API Level on device cannot be less than 1").build();
		FileInputStream in=null;
		int latestApiLevelOnServer;
		try{
			try {
				in=new FileInputStream("E:/Project/version.txt");
			} catch (FileNotFoundException e) {
				return Response.status(500).build();
			}
			char ch1,ch2;
			try {
				if((ch1=(char)in.read())!=-1){
					if((ch2=(char)in.read())!=-1){
						latestApiLevelOnServer=10*(ch1-48) + (ch2-48);
					}
					else{
						latestApiLevelOnServer=ch1-48;
					}
				}
				else{
					return Response.status(500).build();
				}
			} catch (IOException e) {
				return Response.status(500).build();
			}
		}finally{
				if(in!=null)
					try {
						in.close();
					} catch (IOException e) {
						//do nothing
					}
		}
		if(latestApiLevelOnServer>apiLevelOnDevice.intValue())
			return Response.ok().entity("Update available!!!").build();
		else
			return Response.ok().entity("No updates present!").build();
	}
	
}
