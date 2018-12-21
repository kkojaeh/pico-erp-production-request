package pico.erp.production.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pico.erp.shared.data.LocalizedNameable;

@AllArgsConstructor
public enum ProductionRequestStatusKind implements LocalizedNameable {

  /**
   * 주문이 접수가 생성됨을 의미
   */
  CREATED(true, true, true),

  /**
   * 제출 함
   */
  COMMITTED(false, false, true),

  /**
   * 취소 됨
   */
  CANCELED(false, false, false),

  /**
   * 진행중
   */
  IN_PROGRESS(false, false, false),

  /**
   * 생산완료
   */
  COMPLETED(false, false, false);

  @Getter
  private final boolean updatable;

  @Getter
  private final boolean committable;

  @Getter
  private final boolean cancelable;

}
