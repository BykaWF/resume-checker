package y.prozorov.resume_checker.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IngestionConfig implements CommandLineRunner {

    private final VectorStore vectorStore;
    @Value("classpath:/docs/resume-base.pdf")
    private Resource resumePDF;

    public IngestionConfig(VectorStore vectorStore) {
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
