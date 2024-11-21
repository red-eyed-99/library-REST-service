package utils;

import java.util.Random;

public class RandomPhoneGenerator {

    private static final Random RANDOM = new Random();

    public static String generate() {
        var stringBuilder = new StringBuilder();

        stringBuilder
                .append("+7(")
                .append(RANDOM.nextInt(899) + 100)
                .append(")-")
                .append(RANDOM.nextInt(899) + 100)
                .append("-")
                .append(RANDOM.nextInt(89) + 10)
                .append("-")
                .append(RANDOM.nextInt(89) + 10);

        return stringBuilder.toString();
    }
}
