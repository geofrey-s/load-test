package com.mparticle.test;

public class App {

    public static void main(String[] args) {
        CliArgumentsParser parser = CliArgumentsParser.from(args);

        TestRunner runner = new LoadTest().setup(parser);
        runner.execute();
    }
}
