/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.example.metrics.dashboard;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.launcher.application.HookContext;
import io.vertx.launcher.application.VertxApplication;
import io.vertx.launcher.application.VertxApplicationHooks;

import java.util.Random;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Dashboard extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication application = new VertxApplication(new String[]{Dashboard.class.getName()}, new VertxApplicationHooks() {
      @Override
      public void beforeStartingVertx(HookContext context) {
        context.vertxOptions().setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true));
      }
    });
    application.launch();
  }

  @Override
  public void start() {

    MetricsService service = MetricsService.create(vertx);

    Router router = Router.router(vertx);

    // Allow outbound traffic to the news-feed address

    SockJSBridgeOptions options = new SockJSBridgeOptions()
      .addOutboundPermitted(new PermittedOptions().setAddress("metrics"));

    router.route("/eventbus*").subRouter(SockJSHandler.create(vertx).bridge(options));

    // Serve the static resources
    router.route().handler(StaticHandler.create());

    HttpServer httpServer = vertx.createHttpServer();
    httpServer.requestHandler(router).listen(8080);

    // Send a metrics events every second
    vertx.setPeriodic(1000, t -> {
      JsonObject metrics = service.getMetricsSnapshot(vertx.eventBus());
      if (metrics != null) {
        vertx.eventBus().publish("metrics", metrics);
      }
    });

    // Send some messages
    Random random = new Random();
    vertx.eventBus().consumer("whatever", msg -> {
      vertx.setTimer(10 + random.nextInt(50), id -> {
        vertx.eventBus().send("whatever", "hello");
      });
    });
    vertx.eventBus().send("whatever", "hello");
  }

}
