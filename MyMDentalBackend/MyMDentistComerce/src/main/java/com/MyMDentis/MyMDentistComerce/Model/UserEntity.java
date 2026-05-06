package com.MyMDentis.MyMDentistComerce.Model;


import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Table(name = "entity_user")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", length = 7, nullable = false, unique = true)
    private Long idUser;
    @Column(name = "name_user", length = 25, nullable = false, unique = false)
    private String nameUser;
    @Column(name = "surname_user", length = 25, nullable = false, unique = false)
    private String surnameUser;
    @Column(name = "email_user", length = 100, nullable = false, unique = true)
    private String emailUser;
    @Column(name = "password_user", length = 100, nullable = false, unique = false)
    private String passwordUser;
    @Column(name = "cellphone_user", length = 9, nullable = true, unique = true)
    private Long cellphoneUser;
    @Column(name = "role_user", length = 20, nullable = false)
    private Roles role;
}
