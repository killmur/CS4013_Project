import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 * <h1>Payroll Class</h1>
 * Class for performing payroll actions.
 * @author Group 2
 * @version 1.0
 */
public class Payroll {
    private ArrayList<Payslip> payslips = new ArrayList<Payslip>();
    private static File salaryCsv = new File("data/salaryScales.csv");
    private static File payslipCsv = new File("data/all_payslips.csv");
    private static File employeeCsv = new File("data/employees.csv");
    private static File payClaimsCsv = new File("data/payClaims.csv");


    /**
     * Gets the payslips for a person.
     *
     * @param name Name of the person on the payslip.
     * @param x    Either 'a' for all payslips for the person or 'r' for the most recent payslip.
     * @return The details of the payslip.
     * @throws IOException
     */
    public String getPayslips(String name, char x) throws IOException {
        try {
            String[] payslip;
            ArrayList<String> s = new ArrayList<>();
            Scanner sc = new Scanner(payslipCsv);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.contains(name)) {
                    payslip = line.split(",");
                    if (line.contains(name)) {
                        for (int i = 0; i < payslip.length; i++) {
                            s.add(payslip[i]);
                        }
                    }
                }
            }
            for (int i = 0; i < s.size(); ) {
                payslips.add(new Payslip(s.get(i), Double.parseDouble(s.get(i + 1)), Double.parseDouble(s.get(i + 2)), Double.parseDouble(s.get(i + 3)), Double.parseDouble(s.get(i + 4)), Double.parseDouble(s.get(i + 5)), Double.parseDouble(s.get(i + 6)), Double.parseDouble(s.get(i + 7))));
                i += 8;
            }


        } catch (FileNotFoundException e) {
            System.out.println("File " + payslipCsv.getName() + " not found");
        }
        if (x == 'a') {
            String ret = "Payslips for " + name + "\n";
            for (Payslip p : payslips) {
                ret += p.toString() + "\n";
            }
            return ret;
        } else if (x == 'r') {
            String ret = "Most recent payslip for " + name + "\n";
            int i = payslips.size() - 1;
            ret += payslips.get(i).toString();
            return ret;
        }
        return "No payslips found for " + name + "\n";
    }

    /**
     * Gets the salary of a specific position and pay point
     *
     * @param position
     * @param point
     * @return
     * @throws IOException
     */
    public static double getSalary(String position, int point) throws IOException {
        try {
            String[] salary;
            ArrayList<String> s = new ArrayList<>();
            Scanner sc = new Scanner(salaryCsv);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.contains(position)) {
                    salary = line.split(",");
                    for (int i = 0; i < salary.length; i++) {
                        s.add(salary[i]);
                    }
                }
            }
            sc.close();
            for (int i = 2; i < s.size(); ) {
                if ((Integer.parseInt(s.get(i))) == point) {
                    return Double.parseDouble(s.get(i - 1));
                }
                i += 4;

            }

        } catch (FileNotFoundException e) {
            System.out.println("File " + salaryCsv.getName() + " not found");
        }
        return 0;
    }

    /**
     * Generates a payslip given parameters.
     * @param name The name of the person on the payslip.
     * @param gross Their gross pay for the payslip.
     * @return a Payslip object of the payslip.
     * @throws IOException
     */
    public static Payslip generatePayslip(String name, double gross) throws IOException {
        double year = 0;
        try {
            String[] ytd;
            Scanner sc = new Scanner(payslipCsv);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.contains(name)) {
                    ytd = line.split(",");
                    year += Double.parseDouble(ytd[1]);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + payslipCsv.getName() + " not found");
        }
        boolean union = false;
        try {
            String[] details;
            Scanner sc = new Scanner(employeeCsv);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.contains(name)) {
                    details = line.split(",");
                    union = Boolean.getBoolean(details[5]);
                }

            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + employeeCsv.getName() + " not found");
        }
        Payslip p = new Payslip(name, gross, year, union);
        p.paye();
        p.prsi();
        p.usc();
        p.health();
        if (union) {
            p.union();
        }
        p.save();
        return p;
    }

    /**
     * Adds an employee to the csv.
     * @param name Employee's name
     * @param position Employee's position
     * @param point Employee's salary point
     * @throws FileNotFoundException
     */
    public void addEmployee(String name, String position, int point) throws FileNotFoundException {
        LocalDate hireDate = LocalDate.now();
        Employee emp = new Employee(name, position, point, false, hireDate);
        emp.save();
    }

    /**
     * Submits a pay-claim for a part-time employee
     * @param name Employee's name
     * @return A string giving the outcome of the code
     * @throws IOException
     */
    public String submitPayClaim(String name) throws IOException {
        ArrayList<String> s = new ArrayList<>();
        if (employeeCsv.exists()) {
            String[] employees;
            Scanner sc = new Scanner(employeeCsv);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.contains(name)) {
                    employees = line.split(",");
                    for (int i = 0; i < employees.length; i++) {
                        s.add(employees[i]);
                    }
                }
            }
            sc.close();
            Scanner psc = new Scanner(payClaimsCsv);
            while (psc.hasNextLine()) {
                String line = psc.nextLine();
                if (line.contains(name)) {
                    return "You already submitted a pay claim!";
                }
            }
            boolean partTime = isPartTime(s.get(1), Integer.parseInt(s.get(2)));
            if (partTime) {
                PrintWriter writer = new PrintWriter(new FileOutputStream("data/payClaims.csv", true));
                writer.append(name).append("\n");
                writer.close();
                return "You have submitted a pay claim!";
            }
        }
        return "You are not part time, no pay claim submitted!";
    }

    /**
     * Determines if a position is part-time.
     * @param position The position in question.
     * @param point The salary point of the position in question.
     * @return true where the position is part-time and false where it is not.
     * @throws IOException
     */
    public static boolean isPartTime(String position, int point) throws IOException {
        try {
            String[] salary;
            ArrayList<String> s = new ArrayList<>();
            Scanner sc = new Scanner(salaryCsv);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                salary = line.split(",");
                if (line.contains(position)) {
                    int salPoint = Integer.parseInt(salary[2]);
                    if (salary[0].equals(position) && salPoint == (point)) {
                        for (int i = 0; i < salary.length; i++) {
                            s.add(salary[i]);
                        }
                    }
                }
            }
            sc.close();
            return Boolean.parseBoolean(s.get(3));

        } catch (
                FileNotFoundException e) {
            System.out.println("File " + salaryCsv.getName() + " not found");
        }
        return false;
    }

    /**
     * Checks if it's the 25th of the month, if so generates payslips for all employees (part-time employees who submitted a pay-claim).
     *
     * @throws IOException
     */
    public static void isTwentyFive() throws IOException {
        Calendar cal = Calendar.getInstance();
        String day = new SimpleDateFormat("dd").format(cal.getTime());
        ArrayList<String> s = new ArrayList<>();
        if (day.equals("25")) {
            Scanner sc = new Scanner(new File("data/employees.csv"));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(",");
                s.add(parts[0]);
                s.add(parts[1]);
                s.add(parts[2]);
            }
            sc.close();
            for (int i = 0; i < s.size(); ) {
                String name = s.get(i);
                String position = s.get(i + 1);
                int point = Integer.parseInt(s.get(i + 2));
                double salary = Payroll.getSalary(position, point);
                if (isPartTime(position, point)) {
                    Scanner claim = new Scanner(new File("data/payClaims.csv"));
                    while (claim.hasNextLine()) {
                        if (claim.nextLine().contains(name)) {
                            salary = Math.round(salary * 40); //assuming 40 hour work week
                            Payroll.generatePayslip(name, point);
                        }
                    }
                } else {
                    salary = Math.round(salary / 12);
                    Payroll.generatePayslip(name, salary);
                }
                i += 3;
            }
        }
    }

    /**
     * Checks if it is the 26th of the month to clear the pay claims.
     * @throws IOException
     */
    public static void isTwentySix() throws IOException {
        Calendar cal = Calendar.getInstance();
        String day = new SimpleDateFormat("dd").format(cal.getTime());
        //if (day.equals("26")) {
        if (true) {
            PrintWriter pw = new PrintWriter(new File("data/payClaims.csv"));
            pw.close();
        }
    }

    /**
     * Gets the maximum salary point for a given position.
     * @param position The position in question.
     * @return An integer value of the salary point.
     * @throws FileNotFoundException
     */
    public static int getMaxPoint(String position) throws FileNotFoundException {
        int maxPoint = 0;
        if (salaryCsv.exists()) {
            String[] salary;
            Scanner sc = new Scanner(salaryCsv);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                salary = line.split(",");
                if (line.contains(position)) {
                    maxPoint = Integer.parseInt(salary[2]);
                }
            }
        }
        return maxPoint;
    }
}