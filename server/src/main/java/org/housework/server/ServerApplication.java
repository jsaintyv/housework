package org.housework.server;

import java.io.File;

import org.housework.server.models.RoleAuthority;
import org.housework.server.models.User;
import org.housework.server.models.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ServerApplication {
	private static String configurationJson = "./configuration.json";
	private static final Logger LOG = LoggerFactory.getLogger(ServerApplication.class);

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		if(args.length == 1 && args[1].endsWith(".json")) {			
			configurationJson = args[0];
		}
		
		SpringApplication.run(ServerApplication.class, args);
	}

	@Bean
	InitializingBean sendDatabase() {			
		LOG.info("### SendDatabase ### ");
		return () -> {
			ConfigurationService.getInstance().loadInstance(new File(configurationJson));
			
			LOG.info("### Controle base stat ### ");
			User user = userRepository.findByLogin("admin");
			if (user == null) {
				LOG.info("### Initialisation ### ");
				user = new User();
				user.setLogin("admin");
				user.setPassword(new BCryptPasswordEncoder().encode("admin"));
				user.setEnabled(true);
				
				user.setRole(RoleAuthority.ADMIN);
				userRepository.save(user);
				LOG.info("Admin password hash:" + user.getPassword());				
			}

		};
	}

}
