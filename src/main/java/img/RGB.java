package img;

public class RGB {
    public short r;
    public short g;
    public short b;

    public RGB()
    {
        this.r = 0;
        this.g = 0;
        this.b = 0;
    }

    public RGB(short r, short g, short b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public short getR() {
        return r;
    }

    public short getG() {
        return g;
    }

    public short getB() {
        return b;
    }
}
