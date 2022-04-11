package com.prasun.BootCamp.Controller;

import com.prasun.BootCamp.DTOs.AddressDTO;
import com.prasun.BootCamp.DTOs.CustomerDTOs.RequestCustomerDTO;
import com.prasun.BootCamp.DTOs.CustomerDTOs.ResponseCustomerDTO;
import com.prasun.BootCamp.DTOs.PasswordDTO;
import com.prasun.BootCamp.Model.Address;
import com.prasun.BootCamp.Model.ApplicationUser;
import com.prasun.BootCamp.Model.Customer;
import com.prasun.BootCamp.Model.Role;
import com.prasun.BootCamp.repo.AddressRepo;
import com.prasun.BootCamp.repo.CustomerRepo;
import com.prasun.BootCamp.repo.RoleRepo;
import com.prasun.BootCamp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;
     @Autowired
    private PasswordEncoder encoder;

     @Autowired
    AddressRepo addressRepo;

    @PostMapping( "/register")
    public ResponseEntity<ResponseCustomerDTO> registerCustomer(@Valid  @RequestBody RequestCustomerDTO customerDTO) {
        ApplicationUser user = new ApplicationUser();
        user.setFirstName(customerDTO.getFirstName());
        user.setLastName(customerDTO.getLastName());
        user.setPassword(encoder.encode(customerDTO.getPassword()));
        user.setEmail(customerDTO.getEmail());
       user.setMiddleName(customerDTO.getMiddleName());
       user.setPhoneNumber(customerDTO.getPhoneNumber());
        user.setRoles(roleRepo.findByName("ROLE_CUSTOMER"));
        userRepo.save(user);

        Customer customer=new Customer();
        customer.setContact(customerDTO.getContact());
       customer.setUser(user);
      customerRepo.save(customer);
//        private Long id;
//        private String email;
//        private String firstName;
//        private String middleName;
//        private String lastName;
      ResponseCustomerDTO responseCustomerDTO=new ResponseCustomerDTO(user.getId(),user.getEmail(),user.getFirstName(),user.getMiddleName(),user.getLastName());

return new ResponseEntity<ResponseCustomerDTO>(responseCustomerDTO, HttpStatus.CREATED);
    }


    @GetMapping("/profile")
    public ResponseCustomerDTO viewProfile() {
      ApplicationUser user =  (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//ApplicationUser userfound=userRepo.findByEmail(user.getEmail());
        ResponseCustomerDTO responseCustomerDTO=new ResponseCustomerDTO(user.getId(), user.getEmail(), user.getFirstName(), user.getMiddleName(), user.getLastName());
        return responseCustomerDTO;
        //String email, String firstName, String middleName, String lastName
    }
    @PostMapping("/address")
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO){

        ApplicationUser user =  (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser userFound = userRepo.findByEmail(user.getEmail());
        Address address = new Address();
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setAddressLine(addressDTO.getAddressLine());
        address.setZipCode(addressDTO.getZipCode());
        userFound.setAddress(address);
        address.setUser(userFound);
        addressRepo.save(address);

        return new ResponseEntity<AddressDTO>(addressDTO,HttpStatus.CREATED);
    }

    @GetMapping("/address")
    public ResponseEntity<AddressDTO> viewAddress(){
        ApplicationUser user =  (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser userFound = userRepo.findByEmail(user.getEmail());
        Address address = userFound.getAddress();
        AddressDTO addressDTO = new AddressDTO(address.getCity(),address.getState(),address.getCountry(), address.getAddressLine(), address.getZipCode());

        return new ResponseEntity<AddressDTO>(addressDTO, HttpStatus.FOUND);
    }


    @PutMapping("/password")
    public String  updatePassword(@RequestBody PasswordDTO passwordDTO){
        ApplicationUser user =  (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationUser userFound = userRepo.findByEmail(user.getEmail());
        if(!passwordDTO.getPassword().equals(passwordDTO.getConfirmPassword())){
            return "Password and confirm pasword doesnot match";
        }else{
            user.setPassword(encoder.encode(passwordDTO.getPassword()));
        userRepo.save(user);
        }
    return "changed successfully";
    }
}