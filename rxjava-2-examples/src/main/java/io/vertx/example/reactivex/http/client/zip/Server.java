package io.vertx.example.reactivex.http.client.zip;

import io.vertx.core.json.JsonObject;
import io.vertx.launcher.application.VertxApplication;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      req.response().putHeader("content-type", "application/json").end(new JsonObject().put("time", System.currentTimeMillis()).toString());
    });
    server.listen(8080);
  }
}
