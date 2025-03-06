package platform.entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "assets")
@Data
@RequiredArgsConstructor
public class Asset {
    @Id
    private String assetId;
    private String name;
    private Map<String, String> identifiers;
    private String location;
    //private List<WorkOrder> workOrders;
}
