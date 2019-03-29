package pico.erp.production.request

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.attachment.AttachmentApplication
import pico.erp.bom.BomApplication
import pico.erp.company.CompanyApplication
import pico.erp.document.DocumentApplication
import pico.erp.item.ItemApplication
import pico.erp.order.acceptance.OrderAcceptanceApplication
import pico.erp.process.ProcessApplication
import pico.erp.product.specification.ProductSpecificationApplication
import pico.erp.production.plan.ProductionPlanApplication
import pico.erp.project.ProjectApplication
import pico.erp.shared.TestParentApplication
import pico.erp.user.UserApplication
import spock.lang.Specification

@SpringBootTest(classes = [ProductionRequestApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblings = [
  UserApplication, ItemApplication, ProjectApplication, ProcessApplication, CompanyApplication, BomApplication,
  OrderAcceptanceApplication, ProductionPlanApplication, ProductSpecificationApplication, DocumentApplication,
  AttachmentApplication
])
@Transactional
@Rollback
@ActiveProfiles("test")
class ProductionRequestQuerySpec extends Specification {

  @Autowired
  ProductionRequestQuery productionRequestQuery

  def "조회 조건에 맞게 조회"() {
    expect:
    def page = productionRequestQuery.retrieve(condition, pageable)
    page.totalElements == totalElements

    where:
    condition                          | pageable               || totalElements
    new ProductionRequestView.Filter() | new PageRequest(0, 10) || 1
  }
}
