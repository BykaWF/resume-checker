package y.prozorov.resume_checker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import y.prozorov.resume_checker.model.CoverLetter;

import java.util.UUID;
@Repository
public interface CoverLetterRepository extends JpaRepository<CoverLetter, UUID> {

}
