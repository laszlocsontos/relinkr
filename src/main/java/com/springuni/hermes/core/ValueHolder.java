package com.springuni.hermes.core;

import java.io.Serializable;
import lombok.Data;

@Data
public class ValueHolder<T> implements Serializable {

    private final T value;

}
