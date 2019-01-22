package pico.erp.production.request;

import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface ProductionRequestEntityRepository extends
  CrudRepository<ProductionRequestEntity, ProductionRequestId> {

  @Query("SELECT COUNT(r) FROM ProductionRequest r WHERE r.createdDate >= :begin AND r.createdDate <= :end")
  long countCreatedBetween(@Param("begin") OffsetDateTime begin, @Param("end") OffsetDateTime end);

}

@Repository
@Transactional
public class ProductionRequestRepositoryJpa implements ProductionRequestRepository {

  @Autowired
  private ProductionRequestEntityRepository repository;

  @Autowired
  private ProductionRequestMapper mapper;


  @Override
  public ProductionRequest create(ProductionRequest productionRequest) {
    val entity = mapper.jpa(productionRequest);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(ProductionRequestId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(ProductionRequestId id) {
    return repository.exists(id);
  }

  @Override
  public Optional<ProductionRequest> findBy(ProductionRequestId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(ProductionRequest productionRequest) {
    val entity = repository.findOne(productionRequest.getId());
    mapper.pass(mapper.jpa(productionRequest), entity);
    repository.save(entity);
  }

  @Override
  public long countCreatedBetween(OffsetDateTime begin, OffsetDateTime end) {
    return repository.countCreatedBetween(begin, end);
  }
}
