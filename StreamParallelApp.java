import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class StreamParallelApp {

	public static void main(String[] args) {
		// rangeClosed should outperform iterate.
		System.out.println(
				"Iterative sum done in: " + measureSumPerf(StreamParallelApp::iterativeSum, 10_000_000) + " msecs");
		System.out.println(
				"Sequential sum done in: " + measureSumPerf(StreamParallelApp::sequantialSum, 10_000_000) + " msecs");
		System.out.println(
				"Parallel sum done in: " + measureSumPerf(StreamParallelApp::parallelSum, 10_000_000) + " msecs");
		System.out.println(
				"RangedSum sum done in: " + measureSumPerf(StreamParallelApp::rangedSum, 10_000_000) + " msecs");
		System.out.println(
				"ParallelRangedSum sum done in: " + measureSumPerf(StreamParallelApp::parallelRangedSum, 10_000_000) + " msecs");
	}

	public static long iterativeSum(long n) {
		long result = 0;
		for (long i = 1L; i <= n; i++) {
			result += i;
		}
		return result;
	}

	public static long sequantialSum(long n) {
		return Stream.iterate(1L, i -> i + 1).limit(n).reduce(0L, Long::sum);
	}

	public static long parallelSum(long n) {
		return Stream.iterate(1L, i -> i + 1).limit(n).parallel().reduce(0L, Long::sum);
	}

	public static long rangedSum(long n) {
		return LongStream.rangeClosed(1, n).reduce(0L, Long::sum);
	}

	public static long parallelRangedSum(long n) {
		return LongStream.rangeClosed(1, n).parallel().reduce(0L, Long::sum);
	}

	public static long measureSumPerf(Function<Long, Long> adder, long n) {
		long fastest = Long.MAX_VALUE;
		for (int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			long sum = adder.apply(n);
			long duration = (System.nanoTime() - start) / 1_000_000;
			System.out.println("Result: " + sum);
			if (duration < fastest)
				fastest = duration;
		}
		return fastest;
	}

}
