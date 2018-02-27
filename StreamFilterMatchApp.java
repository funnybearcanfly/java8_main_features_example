import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamFilterMatchApp {

	static class Trader {
		private final String name;
		private final String city;

		public Trader(String n, String c) {
			this.name = n;
			this.city = c;
		}

		public String getName() {
			return this.name;
		}

		public String getCity() {
			return this.city;
		}

		public String toString() {
			return "Trader:" + this.name + " in " + this.city;
		}
	}

	static class Transaction {
		private final Trader trader;
		private final int year;
		private final int value;

		public Transaction(Trader trader, int year, int value) {
			this.trader = trader;
			this.year = year;
			this.value = value;
		}

		public Trader getTrader() {
			return this.trader;
		}

		public int getYear() {
			return this.year;
		}

		public int getValue() {
			return this.value;
		}

		public String toString() {
			return "{" + this.trader + ", " + "year: " + this.year + ", " + "value:" + this.value + "}";
		}
	}

	public static void main(String[] args) {
		Trader raoul = new Trader("Raoul", "Cambridge");
		Trader mario = new Trader("Mario", "Milan");
		Trader alan = new Trader("Alan", "Cambridge");
		Trader brian = new Trader("Brian", "Cambridge");
		List<Transaction> transactions = Arrays.asList(new Transaction(brian, 2011, 300),
				new Transaction(raoul, 2012, 1000), new Transaction(raoul, 2011, 400),
				new Transaction(mario, 2012, 710), new Transaction(mario, 2012, 700), new Transaction(alan, 2012, 950));

		List<Transaction> ts2011 = transactions.stream().filter(t -> t.getYear() == 2011)
				.sorted(Comparator.comparing(Transaction::getValue)).collect(Collectors.toList());
		System.out.println(ts2011);

		List<String> cities = transactions.stream().map(t -> t.getTrader().getCity()).distinct()
				.collect(Collectors.toList());
		System.out.println(cities);

		List<Trader> traders = transactions.stream().map(Transaction::getTrader).filter(t -> t.getCity() == "Cambridge")
				.sorted(Comparator.comparing(Trader::getName)).collect(Collectors.toList());
		System.out.println(traders);

		String allNames = transactions.stream().map(t -> t.getTrader().getName()).distinct().sorted().reduce("",
				(t1, t2) -> t1 + t2);
		System.out.println(allNames);

		// This is more efficient, because it uses StringBuilder internally.
		String allNames2 = transactions.stream().map(t -> t.getTrader().getName()).distinct().sorted()
				.collect(Collectors.joining());
		System.out.println(allNames2);

		boolean milanBased = transactions.stream().anyMatch(t -> t.getTrader().getCity() == "Milan");
		System.out.println(milanBased);

		transactions.stream().filter(t -> t.getTrader().getCity() == "Cambridge").map(Transaction::getValue)
				.forEach(System.out::println);

		Optional<Integer> maxValue = transactions.stream().map(Transaction::getValue).max(Integer::compare);
		System.out.println(maxValue);
		
		Optional<Transaction> minTransaction = transactions.stream().min(Comparator.comparing(Transaction::getValue));
		System.out.println(minTransaction);
	}

}
