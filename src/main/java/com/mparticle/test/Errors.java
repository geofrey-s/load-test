package com.mparticle.test;

import com.mparticle.test.CliArgumentsParser.CliOption;

public class Errors {
    public static class InvalidCliOptionsException extends RuntimeException {
        public InvalidCliOptionsException(String option) {
            super(String.format("Invalid cli option: '%s'", option));
        }

        static InvalidCliOptionsException report(String option) {
            throw new InvalidCliOptionsException(option);
        }
    }

    public static class MissingCliOptionsException extends RuntimeException {
        public MissingCliOptionsException(String message) {
            super(message);
        }

        static void report(CliOption option, Object value) {
            if (value == null)
                throw new MissingCliOptionsException(String.format("Missing cli option: '%s'", option.optionName));
        }
    }

    public static class InvalidHttpMethodException extends RuntimeException {
        public InvalidHttpMethodException(String method) {
            super(String.format("Invalid http method: '%s'", method));
        }

        static InvalidHttpMethodException report(String method) {
            throw new InvalidHttpMethodException(method);
        }
    }

    public static class HttpMethodNotSupportedException extends RuntimeException {
        HttpMethodNotSupportedException(String method) {
            super(String.format("Http method: %s is not supported", method));
        }
    }
}
