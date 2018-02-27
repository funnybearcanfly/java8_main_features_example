
public class InterfaceDefaultMethodConflictsApp {
	public static interface A {
		default void hello() {
			System.out.println("Hello from A");
		}
	}

	public static interface B {
		default void hello() {
			System.out.println("Hello from B");
		}
	}

	static class C implements B, A {
		public static void run() {
			new C().hello();
		}

		@Override
		public void hello() {
			A.super.hello();
		}
	}

	public static void main(String... args) {
		C.run();
	}
}
