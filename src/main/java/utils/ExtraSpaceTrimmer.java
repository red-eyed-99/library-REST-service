package utils;

public class ExtraSpaceTrimmer {

    public static String trim(String string) {
        return string
                .trim()
                .replaceAll("\\s{2,}", " ");
    }
}
