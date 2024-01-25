package com.imag.privileges.webcontroller;

import com.imag.privileges.dto.requests.PrivilegeRequestDTO;
import com.imag.privileges.exceptions.UserDefinedException;
import com.imag.privileges.services.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class PrivilegesController {
    @Autowired
    private PrivilegeService privilegeService;

    @GetMapping("/privileges")
    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    public ResponseEntity<?> privileges() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(privilegeService.getPrivileges());
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping("/privilege")
    public ResponseEntity<?> addPrivilege(@RequestBody PrivilegeRequestDTO privilegeRequestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(privilegeService.addPrivileges(privilegeRequestDTO));
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PutMapping("/privilege/{id}")
    public ResponseEntity<?> updatePrivilege(@PathVariable("id") Long id, @RequestBody PrivilegeRequestDTO privilegeRequestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(privilegeService.updatePrivileges(id, privilegeRequestDTO));
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> deletePrivilege(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(privilegeService.deletePrivilege(id));
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}