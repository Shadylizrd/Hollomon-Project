package testing;

import java.util.Objects;

class Doctor implements Comparable<Doctor> {
    private String name;
    private String specialism;
    
    public Doctor(String name, String specialism) {
        this.name = name;
        this.specialism = specialism;
    }
    
    @Override
    public int compareTo(Doctor d) {
    // alphabetcially...?
    // order by specialism then name
    // 1. Ear, Nose and Throat
    // 2. Orthopedics
    int specCompared = this.specialism.compareTo(d.specialism);
    if (specCompared == 0) { return this.name.compareTo(d.name); }
    return specCompared;
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || other.getClass() != getClass()) return false;
        Doctor o = (Doctor) other;
        return Objects.equals(name, o.name)
          && Objects.equals(specialism, o.specialism);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, specialism);
    }
    
    @Override
    public String toString() {
        return name + "(" + specialism + ")";
    }

    public static void main(String[] args) {
        Doctor muneka = new Doctor("Muneka", "Ear, Nose, and Throat");
        Doctor muneka2 = new Doctor("Muneka", "Ear, Nose, and Throat");
        Doctor summers = new Doctor("Summers", "Orthopaedics");

        System.out.println(muneka.compareTo(summers) < 0);
        System.out.println(muneka.compareTo(muneka2) < 0);
        System.out.println(muneka.equals(muneka2));
    }
}