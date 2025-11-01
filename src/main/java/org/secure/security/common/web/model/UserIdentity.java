package org.secure.security.common.web.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;
import org.secure.security.common.web.model.base.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "sys_user_identity",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_identity_user_provider", columnNames = {"user_id", "provider"})
        })
@Comment("用户第三方登录绑定表")
@Schema(name = "UserIdentity", title = "用户登录绑定对象", description = "存储用户绑定的第三方登录信息")
public class UserIdentity extends BaseEntity {

    @Schema(title = "用户ID", description = "对应 sys_user 表主键")
    @Column(name = "user_id", nullable = false)
    @Comment("用户ID")
    private Long userId;

    @Schema(title = "登录提供商", example = "GITHUB", description = "第三方登录来源，如 GITHUB、GOOGLE、WECHAT")
    @Column(name = "provider", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    @Comment("登录提供商")
    private AuthProvider provider;

    @Schema(title = "第三方用户唯一ID", example = "123456", description = "第三方平台返回的用户唯一ID，用于绑定")
    @Column(name = "provider_user_id", nullable = false, length = 64)
    @Comment("第三方用户唯一ID")
    private Long providerUserId;

    @Schema(title = "第三方用户名", example = "octocat", description = "第三方平台用户名，可选")
    @Column(name = "provider_username", length = 100)
    @Comment("第三方用户名")
    private String providerUsername;

    @Schema(title = "第三方邮箱", example = "octocat@github.com", description = "第三方平台邮箱，可选")
    @Column(name = "provider_email", length = 100)
    @Comment("第三方邮箱")
    private String providerEmail;

    public enum AuthProvider {
        EMAIL,
        GITHUB,
        GOOGLE,
        WECHAT
    }
}
