package pico.erp.production.request;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import pico.erp.production.plan.ProductionPlanId;

@Repository
public interface ProductionRequestRepository {

  ProductionRequest create(@NotNull ProductionRequest productionRequest);

  void deleteBy(@NotNull ProductionRequestId id);

  boolean exists(@NotNull ProductionRequestId id);

  boolean exists(@NotNull ProductionPlanId planId);

  Optional<ProductionRequest> findBy(@NotNull ProductionRequestId id);

  Optional<ProductionRequest> findBy(@NotNull ProductionPlanId planId);

  void update(@NotNull ProductionRequest productionRequest);

  long countCreatedBetween(LocalDateTime begin, LocalDateTime end);

}
