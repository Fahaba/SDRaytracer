package profiling;

import java.lang.instrument.Instrumentation;
/**
 * Created by hasselba on 12.12.17.
 */
public class RaytracerAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new RaytracerTransformer());
    }

}
