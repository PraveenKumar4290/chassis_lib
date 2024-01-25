package com.imag.privileges.security;

import com.imag.privileges.model.Group;
import com.imag.privileges.model.Privilege;
import com.imag.privileges.model.User;
import com.imag.privileges.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private UserRepository userRepository;

    private String username;
    private String password;
    private List<SimpleGrantedAuthority> grantedAuthorities;

    public CustomUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.grantedAuthorities = user.getGroups().stream()
                .map(Group::getName).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Set<Privilege> privilegesSet = new HashSet<>();
        List<Group> groups = user.getGroups().stream().toList();
        for (Group group : groups) {
            Set<Privilege> privileges = group.getPrivileges().stream().collect(Collectors.toSet());
            privilegesSet.addAll(privileges);
        }
        Set<Privilege> childPrivileges = getChildPrivileges(privilegesSet);

        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = childPrivileges.stream().map(Privilege::getCode)
                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        grantedAuthorities.addAll(simpleGrantedAuthorities);
        List<SimpleGrantedAuthority> collect = user.getPrivileges().stream().map(Privilege::getName)
                .map(String::new).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        for (SimpleGrantedAuthority authority : collect) {
            grantedAuthorities.add(authority);
        }
    }

    private Set<Privilege> getChildPrivileges(Set<Privilege> privilegesSet) {
        Set<Privilege> privilegeSet = new HashSet<>();
        for (Privilege privilege : privilegesSet) {
            if (privilege.getParentId() != null)
                privilegeSet.add(privilege);
            else continue;
        }
        return privilegeSet;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
