package com.github.andre10dias.spring_rest_api.controller;

import com.github.andre10dias.spring_rest_api.exception.ResourceNotFoundException;
import com.github.andre10dias.spring_rest_api.math.SimpleMath;
import com.github.andre10dias.spring_rest_api.request.converters.NumberConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/math")
public class MathController {

    private final SimpleMath simpleMath = new SimpleMath();

    @RequestMapping("/addition/{numberOne}/{numberTwo}")
    public Double addition(
            @PathVariable("numberOne") String a,
            @PathVariable("numberTwo") String b) {
        if (!NumberConverter.isNumeric(a) || !NumberConverter.isNumeric(b)) {
            throw new ResourceNotFoundException("Please, set a numeric value");
        }

        Double num1 = NumberConverter.convertToDouble(a);
        Double num2 = NumberConverter.convertToDouble(b);
        return simpleMath.addition(num1, num2);
    }

    @RequestMapping("/subtraction/{numberOne}/{numberTwo}")
    public Double subtraction(
            @PathVariable("numberOne") String a,
            @PathVariable("numberTwo") String b) {
        if (!NumberConverter.isNumeric(a) || !NumberConverter.isNumeric(b)) {
            throw new ResourceNotFoundException("Please, set a numeric value");
        }

        Double num1 = NumberConverter.convertToDouble(a);
        Double num2 = NumberConverter.convertToDouble(b);
        return simpleMath.subtraction(num1, num2);
    }

    @RequestMapping("/multiplication/{numberOne}/{numberTwo}")
    public Double multiplication(
            @PathVariable("numberOne") String a,
            @PathVariable("numberTwo") String b) {
        if (!NumberConverter.isNumeric(a) || !NumberConverter.isNumeric(b)) {
            throw new ResourceNotFoundException("Please, set a numeric value");
        }

        Double num1 = NumberConverter.convertToDouble(a);
        Double num2 = NumberConverter.convertToDouble(b);
        return simpleMath.multiplication(num1, num2);
    }

    @RequestMapping("/division/{numberOne}/{numberTwo}")
    public Double division(
            @PathVariable("numberOne") String a,
            @PathVariable("numberTwo") String b) {
        if (!NumberConverter.isNumeric(a) || !NumberConverter.isNumeric(b)) {
            throw new ResourceNotFoundException("Please, set a numeric value");
        }

        Double num1 = NumberConverter.convertToDouble(a);
        Double num2 = NumberConverter.convertToDouble(b);
        return simpleMath.division(num1, num2);
    }

    @RequestMapping("/averange/{numberOne}/{numberTwo}")
    public Double averange(
            @PathVariable("numberOne") String a,
            @PathVariable("numberTwo") String b) {
        if (!NumberConverter.isNumeric(a) || !NumberConverter.isNumeric(b)) {
            throw new ResourceNotFoundException("Please, set a numeric value");
        }

        Double num1 = NumberConverter.convertToDouble(a);
        Double num2 = NumberConverter.convertToDouble(b);
        return simpleMath.average(num1, num2);
    }

    @RequestMapping("/squareoot/{number}")
    public Double squareroot(
            @PathVariable("number") String a) {
        if (!NumberConverter.isNumeric(a)) {
            throw new ResourceNotFoundException("Please, set a numeric value");
        }

        Double num1 = NumberConverter.convertToDouble(a);
        return simpleMath.squareroot(num1);
    }

}
