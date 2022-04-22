package com.prasun.BootCamp.Controller;

import com.prasun.BootCamp.DTOs.Address.AddressDTO;
import com.prasun.BootCamp.DTOs.Address.AddressResDTO;
import com.prasun.BootCamp.DTOs.Category.CategoryDTO;
import com.prasun.BootCamp.DTOs.CustomerDTOs.RequestCustomerDTO;
import com.prasun.BootCamp.DTOs.CustomerDTOs.ResponseCustomerDTO;
import com.prasun.BootCamp.DTOs.PasswordDTO;
import com.prasun.BootCamp.DTOs.Product.ProductResponseDTO;
import com.prasun.BootCamp.ExceptionHandler.CustomerAlreadyExist;
import com.prasun.BootCamp.ExceptionHandler.PasswordMismatchException;
import com.prasun.BootCamp.ExceptionHandler.ResourceDoesNotExist;
import com.prasun.BootCamp.Model.Address;
import com.prasun.BootCamp.Model.Category.Category;
import com.prasun.BootCamp.Model.Product.Product;
import com.prasun.BootCamp.Model.User;
import com.prasun.BootCamp.Model.Customer;
import com.prasun.BootCamp.repo.AddressRepo;
import com.prasun.BootCamp.repo.CategoryRepo.CategoryRepo;
import com.prasun.BootCamp.repo.CustomerRepo;
import com.prasun.BootCamp.repo.ProductRepo.ProductRepository;
import com.prasun.BootCamp.repo.RoleRepo;
import com.prasun.BootCamp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private RoleRepo roleRepo;
     @Autowired
    private PasswordEncoder encoder;
    @Autowired
    CategoryRepo categoryRepo;
     @Autowired
    AddressRepo addressRepo;
    @Autowired
    private DataSource dataSource;

    @PostMapping( "/register")
    public ResponseEntity<String> registerCustomer(@Valid  @RequestBody RequestCustomerDTO customerDTO) {

        if(userRepo.findByEmail(customerDTO.getEmail())!=null){
            throw new CustomerAlreadyExist("Account already exist with Email :- "+customerDTO.getEmail());
        }
        if (!customerDTO.getPassword().equals(customerDTO.getConfrimPassword())){
            throw new RuntimeException("Password Does Not Match");
        }
        User user = new User();
        user.setEmail(customerDTO.getEmail());
        user.setFirstName(customerDTO.getFirstName());
        user.setMiddleName(customerDTO.getMiddleName());
        user.setLastName(customerDTO.getLastName());
        user.setPassword(encoder.encode(customerDTO.getPassword()));

        user.setRoles(roleRepo.findByName("ROLE_CUSTOMER"));

        Customer customer = new Customer();
        customer.setUser(user);
        customer.setContact(customerDTO.getContact());
        customerRepo.save(customer);





        String str = "CUSTOMER ACCOUNT CREATED!!!";
        return new ResponseEntity<>(str, HttpStatus.CREATED);
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


    @PostMapping(value = "/logout")
    public ResponseEntity<String> logoutCustomer(@RequestParam String token, Principal principal){

        User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();




        JdbcTokenStore jdbcTokenStore = new JdbcTokenStore(dataSource);
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication)principal;
        OAuth2AccessToken accessToken = jdbcTokenStore.getAccessToken(oAuth2Authentication);

        if(!accessToken.getValue().equals(token)){
            return new ResponseEntity<String>("This token doesn't exist",HttpStatus.BAD_REQUEST);

        }

        if(accessToken.isExpired()){

            jdbcTokenStore.removeAccessToken(accessToken.getValue());
            jdbcTokenStore.removeRefreshToken(accessToken.getRefreshToken());


            return new ResponseEntity<String>("This token is expired already",HttpStatus.BAD_REQUEST);

        }

        jdbcTokenStore.removeAccessToken(accessToken.getValue());
        jdbcTokenStore.removeRefreshToken(accessToken.getRefreshToken());

        return new ResponseEntity<String>("User id logged out",HttpStatus.ACCEPTED);



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
            if(field.getName()=="id" || field.getName()=="user_id"){
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
    @GetMapping("/category/view")
    public List<CategoryDTO> getCategoryList(@RequestBody Optional<String> id) {




        List<Category> categories = categoryRepo.findAll(Sort.by("id"));

        List<CategoryDTO> list = new ArrayList<>();


        for(Category category : categories){
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setParentId(category.getParentCategoryId());
            categoryDTO.setName(category.getName());
            list.add(categoryDTO);


        }

        List<CategoryDTO> listNeededFinal = new ArrayList<>();



        if(!id.isPresent()){

            for(CategoryDTO categoryDTO : list){
                if(categoryDTO.getParentId() != 0)
                {


                }else{
                    listNeededFinal.add(categoryDTO);

                }



            }

        }else{

            for(CategoryDTO categoryDTO : list){
                if(categoryDTO.getParentId() == 0)
                {


                }else{

                    listNeededFinal.add(categoryDTO);


                }




            }

        }

        return listNeededFinal;

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