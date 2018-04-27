package io.github.lsolovey.lineserver.data.entity;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Objects;

/**
 * Line entity represents a single line from the text file.
 * <p>
 * Contains the following data:
 * <ul>
 *     <li>Line Index. The first line of the file has {@code lineIndex = 1}.</li>
 *     <li>Line Text. Content of the line.</li>
 * </ul>
 *
 * It is mapped to {@code line} table in Cassandra database.
 */
@Table("line")
public class Line
{
    @PrimaryKey("line_index")
    private final int lineIndex;

    @Column("line_text")
    private final String lineText;

    /**
     * Creates new line by index and content.
     *
     * @param lineIndex Index of the line.
     * @param lineText Content of the line.
     */
    public Line(int lineIndex, String lineText)
    {
        this.lineIndex = lineIndex;
        this.lineText = lineText;
    }

    /**
     * @return line index.
     */
    public int getLineIndex()
    {
        return lineIndex;
    }

    /**
     * @return line content.
     */
    public String getLineText()
    {
        return lineText;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return lineIndex == line.lineIndex &&
                Objects.equals(lineText, line.lineText);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(lineIndex, lineText);
    }

    @Override
    public String toString()
    {
        return "Line{" +
                "lineIndex=" + lineIndex +
                ", lineText='" + lineText + '\'' +
                '}';
    }
}
