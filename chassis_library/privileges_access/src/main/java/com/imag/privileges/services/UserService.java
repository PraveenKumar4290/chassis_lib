package com.imag.privileges.services;

import com.imag.privileges.dto.requests.UserRequestDTO;
import com.imag.privileges.exceptions.UserDefinedException;
import com.imag.privileges.model.Group;
import com.imag.privileges.model.Privilege;
import com.imag.privileges.model.User;
import com.imag.privileges.repository.GroupRepository;
import com.imag.privileges.repository.PrivilegeRepository;
import com.imag.privileges.repository.UserRepository;
import com.imag.privileges.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(CustomUserDetails::new).orElseThrow(() -> new UserDefinedException("User not found."));
    }

    public List<User> activeUsers() {
        List<User> users = userRepository.findByStatus(false);
        if (users.isEmpty())
            throw new UserDefinedException("There are no users.");
        return users;
    }

    @Transactional
    public User addUser(UserRequestDTO userRequestDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(userRequestDTO.getEmail().toLowerCase());
        if (!optionalUser.isEmpty())
            throw new UserDefinedException("Already email exits.");
        User newUser = new User(userRequestDTO.getUsername(), passwordEncoder.encode(userRequestDTO.getPassword()), userRequestDTO.getEmail().toLowerCase(), userRequestDTO.getPhoneNumber());
        return userRepository.save(newUser);
    }

    @Transactional
    public User updateRecord(Long userId, UserRequestDTO userRequestDTO) {
        Optional<User> optionalUser = userRepository.findByIdAndStatus(userId, false);
        if (optionalUser.isEmpty()) {
            throw new UserDefinedException("User not found..!");
        }
        User user = optionalUser.get();
        if (userRequestDTO.getUsername() != null && !userRequestDTO.getUsername().isEmpty())
            user.setUsername(userRequestDTO.getUsername());
        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty())
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        if (userRequestDTO.getEmail() != null && !userRequestDTO.getEmail().isEmpty())
            user.setEmail(userRequestDTO.getEmail());
        if (userRequestDTO.getPhoneNumber() != null && !userRequestDTO.getPhoneNumber().isEmpty())
            user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        return userRepository.save(user);
    }

    @Transactional
    public String deleteRecord(Long userId) {
        Optional<User> optionalUser = userRepository.findByIdAndStatus(userId, false);
        if (optionalUser.isEmpty())
            throw new UserDefinedException("user not found..!");
        User user = optionalUser.get();
        user.setStatus(true);
        userRepository.save(user);
        return "Deleted Successfully";
    }

    @Transactional
    public User groupsAssignToUser(Long userId, Long groupId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserDefinedException("User not found"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new UserDefinedException("Group not found"));
        user.getGroups().add(group);
        return userRepository.save(user);
    }

    @Transactional
    public User privilegesAssignToUser(Long userid, Long privilegeid) {
        User user = userRepository.findById(userid).orElseThrow(() -> new UserDefinedException("User not found"));
        Privilege privilege = privilegeRepository.findById(privilegeid).orElseThrow(() -> new UserDefinedException("Privilege not found"));
        user.getPrivileges().add(privilege);
        return userRepository.save(user);
    }
}