package org.yiqixue.hateoas.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;


public class FileDownloadutil {
	private Path foundFile;
	
	public Resource getFileAsResource(String fileCode) throws IOException {
		Path dirPath = Paths.get("Files-Upload");
		
		Files.list(dirPath).forEach(file ->{
			if(file.getFileName().toString().startsWith(fileCode)) {
				foundFile = file;
				return;
			}
		});
		
		if(foundFile != null) {
			return new UrlResource(foundFile.toUri());
		}
		
		return null;
	}
}
