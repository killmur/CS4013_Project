import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
/**
 * A menu for the payroll system.
 */
public class PayrollMenu {
    private String name;
    private Scanner sc;

    public PayrollMenu() {
        sc = new Scanner(System.in);
    }

    /**
     * Runs the Payroll system.
     *
     * @param payroll the payroll commands.
     */
    public void payroll(Payroll payroll) throws IOException {
        boolean flag = true;
        Payslip.isOctober(); //For point promotion
        Payroll.isTwentyFive();
        Payroll.isTwentySix();
        //Gets the user to enter their name.
        System.out.println("Login with your name");
        this.name = sc.nextLine();
        //Choice for the user to enter their preferred control.
        System.out.println("A) Administrator. B) Human Resources C) Employee Q) Quit");
        String answer = sc.nextLine().toUpperCase();

        while (flag) {
            if (answer.equals("A")) {
                //Administrator Control
                System.out.println("A) Add a new Employee. Q) Quit");
                String ad = sc.nextLine().toUpperCase();
                if (ad.equals("A")) {
                    //new employee function
                    System.out.println("Enter the new employee's name: ");
                    String name = sc.nextLine();
                    System.out.println("Enter the new employee's position: ");
                    String position = sc.nextLine();
                    System.out.println("Enter the new employee's position point: ");
                    int point = sc.nextInt();
                    payroll.addEmployee(name, position, point);
                } else if (ad.equals("Q")) {
                    flag = false;
                }
            } else if (answer.equals("B")) {
                //Human Resources Control
                System.out.println("A) Promote an Employee. Q) Quit");
                String hr = sc.nextLine().toUpperCase();
                if (hr.equals("A")) {
                    //promote function
                    System.out.println("Enter Employee's name: ");
                    String name = sc.nextLine();
                    System.out.println("What position will they be promoted to?: ");
                    String pos = sc.nextLine();
                    System.out.println("What point will they be on their new position?: ");
                    int point = sc.nextInt();
                    System.out.println(Employee.promote(name, pos, point));
                } else if (hr.equals("Q")) {
                    flag = false;
                }
            } else if (answer.equals("C")) {
                //Employee Control
                System.out.println("A) View All Payslips. B) View Most Recent Payslip C) Submit Pay Claim (Part Time) Q) Quit");
                String empl = sc.nextLine().toUpperCase();
                if (empl.equals("A")) {
                    //view all payslips
                    System.out.println(payroll.getPayslips(name, 'a'));
                } else if (empl.equals("B")) {
                    //view most recent payslip
                    System.out.println(payroll.getPayslips(name, 'r'));

                } else if (empl.equals("C")) {
                    //submit pay claim
                    System.out.println(payroll.submitPayClaim(name));

                } else if (empl.equals("Q")) {
                    //Quit
                    flag = false;
                }
            } else if (answer.equals("Q")) {
                //Quit
                flag = false;
            }
        }
    }
}