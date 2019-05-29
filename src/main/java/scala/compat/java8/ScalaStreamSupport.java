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

package scala.compat.java8;

import java.util.stream.*;

import scala.collection.*;
import scala.jdk.javaapi.StreamConverters;

/**
 * This class contains static utility methods for creating Java Streams from Scala Collections, similar
 * to the methods in {@code java.util.stream.StreamSupport} for other Java types. It is intended for
 * use from Java code. In Scala code, you can use the extension methods provided by
 * {@code scala.compat.java8.StreamConverters} instead.
 *
 * Streams created from immutable Scala collections are also immutable. Mutable collections should
 * not be modified concurrently. There are no guarantees for success or failure modes of existing
 * streams in case of concurrent modifications.
 */
public class ScalaStreamSupport {
    /////////////////////
    // Generic Streams //
    /////////////////////

    /** 
     * Generates a Stream that traverses a Scala collection.
     * <p>
     * Parallel processing is only efficient for collections that have a Stepper implementation
     * which supports efficient splitting. For collections where this is the case, the stepper
     * method has a return type marked with EfficientSplit.
     *
     * @param coll The IterableOnce to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <T> Stream<T> stream(IterableOnce<T> coll) {
        return StreamConverters.asJavaSeqStream(coll);
    }

    /** 
     * Generates a Stream that traverses the keys of a scala.collection.Map.
     * <p>
     * Parallel processing is only efficient for Maps that have a keyStepper implementation
     * which supports efficient splitting. For collections where this is the case, the keyStepper
     * method has a return type marked with EfficientSplit.
     *
     * @param coll The Map to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <K> Stream<K> streamKeys(Map<K, ?> coll) {
        return StreamSupport.stream(coll.keyStepper(StepperShape.anyStepperShape()).spliterator(), false);
    }

    /** 
     * Generates a Stream that traverses the values of a scala.collection.Map.
     * <p>
     * Parallel processing is only efficient for Maps that have a valueStepper implementation
     * which supports efficient splitting. For collections where this is the case, the valueStepper
     * method has a return type marked with EfficientSplit.
     *
     * @param coll The Map to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <V> Stream<V> streamValues(Map<?, V> coll) {
        return StreamSupport.stream(coll.<AnyStepper<V>>valueStepper(StepperShape.anyStepperShape()).spliterator(), false);
    }

    /** 
     * Generates a Stream that traverses the key-value pairs of a scala.collection.Map.
     * <p>
     * Parallel processing is only efficient for collections that have a Stepper implementation
     * which supports efficient splitting. For collections where this is the case, the stepper
     * method has a return type marked with EfficientSplit.
     *
     * @param coll The Map to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <K,V> Stream< scala.Tuple2<K, V> > stream(Map<K, V> coll) {
        return StreamConverters.asJavaSeqStream(coll);
    }
    
    /**
     * Generates a Stream that traverses any Scala collection by accumulating its entries
     * into a buffer class (Accumulator).
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The collection to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <T> Stream<T> streamAccumulated(IterableOnce<T> coll) {
        return StreamConverters.asJavaSeqStream(scala.jdk.AnyAccumulator.from(coll));
    }

    /** 
     * Generates a Stream that traverses the keys of any Scala map by
     * accumulating those keys into a buffer class (Accumulator).
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The map containing keys to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <K> Stream<K> streamAccumulatedKeys(Map<K, ?> coll) {
        return StreamConverters.asJavaSeqStream(scala.jdk.AnyAccumulator.from(coll.keysIterator()));
    }

    /** 
     * Generates a Stream that traverses the values of any Scala map by
     * accumulating those values into a buffer class (Accumulator).
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The map containing values to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <V> Stream<V> streamAccumulatedValues(Map<?, V> coll) {
        return StreamConverters.asJavaSeqStream(scala.jdk.AnyAccumulator.from(coll.valuesIterator()));
    }

    ////////////////////
    // Double Streams //
    ////////////////////

    /**
     * Generates a DoubleStream that traverses a Scala collection.
     * <p>
     * Parallel processing is only efficient for collections that have a Stepper implementation
     * which supports efficient splitting. For collections where this is the case, the stepper
     * method has a return type marked with EfficientSplit.
     *
     * @param coll The IterableOnce to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStream(IterableOnce<Double> coll) {
        return StreamConverters.asJavaSeqDoubleStream(coll);
    }

    /**
     * Generates a DoubleStream that traverses the keys of a scala.collection.Map.
     * <p>
     * Parallel processing is only efficient for Maps that have a keyStepper implementation
     * which supports efficient splitting. For collections where this is the case, the keyStepper
     * method has a return type marked with EfficientSplit.
     *
     * @param coll The Map to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStreamKeys(Map<Double, ?> coll) {
        return StreamSupport.doubleStream(coll.keyStepper((StepperShape<Double, DoubleStepper>)(Object)StepperShape.doubleStepperShape()).spliterator(), false);
    }

    /**
     * Generates a DoubleStream that traverses the values of a scala.collection.Map.
     * <p>
     * Parallel processing is only efficient for Maps that have a valueStepper implementation
     * which supports efficient splitting. For collections where this is the case, the valueStepper
     * method has a return type marked with EfficientSplit.
     *
     * @param coll The Map to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStreamValues(Map<?, Double> coll) {
        return StreamSupport.doubleStream(coll.valueStepper((StepperShape<Double, DoubleStepper>)(Object)StepperShape.doubleStepperShape()).spliterator(), false);
    }

    /**
     * Generates a DoubleStream that traverses any Scala collection by accumulating its entries
     * into a buffer class (Accumulator).
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The collection to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStreamAccumulated(IterableOnce<Double> coll) {
        return StreamConverters.asJavaSeqDoubleStream((IterableOnce<Double>)(Object)scala.jdk.DoubleAccumulator$.MODULE$.fromSpecific((IterableOnce<Object>)(Object)coll));
    }

    /**
     * Generates a DoubleStream that traverses the keys of any Scala map by
     * accumulating those keys into a buffer class (Accumulator).
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The map containing keys to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStreamAccumulatedKeys(Map<Double, ?> coll) {
        return StreamConverters.asJavaSeqDoubleStream((IterableOnce<Double>)(Object)scala.jdk.DoubleAccumulator$.MODULE$.fromSpecific((IterableOnce<Object>)(Object)coll.keysIterator()));
    }

    /**
     * Generates a DoubleStream that traverses the values of any Scala map by
     * accumulating those values into a buffer class (Accumulator).
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The map containing values to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStreamAccumulatedValues(Map<?, Double> coll) {
        return StreamConverters.asJavaSeqDoubleStream((IterableOnce<Double>)(Object)scala.jdk.DoubleAccumulator$.MODULE$.fromSpecific((IterableOnce<Object>)(Object)coll.valuesIterator()));
    }

    /////////////////
    // Int Streams //
    /////////////////

    /**
     * Generates a IntStream that traverses a Scala collection.
     * <p>
     * Parallel processing is only efficient for collections that have a Stepper implementation
     * which supports efficient splitting. For collections where this is the case, the stepper
     * method has a return type marked with EfficientSplit.
     *
     * @param coll The IterableOnce to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStream(IterableOnce<Integer> coll) {
        return StreamConverters.asJavaSeqIntStream(coll);
    }

    /**
     * Generates a IntStream that traverses the keys of a scala.collection.Map.
     * <p>
     * Parallel processing is only efficient for Maps that have a keyStepper implementation
     * which supports efficient splitting. For collections where this is the case, the keyStepper
     * method has a return type marked with EfficientSplit.
     *
     * @param coll The Map to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStreamKeys(Map<Integer, ?> coll) {
        return StreamSupport.intStream(coll.keyStepper((StepperShape<Integer, IntStepper>)(Object)StepperShape.intStepperShape()).spliterator(), false);
    }

    /**
     * Generates a IntStream that traverses the values of a scala.collection.Map.
     * <p>
     * Parallel processing is only efficient for Maps that have a valueStepper implementation
     * which supports efficient splitting. For collections where this is the case, the valueStepper
     * method has a return type marked with EfficientSplit.
     *
     * @param coll The Map to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStreamValues(Map<?, Integer> coll) {
        return StreamSupport.intStream(coll.valueStepper((StepperShape<Integer, IntStepper>)(Object)StepperShape.intStepperShape()).spliterator(), false);
    }

    /**
     * Generates a IntStream that traverses any Scala collection by accumulating its entries
     * into a buffer class (Accumulator).
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The collection to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStreamAccumulated(IterableOnce<Integer> coll) {
        return StreamConverters.asJavaSeqIntStream((IterableOnce<Integer>)(Object)scala.jdk.IntAccumulator$.MODULE$.fromSpecific((IterableOnce<Object>)(Object)coll));
    }

    /**
     * Generates a IntStream that traverses the keys of any Scala map by
     * accumulating those keys into a buffer class (Accumulator).
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The map containing keys to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStreamAccumulatedKeys(Map<Integer, ?> coll) {
        return StreamConverters.asJavaSeqIntStream((IterableOnce<Integer>)(Object)scala.jdk.IntAccumulator$.MODULE$.fromSpecific((IterableOnce<Object>)(Object)coll.keysIterator()));
    }

    /**
     * Generates a IntStream that traverses the values of any Scala map by
     * accumulating those values into a buffer class (Accumulator).
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The map containing values to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStreamAccumulatedValues(Map<?, Integer> coll) {
        return StreamConverters.asJavaSeqIntStream((IterableOnce<Integer>)(Object)scala.jdk.IntAccumulator$.MODULE$.fromSpecific((IterableOnce<Object>)(Object)coll.valuesIterator()));
    }

    //////////////////
    // Long Streams //
    //////////////////

    /**
     * Generates a LongStream that traverses a Scala collection.
     * <p>
     * Parallel processing is only efficient for collections that have a Stepper implementation
     * which supports efficient splitting. For collections where this is the case, the stepper
     * method has a return type marked with EfficientSplit.
     *
     * @param coll The IterableOnce to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStream(IterableOnce<Long> coll) {
        return StreamConverters.asJavaSeqLongStream(coll);
    }

    /**
     * Generates a LongStream that traverses the keys of a scala.collection.Map.
     * <p>
     * Parallel processing is only efficient for Maps that have a keyStepper implementation
     * which supports efficient splitting. For collections where this is the case, the keyStepper
     * method has a return type marked with EfficientSplit.
     *
     * @param coll The Map to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStreamKeys(Map<Long, ?> coll) {
        return StreamSupport.longStream(coll.keyStepper((StepperShape<Long, LongStepper>)(Object)StepperShape.doubleStepperShape()).spliterator(), false);
    }

    /**
     * Generates a LongStream that traverses the values of a scala.collection.Map.
     * <p>
     * Parallel processing is only efficient for Maps that have a valueStepper implementation
     * which supports efficient splitting. For collections where this is the case, the valueStepper
     * method has a return type marked with EfficientSplit.
     *
     * @param coll The Map to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStreamValues(Map<?, Long> coll) {
        return StreamSupport.longStream(coll.valueStepper((StepperShape<Long, LongStepper>)(Object)StepperShape.doubleStepperShape()).spliterator(), false);
    }

    /**
     * Generates a LongStream that traverses any Scala collection by accumulating its entries
     * into a buffer class (Accumulator).
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The collection to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStreamAccumulated(IterableOnce<Long> coll) {
        return StreamConverters.asJavaSeqLongStream((IterableOnce<Long>)(Object)scala.jdk.LongAccumulator$.MODULE$.fromSpecific((IterableOnce<Object>)(Object)coll));
    }

    /**
     * Generates a LongStream that traverses the keys of any Scala map by
     * accumulating those keys into a buffer class (Accumulator).
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The map containing keys to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStreamAccumulatedKeys(Map<Long, ?> coll) {
        return StreamConverters.asJavaSeqLongStream((IterableOnce<Long>)(Object)scala.jdk.LongAccumulator$.MODULE$.fromSpecific((IterableOnce<Object>)(Object)coll.keysIterator()));
    }

    /**
     * Generates a LongStream that traverses the values of any Scala map by
     * accumulating those values into a buffer class (Accumulator).
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The map containing values to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStreamAccumulatedValues(Map<?, Long> coll) {
        return StreamConverters.asJavaSeqLongStream((IterableOnce<Long>)(Object)scala.jdk.LongAccumulator$.MODULE$.fromSpecific((IterableOnce<Object>)(Object)coll.valuesIterator()));
    }
}
