import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Этот класс содержит тесты для класса Elevator.
 */
public class ElevatorTest {
    // Экземпляр Elevator для тестирования
    private Elevator elevator;

    /**
     * Этот метод настраивает тестовую среду перед каждым тестом.
     * Он инициализирует экземпляр Elevator.
     */
    @BeforeEach
    public void setup() {
        elevator = new Elevator(1, 1);
    }

    /**
     * Этот тест проверяет метод pressFloorButton класса Elevator.
     * Он проверяет, что метод не вызывает исключение при нажатии кнопки этажа и корректно устанавливает текущий этаж.
     */
    @Test
    public void testPressingFloorButton() {
        assertDoesNotThrow(()-> elevator.pressFloorButton(5));
        assertEquals(5, elevator.getCurrentFloor());
    }

    /**
     * Этот тест проверяет метод pressFloorButton класса Elevator при нажатии недопустимой кнопки этажа.
     * Он проверяет, что метод вызывает исключение IllegalArgumentException при нажатии кнопки этажа 0 или 21.
     */
    @Test
    public void testPressingInvalidFloorButton() {
        assertThrows(IllegalArgumentException.class, () -> elevator.pressFloorButton(0));
        assertThrows(IllegalArgumentException.class, () -> elevator.pressFloorButton(21));
    }

    /**
     * Этот тест проверяет метод pressDoorOpenButton класса Elevator.
     * Он проверяет, что метод корректно изменяет состояние лифта на STANDING_DOORS_OPEN.
     */
    @Test
    public void testPressingDoorOpenButton() {
        assertEquals(Elevator.State.STANDING_DOORS_CLOSED, elevator.getCurrentElevatorState());
        elevator.pressDoorOpenButton();
        assertEquals(Elevator.State.STANDING_DOORS_OPEN, elevator.getCurrentElevatorState());
    }

    /**
     * Этот тест проверяет метод pressDoorCloseButton класса Elevator.
     * Он проверяет, что метод корректно изменяет состояние лифта на STANDING_DOORS_CLOSED.
     */
    @Test
    public void testPressingDoorCloseButton() {
        elevator.pressDoorOpenButton();
        assertEquals(Elevator.State.STANDING_DOORS_OPEN, elevator.getCurrentElevatorState());
        elevator.pressDoorCloseButton();
        assertEquals(Elevator.State.STANDING_DOORS_CLOSED, elevator.getCurrentElevatorState());
    }

    /**
     * Этот тест проверяет метод pressCallDispatcherButton класса Elevator.
     * Он проверяет, что метод корректно выводит сообщение "Вызов диспетчера...".
     */
    @Test
    public void testPressCallDispatcherButton()  {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        elevator.pressCallDispatcherButton();
        assertEquals("Вызов диспетчера...\n", outContent.toString());
        System.setOut(System.out); // Сброс System.out после этого теста
    }

    /**
     * Этот тест проверяет метод arrivalOnFloor класса Elevator.
     * Он проверяет, что метод корректно устанавливает текущий этаж и состояние лифта.
     */
    @Test
    public void testArrivalOnFloor() {
        elevator.arrivalOnFloor(5);
        assertEquals(5, elevator.getCurrentFloor());
        assertEquals(Elevator.State.STANDING_DOORS_CLOSED, elevator.getCurrentElevatorState());
    }

    /**
     * Этот тест проверяет метод getCabinNumber класса Elevator.
     * Он проверяет, что метод корректно возвращает номер кабины.
     */
    @Test
    public void testGetCabinNumber() {
        assertEquals(1, elevator.getCabinNumber());
    }

    /**
     * Этот тест проверяет метод getCurrentFloor класса Elevator.
     * Он проверяет, что метод корректно возвращает текущий этаж.
     */
    @Test
    public void testGetCurrentFloor() {
        assertEquals(1, elevator.getCurrentFloor());
    }

    /**
     * Этот тест проверяет метод getCurrentElevatorState класса Elevator.
     * Он проверяет, что метод корректно возвращает текущее состояние лифта.
     */
    @Test
    public void testGetCurrentElevatorState() {
        assertEquals(Elevator.State.STANDING_DOORS_CLOSED, elevator.getCurrentElevatorState());
    }

    /**
     * Этот тест проверяет метод lock класса Elevator.
     * Он проверяет, что метод корректно блокирует лифт.
     */
    @Test
    public void testLock() {
        elevator.lock();
        assertTrue(elevator.isLocked());
    }

    /**
     * Этот тест проверяет метод unlock класса Elevator.
     * Он проверяет, что метод корректно разблокирует лифт.
     */
    @Test
    public void testUnlock() {
        elevator.lock();
        elevator.unlock();
        assertFalse(elevator.isLocked());
    }

    /**
     * Этот тест проверяет метод isLocked класса Elevator.
     * Он проверяет, что метод корректно возвращает состояние блокировки лифта.
     */
    @Test
    public void testIsLocked() {
        assertFalse(elevator.isLocked());  // В начале лифт должен быть разблокирован.
        elevator.lock();
        assertTrue(elevator.isLocked());
    }

    /**
     * Этот тест проверяет срабатывание датчика sensorNoMovement.
     * Он проверяет, что метод возвращает false, если время выполнения больше 1,1 секунды, что указывает на срабатывание датчика движения.
     * <br><br>
     * Тест не совсем корректный из-за случайных ответов сенсора, а также тестирование времени выполнения может быть ненадежным
     * в некоторых случаях из-за различных факторов, которые могут влиять на время выполнения, таких как например загрузка системы.
     * Юнит-тесты обычно фокусируются на функциональности и корректности кода, а не на производительности.
     * Тесты на время выполнения могут быть непредсказуемыми и приводить к ложным срабатываниям.
     */
    @Test
    public void testCloseDoorSensorMovementExecTime() {
        boolean hasReturnedFalse = false;
        for (int i = 0; i < 10; i++) { // Предположим что датчик сработает хотя бы 1 раз из 10
            elevator.pressDoorOpenButton();
            long startTime = System.currentTimeMillis();
            elevator.pressDoorCloseButton();
            long endTime = System.currentTimeMillis();
            System.out.println(endTime-startTime);
            // Если время выполнения больше 1,1 секунды, метод вернул false
            // И, следовательно, сработал датчик движения
            if (endTime - startTime > 1100) {
                hasReturnedFalse = true;
                break;
            }
        }
        // Проверить, что метод хотя бы раз вернул false
        assertTrue(hasReturnedFalse);
    }

}
