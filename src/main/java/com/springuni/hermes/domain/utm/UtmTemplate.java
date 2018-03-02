package com.springuni.hermes.domain.utm;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

import com.springuni.hermes.domain.user.Ownable;
import com.springuni.hermes.domain.user.UserId;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.util.Assert;

@Entity
public class UtmTemplate extends AbstractPersistable<UtmTemplateId> implements Ownable {

    private String name;

    @Embedded
    private UserId owner;

    @ElementCollection
    private Set<UtmParameters> utmParametersSet = new LinkedHashSet<>();

    public UtmTemplate(@NotNull String name, @NotNull UserId owner) {
        this(name, owner, emptySet());
    }

    public UtmTemplate(@NotNull String name, @NotNull UserId owner,
            @NotNull Set<UtmParameters> utmParametersSet) {
        Assert.hasText(name, "name cannot be null");
        Assert.notNull(owner, "owner cannot be null");
        Assert.notNull(utmParametersSet, "owner cannot be null");

        this.name = name;
        this.owner = owner;

        setUtmParametersSet(utmParametersSet);
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

    public Set<UtmParameters> getUtmParametersSet() {
        return unmodifiableSet(utmParametersSet);
    }

    public void setUtmParametersSet(@NotNull Set<UtmParameters> utmParameterSet) {
        Assert.notNull(utmParameterSet, "utmParameterSet cannot be null");
        Set<UtmParameters> newUtmParameterSet = new LinkedHashSet<>(utmParameterSet.size());
        newUtmParameterSet.addAll(utmParameterSet);
        utmParametersSet = newUtmParameterSet;
    }

    public void addUtmParameters(@NotNull UtmParameters utmParameters) {
        Assert.notNull(utmParameters, "utmParameters cannot be null");
        utmParametersSet.add(utmParameters);
    }

    public void removeUtmParameters(UtmParameters utmParameters) {
        Assert.notNull(utmParameters, "utmParameters cannot be null");
        utmParametersSet.remove(utmParameters);
    }

}
