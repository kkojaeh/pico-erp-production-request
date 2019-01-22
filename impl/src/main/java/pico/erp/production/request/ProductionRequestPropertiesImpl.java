package pico.erp.production.request;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import pico.erp.shared.Public;
import pico.erp.user.group.GroupData;

@Public
@Data
@Configuration
@ConfigurationProperties("production-request")
public class ProductionRequestPropertiesImpl implements ProductionRequestProperties {

  GroupData accepterGroup;

}
