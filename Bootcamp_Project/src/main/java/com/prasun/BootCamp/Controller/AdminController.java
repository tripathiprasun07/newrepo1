package com.prasun.BootCamp.Controller;

import com.prasun.BootCamp.DTOs.Category.CategoryDTO;
import com.prasun.BootCamp.DTOs.Category.CategoryFieldValueResDTO;
import com.prasun.BootCamp.DTOs.Category.CategoryMetadataFieldDTO;
import com.prasun.BootCamp.DTOs.Category.CategoryMetadataFieldValueDTO;
import com.prasun.BootCamp.DTOs.CustomerDTOs.ResponseCustomerDTO;
import com.prasun.BootCamp.DTOs.SellerDTOS.ResponseSellerDTO;
import com.prasun.BootCamp.Model.User;
import com.prasun.BootCamp.Model.Category.Category;
import com.prasun.BootCamp.Model.Category.CategoryMetadataField;
import com.prasun.BootCamp.Model.Customer;
import com.prasun.BootCamp.Model.Seller;
import com.prasun.BootCamp.Service.CategoryService;
import com.prasun.BootCamp.repo.CategoryRepo.CategoryMetadataFieldRepo;
import com.prasun.BootCamp.repo.CategoryRepo.CategoryRepo;
import com.prasun.BootCamp.repo.CustomerRepo;
import com.prasun.BootCamp.repo.SellerRepo;
import com.prasun.BootCamp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
@Autowired
    SellerRepo sellerRepo;

@Autowired
    UserRepo userRepo;

@Autowired
    CustomerRepo customerRepo;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    CategoryMetadataFieldRepo categoryMetadataFieldRepo;

    @GetMapping("/get/sellers")
    public List<ResponseSellerDTO> getSellers(){

        List<ResponseSellerDTO> list_sellers = new ArrayList<>();


        List<Seller> list = sellerRepo.findAll();



        for(Seller seller : list){

            ResponseSellerDTO sellerres = new ResponseSellerDTO();

            User user = userRepo.findById(seller.getId()).orElse(null);


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

            User user = userRepo.findById(customer.getId()).orElse(null);


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




        User user = userRepo.findById(customer.getId()).orElse(null);
        userRepo.setActive(user.getId());



        return "User activated " + user.getUsername() + " " + user.getId();

    }
    @PutMapping("/deactivate/customer/{cid}")
    public String setUnActiveCustomer(@PathVariable Long cid){

        Customer customer = customerRepo.findById(cid).orElse(null);




        User user = userRepo.findById(customer.getId()).orElse(null);

        userRepo.setUnActive(user.getId());



        return "User deactivated " + user.getUsername() + " " + user.getId();

    }
    @PutMapping("/activate/seller/{sid}")
    public String setActiveSeller(@PathVariable Long sid){

        Seller seller = sellerRepo.findById(sid).orElse(null);





        User user = userRepo.findById(seller.getId()).orElse(null);

        userRepo.setActive(user.getId());



        return "User activated " + user.getUsername() + " " + user.getId();

    }

    @PutMapping("/deactivate/seller/{sid}")
    public String setUnActiveSeller(@PathVariable Long sid){

        Seller seller = sellerRepo.findById(sid).orElse(null);




        User user = userRepo.findById(seller.getId()).orElse(null);


        userRepo.setUnActive(user.getId());



        return "User deactivated " + user.getUsername() + " " + user.getId();

    }


    @PostMapping("/category/add")
    public Category addCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        if(categoryDTO.getParentId()==0)
            return categoryService.addCategory(categoryDTO.getName());
        else{
            return categoryService.addCategory(categoryDTO.getName(),categoryDTO.getParentId());
        }
    }

    @PostMapping("/metadata/add")
    public CategoryMetadataField addField(@Valid @RequestBody CategoryMetadataFieldDTO categoryMetadataFieldDTO){
        return categoryService.addField(categoryMetadataFieldDTO.getName());
    }

    @GetMapping("/metadata/view")
    public List<CategoryMetadataField> getFieldList(){
        return categoryMetadataFieldRepo.findAll(Sort.by("id"));
    }


    @GetMapping("/category/view")
    public List<Category> getCategoryList() {
        return categoryRepo.findAll(Sort.by("id"));
    }

    @GetMapping("/category/{id}")
    public Map<String,List<Category>> getCategoryById(@PathVariable long id) {
        return categoryService.viewCategoryById(id);
    }






}
