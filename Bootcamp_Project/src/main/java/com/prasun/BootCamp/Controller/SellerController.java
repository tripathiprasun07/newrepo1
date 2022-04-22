package com.prasun.BootCamp.Controller;

import com.prasun.BootCamp.DTOs.Address.AddressResDTO;
import com.prasun.BootCamp.DTOs.Category.CategoryDTO;
import com.prasun.BootCamp.DTOs.PasswordDTO;
import com.prasun.BootCamp.DTOs.Product.ProductRequestDTO;
import com.prasun.BootCamp.DTOs.Product.UpdateProductRequestDTO;
import com.prasun.BootCamp.DTOs.SellerDTOS.RequestSellerDTO;
import com.prasun.BootCamp.DTOs.SellerDTOS.ResponseSellerDTO;
import com.prasun.BootCamp.ExceptionHandler.CustomerAlreadyExist;
import com.prasun.BootCamp.ExceptionHandler.PasswordMismatchException;
import com.prasun.BootCamp.Model.Address;
import com.prasun.BootCamp.Model.Category.Category;
import com.prasun.BootCamp.Model.Product.Product;
import com.prasun.BootCamp.Model.User;
import com.prasun.BootCamp.Model.Seller;
import com.prasun.BootCamp.repo.AddressRepo;
import com.prasun.BootCamp.repo.CategoryRepo.CategoryMetaDataFieldValueRepo;
import com.prasun.BootCamp.repo.CategoryRepo.CategoryMetadataFieldRepo;
import com.prasun.BootCamp.repo.CategoryRepo.CategoryRepo;
import com.prasun.BootCamp.repo.ProductRepo.ProductRepository;

import com.prasun.BootCamp.repo.RoleRepo;
import com.prasun.BootCamp.repo.SellerRepo;
import com.prasun.BootCamp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    CategoryRepo categoryRepo;



    @Autowired
    private CategoryMetadataFieldRepo categoryMetadataFieldRepo;

    @Autowired
    private CategoryMetaDataFieldValueRepo categoryMetaDataFieldValueRepo;

    @Autowired
    ProductRepository productRepository;

    @PostMapping("/register")
    public ResponseEntity<ResponseSellerDTO> createSeller(@Valid @RequestBody RequestSellerDTO sellerDTO){
        List<String> list = new ArrayList<>();
        list.add(sellerDTO.getEmail());
        list.add(sellerDTO.getCompanyName());
        list.add(sellerDTO.getGst());
        if(userRepo.findByEmail(sellerDTO.getEmail())!=null)
            throw new CustomerAlreadyExist("Account already exist with Email :- "+sellerDTO.getEmail());
        if(sellerRepo.findByCompanyName(sellerDTO.getCompanyName())!=null)
            throw new CustomerAlreadyExist("Account already exist with Company Name :- "+sellerDTO.getCompanyName());
        if(sellerRepo.findByGst(sellerDTO.getGst())!=null)
            throw new CustomerAlreadyExist("Account already exist with GST :- "+sellerDTO.getGst());
        if (!sellerDTO.getPassword().equals(sellerDTO.getConfirmPassword()))
            throw new PasswordMismatchException("Password Does Not Match");
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
        address.setCity(sellerDTO.getCity());
        address.setState(sellerDTO.getState());
        address.setCountry(sellerDTO.getCountry());
        address.setAddressLine(sellerDTO.getAddressLine());
        address.setZipCode(sellerDTO.getZipCode());

        user.setAddress(address);
        address.setUser(user);
        addressRepo.save(address);

        seller.setUser(user);
        sellerRepo.save(seller);

        ResponseSellerDTO sellerResDTO = new ResponseSellerDTO(user.getId(), user.getEmail(), user.getFirstName(), user.getMiddleName(), user.getLastName(),sellerDTO.getCompanyContact(),sellerDTO.getCompanyName(),sellerDTO.getGst());

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

        map.forEach((k, v) -> {
            Field field = org.springframework.util.ReflectionUtils.findField(User.class,(String)k);

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



    @PostMapping(value = "/add/product")
    public ResponseEntity<String> addProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO){




        User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Seller seller = sellerRepo.findByUserId(user.getId());

        Category category = categoryRepo.findById(productRequestDTO.getCategoryId()).orElse(null);


        if(category == null){
            return new ResponseEntity<String>("The category for this product doesn't exist please enter valid category id",HttpStatus.BAD_REQUEST);

        }

        if(category.getParentCategoryId()==null) {
            return new ResponseEntity<String>("You can't add a product to a parent Category", HttpStatus.BAD_REQUEST);

        }


        List<Product> sameBrandCategorySeller = productRepository.findSameBrandCategorySeller(productRequestDTO.getBrand(), productRequestDTO.getCategoryId(), seller.getId());

        if(sameBrandCategorySeller == null){
            return new ResponseEntity<String>("A product with same brand and category already exist",HttpStatus.BAD_REQUEST);

        }


        Product product = new Product();

        product.setName(productRequestDTO.getName());
        product.setSeller(seller);
        product.setActive(false);
        product.setBrand(productRequestDTO.getBrand());
        product.setCancellable(productRequestDTO.isCanBeCancelled());
        product.setReturnable(productRequestDTO.isCanBeReturned());
        product.setDescription(productRequestDTO.getDescription());
        product.setDeleted(false);



        product.setCategoryId(category);


        productRepository.save(product);


        return new ResponseEntity<String>("Product Successfully Added",HttpStatus.CREATED);
    }

    @GetMapping("/category/view")
    public List<CategoryDTO> getCategoryList() {


        List<Category> categories = categoryRepo.findAll(Sort.by("id"));

        List<CategoryDTO> list = new ArrayList<>();


        for(Category category : categories){
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setParentId(category.getParentCategoryId());
            categoryDTO.setName(category.getName());
            list.add(categoryDTO);


        }

        return list;

    }

    @PutMapping(value = "/update/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id,@Valid @RequestBody UpdateProductRequestDTO updateProductRequestDTO){

        Product product = productRepository.findById(id).orElse(null);
        User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(product == null){
            return new ResponseEntity<String>("Product doesn't exist with id : "+ id,HttpStatus.BAD_REQUEST);

        }

        List<Product> productWithSameNames = productRepository.findAllByName(updateProductRequestDTO.getName());

        if(!productWithSameNames.isEmpty()){
            return new ResponseEntity<String>("This name already exists : " + updateProductRequestDTO.getName(),HttpStatus.BAD_REQUEST);

        }



        product.setName(updateProductRequestDTO.getName());
        product.setDescription(updateProductRequestDTO.getDescription());
        product.setReturnable(updateProductRequestDTO.isReturnable());
        product.setCancellable(updateProductRequestDTO.isCancellable());

        productRepository.save(product);


        return new ResponseEntity<String>("Product Successfully Updated",HttpStatus.ACCEPTED);









    }
    @DeleteMapping(value = "/delete/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){

        Product product = productRepository.findById(id).orElse(null);
        if(product == null){
            return new ResponseEntity<String>("Product that you are looking to delete doesn't exist",HttpStatus.BAD_REQUEST);

        }
        User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();



        productRepository.delete(product);

        return new ResponseEntity<String>("Product deleted successfully",HttpStatus.ACCEPTED);


    }
}
