import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Schedule {

    //시간 포맷터
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    //필드
    private String title; //이름
    private LocalDateTime startTime; //시작시간
    private LocalDateTime endTime; //종료시간
    private String place; //장소
    private String memo; //메모


    //생성자 (필수 항목 2개만 받을 경우)
    public Schedule(String title, LocalDateTime startTime){
        this.title = title;
        this.startTime = startTime;
        this.endTime= null;
        this.place = null;
        this.memo = null;
    }

    //생성자 (전체 항목 5개 받을 경우)
    public Schedule(String title, LocalDateTime startTime, LocalDateTime endTime, String place, String memo) {
        this.title     = title;
        this.startTime = startTime;
        this.endTime   = endTime;
        this.place     = place;
        this.memo      = memo;
    }


    //getter
    public String getTitle() {return title;}
    public LocalDateTime getStartTime() {return startTime;}
    public LocalDateTime getEndTime() {
        if(this.hasEndTime()) return endTime;
        return null;
    }
    public String getPlace() {
        if (this.hasPlace()) return place;
        return null;
    }
    public String getMemo() {
        if(this.hasMemo()) return memo;
        return  null;
    }

    //setter
    public void setTitle(String title) {this.title = title;}
    public void setStartTime(LocalDateTime start) {this.startTime = start;}
    public void setEndTime(LocalDateTime end) { this.endTime = end;}
    public void setPlace(String place) { this.place = place;}
    public void setMemo(String memo) { this.memo = memo;}

    //항목 존재 여부
    public boolean hasEndTime() { return endTime != null; }
    public boolean hasPlace()   { return place != null && !place.isEmpty(); }
    public boolean hasMemo()    { return memo  != null && !memo.isEmpty();  }
}