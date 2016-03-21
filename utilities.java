/* Author: Aastha Agrawal
 * Utilities file
 */

package com.server.UpdateTry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;

public class Utilities{

	public boolean isUpdateFileModified() throws ParseException, NullPointerException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				
		//obtaining time stamp of digest file
		File digestFile = new File("E:/Project/MD5Digest.txt");
		if(!digestFile.exists())
			return true;
		Date digestDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(sdf.format(digestFile.lastModified()));
		System.out.println(digestDate);
		
		//obtaining time stamp of the update_file
		File updateFile = new File("E:/Project/update_file.txt");
		Date updateDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(sdf.format(updateFile.lastModified()));
		System.out.println(updateDate);
		
		//comparing the two time stamps
		return digestDate.before(updateDate);
		
		/*
		if(digestDate.after(updateDate))
			return false;
		else
			return true;
		*/
	}

	public void createDigestFile() throws IOException, NoSuchAlgorithmException {
		//Creating hash file
			System.out.println("**********************");
			MessageDigest md = MessageDigest.getInstance("MD5");
			InputStream is = Files.newInputStream(Paths.get("E:/Project/update_file.txt"));
			byte[] bytes = new byte[2048];
			int numbytes;
			while((numbytes=is.read(bytes))!=-1){
				md.update(bytes,0,numbytes);
			}
			byte[] digest = md.digest();
			System.out.println(digest);
			String hash = new String(Hex.encodeHex(digest));
			//String hash = digest.toString();
			System.out.println(hash);
					
			File hashFile = new File("E:/Project/MD5Digest.txt");
			if(!hashFile.exists()){
				hashFile.createNewFile();
			}
			FileWriter fw = new FileWriter(hashFile.getAbsoluteFile()); 
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(hash);
			bw.close();				
		
	}
}
