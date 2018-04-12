package com.springuni.hermes.core.security.access;

import static org.springframework.util.ReflectionUtils.findMethod;

import java.lang.reflect.Method;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    static final Method GET_ENTITY_WITH_ID_METHOD =
            findMethod(TestController.class, "getEntityWithId", TestId.class);

    static final Method GET_ENTITY_WITHOUT_ID_METHOD =
            findMethod(TestController.class, "getEntityWithoutId");

    @AuthorizeRolesOrOwner
    @GetMapping("/{testId}")
    void getEntityWithId(@PathVariable TestId testId) {

    }

    @AuthorizeRolesOrOwner
    void getEntityWithoutId() {

    }

}
