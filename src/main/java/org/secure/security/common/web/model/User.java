package org.secure.security.common.web.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;
import org.secure.security.common.web.model.base.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "sys_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_user_email", columnNames = "email")
        })
@Comment("用户表")
@Schema(name = "User", title = "用户对象", description = "系统用户实体，包含认证和状态信息")
public class User extends BaseEntity {

    @Schema(title = "用户名", example = "alice", description = "系统登录名，唯一标识用户")
    @Column(name = "username", nullable = false, length = 20)
    @Comment("用户名")
    private String username;

    @Schema(title = "密码", description = "加密后的密码，不会在接口返回中显示", accessMode = Schema.AccessMode.WRITE_ONLY)
    @Column(name = "password", nullable = false, length = 120)
    @Comment("用户密码")
    private String password;

    @Schema(title = "邮箱", example = "alice@example.com", description = "用户绑定邮箱，用于找回密码或登录")
    @Column(name = "email", nullable = false, length = 50)
    @Comment("邮箱")
    private String email;

    @Schema(title = "手机号", example = "12345678910", description = "用户绑定手机号，用于找回密码或登录")
    @Column(name = "phone", nullable = false, length = 50)
    @Comment("手机号")
    private String phone;

    @Schema(title = "账户是否未锁定", description = "true 表示正常，false 表示锁定")
    @Column(name = "account_non_locked", nullable = false)
    @Comment("账户是否未锁定（true=正常，false=锁定）")
    private Boolean accountNonLocked = true;

    @Schema(title = "账户是否未过期", description = "true 表示有效，false 表示过期")
    @Column(name = "account_non_expired", nullable = false)
    @Comment("账户是否未过期（true=有效，false=过期）")
    private Boolean accountNonExpired = true;

    @Schema(title = "密码是否未过期", description = "true 表示有效，false 表示密码已过期")
    @Column(name = "credentials_non_expired", nullable = false)
    @Comment("密码是否未过期（true=有效，false=已过期）")
    private Boolean credentialsNonExpired = true;

    @Schema(title = "是否启用", description = "true 表示账户启用，false 表示禁用")
    @Column(name = "enabled", nullable = false)
    @Comment("状态（true=启用，false=禁用）")
    private Boolean enabled = true;

    @Schema(title = "双因素认证密钥", example = "JBOSSWS3DPKG3PXP", description = "TOTP Secret，用于Google Authenticator等")
    @Column(name = "two_factor_secret", nullable = true, length = 64)
    @Comment("双因素认证密钥（TOTP Secret，用于Google Authenticator等）")
    private String twoFactorSecret;

    @Schema(title = "是否启用双因素认证", description = "true 表示启用，false 表示未启用")
    @Column(name = "two_factor_enabled", nullable = false)
    @Comment("是否启用双因素认证（true=启用，false=未启用）")
    private Boolean twoFactorEnabled = false;

    @Schema(title = "注册方式", description = "用户注册来源，如 EMAIL、GITHUB、GOOGLE、WECHAT、SYSTEM 等")
    @Column(name = "sign_up_method", nullable = false, length = 10)
    @Comment("注册方式（EMAIL/GITHUB/GOOGLE/WECHAT/SYSTEM等）")
    private String signUpMethod;

}
