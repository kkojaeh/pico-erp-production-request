package pico.erp.production.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pico.erp.shared.data.LabeledValuable;

public interface ProductionRequestQuery {

  Page<ProductionRequestView> retrieve(@NotNull ProductionRequestView.Filter filter,
    @NotNull Pageable pageable);

}
