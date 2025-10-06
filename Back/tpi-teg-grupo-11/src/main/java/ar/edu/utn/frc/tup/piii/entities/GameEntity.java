package ar.edu.utn.frc.tup.piii.entities;

import ar.edu.utn.frc.tup.piii.enums.GameState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "games")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID gameId;
    @OneToOne
    @JoinColumn(referencedColumnName = "settingsId")
    private SettingsEntity settings;
    @Enumerated(EnumType.STRING)
    @Column
    private GameState gameState;
    @Column
    private Date creationDate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "gameId")
    private List<PlayerEntity> players = new ArrayList<>();

    @OneToMany(mappedBy = "game")
    private List<CardStateEntity> cardStates;
    @OneToMany(mappedBy = "game")
    private List<PactEntity> pacts;
}