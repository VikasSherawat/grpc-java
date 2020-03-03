package com.vikas.grpc.unary.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
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

        CalculatorServiceGrpc.CalculatorServiceFutureStub client = CalculatorServiceGrpc.newFutureStub(channel);
        int a= 17, b = 21;
        SumRequest request = SumRequest.newBuilder().setFirstNumber(a).setSecondNumber(b).build();
        Future<SumResponse> response = client.sum(request);
        System.out.println("Result is "+response.get().getResult());
    }
}
