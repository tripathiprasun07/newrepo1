package com.prasun.BootCamp.Controller;

import com.prasun.BootCamp.DTOs.Address.AddressDTO;
import com.prasun.BootCamp.DTOs.Address.AddressResDTO;
import com.prasun.BootCamp.DTOs.CustomerDTOs.RequestCustomerDTO;
import com.prasun.BootCamp.DTOs.CustomerDTOs.ResponseCustomerDTO;
import com.prasun.BootCamp.DTOs.PasswordDTO;
import com.prasun.BootCamp.Model.Address;
import com.prasun.BootCamp.Model.User;
import com.prasun.BootCamp.Model.Customer;
import com.prasun.BootCamp.repo.AddressRepo;
import com.prasun.BootCamp.repo.CustomerRepo;
import com.prasun.BootCamp.repo.RoleRepo;
import com.prasun.BootCamp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.Map;

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
        User user = new User();
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

      ResponseCustomerDTO responseCustomerDTO=new ResponseCustomerDTO(user.getId(), user.getEmail(), user.getFirstName(), user.getMiddleName(), user.getLastName(),customer.getContact());
return new ResponseEntity<ResponseCustomerDTO>(responseCustomerDTO, HttpStatus.CREATED);
    }



    @GetMapping("/profile/view")
    public ResponseCustomerDTO viewProfile() {
        User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepo.getOne(user.getId());
        ResponseCustomerDTO customerResDTO = new ResponseCustomerDTO(user.getId(), user.getEmail(), user.getFirstName(), user.getMiddleName(), user.getLastName(),customer.getContact());
        return customerResDTO;
    }
    @PostMapping("/address")
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO){

        User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userFound = userRepo.findByEmail(user.getEmail());
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
        User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userFound = userRepo.findByEmail(user.getEmail());
        Address address = userFound.getAddress();
        AddressDTO addressDTO = new AddressDTO(address.getCity(),address.getState(),address.getCountry(), address.getAddressLine(), address.getZipCode());

        return new ResponseEntity<AddressDTO>(addressDTO, HttpStatus.FOUND);
    }


    @PutMapping("/password")
    public String  updatePassword(@RequestBody PasswordDTO passwordDTO){
        User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userFound = userRepo.findByEmail(user.getEmail());
        if(!passwordDTO.getPassword().equals(passwordDTO.getConfirmPassword())){
            return "Password and confirm pasword doesnot match";
        }else{
            user.setPassword(encoder.encode(passwordDTO.getPassword()));
        userRepo.save(user);
        }
    return "changed successfully";
    }

    @PatchMapping("/address/update/{id}")
    public AddressResDTO updateAddress(@PathVariable long id , @RequestBody Map<Object, Object> map) {
        Address address = addressRepo.findById(id).orElse(null);
        map.forEach((k, v) -> {
            Field field = org.springframework.util.ReflectionUtils.findField(Address.class,(String)k);
            if(field.getName()=="id" || field.getName()=="user_id" ||field.getName()=="label"){
                return;
            }
            field.setAccessible(true);
            System.out.println(field);
            ReflectionUtils.setField(field, address, v);
        });
        addressRepo.save(address);
        Address address1 = addressRepo.getOne(address.getId());
        AddressResDTO addressResDTO = new AddressResDTO(address1.getId(),address1.getCity(),address1.getState(),address1.getCountry(),address1.getAddressLine(),address1.getZipCode());
        return addressResDTO;
    }
    @PatchMapping("/profile/update")
    public ResponseCustomerDTO editProfile( @RequestBody Map<Object, Object> map) {
        User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //UserEntity user = userRepo.findByEmail(email);
        map.forEach((k, v) -> {
            Field field = org.springframework.util.ReflectionUtils.findField(User.class,(String)k);
            if(field.getName()=="email" || field.getName()=="password" ||field.getName()=="id") {
                return;
            }
            field.setAccessible(true);
            System.out.println(field);
            ReflectionUtils.setField(field, user, v);
        });
        userRepo.save(user);
        Customer customer = customerRepo.getOne(user.getId());
        ResponseCustomerDTO customerResDTO = new ResponseCustomerDTO(user.getId(), user.getEmail(), user.getFirstName(), user.getMiddleName(), user.getLastName(),customer.getContact());
        return customerResDTO;
    }

    //@DeleteMapping("/delete/address")
}