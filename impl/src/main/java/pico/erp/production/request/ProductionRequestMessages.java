package pico.erp.production.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Value;
import pico.erp.bom.BomData;
import pico.erp.company.CompanyData;
import pico.erp.item.ItemData;
import pico.erp.order.acceptance.OrderAcceptanceData;
import pico.erp.process.ProcessData;
import pico.erp.project.ProjectData;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Address;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.Event;

public interface ProductionRequestMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    ProductionRequestId id;

    @Valid
    @NotNull
    ItemData item;

    @NotNull
    @Min(0)
    BigDecimal quantity;

    @Future
    @NotNull
    OffsetDateTime dueDate;

    boolean asap;

    @NotNull
    CompanyData customer;

    @NotNull
    CompanyData purchaser;

    @NotNull
    CompanyData receiver;

    @NotNull
    ProjectData project;

    @Valid
    @NotNull
    Address deliveryAddress;

    @Size(max = TypeDefinitions.PHONE_NUMBER_LENGTH)
    String deliveryTelephoneNumber;

    @Size(max = TypeDefinitions.PHONE_NUMBER_LENGTH)
    String deliveryMobilePhoneNumber;

    @NotNull
    ProductionRequestCodeGenerator codeGenerator;

    @Valid
    OrderAcceptanceData orderAcceptance;

  }

  @Data
  class UpdateRequest {

    @NotNull
    @Min(0)
    BigDecimal quantity;

    @Future
    @NotNull
    OffsetDateTime dueDate;

    boolean asap;

    @NotNull
    CompanyData customer;

    @NotNull
    CompanyData purchaser;

    @NotNull
    CompanyData receiver;

    @NotNull
    ProjectData project;

    @Valid
    @NotNull
    Address deliveryAddress;

    @Size(max = TypeDefinitions.PHONE_NUMBER_LENGTH)
    String deliveryTelephoneNumber;

    @Size(max = TypeDefinitions.PHONE_NUMBER_LENGTH)
    String deliveryMobilePhoneNumber;

  }

  @Data
  class DeleteRequest {

  }

  @Data
  class ProgressRequest {

    @Min(0)
    @Max(1)
    @NotNull
    BigDecimal progressRate;

  }

  @Data
  class CommitRequest {

    BomData bom;

    @NotNull
    Auditor committedBy;

  }

  @Data
  class CancelRequest {

    @NotNull
    Auditor canceledBy;

  }

  @Data
  class CompleteRequest {


  }

  @Value
  class CreateResponse {

    Collection<Event> events;

  }

  @Value
  class UpdateResponse {

    Collection<Event> events;

  }

  @Value
  class DeleteResponse {

    Collection<Event> events;

  }

  @Value
  class CommitResponse {

    Collection<Event> events;

  }

  @Value
  class CancelResponse {

    Collection<Event> events;

  }

  @Value
  class ProgressResponse {

    Collection<Event> events;

  }

  @Value
  class CompleteResponse {

    Collection<Event> events;

  }


}
