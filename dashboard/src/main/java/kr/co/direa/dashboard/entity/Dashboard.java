package kr.co.direa.dashboard.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Table(name = "dashboard")
@Entity
@NoArgsConstructor
public class Dashboard extends BaseTimeEntity{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private UUID userId;

    @OneToMany(mappedBy = "dashboard", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Widget> widgets = new ArrayList<>();

    @Builder
    public Dashboard(UUID userId) {
        this.userId = userId;
    }
}
