import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MonthTopPanel extends JPanel{
    private SchedulePlannerFrame frame;
    private final JButton btnPrev = new JButton("<");
    private JLabel monthLabel;
    private final JButton btnNext = new JButton(">");
    private int year;
    private int month;

    public MonthTopPanel(SchedulePlannerFrame frame){
        this.frame = frame;
        year = frame.getCurrentYear();
        month = frame.getCurrentMonth();

        monthLabel = new JLabel(year+ "년 " + month +"월");
        this.add(btnPrev);
        this.add(monthLabel);
        this.add(btnNext);

        // 버튼에 이벤트 리스너 추가
        btnPrev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { frame.previousMonth(); }
        });
        btnNext.addActionListener( new ActionListener() {
           public void actionPerformed(ActionEvent e){  frame.nextMonth(); }
        });
    }

    public void updateMonthText() {
        year = frame.getCurrentYear();
        month = frame.getCurrentMonth();
        monthLabel.setText(year + "년 " + month + "월");
    }
}


