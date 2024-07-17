package io.vertx.example.core.http2.h2c;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpVersion;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  @Override
  public void start() throws Exception {

    HttpClientOptions options = new HttpClientOptions().setProtocolVersion(HttpVersion.HTTP_2);

    HttpClient client = vertx.createHttpClient(options);
    client.request(HttpMethod.GET, 8080, "localhost", "/")
      .compose(req -> req.send()
        .compose(resp -> {
          System.out.println("Got response " + resp.statusCode());
          return resp.body();
        })).onSuccess(body -> {
      System.out.println("Got data " + body.toString("ISO-8859-1"));
    }).onFailure(Throwable::printStackTrace);
  }
}
