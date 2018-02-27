import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class StreamCollectingApp {

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

	static enum CaloricLevel {
		DIET, NORMAL, FAT
	}

	public static void main(String[] args) {
		List<Dish> menu = Arrays.asList(new Dish("pork", false, 800, Dish.Type.MEAT),
				new Dish("beef", false, 700, Dish.Type.MEAT), new Dish("chicken", false, 400, Dish.Type.MEAT),
				new Dish("french fries", true, 530, Dish.Type.OTHER), new Dish("rice", true, 350, Dish.Type.OTHER),
				new Dish("season fruit", true, 120, Dish.Type.OTHER), new Dish("pizza", true, 550, Dish.Type.OTHER),
				new Dish("prawns", false, 300, Dish.Type.FISH), new Dish("salmon", false, 450, Dish.Type.FISH));

		Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
		Optional<Dish> mostCalorieDish = menu.stream().collect(Collectors.maxBy(dishCaloriesComparator));
		System.out.println(mostCalorieDish);

		int totalCalories = menu.stream().collect(Collectors.summingInt(Dish::getCalories));
		System.out.println(totalCalories);

		double avgCalories = menu.stream().collect(Collectors.averagingInt(Dish::getCalories));
		System.out.println(avgCalories);

		IntSummaryStatistics menuStatistics = menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
		System.out.println(menuStatistics);

		String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining(","));
		System.out.println(shortMenu);

		int totalCalories2 = menu.stream().collect(Collectors.reducing(0, Dish::getCalories, (i, j) -> i + j));
		System.out.println(totalCalories2);

		Optional<Dish> mostCalorieDish2 = menu.stream()
				.collect(Collectors.reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
		System.out.println(mostCalorieDish2);

		Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(Collectors.groupingBy(Dish::getType));
		System.out.println(dishesByType);

		Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(Collectors.groupingBy(dish -> {
			if (dish.getCalories() <= 400)
				return CaloricLevel.DIET;
			else if (dish.getCalories() <= 700)
				return CaloricLevel.NORMAL;
			else
				return CaloricLevel.FAT;
		}));
		System.out.println(dishesByCaloricLevel);

		Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeAndCaloricLevel = menu.stream()
				.collect(Collectors.groupingBy(Dish::getType, Collectors.groupingBy(dish -> {
					if (dish.getCalories() <= 400)
						return CaloricLevel.DIET;
					else if (dish.getCalories() <= 700)
						return CaloricLevel.NORMAL;
					else
						return CaloricLevel.FAT;
				})));
		System.out.println(dishesByTypeAndCaloricLevel);

		Map<Dish.Type, Long> typesCount = menu.stream()
				.collect(Collectors.groupingBy(Dish::getType, Collectors.counting()));
		System.out.println(typesCount);

		Map<Dish.Type, Optional<Dish>> mostCaloricByType = menu.stream().collect(
				Collectors.groupingBy(Dish::getType, Collectors.maxBy(Comparator.comparingInt(Dish::getCalories))));
		System.out.println(mostCaloricByType);

		Map<Dish.Type, Integer> totalCaloriesByType = menu.stream()
				.collect(Collectors.groupingBy(Dish::getType, Collectors.summingInt(Dish::getCalories)));
		System.out.println(totalCaloriesByType);

		Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType = menu.stream()
				.collect(Collectors.groupingBy(Dish::getType, Collectors.mapping(dish -> {
					if (dish.getCalories() <= 400)
						return CaloricLevel.DIET;
					else if (dish.getCalories() <= 700)
						return CaloricLevel.NORMAL;
					else
						return CaloricLevel.FAT;
				}, Collectors.toSet())));
		System.out.println(caloricLevelsByType);
		
		Map<Boolean, List<Dish>> ss = menu.stream()
				.collect(Collectors.groupingBy(Dish::isVegetarian));
		System.out.println(ss);
	}

}
