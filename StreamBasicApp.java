import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class StreamBasicApp {

	static class Dish {
		private final String name;
		private final boolean vegetarian;
		private final int calories;
		private final Type type;

		public Dish(String name, boolean vegetarian, int calories, Type type) {
			this.name = name;
			this.vegetarian = vegetarian;
			this.calories = calories;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public boolean isVegetarian() {
			return vegetarian;
		}

		public int getCalories() {
			return calories;
		}

		public Type getType() {
			return type;
		}

		@Override

		public String toString() {
			return name;
		}

		public enum Type {
			MEAT, FISH, OTHER
		}
	}

	public static void main(String[] args) {
		List<Dish> menu = Arrays.asList(new Dish("pork", false, 800, Dish.Type.MEAT),
				new Dish("beef", false, 700, Dish.Type.MEAT), new Dish("chicken", false, 400, Dish.Type.MEAT),
				new Dish("french fries", true, 530, Dish.Type.OTHER), new Dish("rice", true, 350, Dish.Type.OTHER),
				new Dish("season fruit", true, 120, Dish.Type.OTHER), new Dish("pizza", true, 550, Dish.Type.OTHER),
				new Dish("prawns", false, 300, Dish.Type.FISH), new Dish("salmon", false, 450, Dish.Type.FISH));

		List<String> names = menu.stream().filter(d -> {
			System.out.println("filtering " + d.getName());
			return d.getCalories() > 500;
		}).map(d -> {
			System.out.println("mapping " + d.getName());
			return d.getName();
		}).limit(3).collect(Collectors.toList());

		System.out.println("Ready to print names...");
		System.out.println(names);
		System.out.println(names.getClass());

		menu.stream().filter(Dish::isVegetarian).findAny().ifPresent((p) -> System.out.println(p.getName()));

		int calories = menu.stream().mapToInt(Dish::getCalories).sum();
		System.out.println(calories);

		OptionalInt maxCalories = menu.stream().mapToInt(Dish::getCalories).max();
		System.out.println(maxCalories);
		
		int max = maxCalories.orElse(1);
		System.out.println(max);
	}

}
