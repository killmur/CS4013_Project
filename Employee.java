import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;

/**
 * Class for creating and saving employee details
 */
public class Employee {
    private String name;
    private String position;
    private int point;
    private boolean payClaim;
    private LocalDate hireDate;

    /**
     * Creates an Employee
     * @param name their name
     */
    public Employee(String name) {
        this.name = name;
    }

    /**
     * Saves an employee to the CSV for employees.
     * @throws FileNotFoundException where the CSV is not found.
     */
    public void save() throws FileNotFoundException {
        File output = new File("employees.csv");
        if (output.exists()){
            PrintWriter writer = new PrintWriter(new FileOutputStream("employees.csv", true));
            writer.append(name).append(",");
            writer.append(position).append(",");
            writer.append(String.valueOf(point)).append(",");
            writer.append(String.valueOf(payClaim)).append(",");
            writer.append(hireDate.toString()).append("\n");
            writer.close();
        }
    }

}
