package kr.co.direa.workspace.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Lunch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int weight; // 가중치

    @Builder
    public Lunch(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }
}
