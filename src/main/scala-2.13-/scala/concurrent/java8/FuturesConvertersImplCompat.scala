package scala.concurrent.java8

import scala.concurrent.Future

// TODO: make this private[scala] when genjavadoc allows for that.
object FuturesConvertersImplCompat {
  def InternalCallbackExecutor = Future.InternalCallbackExecutor
}
