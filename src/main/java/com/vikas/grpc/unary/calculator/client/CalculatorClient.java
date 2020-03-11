package com.vikas.grpc.unary.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.ComputeAverageRequest;
import com.proto.calculator.ComputeAverageResponse;
import com.proto.calculator.FindMaximumRequest;
import com.proto.calculator.FindMaximumResponse;
import com.proto.calculator.PrimeNumberDecompositionRequest;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import com.vikas.grpc.unary.calculator.server.CalculatorServer;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class CalculatorClient {
    ManagedChannel channel;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Starting Calculator Client");
        CalculatorClient main = new CalculatorClient();
        main.run();
    }
    private void run(){
        channel = ManagedChannelBuilder.forAddress("localhost", CalculatorServer.PORT)
                .usePlaintext()
                .build();

        //doUnaryCall();
        //doServerStreamingCall();
        //doClientStreamingCall(10);
        doBiDirectionalStreamingCall();
    }
    private void doBiDirectionalStreamingCall() {
        CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<FindMaximumRequest> request = asyncClient.findMaximum(new StreamObserver<FindMaximumResponse>() {
            @Override
            public void onNext(FindMaximumResponse value) {
                System.out.println("Got New Maximum from server: "+value.getMaximum());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                System.out.println("Server has finished sending data");
            }
        });

        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            request.onNext(FindMaximumRequest.newBuilder()
                    .setNumber(random.nextInt(1000))
                    .build());
        }

        try {
            request.onCompleted();
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    private void doClientStreamingCall(int n) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<ComputeAverageRequest> requestObserver = asyncClient.computeAverage(new StreamObserver<ComputeAverageResponse>() {
            @Override
            public void onNext(ComputeAverageResponse value) {
                System.out.println("Received average from server");
                System.out.println(value.getAverage());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                System.out.println("Server has completed sending data");
                latch.countDown();
            }
        });

        for (int i = 1; i <= n; i++) {
            requestObserver.onNext(ComputeAverageRequest.newBuilder()
                    .setNumber(i)
                    .build());
        }

        try {
            requestObserver.onCompleted();
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void doUnaryCall() {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub client = CalculatorServiceGrpc.newBlockingStub(channel);
        SumRequest request = SumRequest.newBuilder()
                .setFirstNumber(4)
                .setSecondNumber(3)
                .build();
        SumResponse response = client.sum(request);
        System.out.println("Sum response is "+response.getResult());
    }

    private void doServerStreamingCall() {
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
