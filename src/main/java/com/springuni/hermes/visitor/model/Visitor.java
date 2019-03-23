package com.springuni.hermes.visitor.model;

import com.springuni.hermes.core.orm.AbstractEntity;
import com.springuni.hermes.user.model.Ownable;
import com.springuni.hermes.user.model.UserId;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public class Visitor extends AbstractEntity<VisitorId> implements Ownable {

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "user_id"))
    private UserId userId;

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    Visitor() {
    }

    private Visitor(UserId userId) {
        this.userId = userId;
    }

    public static Visitor of(UserId userId) {
        return new Visitor(userId);
    }

}
