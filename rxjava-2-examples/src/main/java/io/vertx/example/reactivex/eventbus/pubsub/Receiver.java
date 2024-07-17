package io.vertx.example.reactivex.eventbus.pubsub;

import io.vertx.launcher.application.VertxApplication;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Receiver extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Receiver.class.getName(), "-cluster"});
  }


  @Override
  public void start() throws Exception {

    EventBus eb = vertx.eventBus();

    eb.consumer("news-feed")
      .toFlowable()
      .subscribe(message -> System.out.println("Received news: " + message.body()));

    System.out.println("Ready!");
  }
}
