package ar.edu.utn.frc.tup.piii.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "objectives_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectiveTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long objectiveTypeId;
    @Column
    private String name;
    @ManyToMany
    private List<SettingsEntity> settings;
}