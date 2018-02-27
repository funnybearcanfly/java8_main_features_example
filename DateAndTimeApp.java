import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

public class DateAndTimeApp {

	private static void useLocalDate() {
		LocalDate date = LocalDate.of(2018, 1, 29);
		int year = date.getYear();
		Month month = date.getMonth();
		int day = date.getDayOfMonth();
		DayOfWeek dow = date.getDayOfWeek();
		int len = date.lengthOfMonth();
		boolean leap = date.isLeapYear();

		LocalDate today = LocalDate.now();

		// Reading LocalDate values using a TemporalField.
		year = date.get(ChronoField.YEAR);
		int monthValue = date.get(ChronoField.MONTH_OF_YEAR);
		day = date.get(ChronoField.DAY_OF_MONTH);
	}

	private static void useLocalDateTime() {
		LocalDate date = LocalDate.parse("2014-03-18");
		LocalTime time = LocalTime.parse("13:45:20");

		LocalDateTime dt1 = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45, 20);
		LocalDateTime dt2 = LocalDateTime.of(date, time);
		LocalDateTime dt3 = date.atTime(13, 45, 20);
		LocalDateTime dt4 = date.atTime(time);
		LocalDateTime dt5 = time.atDate(date);
	}

	private static void useInstant() {
		// This is for machine to use.
		Instant.ofEpochSecond(3, 1_000_000_000);
	}

	private static void useDurationPeriod() {
		Duration threeMinutes = Duration.ofMinutes(3);
		threeMinutes = Duration.of(3, ChronoUnit.MINUTES);
		Period tenDays = Period.between(LocalDate.of(2014, 3, 8), LocalDate.of(2014, 3, 18));
		tenDays = Period.ofDays(10);
		Period threeWeeks = Period.ofWeeks(3);
		Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);
	}

	private static void useTemporalAdjusters() {
		LocalDate date1 = LocalDate.of(2018, 1, 29);
		LocalDate date2 = date1.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
		LocalDate date3 = date2.with(TemporalAdjusters.lastDayOfMonth());
		LocalDate date4 = date3.with(new NextWorkingDay());

		// Temporary adjuster created by lambda.
		TemporalAdjuster nextWorkingDay = TemporalAdjusters.ofDateAdjuster(temporal -> {
			DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
			int dayToAdd = 1;
			if (dow == DayOfWeek.FRIDAY)
				dayToAdd = 3;
			if (dow == DayOfWeek.SATURDAY)
				dayToAdd = 2;
			return temporal.plus(dayToAdd, ChronoUnit.DAYS);
		});
		date4 = date3.with(nextWorkingDay);
	}

	private static class NextWorkingDay implements TemporalAdjuster {
		@Override
		public Temporal adjustInto(Temporal temporal) {
			DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
			int dayToAdd = 1;
			if (dow == DayOfWeek.FRIDAY)
				dayToAdd = 3;
			else if (dow == DayOfWeek.SATURDAY)
				dayToAdd = 2;
			return temporal.plus(dayToAdd, ChronoUnit.DAYS);
		}
	}

	private static void printingAndParsing() {
		LocalDate date = LocalDate.of(2018, 1, 29);
		LocalDateTime dateTime = LocalDateTime.of(2018, Month.MARCH, 18, 13, 45);
		System.out.println(date.format(DateTimeFormatter.BASIC_ISO_DATE));
		System.out.println(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
		System.out.println(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

		LocalDate date1 = LocalDate.parse("20140318", DateTimeFormatter.BASIC_ISO_DATE);
		LocalDate date2 = LocalDate.parse("2014-03-18", DateTimeFormatter.ISO_LOCAL_DATE);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate date3 = LocalDate.of(2018, 1, 29);
		System.out.println(date3.format(formatter));
		LocalDate date4 = LocalDate.parse(date3.format(formatter), formatter);

		DateTimeFormatter italianFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ITALIAN);
		LocalDate date5 = LocalDate.of(2018, 1, 29);
		System.out.println(date5.format(italianFormatter));

		DateTimeFormatter italianFormatter2 = new DateTimeFormatterBuilder().appendText(ChronoField.DAY_OF_MONTH)
				.appendLiteral(". ").appendText(ChronoField.MONTH_OF_YEAR).appendLiteral(" ")
				.appendText(ChronoField.YEAR).parseCaseInsensitive().toFormatter(Locale.ITALIAN);
	}

	private static void useTimeZone() {
		ZoneId romeZone = ZoneId.of("Europe/Rome");
		ZoneId shZone = ZoneId.of("Asia/Shanghai");
		LocalDate date = LocalDate.of(2014, Month.MARCH, 18);
		ZonedDateTime zdt1 = date.atStartOfDay(romeZone);
		LocalDateTime dateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
		ZonedDateTime zdt2 = dateTime.atZone(romeZone);
		Instant instant = Instant.now();
		ZonedDateTime zdt3 = instant.atZone(romeZone);
		System.out.println(zdt3);
		ZonedDateTime zdt4 = zdt3.withZoneSameInstant(shZone);
		System.out.println(zdt4);
		
		ZoneOffset newYorkOffset = ZoneOffset.of("-05:00");
		LocalDateTime dt = LocalDateTime.of(2018, Month.MARCH, 18, 13, 45);
		OffsetDateTime dateTimeInNewYork = OffsetDateTime.of(dt, newYorkOffset);
		System.out.println(dateTimeInNewYork);
	}

	public static void main(String[] args) {
		printingAndParsing();
		useTimeZone();
	}

}
