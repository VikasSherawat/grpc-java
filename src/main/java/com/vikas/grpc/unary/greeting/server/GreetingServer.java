package com.vikas.grpc.unary.greeting.server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GreetingServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting gRPC Server--");
        Server server = ServerBuilder.forPort(50051)
                .addService(new GreetServiceImpl())
                .build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("Recieved Shutdown Signal");
            server.shutdown();
            System.out.println("Server shutdown successfully");
        }));

        server.awaitTermination();
    }
}
