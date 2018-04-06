package com.springuni.hermes.user.model;

import com.springuni.hermes.core.orm.AbstractId;

public class UserId extends AbstractId<User> {

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    UserId() {
    }

    private UserId(long id) {
        super(id);
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    public static UserId of(long id) {
        return new UserId(id);
    }

}
