package geometrics;
import color.RGB;
import task.SDRaytracer;

public class Ray {

    static RGB ambient_color = new RGB(0.01f, 0.01f, 0.01f);
    Vec3D start = new Vec3D(0, 0, 0);
    Vec3D dir = new Vec3D(0, 0, 0);

    Light mainLight = new Light(new Vec3D(0, 100, 0), new RGB(0.1f, 0.1f, 0.1f));
    Light[] lights = new Light[]{mainLight
            , new Light(new Vec3D(100, 200, 300), new RGB(0.5f, 0, 0.0f))
            , new Light(new Vec3D(-100, 200, 300), new RGB(0.0f, 0, 0.5f))
            //,new geometrics.Light(new Vec3D(-100,0,0), new color.RGB(0.0f,0.8f,0.0f))
    };

    public void setStart(float x, float y, float z) {
        start = new Vec3D(x, y, z);
    }

    public void setDir(float dx, float dy, float dz) {
        dir = new Vec3D(dx, dy, dz);
    }

    public void normalize() {
        dir.normalize();
    }

    // see M�ller&Haines, page 305
    IPoint intersect(Triangle t) {
        float epsilon = IPoint.EPSILON;
        Vec3D e1 = t.p2.minus(t.p1);
        Vec3D e2 = t.p3.minus(t.p1);
        Vec3D p = dir.cross(e2);
        float a = e1.dot(p);
        if ((a > -epsilon) && (a < epsilon)) return new IPoint(null, null, -1);
        float f = 1 / a;
        Vec3D s = start.minus(t.p1);
        float u = f * s.dot(p);
        if ((u < 0.0) || (u > 1.0)) return new IPoint(null, null, -1);
        Vec3D q = s.cross(e1);
        float v = f * dir.dot(q);
        if ((v < 0.0) || (u + v > 1.0)) return new IPoint(null, null, -1);
        // intersection point is u,v
        float dist = f * e2.dot(q);
        if (dist < epsilon) return new IPoint(null, null, -1);
        Vec3D ip = t.p1.mult(1 - u - v).add(t.p2.mult(u)).add(t.p3.mult(v));
        return new IPoint(t, ip, dist);
    }

    RGB lighting(Ray ray, IPoint ip, int rec) {
        Vec3D point = ip.vec;
        Triangle triangle = ip.triangle;
        RGB color = RGB.addColors(triangle.color, ambient_color, 1);
        Ray shadow_ray = new Ray();
        for (Light light : lights) {
            shadow_ray.start = point;
            shadow_ray.dir = light.position.minus(point).mult(-1);
            shadow_ray.dir.normalize();
            IPoint ip2 = IPoint.hitObject(shadow_ray);
            if (ip2.dist < IPoint.EPSILON) {
                float ratio = Math.max(0, shadow_ray.dir.dot(triangle.normal));
                color = RGB.addColors(color, light.color, ratio);
            }
        }
        Ray reflection = new Ray();
        //R = 2N(N*L)-L)    L ausgehender Vektor
        Vec3D L = ray.dir.mult(-1);
        reflection.start = point;
        reflection.dir = triangle.normal.mult(2 * triangle.normal.dot(L)).minus(L);
        reflection.dir.normalize();
        RGB rcolor = reflection.rayTrace(rec + 1);
        float ratio = (float) Math.pow(Math.max(0, reflection.dir.dot(L)), triangle.shininess);
        color = RGB.addColors(color, rcolor, ratio);
        return (color);
    }

    static RGB black = new RGB(0.0f, 0.0f, 0.0f);

    public RGB rayTrace(int rec) {
        if (rec > SDRaytracer.maxRec) return black;
        IPoint ip = IPoint.hitObject(this);
        if (ip.dist > IPoint.EPSILON)
            return lighting(this, ip, rec);
        else
            return black;
    }
}
