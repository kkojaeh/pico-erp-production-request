package pico.erp.production.request

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
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

import java.util.stream.Collectors
import java.util.stream.Stream

@SpringBootTest(classes = [ProductionRequestApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblings = [
  UserApplication, ItemApplication, ProjectApplication, ProcessApplication, CompanyApplication, BomApplication,
  OrderAcceptanceApplication, ProductionPlanApplication, ProductSpecificationApplication, DocumentApplication,
  AttachmentApplication
])
@Transactional
@Rollback
@ActiveProfiles("test")
class MessageSourceSpec extends Specification {

  @Autowired
  MessageSource messageSource

  def locale = LocaleContextHolder.locale

  def "생산 요청 상태"() {
    when:
    def messages = Stream.of(ProductionRequestStatusKind.values())
      .map({
      kind -> messageSource.getMessage(kind.nameCode, null, locale)
    }).collect(Collectors.toList())

    println messages

    then:
    messages.size() == 7
  }

}
