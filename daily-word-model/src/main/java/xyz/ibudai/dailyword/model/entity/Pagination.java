package xyz.ibudai.dailyword.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination<T> {

    private Long pageNumber;

    private Long pageSize;

    private Long totalSize;

    private List<T> data;


    public Pagination(Long pageNumber, Long pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}
