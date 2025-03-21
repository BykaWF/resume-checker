package y.prozorov.resume_checker.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import y.prozorov.resume_checker.model.Resume;
import y.prozorov.resume_checker.repository.ResumeRepository;

import java.util.UUID;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }
    @Transactional
    public Resume save(Resume resume){
       return resumeRepository.save(resume);
    }
    @Transactional
    public Resume getResumeByUserId(String userId) {
        return resumeRepository.findByUserId(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("Resume not found for userId: " + userId));
    }
}
