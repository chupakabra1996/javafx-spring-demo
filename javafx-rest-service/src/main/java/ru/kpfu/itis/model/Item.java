package ru.kpfu.itis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

// lombok
@NoArgsConstructor
@Getter
@Setter

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String name;

    @Min(0)
    @Column(nullable = false)
    private double price;


    public Item(@NotEmpty String name, @Min(0) double price) {
        this.name = name;
        this.price = price;
    }
}
