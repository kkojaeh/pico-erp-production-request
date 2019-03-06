package pico.erp.production.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface ProductionRequestEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.production-request.created";

    private ProductionRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.production-request.deleted";

    private ProductionRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.production-request.updated";

    private ProductionRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CommittedEvent implements Event {

    public final static String CHANNEL = "event.production-request.committed";

    private ProductionRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class AcceptedEvent implements Event {

    public final static String CHANNEL = "event.production-request.accepted";

    private ProductionRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CanceledEvent implements Event {

    public final static String CHANNEL = "event.production-request.canceled";

    private ProductionRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class ProgressedEvent implements Event {

    public final static String CHANNEL = "event.production-request.progressed";

    private ProductionRequestId id;

    private BigDecimal progressRate;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CompletedEvent implements Event {

    public final static String CHANNEL = "event.production-request.completed";

    private ProductionRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class PlannedEvent implements Event {

    public final static String CHANNEL = "event.production-request.planned";

    private ProductionRequestId id;

    public String channel() {
      return CHANNEL;
    }

  }
}
