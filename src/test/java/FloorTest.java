import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.Arrays;
import java.util.List;

/**
 * Этот класс содержит тесты для класса Floor.
 */
public class FloorTest {
    // Экземпляр Floor для тестирования
    private Floor floor;
    // Список экземпляров Elevator для использования в тестах
    private List<Elevator> elevators;

    /**
     * Этот метод настраивает тестовую среду перед каждым тестом.
     * Он инициализирует экземпляр Floor и список экземпляров Elevator.
     */
    @BeforeEach
    public void setup() {
        floor = new Floor(5);
        elevators = Arrays.asList(new Elevator(1, 1), new Elevator(2, 10), new Elevator(3, 3));
    }

    /**
     * Этот тест проверяет метод pressCallButton класса Floor.
     * Он проверяет, что метод правильно определяет ближайший доступный лифт,
     * блокирует его и устанавливает его текущий этаж на этаж, с которого был сделан вызов.
     */
    @Test
    public void testPressCallButton() {
        Elevator closestElevator = floor.pressCallButton(elevators);
        assertNotNull(closestElevator);
        assertEquals(3, closestElevator.getCabinNumber());
        assertTrue(closestElevator.isLocked());
        assertEquals(5, closestElevator.getCurrentFloor());
    }

    /**
     * Этот тест проверяет метод pressCallButton класса Floor, когда нет доступных лифтов.
     * Он проверяет, что метод правильно возвращает null, когда все лифты заблокированы.
     */
    @Test
    public void testPressCallButton_NoAvailableElevators() {
        elevators.forEach(Elevator::lock);  // Заблокировать все лифты
        Elevator closestElevator = floor.pressCallButton(elevators);
        assertNull(closestElevator);
    }
}
