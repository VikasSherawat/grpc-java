package com.vikas.grpc.unary.calculator.server;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.PrimeNumberDecompositionRequest;
import com.proto.calculator.PrimeNumberDecompositionResponse;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;

import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        int first = request.getFirstNumber(), second = request.getSecondNumber();
        int result = first + second;
        SumResponse response = SumResponse.newBuilder().setResult(result).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void primeNumberDecomposition(PrimeNumberDecompositionRequest request, StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
        int n = request.getNumber();
        int div = 2;
        // Print the number of 2s that divide n
        while(n>1){
            while(n%div==0){
                PrimeNumberDecompositionResponse response = PrimeNumberDecompositionResponse.newBuilder()
                        .setPrimeFactor(div)
                        .build();
                responseObserver.onNext(response);
                n/=div;
            }
                div = div>2?div+2:div+1;

        }

        responseObserver.onCompleted();
    }
}
