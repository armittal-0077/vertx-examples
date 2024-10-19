package io.vertx.example.grpc.producer;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxProducerServiceGrpcClient;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Client extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  private GrpcClient client;

  @Override
  public Future<?> start() {

    // Create the client
    client = GrpcClient.client(vertx);

    // Call the remote service
    return client.request(SocketAddress.inetSocketAddress(8080, "localhost"), VertxProducerServiceGrpcClient.StreamingInputCall)
      .compose(request -> {
        for (int i = 0; i < 10; i++) {
          request.write(Messages.StreamingInputCallRequest.newBuilder().build());
        }
        request.end();
        return request.response();
      })
      .onSuccess(response -> System.out.println("Server replied OK"));
  }
}
