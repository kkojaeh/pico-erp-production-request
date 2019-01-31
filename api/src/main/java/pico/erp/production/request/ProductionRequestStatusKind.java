package pico.erp.production.request;

import lombok.AllArgsConstructor;
import pico.erp.shared.data.LocalizedNameable;

@AllArgsConstructor
public enum ProductionRequestStatusKind implements LocalizedNameable {

  /**
   * 주문이 접수가 생성됨을 의미
   */
  CREATED,

  /**
   * 제출 함
   */
  COMMITTED,

  /**
   * 취소 됨
   */
  CANCELED,

  /**
   * 접수 됨
   */
  ACCEPTED,

  /**
   * 계획중
   */
  IN_PLANNING,

  /**
   * 진행중
   */
  IN_PROGRESS,

  /**
   * 생산완료
   */
  COMPLETED;

  public boolean isAcceptable() {
    return this == COMMITTED;
  }

  public boolean isCancelable() {
    return this == CREATED || this == COMMITTED || this == ACCEPTED || this == IN_PLANNING;
  }

  public boolean isCommittable() {
    return this == CREATED;
  }

  public boolean isCompletable() {
    return this == IN_PROGRESS;
  }

  public boolean isPlannable() {
    return this == ACCEPTED;
  }

  public boolean isUpdatable() {
    return this == CREATED;
  }

  public boolean isProgressable() {
    return this == IN_PLANNING;
  }

}
