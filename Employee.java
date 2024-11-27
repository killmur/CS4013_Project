import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

/**
 * Class for creating and saving employee details
 */
public class Employee {
    private String name;
    private String position;
    private int point;
    private boolean payClaim;
    private LocalDate hireDate;
    static File output = new File("data/employees.csv");


    /**
     * Creates an Employee
     *
     * @param name     their name
     * @param position thier position
     * @param point    their point on their respective payscale
     * @param payClaim if they've submitted a pay-claim
     * @param hireDate the date they were hired
     */
    public Employee(String name, String position, int point, boolean payClaim, LocalDate hireDate) {
        this.name = name;
        this.position = position;
        this.point = point;
        this.payClaim = payClaim;
        this.hireDate = hireDate;

    }

    /**
     * Saves an employee to the CSV for employees.
     *
     * @throws FileNotFoundException where the CSV is not found.
     */
    public void save() throws FileNotFoundException {
        if (output.exists()) {
            PrintWriter writer = new PrintWriter(new FileOutputStream("data/employees.csv", true));
            writer.append(name).append(",");
            writer.append(position).append(",");
            writer.append(String.valueOf(point)).append(",");
            writer.append(String.valueOf(payClaim)).append(",");
            writer.append(hireDate.toString()).append("\n");
            writer.close();
        }
    }

    /**
     * Increments all full time employee salary scales by 1, unless they are at the top of their scale
     *
     * @throws FileNotFoundException
     */
    public static void addPoint() throws FileNotFoundException {
        if (output.exists()) {
            String[] employees;
            ArrayList<String> s = new ArrayList<>();
            Scanner sc = new Scanner(output);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                employees = line.split(",");
                for (int i = 0; i < employees.length; i++) {
                    s.add(employees[i]);
                }
            }
            PrintWriter writer = new PrintWriter(new FileOutputStream("data/employees.csv"));
            for (int i = 2; i < s.size(); i++) {
                if (Integer.parseInt(s.get(i)) == Payroll.getMaxPoint(s.get(i - 1))) { //Checks if employee is on last point of payscale.
                    i += 4;
                } else {
                    s.set(i, String.valueOf(Integer.parseInt(s.get(i)) + 1)); //Increments value by 1

                    i += 4;
                }
            }
            for (int i = 0; i < s.size(); i++) {
                if (i == 4) {
                    writer.append(s.get(i)).append("\n");
                } else {
                    writer.append(s.get(i)).append(",");
                }
                writer.close();


            }

        }
    }

    /**
     * Promotes an employee to a new position and salary point.
     * @param name The employee to be promoted
     * @param newPosition The new position of the employee
     * @param newPoint The new salary point of the employee
     * @return A string of the outcome of the code
     * @throws FileNotFoundException
     */
    public static String promote(String name, String newPosition, int newPoint) throws FileNotFoundException {
        if (output.exists()) {
            if (newPoint > Payroll.getMaxPoint(newPosition)){
                return "New point exceeds maximum point for this position";
            }
            String[] employees;
            ArrayList<String> s = new ArrayList<>();
            ArrayList<String> promotion = new ArrayList<>();
            Scanner sc = new Scanner(output);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                employees = line.split(",");
                for (int i = 0; i < employees.length; i++) {
                    s.add(employees[i]);
                }
            }
            int index = s.indexOf(name);
            s.set(index+1, newPosition);
            s.set(index+2, String.valueOf(newPoint));
            PrintWriter writer = new PrintWriter(new FileOutputStream("data/employees.csv"));
            for (int i = 0; i < s.size(); i++) {
                if (i == 4) {
                    writer.append(s.get(i)).append("\n");
                } else {
                    writer.append(s.get(i)).append(",");
                }
            }
            writer.close();
            return "Employee "+name+" has been promoted to "+newPosition+", point"+newPoint;
        } else {
            return "Error, "+output.getName()+" does not exist";
        }

    }
}