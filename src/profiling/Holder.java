package profiling;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class Holder {

    public static Map<String, Long> sMethodStart    = new HashMap<>();
    public static Map<String, Long> sMethodEnd      = new HashMap<>();

    static Vector vec = new Vector<Pair>();

    public static void trackStartTime(String methodName, long time){
        sMethodStart.put(methodName, time);
    }

    public static void trackEndTime(String methodName, long time){
        sMethodStart.put(methodName, time);
    }

    public static void calculateTimes(String methodName, long time_after){
        System.out.println(methodName);
        long start = sMethodStart.get(methodName);
        long end = sMethodEnd.get(methodName);
        System.out.println(start + " " + end);
        //return "method: " + methodName + " main " + (end - start) + " ns";
    }

    public static void printCalcs() {
        for (Map.Entry<String, Long> entry : sMethodStart.entrySet())
        {
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
    }

    public static void print1(String method, Integer id, long timeBefore, long timeAfter) {
        System.err.println(timeBefore + "    " + timeAfter);
        sMethodStart.put(method, (timeAfter - timeBefore));
        //long timeAfter = System.nanoTime();
        //vec.add(new Pair(method, timeAfter));
        //System.out.println(method + "   Entering!! on : " + (timeAfter - timeBefore) + " at " + id);
    }

    public static void print2(String method) {
        long timeAfter = System.nanoTime();

        System.out.println(method + "   Leaving!!! on : " + timeAfter);
    }

    private static Integer medthodCount = 0 ;
    public static synchronized Integer getMethodID() {
        return medthodCount ++ ;
    }
}

class Pair {
    String methodName;
    long nanoTime;

    public Pair(String methodName, long nanoTime){
        this.methodName = methodName;
        this.nanoTime = nanoTime;
    }
}
