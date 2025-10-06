package ar.edu.utn.frc.tup.piii.entities;

import ar.edu.utn.frc.tup.piii.enums.Color;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "objectives")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectiveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long objectiveId;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "objective_type")
    private ObjectiveTypeEntity objectiveType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Color color;

    @Column
    private int firstRegionTerritoriesNeeded;
    @Column
    private int secondRegionTerritoriesNeeded;
    @Column
    private int thirdRegionTerritoriesNeeded;
    @Column
    private int fourthRegionTerritoriesNeeded;
    @Column
    private int fifthRegionTerritoriesNeeded;
    @Column
    private int sixthRegionTerritoriesNeeded;

}