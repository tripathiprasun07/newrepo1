package com.prasun.BootCamp.events;

import com.prasun.BootCamp.Enums.Role_Enum;
import com.prasun.BootCamp.Model.ApplicationUser;
import com.prasun.BootCamp.Model.Role;
import com.prasun.BootCamp.repo.RoleRepo;
import com.prasun.BootCamp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.Objects;

@Component
public class Bootstrap implements ApplicationRunner {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(Objects.isNull(userRepo.findByEmail("admin@ttn.com"))) {
            Role role = new Role();
            role.setName(String.valueOf(Role_Enum.ROLE_ADMIN));
            Role role1 = new Role();
            role1.setName(String.valueOf(Role_Enum.ROLE_CUSTOMER));
            Role role2 = new Role();
            role2.setName(String.valueOf(Role_Enum.ROLE_SELLER));

            roleRepo.save(role);
            roleRepo.save(role1);
            roleRepo.save(role2);

            ApplicationUser user = new ApplicationUser();
            user.setEmail("admin@ttn.com");
            user.setFirstName("Prasun");
            user.setMiddleName("Raj");
            user.setLastName("Tripathi");
            user.setPhoneNumber("8808230621");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setRoles(roleRepo.findByName("ROLE_ADMIN"));
            user.setActive(true);
            userRepo.save(user);

        }
    }
}
