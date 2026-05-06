package com.MyMDentis.MyMDentistComerce.Repository;

import com.MyMDentis.MyMDentistComerce.Model.Reserved;
import com.MyMDentis.MyMDentistComerce.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservedRepository extends JpaRepository<Reserved, Long> {

    List<Reserved> findByUserEntity(UserEntity userEntity);

    List<Reserved> findByActiveReserved(boolean activeReserved);


}
