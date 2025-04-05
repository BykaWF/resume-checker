package y.prozorov.resume_checker.util;

import org.springframework.stereotype.Component;

@Component
public class PromptBase {

    public String getAnalyseResumePrompt(){
        return """
                You are a resume analyzer. \s
                Compare the given resume with the provided job description. \s
                
                ### Output Format (JSON Only)
                Return a **valid JSON array** of objects in the following format:
                
                ```json
                [
                  {
                    "missingKeyword": "Java",
                    "suggestion": "Add Java programming skills"
                  },
                  {
                    "missingKeyword": "SQL",
                    "suggestion": "Include SQL expertise"
                  }
                ]
                Instructions
                Identify missing skills, technologies, methodologies, or qualifications in the resume.
                
                Provide precise and concise suggestions to improve the resume.
                
                Ensure only valid JSON output—no extra text, explanations, or formatting.
                """;
    }

    public String getGenerateCoverLetterPrompt(){
        return """
                Generate a cover letter for a job application using the following template.\s
                Replace the placeholder text and customize the content according to the provided input.\s
                Ensure that the formatting is consistent and professional, and that no placeholders or instructions remain in the final output.
                Return only String.class
               """;
    }
}
