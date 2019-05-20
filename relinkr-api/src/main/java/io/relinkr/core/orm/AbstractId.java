package io.relinkr.core.orm;

import static lombok.AccessLevel.PROTECTED;

import java.util.Objects;
import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(PROTECTED)
@MappedSuperclass
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractId<E> implements EntityClassAwareId<E> {

    private Long id;

    @Override
    public String toString() {
        return Objects.toString(id);
    }

}
