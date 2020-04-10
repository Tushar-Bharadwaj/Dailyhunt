//package dailyhunt.internship;
//
//import dailyhunt.internship.entities.Role;
//import dailyhunt.internship.enums.RoleName;
//import dailyhunt.internship.repositories.RoleRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//public class DataLoader implements ApplicationRunner {
//
//    private RoleRepository roleRepository;
//
//    @Autowired
//    public DataLoader(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }
//
//    public void run(ApplicationArguments args) {
//        Optional<Role> roleUser = roleRepository.findByName(RoleName.ROLE_USER);
//        if(!roleUser.isPresent())
//            roleRepository.save(Role.builder()
//                .name(RoleName.ROLE_USER).build());
//
//        Optional<Role> roleModerator = roleRepository.findByName(RoleName.ROLE_MODERATOR);
//        if(!roleModerator.isPresent())
//        roleRepository.save(Role.builder()
//                .name(RoleName.ROLE_MODERATOR).build());
//
//        Optional<Role> roleAdmin = roleRepository.findByName(RoleName.ROLE_ADMIN);
//        if(!roleAdmin.isPresent())
//        roleRepository.save(Role.builder()
//                .name(RoleName.ROLE_ADMIN).build());
//    }
//}