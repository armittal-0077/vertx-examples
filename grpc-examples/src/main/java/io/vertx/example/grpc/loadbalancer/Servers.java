package io.vertx.example.grpc.loadbalancer;

import io.vertx.core.Vertx;
import io.vertx.example.grpc.helloworld.Server;

public class Servers {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Server(8080)).await();
    vertx.deployVerticle(new Server(8081)).await();
    vertx.deployVerticle(new Server(8082)).await();
    System.out.println("Servers started on port 8080/8081/8082");
  }
}
