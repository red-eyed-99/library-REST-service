package utils;

import exceptions.BadRequestException;

public class RequestParamExtractor {

    public static String getIdFrom(String pathInfo) {

        if (pathInfo == null) {
            throw new BadRequestException("Missing id parameter");
        }

        return pathInfo.substring(1);
    }

    public static String[] getIdValuesFrom(String pathInfo) {
        final int MAX_PARTS_COUNT = 3;

        if (pathInfo == null) {
            throw new BadRequestException("Missing id parameter");
        }

        var pathInfoParts = pathInfo
                .substring(1)
                .split("/");

        if (pathInfoParts.length > MAX_PARTS_COUNT) {
            throw new BadRequestException("Invalid id parameter");
        }

        if (pathInfoParts.length == 1 || pathInfoParts.length == 2) {
            return new String[]{pathInfoParts[0]};
        }

        return new String[]{pathInfoParts[0], pathInfoParts[2]};

    }
}
