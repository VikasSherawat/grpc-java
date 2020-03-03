package com.vikas.grpc.unary.greeting.client;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;

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
        GreetServiceGrpc.GreetServiceBlockingStub client = GreetServiceGrpc.newBlockingStub(channel);
        Greeting greeting =  Greeting.newBuilder()
                .setFirstName("Vikas")
                .setLastName("Kumar")
                .build();
        GreetRequest request = GreetRequest.newBuilder().setGreeting(greeting).build();
        GreetResponse response = client.greet(request);
        System.out.println("Result is "+response.getResult());
        System.out.println("Shutting down channel");
        channel.shutdown();
    }
}
