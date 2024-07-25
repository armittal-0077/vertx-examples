package io.vertx.example.grpc.empty;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.EmptyProtos;
import io.vertx.example.grpc.VertxEmptyPingPongServiceGrpc;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.client.GrpcClientChannel;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class ClientWithStub extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ClientWithStub.class.getName()});
  }

  @Override
  public void start() {

    // Create the channel
    GrpcClient client = GrpcClient.client(vertx);
    GrpcClientChannel channel = new GrpcClientChannel(client, SocketAddress.inetSocketAddress(8080, "localhost"));

    // Get a stub to use for interacting with the remote service
    VertxEmptyPingPongServiceGrpc.EmptyPingPongServiceVertxStub stub = VertxEmptyPingPongServiceGrpc.newVertxStub(channel);

    // Make a request
    EmptyProtos.Empty request = EmptyProtos.Empty.newBuilder().build();

    // Call the remote service
    stub.emptyCall(request).onComplete(ar -> {
      if (ar.succeeded()) {
        System.out.println("Got the server response.");
      } else {
        System.out.println("Could not reach server " + ar.cause().getMessage());
      }
    });
  }
}
