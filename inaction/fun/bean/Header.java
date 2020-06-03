package inaction.fun.bean;

public class Header {

    private String name;
    private String type;
    private Long totalSize;
    private String number;
    private Long curSize;

    public Header(){}

    public Header(String name, String type, Long totalSize, String number, Long curSize) {
        this.name = name;
        this.type = type;
        this.totalSize = totalSize;
        this.number = number;
        this.curSize = curSize;
    }

    public Header(String name, String type, Long size) {
        this.name = name;
        this.type = type;
        this.totalSize = size;
        this.curSize = size;
        this.number = "1/1";
    }

    @Override
    public String toString() {
        return "Header{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", totalSize=" + totalSize +
                ", number='" + number + '\'' +
                ", curSize=" + curSize +
                '}';
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

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getCurSize() {
        return curSize;
    }

    public void setCurSize(Long curSize) {
        this.curSize = curSize;
    }
}
