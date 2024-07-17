package io.vertx.example.webclient.response.jsonobject;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
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

    client.get(8080, "localhost", "/")
      .as(BodyCodec.jsonObject())
      .send()
      .onComplete(ar -> {
        if (ar.succeeded()) {
          HttpResponse<JsonObject> response = ar.result();
          System.out.println("Got HTTP response body");
          System.out.println(response.body().encodePrettily());
        } else {
          ar.cause().printStackTrace();
        }
      });
  }
}
