package com.web.insurance.modelEntity;

import lombok.*;

/**
 * Created on 2020/4/9
 * Package com.web.insurance.modelEntity
 *
 * @author dsy
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TagTops implements Comparable<TagTops> {

    private Integer tagId;
    private String name;
    private Integer BlogNumber;

    @Override
    public int compareTo(TagTops o) {
        return o.BlogNumber - this.BlogNumber;
    }
}
