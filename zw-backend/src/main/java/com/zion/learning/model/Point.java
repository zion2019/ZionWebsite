package com.zion.learning.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zion.common.basic.BaseEntity;
import com.zion.learning.common.DegreeOfMastery;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "points")
public class Point extends BaseEntity {

    private String title;

    private DegreeOfMastery degreeOfMastery;

    private Integer subPointCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long topicId;

}
