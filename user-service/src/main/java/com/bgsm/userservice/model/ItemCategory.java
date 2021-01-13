package com.bgsm.userservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@Entity(name = "item_categories")
@Getter
@Setter
@NoArgsConstructor
public class ItemCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(targetEntity = Item.class,
            mappedBy = "category",
            cascade = CascadeType.MERGE,
            fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    @Builder
    public ItemCategory(String name) {
        this.name = name;
    }
}
