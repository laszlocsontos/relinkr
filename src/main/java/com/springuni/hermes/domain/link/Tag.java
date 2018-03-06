package com.springuni.hermes.domain.link;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@EqualsAndHashCode(of = "tagName")
@Getter
@ToString(of = "tagName")
public class Tag implements Serializable {

    private String tagName;

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    Tag() {
    }

}
