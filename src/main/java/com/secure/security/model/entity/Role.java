package com.secure.security.model.entity;

import com.secure.security.model.entity.base.BaseEntity;
import com.secure.security.model.entity.relation.RolePermission;
import com.secure.security.model.entity.relation.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "sys_role",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_role_code", columnNames = "code")
        })
@Comment("角色表")
@Schema(name = "Role", title = "角色对象", description = "系统角色实体，用于定义权限分组和访问控制")
public class Role extends BaseEntity {

    @Schema(title = "角色名称", example = "管理员", description = "角色中文名称，用于显示")
    @Column(name = "name", length = 10, nullable = false)
    @Comment("角色名称")
    private String name;

    @Schema(title = "角色编码", example = "ROLE_ADMIN", description = "角色唯一编码，用于权限标识，如 ROLE_USER、ROLE_ADMIN")
    @Column(name = "code", length = 10, nullable = false)
    @Comment("权限标识")
    private String code;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<UserRole> users = new ArrayList<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<RolePermission> permissions = new ArrayList<>();
}
