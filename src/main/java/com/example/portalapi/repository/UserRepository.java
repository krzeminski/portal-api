package com.example.portalapi.repository;

import com.example.portalapi.entity.Award;
import com.example.portalapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

//@RepositoryRestResource(collectionResourceRel = "user", path= "user")
@CrossOrigin("http://localhost:4200/")
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(@RequestParam("email") String email);

    User findByUsername(String username);
//    Page<User> findByUsername(@RequestParam("username") String username, Pageable pageable);

    @Query("SELECT u FROM User u INNER JOIN u.awards a WHERE a IN (:awards)")
    List<User> findByAwards(Collection<Award> awards);

//    @Query("SELECT u FROM User u WHERE CONCAT(u.id, '', u.email, '', u.firstName, '', u.lastName) LIKE %?1%")
    @Query("SELECT DISTINCT u FROM User u JOIN u.awards WHERE u.email LIKE %?1%")
    Page<User> findAll(String keyword, Pageable pageable);


    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.isActive = TRUE WHERE a.email = ?1")
    int activateUser(String email);

    //    Page<User> findById();
//    @Query("select award from awards join distributed_awards.userid a where a.id =:role_id")
//    Page<User> retrieveById(@Param("id") String name);
//    Page<User> findAllById_JoinAndAwards(@RequestParam("username") String username, Pageable pageable);
//    Page<Award> findByUsername(@RequestParam("username") String username, Pageable pageable);
}
