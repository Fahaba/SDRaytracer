package profiling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
/**
 * Created by hasselba on 12.12.17.
 */
public class RaytracerAgent {

    public static BufferedWriter output = null;

    public static void premain(String agentArgs, Instrumentation inst) {

        try {
            File file = new File("./log.log");
            RaytracerAgent.output = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        inst.addTransformer(new RaytracerTransformer());
    }

}
