public class Test {
    public static void main(String[] args) {
        new scala.runtime.F1<String, String>() {
            public String apply(String s) { return s; }
        };
        scala.runtime.F1<String, String> f1 = (String s) -> s;

        f1.apply("");

        scala.runtime.F1<Integer, Integer> f2 = (i) -> i;
        f2.apply(0);
        f2.apply$mcII$sp(0);

        scala.runtime.F1$mcII$sp f3 = (int i) -> i;

        f2.apply$mcII$sp(1);
        f2.apply(1);
    }
}
