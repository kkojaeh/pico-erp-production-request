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
import pico.erp.item.ItemData;
import pico.erp.order.acceptance.OrderAcceptanceData;
import pico.erp.project.ProjectData;
import pico.erp.shared.event.Event;
import pico.erp.user.UserData;

public interface ProductionRequestMessages {

  interface Create {

    @Data
    class Request {

      @Valid
      @NotNull
      ProductionRequestId id;

      @Valid
      @NotNull
      ItemData item;

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
      ProjectData project;

      @NotNull
      ProductionRequestCodeGenerator codeGenerator;

      @Valid
      OrderAcceptanceData orderAcceptance;

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
      ItemData item;

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
      ProjectData project;

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

  interface Commit {

    @Data
    class Request {

      @NotNull
      UserData committer;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Accept {

    @Data
    class Request {

      BomData bom;

      @NotNull
      UserData accepter;

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
      UserData canceler;

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
