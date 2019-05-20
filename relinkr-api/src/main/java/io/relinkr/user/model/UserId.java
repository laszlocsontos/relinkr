package io.relinkr.user.model;

import static lombok.AccessLevel.PROTECTED;

import io.relinkr.core.orm.AbstractId;
import javax.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class UserId extends AbstractId<User> {

    public UserId(long id) {
        super(id);
    }

    public static UserId of(long id) {
        return new UserId(id);
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

}
