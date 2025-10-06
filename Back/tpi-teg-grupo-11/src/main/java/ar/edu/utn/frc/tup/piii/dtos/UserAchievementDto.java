package ar.edu.utn.frc.tup.piii.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAchievementDto {
    private Long userAchievementID;
    private UserDto user;
    private AchievementDto achievement;
    private Date date;
}
