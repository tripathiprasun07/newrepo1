package com.prasun.BootCamp.Controller;

import com.prasun.BootCamp.DTOs.Address.AddressResDTO;
import com.prasun.BootCamp.DTOs.PasswordDTO;
import com.prasun.BootCamp.DTOs.SellerDTOS.RequestSellerDTO;
import com.prasun.BootCamp.DTOs.SellerDTOS.ResponseSellerDTO;
import com.prasun.BootCamp.ExceptionHandler.CustomerAlreadyExist;
import com.prasun.BootCamp.Model.Address;
import com.prasun.BootCamp.Model.User;
import com.prasun.BootCamp.Model.Seller;
import com.prasun.BootCamp.repo.AddressRepo;
import com.prasun.BootCamp.repo.RoleRepo;
import com.prasun.BootCamp.repo.SellerRepo;
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
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private SellerRepo sellerRepo;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    AddressRepo addressRepo;
    @PostMapping("/register")
    public ResponseEntity<ResponseSellerDTO> createSeller(@Valid @RequestBody RequestSellerDTO sellerDTO){

        Seller seller = new Seller();
        seller.setGst(sellerDTO.getGst());
        seller.setCompanyContact(sellerDTO.getCompanyContact());
        seller.setCompanyName(sellerDTO.getCompanyName());

        User user = new User();
        user.setEmail(sellerDTO.getEmail());
        user.setFirstName(sellerDTO.getFirstName());
        user.setMiddleName(sellerDTO.getMiddleName());
        user.setLastName(sellerDTO.getLastName());
        user.setPassword(encoder.encode(sellerDTO.getPassword()));
        user.setRoles(roleRepo.findByName("ROLE_SELLER"));

        Address address = new Address();
        address.setCity(sellerDTO.getAddressDTO().getCity());
        address.setState(sellerDTO.getAddressDTO().getState());
        address.setCountry(sellerDTO.getAddressDTO().getCountry());
        address.setAddressLine(sellerDTO.getAddressDTO().getAddressLine());
        address.setZipCode(sellerDTO.getAddressDTO().getZipCode());
        user.setAddress(address);
        address.setUser(user);

        seller.setUser(user);
        sellerRepo.save(seller);
        addressRepo.save(address);
        userRepo.save(user);

        ResponseSellerDTO sellerResDTO = new ResponseSellerDTO(user.getId(), user.getEmail(), user.getFirstName(), user.getMiddleName(), user.getLastName(),seller.getCompanyContact(),seller.getCompanyName(),seller.getGst());

        return new ResponseEntity<ResponseSellerDTO>(sellerResDTO, HttpStatus.CREATED);
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

    @GetMapping("/address/view")
    public ResponseEntity<AddressResDTO> viewAddress(){
        User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Address address = user.getAddress();
        AddressResDTO addressDTO = new AddressResDTO(address.getId(),address.getCity(),address.getState(),address.getCountry(),address.getAddressLine(), address.getZipCode());
        return new ResponseEntity<AddressResDTO>(addressDTO,HttpStatus.FOUND);
    }
    @PatchMapping("/address/update/{id}")
    public AddressResDTO updateAddress(@PathVariable long id , @RequestBody Map<Object, Object> map) {
        Address address = addressRepo.findById(id).orElse(null);
        map.forEach((k, v) -> {
            Field field = org.springframework.util.ReflectionUtils.findField(Address.class,(String)k);
            if(field.getName()=="id" || field.getName()=="user_id") {
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
    public ResponseSellerDTO editProfile( @RequestBody Map<Object, Object> map) {
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
        try{
            userRepo.save(user);
        }catch (Exception ex){
           throw new CustomerAlreadyExist("Bad Request");
        }

        Seller seller = sellerRepo.getOne(user.getId());
        ResponseSellerDTO sellerResDTO = new ResponseSellerDTO(user.getId(), user.getEmail(), user.getFirstName(), user.getMiddleName(), user.getLastName(),seller.getCompanyContact(),seller.getCompanyName(),seller.getGst());
        return sellerResDTO;
    }







}
