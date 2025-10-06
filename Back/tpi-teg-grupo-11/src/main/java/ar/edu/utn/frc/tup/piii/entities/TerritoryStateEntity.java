package ar.edu.utn.frc.tup.piii.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "TerritoriesStates")
@AllArgsConstructor
@NoArgsConstructor
public class TerritoryStateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long territoryStateId;

//    @ManyToOne
//    @JoinColumn
//    private TurnEntity turn;

    @ManyToOne
    @JoinColumn(name = "territory_id")
    private TerritoryEntity territoryEntity;

    @ManyToOne
    @JoinColumn(name = "player_id")
    @JsonBackReference
    private PlayerEntity player;

    @Column
    private int armyCount;

//    @Column
//    private int reinforcements;
}
