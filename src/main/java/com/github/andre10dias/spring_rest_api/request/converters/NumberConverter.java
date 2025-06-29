package com.github.andre10dias.spring_rest_api.request.converters;

import com.github.andre10dias.spring_rest_api.exception.ResourceNotFoundException;

public class NumberConverter {

    public static Double convertToDouble(String strNum) {
        if (isNumeric(strNum)) {
            return Double.parseDouble(strNum);
        }

        throw new ResourceNotFoundException("Please, set a numeric value.");
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null || strNum.isEmpty()) {
            throw new ResourceNotFoundException("String cannot be null or empty.");
        }
        String num = strNum.replace(",", ".");
        return num.matches("[-+]?\\d*\\.?\\d+");
    }

}
