import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TaxiSchedule extends JFrame {
    private JTable scheduleTable;
    private ArrayList<Taxi> taxis;
    private String[][] schedules;
    private SimpleDateFormat timeFormat;

    public TaxiSchedule() {
        setTitle("Real-Time Taxi Schedules");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BorderLayout());
        schedulePanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Real-Time Taxi Schedules", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        schedulePanel.add(titleLabel, BorderLayout.NORTH);

        taxis = loadTaxiData("taxis.ser");
        taxis = filterRealTimeTaxis(taxis);
        timeFormat = new SimpleDateFormat("hh:mm a");

        String[] columnNames = {"Taxi Number", "Start Time", "End Time"};
        schedules = new String[taxis.size()][3];
        updateSchedulesData();

        scheduleTable = new JTable(schedules, columnNames);
        scheduleTable.setRowHeight(30);
        scheduleTable.setFont(new Font("Arial", Font.PLAIN, 16));
        scheduleTable.setGridColor(new Color(169, 169, 169));

        JTableHeader tableHeader = scheduleTable.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 18));
        tableHeader.setBackground(new Color(52, 152, 219));
        tableHeader.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < scheduleTable.getColumnCount(); i++) {
            scheduleTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        schedulePanel.add(scrollPane, BorderLayout.CENTER);

        RoundedButton backButton = new RoundedButton("Back", new Color(70, 130, 180), Color.WHITE);
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(100, 40));

        backButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
        add(schedulePanel, BorderLayout.CENTER);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateScheduleTable();
            }
        }, 0, 60000);

        setVisible(true);
    }

    private void updateScheduleTable() {
        taxis = filterRealTimeTaxis(taxis);
        schedules = new String[taxis.size()][3];
        updateSchedulesData();
        scheduleTable.setModel(new javax.swing.table.DefaultTableModel(schedules, new String[]{"Taxi Number", "Start Time", "End Time"}));
    }

    private void updateSchedulesData() {
        for (int i = 0; i < taxis.size(); i++) {
            Taxi taxi = taxis.get(i);
            schedules[i][0] = taxi.getTaxiNumber();
            schedules[i][1] = taxi.getStartTime();
            schedules[i][2] = taxi.getEndTime();
        }
    }

    private ArrayList<Taxi> loadTaxiData(String fileName) {
        ArrayList<Taxi> taxiList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            taxiList = (ArrayList<Taxi>) ois.readObject();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Taxi data file not found: " + fileName, "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Failed to load taxi data from file.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return taxiList;
    }

    private ArrayList<Taxi> filterRealTimeTaxis(ArrayList<Taxi> taxis) {
        ArrayList<Taxi> realTimeTaxis = new ArrayList<>();
        String currentTime = timeFormat.format(new Date());

        try {
            Date currentDateTime = timeFormat.parse(currentTime);
            for (Taxi taxi : taxis) {
                Date taxiStartTime = timeFormat.parse(taxi.getStartTime());
                if (!taxiStartTime.before(currentDateTime)) {
                    realTimeTaxis.add(taxi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return realTimeTaxis;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaxiSchedule());
    }
}
