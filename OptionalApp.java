import java.util.Optional;

public class OptionalApp {

	static class Person {
		private Optional<Car> car;
		private int age;

		public Optional<Car> getCar() {
			return car;
		}

		public int getAge() {
			return age;
		}
	}

	static class Car {
		private Optional<Insurance> insurance;

		public Optional<Insurance> getInsurance() {
			return insurance;
		}
	}

	static class Insurance {
		private String name;
		private double price;

		public String getName() {
			return name;
		}

		public double getPrice() {
			return price;
		}
	}

	public static String getCarInsuranceName(Person person) {
		Optional<Person> optPerson = Optional.of(person);
		Optional<String> name = optPerson.flatMap(Person::getCar).flatMap(Car::getInsurance).map(Insurance::getName);
		return name.orElse("Unkown");
	}

	public String getCarInsuranceName(Optional<Person> person, int minAge) {
		return person.filter(p -> p.getAge() >= minAge).flatMap(Person::getCar).flatMap(Car::getInsurance)
				.map(Insurance::getName).orElse("Unknown");
	}

	public static void main(String[] args) {

	}

}
