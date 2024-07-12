package io.vertx.example.reactivex.database.sqlclient;

import io.reactivex.Maybe;
import io.reactivex.functions.Function;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.jdbcclient.JDBCConnectOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.jdbcclient.JDBCPool;
import io.vertx.reactivex.sqlclient.*;
import io.vertx.sqlclient.PoolOptions;

import java.util.Arrays;

/*
 * @author <a href="mailto:emad.albloushi@gmail.com">Emad Alblueshi</a>
 */
public class Transaction extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Transaction.class.getName());
  }

  @Override
  public void start() throws Exception {

    String sql = "CREATE TABLE colors (" +
      "id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY, " +
      "name VARCHAR(255), " +
      "datetime TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL)";

    Pool pool = JDBCPool.pool(vertx, new JDBCConnectOptions().setJdbcUrl("jdbc:hsqldb:mem:test?shutdown=true"), new PoolOptions());

    // Connect to the database
    pool.rxWithTransaction((Function<SqlConnection, Maybe<RowSet<Row>>>) client -> client
      // Create table
      .query(sql).rxExecute()
      // Insert colors
      .flatMap(r -> client
        .preparedQuery("INSERT INTO colors (name) VALUES (?)")
        .rxExecuteBatch(Arrays.asList(Tuple.of("BLACK"), Tuple.of("PURPLE"))))
      // Get colors if all succeeded
      .flatMap(r -> client.query("SELECT * FROM colors").rxExecute())
      .toMaybe())// Subscribe to get the final result
      .subscribe(rowSet -> {
        System.out.println("Results:");
        rowSet.forEach(row -> {
          System.out.println(row.toJson());
        });
      }, Throwable::printStackTrace);
  }
}
