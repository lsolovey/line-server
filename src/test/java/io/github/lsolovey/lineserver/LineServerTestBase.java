package io.github.lsolovey.lineserver;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * Base class for LineServer unit tests.
 */
public abstract class LineServerTestBase
{
    public static final String SAMPLE_FILE = "sample-file.txt";

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }
}
