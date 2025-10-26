package com.secure.security.model.entity.base;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Schema(name = "BaseEntity", title = "基础信息字段", description = "基础信息字段")
public abstract class BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @Comment("主键ID")
    @Schema(name = "id", title = "主键ID")
    private Long id;

}
