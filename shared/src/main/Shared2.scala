package shared

object SharedHelper{
  //  implicit def mapToTransferEntity(map: Map[ApiField, String]) = new TransferEntity(map)
}

case class ApiField(val key: String)

object EntityField {
  val ID = ApiField("id")
}

object UserField {
  val USERNAME = ApiField("username")
  val ABO_COUNT = ApiField("abo_count")
  val EMAIL = ApiField("email")
}

object VideoField {
  val TITLE = ApiField("title")
  val OWNER_USERNAME = ApiField("owner_username")
}

trait UserView {
  def username: String
}

class VideoOwnerView(val map: Map[ApiField, String]) extends TransferEntity with UserView{
  override def username = get(VideoField.OWNER_USERNAME)
}

trait TransferEntity {
  val map: Map[ApiField, String]

  def get(field: ApiField): String = map.get(field).getOrElse("")

  def getInt(field: ApiField): Int = get(field).toInt

  def getDouble(field: ApiField): Double = get(field).toInt

  def getBoolean(field: ApiField): Boolean = get(field).toBoolean
}

case class Paging(val page: Int, val pageSize: Int)

case class Order(val field: ApiField, desc: Boolean)

case class ApiRequest(val fields: Seq[ApiField] = Seq(EntityField.ID), val paging: Paging = Paging(1, 10), val orders: Seq[Order] = Seq())

case class ApiResult(val entities: Seq[Map[ApiField, String]], val resultCount: Int, val totalCount: Int)