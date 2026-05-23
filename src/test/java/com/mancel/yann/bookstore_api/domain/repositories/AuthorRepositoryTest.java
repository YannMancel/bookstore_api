package com.mancel.yann.bookstore_api.domain.repositories;

import com.mancel.yann.bookstore_api.Fixtures;
import com.mancel.yann.bookstore_api.TestContainerInjector;
import com.mancel.yann.bookstore_api.configuration.DataConfiguration;
import com.mancel.yann.bookstore_api.domain.models.AuthorModel;
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
            Given the table is populated by 1 author
            When the findAll method is called
            Then a list is returned with this author
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void findAllPersistedAuthorModels() {
        var persistedAuthorModels = authorRepository.findAll();

        then(persistedAuthorModels)
                .isNotNull()
                .hasSize(1)
                .element(0)
                    .extracting(AuthorModel::id)
                        .isEqualTo(Fixtures.Author.UUID);
    }

    @DisplayName("""
            Given the table is populated by one author
            When the findById method is called with the author's id
            Then an optional is returned with this author
            """)
    @Test
    @Sql({"/scripts/insert_one_author.sql"})
    void findPersistedAuthorModelByItsId() {
        var persistedAuthorModelOptional = authorRepository.findById(Fixtures.Author.UUID);

        then(persistedAuthorModelOptional)
                .isNotNull()
                .isNotEmpty()
                .get()
                .extracting(AuthorModel::id)
                    .isEqualTo(Fixtures.Author.UUID);
    }

    @DisplayName("""
            Given there is a valid transient author
            When the save method is called
            Then the persistence is success
            And the persisted author is returned
            """)
    @Test
    void persistTransientAuthorModel() {
        var authorCreationRequest = Fixtures.Author.getValidCreationRequest();
        var transientAuthorModel = Fixtures.Author.CONTROLLER_MAPPER.toTransientModel(authorCreationRequest);
        givenCode(transientAuthorModel::validOrThrow).doesNotThrowAnyException();

        var persistedAuthorModel = authorRepository.save(transientAuthorModel);

        then(persistedAuthorModel)
                .extracting(AuthorModel::id)
                    .isNotNull();
    }
}
