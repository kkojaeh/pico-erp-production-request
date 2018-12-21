package pico.erp.production.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;
import pico.erp.company.CompanyId;
import pico.erp.item.ItemId;
import pico.erp.order.acceptance.OrderAcceptanceId;
import pico.erp.production.plan.ProductionPlanId;
import pico.erp.project.ProjectId;
import pico.erp.shared.data.Address;
import pico.erp.shared.data.Auditor;

@Data
public class ProductionRequestData {

  ProductionRequestId id;

  ItemId itemId;

  ProductionRequestCode code;

  BigDecimal quantity;

  OffsetDateTime dueDate;

  boolean asap;

  CompanyId customerId;

  CompanyId purchaserId;

  CompanyId receiverId;

  ProjectId projectId;

  ProductionPlanId planId;

  Address deliveryAddress;

  String deliveryTelephoneNumber;

  String deliveryMobilePhoneNumber;

  OrderAcceptanceId orderAcceptanceId;

  ProductionRequestStatusKind status;

  Auditor committedBy;

  OffsetDateTime committedDate;

  Auditor canceledBy;

  OffsetDateTime canceledDate;

}
