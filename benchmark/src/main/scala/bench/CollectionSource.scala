package bench

import java.util.stream._

import scala.collection.generic.CanBuildFrom
import scala.compat.java8.StreamConverters._
import scala.compat.java8.collectionImpl._
import scala.compat.java8.converterImpl._

package object generate {
  private def myInty(n: Int) = 0 until n
  private def myStringy(n: Int) = myInty(n).map(i => (i*i).toString)

  object Coll {
    def i[CC[_]](n: Int)(implicit cbf: CanBuildFrom[Nothing, Int, CC[Int]]): CC[Int] = {
      val b = cbf();
      myInty(n).foreach(b += _)
      b.result()
    }
    def s[CC[_]](n: Int)(implicit cbf: CanBuildFrom[Nothing, String, CC[String]]): CC[String] = {
      val b = cbf();
      myStringy(n).foreach(b += _)
      b.result()
    }
  }

  object Pstep {
    def i[CC](cc: CC)(implicit steppize: CC => MakesIntStepper): IntStepper =
      steppize(cc).stepper
    def s[CC](cc: CC)(implicit steppize: CC => MakesAnyStepper[String]): AnyStepper[String] =
      steppize(cc).stepper
  }

  object Sstep {
    def i[CC](cc: CC)(implicit steppize: CC => MakesIntSeqStepper): IntStepper =
      steppize(cc).stepper
    def s[CC](cc: CC)(implicit steppize: CC => MakesAnySeqStepper[String]): AnyStepper[String] =
      steppize(cc).stepper
  }

  object PsStream {
    def i[CC](cc: CC)(implicit steppize: CC => MakesIntStepper): IntStream =
      steppize(cc).stepper.parStream
    def s[CC](cc: CC)(implicit steppize: CC => MakesAnyStepper[String]): Stream[String] =
      steppize(cc).stepper.parStream
  }

  object SsStream {
    def i[CC](cc: CC)(implicit steppize: CC => MakesIntSeqStepper): IntStream =
      steppize(cc).stepper.seqStream
    def s[CC](cc: CC)(implicit steppize: CC => MakesAnySeqStepper[String]): Stream[String] =
      steppize(cc).stepper.seqStream
  }

  object Sstream {
    def i[CC](cc: CC)(implicit streamize: CC => MakesSequentialStream[java.lang.Integer, IntStream]) =
      streamize(cc).seqStream
    def s[CC](cc: CC)(implicit streamize: CC => MakesSequentialStream[String, Stream[String]]) =
      streamize(cc).seqStream
  }

  object Pstream {
    def i[CC](cc: CC)(implicit streamize: CC => MakesParallelStream[java.lang.Integer, IntStream]) =
      streamize(cc).parStream
    def s[CC](cc: CC)(implicit streamize: CC => MakesParallelStream[String, Stream[String]]) =
      streamize(cc).parStream
  }
}
