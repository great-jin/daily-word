package xyz.ibudai.dailyword.repository.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
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
@DatabaseTable(tableName = "auth_user")
public class AuthUser implements Serializable, UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true)
    private String id;

    @DatabaseField(columnName = "user_name")
    private String username;

    @DatabaseField(columnName = "password")
    private String password;

    @DatabaseField(columnName = "role")
    private String role;

    @DatabaseField(columnName = "authority")
    private String authority;

    @DatabaseField(columnName = "account_non_expired")
    private Integer accountNonExpired;

    @DatabaseField(columnName = "account_non_locked")
    private Integer accountNonLocked;

    @DatabaseField(columnName = "credentials_non_expired")
    private Integer credentialsNonExpired;

    @DatabaseField(columnName = "is_enabled")
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

