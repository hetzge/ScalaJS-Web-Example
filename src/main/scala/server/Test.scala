package server

import collection.JavaConversions._
import java.sql.DriverManager
import org.jooq._
import org.jooq.impl._
import org.jooq.impl.DSL._
import server.generated._
import org.jooq.scala.Conversions._
import server.generated.tables.User

object Test extends App {
  val connection = DriverManager.getConnection("jdbc:mariadb://localhost/test", "root", "")
  val dslContext = DSL.using(connection, SQLDialect.MARIADB)

  val USER = User.USER;

  val results = dslContext.select(USER.ID) from USER where (USER.ID > 1) fetch

  for (result <- results) {
    println(result.value1())
  }
}
