package com.github.andre10dias.spring_rest_api.math;

import org.springframework.web.bind.annotation.PathVariable;

public class SimpleMath {

    public Double addition(Double a, Double b) {
        return a + b;
    }

    public Double subtraction(Double a, Double b) {
        return a - b;
    }

    public Double multiplication(Double a, Double b) {
        return a * b;
    }

    public Double division(Double a, Double b) {
        return a / b;
    }

    public Double average(Double a, Double b) {
        return (a + b) / 2;
    }

    public Double squareroot(Double a) {
        return Math.sqrt(a);
    }

}
