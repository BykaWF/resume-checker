package y.prozorov.resume_checker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import y.prozorov.resume_checker.dto.ApiResponse;
import y.prozorov.resume_checker.util.ResponseUtil;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiResponse<String>> handleFileUploadException(FileUploadException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseUtil.error("Failed to upload resume",e.getMessage()));
    }

    @ExceptionHandler(AnalyzeResumeException.class)
    public ResponseEntity<ApiResponse<String>> handleAnalyzeException(AnalyzeResumeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseUtil.error("Failed to analyze resume",e.getMessage()));
    }
}
