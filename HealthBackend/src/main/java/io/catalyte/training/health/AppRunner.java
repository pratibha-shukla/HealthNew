package io.catalyte.training.health;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class is the main application. Running this class will start the SpringBoot server.
 */
@SpringBootApplication
public class AppRunner {

  /**
   * The main method
   */
  public static void main(String[] args) {
    SpringApplication.run(AppRunner.class);
  }

}
