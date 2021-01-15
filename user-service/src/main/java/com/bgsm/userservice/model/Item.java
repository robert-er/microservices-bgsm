package com.bgsm.userservice.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@Entity(name = "items")
@Getter
@Setter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double minPlayers;
    private double maxPlayers;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id")
    private ItemCategory category;

    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @Builder
    public Item(String name, String description,
                double minPlayers, double maxPlayers,
                ItemCategory category, AppUser user) {
        this.name = name;
        this.description = description;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.category = category;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Double.compare(item.minPlayers, minPlayers) == 0 && Double.compare(item.maxPlayers, maxPlayers) == 0 &&
                Objects.equals(name, item.name) && Objects.equals(description, item.description) &&
                Objects.equals(category, item.category) &&
                Objects.equals(user, item.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, minPlayers, maxPlayers, category, user);
    }
}
