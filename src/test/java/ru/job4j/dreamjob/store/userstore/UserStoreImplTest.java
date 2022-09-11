package ru.job4j.dreamjob.store.userstore;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.dreamjob.model.User;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class UserStoreImplTest {

    private UserStore store;

    @Before
    public void whenSetUp() {
        store = new UserStoreImpl();
        User first = new User("firstName", "test1@test.ru", "p1");
        store.add(first);
    }

    @Test
    public void whenFindUserByEmailAndPwdThanSuccess() {
        Optional<User> rsl = store.findUserByEmailAndPwd(
                "test1@test.ru", "p1"
        );
        assertThat(rsl.get().getName(), is("firstName"));
    }

    @Test
    public void whenFindUserByEmailAndPwdWithAnInvalidEmailThanEmpty() {
        Optional<User> expected = Optional.empty();
        Optional<User> rsl = store.findUserByEmailAndPwd(
                "test1000@test.ru", "p1"
        );
        assertEquals(expected, rsl);
    }

    @Test
    public void whenFindUserByEmailAndPwdWithAnInvalidPasswordThanEmpty() {
        Optional<User> expected = Optional.empty();
        Optional<User> rsl = store.findUserByEmailAndPwd(
                "test1@test.ru", "p1000"
        );
        assertEquals(expected, rsl);
    }
}