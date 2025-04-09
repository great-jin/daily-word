package xyz.ibudai.dailyword.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document("answer_record")
public class AnswerRecord {

    @Field("user_id")
    private Integer userId;

    @Field("match_id")
    private Integer matchId;

    @Field("subject_list")
    private List<SubjectContent> subjectList;

}
