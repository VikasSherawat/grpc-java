package com.vikas.grpc.unary.calculator.server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class CalculatorServer {

    public static final int PORT = 50052;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Staring Calculator Service");
        Server server = ServerBuilder.forPort(PORT)
                .addService(new CalculatorServiceImpl())
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
