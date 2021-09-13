package br.com.zup.academy.frete

import br.com.zup.academy.CalculaFreteRequest
import br.com.zup.academy.CalculaFreteResponse
import br.com.zup.academy.ErroDetails
import br.com.zup.academy.FreteServiceGrpc
import com.google.protobuf.Any
import com.google.rpc.Code
import io.grpc.Status
import io.grpc.protobuf.StatusProto
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

            if (it.cep == null || it.cep.isBlank()) {
                val error = Status.INVALID_ARGUMENT.withDescription("CEP não informado !")
                    .asRuntimeException()

                responseObserver?.onError(error)
                return
            }

            if (!it.cep.matches("[0-9]{5}-[\\d]{3}".toRegex())) {
                val error = Status.INVALID_ARGUMENT.withDescription("CEP informado não é válido !")
                    .augmentDescription("formato esperado: 00000-000")
                    .asRuntimeException()

                responseObserver?.onError(error)
                return
            }

            if (it.cep.endsWith("333")) {
                val statusProto = com.google.rpc.Status.newBuilder()
                    .setMessage("Usuário não pode acessar o recurso !")
                    .setCode(Code.PERMISSION_DENIED.number)
                    .addDetails(Any.pack(ErroDetails.newBuilder()
                        .setCode(401)
                        .setMessage("Token expirado !")
                        .build()))
                    .build()

                val status = StatusProto.toStatusRuntimeException(statusProto)
                responseObserver?.onError(status)
            }

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