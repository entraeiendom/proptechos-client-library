package io.entraos.idun.commands;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class IdunGetCommandTest {

    @Before
    public void setUp() throws Exception {
    }

    @Ignore
    @Test()
    public void getAsJson() {
        String json = new IdunGetCommand(null, "idunGet").getAsJson();
        assertNotNull(json);
    }
}