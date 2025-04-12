package xyz.ibudai.dailyword.model.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.ibudai.dailyword.model.enums.SysRole;

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

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_name")
    private String username;

    @TableField("password")
    private String password;

    @TableField("role")
    private String role;

    @TableField("authority")
    private String authority;

    @TableField("account_non_expired")
    private boolean accountNonExpired;

    @TableField("account_non_locked")
    private boolean accountNonLocked;

    @TableField("credentials_non_expired")
    private boolean credentialsNonExpired;

    @TableField("is_enabled")
    private boolean isEnabled;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String[] roles = role.split(",");
        // 遍历 roles，添加到授予认证类
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        // 返回到已经被授予认证的权限集合
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE.equals(this.accountNonExpired);
    }

    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE.equals(this.accountNonLocked);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE.equals(this.credentialsNonExpired);
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(this.isEnabled);
    }


    /**
     * Init user auth user.
     *
     * @return the auth user
     */
    public static AuthUser initUser() {
        AuthUser authUser = new AuthUser();
        authUser.setRole(SysRole.USER.name());
        authUser.setAuthority(SysRole.USER.name());
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setEnabled(true);
        return authUser;
    }
}

