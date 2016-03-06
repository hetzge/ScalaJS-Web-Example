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
   get(new UserApiRequest(apiRequest))
  }

  def getVideos(apiRequest: ApiRequest): ApiResult = {
    get(new VideoApiRequest(apiRequest))
  }

  def get(apiRequestQuery: ApiRequestQuery) = {
    val apiRequest = apiRequestQuery.apiRequest
    val query = apiRequestQuery.createQuery()
    println(query.toString())

    val results = query.fetch()
    val apiEntities =  packResult(apiRequest, results)
    val resultCount = apiEntities.length
    val totalCount = dslContext.fetchCount(query)

    ApiResult(apiEntities, resultCount, totalCount)
  }

  private def packResult(apiRequest: ApiRequest, results: Result[Record]) = {
    val apiEntities = ArrayBuffer[Map[ApiField, String]]()
    for (result <- results) {
      if(result.size() != apiRequest.fields.size){
        throw new IllegalStateException("The count of fetched fields have to be the same like the requested");
      }
      val values = for (i <- 0 until apiRequest.fields.length) yield (apiRequest.fields(i) -> result.getValue(i).toString)
      apiEntities += values.toMap
    }

    apiEntities.toSeq
  }

}

abstract class ApiRequestQuery(val table: Table[_],val apiRequest: ApiRequest) {

  def fields = apiRequest.fields.collect(field)

  def joins(query: SelectJoinStep[Record]) = apiRequest.fields.foldLeft(query)(join)

  def field: PartialFunction[ApiField, Field[_]]

  def join(query: SelectJoinStep[Record], apiField: ApiField): SelectJoinStep[Record]

  def afterJoin(query: SelectJoinStep[Record]): Select[Record]

  def createQuery() = afterJoin(joins(Server.dbService.dslContext.select(fields: _*).from(table)))

}

class VideoApiRequest(apiRequest: ApiRequest) extends ApiRequestQuery(Tables.USER, apiRequest) {

  override def field = {
    case VideoField.TITLE => Tables.VIDEO.TITLE
    case VideoField.OWNER_USERNAME => Tables.USER.USERNAME
  }

  override def join(query: SelectJoinStep[Record], apiField: ApiField) = {
    apiField match {
      case UserField.USERNAME => query.leftJoin(Tables.USER).on(Tables.VIDEO.USER.equal(Tables.USER.ID))
      case _ => query
    }
  }

  override def afterJoin(query: SelectJoinStep[Record]): Select[Record] = query

}

class UserApiRequest(apiRequest: ApiRequest) extends ApiRequestQuery(Tables.USER, apiRequest) {

  import org.jooq.impl.DSL._

  val abosAlias = Tables.USER_USER.as("abos")

  override def field = {
    case UserField.USERNAME => Tables.USER.USERNAME
    case UserField.EMAIL => Tables.USER.EMAIL
    case UserField.ABO_COUNT => count(abosAlias.USER_OBJECT)
  }

  override def join(query: SelectJoinStep[Record], apiField: ApiField) = {
    apiField match {
      case UserField.ABO_COUNT => query.leftJoin(abosAlias).on(Tables.USER.ID.equal(abosAlias.USER_OBJECT).and(abosAlias.VERB.equal("")))
      case _ => query
    }
  }

  override def afterJoin(query: SelectJoinStep[Record]): Select[Record] = {
    var resultQuery: Select[Record] = query;
    apiRequest.fields.foreach{ apiField =>
      resultQuery = apiField match {
        case UserField.ABO_COUNT => query.groupBy(Tables.USER.ID)
        case _ => query
      }
    }

    resultQuery
  }

}

object Blub extends App {
  val request = ApiRequest(Seq(UserField.USERNAME, UserField.ABO_COUNT))

  val results = Server.dbService.getUsers(request)

  for (entity <- results.entities) {
    println(entity.get(UserField.USERNAME))
    println(entity.get(UserField.ABO_COUNT))
  }

}




