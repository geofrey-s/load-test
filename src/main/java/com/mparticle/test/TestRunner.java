package com.mparticle.test;

public interface TestRunner {
    default public void running() {
        try {
            MetricsCollector.collect();
            Runtime.getRuntime().addShutdownHook(new Thread(MetricsCollector::summarize));

            while (true) Thread.sleep(10);
        } catch (InterruptedException e) {
            this.stop();
        }
    }

    public TestRunner setup(CliArgumentsParser cliOptions);

    public void execute();

    public void stop();
}
