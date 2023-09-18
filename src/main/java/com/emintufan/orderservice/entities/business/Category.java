package com.emintufan.orderservice.entities.business;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@Table(name = "category")
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name",unique = true)
    private String name;
}
