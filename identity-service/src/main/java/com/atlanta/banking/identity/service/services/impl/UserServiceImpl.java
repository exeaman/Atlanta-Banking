package com.atlanta.banking.identity.service.services.impl;

import com.atlanta.banking.identity.service.dto.RegisterRequest;
import com.atlanta.banking.identity.service.dto.RegisterResponse;
import com.atlanta.banking.identity.service.entity.User;
import com.atlanta.banking.identity.service.repository.UserRepository;
import com.atlanta.banking.identity.service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final    PasswordEncoder encoder;
    private final   ModelMapper mapper;
    @Override
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        user.setRoles(registerRequest.getRoles());
        return mapper.map(userRepo.save(user), RegisterResponse.class);
    }
}
