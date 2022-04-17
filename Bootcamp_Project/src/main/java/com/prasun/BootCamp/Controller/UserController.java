package com.prasun.BootCamp.Controller;

import com.prasun.BootCamp.DTOs.ResponseUserDTO;
import com.prasun.BootCamp.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.prasun.BootCamp.Model.User;
import com.prasun.BootCamp.repo.UserRepo;

import javax.validation.Valid;


@RestController

public class UserController {

	@Autowired
	UserRepo userRepo;


	@Autowired
	private SecurityService securityService;

	@Autowired
	private PasswordEncoder encoder;

	@GetMapping("/user/{user}")
	public ResponseUserDTO getUser(@PathVariable String user) {
		User userFound = userRepo.findByEmail(user);
		ResponseUserDTO dto=new ResponseUserDTO();
		dto.setEmail(userFound.getEmail());
		dto.setFirstName(userFound.getFirstName());
		dto.setMiddleName(userFound.getMiddleName());
		dto.setLastName(userFound.getLastName());
		//dto.setPhoneNumber(userFound.getPhoneNumber());

		return dto;
	}

	@PostMapping("/registerUser")


	public ResponseEntity<User> createUser(@Valid @RequestBody User user){

		user.setPassword(encoder.encode(user.getPassword()));


		User userc=userRepo.save(user);

		return new ResponseEntity<User>(userc, HttpStatus.CREATED);

	}


	@RequestMapping("/current/user")
	public ResponseUserDTO getCurrentUser() {
		System.out.println(SecurityContextHolder.getContext().getAuthentication());
		User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		User userFound = userRepo.findByEmail(user.getEmail());
		ResponseUserDTO dto=new ResponseUserDTO();
		dto.setId(userFound.getId());
		dto.setEmail(userFound.getEmail());
		dto.setFirstName(userFound.getFirstName());
		dto.setMiddleName(userFound.getMiddleName());
		dto.setLastName(userFound.getLastName());
		//dto.setPhoneNumber(userFound.getPhoneNumber());
		return dto;
	}
}
	

