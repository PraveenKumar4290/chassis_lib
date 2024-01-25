package com.imag.privileges.webcontroller;

import com.imag.privileges.dto.requests.UserRequestDTO;
import com.imag.privileges.exceptions.UserDefinedException;
import com.imag.privileges.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/all")
    public ResponseEntity<?> allUsers() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.activeUsers());
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRecord(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userRequestDTO));
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PutMapping("/update/{userid}")
    public ResponseEntity<?> updateUser(@PathVariable("userid") Long userid, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateRecord(userid, userRequestDTO));
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @DeleteMapping("/delete/{userid}")
    public ResponseEntity<?> removeUser(@PathVariable("userid") Long userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.deleteRecord(userId));
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userService.deleteRecord(userId));
        }
    }

    @PutMapping("/user/{user-id}/group/{group-id}")
    public ResponseEntity groupsAssignToUser(@PathVariable("user-id") Long userId, @PathVariable("group-id") Long groupId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.groupsAssignToUser(userId, groupId));
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PutMapping("/user/{user-id}/privilege/{privilege-id}")
    public ResponseEntity PrivilegeAssignToUser(@PathVariable("user-id") Long userId, @PathVariable("privilege-id") Long privilegeId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.privilegesAssignToUser(userId, privilegeId));
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }


}