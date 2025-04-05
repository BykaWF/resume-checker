package y.prozorov.resume_checker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import y.prozorov.resume_checker.model.CoverLetter;
import y.prozorov.resume_checker.repository.CoverLetterRepository;

@Slf4j
@Service
public class CoverLetterService {

    private final CoverLetterRepository coverLetterRepository;

    @Autowired
    public CoverLetterService(CoverLetterRepository coverLetterRepository) {
        this.coverLetterRepository = coverLetterRepository;
    }


    public CoverLetter getBaseTemplate() {
        return coverLetterRepository.findAll().getFirst();
    }
}
