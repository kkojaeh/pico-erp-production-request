package pico.erp.production.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.ItemId;
import pico.erp.project.ProjectId;
import pico.erp.user.UserId;

@Data
public class ProductionRequestAwaitAcceptView {

  ProductionRequestId id;

  ItemId itemId;

  ProductionRequestCode code;

  OffsetDateTime dueDate;

  BigDecimal quantity;

  BigDecimal spareQuantity;

  boolean asap;

  ProjectId projectId;

  UserId committerId;

  OffsetDateTime committedDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Filter {

    String code;

    ProjectId projectId;

    ItemId itemId;

    OffsetDateTime startDueDate;

    OffsetDateTime endDueDate;

  }

}
