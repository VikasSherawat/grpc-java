package com.vikas.grpc.unary.calculator.server;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.ComputeAverageRequest;
import com.proto.calculator.ComputeAverageResponse;
import com.proto.calculator.FindMaximumRequest;
import com.proto.calculator.FindMaximumResponse;
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

    @Override
    public StreamObserver<ComputeAverageRequest> computeAverage(StreamObserver<ComputeAverageResponse> responseObserver) {
        StreamObserver<ComputeAverageRequest> requestObserver = new StreamObserver<ComputeAverageRequest>() {
            int sum = 0;
            int count = 0;
            @Override
            public void onNext(ComputeAverageRequest value) {
                sum += value.getNumber();
                count++;
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Error Occured");
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                double average = (double) sum / count;
                responseObserver.onNext(ComputeAverageResponse.newBuilder()
                        .setAverage(average)
                        .build());

                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }

    @Override
    public StreamObserver<FindMaximumRequest> findMaximum(StreamObserver<FindMaximumResponse> responseObserver) {
        StreamObserver<FindMaximumRequest> requestObserver = new StreamObserver<FindMaximumRequest>() {
            int max = Integer.MIN_VALUE;
            @Override
            public void onNext(FindMaximumRequest value) {
                if(value.getNumber()>max) {
                    max = value.getNumber();
                    responseObserver.onNext(FindMaximumResponse.newBuilder()
                            .setMaximum(max)
                            .build());
                }
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Error Occured");
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(FindMaximumResponse.newBuilder()
                        .setMaximum(max)
                        .build());
                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }
}
