package timer;

public class Timer {
    double startTime;
    double endTime;

    public Timer() {
        this.startTime = 0;
        this.endTime = 0;
    }

    public void start() {
        this.startTime = (double) (System.currentTimeMillis());
    }

    public void stop() {
        this.endTime = (double) (System.currentTimeMillis());
    }

    public void printResult(String subject) {
        final double runtime = (this.endTime - this.startTime) / 1000;
        System.out.println(String.format("Processing time for %s: %f\n", subject, runtime));
    }

    public void printResultMili(String subject) {
        final double runtime = this.endTime - this.startTime;
        System.out.println(String.format("Processing time for %s: %f\n", subject, runtime));
    }

}