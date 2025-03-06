package platform;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import platform.worker.PlatformWorker;

@SpringBootApplication
public class PlatformServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlatformServiceApplication.class, args);
    }

//    @Bean
//    @Profile("platform-worker")
//    public CommandLineRunner startPlatformWorker(PlatformWorker platformWorker) {
//        return args -> platformWorker.start();
//    }
}
