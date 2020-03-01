package com.vikas.grpc.Client;

import com.proto.dummy.DummyServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreeetingClient {

    public static void main(String[] args) {
        System.out.println("gRPC client Started---");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",50051)
                .usePlaintext()
                .build();

        System.out.println("Creating  stub");
        // sync client
        DummyServiceGrpc.DummyServiceBlockingStub blockingStub = DummyServiceGrpc.newBlockingStub(channel);

        //async client
        //DummyServiceGrpc.DummyServiceFutureStub futureStub = DummyServiceGrpc.newFutureStub(channel);

        System.out.println("Shutting down channel");
        channel.shutdown();
    }
}
