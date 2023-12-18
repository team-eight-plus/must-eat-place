package com.go.musteatplace.search.domain.repository

import com.go.musteatplace.search.domain.Search
import org.springframework.data.jpa.repository.JpaRepository

interface SearchRepository: JpaRepository<Search, Long> {
//  fun findById(id: String): Search?
}
