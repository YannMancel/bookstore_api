package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.TestContainerInjector;
import com.mancel.yann.bookstore_api.configuration.DataConfiguration;
import com.mancel.yann.bookstore_api.domain.entities.AuthorEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.givenCode;

@DataJpaTest
@ContextConfiguration(classes = DataConfiguration.class)
class AuthorRepositoryTest extends TestContainerInjector {

    @Autowired
    AuthorRepository authorRepository;

    @DisplayName("""
            Given the table is empty
            When the findAll method is called
            Then an empty list is returned
            """)
    @Test
    void test1() {
        var persistedEntities = authorRepository.findAll();

        then(persistedEntities)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName("""
            Given the table is populated by one author
            When the findAll method is called
            Then a list is returned with this author
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void test2() {
        var persistedEntities = authorRepository.findAll();

        then(persistedEntities)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                    .extracting(AuthorEntity::id)
                        .isEqualTo(Fixtures.Author.UUID);
    }

    @DisplayName("""
            Given the table is empty
            When the findById method is called with a random id
            Then an empty optional is returned
            """)
    @Test
    void test3() {
        var persistedEntityOptional = authorRepository.findById(Fixtures.getRandomUUID());

        then(persistedEntityOptional)
                .isNotNull()
                .isEmpty();
    }

    @DisplayName("""
            Given the table is populated by one author
            When the findById method is called with the author's id
            Then an optional is returned with this author
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void test4() {
        var persistedEntityOptional = authorRepository.findById(Fixtures.Author.UUID);

        then(persistedEntityOptional)
                .isNotNull()
                .isNotEmpty()
                .get()
                .extracting(AuthorEntity::id)
                    .isEqualTo(Fixtures.Author.UUID);
    }

    @DisplayName("""
            Given there is a valid transient entity
            When the save method is called
            Then the persistence is success
            And the persisted author is returned
            """)
    @Test
    void test5() {
        var request = Fixtures.Author.getValidCreationRequest();
        var transientEntity = Fixtures.Author.CONTROLLER_MAPPER.toTransientEntity(request);
        givenCode(transientEntity::validOrThrow).doesNotThrowAnyException();

        var persistedEntity = authorRepository.save(transientEntity);

        then(persistedEntity)
                .extracting(AuthorEntity::id)
                    .isNotNull();
    }
}
