package io.relinkr.stats.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.core.Relation;

@Getter
@RequiredArgsConstructor(staticName = "of")
@Relation(collectionRelation = "data")
public class StatEntry<K> {

    private final K key;
    private final int value;

}
