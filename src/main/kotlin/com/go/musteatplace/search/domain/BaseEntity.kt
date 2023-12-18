package com.go.musteatplace.search.domain

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@MappedSuperclass
class BaseEntity {
  @CreatedDate
  @Column(name = "created_at", insertable = false, updatable = false)
  lateinit var createdDateTime: LocalDateTime

  @LastModifiedDate
  @Column(name = "updated_at", insertable = false, updatable = false)
  lateinit var updatedDateTime: LocalDateTime
}
