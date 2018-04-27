package io.github.lsolovey.lineserver.service;

import io.github.lsolovey.lineserver.data.entity.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * File loader service reads lines from text files and passes them to {@link LinesService} for saving.
 */
@Service
public class FileLoaderService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileLoaderService.class);

    private final LinesService linesService;

    /**
     * Creates new instance of the file loader service.
     *
     * @param linesService {@link LinesService} to use for saving lines.
     */
    @Autowired
    public FileLoaderService(LinesService linesService)
    {
        this.linesService = linesService;
    }

    /**
     * Reads lines from text file and passes them to {@link LinesService} for saving.
     * <p>
     * Deletes all previously saved lines to avoid collisions with previously loaded file.
     *
     * @param filePath File path or resource name to load lines from
     * @throws IOException When neither file nor resource are found, or general IO problem occurs
     */
    public void loadFromFile(String filePath) throws IOException
    {
        InputStream inputStream = getInputStream(filePath);

        LOGGER.info("Loading lines from: " + filePath);

        // delete all previously loaded lines if file is found
        linesService.deleteAllLines();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            int index = 1;
            while ((line = bufferedReader.readLine()) != null) {
                linesService.saveLine(new Line(index++, line));
            }
        }

        LOGGER.info("Loading lines completed");
    }

    /**
     * Opens the input stream for the specified {@code filePath}.
     * <p>
     * This method tries to find the file with specified {@code filePath} first,
     * and if the file is not found then tries to look it up as resource in the classpath.
     *
     * @param filePath File path or resource name
     * @return Opened input stream
     * @throws FileNotFoundException When neither file nor resource are found
     */
    private InputStream getInputStream(String filePath) throws FileNotFoundException
    {
        File file = new File(filePath);
        InputStream inputStream;

        if (file.exists()) {
            inputStream = new FileInputStream(file);
        } else {
            // try read as resource for unit tests
            inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        }

        if (inputStream == null) {
            throw new FileNotFoundException(String.format("Input file not found: %s", filePath));
        }

        return inputStream;
    }
}
