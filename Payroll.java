import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Payroll {
    private ArrayList<Payslip> payslips = new ArrayList<Payslip>();
    private File salaryCsv = new File("data/salaryScales.csv");
    private File payslipCsv = new File("data/all_payslips.csv");
    private File employeeCsv = new File("data/employees.csv");


   /* public String getPayslips(String name) throws IOException {
        String[] slip;
        try {
            Scanner sc = new Scanner(payslipCsv);

            while (sc.hasNextLine()) {
                String line = sc.next();
                if (line.contains(name)) {
                    slip = line.split(",");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File " + payslipCsv.getName() + " not found");
        }
        for () {
            payslips.add(new Payslip(slip[i], Double.parseDouble(slip[i + 1]), Double.parseDouble(slip[i + 2]), Double.parseDouble(slip[i + 3]), Double.parseDouble(slip[i + 4]), Double.parseDouble(slip[i + 5]), Double.parseDouble(slip[i + 6]), Double.parseDouble(slip[i + 7]), Double.parseDouble(slip[i + 8]), Boolean.getBoolean(slip[i + 9])));
            i = i + 10;
        }
        for (Payslip p : payslips) {
            return p.toString();
        }
        return null;
    }

    */

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
}
