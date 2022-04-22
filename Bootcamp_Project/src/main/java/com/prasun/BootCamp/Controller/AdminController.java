package com.prasun.BootCamp.Controller;

import com.prasun.BootCamp.DTOs.Category.CategoryDTO;
import com.prasun.BootCamp.DTOs.Category.CategoryFieldValueResDTO;
import com.prasun.BootCamp.DTOs.Category.CategoryMetadataFieldDTO;
import com.prasun.BootCamp.DTOs.Category.CategoryMetadataFieldValueDTO;
import com.prasun.BootCamp.DTOs.CustomerDTOs.ResponseCustomerDTO;
import com.prasun.BootCamp.DTOs.Product.ProductResponseDTO;
import com.prasun.BootCamp.DTOs.SellerDTOS.ResponseSellerDTO;
import com.prasun.BootCamp.ExceptionHandler.ResourceDoesNotExist;
import com.prasun.BootCamp.Model.Product.Product;
import com.prasun.BootCamp.Model.User;
import com.prasun.BootCamp.Model.Category.Category;
import com.prasun.BootCamp.Model.Category.CategoryMetadataField;
import com.prasun.BootCamp.Model.Customer;
import com.prasun.BootCamp.Model.Seller;
import com.prasun.BootCamp.Service.CategoryService;
import com.prasun.BootCamp.repo.CategoryRepo.CategoryMetadataFieldRepo;
import com.prasun.BootCamp.repo.CategoryRepo.CategoryRepo;
import com.prasun.BootCamp.repo.CustomerRepo;
import com.prasun.BootCamp.repo.ProductRepo.ProductRepository;
import com.prasun.BootCamp.repo.SellerRepo;
import com.prasun.BootCamp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    ProductRepository productRepository;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    CategoryMetadataFieldRepo categoryMetadataFieldRepo;

    @GetMapping(value = "/get/sellers/{offset}/{size}")
    public List<ResponseSellerDTO> getSellers(@PathVariable Long offset, @PathVariable Long size){

        List<ResponseSellerDTO> list_sellers = new ArrayList<>();
        Pageable page = PageRequest.of(Math.toIntExact(offset), Math.toIntExact(size),Sort.by("sid"));

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
            sellerres.setGst(seller.getGst());


            list_sellers.add(sellerres);




        }

       return list_sellers;

    }
    @GetMapping(value = "/get/customers/{offset}/{size}")
    public List<ResponseCustomerDTO> getCustomers(@PathVariable Long offset,@PathVariable Long size){

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
            customerres.setContact(user.getPhoneNumber());



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
public String addCategory(@Valid @RequestBody CategoryDTO categoryDTO){
    if(categoryDTO.getParentId()==0) {
        Category category = categoryService.addCategory(categoryDTO.getName());
        return "Your category is created with id :" + category.getId();

    } else{

        Category category = categoryService.addCategory(categoryDTO.getName(), categoryDTO.getParentId());
        return "Your category is created with id :" + category.getId();

    }
}

    @PostMapping("/metadata/add")
    public String addField(@Valid @RequestBody CategoryMetadataFieldDTO categoryMetadataFieldDTO){
        CategoryMetadataField categoryMetadataField = categoryService.addField(categoryMetadataFieldDTO.getName());
        return "Your metadata is created with id : " + categoryMetadataField.getId();


    }

    @GetMapping("/metadata/view")
    public List<CategoryMetadataField> getFieldList(){
        return categoryMetadataFieldRepo.findAll(Sort.by("id"));
    }


    @GetMapping("/category/view")
    public List<CategoryDTO> getCategoryList() {


        List<Category> categories = categoryRepo.findAll(Sort.by("id"));

        List<CategoryDTO> listNeeded = new ArrayList<>();


        for(Category category : categories){
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setParentId(category.getParentCategoryId());
            categoryDTO.setName(category.getName());
            listNeeded.add(categoryDTO);


        }

        return listNeeded;

    }

    @GetMapping("/category/{id}")
    public Map<String, List<CategoryDTO>> getCategoryById(@PathVariable Long id) {
        return categoryService.viewCategoryById(id);
    }


    @PutMapping("/category/update/{id}")
    public Category addCategory(@PathVariable Long id,@Valid @RequestBody CategoryDTO categoryDTO){
        return categoryService.updateCategory(id,categoryDTO.getName());
    }

    @PostMapping("category/metadatavalue/add")
    public CategoryFieldValueResDTO addMetadataValue(@Valid @RequestBody CategoryMetadataFieldValueDTO metadataFieldValueDTO){
        return categoryService.addMetadataValue(metadataFieldValueDTO.getCategoryId(), metadataFieldValueDTO.getFieldId(),metadataFieldValueDTO.getValues());
    }

    @PutMapping("category/metadatavalue/update")
    public CategoryFieldValueResDTO updateMetadataValue(@Valid @RequestBody CategoryMetadataFieldValueDTO metadataFieldValueDTO){
        return categoryService.updateMetadataValue(metadataFieldValueDTO.getCategoryId(), metadataFieldValueDTO.getFieldId(),metadataFieldValueDTO.getValues());
    }
    @PutMapping(value = "/deactivate/product/{id}")
    public ResponseEntity<String> deActivateProduct(@PathVariable Long id){

        Product product = productRepository.findById(id).orElse(null);
        if(product == null){
            return new ResponseEntity<String>("Product doesn't exist", HttpStatus.BAD_REQUEST);

        }
        if(!product.isActive()){
            return new ResponseEntity<String>("Product already deactivated",HttpStatus.BAD_REQUEST);

        }
        product.setActive(false);

        productRepository.save(product);

        User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new ResponseEntity<String>("Product Deactivated",HttpStatus.BAD_REQUEST);

    }
    @PutMapping(value = "/activate/product/{id}")
    public ResponseEntity<String> activateProduct(@PathVariable Long id){

        Product product = productRepository.findById(id).orElse(null);
        if(product == null){
            return new ResponseEntity<String>("Product doesn't exist", HttpStatus.BAD_REQUEST);

        }

        if(product.isActive()){
            return new ResponseEntity<String>("Product already active",HttpStatus.BAD_REQUEST);

        }

        product.setActive(true);

        productRepository.save(product);

        User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new ResponseEntity<String>("Product Activated",HttpStatus.BAD_REQUEST);





    }
    @GetMapping(value = "/view/product/{id}")
    public ProductResponseDTO viewProducts(@PathVariable Long id){

        Product product = productRepository.findById(id).orElse(null);

        if(product == null){
            throw new ResourceDoesNotExist("Product Doesn't exist with id : " + id);

        }

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setBrand(product.getBrand());
        productResponseDTO.setCancellable(product.isCancellable());
        productResponseDTO.setDeleted(product.isDeleted());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setActive(product.isActive());
        productResponseDTO.setCategoryName(product.getCategoryId().getName());
        productResponseDTO.setReturnable(product.isReturnable());


        return productResponseDTO;



    }

}
