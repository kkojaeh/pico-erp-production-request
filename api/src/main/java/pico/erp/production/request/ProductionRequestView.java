package pico.erp.production.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.ItemId;
import pico.erp.project.ProjectId;
import pico.erp.shared.data.Auditor;
import pico.erp.user.UserId;

@Data
public class ProductionRequestView {

  ProductionRequestId id;

  ItemId itemId;

  ProductionRequestCode code;

  OffsetDateTime dueDate;

  BigDecimal quantity;

  BigDecimal spareQuantity;

  boolean asap;

  ProjectId projectId;

  ProductionRequestStatusKind status;

  UserId requesterId;

  Auditor createdBy;

  OffsetDateTime createdDate;

  UserId committerId;

  OffsetDateTime committedDate;

  UserId cancelerId;

  OffsetDateTime canceledDate;

  UserId accepterId;

  OffsetDateTime acceptedDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Filter {

    String code;

    ProjectId projectId;

    UserId requesterId;

    ItemId itemId;

    Set<ProductionRequestStatusKind> statuses;

    OffsetDateTime startDueDate;

    OffsetDateTime endDueDate;

  }

}
