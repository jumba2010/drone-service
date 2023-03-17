package jumba.com.droneservice.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MedicationRequest {
    private ArrayList<Long> medicationIds=new ArrayList<>();
}
