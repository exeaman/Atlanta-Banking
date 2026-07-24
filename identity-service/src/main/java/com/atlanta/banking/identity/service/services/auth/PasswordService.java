package com.atlanta.banking.identity.service.services.auth;

import com.atlanta.banking.identity.service.dto.auth.ChangePasswordRequest;

public interface PasswordService {

    void changePassword(ChangePasswordRequest request);

}