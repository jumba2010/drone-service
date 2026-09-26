package jumba.com.droneservice.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MedicationRequest {

    @NotEmpty(message = "at least one medication id is required")
    private List<Long> medicationIds = new ArrayList<>();
}
