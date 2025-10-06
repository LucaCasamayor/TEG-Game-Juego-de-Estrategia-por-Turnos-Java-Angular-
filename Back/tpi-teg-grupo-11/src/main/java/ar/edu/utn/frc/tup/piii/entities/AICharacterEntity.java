package ar.edu.utn.frc.tup.piii.entities;

import ar.edu.utn.frc.tup.piii.enums.AIProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Ai_Characters")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AICharacterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long characterId;
    @Column(nullable = false, name = "name")
    private String name;
    @Column(nullable = false, name = "image")
    private String image;
    @Enumerated(EnumType.STRING)
    private AIProfile profile;
    @Column(nullable = false, name = "description")
    private String description;

}
