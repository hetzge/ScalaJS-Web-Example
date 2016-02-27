package server

object Server {
  val dbService = new DbService()
}

class DbService {
  import java.sql.DriverManager
  import org.jooq.impl.DSL
  import org.jooq.SQLDialect

  val JDBC_URL = "jdbc:mariadb://localhost/test"
  val DB_USER = "root"
  val DB_PASSWORD = ""

  val connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)
  val dslContext = DSL.using(connection, SQLDialect.MARIADB)
}




