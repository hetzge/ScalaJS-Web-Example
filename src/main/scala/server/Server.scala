package server

object Server {
  val dbService = new DbService()
  val clientService = new ClientService()
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

trait ClientServiceApi {
  def doThing(i: Int, s: String): Seq[String]
}

class ClientServiceApiImpl extends ClientServiceApi {
  def doThing(i: Int, s: String): Seq[String] = Seq(i.toString(), s)
}

class ClientService {
  import autowire._
  import upickle.default._
  import scala.concurrent.ExecutionContext.Implicits.global

  object MyServer extends autowire.Server[String, Reader, Writer] {
    def write[Result: Writer](r: Result) = write(r)
    def read[Result: Reader](p: String) = read[Result](p)

    val routes = MyServer.route[ClientServiceApi](new ClientServiceApiImpl())
  }
}

