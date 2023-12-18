package com.go.musteatplace.search.domain

import jakarta.persistence.*

@Entity
class Search(
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  val keywordId: Long?  = null,

  @Column
  val title: String,

  @Column
  val category: String,

  @Column
  val description: String,

  @Column
  val telephone: String,

  @Column
  val address: String,
)
