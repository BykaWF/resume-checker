package y.prozorov.resume_checker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import y.prozorov.resume_checker.model.Resume;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    Optional<Resume> findByUserId(UUID uuid);


    boolean existsByUserId(UUID userId);
}
