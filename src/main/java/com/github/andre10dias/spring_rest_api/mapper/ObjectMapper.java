package com.github.andre10dias.spring_rest_api.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.List;

public final class ObjectMapper {

    private static final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    private ObjectMapper() {
        // Evita instanciamento
    }

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListObject(List<O> origin, Class<D> destination) {
        return origin.stream()
                .map(o -> mapper.map(o, destination))
                .toList(); // Java 16+ ou use collect(Collectors.toList()) no Java 8
    }
}

