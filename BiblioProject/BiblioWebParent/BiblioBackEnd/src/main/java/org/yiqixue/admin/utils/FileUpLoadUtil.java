package org.yiqixue.admin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUpLoadUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUpLoadUtil.class);
	
	public static void saveFile(String uploadDir, String fileName,
			MultipartFile multipartFile) throws IOException{
		Path uploadPath = Paths.get(uploadDir);
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try(InputStream inputStream = multipartFile.getInputStream()){
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		}catch(IOException ex) {
			throw new IOException("Could not save file: "+ fileName, ex);
		}
	}
	
	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);
		System.out.println("dirPath------------: "+dirPath); //here dirPath is '../category-images/5'
		try {
			Files.list(dirPath).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException ex) {
						LOGGER.error("Could not delete file: " + file);
					}
				}
			});
		} catch (IOException ex) {
			LOGGER.error("Could not list directory: " + dirPath);
		}
	}
	
	public static void removeDir(String dir) {
		cleanDir(dir);
		
		try {
			Files.delete(Paths.get(dir));
		}catch (IOException e) {
			LOGGER.error("Could not remove directory: "+dir);
		}
	}
//	public static String saveFile(String fileName,String fileTag, MultipartFile multipartFile)
//            throws IOException {
//        Path uploadPath = Paths.get("Files-Upload"+"/"+fileTag);
//          
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
// 
//        String fileCode = RandomStringUtils.randomAlphanumeric(5);
//         
//        try (InputStream inputStream = multipartFile.getInputStream()) {
//            Path filePath = uploadPath.resolve(fileCode + "-" + fileName);
//            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException ioe) {       
//            throw new IOException("Could not save file: " + fileName, ioe);
//        }
//         
//        return fileCode;
//    }
}
