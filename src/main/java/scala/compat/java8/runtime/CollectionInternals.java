package scala.compat.java8.runtime;

// No imports! All type names are fully qualified to avoid confusion!

public class CollectionInternals {
    public static <A> Object[] getTable(scala.collection.mutable.FlatHashTable<A> fht) { return fht.hashTableContents().table(); }
    public static <A, E extends scala.collection.mutable.HashEntry<A,E>> scala.collection.mutable.HashEntry<A, E>[] getTable(scala.collection.mutable.HashTable<A,E> ht) { return ht.hashTableContents().table(); }
    public static <A> Object[] getDisplay0(scala.collection.immutable.Vector<A> v) { return v.display0(); }
    public static <A> Object[] getDisplay1(scala.collection.immutable.Vector<A> v) { return v.display1(); }
    public static <A> Object[] getDisplay2(scala.collection.immutable.Vector<A> v) { return v.display2(); }
    public static <A> Object[] getDisplay3(scala.collection.immutable.Vector<A> v) { return v.display3(); }
    public static <A> Object[] getDisplay4(scala.collection.immutable.Vector<A> v) { return v.display4(); }
    public static <A> Object[] getDisplay5(scala.collection.immutable.Vector<A> v) { return v.display5(); }
    public static <A> scala.Tuple2< scala.Tuple2< scala.collection.Iterator<A>, Object >, scala.collection.Iterator<A> > trieIteratorSplit(scala.collection.Iterator<A> it) {
        if (it instanceof scala.collection.immutable.TrieIterator) {
            scala.collection.immutable.TrieIterator<A> trie = (scala.collection.immutable.TrieIterator<A>)it;
            return trie.split();
        }
        return null;
    }
    public static long[] getBitSetInternals(scala.collection.mutable.BitSet bitSet) { return bitSet.elems(); }
}

