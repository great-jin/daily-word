package xyz.ibudai.dailyword.model.vo;

import lombok.Data;
import xyz.ibudai.dailyword.model.dto.TaskWordDTO;
import xyz.ibudai.dailyword.model.mongo.SubjectContent;

import java.util.List;

@Data
public class AnswerVo {

    private List<TaskWordDTO> answers;

    private List<SubjectContent> submits;
}
