import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class MonthSchedulePanel extends JPanel {
    private static final String[] WEEK_NAMES = {"SUN", "MON", "TUE", "WED", "THR", "FRI", "SAT"};
    private int year;
    private int month;
    private int days;
    ScheduleList list;
    private JButton[] dayButtons = new JButton[43];
    private SchedulePlannerFrame frame;
    private int blankNum;

    public MonthSchedulePanel(SchedulePlannerFrame frame){
        this.frame = frame;
        this.setLayout(new GridLayout(0, 7));
        for (int i=0; i<7; i++){ this.add(new JLabel( WEEK_NAMES[i]) ); }

        for (int i=1; i<=42; i++ ) {
            dayButtons[i] = new JButton("");
            dayButtons[i].setEnabled(false);

            int finalI = i;
            dayButtons[i].addActionListener(new ActionListener() {
                public void actionPerformed ( ActionEvent e ){ frame.selectDate(finalI); }
            });
            this.add(dayButtons[i]);
        }
        updateMonthSchedule();
    }


    public void loadScheduleToMonth(ScheduleList list){
        for (int i=0; i<list.numSchedules(); i++){
            Schedule s = list.getSchedule(i);
            if (s == null) { continue; }
            int d = s.getStartTime().getDayOfMonth();
            if ((month == s.getStartTime().getMonth().getValue() ) && (year == s.getStartTime().getYear())) {
                dayButtons[d+ blankNum].setText("<html>" + d + "<br>" + s.getTitle() + "</html>" );
            }
        }
    }

    public void updateMonthSchedule(){
        year = frame.getCurrentYear();
        month = frame.getCurrentMonth();
        days = frame.getDaysInMonth();
        list = frame.getScheduleList();

        // 모든 버튼 초기화 (비활성화 및 텍스트 제거)
        for (int i = 1; i <= 42; i++) {
            dayButtons[i].setText("");
            dayButtons[i].setEnabled(false);
        }

        //한달의 시작 요일 맞추기위해 빈칸
        blankNum = LocalDate.of(year, month, 1).getDayOfWeek().getValue()%7;

        // 해당 월의 날짜 버튼만 활성화 및 텍스트 설정
        for (int i = 1; i <= days; i++) {
            dayButtons[i + blankNum].setText(String.valueOf(i));
            dayButtons[i + blankNum].setEnabled(true);
        }

        loadScheduleToMonth(list);
    }

    public LocalDate getDateFromIndex(int i){
        year = frame.getCurrentYear();
        month = frame.getCurrentMonth();
        days = frame.getDaysInMonth();
        blankNum = LocalDate.of(year, month, 1).getDayOfWeek().getValue()%7;

        if  (i-blankNum >0 && i-blankNum<=days) return LocalDate.of(year, month, i-blankNum);
        return null;
    }
}