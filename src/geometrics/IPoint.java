package geometrics;
import task.SDRaytracer;

class IPoint {
    final static float EPSILON = 0.0001f;
    Triangle triangle;
    Vec3D vec;
    float dist;

    IPoint(Triangle tt, Vec3D ip, float d) {
        triangle = tt;
        vec = ip;
        dist = d;
    }

    static IPoint hitObject(Ray ray) {
        IPoint isect = new IPoint(null, null, -1);
        float idist = -1;
        for (Triangle t : SDRaytracer.triangles) {
            IPoint ip = ray.intersect(t);
            if (ip.dist != -1 && ((idist == -1) || (ip.dist < idist) )) { // save that intersection
                idist = ip.dist;
                isect.vec = ip.vec;
                isect.dist = ip.dist;
                isect.triangle = t;
            }
        }
        return isect;  // return intersection point and normal
    }
}
