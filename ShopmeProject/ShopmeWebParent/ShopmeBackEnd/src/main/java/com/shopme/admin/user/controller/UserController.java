package com.shopme.admin.user.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.admin.user.export.UserExcelExporter;
import com.shopme.admin.user.export.UserPdfExporter;
import com.shopme.admin.user.service.UserNotFoundException;
import com.shopme.admin.user.service.UserService;
import com.shopme.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@ComponentScan
public class UserController {
	@Autowired
	private UserService service;
	
	@RequestMapping({"/users/","/users"})
	public String listAll(Model model) {
		return listByPage(1, model, "firstName", "asc", null);
		//return "redirect:/users/page/1";
		//List<User> users = service.findAllUsers();
		//model.addAttribute("users", users);
		//return "users";
	}
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, 
			Model model,
			@Param("sortField") String sortField,
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		Page<User> page = service.listByPage(pageNum, sortField, sortDir,keyword);
		List<User> listUsers = page.getContent();
		
//		System.out.println("Pagenumber" + pageNum);
//		System.out.println("Total elements = " + page.getTotalElements());
//		System.out.println("Total pages" + page.getTotalPages());

		long startCount = (pageNum - 1)* service.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE -1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
			
		}
		
		String reverseSortDir = sortDir.equals("asc")?  "desc": "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("users", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		return "users/users";
	}
	
	
	@GetMapping("/users/new")
	public String createNewUser(Model model) {
		List<Role> listRoles = service.listRoles();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		return "users/user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			user.setPhotos(fileName);
			User savedUser = service.save(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);			
		}else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			service.save(user);
		}


		
		//service.save(user);
		
		redirectAttributes.addFlashAttribute("message", "the user has been saved successfully.");
		
		String frontPartOftheEmail = user.getEmail().split("@")[0];
		
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword="+frontPartOftheEmail;
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name="id") Integer id,
			Model model,
			RedirectAttributes redirectAttributes) {
		
		try {
		User user =service.getUserById(id);
		List<Role> listRoles = service.listRoles();
		model.addAttribute("user",user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Edit User (ID"+id+")");
		
			return "users/user_form";
		}catch(UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
		
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, 
			Model model, 
			RedirectAttributes redirectAttributes) {
		try {
			service.deleteUserById(id);
			redirectAttributes.addFlashAttribute("message", "The user ID "+id+" hase been deleted successfully.");
		}catch(UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String udpateUserEnabledStatu(
			@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, 
			RedirectAttributes red) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled? "enabled":"disabled";
		String message ="The user with ID "+id+" has been " + status;
		red.addFlashAttribute("message", message);
		return "redirect:/users";
	}
	
	@GetMapping("/users/export/csv")
	public void exportUsersToCsv(HttpServletResponse response) throws IOException {
		List<User> users = service.findAllUsers();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(users, response);
	}
	
	@GetMapping("/users/export/excel")
	public void exportUsersToExcel(HttpServletResponse response) throws IOException {
		List<User> users = service.findAllUsers();
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(users, response);
	}
	
	@GetMapping("/users/export/pdf")
	public void exportUsersToPdf(HttpServletResponse response) throws IOException {
		List<User> users = service.findAllUsers();
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(users, response);
	}	
}


















