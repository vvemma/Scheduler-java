import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SchedulePlannerFrame extends JFrame {
    private MonthTopPanel monthTopPanel;
    private MonthSchedulePanel monthSchedulePanel;
    private ScheduleList list = new ScheduleList();
    private DaySchedulePanel daySchedulePanel;
    private LocalDate currentDate;
    private Schedule currentSchedule;

    //생성자
    public SchedulePlannerFrame(int year, int month) {
        this.setTitle("Schedule Planer");
        this.setSize(500,700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //창 닫기
        this.setLayout(new BorderLayout() ); //레이아웃 매니저
        this.currentDate = LocalDate.of(year,month,1);

        monthTopPanel = new MonthTopPanel(this);
        monthSchedulePanel = new MonthSchedulePanel(this);
        daySchedulePanel = new DaySchedulePanel(this);

        this.add(monthTopPanel, BorderLayout.NORTH);
        this.add(monthSchedulePanel, BorderLayout.CENTER);
        this.add(daySchedulePanel,BorderLayout.SOUTH);

        this.setVisible(true);
    }

    // 스케줄 불러오기
    public void loadSchedule(String fileName){
        this.list = new ScheduleList(fileName);
        this.updateDisplay();
    }

    // getter
    public ScheduleList getScheduleList(){ return list; }
    public LocalDate getCurrentDate() { return currentDate; }
    public int getCurrentYear(){ return currentDate.getYear(); }
    public int getCurrentMonth(){ return currentDate.getMonthValue(); }
    public int getDaysInMonth(){ return currentDate.lengthOfMonth(); }
    public Schedule getCurrentSchedule(){ return currentSchedule; }

    //setter
    public void setCurrentDate(LocalDate date){ this.currentDate = date; }
    public void setCurrentSchedule(Schedule schedule){ this.currentSchedule = schedule; }

    public void previousMonth() {
        setCurrentDate(currentDate.minusMonths(1));
        setCurrentSchedule(list.findByDate(currentDate));
        updateDisplay();
    }
    public void nextMonth() {
        setCurrentDate(currentDate.plusMonths(1));
        setCurrentSchedule(list.findByDate(currentDate));
        updateDisplay();
    }

    // 화면 갱신
    private void updateDisplay() {
        monthTopPanel.updateMonthText();
        monthSchedulePanel.updateMonthSchedule();
        daySchedulePanel.updateDaySchedule();
        this.repaint();
    }

    // 특정 날짜 선택
    public void selectDate(int index) {
        LocalDate d = monthSchedulePanel.getDateFromIndex(index);
        this.setCurrentDate(d);

        Schedule s = list.findByDate(currentDate);
        // 일정이 없으면 null로 설정

        this.setCurrentSchedule(s);
        daySchedulePanel.setVisible(true);
        this.updateDisplay();
    }

    //일정 삭제
    public void onCancel(){
        currentDate = getCurrentDate();
        currentSchedule = getCurrentSchedule();

        if (currentSchedule != null) {
            list.delete(currentSchedule);
            list.save();
            setCurrentSchedule(null);
        }

        this.daySchedulePanel.setVisible(false);
        updateDisplay();
    };

    //일정 저장
    public void onSave(){
        currentDate = getCurrentDate();
        currentSchedule = getCurrentSchedule();

        String title = daySchedulePanel.getTitle();
        LocalDateTime start = daySchedulePanel.getStartTime();
        if (title == null || title.isEmpty() || start == null) return;
        LocalDateTime end = daySchedulePanel.getEndTime();
        String place = daySchedulePanel.getPlace();
        String memo = daySchedulePanel.getMemo();


        Schedule newSchedule = new Schedule(title, start, end, place, memo);

        if (currentSchedule != null) { list.update(currentSchedule, newSchedule); }
        else { list.add(newSchedule); }

        setCurrentSchedule(newSchedule);
        list.save();
        updateDisplay();
    };

    //창 닫기
    public void onClose(){
        this.daySchedulePanel.setVisible(false);
        updateDisplay();
    };
}
