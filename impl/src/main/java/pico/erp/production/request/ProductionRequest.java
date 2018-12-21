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
import pico.erp.company.CompanyData;
import pico.erp.item.ItemData;
import pico.erp.item.ItemStatusKind;
import pico.erp.order.acceptance.OrderAcceptanceData;
import pico.erp.process.ProcessStatusKind;
import pico.erp.production.plan.ProductionPlanData;
import pico.erp.production.plan.ProductionPlanId;
import pico.erp.production.request.ProductionRequestExceptions.CannotUpdateException;
import pico.erp.project.ProjectData;
import pico.erp.shared.data.Address;
import pico.erp.shared.data.Auditor;

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

  OffsetDateTime dueDate;

  boolean asap;

  CompanyData customer;

  CompanyData purchaser;

  CompanyData receiver;

  ProjectData project;

  BigDecimal progressRate;

  OrderAcceptanceData orderAcceptance;

  Address deliveryAddress;

  String deliveryTelephoneNumber;

  String deliveryMobilePhoneNumber;

  ProductionRequestStatusKind status;

  Auditor committedBy;

  OffsetDateTime committedDate;

  Auditor canceledBy;

  OffsetDateTime canceledDate;

  OffsetDateTime completedDate;

  ProductionPlanData plan;

  public ProductionRequest() {

  }

  public ProductionRequestMessages.CreateResponse apply(
    ProductionRequestMessages.CreateRequest request) {
    this.id = request.getId();
    this.item = request.getItem();
    this.quantity = request.getQuantity();
    this.dueDate = request.getDueDate();
    this.asap = request.isAsap();
    this.customer = request.getCustomer();
    this.receiver = request.getReceiver();
    this.purchaser = request.getPurchaser();
    this.project = request.getProject();
    this.status = ProductionRequestStatusKind.CREATED;
    this.deliveryAddress = request.getDeliveryAddress();
    this.deliveryMobilePhoneNumber = request.getDeliveryMobilePhoneNumber();
    this.deliveryTelephoneNumber = request.getDeliveryTelephoneNumber();
    this.orderAcceptance = request.getOrderAcceptance();
    this.progressRate = BigDecimal.ZERO;
    this.code = request.getCodeGenerator().generate(this);

    return new ProductionRequestMessages.CreateResponse(
      Arrays.asList(new ProductionRequestEvents.CreatedEvent(this.id))
    );
  }

  public ProductionRequestMessages.UpdateResponse apply(
    ProductionRequestMessages.UpdateRequest request) {
    if (!isModifiable()) {
      throw new CannotUpdateException();
    }
    this.quantity = request.getQuantity();
    this.dueDate = request.getDueDate();
    this.asap = request.isAsap();
    this.customer = request.getCustomer();
    this.purchaser = request.getPurchaser();
    this.receiver = request.getReceiver();
    this.project = request.getProject();
    this.deliveryAddress = request.getDeliveryAddress();
    this.deliveryMobilePhoneNumber = request.getDeliveryMobilePhoneNumber();
    this.deliveryTelephoneNumber = request.getDeliveryTelephoneNumber();

    return new ProductionRequestMessages.UpdateResponse(
      Arrays.asList(new ProductionRequestEvents.UpdatedEvent(this.id))
    );
  }

  public ProductionRequestMessages.DeleteResponse apply(
    ProductionRequestMessages.DeleteRequest request) {
    return new ProductionRequestMessages.DeleteResponse(
      Arrays.asList(new ProductionRequestEvents.DeletedEvent(this.id))
    );
  }

  public ProductionRequestMessages.ProgressResponse apply(
    ProductionRequestMessages.ProgressRequest request) {
    this.status = ProductionRequestStatusKind.IN_PROGRESS;
    this.progressRate = request.getProgressRate();
    return new ProductionRequestMessages.ProgressResponse(
      Arrays.asList(new ProductionRequestEvents.ProgressedEvent(this.id, this.progressRate))
    );
  }

  public ProductionRequestMessages.CompleteResponse apply(
    ProductionRequestMessages.CompleteRequest request) {
    this.status = ProductionRequestStatusKind.COMPLETED;
    this.completedDate = OffsetDateTime.now();
    return new ProductionRequestMessages.CompleteResponse(
      Arrays.asList(new ProductionRequestEvents.CompletedEvent(this.id))
    );
  }

  public ProductionRequestMessages.CommitResponse apply(
    ProductionRequestMessages.CommitRequest request) {
    val bom = request.getBom();
    if (!isCommittable()) {
      throw new ProductionRequestExceptions.CannotCommitException();
    }
    if(item.getStatus() != ItemStatusKind.ACTIVATED){
      throw new ProductionRequestExceptions.CannotCommitItemDeactivatedException();
    }
    if (bom.getStatus() != BomStatusKind.DETERMINED) {
      throw new ProductionRequestExceptions.CannotCommitBomNotDeterminedException();
    }
    status = ProductionRequestStatusKind.COMMITTED;
    committedBy = request.getCommittedBy();
    committedDate = OffsetDateTime.now();
    return new ProductionRequestMessages.CommitResponse(
      Arrays.asList(new ProductionRequestEvents.CommittedEvent(this.id))
    );
  }

  public ProductionRequestMessages.CancelResponse apply(
    ProductionRequestMessages.CancelRequest request) {
    if (!isCancelable()) {
      throw new ProductionRequestExceptions.CannotCancelException();
    }
    status = ProductionRequestStatusKind.CANCELED;
    canceledBy = request.getCanceledBy();
    canceledDate = OffsetDateTime.now();
    return new ProductionRequestMessages.CancelResponse(
      Arrays.asList(new ProductionRequestEvents.CanceledEvent(this.id))
    );
  }

  public boolean isCommittable() {
    return status.isCommittable();
  }

  public boolean isCancelable() {
    return status.isCancelable();
  }

  public boolean isModifiable() {
    return status.isUpdatable();
  }


}
