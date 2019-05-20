@TypeDefs({@TypeDef(name = "URI", typeClass = UriUserType.class, defaultForType = URI.class)})
package io.relinkr;

import io.relinkr.core.orm.UriUserType;
import java.net.URI;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
