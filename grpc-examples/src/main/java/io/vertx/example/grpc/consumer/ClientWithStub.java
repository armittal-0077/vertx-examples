package io.vertx.example.grpc.consumer;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxConsumerServiceGrpcClient;
import io.vertx.grpc.client.GrpcClient;

import io.vertx.launcher.application.VertxApplication;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class ClientWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ClientWithStub.class.getName()});
  }

  private GrpcClient client;

  @Override
  public Future<?> start() {

    // Create the channel
    client = GrpcClient.client(vertx);

    // Get a stub to use for interacting with the remote service
    VertxConsumerServiceGrpcClient stub = new VertxConsumerServiceGrpcClient(client, SocketAddress.inetSocketAddress(8080, "localhost"));

    // Make a request
    Messages.StreamingOutputCallRequest request = Messages
      .StreamingOutputCallRequest
      .newBuilder()
      .build();

    // Call the remote service
    return stub
      .streamingOutputCall(request)
      .compose(response -> {
        response.handler(msg -> {
          System.out.println(new String(msg.getPayload().toByteArray(), StandardCharsets.UTF_8));
        });
        return response.last().map("Response has ended.");
      });
  }
}
