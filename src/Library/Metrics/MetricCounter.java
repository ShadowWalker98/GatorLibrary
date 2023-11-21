package Library.Metrics;

public class MetricCounter {

    int colorFlipCount;
    public MetricCounter() {
        colorFlipCount = 0;
    }

    public void increment() {
        this.colorFlipCount++;
    }

    public void incrementBy(int amount) {
        this.colorFlipCount += amount;
    }

    public void decrement() {
        this.colorFlipCount--;
    }

    public int getColorFlipCount() {
        return colorFlipCount;
    }
}
