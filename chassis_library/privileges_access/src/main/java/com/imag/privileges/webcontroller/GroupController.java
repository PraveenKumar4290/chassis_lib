package com.imag.privileges.webcontroller;

import com.imag.privileges.dto.requests.GroupRequestDTO;
import com.imag.privileges.exceptions.UserDefinedException;
import com.imag.privileges.services.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/group")
    public ResponseEntity<?> addGroup(@Valid @RequestBody GroupRequestDTO groupRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(groupService.addGroup(groupRequestDto));
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PutMapping("/group/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable Long id, @RequestBody GroupRequestDTO groupRequestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(groupService.updateGroup(id, groupRequestDTO));
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @DeleteMapping("/group/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(groupService.deleteGroup(id));
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/groups")
    public ResponseEntity<?> groups() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(groupService.activeGroups());
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PutMapping("/groups/{group-id}/privileges/{privilege-id}")
    public ResponseEntity<?> privilegesAssignToGroup(@PathVariable("group-id") Long groupId, @PathVariable("privilege-id") Long privilegeId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(groupService.privilegesAssignToGroup(groupId, privilegeId));
        } catch (UserDefinedException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
