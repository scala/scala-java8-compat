package scala.concurrent.java8

import scala.concurrent.ExecutionContext

// TODO: make this private[scala] when genjavadoc allows for that.
object FuturesConvertersImplCompat {
  def InternalCallbackExecutor = ExecutionContext.parasitic
}
