package pico.erp.production.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.val;
import pico.erp.bom.BomStatusKind;
import pico.erp.company.CompanyId;
import pico.erp.item.ItemId;
import pico.erp.item.ItemStatusKind;
import pico.erp.order.acceptance.OrderAcceptanceId;
import pico.erp.product.specification.ProductSpecificationStatusKind;
import pico.erp.production.plan.ProductionPlanId;
import pico.erp.production.request.ProductionRequestExceptions.CannotUpdateException;
import pico.erp.project.ProjectId;
import pico.erp.shared.data.UnitKind;
import pico.erp.user.UserId;

/**
 * 주문 접수
 */
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductionRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  ProductionRequestId id;

  ProductionRequestCode code;

  ItemId itemId;

  BigDecimal quantity;

  BigDecimal spareQuantity;

  OffsetDateTime dueDate;

  boolean asap;

  ProjectId projectId;

  BigDecimal progressRate;

  OrderAcceptanceId orderAcceptanceId;

  ProductionRequestStatusKind status;

  UserId requesterId;

  UserId committerId;

  OffsetDateTime committedDate;

  UserId cancelerId;

  OffsetDateTime canceledDate;

  UserId accepterId;

  OffsetDateTime acceptedDate;

  OffsetDateTime completedDate;

  ProductionPlanId planId;

  CompanyId receiverId;

  UnitKind unit;

  public ProductionRequest() {

  }

  public ProductionRequestMessages.Create.Response apply(
    ProductionRequestMessages.Create.Request request) {
    this.id = request.getId();
    this.itemId = request.getItemId();
    this.quantity = request.getQuantity();
    this.spareQuantity = request.getSpareQuantity();
    this.dueDate = request.getDueDate();
    this.asap = request.isAsap();
    this.projectId = request.getProjectId();
    this.status = ProductionRequestStatusKind.CREATED;
    this.orderAcceptanceId = request.getOrderAcceptanceId();
    this.progressRate = BigDecimal.ZERO;
    this.requesterId = request.getRequesterId();
    this.unit = request.getUnit();
    this.receiverId = request.getReceiverId();
    this.code = request.getCodeGenerator().generate(this);

    return new ProductionRequestMessages.Create.Response(
      Arrays.asList(new ProductionRequestEvents.CreatedEvent(this.id))
    );
  }

  public ProductionRequestMessages.Update.Response apply(
    ProductionRequestMessages.Update.Request request) {
    if (!isUpdatable()) {
      throw new CannotUpdateException();
    }
    this.itemId = request.getItemId();
    this.quantity = request.getQuantity();
    this.spareQuantity = request.getSpareQuantity();
    this.dueDate = request.getDueDate();
    this.asap = request.isAsap();
    this.projectId = request.getProjectId();
    this.unit = request.getUnit();
    this.receiverId = request.getReceiverId();

    return new ProductionRequestMessages.Update.Response(
      Arrays.asList(new ProductionRequestEvents.UpdatedEvent(this.id))
    );
  }

  public ProductionRequestMessages.Progress.Response apply(
    ProductionRequestMessages.Progress.Request request) {
    if (!isProgressable()) {
      throw new ProductionRequestExceptions.CannotProgressException();
    }
    this.status = ProductionRequestStatusKind.IN_PROGRESS;
    this.progressRate = request.getProgressRate();
    return new ProductionRequestMessages.Progress.Response(
      Arrays.asList(new ProductionRequestEvents.ProgressedEvent(this.id, this.progressRate))
    );
  }

  public ProductionRequestMessages.Complete.Response apply(
    ProductionRequestMessages.Complete.Request request) {
    if (!isCompletable()) {
      throw new ProductionRequestExceptions.CannotCompleteException();
    }
    this.status = ProductionRequestStatusKind.COMPLETED;
    this.completedDate = OffsetDateTime.now();
    return new ProductionRequestMessages.Complete.Response(
      Arrays.asList(new ProductionRequestEvents.CompletedEvent(this.id))
    );
  }

  public ProductionRequestMessages.Commit.Response apply(
    ProductionRequestMessages.Commit.Request request) {
    if (!isCommittable() || !requesterId.equals(request.getCommitterId())) {
      throw new ProductionRequestExceptions.CannotCommitException();
    }
    status = ProductionRequestStatusKind.COMMITTED;
    committerId = request.getCommitterId();
    committedDate = OffsetDateTime.now();
    return new ProductionRequestMessages.Commit.Response(
      Arrays.asList(new ProductionRequestEvents.CommittedEvent(this.id))
    );
  }

  public ProductionRequestMessages.Accept.Response apply(
    ProductionRequestMessages.Accept.Request request) {
    if (!isAcceptable()) {
      throw new ProductionRequestExceptions.CannotAcceptException();
    }
    val item = request.getItem();
    val bom = request.getBom();
    val productSpecification = request.getProductSpecification();
    if (item.getStatus() != ItemStatusKind.ACTIVATED) {
      throw new ProductionRequestExceptions.CannotAcceptItemDeactivatedException();
    }
    if (bom == null || bom.getStatus() != BomStatusKind.DETERMINED) {
      throw new ProductionRequestExceptions.CannotAcceptBomNotDeterminedException();
    }
    if (productSpecification == null
      || productSpecification.getStatus() != ProductSpecificationStatusKind.COMMITTED) {
      throw new ProductionRequestExceptions.CannotAcceptProductSpecificationNotCommittedException();
    }
    status = ProductionRequestStatusKind.ACCEPTED;
    accepterId = request.getAccepterId();
    acceptedDate = OffsetDateTime.now();
    return new ProductionRequestMessages.Accept.Response(
      Arrays.asList(new ProductionRequestEvents.AcceptedEvent(this.id))
    );
  }

  public ProductionRequestMessages.Plan.Response apply(
    ProductionRequestMessages.Plan.Request request) {
    if (!isPlannable()) {
      throw new ProductionRequestExceptions.CannotPlanException();
    }
    planId = request.getPlanId();
    status = ProductionRequestStatusKind.IN_PLANNING;
    return new ProductionRequestMessages.Plan.Response(
      Arrays.asList(new ProductionRequestEvents.PlannedEvent(this.id))
    );
  }

  public ProductionRequestMessages.Cancel.Response apply(
    ProductionRequestMessages.Cancel.Request request) {
    if (!isCancelable()) {
      throw new ProductionRequestExceptions.CannotCancelException();
    }
    status = ProductionRequestStatusKind.CANCELED;
    cancelerId = request.getCancelerId();
    canceledDate = OffsetDateTime.now();
    return new ProductionRequestMessages.Cancel.Response(
      Arrays.asList(new ProductionRequestEvents.CanceledEvent(this.id))
    );
  }

  public boolean isCommittable() {
    return status.isCommittable();
  }

  public boolean isCancelable() {
    return status.isCancelable();
  }

  public boolean isAcceptable() {
    return status.isAcceptable();
  }

  public boolean isCompletable() {
    return status.isCompletable();
  }

  public boolean isProgressable() {
    return status.isProgressable();
  }

  public boolean isUpdatable() {
    return status.isUpdatable();
  }

  public boolean isPlannable() {
    return status.isPlannable();
  }


}
