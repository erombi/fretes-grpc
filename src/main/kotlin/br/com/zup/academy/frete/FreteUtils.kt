package br.com.zup.academy.frete

import br.com.zup.academy.CalculaFreteRequest

fun CalculaFreteRequest.toModel() : Frete {
    return Frete(this.cep)
}