package ar.edu.utn.frc.tup.piii.entities;

import ar.edu.utn.frc.tup.piii.enums.TurnPhase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "movements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "start_territory")
    @JsonIgnore
    private TerritoryStateEntity startTerritory;

    @ManyToOne
    @JoinColumn(name = "end_territory")
    @JsonIgnore
    private TerritoryStateEntity endTerritory;
    @Column
    private int armyCount;

    @Enumerated(EnumType.STRING)
    @Column
    private TurnPhase movementType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DiceEntity> dice;
}