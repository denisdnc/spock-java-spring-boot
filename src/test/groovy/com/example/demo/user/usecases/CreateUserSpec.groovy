package com.example.demo.user.usecases

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import com.example.demo.user.domains.SerasaWrapper
import com.example.demo.user.domains.User
import com.example.demo.user.gateways.database.UserGateway
import com.example.demo.user.gateways.httpclient.SerasaGateway
import spock.lang.Specification

class CreateUserSpec extends Specification {

    /** dependencies */
    SerasaGateway serasaGateway = Mock(SerasaGateway)
    UserGateway userGateway = Mock(UserGateway)

    /** use case to test */
    CreateUser createUser = new CreateUser(userGateway, serasaGateway)

    def setupSpec() {
        FixtureFactoryLoader.loadTemplates("com.example")
    }

    def "should validate mandatory parameters"() {
        given: "an invalid user"
        User user = new User()

        when: "create user is called"
        User result = createUser.execute(user)

        then: "should return errors"
        result.errors != null
        result.errors.size() == 2
        result.errors.stream().anyMatch { error -> error.message == "name is mandatory" }
        result.errors.stream().anyMatch { error -> error.message == "document is mandatory" }
    }

    def "should return user data with success"() {
        given: "an valid user"
        User user = Fixture.from(User.class).gimme("valid")

        and: "has an valid status on serasa"
        serasaGateway.find(_) >> Fixture.from(SerasaWrapper.class).gimme("pending")

        when: "create user is called"
        User result = createUser.execute(user)

        then: "should return user without errors"
        result.errors.isEmpty()
        result.name == "Jack"
        result.document == "123456789"
    }

    def "should save user on database"() {
        given: "an valid user"
        User user = Fixture.from(User.class).gimme("valid")

        and: "has an valid status on serasa"
        serasaGateway.find(_) >> Fixture.from(SerasaWrapper.class).gimme("pending")

        when: "create user is called"
        createUser.execute(user)

        then: "should call database with right user attributes"

        1 * userGateway.save(_ as User) >> { User item ->
            assert item.name == "Jack"
            assert item.document == "123456789"
        }
    }

    def "should not save user on database if user is invalid"() {
        given: "an invalid user"
        User user = new User()

        when: "create user is called"
        createUser.execute(user)

        then: "should not save on database"
        0 * userGateway.save(_ as User)
    }

    def "validate user serasa status"() {
        given: "an valid user"
        User user = Fixture.from(User.class).gimme("valid")

        and: "an valid status on serasa"
        serasaGateway.find(_) >> Fixture.from(SerasaWrapper.class).gimme("pending")

        when: "create user is called"
        User item = createUser.execute(user)

        then: "should return serasa status"
        item.status != null
        item.status == "PENDING"
    }

}
