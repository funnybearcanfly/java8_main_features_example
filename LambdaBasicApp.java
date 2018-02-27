import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaBasicApp {

	interface MyRunnable {
		void run();
	}

	private LambdaBasicApp.MyRunnable runnable;

	private int a = 10;

	public static void main(String[] args) {

		LambdaBasicApp app = new LambdaBasicApp();

		app.runnable = () -> {
			System.out.println("Lambda" + app.a);
		};
		app.runnable.run();
		System.out.println(app.runnable.getClass());

		app.runnable = LambdaBasicApp::staticDisplay;
		app.runnable.run();
		System.out.println(app.runnable.getClass());

		app.runnable = app::instanceDisplay;
		app.runnable.run();
		System.out.println(app.runnable.getClass());

		BiPredicate<List<String>, String> contains = List::contains;

		Predicate<Integer> pred1 = (p) -> p > 10;
		Predicate<Integer> pred2 = (p) -> p < 100;
		Predicate<Integer> pred12 = pred1.and(pred2);

		List<double[]> pythagoreanTriples = IntStream.rangeClosed(1, 100).boxed()
				.flatMap(a -> IntStream.rangeClosed(a, 100)
						.mapToObj(b -> new double[] { a, b, Math.sqrt(a * a + b * b) }).filter(t -> t[2] % 1 == 0))
				.collect(Collectors.toList());
		System.out.println(Arrays.deepToString(pythagoreanTriples.toArray()));

		Stream.iterate(0, n -> n + 2).limit(10).forEach(System.out::println);
	}

	private static void staticDisplay() {
		System.out.println("Method Reference to static");
	}

	private void instanceDisplay() {
		System.out.println("Method Reference to instance");
	}

}
