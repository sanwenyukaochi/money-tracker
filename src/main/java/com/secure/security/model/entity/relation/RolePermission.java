package com.secure.security.model.entity.relation;

import com.secure.security.model.entity.Permission;
import com.secure.security.model.entity.Role;
import com.secure.security.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_role_permission_rel",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_role_permission", columnNames = "role_id, permission_id")
        })
@Comment("角色权限关联表")
public class RolePermission extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_role_permission_role_id"))
    @Comment("角色Id")
    private Role role;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permission_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_role_permission_permission_id"))
    @Comment("权限Id")
    private Permission permission;
}
