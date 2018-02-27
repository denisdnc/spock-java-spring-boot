package com.example.demo.user.gateways.controllers

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import com.example.demo.config.ApplicationComponentTestConfig
import com.example.demo.user.domains.SerasaWrapper
import com.example.demo.user.domains.User
import com.example.demo.user.gateways.httpclient.SerasaGateway
import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.ScanScopedBeans
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

@SpringBootTest(classes = ApplicationComponentTestConfig.class)
@ScanScopedBeans
class UserControllerSpec extends Specification {

    /** dependencies */
    @Autowired
    private SerasaGateway serasaGateway

    /** controller to test */
    @Autowired
    private UserController controller

    private MockMvc mockMvc
    private ObjectMapper objectMapper = new ObjectMapper()

    def setup() {
        mockMvc = standaloneSetup(controller).build()
        FixtureFactoryLoader.loadTemplates("com.example")
    }

    def "do POST with success"() {
        given: "an valid payload"
        User user = Fixture.from(User.class).gimme("valid");
        String requestBody = objectMapper.writeValueAsString(user)

        when: "the API receives an POST with body"
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andReturn()

        then: "http status should be created"
        result.getResponse().getStatus() == HttpStatus.CREATED.value()

        and: "serasa returns an valid response"
        serasaGateway.find(_ as String) >> SerasaWrapper.builder().status("PENDING").build()

        and: "body should contains"
        User resultBody = objectMapper.readValue(result.getResponse().getContentAsString(), User.class)
        resultBody.name == "Jack"
        resultBody.document == "123456789"
        resultBody.id != null
        resultBody.errors.empty
        resultBody.status == "PENDING"
    }

    @TestConfiguration
    static class Mocks {
        def factory = new DetachedMockFactory()

        @Bean
        SerasaGateway serasaGateway() {
            factory.Mock(SerasaGateway)
        }
    }

}
