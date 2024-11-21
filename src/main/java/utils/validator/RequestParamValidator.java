package utils.validator;

import exceptions.BadRequestException;
import utils.ExtraSpaceTrimmer;

public class RequestParamValidator {

    private static final int NAME_LENGTH_CONSTRAINT = 30;
    private static final int TITLE_LENGTH_CONSTRAINT = 255;
    private static final int YEAR_LENGTH_CONSTRAINT = 4;
    private static final int REVIEW_LENGTH_CONSTRAINT = 500;

    public static void validateId(String parameterValue) {

        if (parameterValue == null || parameterValue.isBlank()) {
            throw new BadRequestException("Missing id parameter");
        }

        try {
            var id = Long.parseLong(parameterValue);

            if (id <= 0) {
                throw new BadRequestException("id must be a positive number");
            }

        } catch (NumberFormatException e) {
            throw new BadRequestException("id must be a number");
        }
    }

    public static void validateIdValues(String[] parameterValues) {

        if (parameterValues == null) {
            throw new BadRequestException("Missing authors id parameter");
        }

        for (var id : parameterValues) {
            validateId(id);
        }
    }

    public static void validateName(String parameterName, String parameterValue) {
        checkNullOrBlank(parameterName, parameterValue);

        parameterValue = ExtraSpaceTrimmer.trim(parameterValue);

        if (parameterValue.length() > NAME_LENGTH_CONSTRAINT) {
            throw new BadRequestException(parameterName + " parameter must be no more than " + NAME_LENGTH_CONSTRAINT + " characters");
        }

        if (!containsOnlyLetters(parameterValue)) {
            throw new BadRequestException(parameterName + " parameter must contain only letters");
        }
    }

    public static void validateTitle(String parameterName, String parameterValue) {
        checkNullOrBlank(parameterName, parameterValue);

        parameterValue = ExtraSpaceTrimmer.trim(parameterValue);

        if (parameterValue.length() > TITLE_LENGTH_CONSTRAINT) {
            throw new BadRequestException(parameterName + " parameter must be no more than " + TITLE_LENGTH_CONSTRAINT + " characters");
        }

        if (!titleIsValid(parameterValue)) {
            throw new BadRequestException("Only letters and numbers are allowed in the " + parameterName + " parameter");
        }
    }

    public static void validateYear(String parameterName, String parameterValue) {
        checkNullOrBlank(parameterName, parameterValue);

        if (parameterValue.length() != YEAR_LENGTH_CONSTRAINT) {
            throw new BadRequestException(parameterName + " parameter must contain only 4 numbers");
        }

        try {
            var year = Integer.parseInt(parameterValue);

            if (year <= 0) {
                throw new BadRequestException(parameterName + " must be a positive number");
            }

        } catch (NumberFormatException e) {
            throw new BadRequestException("Incorrect " + parameterName + " parameter");
        }
    }

    public static void validatePhone(String parameterName, String parameterValue) {
        checkNullOrBlank(parameterName, parameterValue);

        if (!parameterValue.matches("^\\+7\\(\\d{3}\\)-\\d{3}-\\d{2}-\\d{2}$")) {
            throw new BadRequestException("The phone number must be in this format: +7(xxx)-xxx-xx-xx");
        }
    }

    public static void validateContent(String parameterName, String parameterValue) {
        checkNullOrBlank(parameterName, parameterValue);

        parameterValue = ExtraSpaceTrimmer.trim(parameterValue);

        if (parameterValue.length() > REVIEW_LENGTH_CONSTRAINT) {
            throw new BadRequestException(parameterName + " parameter must be no more than " + REVIEW_LENGTH_CONSTRAINT + " characters");
        }
    }

    private static void checkNullOrBlank(String parameterName, String parameterValue) {
        if (parameterValue == null || parameterValue.isBlank()) {
            throw new BadRequestException("Missing " + parameterName + " parameter");
        }
    }

    private static boolean containsOnlyLetters(String parameterValue) {
        var chars = parameterValue.toCharArray();

        for (var symbol : chars) {
            if (!Character.isLetter(symbol)) {
                return false;
            }
        }

        return true;
    }

    private static boolean titleIsValid(String parameterValue) {
        var chars = parameterValue.toCharArray();

        for (var symbol : chars) {
            if (!Character.isLetter(symbol) && !Character.isDigit(symbol) && !Character.isSpaceChar(symbol)) {
                return false;
            }
        }

        return true;
    }
}
