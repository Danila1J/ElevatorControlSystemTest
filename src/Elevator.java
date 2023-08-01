import java.util.Random;

/**
 * Класс Elevator, представляющий функциональность лифта.
 * Содержит набор методов, описывающих поведение лифта, включая переход между этажами,
 * управление дверьми, реагирование на кнопки, вызов диспетчера и т.д.
 */
public class Elevator {
    private static final int TIME_TO_CLOSE_DOOR = 1000; // Время для закрытия дверей в миллисекундах
    private static final int TIME_TO_OPEN_DOOR = 1000; // Время для открытия дверей в миллисекундах
    private static final int TIME_TO_WAIT = 10000; // Время ожидания в миллисекундах
    private static final int TIME_TO_SENSOR_REQUEST = 1000; // Время ожидания между запросами к датчику
    private static final int MIN_FLOOR = 1; // Минимально допустимый этаж
    private static final int MAX_FLOOR = 20; // Максимально допустимый этаж

    // Возможные состояния для лифта
    public enum State {
        MOVING_UP, MOVING_DOWN, DOOR_OPENING, DOOR_CLOSING, STANDING_DOORS_OPEN, STANDING_DOORS_CLOSED
    }

    // Текущий этаж, на котором находится лифт
    private int currentFloor;
    // Номер кабины лифта
    private final int cabinNumber;
    // Переменная, указывающая, заблокирован ли лифт
    private boolean isLocked;
    // Текущее состояние лифта
    private State currentElevatorState;

    /**
     * Конструктор класса Elevator.
     * Инициализирует лифт на указанном этаже и с указанным номером кабины.
     *
     * @param cabinNumber  номер кабины лифта
     * @param currentFloor текущий этаж, на котором расположен лифт
     */
    public Elevator(int cabinNumber, int currentFloor) {
        // проверка валидности начальных параметров
        if (!isValidFloor(currentFloor)) {
            throw new IllegalArgumentException("Неверные параметры инициализации для лифта");
        }
        this.cabinNumber = cabinNumber;
        this.currentFloor = currentFloor;
        currentElevatorState = State.STANDING_DOORS_CLOSED; // Сначала лифт находится на месте с закрытыми дверьми
    }

    /**
     * Метод pressFloorButton обрабатывает нажатие кнопки этажа внутри лифта.
     * Определяет направление движения лифта исходя из текущего и целевого этажа.
     *
     * @param floor этаж, на который пользователь хочет переместиться
     */
    public void pressFloorButton(int floor) {
        // Проверка валидности номера этажа
        if (!isValidFloor(floor)) {
            throw new IllegalArgumentException("Невалидный номер этажа: " + floor);
        }
        // Движение лифта вверх или вниз в зависимости от текущего этажа
        currentElevatorState = (floor > currentFloor) ? State.MOVING_UP : State.MOVING_DOWN;
        String direction = (currentElevatorState == State.MOVING_UP) ? "вверх" : "вниз";
        System.out.printf("Лифт %1$d: Нажата кнопка этажа: %2$d\nДвижется %3$s, на %2$d этаж\n\n", cabinNumber, floor, direction);
        moveElevator(floor);
        openCloseDoor();
    }

    /**
     * Метод pressDoorOpenButton нажимает кнопку открытия дверей лифта.
     * Если лифт находится в подходящем состоянии (не движется и двери не открываются), то двери открываются.
     */
    public void pressDoorOpenButton() {
        switch (currentElevatorState) {
            case MOVING_UP, MOVING_DOWN, DOOR_OPENING, STANDING_DOORS_OPEN -> {
            } // ничего не делать
            default -> openDoor();
        }
    }

    /**
     * Метод pressDoorCloseButton нажимает кнопку закрытия дверей лифта.
     * Если лифт находится в подходящем состоянии (не движется и двери не закрываются), то двери закрываются.
     */
    public void pressDoorCloseButton() {
        switch (currentElevatorState) {
            case MOVING_UP, MOVING_DOWN, DOOR_CLOSING, STANDING_DOORS_CLOSED -> {
            }// ничего не делать
            default -> closeDoor();
        }
    }

    // Внутренняя функциональность для открытия дверей лифта
    private void openDoor() {
        // установка состояния открытия двери
        currentElevatorState = State.DOOR_OPENING;
        System.out.printf("Лифт %d: Дверь открывается\n\n", cabinNumber);
        // Симуляция операции открытия двери
        simulateDoorOperation(TIME_TO_OPEN_DOOR, State.STANDING_DOORS_OPEN);
        System.out.printf("Лифт %d: Дверь открыта\n\n", cabinNumber);
    }

    // Внутренняя функциональность для закрытия дверей лифта
    private void closeDoor() {
        // Ожидаем, пока sensorNoMovement не вернет true
        while (!sensorNoMovement()) {
            try {
                Thread.sleep(TIME_TO_SENSOR_REQUEST); //Проверяем значение датчика каждую секунду
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        // установка состояния закрытия двери
        currentElevatorState = State.DOOR_CLOSING;
        System.out.printf("Лифт %d: Дверь закрывается\n\n", cabinNumber);
        // Симуляция операции закрытия двери
        simulateDoorOperation(TIME_TO_CLOSE_DOOR, State.STANDING_DOORS_CLOSED);
        System.out.printf("Лифт %d: Дверь закрыта\n\n", cabinNumber);
    }

    // Внутренняя функциональность для симуляции времени, необходимого для открытия/закрытия дверей лифта
    private void simulateDoorOperation(long time, State endState) {
        // Фактическое закрытие моделируется за счет сна на время закрытия.
        try {
            Thread.sleep(time);
            currentElevatorState = endState; // После ожидания состояние лифта меняется
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Метод для симуляции ожидания внутри лифта после открытия дверей
    private void waitInsideElevator() {
        try {
            Thread.sleep(TIME_TO_WAIT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод pressCallDispatcherButton вызывает диспетчера.
     * Может использоваться в случаях, когда требуется помощь или есть проблемы с работой лифта.
     */
    public void pressCallDispatcherButton() {
        System.out.println("Вызов диспетчера...");
    }

    // Обнаружение движения и предотвращение закрытия дверей.
    private boolean sensorMovement() {
        boolean movement = new Random().nextBoolean();  // Осуществляется симуляция с помощью генерации случайного значения true или false
        if (movement) {
            System.out.println("Обнаружено движение");
        } else {
            System.out.println("Не обнаружено движение");
        }
        return movement;
    }

    // Обнаружение отсутствия движения и продолжение закрытия дверей.
    private boolean sensorNoMovement() {
        boolean noMovement = new Random().nextBoolean();  // Осуществляется симуляция с помощью генерации случайного значения true или false
        if (noMovement) {
            // Так как выводить необходимо только состояние лифта
            // И действия пользователя
            // Вывод в консоль, закомментирован
            //System.out.println("Не обнаружено движение");
        } else {
            //System.out.println("Обнаружено движение");
        }
        return noMovement;
    }

    /**
     * Метод arrivalOnFloor осуществляет прибытие на вызванный этаж
     */
    public void arrivalOnFloor(int floor) {
        if (currentFloor != floor) {// Открываем, закрываем двери если лифт находится на этом же этаже
            currentElevatorState = (floor > currentFloor) ? State.MOVING_UP : State.MOVING_DOWN;
            String direction = (currentElevatorState == State.MOVING_UP) ? "вверх" : "вниз";
            System.out.printf("Лифт %d: Движется %s, на %d этаж\n\n", cabinNumber, direction, floor);
            moveElevator(floor);
        }
        openCloseDoor();
    }

    // Открытие и закрытие дверей лифта, после прибытия на целевой этаж
    private void openCloseDoor() {
        openDoor();
        waitInsideElevator();
        closeDoor();
    }

    // Метод для симуляции движения лифта между этажами
    private void moveElevator(int floor) {
        int totalFloorsToTravel = Math.abs(floor - currentFloor);
        try {
            for (int i = 0; i < totalFloorsToTravel; i++) {
                Thread.sleep(2000); // Ожидание 2 секунды для симуляции времени, необходимого для перемещения на один этаж
                currentFloor += (floor > currentFloor) ? 1 : -1;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Метод для проверки валидного этажа
    private boolean isValidFloor(int floor) {
        return floor >= MIN_FLOOR && floor <= MAX_FLOOR;
    }

    /**
     * Метод getCabinNumber возвращает номер кабины лифта.
     *
     * @return номер кабины
     */
    public int getCabinNumber() {
        return cabinNumber;
    }

    /**
     * Метод getCurrentFloor возвращает текущий этаж, на котором находится лифт.
     *
     * @return текущий этаж
     */
    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * Метод getCurrentElevatorState возвращает текущее состояние лифта.
     *
     * @return текущее состояние лифта
     */
    public State getCurrentElevatorState() {
        return currentElevatorState;
    }

    /**
     * Метод lock блокирует лифт, для вызовов других пользователей.
     */
    public void lock() {
        isLocked = true;
    }

    /**
     * Метод unlock снимает блокировку с лифта, делая его доступным для использования.
     */
    public void unlock() {
        isLocked = false;
    }

    /**
     * Метод isLocked проверяет, заблокирован ли лифт.
     *
     * @return возвращает true, если лифт заблокирован, и false, если не заблокирован
     */
    public boolean isLocked() {
        return isLocked;
    }
}