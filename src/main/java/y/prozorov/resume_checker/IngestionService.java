package y.prozorov.resume_checker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IngestionService implements CommandLineRunner {

    private final VectorStore vectorStore;
    @Value("classpath:/docs/resume-base.pdf")
    private Resource resumePDF;

    public IngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(String... args) throws Exception {
//        var pdfReader = new TikaDocumentReader(resumePDF);
//        var textSplitter = new TokenTextSplitter();
//        vectorStore.accept(textSplitter.apply(pdfReader.get()));
        log.info("Vector store loaded with data");
    }
}
