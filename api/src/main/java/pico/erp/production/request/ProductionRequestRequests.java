package pico.erp.production.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pico.erp.item.ItemId;
import pico.erp.order.acceptance.OrderAcceptanceId;
import pico.erp.project.ProjectId;
import pico.erp.user.UserId;

public interface ProductionRequestRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    ProductionRequestId id;

    @Valid
    @NotNull
    ItemId itemId;

    @NotNull
    @Min(0)
    BigDecimal quantity;

    @Future
    @NotNull
    OffsetDateTime dueDate;

    boolean asap;

    @NotNull
    ProjectId projectId;

    @Valid
    OrderAcceptanceId orderAcceptanceId;


  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    ProductionRequestId id;

    @NotNull
    @Min(0)
    BigDecimal quantity;

    @Future
    @NotNull
    OffsetDateTime dueDate;

    boolean asap;

    @NotNull
    ProjectId projectId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class AcceptRequest {

    @Valid
    @NotNull
    ProductionRequestId id;

    @Valid
    @NotNull
    UserId accepterId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CommitRequest {

    @Valid
    @NotNull
    ProductionRequestId id;

    @Valid
    @NotNull
    UserId committerId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CancelRequest {

    @Valid
    @NotNull
    ProductionRequestId id;

    @Valid
    @NotNull
    UserId cancelerId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class ProgressRequest {

    @Valid
    @NotNull
    ProductionRequestId id;

    @Min(0)
    @Max(1)
    @NonNull
    BigDecimal progressRate;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CompleteRequest {

    @Valid
    @NotNull
    ProductionRequestId id;

  }

}
