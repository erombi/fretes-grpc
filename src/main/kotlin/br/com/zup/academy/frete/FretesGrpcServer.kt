package br.com.zup.academy.frete

import br.com.zup.academy.CalculaFreteRequest
import br.com.zup.academy.CalculaFreteResponse
import br.com.zup.academy.FreteServiceGrpc
import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class FretesGrpcServer(
    val repository: FreteRepository
) : FreteServiceGrpc.FreteServiceImplBase() {

    private val logger: Logger = LoggerFactory.getLogger(FretesGrpcServer::class.java)

    override fun calculaFrete(request: CalculaFreteRequest?, responseObserver: StreamObserver<CalculaFreteResponse>?) {

        request?.let {
            logger.info("Calculando frete da request: $request")

            val frete = request.toModel()

            repository.save(frete)

            val response = CalculaFreteResponse.newBuilder()
                .setCep(frete.cep)
                .setValor(frete.valor)
                .build()

            logger.info("Frete calculado: $frete")

            responseObserver?.onNext(response)
            responseObserver?.onCompleted()
        }

    }

}