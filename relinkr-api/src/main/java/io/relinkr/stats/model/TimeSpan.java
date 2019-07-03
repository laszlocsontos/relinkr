package io.relinkr.stats.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class TimeSpan <K> {

    private final String name;
    private final K startDate;
    private final K endDate;
}
