import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DaySchedulePanel extends JPanel {
    private SchedulePlannerFrame frame;
    private final JButton btnCancel = new JButton("Cancel");
    private final JButton btnClose = new JButton("Close");
    private final JButton btnSave = new JButton("Save");
    private Schedule currentSchedule = null;
    private LocalDate currentDate;

    private JTextField titleField = new JTextField("");
    private JTextField startField = new JTextField("");
    private JTextField endField = new JTextField("");
    private JTextField placeField = new JTextField("");
    private JTextField memoField  = new JTextField("");

    public DaySchedulePanel(SchedulePlannerFrame frame) {
        this.frame = frame;
        this.setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.setLayout(new GridLayout(2, 5));

        top.add(new JLabel("Title"));
        top.add(new JLabel("Start Time"));
        top.add(new JLabel("End Time"));
        top.add(new JLabel("Place"));
        top.add(new JLabel("Memo"));

        top.add(titleField);
        top.add(startField);
        top.add(endField);
        top.add(placeField);
        top.add(memoField);

        JPanel bottom = new JPanel();
        bottom.add(btnCancel);
        bottom.add(btnClose);
        bottom.add(btnSave);
        btnCancel.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){ frame.onCancel(); }
        });
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){ frame.onSave(); }
        });
        btnClose.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){ frame.onClose(); }
        });

        this.add(top, BorderLayout.CENTER);
        this.add(bottom, BorderLayout.SOUTH);

        updateDaySchedule();
    }

    public void updateDaySchedule() {
        currentDate = frame.getCurrentDate();
        currentSchedule = frame.getCurrentSchedule();

        String title = "", start = "", end = "", place = "", memo = "";

        if (this.currentSchedule != null) {
            title = currentSchedule.getTitle();
            start = formatTime(currentSchedule.getStartTime());
            end = formatTime(currentSchedule.getEndTime());
            place = currentSchedule.getPlace() != null ? currentSchedule.getPlace() : "";
            memo = currentSchedule.getMemo() != null ? currentSchedule.getMemo() : "";
        }

        // 텍스트 필드 교체
        titleField.setText(title);
        startField.setText(start);
        endField.setText(end);
        placeField.setText(place);
        memoField.setText(memo);
    }

    // 시간 포맷팅 헬퍼 메서드
    private String formatTime(LocalDateTime time) {
        if (time == null) return "";
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }

    //getter
    public String getTitle(){ return titleField.getText(); }
    public LocalDateTime getStartTime(){
        if ( startField.getText().isEmpty() ) return null;
        LocalTime time = LocalTime.parse( startField.getText() );
        return LocalDateTime.of(currentDate, time);
    }
    public LocalDateTime getEndTime(){
        if ( endField.getText().isEmpty() ) return null;
        LocalTime time = LocalTime.parse( endField.getText() );
        return LocalDateTime.of(currentDate, time);
    }
    public String getPlace(){ return placeField.getText(); }
    public String getMemo(){ return memoField.getText(); }
}
