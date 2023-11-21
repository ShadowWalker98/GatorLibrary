public class MetricCounter {
    // handles the color flip count for a red black tree
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
