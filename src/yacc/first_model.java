package yacc;


import java.util.HashSet;
import java.util.Set;

    
class first_model {
    private String flag;
    private Set<String> first = null;

    public first_model(String flag){
        this.flag = flag;
        this.first = new HashSet<String>();
    }

    public void addfirst(String first) { this.first.add(first);}

    public void addSetfirst(Set<String> first) { this.first.addAll(first); }

    public Set<String> getfirst () {return  this.first; }

    public String getflag() { return  this.flag; }
}