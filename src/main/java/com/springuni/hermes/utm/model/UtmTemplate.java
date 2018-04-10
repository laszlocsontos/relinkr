package com.springuni.hermes.utm.model;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

import com.springuni.hermes.core.orm.AbstractEntity;
import com.springuni.hermes.user.model.Ownable;
import com.springuni.hermes.user.model.UserId;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

@Entity
public class UtmTemplate extends AbstractEntity<UtmTemplateId> implements Ownable {

    private String name;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "user_id"))
    private UserId userId;

    @ElementCollection
    @CollectionTable(name = "utm_template_parameters", joinColumns = @JoinColumn(name = "utm_template_id"))
    private Set<UtmParameters> utmParametersSet = new LinkedHashSet<>();

    public UtmTemplate(@NotNull String name, @NotNull UserId userId) {
        this(name, userId, emptySet());
    }

    public UtmTemplate(@NotNull String name, @NotNull UserId userId,
            @NotNull Set<UtmParameters> utmParametersSet) {
        Assert.hasText(name, "name cannot be null");
        Assert.notNull(userId, "userId cannot be null");
        Assert.notNull(utmParametersSet, "userId cannot be null");

        this.name = name;
        this.userId = userId;

        setUtmParametersSet(utmParametersSet);
    }

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    UtmTemplate() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
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
