package pico.erp.production.request;

import static org.springframework.util.StringUtils.isEmpty;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.shared.jpa.QueryDslJpaSupport;

@Service
@Public
@Transactional(readOnly = true)
@Validated
public class ProductionRequestQueryJpa implements ProductionRequestQuery {


  private final QProductionRequestEntity productionRequest = QProductionRequestEntity.productionRequestEntity;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QueryDslJpaSupport queryDslJpaSupport;

  @Override
  public Page<ProductionRequestView> retrieve(ProductionRequestView.Filter filter,
    Pageable pageable) {
    val query = new JPAQuery<ProductionRequestView>(entityManager);
    val select = Projections.bean(ProductionRequestView.class,
      productionRequest.id,
      productionRequest.itemId,
      productionRequest.code,
      productionRequest.dueDate,
      productionRequest.asap,
      productionRequest.projectId,
      productionRequest.status,
      productionRequest.createdBy,
      productionRequest.createdDate,
      productionRequest.committerId,
      productionRequest.committedDate,
      productionRequest.cancelerId,
      productionRequest.canceledDate,
      productionRequest.accepterId,
      productionRequest.acceptedDate
    );
    query.select(select);
    query.from(productionRequest);

    val builder = new BooleanBuilder();

    if (!isEmpty(filter.getCode())) {
      builder.and(productionRequest.code.value
        .likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", filter.getCode(), "%")));
    }

    if (filter.getProjectId() != null) {
      builder.and(productionRequest.projectId.eq(filter.getProjectId()));
    }

    if (filter.getStartDueDate() != null) {
      builder.and(productionRequest.dueDate.goe(filter.getStartDueDate()));
    }
    if (filter.getEndDueDate() != null) {
      builder.and(productionRequest.dueDate.loe(filter.getEndDueDate()));
    }

    if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
      builder.and(productionRequest.status.in(filter.getStatuses()));
    }

    if (filter.getItemId() != null) {
      builder.and(productionRequest.itemId.eq(filter.getItemId()));
    }

    query.where(builder);
    return queryDslJpaSupport.paging(query, pageable, select);
  }
}