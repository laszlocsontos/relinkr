package com.springuni.hermes.link;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

import com.springuni.hermes.user.Ownable;
import com.springuni.hermes.user.UserId;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.util.Assert;

@Entity
public class UtmTemplate extends AbstractPersistable<UtmTemplateId> implements Ownable {

    private String name;

    @Embedded
    private UserId owner;

    @ElementCollection
    private Set<UtmParameters> utmParameters;

    public UtmTemplate(String name, UserId owner) {
        this(name, owner, emptySet());
    }

    public UtmTemplate(String name, UserId owner, Set<UtmParameters> utmParameters) {
        Assert.hasText(name, "name cannot be null");
        Assert.notNull(owner, "owner cannot be null");
        Assert.notNull(utmParameters, "utmParameters cannot be null");

        this.name = name;
        this.owner = owner;
        this.utmParameters = utmParameters;
    }

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    UtmTemplate() {
    }

    @Override
    @EmbeddedId
    public UtmTemplateId getId() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UserId getOwner() {
        return owner;
    }

    public void setOwner(UserId owner) {
        this.owner = owner;
    }

    public Set<UtmParameters> getUtmParameters() {
        return unmodifiableSet(utmParameters);
    }

    public void setUtmParameters(Set<UtmParameters> utmParameterSet) {
        Set<UtmParameters> newUtmParameterSet = new LinkedHashSet<>(utmParameterSet.size());
        newUtmParameterSet.addAll(newUtmParameterSet);
        this.utmParameters = newUtmParameterSet;
    }

}
