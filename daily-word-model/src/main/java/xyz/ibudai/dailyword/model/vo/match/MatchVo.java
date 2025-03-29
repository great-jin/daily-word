package xyz.ibudai.dailyword.model.vo.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.ibudai.dailyword.model.dto.TaskWordDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchVo {

    private String matchId;

    private List<TaskWordDTO> taskWords;

}
