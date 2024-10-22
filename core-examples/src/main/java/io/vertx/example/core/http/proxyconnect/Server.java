package io.vertx.example.core.http.proxyconnect;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.SelfSignedCertificate;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public Future<?> start() throws Exception {

    SelfSignedCertificate certificate = SelfSignedCertificate.create();
    HttpServerOptions serverOptions = new HttpServerOptions()
      .setSsl(true)
      .setKeyCertOptions(certificate.keyCertOptions());

    return vertx.createHttpServer(serverOptions)
      .requestHandler(req -> {

        System.out.println("Got request " + req.uri());

        for (String name : req.headers().names()) {
          System.out.println(name + ": " + req.headers().get(name));
        }

        req.handler(data -> System.out.println("Got data " + data.toString("ISO-8859-1")));

        req.endHandler(v -> {
          // Now send back a response
          req.response().setChunked(true);

          for (int i = 0; i < 10; i++) {
            req.response().write("server-data-chunk-" + i);
          }

          req.response().end();
        });
      }).listen(8282);
  }
}
