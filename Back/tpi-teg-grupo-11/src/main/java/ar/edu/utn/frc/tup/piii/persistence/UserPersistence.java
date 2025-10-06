package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.UserEntity;
import ar.edu.utn.frc.tup.piii.models.User;
import ar.edu.utn.frc.tup.piii.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPersistence {

    private final UserJpaRepository repository;
    private final ModelMapper modelMapper;

    public User findById(UUID id) {
        return modelMapper.map(repository.getReferenceById(id), User.class);
    }

    public User save(User user) {
        UserEntity entity = modelMapper.map(user, UserEntity.class);
        return modelMapper.map(repository.save(entity), User.class);
    }

    public User update(UUID id,User user) {
        UserEntity entity = repository.getReferenceById(id);
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setImgUrl(user.getImgUrl());
        entity.setActive(user.isActive());
        return modelMapper.map(repository.save(entity), User.class);
    }

    public User findByUsernameAndPassword(String username, String password) {
        return modelMapper.map(repository.findByUsernameAndPassword(username, password), User.class);
    }

    public User findByUsername(String username) {
        UserEntity result = repository.findByUsername(username);
        if(result != null) {
            return modelMapper.map(result, User.class);
        }
        return null;
    }
}
