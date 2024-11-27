import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 * <h1>Payslip Generator</h1>
 * Generates the details for a payslip and calculates all deductions and tax.
 * @author Group 2
 * @version 1.0
 */
public class Payslip {
    private String name;
    private double ytd; //Pay "Year to Date"
    private double gross;//Pay before any deductions
    private double net; //Pay after all deductions
    private double paye;
    private double prsi;
    private double usc;
    private double health; //health insurance
    private double union; //union fees
    private boolean unite; //Is a member of unite?
    File output = new File("data/all_payslips.csv");

    /**
     * Constructs a payslip given the gorss pay, year to date, and whether the person is a union member.
     *
     * @param name         The name of the employee on the payslip.
     * @param gross_pay    The gross pay (pay before all deductions) for the payslip.
     * @param year_to_date The gross earnings of the employee for the year to date.
     * @param unite        Is the employee a member of the union i.e. "Unite"?
     */
    public Payslip(String name, double gross_pay, double year_to_date, boolean unite) {
        this.name = name;
        this.gross = gross_pay;
        this.ytd = year_to_date + gross_pay;
        this.net = gross;
        this.unite = unite;
    }

    /**
     * Constructs a payslip given all variables for the payslip, no calculations are made.
     *
     * @param name     The name of the employee on the payslip.
     * @param grossPay The gorss pay for the payslip.
     * @param net      The pay after deductions for the payslip.
     * @param paye     The PAYE tax amount in euros.
     * @param prsi     The PRSI tax amount in euros
     * @param usc      The USC tax amount in euros.
     * @param health   Health insurance deductible in euros.
     * @param union    Union deductible in euros.
     */
    public Payslip(String name, double grossPay, double net, double paye, double prsi, double usc, double health, double union) {
        this.name = name;
        this.gross = grossPay;
        this.net = net;
        this.paye = paye;
        this.prsi = prsi;
        this.usc = usc;
        this.health = health;
        this.union = union;
    }
// Tax is to be calculated as if person is single, i.e. 42,000 @ 20% and balance @ 40%.

    /**
     * Calculates the PAYE tax.
     */
    public void paye() {
        if (ytd <= 42000) {
            this.paye = gross * .2;
        } else {
            this.paye += gross * .4;
        }
        net -= paye;
    }

    /**
     * Calculates the PRSI tax.
     */
    public void prsi() {
        if (gross <= 352) {
            this.prsi = 0;
        } else if (gross <= 424) {
            double credit = 12 - ((gross - 352) * ((double) 1 / 6));
            this.prsi = (gross * .041) - credit;
        } else if (gross > 424) {
            this.prsi = gross * .041;
        }
        net -= prsi;
    }

    /**
     * Calculates the USC tax.
     */
    public void usc() {
        if (ytd <= 13000) {
            this.usc = 0;
        } else if (ytd < 25012) {
            this.usc = gross * .005;
        } else if (ytd < 38760) {
            this.usc = gross * .02;
        } else if (ytd < 83044) {
            this.usc = gross * .04;
        } else {
            this.usc = gross * .08;
        }
        net -= usc;
    }

    /**
     * Calculates the health insurance payment.
     */
    public void health() {

    }

    /**
     * Calcualtes the union payment.
     */
    public void union() {
        if (unite) {
            union = 19;
        } else {
            union = 0;
        }
        net -= union;
    }

    /**
     * @return Prints the details of the payslip.
     */
    public String toString() {
        String output = "Name: " + name + "\n" +
                "\t Gross Pay: " + gross + "\n"
                + "\t Net Pay: " + Math.abs(net) + "\n"
                + "\t\t PAYE: " + paye + "\n"
                + "\t\t PRSI: " + prsi + "\n"
                + "\t\t USC: " + usc + "\n"
                + "\t\t Health Insurance: " + health + "\n";
        if (unite) {
            output += "\t\t Union Fee: " + union + "\n";
        }
        return output;
    }

    /**
     * @return Returns the gross pay to date.
     */
    public double year_to_date() {
        return ytd;
    }

    /**
     * Saves the payslip to the CSV file.
     *
     * @throws FileNotFoundException where the CSV for payslips is not found.
     */
    public void save() throws FileNotFoundException {
        if (output.exists()) {
            paye();
            prsi();
            usc();
            health();
            union();
            PrintWriter writer = new PrintWriter(new FileOutputStream("data/all_payslips.csv", true));
            writer.append(name).append(",");
            writer.append(String.valueOf(gross)).append(",");
            writer.append(String.valueOf(Math.abs(net))).append(",");
            writer.append(String.valueOf(paye)).append(",");
            writer.append(String.valueOf(prsi)).append(",");
            writer.append(String.valueOf(usc)).append(",");
            writer.append(String.valueOf(health)).append(",");
            writer.append(String.valueOf(union)).append("\n");
            writer.close();
        }
    }

    /**
     * Checks if it is the 1st of October for salary scale point movement and runs Employee.addPoint() if it is.
     *
     * @throws FileNotFoundException
     */
    public static void isOctober() throws FileNotFoundException {
        Calendar cal = Calendar.getInstance();
        String mon = new SimpleDateFormat("MMM").format(cal.getTime());
        String day = new SimpleDateFormat("dd").format(cal.getTime());
        if (mon.equals("Oct") && day.equals("01")) {
            Employee.addPoint();
        }
    }
}