package io.relinkr.link.service;

import io.relinkr.core.orm.OwnableRepository;
import io.relinkr.link.model.Link;
import io.relinkr.link.model.LinkId;
import java.util.Optional;

interface LinkRepository extends OwnableRepository<Link, LinkId> {

  Optional<Link> findByPath(String path);

}
