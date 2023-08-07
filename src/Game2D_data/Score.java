package Game2D_data;

import java.sql.Timestamp;

public class Score {
    private String create_time;
    private int phone_number;
    private int score;
    private String time;

    public Score(String create_time, int phone_number, int score, String time) {
        this.create_time = create_time;
        this.phone_number = phone_number;
        this.score = score;
        this.time = time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public int getScore() {
        return score;
    }

    public String getTime() {
        return time;
    }
    
}
