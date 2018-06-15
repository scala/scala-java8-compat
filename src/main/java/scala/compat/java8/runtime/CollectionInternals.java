package scala.compat.java8.runtime;

// No imports! All type names are fully qualified to avoid confusion!

public class CollectionInternals {
    public static <A> Object[] getTable(scala.collection.mutable.HashSet<A> hs) { return hs.getTable().table(); }
    public static <A> Object[] getTable(scala.collection.mutable.LinkedHashSet<A> hm) { return hm.getTable().table(); }

    public static <A, B> Object[] getTable(scala.collection.mutable.HashMap<A, B> hm) { return hm.getTable().table(); }
    public static <A, B> Object[] getTable(scala.collection.mutable.LinkedHashMap<A, B> hm) { return hm.getTable().table(); }

    public static <K> K hashEntryKey(Object hashEntry) { return ((scala.collection.mutable.HashEntry<K, ?>)hashEntry).key(); }
    public static Object hashEntryNext(Object hashEntry) { return ((scala.collection.mutable.HashEntry<?, ?>)hashEntry).next(); }
    public static <V> V linkedEntryValue(Object hashEntry) { return ((scala.collection.mutable.LinkedHashMap.LinkedEntry<?, V>)hashEntry).value(); }
    public static <V> V defaultEntryValue(Object hashEntry) { return ((scala.collection.mutable.DefaultEntry<?, V>)hashEntry).value(); }

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

