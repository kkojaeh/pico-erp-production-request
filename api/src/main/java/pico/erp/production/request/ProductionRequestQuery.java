package pico.erp.production.request;

import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductionRequestQuery {

  Page<ProductionRequestView> retrieve(@NotNull ProductionRequestView.Filter filter,
    @NotNull Pageable pageable);

  Page<ProductionRequestAwaitAcceptView> retrieve(
    @NotNull ProductionRequestAwaitAcceptView.Filter filter,
    @NotNull Pageable pageable);

}
