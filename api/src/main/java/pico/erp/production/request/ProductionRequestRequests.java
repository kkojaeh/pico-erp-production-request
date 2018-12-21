package pico.erp.production.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pico.erp.company.CompanyId;
import pico.erp.item.ItemId;
import pico.erp.order.acceptance.OrderAcceptanceId;
import pico.erp.project.ProjectId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Address;
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
    CompanyId customerId;

    @NotNull
    CompanyId purchaserId;

    @NotNull
    CompanyId receiverId;

    @NotNull
    ProjectId projectId;

    @Valid
    @NotNull
    Address deliveryAddress;

    @Size(max = TypeDefinitions.PHONE_NUMBER_LENGTH)
    String deliveryTelephoneNumber;

    @Size(max = TypeDefinitions.PHONE_NUMBER_LENGTH)
    String deliveryMobilePhoneNumber;

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
    CompanyId customerId;

    @NotNull
    CompanyId purchaserId;

    @NotNull
    CompanyId receiverId;

    @NotNull
    ProjectId projectId;

    @Valid
    @NotNull
    Address deliveryAddress;

    @Size(max = TypeDefinitions.PHONE_NUMBER_LENGTH)
    String deliveryTelephoneNumber;

    @Size(max = TypeDefinitions.PHONE_NUMBER_LENGTH)
    String deliveryMobilePhoneNumber;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class DeleteRequest {

    @Valid
    @NotNull
    ProductionRequestId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CommitRequest {

    @Valid
    @NotNull
    ProductionRequestId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CancelRequest {

    @Valid
    @NotNull
    ProductionRequestId id;

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
