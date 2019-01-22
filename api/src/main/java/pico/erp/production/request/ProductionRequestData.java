package pico.erp.production.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Data;
import pico.erp.item.ItemId;
import pico.erp.order.acceptance.OrderAcceptanceId;
import pico.erp.production.plan.ProductionPlanId;
import pico.erp.project.ProjectId;
import pico.erp.user.UserId;

@Data
public class ProductionRequestData {

  ProductionRequestId id;

  ItemId itemId;

  ProductionRequestCode code;

  BigDecimal quantity;

  BigDecimal spareQuantity;

  OffsetDateTime dueDate;

  boolean asap;

  ProjectId projectId;

  ProductionPlanId planId;

  OrderAcceptanceId orderAcceptanceId;

  ProductionRequestStatusKind status;

  UserId requesterId;

  UserId committerId;

  OffsetDateTime committedDate;

  UserId cancelerId;

  OffsetDateTime canceledDate;

  UserId accepterId;

  OffsetDateTime acceptedDate;

  boolean committable;

  boolean cancelable;

  boolean acceptable;

  boolean completable;

  boolean progressable;

  boolean updatable;

  boolean plannable;

}
