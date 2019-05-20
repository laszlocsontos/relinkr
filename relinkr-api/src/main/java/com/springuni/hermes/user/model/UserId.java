package com.springuni.hermes.user.model;

import static lombok.AccessLevel.PROTECTED;

import com.springuni.hermes.core.orm.AbstractId;
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
