import java.util.List;

public class Floor {
    private boolean isCallButtonPressed; // Состояние кнопки вызова лифта
    private final int floorNumber; // Номер этажа

    /**
     * Конструктор этажа.
     *
     * @param floorNumber Номер этажа.
     */
    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.isCallButtonPressed = false;
    }

    /**
     * Метод для вызова лифта на этаже. Выбирает ближайший доступный лифт.
     *
     * @param elevators Список всех лифтов в здании.
     * @return Ближайший доступный лифт или null, если все лифты заняты.
     */
    public Elevator pressCallButton(List<Elevator> elevators) {
        System.out.printf("Нажата кнопка вызова лифта на этаже: %d\n\n",floorNumber);
        this.isCallButtonPressed = true;
        Elevator closestElevator = findClosestElevator(elevators);
        if (closestElevator != null) {
            closestElevator.lock();
            closestElevator.arrivalOnFloor(floorNumber);
        }
        return closestElevator;
    }

    /**
     * Метод для поиска ближайшего доступного лифта.
     *
     * @param elevators Список всех лифтов в здании.
     * @return Ближайший доступный лифт или null, если все лифты заняты.
     */
    private Elevator findClosestElevator(List<Elevator> elevators) {
        Elevator closestElevator = null;
        int minDistance = Integer.MAX_VALUE;
        for (Elevator elevator : elevators) {
            if (!elevator.isLocked()) {
                int distance = Math.abs(elevator.getCurrentFloor() - floorNumber);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestElevator = elevator;
                }
            }
        }
        return closestElevator;
    }
}
