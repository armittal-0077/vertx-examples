package io.vertx.example.grpc.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxConsumerServiceGrpcClient;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.launcher.application.VertxApplication;

import java.nio.charset.StandardCharsets;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  @Override
  public void start() {

    // Create the client
    GrpcClient client = GrpcClient.client(vertx);

    // Call the remote service
    client.request(SocketAddress.inetSocketAddress(8080, "localhost"), VertxConsumerServiceGrpcClient.StreamingOutputCall)
      .compose(request -> {
        request
          .end(Messages
            .StreamingOutputCallRequest
            .newBuilder()
            .build());
        return request.response().compose(response -> response
          .handler(msg -> System.out.println(new String(msg.getPayload().toByteArray(), StandardCharsets.UTF_8)))
          .end());
      }).onSuccess(resp -> {
        System.out.println("Response has ended.");
      });
  }
}
