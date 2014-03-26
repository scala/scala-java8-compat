package scala.concurrent.java8

import scala.concurrent.{ Future, Promise, ExecutionContext, ExecutionContextExecutorService, ExecutionContextExecutor, impl }
import java.util.concurrent.{ CompletionStage, Executor, ExecutorService, CompletableFuture }
import scala.util.{ Try, Success, Failure }
import java.util.function.{ BiConsumer, Function ⇒ JF, Consumer, BiFunction }

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
 * import static scala.concurrent.java8.FutureConverter.*;
 *
 * final CompletionStage<String> cs = ... // from an async Java API
 * final Future<String> f = toScala(cs);
 * ...
 * final Future<Integer> f2 = ... // from an async Scala API
 * final CompletionStage<Integer> cs2 = toJava(f2);
 * }}}
 */
object FutureConverter {

  private class CF[T] extends CompletableFuture[T] with (Try[T] => Unit) {
    override def apply(t: Try[T]): Unit = t match {
      case Success(v) ⇒ complete(v)
      case Failure(e) ⇒ completeExceptionally(e)
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
                case thr: Throwable ⇒ cf.completeExceptionally(thr); this
              }
            if (n ne this) cf.complete(n.asInstanceOf[T])
          }
        }
      })
      cf
    }

    override def toCompletableFuture(): CompletableFuture[T] =
      throw new UnsupportedOperationException("this CompletionStage represents a read-only Scala Future")
    
    override def toString: String = super[CompletableFuture].toString
  }

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
    val cf = new CF[T]
    implicit val ec = Future.InternalCallbackExecutor
    f onComplete cf
    cf
  }

  private class P[T] extends impl.Promise.DefaultPromise[T] with BiConsumer[T, Throwable] {
    override def accept(v: T, e: Throwable): Unit = {
      if (e == null) complete(Success(v))
      else complete(Failure(e))
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
    val p = new P[T]
    cs whenComplete p
    p.future
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

}
