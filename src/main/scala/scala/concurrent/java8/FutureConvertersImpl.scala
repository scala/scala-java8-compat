/*
 * Scala (https://www.scala-lang.org)
 *
 * Copyright EPFL and Lightbend, Inc.
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

package scala.concurrent.java8

// Located in this package to access private[concurrent] members

import java.util.concurrent._
import java.util.function.{BiConsumer, BiFunction, Consumer, Function => JF}

import scala.concurrent.Future
import scala.concurrent.impl.Promise.DefaultPromise
import scala.util.{Failure, Success, Try}

// TODO: make this private[scala] when genjavadoc allows for that.
object FuturesConvertersImpl {
  class CF[T](val wrapped: Future[T]) extends CompletableFuture[T] with (Try[T] => Unit) {
    override def apply(t: Try[T]): Unit = t match {
      case Success(v) => complete(v)
      case Failure(e) => completeExceptionally(e)
    }

    /*
     * Ensure that completions of this future cannot hold the Scala Future’s completer hostage.
     */
    override def thenApply[U](fn: JF[_ >: T, _ <: U]): CompletableFuture[U] = thenApplyAsync(fn)

    override def thenAccept(fn: Consumer[_ >: T]): CompletableFuture[Void] = thenAcceptAsync(fn)

    override def thenRun(fn: Runnable): CompletableFuture[Void] = thenRunAsync(fn)

    override def thenCombine[U, V](cs: CompletionStage[_ <: U], fn: BiFunction[_ >: T, _ >: U, _ <: V]): CompletableFuture[V] = thenCombineAsync(cs, fn)

    override def thenAcceptBoth[U](cs: CompletionStage[_ <: U], fn: BiConsumer[_ >: T, _ >: U]): CompletableFuture[Void] = thenAcceptBothAsync(cs, fn)

    override def runAfterBoth(cs: CompletionStage[_], fn: Runnable): CompletableFuture[Void] = runAfterBothAsync(cs, fn)

    override def applyToEither[U](cs: CompletionStage[_ <: T], fn: JF[_ >: T, U]): CompletableFuture[U] = applyToEitherAsync(cs, fn)

    override def acceptEither(cs: CompletionStage[_ <: T], fn: Consumer[_ >: T]): CompletableFuture[Void] = acceptEitherAsync(cs, fn)

    override def runAfterEither(cs: CompletionStage[_], fn: Runnable): CompletableFuture[Void] = runAfterEitherAsync(cs, fn)

    override def thenCompose[U](fn: JF[_ >: T, _ <: CompletionStage[U]]): CompletableFuture[U] = thenComposeAsync(fn)

    override def whenComplete(fn: BiConsumer[_ >: T, _ >: Throwable]): CompletableFuture[T] = whenCompleteAsync(fn)

    override def handle[U](fn: BiFunction[_ >: T, Throwable, _ <: U]): CompletableFuture[U] = handleAsync(fn)

    override def exceptionally(fn: JF[Throwable, _ <: T]): CompletableFuture[T] = {
      val cf = new CompletableFuture[T]
      whenCompleteAsync(new BiConsumer[T, Throwable] {
        override def accept(t: T, e: Throwable): Unit = {
          if (e == null) cf.complete(t)
          else {
            val n: AnyRef =
              try {
                fn(e).asInstanceOf[AnyRef]
              } catch {
                case thr: Throwable => cf.completeExceptionally(thr); this
              }
            if (n ne this) cf.complete(n.asInstanceOf[T])
          }
        }
      })
      cf
    }

    /**
     * @inheritdoc
     *
     * WARNING: completing the result of this method will not complete the underlying
     *          Scala Future or Promise (ie, the one that that was passed to `toJava`.)
     */
    override def toCompletableFuture(): CompletableFuture[T] = this

    override def obtrudeValue(value: T): Unit = throw new UnsupportedOperationException("obtrudeValue may not be used on the result of toJava(scalaFuture)")

    override def obtrudeException(ex: Throwable): Unit = throw new UnsupportedOperationException("obtrudeException may not be used on the result of toJava(scalaFuture)")

    override def get(): T = scala.concurrent.blocking(super.get())

    override def get(timeout: Long, unit: TimeUnit): T = scala.concurrent.blocking(super.get(timeout, unit))

    override def toString: String = super[CompletableFuture].toString
  }

  class P[T](val wrapped: CompletionStage[T]) extends DefaultPromise[T] with BiConsumer[T, Throwable] {
    override def accept(v: T, e: Throwable): Unit = {
      if (e == null) complete(Success(v))
      else complete(Failure(e))
    }
  }
}
