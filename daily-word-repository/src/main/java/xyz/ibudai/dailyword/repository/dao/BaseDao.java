package xyz.ibudai.dailyword.repository.dao;

import xyz.ibudai.dailyword.repository.entity.Pagination;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * The interface Base dao.
 *
 * @param <T>  the type parameter
 * @param <ID> the type parameter
 */
public interface BaseDao<T, ID> extends AutoCloseable {

    /**
     * Find by id t.
     *
     * @param id the id
     * @return the t
     * @throws SQLException the sql exception
     */
    T findById(ID id) throws SQLException;

    /**
     * Find first t.
     *
     * @param t the t
     * @return the t
     * @throws SQLException the sql exception
     */
    T findFirst(T t) throws SQLException;

    /**
     * Count long.
     *
     * @param t the t
     * @return the long
     * @throws SQLException the sql exception
     */
    long count(T t) throws SQLException;

    /**
     * List data.
     *
     * @param t the t
     * @return the list
     * @throws SQLException the sql exception
     */
    List<T> list(T t) throws SQLException;

    /**
     * List all list.
     *
     * @return the list
     * @throws SQLException the sql exception
     */
    List<T> listAll() throws SQLException;

    /**
     * Paging base pagination.
     *
     * @param t        the t
     * @param pageNum  the page num
     * @param pageSize the page size
     * @return the base pagination
     * @throws SQLException the sql exception
     */
    Pagination<T> paging(T t, long pageNum, long pageSize) throws SQLException;

    /**
     * Save int.
     *
     * @param t the t
     * @return the int
     * @throws SQLException the sql exception
     */
    int save(T t) throws SQLException;

    /**
     * Batch save int.
     *
     * @param list the list
     * @return the int
     * @throws SQLException the sql exception
     */
    int batchSave(List<T> list) throws SQLException;

    /**
     * Batch save int.
     *
     * @param list      the list
     * @param batchSize the batch size
     * @return the int
     * @throws SQLException the sql exception
     */
    int batchSave(List<T> list, int batchSize) throws SQLException;

    /**
     * Update int.
     *
     * @param id the id
     * @param t  the t
     * @return the int
     * @throws SQLException the sql exception
     */
    int update(ID id, T t) throws SQLException;

    /**
     * Delete int.
     *
     * @param id the id
     * @return the int
     * @throws SQLException the sql exception
     */
    int delete(ID id) throws SQLException;

    /**
     * Batch delete int.
     *
     * @param ids the ids
     * @return the int
     * @throws SQLException the sql exception
     */
    int batchDelete(Collection<ID> ids) throws SQLException;

    /**
     * Truncate int.
     *
     * @return the int
     * @throws SQLException the sql exception
     */
    int truncate() throws SQLException;
}

