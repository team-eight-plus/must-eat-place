package com.go.musteatplace.search.domain

import jakarta.persistence.*

@Entity
class Search(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val keywordId: Long? = null,

  @Column
  val keyword: String,

  @Column
  val title: String,

  @Column
  val link: String,

  @Column
  val category: String,

  @Column
  val description: String,

  @Column
  val telephone: String,

  @Column
  val address: String,

  @Column
  val roadAddress: String,

  @Column
  val mapx: String,

  @Column
  val mapy: String,
)
