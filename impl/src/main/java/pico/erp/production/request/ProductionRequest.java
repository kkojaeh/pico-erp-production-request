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
import pico.erp.audit.annotation.Audit;
import pico.erp.bom.BomStatusKind;
import pico.erp.item.ItemData;
import pico.erp.item.ItemStatusKind;
import pico.erp.order.acceptance.OrderAcceptanceData;
import pico.erp.product.specification.ProductSpecificationStatusKind;
import pico.erp.production.plan.ProductionPlanData;
import pico.erp.production.request.ProductionRequestExceptions.CannotUpdateException;
import pico.erp.project.ProjectData;
import pico.erp.user.UserData;

/**
 * 주문 접수
 */
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Audit(alias = "production-request")
public class ProductionRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  ProductionRequestId id;

  ProductionRequestCode code;

  ItemData item;

  BigDecimal quantity;

  BigDecimal spareQuantity;

  OffsetDateTime dueDate;

  boolean asap;

  ProjectData project;

  BigDecimal progressRate;

  OrderAcceptanceData orderAcceptance;

  ProductionRequestStatusKind status;

  UserData requester;

  UserData committer;

  OffsetDateTime committedDate;

  UserData canceler;

  OffsetDateTime canceledDate;

  UserData accepter;

  OffsetDateTime acceptedDate;

  OffsetDateTime completedDate;

  ProductionPlanData plan;

  public ProductionRequest() {

  }

  public ProductionRequestMessages.Create.Response apply(
    ProductionRequestMessages.Create.Request request) {
    this.id = request.getId();
    this.item = request.getItem();
    this.quantity = request.getQuantity();
    this.spareQuantity = request.getSpareQuantity();
    this.dueDate = request.getDueDate();
    this.asap = request.isAsap();
    this.project = request.getProject();
    this.status = ProductionRequestStatusKind.CREATED;
    this.orderAcceptance = request.getOrderAcceptance();
    this.progressRate = BigDecimal.ZERO;
    this.requester = request.getRequester();
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
    this.item = request.getItem();
    this.quantity = request.getQuantity();
    this.spareQuantity = request.getSpareQuantity();
    this.dueDate = request.getDueDate();
    this.asap = request.isAsap();
    this.project = request.getProject();

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
    if (!isCommittable() || !requester.equals(request.getCommitter())) {
      throw new ProductionRequestExceptions.CannotCommitException();
    }
    status = ProductionRequestStatusKind.COMMITTED;
    committer = request.getCommitter();
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
    accepter = request.getAccepter();
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
    plan = request.getPlan();
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
    canceler = request.getCanceler();
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
