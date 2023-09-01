package com.netsmartz.springsecurity.controller;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.netsmartz.springsecurity.model.UserDetails;
import com.netsmartz.springsecurity.repository.UserRepository;
import com.netsmartz.springsecurity.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository repository;

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String home() {
		return "user/home";
	}

//	@GetMapping("/getAll")
//	public String getAll(Model m) {
//		List<UserDetails> list = userService.getAllUser();
//		m.addAttribute("list", list);
//		return "user/getAll";
//	}

	@GetMapping("/getAll")
	public String getAll(@RequestParam(defaultValue = "0") int page, Model model,
			@RequestParam(defaultValue = "10") int selectedPageSize) {
		int pageSize = selectedPageSize; // Number of items per page

		System.out.println("\nSelected size is :" + pageSize);
//		PageRequest pageRequest = PageRequest.of(page, pageSize);
		Page<UserDetails> dtosPage = userService.getPaginated(page, pageSize);

		model.addAttribute("page", dtosPage);

		System.out.println(model);
		return "/user/getAll";
	}

//	@GetMapping("/list")
//	public String listCustomers(Model model, @RequestParam(defaultValue = "0") int page,
//			@RequestParam(defaultValue = "6") int selectedPageSize) {
//		int pageSize = selectedPageSize;
//		// Perform logic to retrieve paginated list of customers
//		System.out.println("\nSelected size is :" + pageSize);
//		PageRequest pageRequest = PageRequest.of(page, pageSize);
//		Page<UserDetails> dtosPage = userService.getAll(pageRequest);
//
//		model.addAttribute("page", dtosPage);
//
//		model.addAttribute("currentPage", page);
//
//		System.out.println(model);
//		return "user/getAll";
//
//	}

	@ModelAttribute
	private void userDetails(Model m, Principal p) {
		String email = p.getName();
		UserDetails user = repository.findByEmail(email);
		m.addAttribute("user", user);
	}

	@GetMapping(value = "/pdf/{email}", produces = "application/pdf")
	public ResponseEntity<InputStreamResource> createPdf(@PathVariable String email){
		ByteArrayInputStream pdf = userService.createPdf(email);
		String filename = "Student.pdf";
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Disposition", "attachment; filename=" + filename);
		return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
	}

}
