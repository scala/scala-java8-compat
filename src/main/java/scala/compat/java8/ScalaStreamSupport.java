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

import scala.compat.java8.converterImpl.*;
import scala.compat.java8.collectionImpl.*;
import java.util.stream.*;
import scala.compat.java8.runtime.CollectionInternals;

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
     * Generates a Stream that traverses an IndexedSeq.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The IndexedSeq to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <T> Stream<T> stream(scala.collection.IndexedSeq<T> coll) {
        return StreamSupport.stream(new StepsAnyIndexedSeq<T>((scala.collection.IndexedSeq)coll, 0, coll.length()), false);
    }

    /** 
     * Generates a Stream that traverses a scala.collection.immutable.HashSet.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The immutable.HashSet to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <T> Stream<T> stream(scala.collection.immutable.HashSet<T> coll) {
        return StreamSupport.stream(new StepsAnyImmHashSet<T>(coll.iterator(), coll.size()), false);
    }

    /** 
     * Generates a Stream that traverses the keys of a scala.collection.mutable.HashMap.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The mutable.HashMap to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <K> Stream<K> streamKeys(scala.collection.mutable.HashMap<K, ?> coll) {
        Object[] tbl = CollectionInternals.getTable(coll);
        return StreamSupport.stream(new StepsAnyHashTableKey(tbl, 0, tbl.length), false);
    }

    /** 
     * Generates a Stream that traverses the values of a scala.collection.mutable.HashMap.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The mutable.HashMap to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <V> Stream<V> streamValues(scala.collection.mutable.HashMap<? super Object, V> coll) {
        Object[] tbl = CollectionInternals.getTable(coll);
        return StreamSupport.stream(new StepsAnyDefaultHashTableValue(tbl, 0, tbl.length), false);
    }

    /** 
     * Generates a Stream that traverses the key-value pairs of a scala.collection.mutable.HashMap.
     * The key-value pairs are presented as instances of scala.Tuple2.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The mutable.HashMap to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <K, V> Stream< scala.Tuple2<K, V> > stream(scala.collection.mutable.HashMap<K, V> coll) {
        Object[] tbl =
            CollectionInternals.getTable(coll);
        return StreamSupport.stream(new StepsAnyDefaultHashTable<K, V>(tbl, 0, tbl.length), false);
    }

    /** 
     * Generates a Stream that traverses a scala.collection.mutable.HashSet.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The mutable.HashSet to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <T> Stream<T> stream(scala.collection.mutable.HashSet<T> coll) {
        Object[] tbl = CollectionInternals.getTable(coll);
        return StreamSupport.stream(new StepsAnyFlatHashTable<T>(tbl, 0, tbl.length), false);
    }

    /** 
     * Generates a Stream that traverses a scala.collection.immutable.Vector.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The Vector to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <T> Stream<T> stream(scala.collection.immutable.Vector<T> coll) {
        return StreamSupport.stream(new StepsAnyVector<T>(coll, 0, coll.length()), false);
    }

    /** 
     * Generates a Stream that traverses the keys of a scala.collection.Map.
     * <p>
     * Only sequential operations will be efficient. 
     * For efficient parallel operation, use the streamAccumulatedKeys method instead, but
     * note that this creates a new collection containing the Map's keys.
     *
     * @param coll The Map to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <K> Stream<K> streamKeys(scala.collection.Map<K, ?> coll) {
        return StreamSupport.stream(new StepsAnyIterator<K>(coll.keysIterator()), false);
    }

    /** 
     * Generates a Stream that traverses the values of a scala.collection.Map.
     * <p>
     * Only sequential operations will be efficient. 
     * For efficient parallel operation, use the streamAccumulatedValues method instead, but
     * note that this creates a new collection containing the Map's values.
     *
     * @param coll The Map to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <V> Stream<V> streamValues(scala.collection.Map<?, V> coll) {
        return StreamSupport.stream(new StepsAnyIterator<V>(coll.valuesIterator()), false);
    }

    /** 
     * Generates a Stream that traverses the key-value pairs of a scala.collection.Map.
     * <p>
     * Only sequential operations will be efficient. 
     * For efficient parallel operation, use the streamAccumulated method instead, but
     * note that this creates a new collection containing the Map's key-value pairs.
     *
     * @param coll The Map to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <K,V> Stream< scala.Tuple2<K, V> > stream(scala.collection.Map<K, V> coll) {
        return StreamSupport.stream(new StepsAnyIterator< scala.Tuple2<K, V> >(coll.iterator()), false);
    }
    
    /** 
     * Generates a Stream that traverses a scala.collection.Iterator.
     * <p>
     * Only sequential operations will be efficient.
     * For efficient parallel operation, use the streamAccumulated method instead,
     * but note that this creates a copy of the contents of the Iterator.
     *
     * @param coll The scala.collection.Iterator to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <T> Stream<T> stream(scala.collection.Iterator<T> coll) {
        return StreamSupport.stream(new StepsAnyIterator<T>(coll), false);
    }

    /** 
     * Generates a Stream that traverses a scala.collection.Iterable.
     * <p>
     * Only sequential operations will be efficient.
     * For efficient parallel operation, use the streamAccumulated method instead,
     * but note that this creates a copy of the contents of the Iterable
     *
     * @param coll The scala.collection.Iterable to traverse
     * @return     A Stream view of the collection which, by default, executes sequentially.
     */
    public static <T> Stream<T> stream(scala.collection.Iterable<T> coll) {
        return StreamSupport.stream(new StepsAnyIterator<T>(coll.iterator()), false);
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
    public static <T> Stream<T> streamAccumulated(scala.collection.IterableOnce<T> coll) {
        scala.compat.java8.collectionImpl.Accumulator<T> acc = scala.compat.java8.collectionImpl.Accumulator.from(coll);
        return StreamSupport.stream(acc.spliterator(), false);
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
    public static <K> Stream<K> streamAccumulatedKeys(scala.collection.Map<K, ?> coll) {
        scala.compat.java8.collectionImpl.Accumulator<K> acc = scala.compat.java8.collectionImpl.Accumulator.from(coll.keysIterator());
        return StreamSupport.stream(acc.spliterator(), false);
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
    public static <V> Stream<V> streamAccumulatedValues(scala.collection.Map<?, V> coll) {
        scala.compat.java8.collectionImpl.Accumulator<V> acc = scala.compat.java8.collectionImpl.Accumulator.from(coll.valuesIterator());
        return StreamSupport.stream(acc.spliterator(), false);
    }

    ////////////////////
    // Double Streams //
    ////////////////////

    /** 
     * Generates a DoubleStream that traverses an IndexedSeq of Doubles.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The IndexedSeq to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStream(scala.collection.IndexedSeq<Double> coll) {
        return StreamSupport.doubleStream(new StepsDoubleIndexedSeq(coll, 0, coll.length()), false);
    }

    /** 
     * Generates a DoubleStream that traverses a scala.collection.immutable.HashSet of Doubles.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The immutable.HashSet to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStream(scala.collection.immutable.HashSet<Double> coll) {
        scala.collection.Iterator iter = (scala.collection.Iterator)coll.iterator();
        return StreamSupport.doubleStream(new StepsDoubleImmHashSet(iter, coll.size()), false);
    }

    /** 
     * Generates a DoubleStream that traverses double-valued keys of a scala.collection.mutable.HashMap.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The mutable.HashMap to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStreamKeys(scala.collection.mutable.HashMap<Double, ?> coll) {
        Object[] tbl = CollectionInternals.getTable(coll);
        return StreamSupport.doubleStream(new StepsDoubleHashTableKey(tbl, 0, tbl.length), false);
    }

    /** 
     * Generates a DoubleStream that traverses double-valued values of a scala.collection.mutable.HashMap.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The mutable.HashMap to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStreamValues(scala.collection.mutable.HashMap<? super Object, Double> coll) {
        Object[] tbl = CollectionInternals.getTable(coll);
        return StreamSupport.doubleStream(new StepsDoubleDefaultHashTableValue(tbl, 0, tbl.length), false);
    }

    /** 
     * Generates a DoubleStream that traverses a scala.collection.mutable.HashSet of Doubles.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The mutable.HashSet to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStream(scala.collection.mutable.HashSet<Double> coll) {
        Object[] tbl = CollectionInternals.getTable(coll);
        return StreamSupport.doubleStream(new StepsDoubleFlatHashTable(tbl, 0, tbl.length), false);
    }

    /** 
     * Generates a DoubleStream that traverses a scala.collection.immutable.Vector of Doubles.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The Vector to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStream(scala.collection.immutable.Vector<Double> coll) {
        scala.collection.immutable.Vector erased = (scala.collection.immutable.Vector)coll;
        return StreamSupport.doubleStream(new StepsDoubleVector(erased, 0, coll.length()), false);
    }

    /** 
     * Generates a DoubleStream that traverses the double-valued keys of a scala.collection.Map.
     * <p>
     * Only sequential operations will be efficient. 
     * For efficient parallel operation, use the doubleStreamAccumulatedKeys method instead, but
     * note that this creates a new collection containing the Map's keys.
     *
     * @param coll The Map to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStreamKeys(scala.collection.Map<Double, ?> coll) {
        scala.collection.Iterator iter = (scala.collection.Iterator)coll.keysIterator();
        return StreamSupport.doubleStream(new StepsDoubleIterator(iter), false);
    }

    /** 
     * Generates a DoubleStream that traverses the double-valued values of a scala.collection.Map.
     * <p>
     * Only sequential operations will be efficient. 
     * For efficient parallel operation, use the doubleStreamAccumulatedValues method instead, but
     * note that this creates a new collection containing the Map's values.
     *
     * @param coll The Map to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStreamValues(scala.collection.Map<?, Double> coll) {
        scala.collection.Iterator iter = (scala.collection.Iterator)coll.valuesIterator();
        return StreamSupport.doubleStream(new StepsDoubleIterator(iter), false);
    }

    /** 
     * Generates a DoubleStream that traverses a double-valued scala.collection.Iterator.
     * <p>
     * Only sequential operations will be efficient.
     * For efficient parallel operation, use the doubleStreamAccumulated method instead,
     * but note that this creates a copy of the contents of the Iterator.
     *
     * @param coll The scala.collection.Iterator to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStream(scala.collection.Iterator<Double> coll) {
        return StreamSupport.doubleStream(new StepsDoubleIterator((scala.collection.Iterator)coll), false);
    }

    /** 
     * Generates a DoubleStream that traverses a double-valued scala.collection.Iterable.
     * <p>
     * Only sequential operations will be efficient.
     * For efficient parallel operation, use the doubleStreamAccumulated method instead,
     * but note that this creates a copy of the contents of the Iterable.
     *
     * @param coll The scala.collection.Iterable to traverse
     * @return     A DoubleStream view of the collection which, by default, executes sequentially.
     */
    public static DoubleStream doubleStream(scala.collection.Iterable<Double> coll) {
        scala.collection.Iterator iter = (scala.collection.Iterator)coll.iterator();        
        return StreamSupport.doubleStream(new StepsDoubleIterator(iter), false);
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
    public static DoubleStream doubleStreamAccumulated(scala.collection.IterableOnce<Double> coll) {
        scala.compat.java8.collectionImpl.DoubleAccumulator acc = 
          scala.compat.java8.collectionImpl.DoubleAccumulator.from((scala.collection.IterableOnce)coll);
        return StreamSupport.doubleStream(acc.spliterator(), false);
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
    public static DoubleStream doubleStreamAccumulatedKeys(scala.collection.Map<Double, ?> coll) {
        scala.compat.java8.collectionImpl.DoubleAccumulator acc = 
          scala.compat.java8.collectionImpl.DoubleAccumulator.from((scala.collection.Iterator)coll.keysIterator());
        return StreamSupport.doubleStream(acc.spliterator(), false);
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
    public static DoubleStream doubleStreamAccumulatedValues(scala.collection.Map<?, Double> coll) {
        scala.compat.java8.collectionImpl.DoubleAccumulator acc = 
          scala.compat.java8.collectionImpl.DoubleAccumulator.from((scala.collection.Iterator)coll.valuesIterator());
        return StreamSupport.doubleStream(acc.spliterator(), false);
    }

    /////////////////
    // Int Streams //
    /////////////////

    /** 
     * Generates a IntStream that traverses a BitSet.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The BitSet to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStream(scala.collection.BitSet coll) {
        // Let the value class figure out the casting!
        scala.compat.java8.converterImpl.RichBitSetCanStep rbscs =
          new scala.compat.java8.converterImpl.RichBitSetCanStep(coll);
        return StreamSupport.intStream(rbscs.stepper(StepperShape$.MODULE$.intStepperShape()), false);
    }

    /** 
     * Generates a IntStream that traverses a Range.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The Range to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStream(scala.collection.immutable.Range coll) {
        return StreamSupport.intStream(new scala.compat.java8.converterImpl.StepsIntRange(coll, 0, coll.length()), false);
    }

    /** 
     * Generates a IntStream that traverses an IndexedSeq of Ints.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The IndexedSeq to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStream(scala.collection.IndexedSeq<Integer> coll) {
        return StreamSupport.intStream(new StepsIntIndexedSeq(coll, 0, coll.length()), false);
    }

    /** 
     * Generates a IntStream that traverses a scala.collection.immutable.HashSet of Ints.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The immutable.HashSet to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStream(scala.collection.immutable.HashSet<Integer> coll) {
        scala.collection.Iterator iter = (scala.collection.Iterator)coll.iterator();
        return StreamSupport.intStream(new StepsIntImmHashSet(iter, coll.size()), false);
    }

    /** 
     * Generates a IntStream that traverses int-valued keys of a scala.collection.mutable.HashMap.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The mutable.HashMap to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStreamKeys(scala.collection.mutable.HashMap<Integer, ?> coll) {
        Object[] tbl = CollectionInternals.getTable(coll);
        return StreamSupport.intStream(new StepsIntHashTableKey(tbl, 0, tbl.length), false);
    }

    /** 
     * Generates a IntStream that traverses int-valued values of a scala.collection.mutable.HashMap.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The mutable.HashMap to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStreamValues(scala.collection.mutable.HashMap<? super Object, Integer> coll) {
        Object[] tbl = CollectionInternals.getTable(coll);
        return StreamSupport.intStream(new StepsIntDefaultHashTableValue(tbl, 0, tbl.length), false);
    }

    /** 
     * Generates a IntStream that traverses a scala.collection.mutable.HashSet of Ints.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The mutable.HashSet to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStream(scala.collection.mutable.HashSet<Integer> coll) {
        Object[] tbl = CollectionInternals.getTable(coll);
        return StreamSupport.intStream(new StepsIntFlatHashTable(tbl, 0, tbl.length), false);
    }

    /** 
     * Generates a IntStream that traverses a scala.collection.immutable.Vector of Ints.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The Vector to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStream(scala.collection.immutable.Vector<Integer> coll) {
        scala.collection.immutable.Vector erased = (scala.collection.immutable.Vector)coll;
        return StreamSupport.intStream(new StepsIntVector(erased, 0, coll.length()), false);
    }

    /** 
     * Generates a IntStream that traverses the int-valued keys of a scala.collection.Map.
     * <p>
     * Only sequential operations will be efficient. 
     * For efficient parallel operation, use the intStreamAccumulatedKeys method instead, but
     * note that this creates a new collection containing the Map's keys.
     *
     * @param coll The Map to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStreamKeys(scala.collection.Map<Integer, ?> coll) {
        scala.collection.Iterator iter = (scala.collection.Iterator)coll.keysIterator();
        return StreamSupport.intStream(new StepsIntIterator(iter), false);
    }

    /** 
     * Generates a IntStream that traverses the int-valued values of a scala.collection.Map.
     * <p>
     * Only sequential operations will be efficient. 
     * For efficient parallel operation, use the intStreamAccumulatedValues method instead, but
     * note that this creates a new collection containing the Map's values.
     *
     * @param coll The Map to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStreamValues(scala.collection.Map<?, Integer> coll) {
        scala.collection.Iterator iter = (scala.collection.Iterator)coll.valuesIterator();
        return StreamSupport.intStream(new StepsIntIterator(iter), false);
    }

    /** 
     * Generates a IntStream that traverses a int-valued scala.collection.Iterator.
     * <p>
     * Only sequential operations will be efficient.
     * For efficient parallel operation, use the intStreamAccumulated method instead,
     * but note that this creates a copy of the contents of the Iterator.
     *
     * @param coll The scala.collection.Iterator to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStream(scala.collection.Iterator<Integer> coll) {
        return StreamSupport.intStream(new StepsIntIterator((scala.collection.Iterator)coll), false);
    }

    /** 
     * Generates a IntStream that traverses a int-valued scala.collection.Iterable.
     * <p>
     * Only sequential operations will be efficient.
     * For efficient parallel operation, use the intStreamAccumulated method instead,
     * but note that this creates a copy of the contents of the Iterable.
     *
     * @param coll The scala.collection.Iterable to traverse
     * @return     A IntStream view of the collection which, by default, executes sequentially.
     */
    public static IntStream intStream(scala.collection.Iterable<Integer> coll) {
        scala.collection.Iterator iter = (scala.collection.Iterator)coll.iterator();        
        return StreamSupport.intStream(new StepsIntIterator(iter), false);
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
    public static IntStream intStreamAccumulated(scala.collection.IterableOnce<Integer> coll) {
        scala.compat.java8.collectionImpl.IntAccumulator acc = 
          scala.compat.java8.collectionImpl.IntAccumulator.from((scala.collection.IterableOnce)coll);
        return StreamSupport.intStream(acc.spliterator(), false);
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
    public static IntStream intStreamAccumulatedKeys(scala.collection.Map<Integer, ?> coll) {
        scala.compat.java8.collectionImpl.IntAccumulator acc = 
          scala.compat.java8.collectionImpl.IntAccumulator.from((scala.collection.Iterator)coll.keysIterator());
        return StreamSupport.intStream(acc.spliterator(), false);
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
    public static IntStream intStreamAccumulatedValues(scala.collection.Map<?, Integer> coll) {
        scala.compat.java8.collectionImpl.IntAccumulator acc = 
          scala.compat.java8.collectionImpl.IntAccumulator.from((scala.collection.Iterator)coll.valuesIterator());
        return StreamSupport.intStream(acc.spliterator(), false);
    }

    //////////////////
    // Long Streams //
    //////////////////

    /** 
     * Generates a LongStream that traverses an IndexedSeq of Longs.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The IndexedSeq to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStream(scala.collection.IndexedSeq<Long> coll) {
        return StreamSupport.longStream(new StepsLongIndexedSeq(coll, 0, coll.length()), false);
    }

    /** 
     * Generates a LongStream that traverses a scala.collection.immutable.HashSet of Longs.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The immutable.HashSet to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStream(scala.collection.immutable.HashSet<Long> coll) {
        scala.collection.Iterator iter = (scala.collection.Iterator)coll.iterator();
        return StreamSupport.longStream(new StepsLongImmHashSet(iter, coll.size()), false);
    }

    /** 
     * Generates a LongStream that traverses long-valued keys of a scala.collection.mutable.HashMap.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The mutable.HashMap to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStreamKeys(scala.collection.mutable.HashMap<Long, ?> coll) {
        Object[] tbl = CollectionInternals.getTable(coll);
        return StreamSupport.longStream(new StepsLongHashTableKey(tbl, 0, tbl.length), false);
    }

    /** 
     * Generates a LongStream that traverses long-valued values of a scala.collection.mutable.HashMap.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The mutable.HashMap to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStreamValues(scala.collection.mutable.HashMap<? super Object, Long> coll) {
        Object[] tbl = CollectionInternals.getTable(coll);
        return StreamSupport.longStream(new StepsLongDefaultHashTableValue(tbl, 0, tbl.length), false);
    }

    /** 
     * Generates a LongStream that traverses a scala.collection.mutable.HashSet of Longs.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The mutable.HashSet to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStream(scala.collection.mutable.HashSet<Long> coll) {
        Object[] tbl = CollectionInternals.getTable(coll);
        return StreamSupport.longStream(new StepsLongFlatHashTable(tbl, 0, tbl.length), false);
    }

    /** 
     * Generates a LongStream that traverses a scala.collection.immutable.Vector of Longs.
     * <p>
     * Both sequential and parallel operations will be efficient.
     *
     * @param coll The Vector to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStream(scala.collection.immutable.Vector<Long> coll) {
        scala.collection.immutable.Vector erased = (scala.collection.immutable.Vector)coll;
        return StreamSupport.longStream(new StepsLongVector(erased, 0, coll.length()), false);
    }

    /** 
     * Generates a LongStream that traverses the long-valued keys of a scala.collection.Map.
     * <p>
     * Only sequential operations will be efficient. 
     * For efficient parallel operation, use the longStreamAccumulatedKeys method instead, but
     * note that this creates a new collection containing the Map's keys.
     *
     * @param coll The Map to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStreamKeys(scala.collection.Map<Long, ?> coll) {
        scala.collection.Iterator iter = (scala.collection.Iterator)coll.keysIterator();
        return StreamSupport.longStream(new StepsLongIterator(iter), false);
    }

    /** 
     * Generates a LongStream that traverses the long-valued values of a scala.collection.Map.
     * <p>
     * Only sequential operations will be efficient. 
     * For efficient parallel operation, use the longStreamAccumulatedValues method instead, but
     * note that this creates a new collection containing the Map's values.
     *
     * @param coll The Map to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStreamValues(scala.collection.Map<?, Long> coll) {
        scala.collection.Iterator iter = (scala.collection.Iterator)coll.valuesIterator();
        return StreamSupport.longStream(new StepsLongIterator(iter), false);
    }

    /** 
     * Generates a LongStream that traverses a long-valued scala.collection.Iterator.
     * <p>
     * Only sequential operations will be efficient.
     * For efficient parallel operation, use the longStreamAccumulated method instead,
     * but note that this creates a copy of the contents of the Iterator.
     *
     * @param coll The scala.collection.Iterator to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStream(scala.collection.Iterator<Long> coll) {
        return StreamSupport.longStream(new StepsLongIterator((scala.collection.Iterator)coll), false);
    }

    /** 
     * Generates a LongStream that traverses a long-valued scala.collection.Iterable.
     * <p>
     * Only sequential operations will be efficient.
     * For efficient parallel operation, use the longStreamAccumulated method instead,
     * but note that this creates a copy of the contents of the Iterable.
     *
     * @param coll The scala.collection.Iterable to traverse
     * @return     A LongStream view of the collection which, by default, executes sequentially.
     */
    public static LongStream longStream(scala.collection.Iterable<Long> coll) {
        scala.collection.Iterator iter = (scala.collection.Iterator)coll.iterator();        
        return StreamSupport.longStream(new StepsLongIterator(iter), false);
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
    public static LongStream longStreamAccumulated(scala.collection.IterableOnce<Long> coll) {
        scala.compat.java8.collectionImpl.LongAccumulator acc = 
          scala.compat.java8.collectionImpl.LongAccumulator.from((scala.collection.IterableOnce)coll);
        return StreamSupport.longStream(acc.spliterator(), false);
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
    public static LongStream longStreamAccumulatedKeys(scala.collection.Map<Long, ?> coll) {
        scala.compat.java8.collectionImpl.LongAccumulator acc = 
          scala.compat.java8.collectionImpl.LongAccumulator.from((scala.collection.Iterator)coll.keysIterator());
        return StreamSupport.longStream(acc.spliterator(), false);
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
    public static LongStream longStreamAccumulatedValues(scala.collection.Map<?, Long> coll) {
        scala.compat.java8.collectionImpl.LongAccumulator acc = 
          scala.compat.java8.collectionImpl.LongAccumulator.from((scala.collection.Iterator)coll.valuesIterator());
        return StreamSupport.longStream(acc.spliterator(), false);
    }
}
