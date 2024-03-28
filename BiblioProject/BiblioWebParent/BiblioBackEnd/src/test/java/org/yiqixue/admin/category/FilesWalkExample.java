package org.yiqixue.admin.category;

import static org.mockito.ArgumentMatchers.nullable;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.xpath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Path;



public class FilesWalkExample {
	
	public static void main(String[] args) throws IOException{
		Path path = Paths.get("../book-images/28/extras/");
		List<Path> paths = listFiles(path);
		paths.forEach(x->{System.out.println(processImages(x));});
	}

	private static List<Path> listFiles(Path path) throws IOException{
		// TODO Auto-generated method stub
	       List<Path> result;
	        try (Stream<Path> walk = Files.walk(path)) {
	            result = walk.filter(Files::isRegularFile)
	                    .collect(Collectors.toList());
	        }
	        return result;
		
		
	}
	
	private static byte[] processImages(Path file) {
		try {
			byte[] raw = null;
			if(file.toFile().exists()) {
				raw = java.nio.file.Files.readAllBytes(file);
				
			}
			return raw;
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			return null;
		}
	}
}
