import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Payroll {
    private ArrayList<Payslip> payslips = new ArrayList<Payslip>();
    private File salaryCsv = new File("data/salaryScales.csv");
    private File payslipCsv = new File("data/all_payslips.csv");
    private File employeeCsv = new File("data/employees.csv");


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

    public double getSalary(String position, int point) throws IOException {
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

    public Payslip generatePayslip(String name, double gross) throws IOException {
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
        } catch (FileNotFoundException e) {
            System.out.println("File " + payslipCsv.getName() + " not found");
        }
        boolean union = false;
        try{
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
        }catch (FileNotFoundException e){
            System.out.println("File " + employeeCsv.getName() + " not found");
        }
        Payslip p = new Payslip(name, gross, year, union);
        p.paye();
        p.prsi();
        p.usc();
        p.health();
        if (union){
            p.union();
        }
        p.save();
        return p;
    }

    public void addEmployee(String name, String position, int point) throws FileNotFoundException {
        LocalDate hireDate = LocalDate.now();
        Employee emp = new Employee(name, position, point, false, hireDate);
        emp.save();
    }
}
