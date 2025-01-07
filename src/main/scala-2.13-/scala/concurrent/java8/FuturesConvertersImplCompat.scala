/*
 * Scala (https://www.scala-lang.org)
 *
 * Copyright EPFL and Lightbend, Inc. dba Akka
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

package scala.concurrent.java8

import scala.concurrent.Future

// TODO: make this private[scala] when genjavadoc allows for that.
object FuturesConvertersImplCompat {
  def InternalCallbackExecutor = Future.InternalCallbackExecutor
}
