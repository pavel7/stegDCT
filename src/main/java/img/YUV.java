package img;

public class YUV {
    public float y;
    public float u;
    public float v;

    public YUV ()
    {
        this.y = 0;
        this.u = 0;
        this.v = 0;
    }

    public YUV (float y, float u, float v)
    {
        this.y = y;
        this.u = u;
        this.v = v;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }
}
