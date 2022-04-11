package com.prasun.BootCamp.Controller;

import com.prasun.BootCamp.DTOs.AddressDTO;
import com.prasun.BootCamp.DTOs.PasswordDTO;
import com.prasun.BootCamp.DTOs.SellerDTOS.RequestSellerDTO;
import com.prasun.BootCamp.DTOs.SellerDTOS.ResponseSellerDTO;
import com.prasun.BootCamp.Model.Address;
import com.prasun.BootCamp.Model.ApplicationUser;
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

        ApplicationUser user = new ApplicationUser();
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

        ResponseSellerDTO sellerResDTO = new ResponseSellerDTO(user.getId(), user.getEmail(), user.getFirstName(), user.getMiddleName(), user.getLastName());

        return new ResponseEntity<ResponseSellerDTO>(sellerResDTO, HttpStatus.CREATED);
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

    @GetMapping("/address/view")
    public ResponseEntity<AddressDTO> viewAddress(){
        ApplicationUser user =  (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Address address = user.getAddress();
        AddressDTO addressDTO = new AddressDTO(address.getCity(),address.getState(),address.getCountry(),address.getAddressLine(), address.getZipCode());
        return new ResponseEntity<AddressDTO>(addressDTO,HttpStatus.FOUND);
    }
    @PatchMapping("/address/update/{id}")
    public String updateAddress(@PathVariable long id , @RequestBody Map<Object, Object> map) {
        Address address = addressRepo.findById(id).orElse(null);

        map.forEach((k, v) -> {
            Field field = org.springframework.util.ReflectionUtils.findField(Address.class,(String)k);
            if(field.getName()=="id" || field.getName()=="user_id") {
                return;
            }
            field.setAccessible(true);
            System.out.println(">>>>>>"+field);
            ReflectionUtils.setField(field, address, v);
        });
        addressRepo.save(address);
        Address address1 = addressRepo.getOne(address.getId());

        return "Changed SUccessfully";
    }






//    @PutMapping("/address")
//    public String  updatePassword(@Valid @RequestBody RequestSellerDTO sellerDTO){
//
//    return "successfull";
//    }




}
