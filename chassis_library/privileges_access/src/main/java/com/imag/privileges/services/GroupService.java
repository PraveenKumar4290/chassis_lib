package com.imag.privileges.services;


import com.imag.privileges.dto.requests.GroupRequestDTO;
import com.imag.privileges.exceptions.UserDefinedException;
import com.imag.privileges.model.Group;
import com.imag.privileges.model.Privilege;
import com.imag.privileges.repository.GroupRepository;
import com.imag.privileges.repository.PrivilegeRepository;
import com.imag.privileges.repository.UserRepository;
import com.imag.privileges.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Transactional
    public Group addGroup(GroupRequestDTO groupRequestDto) {
        Group group = new Group();
        Optional<Group> optionalGroup = groupRepository.findByName(groupRequestDto.getName());
        if (!optionalGroup.isEmpty())
            throw new UserDefinedException("Already group exists.");
        group.setName(groupRequestDto.getName());
        group.setDescription(groupRequestDto.getDescription());
        group.setCreatedBy("admin");
        group.setCreatedAt(LocalDateTime.now());
        group.setUpdatedBy("admin");
        group.setUpdatedAt(LocalDateTime.now());
        group.setDeletedAt(null);
        return groupRepository.save(group);
    }

    @Transactional
    public Group updateGroup(Long id, GroupRequestDTO groupRequestDTO) {
        Optional<Group> optionalGroup = groupRepository.findByIdAndStatus(id, false);
        if (optionalGroup.isEmpty()) {
            throw new RuntimeException("Group not found");
        }
        Group group = optionalGroup.get();
        if (groupRequestDTO.getName() != null && !groupRequestDTO.getName().isEmpty())
            group.setName(groupRequestDTO.getName());
        if (groupRequestDTO.getDescription() != null && !groupRequestDTO.getDescription().isEmpty())
            group.setDescription(groupRequestDTO.getDescription());
        group.setUpdatedBy("admin");
        group.setUpdatedAt(LocalDateTime.now());
        return groupRepository.save(group);
    }

    @Transactional
    public String deleteGroup(Long id) {
        Optional<Group> optionalGroup = groupRepository.findByIdAndStatus(id, false);
        if (optionalGroup.isEmpty()) {
            throw new RuntimeException("Group not found");
        }
        Group group = optionalGroup.get();
        group.setDeletedAt(LocalDateTime.now());
        group.setStatus(true);
        groupRepository.save(group);
        return "Group deleted successfully...!";
    }

    public List<Group> activeGroups() {
        List<Group> groups = groupRepository.findByStatus(false);
        if (groups.isEmpty())
            throw new UserDefinedException("There are no groups.");
        return groups;
    }

    @Transactional
    public Group privilegesAssignToGroup(Long groupId, Long privilegeId) {
        Group group = groupRepository.findByIdAndStatus(groupId, false)
                .orElseThrow(() -> new UserDefinedException("Group not found"));
        System.out.println(group);
        Privilege privilege = privilegeRepository.findByIdAndStatus(privilegeId, false)
                .orElseThrow(() -> new UserDefinedException("Privilege not found"));
        System.out.println(privilege);
        List<Privilege> privileges = group.getPrivileges();
        System.out.println(privileges);
        for (Privilege checkPrivilege : privileges) {
            if (checkPrivilege.getId() == privilegeId)
                throw new UserDefinedException("Privilege already assigned");

            if (privilege.getParentId() == checkPrivilege.getId())
                throw new UserDefinedException("Privilege already assigned");

            if (checkPrivilege.getParentId() == privilegeId) {
                group.getPrivileges().remove(checkPrivilege);
                group.getPrivileges().add(privilege);
                return groupRepository.save(group);
            }
        }
        group.getPrivileges().add(privilege);
        return groupRepository.save(group);
    }

    public Set<Privilege> getPrivileges(Authentication authenticate) {
        Set<Privilege> privileges = new HashSet<>();
        CustomUserDetails userDetails = (CustomUserDetails) authenticate.getPrincipal();
        Collection<? extends GrantedAuthority> grantedAuthorities = userDetails.getAuthorities();
        Set<String> authorities = grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(String::new).collect(Collectors.toSet());
        for (String authority : authorities) {
            System.out.println(authority);
            if (groupRepository.existsByName(authority)) {
                Set<Privilege> privilegeSet = groupRepository.findByName(authority).get().getPrivileges()
                        .stream().collect(Collectors.toSet());
                privileges.addAll(privilegeSet);
            } else if (privilegeRepository.existsByName(authority)) {
                Privilege newPrivilege = privilegeRepository.findByName(authority).get();
                privileges.add(newPrivilege);
            } else
                continue;
        }
        return privileges;
    }
}
