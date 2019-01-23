package pico.erp.production.request;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ProductionRequestCodeGeneratorImpl implements ProductionRequestCodeGenerator {

  @Lazy
  @Autowired
  private ProductionRequestRepository productionRequestRepository;

  public static String toAlphabeticRadix(int num) {
    char[] str = Integer.toString(num, 26).toCharArray();
    for (int i = 0; i < str.length; i++) {
      str[i] += str[i] > '9' ? 10 : 49;
    }
    return new String(str).toUpperCase();
  }

  @Override
  public ProductionRequestCode generate(ProductionRequest productionRequest) {
    val now = OffsetDateTime.now();
    val begin = now.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
    val end = now.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
    val count = productionRequestRepository.countCreatedBetween(begin, end);
    val code = String.format("%04d%02d-%04d", now.getYear(), now.getMonthValue(), count + 1)
      .toUpperCase();
    return ProductionRequestCode.from(code);
  }

}
