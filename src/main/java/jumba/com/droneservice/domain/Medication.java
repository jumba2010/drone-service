package jumba.com.droneservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Entity
@Data
public class Medication {

    @Id
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$")
    private String code;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Min(0)
    private double weight;

    @Lob
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY)
    private Drone drone;
}
