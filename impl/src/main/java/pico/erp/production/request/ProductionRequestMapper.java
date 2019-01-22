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
    @Mapping(target = "committerId", source = "committer.id"),
    @Mapping(target = "cancelerId", source = "canceler.id"),
    @Mapping(target = "accepterId", source = "accepter.id"),
    @Mapping(target = "orderAcceptanceId", source = "orderAcceptance.id"),
    @Mapping(target = "projectId", source = "project.id")
  })
  public abstract ProductionRequestData map(ProductionRequest domain);


  @Mappings({
    @Mapping(target = "planId", source = "plan.id"),
    @Mapping(target = "projectId", source = "project.id"),
    @Mapping(target = "itemId", source = "item.id"),
    @Mapping(target = "committerId", source = "committer.id"),
    @Mapping(target = "cancelerId", source = "canceler.id"),
    @Mapping(target = "accepterId", source = "accepter.id"),
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
      .spareQuantity(entity.getSpareQuantity())
      .dueDate(entity.getDueDate())
      .asap(entity.isAsap())
      .project(map(entity.getProjectId()))
      .status(entity.getStatus())
      .committer(map(entity.getCommitterId()))
      .committedDate(entity.getCommittedDate())
      .canceler(map(entity.getCancelerId()))
      .canceledDate(entity.getCanceledDate())
      .accepter(map(entity.getAccepterId()))
      .acceptedDate(entity.getAcceptedDate())
      .plan(map(entity.getPlanId()))
      .build();
  }

  @Mappings({
    @Mapping(target = "bom", source = "id"),
    @Mapping(target = "accepter", source = "accepterId")
  })
  public abstract ProductionRequestMessages.Accept.Request map(
    ProductionRequestRequests.AcceptRequest request);

  @Mappings({
    @Mapping(target = "committer", source = "committerId")
  })
  public abstract ProductionRequestMessages.Commit.Request map(
    ProductionRequestRequests.CommitRequest request);

  @Mappings({
    @Mapping(target = "canceler", source = "cancelerId")
  })
  public abstract ProductionRequestMessages.Cancel.Request map(
    ProductionRequestRequests.CancelRequest request);

  @Mappings({
  })
  public abstract ProductionRequestMessages.Progress.Request map(
    ProductionRequestRequests.ProgressRequest request);

  @Mappings({
  })
  public abstract ProductionRequestMessages.Complete.Request map(
    ProductionRequestRequests.CompleteRequest request);

  @Mappings({
    @Mapping(target = "plan", source = "planId")
  })
  public abstract ProductionRequestMessages.Plan.Request map(
    ProductionRequestRequests.PlanRequest request);

  @Mappings({
    @Mapping(target = "item", source = "itemId"),
    @Mapping(target = "project", source = "projectId"),
    @Mapping(target = "orderAcceptance", source = "orderAcceptanceId"),
    @Mapping(target = "codeGenerator", expression = "java(productionRequestCodeGenerator)")
  })
  public abstract ProductionRequestMessages.Create.Request map(
    ProductionRequestRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "item", source = "itemId"),
    @Mapping(target = "project", source = "projectId")
  })
  public abstract ProductionRequestMessages.Update.Request map(
    ProductionRequestRequests.UpdateRequest request);

  public abstract void pass(ProductionRequestEntity from,
    @MappingTarget ProductionRequestEntity to);


}


