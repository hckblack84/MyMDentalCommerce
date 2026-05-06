package com.MyMDentis.MyMDentistComerce.Repository;

import com.MyMDentis.MyMDentistComerce.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailUser(String email);

    Optional<UserEntity> findByNameUser(String nameUser);

    Optional<UserEntity> findByNameUserAndSurnameUser(String nameUser, String surnameUser);

}
