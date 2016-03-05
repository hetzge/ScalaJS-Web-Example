package client

import scala.collection.mutable.ListBuffer

object Model{
  def apply[T](value: T):Model[T] = new Model(value);
}
class Model[T](var value:T){
  val watcher: ListBuffer[(T) => Unit] = ListBuffer()

  def apply(t:T) = {
    value = t
    watcher.foreach( (listener: (T) => Unit) => listener(t) )
  }

  def get():T = value

  def watch(listener: T => Unit) = watcher += listener
}