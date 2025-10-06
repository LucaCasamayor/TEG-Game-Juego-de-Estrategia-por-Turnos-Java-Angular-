package ar.edu.utn.frc.tup.piii.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private TerritoryStateEntity territory1;

    @ManyToOne
    @JoinColumn
    private TerritoryStateEntity territory2;

    @ManyToOne
    @JoinColumn
    private GameEntity game;

    @Column
    private boolean isActive;

}
