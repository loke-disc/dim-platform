package platform.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import platform.entity.WorkOrder;

import java.util.Optional;

public interface WorkOrderRepository extends MongoRepository<WorkOrder, String> {
    Optional<WorkOrder> findByWorkOrderId(String workOrderId);
}
