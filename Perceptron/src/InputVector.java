public class InputVector {
    private double[] attributes;
    private String classification;

    public InputVector(String[] line, boolean isTest){
        if(isTest){
            this.attributes = new double[line.length];
            for(int i = 0; i < attributes.length; i++){
                this.attributes[i] = Double.parseDouble(line[i]);
            }
        } else {
            this.attributes = new double[line.length - 1];
            for(int i = 0; i < attributes.length; i++) {
                this.attributes[i] = Double.parseDouble(line[i]);
            }
            this.classification = line[line.length-1];
        }


    }

    public double[] getAttributes() {
        return attributes;
    }

    public void setAttributes(double[] attributes) {
        this.attributes = attributes;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getClassification() {
        return classification;
    }
}
