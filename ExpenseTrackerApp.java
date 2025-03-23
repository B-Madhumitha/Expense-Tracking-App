import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// Expense class to store details
class Expense {
    String category;
    double amount;
    String date;

    public Expense(String category, double amount, String date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }
}

// ExpenseTrackerApp class with GUI
public class ExpenseTrackerApp {
    private final List<Expense> expenses = new ArrayList<>();
    private double totalBudget = 0;
    private final JTextField categoryField, amountField, dateField, budgetField;
    private final JLabel budgetLabel;
    private final DefaultTableModel tableModel;

    public ExpenseTrackerApp() {
        JFrame frame = new JFrame("Expense Tracker");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.add(new JLabel("Total Budget: Rs."));
        budgetField = new JTextField();
        inputPanel.add(budgetField);
        JButton setBudgetButton = new JButton("Set Budget");
        inputPanel.add(setBudgetButton);

        inputPanel.add(new JLabel("Category: "));
        categoryField = new JTextField();
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Amount: Rs."));
        amountField = new JTextField();
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Date (dd-mm-yyyy): "));
        dateField = new JTextField();
        inputPanel.add(dateField);
        JButton addButton = new JButton("Add Expense");
        inputPanel.add(addButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        // Table to show expenses
        String[] columns = {"Category", "Amount", "Date"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Budget Panel
        JPanel budgetPanel = new JPanel();
        budgetLabel = new JLabel("Remaining Budget: Rs. 0.0");
        budgetPanel.add(budgetLabel);
        JButton deleteButton = new JButton("Delete Expense");
        budgetPanel.add(deleteButton);
        frame.add(budgetPanel, BorderLayout.SOUTH);

        // Button Actions
        setBudgetButton.addActionListener(e -> setBudget());
        addButton.addActionListener(e -> addExpense());
        deleteButton.addActionListener(e -> deleteExpense(table));

        frame.setVisible(true);
    }

    private void setBudget() {
        String budgetText = budgetField.getText().trim();
        if (budgetText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a valid budget.");
            return;
        }
        try {
            totalBudget = Double.parseDouble(budgetText);
            budgetLabel.setText("Remaining Budget: Rs. " + totalBudget);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid budget amount. Please enter a numeric value.");
        }
    }

    private void addExpense() {
        String category = categoryField.getText().trim();
        String amountText = amountField.getText().trim();
        String date = dateField.getText().trim();

        // Check for empty fields
        if (category.isEmpty() || amountText.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all fields before adding an expense.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);

            if (amount > totalBudget) {
                JOptionPane.showMessageDialog(null, "Warning: Expense exceeds budget!");
                return;
            }

            expenses.add(new Expense(category, amount, date));
            tableModel.addRow(new Object[]{category, amount, date});
            totalBudget -= amount;
            budgetLabel.setText("Remaining Budget: Rs. " + totalBudget);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a numeric value.");
        }
    }

    private void deleteExpense(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            double amount = (double) tableModel.getValueAt(selectedRow, 1);
            totalBudget += amount;
            expenses.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            budgetLabel.setText("Remaining Budget: Rs. " + totalBudget);
        } else {
            JOptionPane.showMessageDialog(null, "Select an expense to delete!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseTrackerApp::new);
    }
}