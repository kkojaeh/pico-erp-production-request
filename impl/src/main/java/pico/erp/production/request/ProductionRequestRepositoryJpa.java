package pico.erp.production.request;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.production.plan.ProductionPlanId;

@Repository
interface ProductionRequestEntityRepository extends
  CrudRepository<ProductionRequestEntity, ProductionRequestId> {

  @Query("SELECT COUNT(r) FROM ProductionRequest r WHERE r.createdDate >= :begin AND r.createdDate <= :end")
  long countCreatedBetween(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);

  @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM ProductionRequest r WHERE r.planId = :planId")
  boolean exists(@Param("planId") ProductionPlanId planId);

  @Query("SELECT r FROM ProductionRequest r WHERE r.planId = :planId")
  Optional<ProductionRequestEntity> findBy(@Param("planId") ProductionPlanId planId);

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
  public long countCreatedBetween(LocalDateTime begin, LocalDateTime end) {
    return repository.countCreatedBetween(begin, end);
  }

  @Override
  public void deleteBy(ProductionRequestId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(ProductionPlanId planId) {
    return repository.exists(planId);
  }

  @Override
  public boolean exists(ProductionRequestId id) {
    return repository.existsById(id);
  }

  @Override
  public Optional<ProductionRequest> findBy(ProductionPlanId planId) {
    return repository.findBy(planId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<ProductionRequest> findBy(ProductionRequestId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(ProductionRequest productionRequest) {
    val entity = repository.findById(productionRequest.getId()).get();
    mapper.pass(mapper.jpa(productionRequest), entity);
    repository.save(entity);
  }
}
