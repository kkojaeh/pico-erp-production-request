package pico.erp.production.request;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.production.plan.ProductionPlanId;
import pico.erp.production.plan.ProductionPlanRequests;
import pico.erp.production.plan.ProductionPlanService;

@SuppressWarnings("unused")
@Component
public class ProductionRequestEventListener {

  private static final String LISTENER_NAME = "listener.production-request-event-listener";

  @Lazy
  @Autowired
  private ProductionRequestService productionRequestService;

  @Lazy
  @Autowired
  private ProductionPlanService productionPlanService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + ProductionRequestEvents.AcceptedEvent.CHANNEL)
  public void onProductionRequestAccepted(ProductionRequestEvents.AcceptedEvent event) {
    val request = productionRequestService.get(event.getProductionRequestId());

    val planId = ProductionPlanId.generate();
    productionPlanService.create(
      ProductionPlanRequests.CreateRequest.builder()
        .id(planId)
        .itemId(request.getItemId())
        .projectId(request.getProjectId())
        .quantity(request.getQuantity())
        .spareQuantity(request.getSpareQuantity())
        .dueDate(request.getDueDate())
        .build()
    );

    productionRequestService.plan(
      ProductionRequestRequests.PlanRequest.builder()
        .id(event.getProductionRequestId())
        .planId(planId)
        .build()
    );
  }


}
