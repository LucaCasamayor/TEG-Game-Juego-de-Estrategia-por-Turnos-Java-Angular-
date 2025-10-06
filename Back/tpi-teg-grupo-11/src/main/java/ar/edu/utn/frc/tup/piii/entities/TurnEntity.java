package ar.edu.utn.frc.tup.piii.entities;

import ar.edu.utn.frc.tup.piii.enums.TurnPhase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Turns")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "turn_id")
    private Long turnId;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game;

    @ManyToOne
    @JoinColumn(name = "player_id")
    @JsonIgnore
    private PlayerEntity player;

    @Enumerated(EnumType.STRING)
    private TurnPhase turnPhase;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<MovementEntity> movements;
//    @OneToMany(mappedBy = "turn")
//    private List<TerritoryStateEntity> territoryStates;
}
