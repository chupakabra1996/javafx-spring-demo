package ru.kpfu.itis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.kpfu.itis.model.Item;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"ru.kpfu.itis.repo"}) // narrow down the scope of JPA repositories
public class JavafxRestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavafxRestServiceApplication.class, args);
    }
}
