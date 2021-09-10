package br.com.zup.academy.frete

import javax.persistence.*
import kotlin.random.Random

@Entity
@Table(name = "tb_frete")
class Frete(
    cep: String
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val cep = cep
    var valor : Double

    init {
        valor = calculaValor()
    }

    private fun calculaValor(): Double {
        return Random.nextDouble(10.0, 10000.0)
    }
}