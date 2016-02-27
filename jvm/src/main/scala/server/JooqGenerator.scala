package server

import org.jooq.util.jaxb._
import org.jooq.util.GenerationTool

object JooqGenerator extends App{

  val configuration = new Configuration()
    .withJdbc(new Jdbc()
      .withDriver("org.mariadb.jdbc.Driver")
      .withUrl(Server.dbService.JDBC_URL)
      .withUser(Server.dbService.DB_USER)
      .withPassword(Server.dbService.DB_PASSWORD))
    .withGenerator(new Generator()
      .withDatabase(new Database()
        .withName("org.jooq.util.mariadb.MariaDBDatabase")
        .withIncludes(".*")
        .withExcludes("")
        .withInputSchema("test"))
      .withTarget(new Target()
        .withPackageName("server.generated")
        .withDirectory("jvm/src/main/scala")))

  GenerationTool.generate(configuration)
}