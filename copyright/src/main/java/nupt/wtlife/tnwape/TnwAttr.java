package nupt.wtlife.tnwape;

public class TnwAttr {
    public String name;
    public String type;
    public int weight;

    public TnwAttr(String name, String type, int weight) {
        this.name = name;
        this.type = type;
        this.weight = weight;
    }

    public TnwAttr(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
