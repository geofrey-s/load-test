package com.mparticle.test;

import com.mparticle.test.CliArgumentsParser.CliOption;
import org.junit.Test;

import static org.junit.Assert.*;

public class CliArgumentsParserTest {
    @Test
    public void should_parse_cli_option() {
        CliOption option = CliOption.from("--host");

        assertEquals(option, CliOption.HOST);
    }

    @Test(expected = Errors.InvalidCliOptionsException.class)
    public void shoudl_throw_an_error_when_invalid_option_used() {
        CliOption.from("invalid-options");
    }

    @Test
    public void should_set_default_option_values() {
        CliOption option = CliOption.from("--http-method");

        assertEquals(option.defaultValue, "get");
    }

    @Test
    public void should_parse_cli_arguments_input() {
        CliArgumentsParser cliOptions = CliArgumentsParser.from(new String[]{
                "--host", "http://www.mparticle.com",
                "--path", "/user/1234",
                "--target-rps", "200",
        });

        assertEquals(cliOptions.getString(CliOption.HOST), "http://www.mparticle.com");
        assertEquals(cliOptions.getString(CliOption.PATH), "/user/1234");
        assertEquals(cliOptions.getString(CliOption.TARGET_RPS), "200");
    }
}