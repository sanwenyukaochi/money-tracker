package com.secure.security.model.entity.relation;

import com.secure.security.model.entity.Role;
import com.secure.security.model.entity.User;
import com.secure.security.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user_role_rel",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_role", columnNames = "user_id, role_id")
        })
@Comment("用户角色关联表")
public class UserRole extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, 
            foreignKey = @ForeignKey(name = "fk_user_role_user_id"))
    @Comment("用户Id")
    private User user;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_role_role_id"))
    @Comment("角色Id")
    private Role role;
}
