package pico.erp.production.request;

import java.math.BigDecimal;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.production.plan.ProductionPlanEvents;
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

  /**
   * 생산 계획이 취소되면 생산 요청도 취소됨
   */
  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + ProductionPlanEvents.CanceledEvent.CHANNEL)
  public void onProductionPlanCanceled(ProductionPlanEvents.CanceledEvent event) {
    val exists = productionRequestService.exists(event.getId());
    if (exists) {
      val request = productionRequestService.get(event.getId());
      if (request.isCancelable()) {
        productionRequestService.cancel(
          ProductionRequestRequests.CancelRequest.builder()
            .id(request.getId())
            .build()
        );
      }
    }
  }

  /**
   * 생산 계획이 완료되면 요청도 완료상태로 변경
   */
  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + ProductionPlanEvents.CompletedEvent.CHANNEL)
  public void onProductionPlanCompleted(ProductionPlanEvents.CompletedEvent event) {
    val planId = event.getId();
    val exists = productionRequestService.exists(planId);
    if (exists) {
      val request = productionRequestService.get(planId);
      if (request.isCompletable()) {
        productionRequestService.complete(
          ProductionRequestRequests.CompleteRequest.builder()
            .id(request.getId())
            .build()
        );
      }
    }
  }

  /**
   * 생산 계획이 확정되면 요청을 진행상태로 변경
   */
  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + ProductionPlanEvents.DeterminedEvent.CHANNEL)
  public void onProductionPlanDetermined(ProductionPlanEvents.DeterminedEvent event) {
    val exists = productionRequestService.exists(event.getId());
    if (exists) {
      val request = productionRequestService.get(event.getId());
      if (request.isProgressable()) {
        productionRequestService.progress(
          ProductionRequestRequests.ProgressRequest.builder()
            .id(request.getId())
            .progressRate(BigDecimal.ZERO)
            .build()
        );
      }
    }
  }

  /**
   * 생산 계획이 진행되면 요청도 진행상태로 변경
   */
  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + ProductionPlanEvents.ProgressedEvent.CHANNEL)
  public void onProductionPlanProgressed(ProductionPlanEvents.ProgressedEvent event) {
    val plan = productionPlanService.get(event.getId());
    val exists = productionRequestService.exists(plan.getId());
    if (exists) {
      val request = productionRequestService.get(plan.getId());
      if (request.isProgressable()) {
        productionRequestService.progress(
          ProductionRequestRequests.ProgressRequest.builder()
            .id(request.getId())
            .progressRate(plan.getProgressRate())
            .build()
        );
      }
    }
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + ProductionRequestEvents.AcceptedEvent.CHANNEL)
  public void onProductionRequestAccepted(ProductionRequestEvents.AcceptedEvent event) {
    val request = productionRequestService.get(event.getId());

    val planId = ProductionPlanId.generate();
    productionPlanService.create(
      ProductionPlanRequests.CreateRequest.builder()
        .id(planId)
        .itemId(request.getItemId())
        .projectId(request.getProjectId())
        .quantity(request.getQuantity())
        .spareQuantity(request.getSpareQuantity())
        .dueDate(request.getDueDate())
        .receiverId(request.getReceiverId())
        .unit(request.getUnit())
        .plannerId(request.getAccepterId())
        .build()
    );

    productionRequestService.plan(
      ProductionRequestRequests.PlanRequest.builder()
        .id(event.getId())
        .planId(planId)
        .build()
    );
  }

  /**
   * 생산 요청이 취소되면 계획도 취소시킴
   */
  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + ProductionRequestEvents.CanceledEvent.CHANNEL)
  public void onProductionRequestCanceled(ProductionRequestEvents.CanceledEvent event) {
    val exists = productionRequestService.exists(event.getId());
    if (exists) {
      val request = productionRequestService.get(event.getId());
      if (request.getPlanId() != null) {
        val plan = productionPlanService.get(request.getPlanId());
        if (plan.isCancelable()) {
          productionPlanService.cancel(
            ProductionPlanRequests.CancelRequest.builder()
              .id(plan.getId())
              .build()
          );
        }
      }
    }
  }


}
