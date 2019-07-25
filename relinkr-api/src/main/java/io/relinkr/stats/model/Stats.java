package io.relinkr.stats.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class Stats <K> {

    private final StatType type;
    private final Collection<StatEntry<K>> entries;

    private final TimeSpan timeSpan;
    private final Collection<TimeSpan> availableTimeSpans;
}
