package scala.compat.java8.runtime;

public class CollectionInternals {
    public static <A> Object[] getTable(scala.collection.mutable.FlatHashTable<A> fht) { return fht.hashTableContents().table(); }
}
