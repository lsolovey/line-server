package io.github.lsolovey.lineserver;

import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitDependencyInjectionTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests verifying end-to-end workflow by invoking REST service through HTTP requests.
 * <p>
 * It uses embedded instance of Cassandra as the database.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners(listeners = {
        CassandraUnitDependencyInjectionTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class })
@CassandraDataSet(keyspace = "lineServer", value = "keyspace.cql")
@EmbeddedCassandra(timeout = 60000)
public class LineServerApplicationTests
{
    @Autowired
    private LineServerApplication lineServerApplication;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void setUp() throws Exception
    {
        lineServerApplication.run(LineServerTestBase.SAMPLE_FILE);
    }

	@Test
	public void testBadRequests()
	{
	    // test negative index
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/lines/-1", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST); // HTTP 400
        assertThat(entity.getBody()).isEqualTo("Illegal line index.");

        // test zero index
        entity = this.restTemplate.getForEntity("/lines/0", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST); // HTTP 400
        assertThat(entity.getBody()).isEqualTo("Illegal line index.");

        // test bad index format
        entity = this.restTemplate.getForEntity("/lines/abc", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST); // HTTP 400
        assertThat(entity.getBody()).contains("For input string: \"abc\"");
	}

    @Test
    public void testValidLineIndex()
    {
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/lines/1", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK); // HTTP 200
        assertThat(entity.getBody()).isEqualTo("This is the first line");

        entity = this.restTemplate.getForEntity("/lines/2", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK); // HTTP 200
        assertThat(entity.getBody()).isEqualTo("This is the second line");

        entity = this.restTemplate.getForEntity("/lines/3", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK); // HTTP 200
        assertThat(entity.getBody()).isNull();

        entity = this.restTemplate.getForEntity("/lines/4", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK); // HTTP 200
        assertThat(entity.getBody()).isEqualTo("This is the fourth line");
    }

    @Test
    public void testLineIndexBeyondFileLimit()
    {
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/lines/5", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE); // HTTP 413
        assertThat(entity.getBody()).isEqualTo("Line 5 not found");
    }
}
