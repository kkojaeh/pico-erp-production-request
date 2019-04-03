package pico.erp.production.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import pico.erp.company.CompanyId;
import pico.erp.item.ItemId;
import pico.erp.order.acceptance.OrderAcceptanceId;
import pico.erp.production.plan.ProductionPlanId;
import pico.erp.project.ProjectId;
import pico.erp.shared.data.UnitKind;
import pico.erp.user.UserId;

@Data
public class ProductionRequestData {

  ProductionRequestId id;

  ItemId itemId;

  ProductionRequestCode code;

  BigDecimal quantity;

  BigDecimal spareQuantity;

  LocalDateTime dueDate;

  boolean asap;

  ProjectId projectId;

  ProductionPlanId planId;

  OrderAcceptanceId orderAcceptanceId;

  ProductionRequestStatusKind status;

  UserId requesterId;

  UserId committerId;

  LocalDateTime committedDate;

  UserId cancelerId;

  LocalDateTime canceledDate;

  UserId accepterId;

  LocalDateTime acceptedDate;

  LocalDateTime completedDate;

  CompanyId receiverId;

  UnitKind unit;

  boolean committable;

  boolean cancelable;

  boolean acceptable;

  boolean completable;

  boolean progressable;

  boolean updatable;

  boolean plannable;

}
