package ar.edu.utn.frc.tup.piii.entities;

import ar.edu.utn.frc.tup.piii.enums.BotDifficulty;
import ar.edu.utn.frc.tup.piii.enums.Color;
import ar.edu.utn.frc.tup.piii.enums.PlayerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "players")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerId;
    @Column(name = "gameId", nullable = false)
    private UUID gameId;
    @JoinColumn(referencedColumnName = "userId")
    @ManyToOne
    private UserEntity user;
    @ManyToOne
    @JoinColumn(referencedColumnName = "objectiveId")
    private ObjectiveEntity objective;
    @Enumerated(EnumType.STRING)
    @Column
    private Color playerColor;
    @Column
    private int turnOrder;
    @Column
    private boolean isWinner;
    @Column
    private boolean hasLost;
    @Enumerated(EnumType.STRING)
    @Column
    private PlayerType playerType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private BotDifficulty botDifficulty;

//    @OneToMany(mappedBy = "player")
//    private List<TurnEntity> turns;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CardStateEntity> cards;
//    @OneToMany(mappedBy = "player1")
//    private List<GlobalPactEntity> globalPactsStarted;
//    @OneToMany(mappedBy = "player2")
//    private List<GlobalPactEntity> globalPactsAccepted;
//    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<TerritoryStateEntity> territories;
}