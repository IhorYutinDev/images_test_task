package ua.ihor.ImagesTestTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ihor.ImagesTestTask.models.ProofOfPlay;

@Repository
public interface ProofOfPlayRepository extends JpaRepository<ProofOfPlay, Long> {

}
