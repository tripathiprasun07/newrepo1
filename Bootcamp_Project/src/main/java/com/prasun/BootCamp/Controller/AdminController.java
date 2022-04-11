package com.prasun.BootCamp.Controller;

import com.prasun.BootCamp.DTOs.CustomerDTOs.ResponseCustomerDTO;
import com.prasun.BootCamp.DTOs.SellerDTOS.ResponseSellerDTO;
import com.prasun.BootCamp.Model.ApplicationUser;
import com.prasun.BootCamp.Model.Customer;
import com.prasun.BootCamp.Model.Seller;
import com.prasun.BootCamp.repo.CustomerRepo;
import com.prasun.BootCamp.repo.SellerRepo;
import com.prasun.BootCamp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/admin")
public class AdminController {
@Autowired
    SellerRepo sellerRepo;

@Autowired
    UserRepo userRepo;

@Autowired
    CustomerRepo customerRepo;

    @GetMapping("/get/sellers")
    public List<ResponseSellerDTO> getSellers(){

        List<ResponseSellerDTO> list_sellers = new ArrayList<>();


        List<Seller> list = sellerRepo.findAll();



        for(Seller seller : list){

            ResponseSellerDTO sellerres = new ResponseSellerDTO();

            ApplicationUser user = userRepo.findById(seller.getId()).orElse(null);


            sellerres.setEmail(user.getEmail());


           sellerres.setFirstName(user.getFirstName());
           sellerres.setMiddleName(user.getMiddleName());
            sellerres.setId(user.getId());
            sellerres.setLastName(user.getLastName());
            sellerres.setCompanyContact(seller.getCompanyContact());
            sellerres.setCompanyName(seller.getCompanyName());


            list_sellers.add(sellerres);




        }

        return list_sellers;

    }
    @GetMapping("/get/customers")
    public List<ResponseCustomerDTO> getCustomers(){

        List<ResponseCustomerDTO> list_customers = new ArrayList<>();


        List<Customer> list = customerRepo.findAll();



        for(Customer customer : list){

            ResponseCustomerDTO customerres = new ResponseCustomerDTO();

            ApplicationUser user = userRepo.findById(customer.getId()).orElse(null);


            customerres.setEmail(user.getEmail());


            customerres.setFirstName(user.getFirstName());
            customerres.setMiddleName(user.getMiddleName());
            customerres.setId(user.getId());
            customerres.setLastName(user.getLastName());



            list_customers.add(customerres);




        }

        return list_customers;

    }

    @PutMapping("/activate/customer/{cid}")
    public String setActiveCustomer(@PathVariable Long cid){

        Customer customer = customerRepo.findById(cid).orElse(null);




        ApplicationUser user = userRepo.findById(customer.getId()).orElse(null);
        userRepo.setActive(user.getId());



        return "User activated " + user.getUsername() + " " + user.getId();

    }
    @PutMapping("/deactivate/customer/{cid}")
    public String setUnActiveCustomer(@PathVariable Long cid){

        Customer customer = customerRepo.findById(cid).orElse(null);




        ApplicationUser user = userRepo.findById(customer.getId()).orElse(null);

        userRepo.setUnActive(user.getId());



        return "User deactivated " + user.getUsername() + " " + user.getId();

    }
    @PutMapping("/activate/seller/{sid}")
    public String setActiveSeller(@PathVariable Long sid){

        Seller seller = sellerRepo.findById(sid).orElse(null);





        ApplicationUser user = userRepo.findById(seller.getId()).orElse(null);

        userRepo.setActive(user.getId());



        return "User activated " + user.getUsername() + " " + user.getId();

    }

    @PutMapping("/deactivate/seller/{sid}")
    public String setUnActiveSeller(@PathVariable Long sid){

        Seller seller = sellerRepo.findById(sid).orElse(null);




        ApplicationUser user = userRepo.findById(seller.getId()).orElse(null);


        userRepo.setUnActive(user.getId());



        return "User deactivated " + user.getUsername() + " " + user.getId();

    }

}
