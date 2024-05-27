package com.ecommerce.library.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Pay {

    private Double price;
    private String currency;
    private String method;
    private String intent;
    private String description;
}
