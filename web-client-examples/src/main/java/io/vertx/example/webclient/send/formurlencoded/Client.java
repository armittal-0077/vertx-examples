package io.vertx.example.webclient.send.formurlencoded;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  @Override
  public void start() throws Exception {

    WebClient client = WebClient.create(vertx);

    MultiMap form = MultiMap.caseInsensitiveMultiMap();
    form.add("firstName", "Dale");
    form.add("lastName", "Cooper");
    form.add("male", "true");

    client
      .post(8080, "localhost", "/")
      .sendForm(form)
      .onComplete(ar -> {
      if (ar.succeeded()) {
        HttpResponse<Buffer> response = ar.result();
        System.out.println("Got HTTP response with status " + response.statusCode());
      } else {
        ar.cause().printStackTrace();
      }
    });
  }
}
