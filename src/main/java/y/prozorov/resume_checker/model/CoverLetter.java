package y.prozorov.resume_checker.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "cover_letter")
public class CoverLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Lob
    @Column(name = "letter_text", nullable = false)
    private String letterText;

    @CreationTimestamp
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private Instant uploadedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;


}
