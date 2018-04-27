package io.github.lsolovey.lineserver.service;

import io.github.lsolovey.lineserver.LineServerTestBase;
import io.github.lsolovey.lineserver.data.entity.Line;
import io.github.lsolovey.lineserver.data.repository.LineRepository;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Unit tests for {@link LinesService}
 */
public class LinesServiceTests extends LineServerTestBase
{
    @Mock
    private LineRepository lineRepository;

    @InjectMocks
    private LinesService linesService;

    ///////////////////////////
    // saveLine() tests
    ///////////////////////////

    @Test
    public void testSaveLine()
    {
        Line line = new Line(1, "Test line");
        linesService.saveLine(line);

        ArgumentCaptor<Line> captor = ArgumentCaptor.forClass(Line.class);
        verify(lineRepository, times(1)).save(captor.capture());

        Line actualLine = captor.getValue();
        assertEquals(line, actualLine);
    }

    ///////////////////////////
    // deleteAllLines() tests
    ///////////////////////////

    @Test
    public void testDeleteAllLines()
    {
        linesService.deleteAllLines();
        verify(lineRepository, times(1)).deleteAll();
    }

    ///////////////////////////
    // getLineByIndex() tests
    ///////////////////////////

    @Test
    public void testGetLineByIndex() throws Exception
    {
        Line line = new Line(1, "Test line");
        when(lineRepository.findById(1)).thenReturn(Optional.of(line));

        Line actualLine = linesService.getLineByIndex(1);
        assertEquals(line, actualLine);
    }

    @Test(expected = LineNotFoundException.class)
    public void testGetLineByIndexTooLarge() throws Exception
    {
        when(lineRepository.findById(100)).thenReturn(Optional.empty());
        linesService.getLineByIndex(100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLineByIndexZero() throws Exception
    {
        linesService.getLineByIndex(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLineByIndexNegative() throws Exception
    {
        linesService.getLineByIndex(-1);
    }
}
