package kr.co.direa.workspace.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class KeycloakServiceTest {

    @Autowired
    private KeycloakService keycloakService;

    @Test
    void testGetFullNameByUserId() {
        UUID userId = UUID.fromString("d4b2b0ad-2dd0-45b9-85f4-683e16289f59");
        String fullName = keycloakService.getFullNameByUserId(userId);
        System.out.println("Full name = " + fullName);
    }
}