package pico.erp.production.request;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.audit.AuditService;
import pico.erp.production.request.ProductionRequestRequests.CancelRequest;
import pico.erp.production.request.ProductionRequestRequests.CompleteRequest;
import pico.erp.production.request.ProductionRequestRequests.ProgressRequest;
import pico.erp.project.ProjectExceptions.NotFoundException;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class ProductionRequestServiceLogic implements ProductionRequestService {

  @Autowired
  private ProductionRequestRepository productionRequestRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private ProductionRequestMapper mapper;

  @Lazy
  @Autowired
  private AuditService auditService;

  @Override
  public void commit(ProductionRequestRequests.CommitRequest request) {
    val productionRequest = productionRequestRepository.findBy(request.getId())
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
    val response = productionRequest.apply(mapper.map(request));
    productionRequestRepository.update(productionRequest);
    auditService.commit(productionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void cancel(CancelRequest request) {
    val productionRequest = productionRequestRepository.findBy(request.getId())
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
    val response = productionRequest.apply(mapper.map(request));
    productionRequestRepository.update(productionRequest);
    auditService.commit(productionRequest);
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
    auditService.commit(created);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(ProductionRequestRequests.DeleteRequest request) {
    val productionRequest = productionRequestRepository.findBy(request.getId())
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
    val response = productionRequest.apply(mapper.map(request));
    productionRequestRepository.update(productionRequest);
    auditService.delete(productionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(ProductionRequestId id) {
    return productionRequestRepository.exists(id);
  }

  @Override
  public ProductionRequestData get(ProductionRequestId id) {
    return productionRequestRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
  }

  @Override
  public void update(ProductionRequestRequests.UpdateRequest request) {
    val productionRequest = productionRequestRepository.findBy(request.getId())
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
    val response = productionRequest.apply(mapper.map(request));
    productionRequestRepository.update(productionRequest);
    auditService.commit(productionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void progress(ProgressRequest request) {
    val productionRequest = productionRequestRepository.findBy(request.getId())
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
    val response = productionRequest.apply(mapper.map(request));
    productionRequestRepository.update(productionRequest);
    auditService.commit(productionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void complete(CompleteRequest request) {
    val productionRequest = productionRequestRepository.findBy(request.getId())
      .orElseThrow(ProductionRequestExceptions.NotFoundException::new);
    val response = productionRequest.apply(mapper.map(request));
    productionRequestRepository.update(productionRequest);
    auditService.commit(productionRequest);
    eventPublisher.publishEvents(response.getEvents());
  }
}
