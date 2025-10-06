package ar.edu.utn.frc.tup.piii.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "games_maps")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameMapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "game_map_id")
    private Long gameMapId;

    @Column(nullable = false, name = "name")
    private String name;

    @Column
    private String image;
}