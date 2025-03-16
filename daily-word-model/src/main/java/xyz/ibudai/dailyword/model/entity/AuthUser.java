package xyz.ibudai.dailyword.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @author ibudai
 * @since 2023-01-31 14:31:28
 */
@Data
@TableName("auth_user")
public class AuthUser implements Serializable, UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @TableField("user_name")
    private String username;

    @TableField("password")
    private String password;

    @TableField("role")
    private String role;

    @TableField("authority")
    private String authority;

    @TableField("account_non_expired")
    private Boolean accountNonExpired;

    @TableField("account_non_locked")
    private Boolean accountNonLocked;

    @TableField("credentials_non_expired")
    private Boolean credentialsNonExpired;

    @TableField("is_enabled")
    private Boolean isEnabled;


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
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}

