import java.io.IOException;

public class Run {
    public static void main(String[] args) throws IOException {
       /* Payroll payroll = new Payroll();
        PayrollMenu menu = new PayrollMenu();

        menu.payroll(payroll);
        */

        Payroll p = new Payroll();

        double full = p.getSalary("Full Professor",2);
        full = full/13;
        System.out.print(p.generatePayslip("Emma McAuley",full).toString());

    }
}