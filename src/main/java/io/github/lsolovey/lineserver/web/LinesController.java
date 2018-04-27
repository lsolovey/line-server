package io.github.lsolovey.lineserver.web;

import io.github.lsolovey.lineserver.data.entity.Line;
import io.github.lsolovey.lineserver.service.LineNotFoundException;
import io.github.lsolovey.lineserver.service.LinesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * REST controller serving lines by line index.
 */
@RestController
@RequestMapping(value = "/lines")
public class LinesController
{
    @Autowired
    private LinesService linesService;

    /**
     * Looks up line by the provided line index.
     * <p>
     * Index starts with 1, i.e. the first line of the file has {@code index = 1}.
     *
     * @param index Index of the line, positive integer.
     * @return Found line
     * @throws LineNotFoundException if line with {@code index} is not found
     * @throws IllegalArgumentException if {@code index} is less or equal to zero
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{index}")
    public String getLineByIndex(@PathVariable(value = "index") int index) throws LineNotFoundException
    {
        Line line = linesService.getLineByIndex(index);
        return line.getLineText();
    }

    ///////////////////////////////
    // Exception Handlers
    ///////////////////////////////
    
    @ExceptionHandler(value = {LineNotFoundException.class})
    protected ResponseEntity<Object> handlePayloadTooLarge(Exception ex)
    {
        // HTTP 413
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleBadRequest(Exception ex)
    {
        // HTTP 400
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
