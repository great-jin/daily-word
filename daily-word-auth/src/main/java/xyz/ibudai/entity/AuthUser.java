package xyz.ibudai.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * (TbUser)实体类
 *
 * @author makejava
 * @since 2023-01-31 14:31:28
 */
@Data
public class AuthUser implements Serializable, UserDetails {

    private static final long serialVersionUID = 213034465413641992L;

    private String id;

    private String username;

    private String password;

    private String role;

    private String authority;

    private Integer accountNonExpired;

    private Integer accountNonLocked;

    private Integer credentialsNonExpired;

    private Integer isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 获取用户所有权限
        String[] roles = role.split(",");
        // 遍历 roles，取出每一个权限进行认证，添加到简单的授予认证类
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        // 返回到已经被授予认证的权限集合, 这里面的角色所拥有的权限都已经被 spring security 所知道
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired != null && this.accountNonExpired == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked != null && this.accountNonLocked == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired != null && this.credentialsNonExpired == 1;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled != null && this.isEnabled == 1;
    }
}

