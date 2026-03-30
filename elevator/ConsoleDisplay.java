public class ConsoleDisplay implements ElevatorObserver {

    @Override
    public void onStateChange(Elevator elevator) {
        System.out.println("  [Display] " + elevator);
    }
}
