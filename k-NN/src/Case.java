public class Case {
    private double[] attributes;
    private String decision;

    public Case(String line) {
        String[] parts = line.split(",");
        this.attributes = new double[parts.length-1];
        for (int i = 0; i < parts.length - 1; i++){
            this.attributes[i] = Double.parseDouble(parts[i]);
        }
        this.decision = parts[parts.length-1];
    }

    public String getDecision() {
        return decision;
    }

    public double[] getAttributes() {
        return attributes;
    }
}
