package br.com.zup.academy.frete

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface FreteRepository : JpaRepository<Frete, Long> {
}