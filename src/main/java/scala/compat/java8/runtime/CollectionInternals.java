package scala.compat.java8.runtime;

public class CollectionInternals {
    public static <A> Object[] getTable(scala.collection.mutable.FlatHashTable<A> fht) { return fht.hashTableContents().table(); }
    public static <A> Object[] getDisplay0(scala.collection.immutable.Vector<A> v) { return v.display0(); }
    public static <A> Object[] getDisplay1(scala.collection.immutable.Vector<A> v) { return v.display1(); }
    public static <A> Object[] getDisplay2(scala.collection.immutable.Vector<A> v) { return v.display2(); }
    public static <A> Object[] getDisplay3(scala.collection.immutable.Vector<A> v) { return v.display3(); }
    public static <A> Object[] getDisplay4(scala.collection.immutable.Vector<A> v) { return v.display4(); }
    public static <A> Object[] getDisplay5(scala.collection.immutable.Vector<A> v) { return v.display5(); }
}
