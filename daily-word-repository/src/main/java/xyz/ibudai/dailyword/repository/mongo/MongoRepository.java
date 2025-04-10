package xyz.ibudai.dailyword.repository.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MongoRepository {

    private final MongoTemplate mongoTemplate;


    /**
     * Insert data.
     *
     * @param <T> the type parameter
     * @param t   the t
     */
    public <T> void insert(T t) {
        mongoTemplate.insert(t);
    }

    /**
     * Find data.
     *
     * @param <T>        the type parameter
     * @param tClass     the t class
     * @param conditions the conditions
     * @return the t
     */
    public <T> T find(Class<T> tClass, Map<String, Object> conditions) {
        Criteria criteria = new Criteria();
        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            criteria.and(entry.getKey()).is(entry.getValue());
        }
        return mongoTemplate.findOne(new Query(criteria), tClass);
    }
}
