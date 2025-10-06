package ar.edu.utn.frc.tup.piii.entities;

import ar.edu.utn.frc.tup.piii.enums.AIProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "settings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settingsId;

    @Column
    private int turnTime;

    @ManyToOne
    @JoinColumn
    private GameMapEntity map;

    @Enumerated(EnumType.STRING)
    @Column
    private AIProfile aiProfile;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "settings_objective_types",
            joinColumns = @JoinColumn(name = "settings_id"),
            inverseJoinColumns = @JoinColumn(name = "objective_type_id")
    )
    private List<ObjectiveTypeEntity> objectiveTypes;

    @Column
    private String password;

    @Column
    private boolean isPrivate;
}
