package pico.erp.production.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.production.plan.ProductionPlanId;

public interface ProductionRequestService {


  void commit(@Valid @NotNull ProductionRequestRequests.CommitRequest request);

  void cancel(@Valid @NotNull ProductionRequestRequests.CancelRequest request);

  ProductionRequestData create(@Valid @NotNull ProductionRequestRequests.CreateRequest request);

  void accept(@Valid @NotNull ProductionRequestRequests.AcceptRequest request);

  boolean exists(@Valid @NotNull ProductionRequestId id);

  boolean exists(@Valid @NotNull ProductionPlanId planId);

  ProductionRequestData get(@Valid @NotNull ProductionRequestId id);

  ProductionRequestData get(@Valid @NotNull ProductionPlanId planId);

  void update(@Valid @NotNull ProductionRequestRequests.UpdateRequest request);

  void progress(@Valid @NotNull ProductionRequestRequests.ProgressRequest request);

  void complete(@Valid @NotNull ProductionRequestRequests.CompleteRequest request);

  void plan(@Valid @NotNull ProductionRequestRequests.PlanRequest request);



}
