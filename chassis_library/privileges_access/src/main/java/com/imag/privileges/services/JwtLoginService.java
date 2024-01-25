package com.imag.privileges.services;

import com.imag.privileges.dto.requests.LoginRequest;
import com.imag.privileges.dto.requests.RefreshTokenRequest;
import com.imag.privileges.dto.responses.AuthenticationResponse;
import com.imag.privileges.exceptions.UserDefinedException;
import com.imag.privileges.model.Group;
import com.imag.privileges.model.Privilege;
import com.imag.privileges.model.User;
import com.imag.privileges.repository.GroupRepository;
import com.imag.privileges.repository.PrivilegeRepository;
import com.imag.privileges.repository.UserRepository;
import com.imag.privileges.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtLoginService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private GroupRepository groupRepository;

    public AuthenticationResponse tokenGenerate(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            Set<Privilege> privileges = groupService.getPrivileges(authenticate);
            String token = jwtService.generateToken(loginRequest.getUsername());
            String refreshToken = jwtService.generateRefreshToken(authenticate);
            AuthenticationResponse authResponse = AuthenticationResponse.builder()
                    .privileges(privileges).token(token).refreshToken(refreshToken).build();
            return authResponse;
        } else
            throw new UserDefinedException("Invalid credentials..");
    }

    public AuthenticationResponse refreshTokenGenerate(RefreshTokenRequest tokenRequest) {
        String token = jwtService.refreshTokenGenerate(tokenRequest);
        Set<Privilege> privileges = new HashSet<>();
        String username = jwtService.extractUsername(tokenRequest.getRefreshToken());
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserDefinedException("Invalid user"));
        Set<String> groupNames = user.getGroups().stream().map(Group::getName).map(String::new).collect(Collectors.toSet());
        for (String groupName : groupNames) {
            System.out.println(groupName);
            Set<Privilege> privilegeSet = groupRepository.findByName(groupName).get().getPrivileges()
                    .stream().collect(Collectors.toSet());
            privileges.addAll(privilegeSet);
        }
        AuthenticationResponse authResponse = AuthenticationResponse.builder().privileges(privileges)
                .token(token).refreshToken(tokenRequest.getRefreshToken()).build();
        return authResponse;
    }
}
