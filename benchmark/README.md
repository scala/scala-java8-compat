# Benchmark suite for Java 8 Streams compatibility layer

This project is intended to support semi-manual benchmarking of the Java 8 streams compatibility layer in Scala collections.

Because the benchmarking is **very computationally expensive** it should be done occasionally, not automatically.

## Code generation step

1. Run `sbt console`

2. If the `JmhBench.scala` file already exists, delete it.

3. Enter `bench.codegen.Generate.jmhBench()` to generate the `JmhBench.scala` file.

## Benchmarking step

1. Make sure your terminal has plenty of lines of scrollback.  (A couple thousand should do.)

2. Run `sbt`

3. Enter `jmh:run -i 5 -wi 3 -f5`.  Wait overnight.

4. Clip off the last set of lines from the terminal window starting before the line that contains `[info] # Run complete. Total time:` and including that line until the end.

5. Save that in the file `results/jmhbench.log`

## Comparison step

1. Run `sbt console`

2. Enter `bench.examine.SpeedReports()`

3. Look at the ASCII art results showing speed comparisons.
