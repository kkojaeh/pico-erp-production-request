package pico.erp.production.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
import pico.erp.item.ItemId;
import pico.erp.project.ProjectId;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.data.UnitKind;
import pico.erp.user.UserId;

@Data
public class ProductionRequestView {

  ProductionRequestId id;

  ItemId itemId;

  ProductionRequestCode code;

  LocalDateTime dueDate;

  BigDecimal quantity;

  BigDecimal spareQuantity;

  boolean asap;

  ProjectId projectId;

  ProductionRequestStatusKind status;

  UserId requesterId;

  Auditor createdBy;

  LocalDateTime createdDate;

  UserId committerId;

  LocalDateTime committedDate;

  UserId cancelerId;

  LocalDateTime canceledDate;

  UserId accepterId;

  LocalDateTime acceptedDate;

  LocalDateTime completedDate;

  CompanyId receiverId;

  UnitKind unit;

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

    LocalDateTime startDueDate;

    LocalDateTime endDueDate;

  }

}
