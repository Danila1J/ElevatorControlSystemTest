import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    // Константы для удобства чтения и поддержки кода
    private static final int NUM_FLOORS = 20;
    private static final int FIRST_ELEVATOR_CALL_FLOOR = 0;
    private static final int FIRST_ELEVATOR_DESTINATION_FLOOR = 14;
    private static final int SECOND_ELEVATOR_CALL_FLOOR = 14;
    private static final int SECOND_ELEVATOR_DESTINATION_FLOOR = 1;

    public static void main(String[] args) {
        // Создание списка лифтов
        List<Elevator> elevators = Arrays.asList(new Elevator(1,1), new Elevator(2,1));

        // Создание списка этажей с использованием потоков
        List<Floor> floors = IntStream.range(0, NUM_FLOORS)
                .mapToObj(i -> new Floor(i + 1))
                .collect(Collectors.toList());

        // Запуск задач для двух лифтов
        startElevatorTask(floors, FIRST_ELEVATOR_CALL_FLOOR, FIRST_ELEVATOR_DESTINATION_FLOOR, elevators);
        startElevatorTask(floors, SECOND_ELEVATOR_CALL_FLOOR, SECOND_ELEVATOR_DESTINATION_FLOOR, elevators);
    }

    /**
     * Запускает задачу для лифта в новом потоке. Пассажир вызывает лифт на этаже callFloor,
     * затем выбирает этаж назначения destinationFloor.
     *
     * @param floors Список всех этажей в здании.
     * @param callFloor Этаж, на котором пассажир вызывает лифт.
     * @param destinationFloor Этаж, на который пассажир хочет поехать.
     * @param elevators Список всех лифтов в здании.
     */
    private static void startElevatorTask(List<Floor> floors, int callFloor, int destinationFloor, List<Elevator> elevators) {
        new Thread(() -> {
            // Пассажир вызывает лифт на определенном этаже
            Elevator elevator = floors.get(callFloor).pressCallButton(elevators);
            // Пассажир выбирает этаж назначения
            elevator.pressFloorButton(destinationFloor);
            elevator.unlock();
        }).start();
    }
}