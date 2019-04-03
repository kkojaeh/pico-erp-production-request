package pico.erp.production.request;

import kkojaeh.spring.boot.component.ComponentBean;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import pico.erp.user.group.GroupData;

@ComponentBean
@Data
@Configuration
@ConfigurationProperties("production-request")
public class ProductionRequestPropertiesImpl implements ProductionRequestProperties {

  GroupData accepterGroup;

}
