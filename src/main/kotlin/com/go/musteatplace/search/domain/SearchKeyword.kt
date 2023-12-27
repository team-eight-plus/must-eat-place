package com.go.musteatplace.search.domain

import jakarta.persistence.*

@Entity
@Table(name = "tb_rank")
class SearchKeyword(

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(nullable = false, unique = true)
  val keyword: String,

  @Column(nullable = false)
  var count: Long = 0

)
