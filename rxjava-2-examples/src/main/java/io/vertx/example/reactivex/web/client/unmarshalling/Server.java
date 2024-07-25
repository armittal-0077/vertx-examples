package io.vertx.example.reactivex.web.client.unmarshalling;

import io.vertx.launcher.application.VertxApplication;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      req.response().putHeader("content-type", "application/json").end("{\"message\":\"Hello World\"}");
    });
    server.listen(8080);
  }
}
