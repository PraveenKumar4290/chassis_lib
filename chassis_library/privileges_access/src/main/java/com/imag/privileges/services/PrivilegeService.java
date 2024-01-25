package com.imag.privileges.services;

import com.imag.privileges.dto.requests.PrivilegeRequestDTO;
import com.imag.privileges.exceptions.UserDefinedException;
import com.imag.privileges.model.Privilege;
import com.imag.privileges.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PrivilegeService {
    @Autowired
    private PrivilegeRepository privilegeRepository;

    public List<Privilege> getPrivileges() {
        List<Privilege> privileges = privilegeRepository.findByStatus(false);
        if (privileges.isEmpty())
            throw new RuntimeException("There are no Privileges.");
        return get(privileges);
    }

    private List<Privilege> get(List<Privilege> privileges) {
        List<Privilege> privilegeList = new ArrayList<>();
        for (Privilege privilege : privileges) {
            if (privilege.getParentId() != null)
                continue;
            else
                privilegeList.add(privilege);
        }
        return privilegeList;
    }

    @Transactional
    public Privilege addPrivileges(PrivilegeRequestDTO privilegeRequestDTO) {
        Optional<Privilege> optionalPrivilege = privilegeRepository.findByName(privilegeRequestDTO.getName());
        if (!optionalPrivilege.isEmpty())
            throw new UserDefinedException("Already privilege exits.");
        Privilege privilege = new Privilege(privilegeRequestDTO.getName(), privilegeRequestDTO.getDescription(), privilegeRequestDTO.getParentId(), privilegeRequestDTO.getCode());
        return privilegeRepository.save(privilege);
    }

    @Transactional
    public Privilege updatePrivileges(Long id, PrivilegeRequestDTO privilegeRequestDTO) {
        Optional<Privilege> optionalPrivilege = privilegeRepository.findByIdAndStatus(id, false);
        if (optionalPrivilege.isEmpty())
            throw new UserDefinedException("Privilege not found.");
        Privilege privilege = optionalPrivilege.get();
        if (privilegeRequestDTO.getName() != null && !privilegeRequestDTO.getName().isEmpty())
            privilege.setName(privilegeRequestDTO.getName());
        if (privilegeRequestDTO.getDescription() != null && !privilegeRequestDTO.getDescription().isEmpty())
            privilege.setDescription(privilegeRequestDTO.getDescription());
        if (privilegeRequestDTO.getParentId() != null && privilegeRequestDTO.getParentId() != 0)
            privilege.setParentId(privilegeRequestDTO.getParentId());
        if (privilegeRequestDTO.getCode() != null && !privilegeRequestDTO.getCode().isEmpty())
            privilege.setCode(privilegeRequestDTO.getCode());
        return privilegeRepository.save(privilege);
    }

    @Transactional
    public String deletePrivilege(Long id) {
        Optional<Privilege> optionalPrivilege = privilegeRepository.findByIdAndStatus(id, false);
        if (optionalPrivilege.isEmpty())
            throw new UserDefinedException("Privilege not found.");
        Privilege privilege = optionalPrivilege.get();
        privilege.setStatus(true);
        privilegeRepository.save(privilege);
        return "Deleted successfully.";
    }


}