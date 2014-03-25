package scala.concurrent

import java.util.concurrent.CompletionStage

package object java8 {

  implicit class futureToCompletionStage[T](val f: Future[T]) extends AnyVal {
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
    def toJava: CompletionStage[T] = FutureConverter.toJava(f)
  }

  implicit class completionStageToFuture[T](val cs: CompletionStage[T]) extends AnyVal {
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
    def toScala: Future[T] = FutureConverter.toScala(cs)
  }
}