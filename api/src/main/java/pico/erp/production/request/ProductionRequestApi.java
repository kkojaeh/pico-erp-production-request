package pico.erp.production.request;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.ApplicationId;
import pico.erp.shared.data.Role;

public final class ProductionRequestApi {

  public final static ApplicationId ID = ApplicationId.from("production-request");

  @RequiredArgsConstructor
  public enum Roles implements Role {

    PRODUCTION_REQUEST_MANAGER,
    PRODUCTION_REQUESTER,
    PRODUCTION_REQUEST_ACCEPTER;

    @Id
    @Getter
    private final String id = name();

  }
}
