package io.relinkr.visitor.model;

import io.relinkr.core.orm.AbstractEntity;
import io.relinkr.user.model.Ownable;
import io.relinkr.user.model.UserId;
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
