package com.lemkhantar.fundamental.entity;

/**
 * Created by lemkhantar1 on 7/6/16.
 */
public class Time {

    private int heure;
    private int minute;
    private int seconde;

    public Time(int heure, int minute, int seconde) {
        this.heure = heure;
        this.minute = minute;
        this.seconde = seconde;
    }

    public void setHeure(int heure) {
        this.heure = heure;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setSeconde(int seconde) {
        this.seconde = seconde;
    }

    public int getHeure() {
        return heure;
    }

    public int getMinute() {
        return minute;
    }

    public int getSeconde() {
        return seconde;
    }

    public int getTime()
    {
        String h = String.valueOf(heure);
        String m = String.valueOf(minute);
        String s = String.valueOf(seconde);

        if(h.length()==1)h="0"+h;
        if(m.length()==1)m="0"+m;
        if(s.length()==1)s="0"+s;

        String temps = h+""+m+""+s;

        return Integer.parseInt(temps);
    }
}
