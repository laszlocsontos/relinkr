package com.springuni.hermes.utm.model;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

import com.springuni.hermes.core.orm.AbstractEntity;
import com.springuni.hermes.user.model.Ownable;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

@Entity
public class UtmTemplate extends AbstractEntity<Long, UtmTemplate> implements Ownable {

    private String name;

    private Long userId;

    @ElementCollection
    private Set<UtmParameters> utmParametersSet = new LinkedHashSet<>();

    public UtmTemplate(@NotNull String name, @NotNull Long userId) {
        this(name, userId, emptySet());
    }

    public UtmTemplate(@NotNull String name, @NotNull Long userId,
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
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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
