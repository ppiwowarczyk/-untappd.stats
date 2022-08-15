package biz.piwowarczyk.untappd.stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(UntappdConfig.class)
public class UntappdStatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UntappdStatsApplication.class, args);
	}

}
