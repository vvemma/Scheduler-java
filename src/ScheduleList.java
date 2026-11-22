import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class ScheduleList {

    private ArrayList<Schedule> schedules;
    private String fileName; //파일 이름 저장


    //생성자 (빈 일정)
    public ScheduleList(){
        schedules = new ArrayList<>();
        fileName = null;
    }

    //생성자 (파일 이름을 받아 파일의 내용으로 일정리스트 생성)
    public ScheduleList(String fileName ){
        schedules = new ArrayList<>();
        this.fileName = fileName;
        produceFromFile(fileName);
    }


    //파일 읽기
    public void produceFromFile(String fileName){
        Scanner input = null;

        //파일 읽기에 실패할 경우 프로그램이 강제종료되지않게 try-catch 사용
        try {
            File file = new File(fileName);
            input = new Scanner(file);
        } catch (Exception e){
            System.out.println("파일 읽기에 실패했습니다.");
            return;
        }

        //파일 파싱
        String parsingTitle = null;
        LocalDateTime parsingStart = null;
        LocalDateTime parsingEnd = null;
        String parsingPlace = null;
        String parsingMemo = null;
        boolean hasError = false;

        while (input.hasNext()){
            String raw = input.nextLine();
            raw = raw.trim(); //문장 앞 뒤 공백 제거

            //빈 줄과 주석 무시
            if ( raw.isEmpty() || raw.startsWith("//") ) {
                continue; //다음 while문 수행
            }

            //data에서 일정과 일정을 ;로 구분함
            //parsing해둔 data 저장, 파일의 다음으로
            if ( raw.equals(";") ) {
                saveSchedule(parsingTitle, parsingStart, parsingEnd, parsingPlace, parsingMemo, hasError);
                //상태 초기화
                parsingTitle = null;
                parsingStart = null;
                parsingEnd = null;
                parsingPlace = null;
                parsingMemo = null;
                hasError = false;
                continue; //다음 while문 수행
            }

            int index = raw.indexOf("=");

            if( index<0 ){
                System.out.println("data 형식이 잘못되었습니다.");
                continue; //다음 while문 수행
            }

            //raw에서 keyword 추출
            String keyword = "";
            for (int i=0; i<index; i++){
                keyword += raw.charAt(i);
            }
            keyword = keyword.trim();

            //raw에서 value 추출
            String value ="";
            for (int i=index+1; i<raw.length(); i++){
                value += raw.charAt(i);
            }
            value = value.trim();

            //switch문으로 값 저장
            switch (keyword){

                case "TITLE" :
                    //필수 항목 없으면 해당 일정은 일정리스트에 등록하지 않음
                    if ( value.isEmpty() ){
                        System.out.println("일정의 이름이 존재하지 않습니다.");
                        hasError = true;
                    } else{ parsingTitle = value; }
                    break;

                case "START" :
                    if ( value.isEmpty() ){
                        System.out.println("일정의 시작시간이 존재하지 않습니다.");
                        hasError = true;
                    }
                    else{
                        //String으로 저장되어있는 시작시간을 포맷터로 data type 변경
                        try{
                            parsingStart = LocalDateTime.parse(value, Schedule.FORMATTER);
                        } catch (Exception e){
                            System.out.println("data가 유효한 시간형식을 갖추고 있지 않습니다.");
                            hasError = true;
                        }
                    }
                    break;

                case "END":
                    if( !value.isEmpty() ){
                        try{
                            parsingEnd = LocalDateTime.parse(value, Schedule.FORMATTER);
                        } catch (Exception e){
                            System.out.println("data가 유효한 시간형식을 갖추고 있지 않습니다.");
                            //종료시간 필수항목 아니므로 일정리스트에 등록 o
                        }
                    }
                    break;

                case "PLACE":
                    if( !value.isEmpty() ) {
                        parsingPlace = value;
                    }
                    break;

                case "MEMO":
                    if( !value.isEmpty() ) {
                        parsingMemo = value;
                    }
                    break;

                default:
                    System.out.println("data에 알 수 없는 Keyword가 포함되어 있습니다.");
                    break;
            }
        }

        //파일 끝에 도달했을 때 마지막 일정 저장 (;가 없는 경우 대비)
        //파싱 중인 데이터가 있는 경우에만 저장
        if (parsingTitle != null || parsingStart != null) {
            saveSchedule(parsingTitle, parsingStart, parsingEnd, parsingPlace, parsingMemo, hasError);
        }

        input.close();
    }


    //파일을 읽어 가져온 parsing정보를 일정리스트에 저장
    private void saveSchedule(String title, LocalDateTime start, LocalDateTime end, String place, String memo, boolean hasError ){
        if (hasError) {
            System.out.println("일정을 저장하지 못했습니다.");
            return;
        }

        //시간 순서 검사
        if (end != null && end.isBefore(start)) {
            System.out.println("종료 시간이 시작 시간보다 앞선 data를 발견했습니다.");
            System.out.println("해당 일정을 저장하지 못했습니다.");
            return;
        }

        //정상 일정 추가
        this.add(new Schedule(title, start, end, place, memo));
    }


    //전체 일정 수
    public int numSchedules(){
        return schedules.size();
    }

    //i번째 일정 정보
    public Schedule getSchedule(int i){
        if ( i<0 || i>=schedules.size() ) {
            System.out.println("입력한 번호의 일정이 존재하지 않습니다.");
            return null;
        }
        return schedules.get(i);
    }

    //새 일정 추가
    public void add(Schedule s){
        if ( s==null ){
            System.out.println("일정을 추가할 수 없습니다.");
            return;
        }
        schedules.add(s);
    }

    //특정 날짜의 일정 찾기
    public Schedule findByDate(LocalDate date){
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        for (Schedule s : schedules) {
            int scheduleYear = s.getStartTime().getYear();
            int scheduleMonth = s.getStartTime().getMonthValue();
            int scheduleDay = s.getStartTime().getDayOfMonth();

            if (scheduleYear == year && scheduleMonth == month && scheduleDay == day) {
                return s;
            }
        }

        return null;
    }

    //특정 일정 삭제
    public void delete(Schedule s){
        schedules.remove(s);
    }

    //특정 일정 업데이트
    public void update(Schedule originalSchedule, Schedule newSchedule){
        int index = schedules.indexOf(originalSchedule);
        if (index >= 0) {
            schedules.set(index, newSchedule);
        }
    }

    //일정 리스트를 파일에 저장
    private void saveToFile(String fileName) {
        PrintWriter output = null;
        try {
            output = new PrintWriter(new File(fileName));
        } catch (Exception e) {
            System.out.println("파일을 생성할 수 없습니다.");
            return;
        }

        for (Schedule s : schedules) {
            output.println("TITLE = " + s.getTitle());
            output.println("START = " + s.getStartTime().format(Schedule.FORMATTER));
            if (s.hasEndTime()) {
                output.println("END = " + s.getEndTime().format(Schedule.FORMATTER));
            }
            if (s.hasPlace()) {
                output.println("PLACE = " + s.getPlace());
            }
            if (s.hasMemo()) {
                output.println("MEMO = " + s.getMemo());
            }
            output.println(";");
        }

        output.close();
        System.out.println(fileName + "(으)로 일정리스트를 저장했습니다.");
    }

    //일정 리스트를 현재 파일명으로 저장
    public void save() {
        if (fileName != null) {
            saveToFile(fileName);
        }
    }
}