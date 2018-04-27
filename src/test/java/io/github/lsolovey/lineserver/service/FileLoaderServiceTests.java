package io.github.lsolovey.lineserver.service;

import io.github.lsolovey.lineserver.LineServerTestBase;
import io.github.lsolovey.lineserver.data.entity.Line;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Unit tests for {@link FileLoaderService}
 */
public class FileLoaderServiceTests extends LineServerTestBase
{
    @Mock
    private LinesService linesService;

    @InjectMocks
    private FileLoaderService fileLoaderService;

    ///////////////////////////
    // loadFromFile() tests
    ///////////////////////////

    @Test
    public void testLoadFromFile() throws Exception
    {
        fileLoaderService.loadFromFile(SAMPLE_FILE);

        ArgumentCaptor<Line> captor = ArgumentCaptor.forClass(Line.class);

        InOrder inOrder = inOrder(linesService);
        inOrder.verify(linesService, times(1)).deleteAllLines();
        inOrder.verify(linesService, times(4)).saveLine(captor.capture());

        List<Line> actualLines = captor.getAllValues();

        assertEquals(4, actualLines.size());
        assertEquals(new Line(1, "This is the first line"), actualLines.get(0));
        assertEquals(new Line(2, "This is the second line"), actualLines.get(1));
        assertEquals(new Line(3, ""), actualLines.get(2));
        assertEquals(new Line(4, "This is the fourth line"), actualLines.get(3));
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadFromFileNotFound() throws IOException
    {
        try {
            fileLoaderService.loadFromFile("InvalidFileName");
        } finally {
            ArgumentCaptor<Line> captor = ArgumentCaptor.forClass(Line.class);
            verify(linesService, never()).deleteAllLines();
            verify(linesService, never()).saveLine(captor.capture());
        }
    }
}
