package profiling;

import java.io.IOException;

public class MyLogger {


    public static void log(String method, long timeBefore, long timeAfter) {
        //System.err.println(method + ":    " + timeBefore + "    " + timeAfter + "    " + (timeAfter - timeBefore));

        try{
            RaytracerAgent.output.write(method + ";" + (timeAfter - timeBefore));
            RaytracerAgent.output.newLine();
            RaytracerAgent.output.flush();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    /*
    private static Integer medthodCount = 0 ;
    public static synchronized Integer getMethodID() {
        return medthodCount ++ ;
    }
    */
}

