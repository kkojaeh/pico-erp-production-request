package pico.erp.production.request;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.bom.BomData;
import pico.erp.bom.BomService;
import pico.erp.company.CompanyData;
import pico.erp.company.CompanyId;
import pico.erp.company.CompanyService;
import pico.erp.item.ItemData;
import pico.erp.item.ItemId;
import pico.erp.item.ItemService;
import pico.erp.order.acceptance.OrderAcceptanceData;
import pico.erp.order.acceptance.OrderAcceptanceId;
import pico.erp.order.acceptance.OrderAcceptanceService;
import pico.erp.process.ProcessData;
import pico.erp.process.ProcessService;
import pico.erp.production.plan.ProductionPlanData;
import pico.erp.production.plan.ProductionPlanId;
import pico.erp.production.plan.ProductionPlanService;
import pico.erp.project.ProjectData;
import pico.erp.project.ProjectId;
import pico.erp.project.ProjectService;
import pico.erp.shared.data.Auditor;
import pico.erp.user.UserData;
import pico.erp.user.UserId;
import pico.erp.user.UserService;

@Mapper
public abstract class ProductionRequestMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Lazy
  @Autowired
  private CompanyService companyService;

  @Lazy
  @Autowired
  private UserService userService;

  @Lazy
  @Autowired
  private ProductionRequestRepository orderAcceptanceRepository;

  @Lazy
  @Autowired
  OrderAcceptanceService orderAcceptanceService;

  @Lazy
  @Autowired
  private ProjectService projectService;

  @Lazy
  @Autowired
  protected ItemService itemService;

  @Lazy
  @Autowired
  protected BomService bomService;

  @Autowired
  protected ProductionRequestCodeGenerator productionRequestCodeGenerator;

  @Lazy
  @Autowired
  protected ProductionPlanService productionPlanService;


  protected UserData map(UserId userId) {
    return Optional.ofNullable(userId)
      .map(userService::get)
      .orElse(null);
  }

  protected CompanyData map(CompanyId companyId) {
    return Optional.ofNullable(companyId)
      .map(companyService::get)
      .orElse(null);
  }

  protected OrderAcceptanceData map(OrderAcceptanceId orderAcceptanceId) {
    return Optional.ofNullable(orderAcceptanceId)
      .map(orderAcceptanceService::get)
      .orElse(null);
  }

  protected ProjectData map(ProjectId projectId) {
    return Optional.ofNullable(projectId)
      .map(projectService::get)
      .orElse(null);
  }

  public ProductionRequest map(ProductionRequestId requestId) {
    return Optional.ofNullable(requestId)
      .map(id -> orderAcceptanceRepository.findBy(id)
        .orElseThrow(ProductionRequestExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  protected ProductionPlanData map(ProductionPlanId planId) {
    return Optional.ofNullable(planId)
      .map(productionPlanService::get)
      .orElse(null);
  }


  protected ItemData map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(itemService::get)
      .orElse(null);
  }

  protected BomData bom(ItemId itemId) {
    if (itemId == null) {
      return null;
    }
    if (bomService.exists(itemId)) {
      return bomService.get(itemId);
    } else {
      return null;
    }
  }

  protected BomData bom(ProductionRequestId requestId) {
    return bom(map(requestId).getItem().getId());
  }

  @Mappings({
    @Mapping(target = "itemId", source = "item.id"),
    @Mapping(target = "planId", source = "plan.id"),
    @Mapping(target = "orderAcceptanceId", source = "orderAcceptance.id"),
    @Mapping(target = "customerId", source = "customer.id"),
    @Mapping(target = "purchaserId", source = "purchaser.id"),
    @Mapping(target = "receiverId", source = "receiver.id"),
    @Mapping(target = "projectId", source = "project.id")
  })
  public abstract ProductionRequestData map(ProductionRequest domain);


  @Mappings({
    @Mapping(target = "item", source = "itemId"),
    @Mapping(target = "customer", source = "customerId"),
    @Mapping(target = "purchaser", source = "purchaserId"),
    @Mapping(target = "receiver", source = "receiverId"),
    @Mapping(target = "project", source = "projectId"),
    @Mapping(target = "orderAcceptance", source = "orderAcceptanceId"),
    @Mapping(target = "codeGenerator", expression = "java(productionRequestCodeGenerator)")
  })
  public abstract ProductionRequestMessages.CreateRequest map(
    ProductionRequestRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "customer", source = "customerId"),
    @Mapping(target = "purchaser", source = "purchaserId"),
    @Mapping(target = "receiver", source = "receiverId"),
    @Mapping(target = "project", source = "projectId")
  })
  public abstract ProductionRequestMessages.UpdateRequest map(
    ProductionRequestRequests.UpdateRequest request);

  public abstract ProductionRequestMessages.DeleteRequest map(
    ProductionRequestRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "bom", source = "id"),
    @Mapping(target = "committedBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract ProductionRequestMessages.CommitRequest map(
    ProductionRequestRequests.CommitRequest request);

  @Mappings({
    @Mapping(target = "canceledBy", expression = "java(auditorAware.getCurrentAuditor())")
  })
  public abstract ProductionRequestMessages.CancelRequest map(
    ProductionRequestRequests.CancelRequest request);

  @Mappings({
  })
  public abstract ProductionRequestMessages.ProgressRequest map(
    ProductionRequestRequests.ProgressRequest request);

  @Mappings({
  })
  public abstract ProductionRequestMessages.CompleteRequest map(
    ProductionRequestRequests.CompleteRequest request);

  @Mappings({
    @Mapping(target = "planId", source = "plan.id"),
    @Mapping(target = "customerId", source = "customer.id"),
    @Mapping(target = "purchaserId", source = "purchaser.id"),
    @Mapping(target = "receiverId", source = "receiver.id"),
    @Mapping(target = "projectId", source = "project.id"),
    @Mapping(target = "itemId", source = "item.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract ProductionRequestEntity jpa(ProductionRequest data);

  public ProductionRequest jpa(ProductionRequestEntity entity) {
    return ProductionRequest.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .item(map(entity.getItemId()))
      .quantity(entity.getQuantity())
      .dueDate(entity.getDueDate())
      .asap(entity.isAsap())
      .customer(map(entity.getCustomerId()))
      .purchaser(map(entity.getPurchaserId()))
      .receiver(map(entity.getReceiverId()))
      .project(map(entity.getProjectId()))
      .deliveryAddress(entity.getDeliveryAddress())
      .deliveryMobilePhoneNumber(entity.getDeliveryMobilePhoneNumber())
      .deliveryTelephoneNumber(entity.getDeliveryTelephoneNumber())
      .status(entity.getStatus())
      .committedBy(entity.getCommittedBy())
      .committedDate(entity.getCommittedDate())
      .canceledBy(entity.getCanceledBy())
      .canceledDate(entity.getCanceledDate())
      .plan(map(entity.getPlanId()))
      .build();
  }

  public abstract void pass(ProductionRequestEntity from,
    @MappingTarget ProductionRequestEntity to);


}


