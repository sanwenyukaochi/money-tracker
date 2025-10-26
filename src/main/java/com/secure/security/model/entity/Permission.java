package com.secure.security.model.entity;

import com.secure.security.model.entity.base.BaseEntity;
import com.secure.security.model.entity.relation.RolePermission;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Table(name = "sys_permission",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_permission_code", columnNames = "code")
        })
@Comment("权限表")
@Schema(name = "Permission", title = "权限对象", description = "系统权限定义表，用于接口访问控制")
public class Permission extends BaseEntity {

    @Schema(title = "父级ID", description = "父权限ID，用于构建权限树结构", example = "0")
    @Column(name = "parent_id", nullable = false)
    @Comment("父级ID")
    private Long parentId;

    @Schema(title = "权限名称", description = "权限中文名称，用于界面显示", example = "创建用户")
    @Column(name = "name", length = 10, nullable = false)
    @Comment("权限名称")
    private String name;

    @Schema(title = "权限编码", description = "权限唯一标识（如 USER_CREATE、ROLE_DELETE 等）", example = "USER_CREATE")
    @Column(name = "code", length = 10, nullable = false)
    @Comment("权限编码")
    private String code;

    @Schema(title = "请求路径", description = "接口的请求路径（可用于精细化权限控制）", example = "/api/users")
    @Column(name = "request_url", length = 200)
    @Comment("请求路径")
    private String requestUrl;

    @Schema(title = "请求方法", description = "HTTP 方法（GET/POST/PUT/DELETE等）", example = "POST")
    @Column(name = "request_method", length = 10)
    @Enumerated(EnumType.STRING)
    @Comment("请求方法")
    private HttpMethod requestMethod;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<RolePermission> roles = new ArrayList<>();


    public enum HttpMethod {
        GET, POST, PUT, DELETE, PATCH, OPTIONS, HEAD
    }

}
