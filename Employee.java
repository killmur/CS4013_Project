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
     * @param position thier position
     * @param point their point on their respective payscale
     * @param payClaim if they've submitted a pay-claim
     * @param hireDate the date they were hired
     */
    public Employee(String name,String position,int point,boolean payClaim,LocalDate hireDate) {
        this.name = name;
        this.position = position;
        this.point = point;
        this.payClaim = payClaim;
        this.hireDate = hireDate;

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

    public void addPoint() {

    }

}
