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
 * @param payroll the payroll commands.
 */
public void payroll(Payroll payroll) throws IOException {
    boolean flag = true;

    //Gets the user to enter their name.
    System.out.print("Login with your name");
    this.name = sc.nextLine();
    //Choice for the user to enter their preferred control.
    System.out.print("A) Administrator. B) Human Resources C) Employee Q) Quit");
    String answer = sc.nextLine().toUpperCase();

    while (flag) {
        if (answer.equals("A")) {
            //Administrator Control
        } else if(answer.equals("B")) {
            //Human Resources Control
        }else if (answer.equals("C")) {
            //Employee Control
            System.out.print("A) View All Payslips. B) View Most Recent Payslip C) Submit Pay Claim (Part Time) Q) Quit");
            String empl = sc.nextLine().toUpperCase();
            if (empl.equals("A")) {
                //view all payslips
               System.out.print(payroll.getPayslips(name, 'a'));
            }else if(empl.equals("B")) {
                //view most recent payslip
                System.out.print(payroll.getPayslips(name, 'r'));

            }else if(empl.equals("C")) {
                //submit pay claim

            } else if(empl.equals("Q")) {
                //Quit
                flag = false;
            }
        } else if (answer.equals("Q")) {
            //Quit
            flag = false;
        }
    }
}

    /**
     *Allows the user to get a choice from an array.
     * @param choices the array from which the user will enter their choice.
     * @return The array element which the user chose.
     */
    private Object getChoice(Object[] choices)
    {
        while (true)
        {
            char c = 'A';
            for (Object choice : choices)
            {
                System.out.println(c + ") " + choice);
                c++;
            }
            String input = sc.nextLine();
            int n = input.toUpperCase().charAt(0) - 'A';
            if (0 <= n && n < choices.length)
                return choices[n];
        }
    }
}
