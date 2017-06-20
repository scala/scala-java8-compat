/*
 * Copyright (C) 2012-2015 Lightbend Inc. <http://www.lightbend.com>
 */
package scala.compat.java8;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import scala.runtime.java8.*;

public class SpecializedTestSupport {
    public static class IntIdentity implements JFunction1$mcII$sp {
      public int apply$mcII$sp(int i) {
          List<StackTraceElement> stackTrace = Arrays.asList(Thread.currentThread().getStackTrace());
          long count = stackTrace.stream().filter(x -> x.getMethodName().equals("apply")).count();
          Assert.assertEquals("the unspecialized apply method should not have been called", 0, count);
          return i;
      }
    }
}
