package Utils;

public enum Level {

    EASY(1),MEDIUM(2) , HARD(3);

    public final int level;

    Level(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static Level getLevelByNumber(int lev) {
        if(lev ==1) return EASY;
        if(lev ==2) return MEDIUM;
        return HARD;
    }

    //cast level value to a number (String format)
    public String castLevel(){
        if(this.equals(Level.EASY)){
            return "1";
        } else if(this.equals(Level.MEDIUM)){
            return "2";
        } else
            return "3";
    }

}

