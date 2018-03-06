package com.springuni.hermes.domain.user;

import static com.springuni.hermes.domain.Mocks.EMAIL_ADDRESS;
import static org.junit.Assert.assertEquals;

import com.springuni.hermes.domain.core.BaseRepositoryTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest extends BaseRepositoryTest<User, Long, UserRepository> {

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
        return new User(EMAIL_ADDRESS, "Test", "test");
    }

}
