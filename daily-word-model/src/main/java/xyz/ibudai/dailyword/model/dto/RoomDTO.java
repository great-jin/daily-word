package xyz.ibudai.dailyword.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import xyz.ibudai.dailyword.model.enums.Catalogue;
import xyz.ibudai.dailyword.model.enums.RankMode;

/**
 * The type Room.
 */
@Data
public class RoomDTO {

    /**
     * 匹配模式
     */
    private RankMode mode;

    /**
     * 房间人数
     */
    private Integer roomSize;

    /**
     * 房间号
     */
    private Integer roomNumber;

    /**
     * 词汇
     */
    private Catalogue catalogue;

    /**
     * 词汇组数
     */
    private Integer size;

    @JsonIgnore
    private String wordIndies;
}
