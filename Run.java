import java.io.IOException;

public class Run {
    public static void main(String[] args) throws IOException {

       Payroll payroll = new Payroll();
       PayrollMenu menu = new PayrollMenu();
       menu.payroll(payroll);


    }
}