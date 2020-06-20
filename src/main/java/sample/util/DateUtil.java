package sample.util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * description: <一句话功能简述>
 *
 * @author Chenz
 * @date 2020/6/12
 */
public class DateUtil {

    private static final String DATE_PATTERN = "dd.MM.yyyy";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static String fomat(LocalDate date) {
        if (date == null) return null;
        return DATE_TIME_FORMATTER.format(date);
    }

    public static LocalDate parse(String str) {
        try {
            return DATE_TIME_FORMATTER.parse(str, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static boolean validDate(String str) {
        return parse(str) != null;
    }

}
