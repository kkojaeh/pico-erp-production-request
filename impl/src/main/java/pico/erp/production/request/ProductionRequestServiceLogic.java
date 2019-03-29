package pico.erp.production.request;

import kkojaeh.spring.boot.component.Give;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.production.plan.ProductionPlanId;
import pico.erp.production.request.ProductionRequestRequests.CancelRequest;
import pico.erp.production.request.ProductionRequestRequests.CompleteRequest;
import pico.erp.production.request.ProductionRequestRequests.PlanRequest;
import pico.erp.production.request.ProductionRequestRequests.ProgressRequest;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@Give
@Transactional
@Validated
public class ProductionRequestServiceLogic implements ProductionRequestService {

  @Autowired
  private ProductionRequestRepository productionRequestRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private ProductionRequestMapper mapper;

  @Override
  public void commit(ProductionRequestRequests.CommitRequest request) {
    val productionRequest = productionRequestRepository.findBy(request.getId())
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
    val response = productionRequest.apply(mapper.map(request));
    productionRequestRepository.update(productionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void cancel(CancelRequest request) {
    val productionRequest = productionRequestRepository.findBy(request.getId())
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
    val response = productionRequest.apply(mapper.map(request));
    productionRequestRepository.update(productionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public ProductionRequestData create(ProductionRequestRequests.CreateRequest request) {
    val productionRequest = new ProductionRequest();
    val response = productionRequest.apply(mapper.map(request));
    if (productionRequestRepository.exists(productionRequest.getId())) {
      throw new ProductionRequestExceptions.AlreadyExistsException();
    }
    val created = productionRequestRepository.create(productionRequest);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void accept(ProductionRequestRequests.AcceptRequest request) {
    val productionRequest = productionRequestRepository.findBy(request.getId())
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
    val response = productionRequest.apply(mapper.map(request));
    productionRequestRepository.update(productionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(ProductionRequestId id) {
    return productionRequestRepository.exists(id);
  }

  @Override
  public boolean exists(ProductionPlanId planId) {
    return productionRequestRepository.exists(planId);
  }

  @Override
  public ProductionRequestData get(ProductionRequestId id) {
    return productionRequestRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
  }

  @Override
  public ProductionRequestData get(ProductionPlanId planId) {
    return productionRequestRepository.findBy(planId)
      .map(mapper::map)
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
  }

  @Override
  public void update(ProductionRequestRequests.UpdateRequest request) {
    val productionRequest = productionRequestRepository.findBy(request.getId())
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
    val response = productionRequest.apply(mapper.map(request));
    productionRequestRepository.update(productionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void progress(ProgressRequest request) {
    val productionRequest = productionRequestRepository.findBy(request.getId())
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
    val response = productionRequest.apply(mapper.map(request));
    productionRequestRepository.update(productionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void complete(CompleteRequest request) {
    val productionRequest = productionRequestRepository.findBy(request.getId())
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
    val response = productionRequest.apply(mapper.map(request));
    productionRequestRepository.update(productionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void plan(PlanRequest request) {
    val productionRequest = productionRequestRepository.findBy(request.getId())
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
    val response = productionRequest.apply(mapper.map(request));
    productionRequestRepository.update(productionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }
}
