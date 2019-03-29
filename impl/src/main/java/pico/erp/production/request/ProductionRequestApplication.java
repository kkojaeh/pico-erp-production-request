package pico.erp.production.request;

import kkojaeh.spring.boot.component.Give;
import kkojaeh.spring.boot.component.SpringBootComponent;
import kkojaeh.spring.boot.component.SpringBootComponentBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pico.erp.production.request.ProductionRequestApi.Roles;
import pico.erp.shared.SharedConfiguration;
import pico.erp.shared.data.Role;

@Slf4j
@SpringBootComponent("production-request")
@EntityScan
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableJpaRepositories
@EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "dateTimeProvider")
@SpringBootApplication
@Import(value = {
  SharedConfiguration.class
})
public class ProductionRequestApplication {

  public static void main(String[] args) {
    new SpringBootComponentBuilder()
      .component(ProductionRequestApplication.class)
      .run(args);
  }

  @Bean
  @Give
  public Role productionRequestAccepterRole() {
    return Roles.PRODUCTION_REQUEST_ACCEPTER;
  }

  @Bean
  @Give
  public Role productionRequestManagerRole() {
    return Roles.PRODUCTION_REQUEST_MANAGER;
  }

  @Bean
  @Give
  public Role productionRequesterRole() {
    return Roles.PRODUCTION_REQUESTER;
  }


}
