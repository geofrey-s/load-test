package com.mparticle.test;

import com.mparticle.test.Errors.MissingCliOptionsException;

import java.util.*;

public class CliArgumentsParser {
    public enum CliOption {
        HOST("--host", null),
        PATH("--path", ""),
        TARGET_RPS("--target-rps", Constants.DEFAULT_TARGET_RPS),
        AUTH_KEY("--auth-key", Constants.DEFAULT_AUTH_KEY),
        METHOD("--http-method", Constants.DEFAULT_HTTP_METHOD),
        PAYLOAD("--http-data", Constants.DEFAULT_POST_PAYLOAD),
        HELP("--help", "");

        String optionName;
        String defaultValue;

        CliOption(String optionName, String defaultValue) {
            this.optionName = optionName;
            this.defaultValue = defaultValue;
        }

        public static CliOption from(String option) {
            return Arrays.stream(CliOption.values())
                    .filter(_option -> _option.optionName.equals(option.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> Errors.InvalidCliOptionsException.report(option));
        }
    }

    private Map<CliOption, String> options = new HashMap<>();

    CliArgumentsParser(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            CliOption option = CliOption.from(args[i]);

            if (option.equals(CliOption.HELP)) this.help();
            else options.put(option, args[i + 1]);
        }
    }

    static CliArgumentsParser from(String[] args) {
        return new CliArgumentsParser(args);
    }

    public String getString(CliOption option) {
        String value = options.getOrDefault(option, option.defaultValue);
        MissingCliOptionsException.report(option, value);
        return value;
    }

    public Integer getInteger(CliOption option) {
        String value = this.getString(option);
        return Integer.valueOf(value);
    }

    public void help() {
        System.out.println("======================================");
        StringBuilder output = new StringBuilder("load-test help\n");
        output.append("Usage: load-test ");
        Arrays.stream(CliOption.values()).forEach(option -> output.append(option.optionName).append(" <value> "));
        System.out.println(output.toString());
        System.out.println("======================================");

        System.exit(0);
    }
}
