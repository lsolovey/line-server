package io.github.lsolovey.lineserver;

import io.github.lsolovey.lineserver.service.FileLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Main application class.
 */
@SpringBootApplication
public class LineServerApplication implements CommandLineRunner
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LineServerApplication.class);

    @Autowired
    private FileLoaderService fileLoaderService;

    /**
     * Main application entry point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args)
    {
        SpringApplication.run(LineServerApplication.class, args);
    }

    /**
     * Triggers loading of lines from the text file or resource
     * (if file path is provided as the first command line argument).
     * <p>
     * This methods is invoked by Spring eco-system during the application startup.
     *
     * @param args Command line arguments
     * @throws IOException When neither file nor resource are found, or general IO problem occurs
     */
    @Override
    public void run(String... args) throws IOException
    {
        // load lines from file (specified as command-line argument)
        if (args != null && args.length > 0) {
            fileLoaderService.loadFromFile(args[0]);
        } else {
            LOGGER.info("Starting LineServerApplication without loading lines from file. Application will use lines previously saved in the database.");
        }
    }
}
