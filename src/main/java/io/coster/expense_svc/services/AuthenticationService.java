package io.coster.expense_svc.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Value("${user.service.host}")
    private String userServiceHost;

    @Value("${user.service.port}")
    private int userServicePort;

    public boolean isUserAndTokenValid(String userId, String token) {
        // TODO: add rest call to User service
        return true;
    }

    public boolean doesUserIdExist(String userId) {
        // TODO: add rest call to User service
        return true;
    }

}
