package com.vikas.grpc.unary.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.PrimeNumberDecompositionRequest;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import com.vikas.grpc.unary.calculator.server.CalculatorServer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Starting Calculator Client");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", CalculatorServer.PORT)
                .usePlaintext()
                .build();

        CalculatorServiceGrpc.CalculatorServiceBlockingStub client = CalculatorServiceGrpc.newBlockingStub(channel);

        PrimeNumberDecompositionRequest request = PrimeNumberDecompositionRequest.newBuilder()
                .setNumber(567890304)
                .build();
        client.primeNumberDecomposition(request)
                .forEachRemaining(r->
                        System.out.println(r.getPrimeFactor())
                );
    }
}
