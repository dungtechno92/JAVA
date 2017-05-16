package com.dungdv.java;

import java.util.Date;

/**
 * Created by DUNGDV on 5/17/2017.
 */
public final class Peroid {
    private final Date start;
    private final Date end;

    /**
     * @param start the beginning of the period
     * @param end   the end of the period; must not precede start
     * @throws IllegalArgumentException if start is after end
     * @throws NullPointerException     if start or end is null
     */
    public Peroid(Date start, Date end) {
        if (start.compareTo(end) > 0)
            throw new IllegalArgumentException(
                    start + " after " + end);
        this.start = start;
        this.end = end;
        end.setYear(78);
    }

    public Date start() {
        return start;
    }

    public Date end() {
        return end;
    }

}
