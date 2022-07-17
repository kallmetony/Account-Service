package com.aaronr92.accountservice.entity;

import com.aaronr92.accountservice.util.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SortNatural;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.*;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(generator = "mySeq")
    @SequenceGenerator(name = "mySeq", sequenceName = "MY_SEQ", allocationSize = 1)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @NotBlank
    @Column
    private String name;

    @NotBlank
    @Column
    private String lastname;

    @NotBlank
    @Pattern(regexp = ".+(@acme.com)$")
    @Column
    private String email;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @SortNatural
    @Column
    private Set<Role> roles;

    @JsonIgnore
    @Column
    private boolean isNonLocked;

    @JsonIgnore
    @Column
    private int failedAttempts;

    @JsonIgnore
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return isNonLocked;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    public void grantAuthority(Role authority) {
        if (roles == null)
            roles = new TreeSet<>();
        roles.add(authority);
    }

    @JsonIgnore
    public void removeAuthority(Role authority) {
        roles.remove(authority);
    }

    @JsonIgnore
    public boolean isNonLocked() {
        return isNonLocked;
    }
}
