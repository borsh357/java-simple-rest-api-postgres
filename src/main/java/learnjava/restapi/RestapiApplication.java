package learnjava.restapi;

import learnjava.restapi.controllers.UsersController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class RestapiApplication {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(RestapiApplication.class, args);
		UsersController Users = new UsersController();
	}

}
