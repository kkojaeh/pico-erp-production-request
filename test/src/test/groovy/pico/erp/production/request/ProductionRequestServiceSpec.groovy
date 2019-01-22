package pico.erp.production.request

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.item.ItemId
import pico.erp.project.ProjectId
import pico.erp.shared.IntegrationConfiguration
import pico.erp.user.UserId
import spock.lang.Specification

import java.time.OffsetDateTime

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class ProductionRequestServiceSpec extends Specification {

  @Autowired
  ProductionRequestService requestService

  def requestId = ProductionRequestId.from("request-1")

  def unknownRequestId = ProductionRequestId.from("unknown")

  def itemId = ItemId.from("toothbrush-0")

  def dueDate = OffsetDateTime.now().plusDays(7)

  def committerId = UserId.from("kjh")

  def accepterId = UserId.from("kjh")

  def cancelerId = UserId.from("kjh")

  def projectId = ProjectId.from("sample-project1")

  def setup() {
    requestService.create(
      new ProductionRequestRequests.CreateRequest(
        id: requestId,
        itemId: itemId,
        asap: true,
        quantity: 100,
        spareQuantity: 10,
        dueDate: dueDate,
        projectId: projectId
      )
    )
  }

  def updateRequest() {
    requestService.update(
      new ProductionRequestRequests.UpdateRequest(
        id: requestId,
        itemId: itemId,
        asap: false,
        quantity: 100,
        spareQuantity: 5,
        dueDate: dueDate,
        projectId: projectId
      )
    )
  }

  def commitRequest() {
    requestService.commit(
      new ProductionRequestRequests.CommitRequest(
        id: requestId,
        committerId: committerId
      )
    )
  }

  def completeRequest() {
    requestService.complete(
      new ProductionRequestRequests.CompleteRequest(
        id: requestId
      )
    )
  }

  def cancelRequest() {
    requestService.cancel(
      new ProductionRequestRequests.CancelRequest(
        id: requestId,
        cancelerId: cancelerId
      )
    )
  }

  def acceptRequest() {
    requestService.accept(
      new ProductionRequestRequests.AcceptRequest(
        id: requestId,
        accepterId: accepterId
      )
    )
  }

  def progressRequest(rate) {
    requestService.progress(
      new ProductionRequestRequests.ProgressRequest(
        id: requestId,
        progressRate: rate
      )
    )
  }

  def "존재 - 아이디로 존재 확인"() {
    when:
    def exists = requestService.exists(requestId)

    then:
    exists == true
  }

  def "존재 - 존재하지 않는 아이디로 확인"() {
    when:
    def exists = requestService.exists(unknownRequestId)

    then:
    exists == false
  }

  def "조회 - 아이디로 조회"() {
    when:
    def request = requestService.get(requestId)

    then:
    request.itemId == itemId
    request.quantity == 100
    request.spareQuantity == 10
    request.dueDate == dueDate
    request.asap == true
    request.projectId == projectId
  }

  def "조회 - 존재하지 않는 아이디로 조회"() {
    when:
    requestService.get(unknownRequestId)

    then:
    thrown(ProductionRequestExceptions.NotFoundException)
  }

  def "수정 - 제출 후 수정 불가"() {
    when:
    commitRequest()
    updateRequest()

    then:
    thrown(ProductionRequestExceptions.CannotUpdateException)
  }

  def "수정 - 취소 후 수정 불가"() {
    when:
    cancelRequest()
    updateRequest()

    then:
    thrown(ProductionRequestExceptions.CannotUpdateException)
  }

  def "수정 - 진행 중 수정 불가"() {
    when:
    commitRequest()
    acceptRequest()
    progressRequest(0.1)
    updateRequest()

    then:
    thrown(ProductionRequestExceptions.CannotUpdateException)
  }

  def "수정 - 완료 후 수정 불가"() {
    when:
    commitRequest()
    acceptRequest()
    progressRequest(0.5)
    completeRequest()
    updateRequest()

    then:
    thrown(ProductionRequestExceptions.CannotUpdateException)
  }

  def "수정 - 생성 후 수정"() {
    when:
    updateRequest()
    def request = requestService.get(requestId)

    then:
    request.itemId == itemId
    request.quantity == 100
    request.spareQuantity == 5
    request.dueDate == dueDate
    request.asap == false
    request.projectId == projectId
  }

  def "제출 - 제출 후 제출 불가"() {
    when:
    commitRequest()
    commitRequest()

    then:
    thrown(ProductionRequestExceptions.CannotCommitException)
  }

  def "제출 - 취소 후 제출 불가"() {
    when:
    cancelRequest()
    commitRequest()

    then:
    thrown(ProductionRequestExceptions.CannotCommitException)
  }

  def "제출 - 진행 중 제출 불가"() {
    when:
    commitRequest()
    acceptRequest()
    progressRequest(0.1)
    commitRequest()

    then:
    thrown(ProductionRequestExceptions.CannotCommitException)
  }

  def "제출 - 완료 후 제출 불가"() {
    when:
    commitRequest()
    acceptRequest()
    progressRequest(0.5)
    completeRequest()
    commitRequest()

    then:
    thrown(ProductionRequestExceptions.CannotCommitException)
  }

  def "취소 - 취소 후 취소 불가"() {
    when:
    cancelRequest()
    cancelRequest()

    then:
    thrown(ProductionRequestExceptions.CannotCancelException)
  }

  def "취소 - 진행 중 취소 불가"() {
    when:
    commitRequest()
    acceptRequest()
    progressRequest(0.1)
    cancelRequest()

    then:
    thrown(ProductionRequestExceptions.CannotCancelException)
  }

  def "취소 - 완료 휴 취소 불가"() {
    when:
    commitRequest()
    acceptRequest()
    progressRequest(0.5)
    completeRequest()
    cancelRequest()

    then:
    thrown(ProductionRequestExceptions.CannotCancelException)
  }

}
