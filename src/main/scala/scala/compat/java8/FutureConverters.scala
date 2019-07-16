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

package scala.compat.java8

import scala.language.implicitConversions

import scala.concurrent.java8.FuturesConvertersImpl._
import scala.concurrent.java8.FuturesConvertersImplCompat._
import scala.concurrent.{ Future, Promise, ExecutionContext, ExecutionContextExecutorService, ExecutionContextExecutor }
import java.util.concurrent.{ CompletionStage, Executor, ExecutorService }
import java.util.function.Consumer

/**
 * This class contains static methods which convert between Java CompletionStage
 * and Scala Future. This is useful when mediating between Scala and Java
 * libraries with asynchronous APIs.
 *
 * Note that the bridge is implemented at the read-only side of asynchronous
 * handles, namely scala.concurrent.Future instead of scala.concurrent.Promise
 * and CompletionStage instead of CompletableFuture. This is intentional, as
 * the semantics of bridging the write-handles would be prone to race
 * conditions; if both ends (CompletableFuture and Promise) are completed
 * independently at the same time, they may contain different values afterwards.
 * For this reason, <code>toCompletableFuture()</code> is not supported on the
 * created CompletionStages.
 *
 * Example usage:
 *
 * {{{
 * import java.util.concurrent.CompletionStage;
 * import scala.concurrent.Future;
 * import static scala.concurrent.java8.FutureConverters.*;
 *
 * final CompletionStage<String> cs = ... // from an async Java API
 * final Future<String> f = toScala(cs);
 * ...
 * final Future<Integer> f2 = ... // from an async Scala API
 * final CompletionStage<Integer> cs2 = toJava(f2);
 * }}}
 */
object FutureConverters {
  /**
   * Returns a CompletionStage that will be completed with the same value or
   * exception as the given Scala Future when that completes. Since the Future is a read-only
   * representation, this CompletionStage does not support the
   * <code>toCompletableFuture</code> method. The semantics of Scala Future
   * demand that all callbacks are invoked asynchronously by default, therefore
   * the returned CompletionStage routes all calls to synchronous
   * transformations to their asynchronous counterparts, i.e.
   * <code>thenRun</code> will internally call <code>thenRunAsync</code>.
   *
   * @param f The Scala Future which may eventually supply the completion for
   * the returned CompletionStage
   * @return a CompletionStage that runs all callbacks asynchronously and does
   * not support the CompletableFuture interface
   */
  def toJava[T](f: Future[T]): CompletionStage[T] = {
    f match {
      case p: P[T] => p.wrapped
      case _ =>
        val cf = new CF[T](f)
        implicit val ec = InternalCallbackExecutor
        f onComplete cf
        cf
    }
  }

  /**
   * Returns a Scala Future that will be completed with the same value or
   * exception as the given CompletionStage when that completes. Transformations
   * of the returned Future are executed asynchronously as specified by the
   * ExecutionContext that is given to the combinator methods.
   *
   * @param cs The CompletionStage which may eventually supply the completion
   * for the returned Scala Future
   * @return a Scala Future that represents the CompletionStage's completion
   */
  def toScala[T](cs: CompletionStage[T]): Future[T] = {
    cs match {
      case cf: CF[T] => cf.wrapped
      case _ =>
        val p = new P[T](cs)
        cs whenComplete p
        p.future
    }
  }

  /**
   * Creates an ExecutionContext from a given ExecutorService, using the given
   * Consumer for reporting errors. The latter can be created as in the
   * following example:
   *
   * {{{
   * final ExecutionContext ec = Converter.fromExecutorService(es, thr -> thr.printStackTrace());
   * }}}
   *
   * @param e an ExecutorService
   * @param reporter a Consumer for reporting errors during execution
   * @return an ExecutionContext backed by the given ExecutorService
   */
  def fromExecutorService(e: ExecutorService, reporter: Consumer[Throwable]): ExecutionContextExecutorService =
    ExecutionContext.fromExecutorService(e, reporter.accept)

  /**
   * Creates an ExecutionContext from a given ExecutorService, using the
   * default reporter for uncaught exceptions which will just call
   * <code>.printStackTrace()</code>.
   *
   * @param e an ExecutorService
   * @return an ExecutionContext backed by the given ExecutorService
   */
  def fromExecutorService(e: ExecutorService): ExecutionContextExecutorService =
    ExecutionContext.fromExecutorService(e, ExecutionContext.defaultReporter)

  /**
   * Creates an ExecutionContext from a given Executor, using the given
   * Consumer for reporting errors. The latter can be created as in the
   * following example:
   *
   * {{{
   * final ExecutionContext ec = Converter.fromExecutor(es, thr -> thr.printStackTrace());
   * }}}
   *
   * @param e an Executor
   * @param reporter a Consumer for reporting errors during execution
   * @return an ExecutionContext backed by the given Executor
   */
  def fromExecutor(e: Executor, reporter: Consumer[Throwable]): ExecutionContextExecutor =
    ExecutionContext.fromExecutor(e, reporter.accept)

  /**
   * Creates an ExecutionContext from a given Executor, using the
   * default reporter for uncaught exceptions which will just call
   * <code>.printStackTrace()</code>.
   *
   * @param e an Executor
   * @return an ExecutionContext backed by the given Executor
   */
  def fromExecutor(e: Executor): ExecutionContextExecutor =
    ExecutionContext.fromExecutor(e, ExecutionContext.defaultReporter)

  /**
   * Return the global ExecutionContext for Scala Futures.
   *
   * @return the ExecutionContext
   */
  def globalExecutionContext: ExecutionContext = ExecutionContext.global

  /**
   * Construct an empty <code>scala.concurrent.Promise</code>.
   *
   * @return a Promise which is not yet completed
   */
  def promise[T](): Promise[T] = Promise()

  /**
   * Construct an already fulfilled <code>scala.concurrent.Promise</code> which holds the given value.
   *
   * @return the fulfilled Promise
   */
  def keptPromise[T](v: T): Promise[T] = Promise.successful(v)

  /**
   * Construct an already fulfilled <code>scala.concurrent.Promise</code> which holds the given failure.
   *
   * @return the fulfilled Promise
   */
  def failedPromise[T](ex: Throwable): Promise[T] = Promise.failed(ex)

  implicit def FutureOps[T](f: Future[T]): FutureOps[T] = new FutureOps[T](f)
  final class FutureOps[T](val __self: Future[T]) extends AnyVal {
    /**
     * Returns a CompletionStage that will be completed with the same value or
     * exception as the given Scala Future when that completes. Since the Future is a read-only
     * representation, this CompletionStage does not support the
     * <code>toCompletableFuture</code> method. The semantics of Scala Future
     * demand that all callbacks are invoked asynchronously by default, therefore
     * the returned CompletionStage routes all calls to synchronous
     * transformations to their asynchronous counterparts, i.e.
     * <code>thenRun</code> will internally call <code>thenRunAsync</code>.
     *
     * @return a CompletionStage that runs all callbacks asynchronously and does
     * not support the CompletableFuture interface
     */
    def toJava: CompletionStage[T] = FutureConverters.toJava(__self)
  }

  implicit def CompletionStageOps[T](cs: CompletionStage[T]): CompletionStageOps[T] = new CompletionStageOps(cs)

  final class CompletionStageOps[T](val __self: CompletionStage[T]) extends AnyVal {
    /**
     * Returns a Scala Future that will be completed with the same value or
     * exception as the given CompletionStage when that completes. Transformations
     * of the returned Future are executed asynchronously as specified by the
     * ExecutionContext that is given to the combinator methods.
     *
     * @return a Scala Future that represents the CompletionStage's completion
     */
    def toScala: Future[T] = FutureConverters.toScala(__self)
  }
}
