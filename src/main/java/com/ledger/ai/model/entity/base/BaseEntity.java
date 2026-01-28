package com.ledger.ai.model.entity.base;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Schema(name = "BaseEntity", title = "基础信息字段")
public abstract class BaseEntity {

    @Id
    @Schema(title = "主键ID")
    @Column(comment = "主键ID", name = "id", nullable = false)
    private Long id;

    @CreatedBy
    @Schema(title = "创建者")
    @Column(comment = "创建者", name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @CreatedDate
    @Schema(title = "创建时间")
    @Column(comment = "创建时间", name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @LastModifiedBy
    @Schema(title = "更新者")
    @Column(comment = "更新者", name = "updated_by", nullable = false)
    private Long updatedBy;

    @LastModifiedDate
    @Schema(title = "更新时间")
    @Column(comment = "更新时间", name = "updated_at", nullable = false)
    private Long updatedAt;

}
