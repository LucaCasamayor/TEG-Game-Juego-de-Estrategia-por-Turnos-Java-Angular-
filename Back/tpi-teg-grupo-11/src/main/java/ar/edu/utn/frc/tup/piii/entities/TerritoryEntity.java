package ar.edu.utn.frc.tup.piii.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "territories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerritoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long territoryId;

    @Column(nullable = false, name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private RegionEntity region;

    @ManyToMany
    @JoinTable(name="borders", joinColumns=@JoinColumn(name = "territory_id"),
    inverseJoinColumns = @JoinColumn(name ="border_id"))
    private List<TerritoryEntity> borders;
}
