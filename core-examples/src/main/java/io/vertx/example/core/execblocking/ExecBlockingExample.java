package io.vertx.example.core.execblocking;

import io.vertx.core.AbstractVerticle;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class ExecBlockingExample extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ExecBlockingExample.class.getName()});
  }

  @Override
  public void start() throws Exception {

    vertx.createHttpServer().requestHandler(request -> {

      // Let's say we have to call a blocking API (e.g. JDBC) to execute a query for each
      // request. We can't do this directly or it will block the event loop
      // But you can do this using executeBlocking:

      vertx.<String>executeBlocking(() -> {

        // Do the blocking operation in here

        // Imagine this was a call to a blocking API to get the result
        try {
          Thread.sleep(500);
        } catch (Exception ignore) {
        }

        return "armadillos!";
      })
        .onSuccess(res -> request
          .response()
          .putHeader("content-type", "text/plain")
          .end(res))
        .onFailure(Throwable::printStackTrace);

    }).listen(8080);

  }
}
