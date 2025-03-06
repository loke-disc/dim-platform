package platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import platform.repository.AssetRepository;
import platform.repository.WorkOrderRepository;

@RestController
@RequestMapping("/mongo")
public class MongoDBController {

    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/test")
    public String testConnection() {
        long count = assetRepository.count();
        System.out.println("Collections - assets: " + mongoTemplate.getCollectionNames());
        System.out.println("Assets Count: " + mongoTemplate.getCollection("assets").countDocuments());
        System.out.println("Collections - work_orders: " + mongoTemplate.getCollectionNames());
        System.out.println("WorkOrders Count: " + mongoTemplate.getCollection("work_orders").countDocuments());
        return "Connected t0 Mongo. Total Assets - "+count;
    }
}
