package server

import scala.collection.mutable.ArrayBuffer
import org.jooq._
import server.generated.Tables
import shared._

object Server {
  val dbService = new DbService()
}

class DbService {

  import java.sql.DriverManager
  import org.jooq.impl.DSL
  import org.jooq.SQLDialect
  import collection.JavaConversions._

  val JDBC_URL = "jdbc:mariadb://localhost/test"
  val DB_USER = "root"
  val DB_PASSWORD = ""

  val connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)
  val dslContext = DSL.using(connection, SQLDialect.MARIADB)

  def getUsers(apiRequest: ApiRequest): ApiResult = {
    val joinedQuery = get(apiRequest, Tables.USER)
    val results = joinedQuery.fetch()
    val apiEntities = packResult(apiRequest, results)
    val resultCount = apiEntities.length
    val totalCount = dslContext.fetchCount(joinedQuery)

    ApiResult(apiEntities.toSeq, resultCount, totalCount)
  }

  def getVideos(apiRequest: ApiRequest): ApiResult = {
    val joinedQuery = get(apiRequest, Tables.VIDEO)
    val results = joinedQuery.fetch()
    val apiEntities = packResult(apiRequest, results)
    val resultCount = apiEntities.length
    val totalCount = dslContext.fetchCount(joinedQuery)

    ApiResult(apiEntities, resultCount, totalCount)
  }

  private def get(apiRequest: ApiRequest, table: TableLike[_]) = {
    val fields = apiRequest.fields.map(mapVideoField(_)).map(_._1)
    val joins = apiRequest.fields.map(mapVideoField(_)).map(_._2).filter(_ != null)

    val baseQuery = dslContext.select(fields: _*).from(table)

    val joinedQuery = joins.foldLeft(baseQuery) { (query: SelectJoinStep[Record], joinDescription: JoinDescription[Integer]) =>
      val table = joinDescription.table
      val onField = joinDescription.onField
      val equalField  = joinDescription.equalField
      val condition: Condition = onField.equal(equalField)

      query.join(table).on(condition)
    }

    println(joins.size)
    println(joinedQuery.toString)

    joinedQuery
  }

  private def packResult(apiRequest: ApiRequest, results: Result[Record]) = {
    val fields = apiRequest.fields.map(mapVideoField(_)).map(_._1)

    val apiEntities = ArrayBuffer[Map[ApiField, String]]()
    for (result <- results) {
      val values = for (i <- 0 until fields.length) yield (apiRequest.fields(i) -> result.getValue(fields(i)).toString)
      apiEntities += values.toMap
    }

    apiEntities
  }

  case class JoinDescription[T](table: TableLike[_], onField: Field[T], equalField: Field[T])

  // TODO extract objects
  // TODO implicit mapper

  def mapUserField(field: ApiField): (Field[_], JoinDescription[Integer]) = {
    field match {
      case EntityField.ID => (Tables.USER.ID, null)
      case UserField.USERNAME => (Tables.USER.USERNAME, null)
      case UserField.EMAIL => (Tables.USER.EMAIL, null)
      case _ => throw new IllegalStateException("Invalid api field " + field.name)
    }
  }

  def mapVideoField(field: ApiField): (Field[_], JoinDescription[Integer]) = {
    field match {
      case EntityField.ID => (Tables.VIDEO.ID, null)
      case VideoField.USERNAME => (Tables.USER.USERNAME, JoinDescription(Tables.USER, Tables.VIDEO.USER, Tables.USER.ID))
      case _ => throw new IllegalStateException("Invalid api field " + field.name)
    }
  }

}

object Blub extends App {
  val request = ApiRequest(Seq(VideoField.USERNAME))

  val results = Server.dbService.getVideos(request)

  for(entity <- results.entities){
    println(entity.get(VideoField.USERNAME))
  }

}




