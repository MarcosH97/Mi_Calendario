package cu.uci.micalendario;

public class Events {
    String DATE, EVENT, MONTH, TIME, YEAR;

    public Events(String DATE, String EVENT, String MONTH, String TIME, String YEAR) {
        this.DATE = DATE;
        this.EVENT = EVENT;
        this.MONTH = MONTH;
        this.TIME = TIME;
        this.YEAR = YEAR;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getEVENT() {
        return EVENT;
    }

    public void setEVENT(String EVENT) {
        this.EVENT = EVENT;
    }

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }
}
