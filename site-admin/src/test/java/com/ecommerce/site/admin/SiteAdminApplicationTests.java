package com.ecommerce.site.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SiteAdminApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testEncodePassword() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String password = "test";
        String encodedPassword = passwordEncoder.encode(password);

        System.out.println(encodedPassword);

        boolean matches = passwordEncoder.matches(password, encodedPassword);

        assertThat(matches).isTrue();
    }

}
