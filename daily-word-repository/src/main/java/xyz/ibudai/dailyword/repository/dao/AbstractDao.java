package xyz.ibudai.dailyword.repository.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.SneakyThrows;
import xyz.ibudai.dailyword.basic.tool.CollTool;
import xyz.ibudai.dailyword.repository.entity.Pagination;
import xyz.ibudai.dailyword.repository.tool.DaoTool;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.*;

/**
 * The type Abstract dao.
 *
 * @param <T>  the type parameter
 * @param <ID> the type parameter
 */
public abstract class AbstractDao<T, ID> implements BaseDao<T, ID> {

    protected Class<T> tClass;

    protected ConnectionSource conn;

    protected Dao<T, ID> baseDao;


    /**
     * Instantiates a new Abstract dao.
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public AbstractDao() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.tClass = (Class<T>) type.getActualTypeArguments()[0];
        this.conn = new JdbcConnectionSource("jdbc:sqlite:db/daily_word.db");
        this.baseDao = DaoManager.createDao(conn, tClass);
    }

    @Override
    public T findById(ID id) throws SQLException {
        return baseDao.queryForId(id);
    }

    @Override
    public T findFirst(T t) throws SQLException {
        Where<T, ID> where = baseDao.queryBuilder().where();
        PreparedQuery<T> condition = this.buildCondition(t, where);
        if (Objects.isNull(condition)) {
            return null;
        }

        return baseDao.queryForFirst(condition);
    }

    @Override
    public long count(T t) throws SQLException {
        if (Objects.isNull(t)) {
            return 0L;
        }

        Where<T, ID> where = baseDao.queryBuilder()
                .setCountOf(true)
                .where();
        PreparedQuery<T> condition = this.buildCondition(t, where);
        if (Objects.isNull(condition)) {
            return 0L;
        }
        return baseDao.countOf(condition);
    }

    @Override
    public List<T> list(T t) throws SQLException {
        if (Objects.isNull(t)) {
            return Collections.emptyList();
        }

        Map<String, Object> condition = DaoTool.objToMap(t);
        return baseDao.queryForFieldValues(condition);
    }

    @Override
    public List<T> listAll() throws SQLException {
        return baseDao.queryForAll();
    }

    @Override
    public Pagination<T> paging(T t, long pageNum, long pageSize) throws SQLException {
        Pagination<T> pageData = new Pagination<>(pageNum, pageSize);
        long total = this.count(t);
        if (total == 0) {
            pageData.setTotalSize(total);
            pageData.setData(Collections.emptyList());
            return pageData;
        }

        if (pageNum < 0 || pageSize < 0) {
            throw new IllegalArgumentException("page size must be greater than zero");
        }
        Where<T, ID> where = baseDao.queryBuilder()
                .offset(pageNum * pageSize)
                .limit(pageSize)
                .where();
        PreparedQuery<T> condition = this.buildCondition(t, where);
        pageData.setTotalSize(total);
        pageData.setData(baseDao.query(condition));
        return pageData;
    }

    @Override
    public int save(T t) throws SQLException {
        return this.batchSave(Collections.singletonList(t));
    }

    @Override
    public int batchSave(List<T> list) throws SQLException {
        return this.batchSave(list, 100);
    }

    @Override
    public int batchSave(List<T> list, int batchSize) throws SQLException {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }

        int count = 0;
        List<List<T>> partition = CollTool.partition(list, batchSize);
        for (List<T> batch : partition) {
            count += baseDao.create(batch);
        }
        return count;
    }

    @Override
    public int update(ID id, T t) throws SQLException {
        return baseDao.updateId(t, id);
    }

    @Override
    public int delete(ID id) throws SQLException {
        return baseDao.deleteById(id);
    }

    @Override
    public int batchDelete(Collection<ID> ids) throws SQLException {
        return baseDao.deleteIds(ids);
    }

    @Override
    public int truncate() throws SQLException {
        return TableUtils.clearTable(conn, tClass);
    }

    /**
     * 构建查询条件
     *
     * @param obj   条件
     * @param where the where
     * @return the prepared query
     * @throws SQLException the sql exception
     */
    protected PreparedQuery<T> buildCondition(Object obj, Where<T, ID> where) throws SQLException {
        Map<String, Object> map = DaoTool.objToMap(obj);
        if (map.isEmpty()) {
            return null;
        }

        boolean needAnd = false;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (needAnd) {
                where.and();
            }
            where.eq(key, value);
            needAnd = true;
        }
        return where.prepare();
    }

    @Override
    public void close() throws Exception {
        try {
            baseDao.clearObjectCache();
        } catch (Exception ignored) {
        }

        try {
            baseDao.closeLastIterator();
        } catch (Exception ignored) {
        }

        try {
            conn.close();
        } catch (Exception ignored) {
        }
    }
}
