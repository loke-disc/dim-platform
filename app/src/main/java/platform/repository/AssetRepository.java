package platform.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import platform.entity.Asset;

import java.util.Optional;

public interface AssetRepository extends MongoRepository<Asset, String> {
    Optional<Asset> findByAssetId(String assetId);
}
