package Teach.Exams.Exam1.Server;

import java.time.Year;

public class Candidate {
    private int id;
    private String name;
    private String birthday;
    private String address;

    public Candidate(String name, String birthday, String address) {
        this.name = name;
        this.birthday = birthday;
        this.address = address;
    }

    public boolean isValidAge() {
        String[] birthdaySplit = birthday.split("/");
        int year = Integer.parseInt(birthdaySplit[birthdaySplit.length - 1]);
        return Year.now().getValue() - year <= 10;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
