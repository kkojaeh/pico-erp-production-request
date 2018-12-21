package pico.erp.production.request;

import java.time.OffsetDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
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

  boolean asap;

  CompanyId customerId;

  CompanyId purchaserId;

  CompanyId receiverId;

  ProjectId projectId;

  ProductionRequestStatusKind status;

  Auditor createdBy;

  OffsetDateTime createdDate;

  Auditor committedBy;

  OffsetDateTime committedDate;

  Auditor canceledBy;

  OffsetDateTime canceledDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Filter {

    String code;

    CompanyId relatedCompanyId;

    ProjectId projectId;

    ItemId itemId;

    Set<ProductionRequestStatusKind> statuses;

    OffsetDateTime startDueDate;

    OffsetDateTime endDueDate;

  }

}
