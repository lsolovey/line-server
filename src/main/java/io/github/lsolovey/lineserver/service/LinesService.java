package io.github.lsolovey.lineserver.service;

import io.github.lsolovey.lineserver.data.entity.Line;
import io.github.lsolovey.lineserver.data.repository.LineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Business service for operations on {@link Line} entities.
 * <p>
 * It's backed by the instance of {@link LineRepository}.
 */
@Service
public class LinesService
{
    private final LineRepository lineRepository;

    // NOTE: this class doesn't have a lot of business logic at this point.
    // It allows supporting more complex business operations in future.

    /**
     * Creates new instance of the lines service.
     *
     * @param lineRepository {@link LineRepository} to use for CRUD operations.
     */
    @Autowired
    public LinesService(LineRepository lineRepository)
    {
        this.lineRepository = lineRepository;
    }

    /**
     * Looks up line by the provided line index.
     * <p>
     * Index starts with 1, i.e. the first line of the file has {@code lineIndex = 1}.
     *
     * @param lineIndex Index of the line, positive integer.
     * @return Found line
     * @throws LineNotFoundException if line with {@code lineIndex} is not found
     * @throws IllegalArgumentException if {@code lineIndex} is less or equal to zero
     */
    public Line getLineByIndex(int lineIndex) throws LineNotFoundException
    {
        if (lineIndex <= 0) {
            throw new IllegalArgumentException("Illegal line index.");
        }

        Optional<Line> optional = lineRepository.findById(lineIndex);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new LineNotFoundException(lineIndex);
        }
    }

    /**
     * Saves {@link Line} entity to the repository.
     *
     * @param line Line to save
     */
    public void saveLine(Line line)
    {
        lineRepository.save(line);
    }

    /**
     * Deletes all currently loaded lines from the repository.
     */
    public void deleteAllLines()
    {
        lineRepository.deleteAll();
    }
}
