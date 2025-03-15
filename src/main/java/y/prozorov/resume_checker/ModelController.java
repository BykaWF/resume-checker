package y.prozorov.resume_checker;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelController {
    private final ChatClient chatClient;

    public ModelController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();
    }
// TODO .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory())) test
    @GetMapping("/")
    public String chat() {
        return chatClient.prompt()
                .user("Analyze current resume : John Doe\n" +
                        "\uD83D\uDCCD New York, NY | \uD83D\uDCE7 johndoe@email.com | \uD83D\uDCDE (123) 456-7890 | \uD83D\uDD17 linkedin.com/in/johndoe\n" +
                        "\n" +
                        "Professional Summary\n" +
                        "Results-driven Software Engineer with over a decade of experience, specializing in cloud-based technologies, full-stack development, machine learning, big data processing, and data visualization. Passionate about driving innovation, improving efficiency, and delivering scalable solutions. Seeking an opportunity to leverage my skills in a fast-paced, dynamic environment.\n" +
                        "\n" +
                        "Work Experience\n" +
                        "Senior Software Engineer\n" +
                        "TechCorp Inc. | New York, NY | 2018 - Present\n" +
                        "\n" +
                        "Led a team in developing cloud-based applications, implementing machine learning models, and optimizing big data pipelines.\n" +
                        "Designed and developed scalable full-stack web applications using React, Node.js, and Python.\n" +
                        "Managed and visualized large datasets using Tableau and Power BI.\n" +
                        "Software Engineer\n" +
                        "InnovateTech | San Francisco, CA | 2014 - 2018\n" +
                        "\n" +
                        "Developed and deployed cloud solutions on AWS and Google Cloud Platform.\n" +
                        "Worked on backend services, REST APIs, and front-end development.\n" +
                        "Optimized big data workflows, integrating ML algorithms for predictive analytics.\n" +
                        "Education\n" +
                        "Bachelor of Science in Computer Science\n" +
                        "XYZ University | 2010 - 2014\n" +
                        "\n" +
                        "Skills\n" +
                        "Cloud Technologies: AWS, Azure, Google Cloud\n" +
                        "Programming Languages: JavaScript, Python, Java, C++\n" +
                        "Frameworks: React, Angular, Node.js, Django\n" +
                        "Machine Learning & Data Science: TensorFlow, PyTorch, Pandas\n" +
                        "Big Data: Apache Spark, Hadoop, Kafka\n" +
                        "Data Visualization: Tableau, Power BI\n" +
                        "Certifications\n" +
                        "AWS Certified Solutions Architect\n" +
                        "Google Cloud Professional Data Engineer\n" +
                        "And find common mistakes, suggest only improvements that follow this common mistakes")
                .call()
                .content();
    }


}
