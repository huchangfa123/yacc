package yacc;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class first_follow_model {

    private String nterminal;
    private Set<String> first;
    private Set<String> follow;
    private ArrayList<first_model> mfirst;
    private ArrayList<String> mfollow;

    public first_follow_model(String nterminal){
        this.mfirst = new ArrayList<first_model>();
        this.mfollow = new ArrayList<String>();
        this.first = new HashSet<String>();
        this.follow = new HashSet<String>();
        this.nterminal = nterminal;
    }

    public String getNterminal() {return this.nterminal;}

    public void add_setmfollow(Set<String> follow) {  this.follow.addAll(follow); }

    public void add_mfollow(String follow){ this.follow.add(follow); }

    public void add_follow(String follow){mfollow.add(follow);}

    public Set<String> getFollow() {return this.follow;}

    public ArrayList<String> getMfollow() {return  this.mfollow;}

    public void add_setmfirst(Set<String> first) {  this.first.addAll(first); }

    public void add_mfirst(String first){ this.first.add(first); }

    public void add_first(first_model first){mfirst.add(first);}

    public Set<String> getFirst() {return  this.first;}

    public ArrayList<first_model> getMfirst() {return  this.mfirst;}
}