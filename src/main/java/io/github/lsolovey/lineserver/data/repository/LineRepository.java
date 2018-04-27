package io.github.lsolovey.lineserver.data.repository;

import io.github.lsolovey.lineserver.data.entity.Line;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface for generic CRUD operations on {@link Line} entity.
 * <p>
 * Extends standard spring-data {@link CrudRepository}.
 *
 * @see Line
 */
@Repository
public interface LineRepository extends CrudRepository<Line, Integer>
{
    // no additional methods required so far
}
