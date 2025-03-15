package y.prozorov.resume_checker.dto;

import java.util.List;

public record ResumeAnalysisResponse(List<String> mistakes, List<String> suggestions) {
}
