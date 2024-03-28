package org.yiqixue.admin.controller.book;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yiqixue.admin.controller.category.CategoryRestController;
import org.yiqixue.admin.persistence.book.BookImageRepository;
import org.yiqixue.admin.service.book.BookService;
import org.yiqixue.common.book.Book;
import org.yiqixue.common.book.BookImage;
import org.yiqixue.common.book.Category;
import org.yiqixue.admin.utils.FileUpLoadUtil;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:4200")
public class BookRestController {

	@Autowired
	private BookService bookService;
	private BookModelAssembler assembler;

	@Autowired
	private PagedResourcesAssembler<Book> pagedResourcesAssembler;

	public BookRestController(BookService service, BookModelAssembler assembler) {
		this.bookService = service;
		this.assembler = assembler;
	}

	@GetMapping
	public CollectionModel<EntityModel<Book>> listAll() {
		List<EntityModel<Book>> listEntityModel = bookService.listAll().stream().map(assembler::toModel)
				.collect(Collectors.toList());

		CollectionModel<EntityModel<Book>> collectionModel = CollectionModel.of(listEntityModel);

		collectionModel.add(linkTo(methodOn(BookRestController.class).listAll()).withSelfRel());

		return collectionModel;
	}

	@GetMapping("/{bookId}/images")
	@CrossOrigin
	public ResponseEntity<List<BookImage>> getImagesByBookId(@PathVariable Integer bookId){
		List<BookImage> bookImages = new ArrayList<>();
		bookImages = bookService.listAllImagesByBook(new Book(bookId));
		
		List<byte[]> physicalImages = getPhysicalImages(bookId);
		
		for(int i=0; i<bookImages.size(); i++) {
			bookImages.get(i).setPicByte(physicalImages.get(i));
		}
		
		return ResponseEntity.ok().body(bookImages);
		
	}

	private static List<byte[]> getPhysicalImages(Integer bookId) {
		List<byte[]> imageBytes = new ArrayList<byte[]>();
		Path path = Paths.get("../book-images/" + bookId.intValue() + "/extras/");
		try {
			List<Path> paths = listFiles(path);

			for (Path file : paths) {
				byte[] raw = null;
				if (file.toFile().exists()) {
					raw = java.nio.file.Files.readAllBytes(file);
					imageBytes.add(raw);
				}

			}
			return imageBytes;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			return null;
		}

	}

	private static List<Path> listFiles(Path path) throws IOException {
		// TODO Auto-generated method stub
		List<Path> result;
		try (Stream<Path> walk = Files.walk(path)) {
			result = walk.filter(Files::isRegularFile).collect(Collectors.toList());
		}
		return result;

	}

	@GetMapping("/page")
	public CollectionModel<EntityModel<Book>> listByPage(@RequestParam(name = "page") int pageNum,
			@RequestParam(name = "size") int pageSize,
			@RequestParam(name = "keyword", required = false) String keyword) {
		Page<Book> listBooks = bookService.listBooksByPage(pageNum, pageSize, null, null, keyword);

		PagedModel<EntityModel<Book>> pagedModel = pagedResourcesAssembler.toModel(listBooks, assembler);

		pagedModel.add(linkTo(methodOn(BookRestController.class).listAll()).withSelfRel());

		return pagedModel;
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Book>> getOne(@PathVariable("id") Integer id) {
		try {
			Book book = bookService.get(id);
			EntityModel<Book> model = assembler.toModel(book);

			return new ResponseEntity<>(model, HttpStatus.OK);
		} catch (NoSuchElementException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) // (consumes={
	@CrossOrigin
	public int add(@RequestBody Book book) { // @RequestBody @Valid

		return book.getBindingType().getCode();
	}

//	@PostMapping(
//			value="/add",
//	            consumes = MediaType.APPLICATION_JSON_VALUE,
//	            produces = MediaType.APPLICATION_JSON_VALUE
//			)//(consumes={ MediaType.MULTIPART_FORM_DATA_VALUE })
//	@CrossOrigin
//	public ResponseEntity<EntityModel<Book>> add(@RequestBody Book book) { //@RequestBody @Valid 
//		Book savedBook = new Book(); 
//		savedBook=bookService.save(book);
//		EntityModel<Book> model = assembler.toModel(savedBook);
//
//		return ResponseEntity
//				.created(linkTo(methodOn(BookRestController.class).getOne(savedBook.getId())).toUri())
//				.body(model);
//	}
//	
	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@CrossOrigin
	public ResponseEntity<EntityModel<Book>> saveNewBook(@RequestPart("book") @Valid Book book,
			@RequestPart(name = "imageFiles", required = false) MultipartFile[] multipartFiles) throws IOException {
		// Book savedBook = new Book();

		setImageNames(multipartFiles, book);

		Book savedBook = bookService.save(book);

		saveUploadedImages(multipartFiles, book);

		EntityModel<Book> model = assembler.toModel(savedBook);

		return ResponseEntity.created(linkTo(methodOn(BookRestController.class).getOne(savedBook.getId())).toUri())
				.body(model);
	}

	private void saveUploadedImages(MultipartFile[] imageMultiparts, Book savedBook) throws IOException {

		if (imageMultiparts.length > 0) {
			String uploadDir = "../book-images/" + savedBook.getId() + "/extras";

			for (MultipartFile multipart : imageMultiparts) {
				if (multipart.isEmpty())
					continue;

				String fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
				FileUpLoadUtil.saveFile(uploadDir, fileName, multipart);
			}
		}

	}

	private void setImageNames(MultipartFile[] imageMultiparts, Book book) {
		if (imageMultiparts.length > 0) {
			for (MultipartFile multipart : imageMultiparts) {
				if (!multipart.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
					String fileType = multipart.getContentType();
					if (!book.containsImageName(fileName)) {
						book.addImage(fileName, fileType);
					}
				}
			}
		}
	}
}
