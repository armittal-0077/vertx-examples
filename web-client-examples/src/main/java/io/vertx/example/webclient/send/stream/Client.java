package io.vertx.example.webclient.send.stream;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.ext.web.client.HttpRequest;
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

    String filename = "io/vertx/example/webclient/send/stream/upload.txt";
    FileSystem fs = vertx.fileSystem();

    WebClient client = WebClient.create(vertx);

    fs.props(filename)
      .onComplete(ares -> {
      FileProps props = ares.result();
      System.out.println("props is " + props);
      long size = props.size();

      HttpRequest<Buffer> req = client.put(8080, "localhost", "/");
      req.putHeader("content-length", "" + size);

      fs.open(filename, new OpenOptions())
        .compose(ares2 -> req.sendStream(ares2))
        .onComplete(ar -> {
          if (ar.succeeded()) {
            HttpResponse<Buffer> response = ar.result();
            System.out.println("Got HTTP response with status " + response.statusCode());
          } else {
            ar.cause().printStackTrace();
          }
        });
    });
  }
}
