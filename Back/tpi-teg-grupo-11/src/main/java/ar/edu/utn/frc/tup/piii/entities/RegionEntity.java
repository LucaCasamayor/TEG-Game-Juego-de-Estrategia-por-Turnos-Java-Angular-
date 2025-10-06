package ar.edu.utn.frc.tup.piii.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "regions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long regionId;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "number_of_territories")
    private int numberOfTerritories;

    @ManyToOne
    @JoinColumn(name = "game_map_id")
    private GameMapEntity map;
}