package pico.erp.production.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.bom.BomData;
import pico.erp.company.CompanyId;
import pico.erp.item.ItemData;
import pico.erp.item.ItemId;
import pico.erp.order.acceptance.OrderAcceptanceId;
import pico.erp.product.specification.ProductSpecificationData;
import pico.erp.production.plan.ProductionPlanId;
import pico.erp.project.ProjectId;
import pico.erp.shared.data.UnitKind;
import pico.erp.shared.event.Event;
import pico.erp.user.UserId;

public interface ProductionRequestMessages {

  interface Create {

    @Data
    class Request {

      @Valid
      @NotNull
      ProductionRequestId id;

      @Valid
      @NotNull
      ItemId itemId;

      @NotNull
      @Min(0)
      BigDecimal quantity;

      @NotNull
      @Min(0)
      BigDecimal spareQuantity;

      @Future
      @NotNull
      OffsetDateTime dueDate;

      boolean asap;

      @Valid
      @NotNull
      ProjectId projectId;

      @NotNull
      ProductionRequestCodeGenerator codeGenerator;

      @Valid
      OrderAcceptanceId orderAcceptanceId;

      @Valid
      @NotNull
      UserId requesterId;

      @Valid
      @NotNull
      CompanyId receiverId;

      @NotNull
      UnitKind unit;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Update {

    @Data
    class Request {

      @Valid
      @NotNull
      ItemId itemId;

      @NotNull
      @Min(0)
      BigDecimal quantity;

      @NotNull
      @Min(0)
      BigDecimal spareQuantity;

      @Future
      @NotNull
      OffsetDateTime dueDate;

      boolean asap;

      @NotNull
      ProjectId projectId;

      @Valid
      @NotNull
      CompanyId receiverId;

      @NotNull
      UnitKind unit;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Progress {

    @Data
    class Request {

      @Min(0)
      @Max(1)
      @NotNull
      BigDecimal progressRate;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }


  interface Plan {

    @Data
    class Request {

      @Valid
      @NotNull
      ProductionPlanId planId;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Commit {

    @Data
    class Request {

      @NotNull
      UserId committerId;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Accept {

    @Data
    class Request {

      @NotNull
      ItemData item;

      BomData bom;

      ProductSpecificationData productSpecification;

      @NotNull
      UserId accepterId;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Cancel {

    @Data
    class Request {

      @NotNull
      UserId cancelerId;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Complete {

    @Data
    class Request {


    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

}
