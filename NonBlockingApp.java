import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NonBlockingApp {
	private static final Random random = new Random();

	private static class Shop {
		private String name;
		private Random random;

		public Shop(String name) {
			this.name = name;
			this.random = new Random();
		}

		private double calculatePrice(String product) {
			delay();
			return random.nextDouble() * product.charAt(0) + product.charAt(1);
		}

		public String getPrice(String product) {
			double price = calculatePrice(product);
			Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
			return String.format("%s:%.2f:%s", name, price, code);
		}

		public CompletableFuture<String> getPriceAsync(String product) {
			// This uses ForkJoinPool by default. Custom executor can be passed
			// in as second parameter.
			return CompletableFuture.supplyAsync(() -> getPrice(product));
		}
	}

	private static class Quote {
		private final String shopName;
		private final double price;
		private final Discount.Code discountCode;

		public Quote(String shopName, double price, Discount.Code code) {
			this.shopName = shopName;
			this.price = price;
			this.discountCode = code;
		}

		public static Quote parse(String s) {
			String[] split = s.split(":");
			String shopName = split[0];
			double price = Double.parseDouble(split[1]);
			Discount.Code discountCode = Discount.Code.valueOf(split[2]);
			return new Quote(shopName, price, discountCode);
		}

		public String getShopName() {
			return shopName;
		}

		public double getPrice() {
			return price;
		}

		public Discount.Code getDiscountCode() {
			return discountCode;
		}
	}

	private static class Discount {
		public enum Code {
			NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);
			private final int percentage;

			Code(int percentage) {
				this.percentage = percentage;
			}
		}

		public static String applyDiscount(Quote quote) {
			return quote.getShopName() + " price is " + Discount.apply(quote.getPrice(), quote.getDiscountCode());
		}

		private static double apply(double price, Code code) {
			delay();
			return price * (100 - code.percentage) / 100;
		}
	}

	private static enum Money {
		EUR, USD;

		public static double getRate(Money from, Money to) {
			delay();
			if (from == EUR && to == USD) {
				return 1.24;
			}
			return 0;
		}
	}

	private static void delay() {
		int delay = 500 + random.nextInt(2000);
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private static void getPriceFromOneShop() {
		Shop shop = new Shop("BestShop");
		Future<String> futurePrice = shop.getPriceAsync("my favorate product");
		// do something else.
		try {
			String price = futurePrice.get();
			System.out.println(price);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void getPriceFromMultiShops() {
		List<Shop> shops = Arrays.asList(new Shop("BestPrice"), new Shop("LetsSaveBig"), new Shop("MyFavoriteShop"),
				new Shop("BuyItAll"));
		List<CompletableFuture<String>> priceFutures = shops.stream().map(shop -> shop.getPriceAsync("iphone27"))
				.map(future -> future.thenApply(Quote::parse))
				.map(future -> future
						.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote))))
				.collect(Collectors.toList());
		List<String> prices = priceFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
		System.out.println(prices);
	}

	private static void getPriceWithRate() {
		Shop shop = new Shop("BestPrice");
		Double price = CompletableFuture.supplyAsync(() -> shop.getPrice("my favorate product"))
				.thenCombine(CompletableFuture.supplyAsync(() -> Money.getRate(Money.EUR, Money.USD)),
						(p, r) -> Double.parseDouble(p.split(":")[1]) * r)
				.join();
		System.out.println(price);
	}

	public static void findPricesStream() {
		List<Shop> shops = Arrays.asList(new Shop("BestPrice"), new Shop("LetsSaveBig"), new Shop("MyFavoriteShop"),
				new Shop("BuyItAll"));
		Stream<CompletableFuture<String>> priceStream = shops.stream().map(shop -> shop.getPriceAsync("iphone27"))
				.map(future -> future.thenApply(Quote::parse)).map(future -> future
						.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote))));
		CompletableFuture[] futures = priceStream.map(f -> f.thenAccept(System.out::println))
				.toArray(size -> new CompletableFuture[size]);
		CompletableFuture.allOf(futures).join();
	}

	public static void main(String[] args) {
		getPriceFromOneShop();
		getPriceFromMultiShops();
		getPriceWithRate();
		findPricesStream();
	}
}
