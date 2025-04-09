package com.ecommerce.configuration;

import com.ecommerce.entity.User;
import com.ecommerce.entity.UserInfo;
import com.ecommerce.enums.Role;
import com.ecommerce.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public ApplicationInitConfig(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")
    public ApplicationRunner applicationRunner() {
        return args -> {
            if (userRepository.findByLogin("admin@gmail.com").isEmpty()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setName("admin");
                userInfo.setSurname("admin");
                userInfo.setPhone("0865761892");

                User user = new User();
                user.setLogin("admin@gmail.com");
                user.setEmail("admin@gmail.com");
                user.setPassword(passwordEncoder.encode("admin"));
                user.setRole(Role.ADMIN);
                user.setUserInfo(userInfo);
                userInfo.setUser(user);
                userRepository.save(user);
            }
        };
    }
}
