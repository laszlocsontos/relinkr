package com.springuni.hermes.user.service;

import static com.springuni.hermes.Mocks.EMAIL_ADDRESS;
import static com.springuni.hermes.Mocks.USER_ID;
import static com.springuni.hermes.Mocks.USER_ID_ZERO;
import static org.junit.Assert.assertEquals;

import com.springuni.hermes.core.BaseRepositoryTest;
import com.springuni.hermes.user.model.User;
import com.springuni.hermes.user.model.UserId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest extends BaseRepositoryTest<User, UserId, UserRepository> {

    @Test
    public void findByEmailAddress() {
        repository.save(entity);
        assertEquals(entity.getId(), repository.findByEmailAddress(EMAIL_ADDRESS).get().getId());
    }

    @Test
    public void findByTwitterHandle() {
        repository.save(entity);
        assertEquals(entity.getId(), repository.findByTwitterHandle("test").get().getId());
    }

    @Override
    protected User createEntity() {
        return new User(EMAIL_ADDRESS, "Secret", "Test", "test");
    }

    @Override
    protected UserId getId() {
        return USER_ID;
    }

    @Override
    protected UserId getNonExistentId() {
        return USER_ID_ZERO;
    }

}
