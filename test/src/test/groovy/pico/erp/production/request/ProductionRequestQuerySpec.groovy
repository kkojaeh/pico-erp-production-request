package pico.erp.production.request

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.shared.IntegrationConfiguration
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
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
