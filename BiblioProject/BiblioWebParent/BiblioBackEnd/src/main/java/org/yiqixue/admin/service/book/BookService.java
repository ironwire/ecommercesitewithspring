package org.yiqixue.admin.service.book;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import javax.swing.ImageIcon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.yiqixue.admin.persistence.book.BookImageRepository;
import org.yiqixue.admin.persistence.book.BookRepository;
import org.yiqixue.common.book.Book;
import org.yiqixue.common.book.BookImage;
import org.yiqixue.common.book.Category;

@Service
public class BookService {
	private static final int BOOKS_PER_PAGE = 5;
	
	@Autowired
	BookRepository bookRepo;
	@Autowired
	BookImageRepository imageRepo;
	
	
	public List<BookImage> listAllImagesByBook(Book book){
		return imageRepo.findByBook(book);
	}
	
	public List<Book> listAll(){
		return bookRepo.findAll();
	}
	
	public Book get(Integer id) {
		return bookRepo.findById(id).get();
	}
	
	public void delete(Integer id) {
		bookRepo.deleteById(id);
	}
	
	public Page<Book> listBooksByPage(int pageNum, int pageSize){
		
		
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
		
		//if(keyword != null) return categoryRepo.listByPage(keyword, pageable);
		
		return bookRepo.findAll(pageable);
	}
	
	public Page<Book> listBooksByPage(int pageNum, int pageSize, String sortField, String sortDir, String keyword){
		
		Pageable pageable;
		
		if(sortField!=null && !sortField.isEmpty()) {
			Sort sort = Sort.by(sortField);
			sort = sortDir.equals("asc")? sort.ascending() : sort.descending();
			pageable = PageRequest.of(pageNum, pageSize, sort);
		}else {
			pageable = PageRequest.of(pageNum, pageSize);
		}
		
		if(keyword != null && !keyword.isEmpty()) return bookRepo.listBooksByPage(keyword, pageable);

		return bookRepo.findAll(pageable);
		//		try {
//			return processImages(bookRepo.findAll(pageable));	
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println(e);
//			return null;
//		}
		
		
	}	
	
	public Book save(Book book) {
		return bookRepo.save(book);
	}
	
//	private Page<Book> processImages(Page<Book> page) throws IOException {
//		
//		List<Book> books=page.getContent();
//		for(Book book : books) {
//			for(BookImage image : book.getImages()) {
//				image.setPicByte(Files.readAllBytes(Paths.get(image.getPath())));
//			}
//			
//		}
//		return  page;
//	}
	
}
