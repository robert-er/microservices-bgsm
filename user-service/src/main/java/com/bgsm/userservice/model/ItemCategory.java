package com.bgsm.userservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            cascade = {CascadeType.MERGE,CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    @Builder
    public ItemCategory(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCategory that = (ItemCategory) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
