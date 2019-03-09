package com.springuni.hermes.link.service;

import com.springuni.hermes.core.orm.OwnableRepository;
import com.springuni.hermes.link.model.Link;
import com.springuni.hermes.link.model.LinkId;
import java.util.Optional;

interface LinkRepository extends OwnableRepository<Link, LinkId> {

    Optional<Link> findByPath(String path);

}
