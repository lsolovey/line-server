package io.github.lsolovey.lineserver.service;

/**
 * Exception thrown when application can't find the line by the provided {@code lineIndex}.
 */
public class LineNotFoundException extends Exception
{
    /**
     * Creates exception.
     *
     * @param lineIndex Index of the line that isn't found.
     */
    public LineNotFoundException(int lineIndex)
    {
        super(String.format("Line %d not found", lineIndex));
    }
}
