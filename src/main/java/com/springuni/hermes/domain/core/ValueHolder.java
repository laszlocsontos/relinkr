package com.springuni.hermes.domain.core;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValueHolder<T> implements Serializable {

    private T value;

}
