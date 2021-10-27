package com.example.portalapi.repository;

import com.example.portalapi.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("http://localhost:4200/")
public interface AwardRepository extends JpaRepository<Award, Long> {
//    List<Award> findByUsers_Email(@RequestParam("email") String email);
//
//    private EntityManager entityManager;
//
//    public AwardRepository(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }
//
//    public Optional<Award> save(Award award) {
//        try {
//            entityManager.getTransaction().begin();
//            entityManager.persist(award);
//            entityManager.getTransaction().commit();
//            return Optional.of(award);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return Optional.empty();
//    }
//
//    public Optional<Award> findById(Double id) {
//        Award award = entityManager.find(Award.class, id);
//        return award != null ? Optional.of(award) : Optional.empty();
//    }
//
//    public List<Award> findAll() {
//        return entityManager.createQuery("from Award").getResultList();
//    }
//
//    public void deleteById(Integer id) {
//        // Retrieve the award with this ID
//        Award award = entityManager.find(Award.class, id);
//        if (award != null) {
//            try {
//                // Start a transaction because we're going to change the database
//                entityManager.getTransaction().begin();
//
//                // Remove all references to this award by superheroes
//                award.getRecipients().forEach(user -> {
//                    user.getAwards().remove(award);
//                });
//
//                // Now remove the award
//                entityManager.remove(award);
//
//                // Commit the transaction
//                entityManager.getTransaction().commit();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
