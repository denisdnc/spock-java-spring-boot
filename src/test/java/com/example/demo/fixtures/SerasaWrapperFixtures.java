package com.example.demo.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.example.demo.user.domains.SerasaWrapper;

public class SerasaWrapperFixtures implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(SerasaWrapper.class).addTemplate("pending", new Rule() {{
            add("status", "PENDING");
        }});
    }
}
