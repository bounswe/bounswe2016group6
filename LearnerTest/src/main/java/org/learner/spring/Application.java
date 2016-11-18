package org.learner.spring;

import org.learner.storage.FileSystemStorageService;
import org.learner.storage.StorageProperties;
import org.learner.storage.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;


@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Application extends SpringBootServletInitializer {
    @Value("${staticpath}")
    private String staticPath;
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
    
    @Bean
    public StorageService storageService(){
    	System.out.println("Bean init " + staticPath);
    	return new FileSystemStorageService(staticPath);
    }
    
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			System.out.println("StorageServiceInit");
			
            //storageService.deleteAll();
           // storageService.init();
		};
	}
}