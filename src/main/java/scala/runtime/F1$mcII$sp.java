package scala.runtime;

@FunctionalInterface
public interface F1$mcII$sp extends F1 {
    abstract int apply$mcII$sp(int v1);

    default Object apply(Object s) { return (Integer) apply$mcII$sp((Integer) s); }
}
