package br.com.neto.sparkwebpot;

import com.fasterxml.jackson.annotation.JsonInclude;
import spark.utils.StringUtils;

import java.util.List;

public record OneDTO(String name,
                     int age,
                     @JsonInclude(JsonInclude.Include.NON_EMPTY)
                     List<OneDTO> parents) {
    public OneDTO {
        if (StringUtils.isBlank(name)) throw new IllegalArgumentException("name must not be empty");
        if (age < 1) throw new IllegalArgumentException("age must be greater than 0");
        if (parents != null && !parents.isEmpty() && parents.size() > 2)
            throw new IllegalArgumentException("parents size cannnot be greater than 2");
    }

    public OneDTO(String name, int age) {
        this(name, age, null);
    }
}
