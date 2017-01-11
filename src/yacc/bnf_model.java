package yacc;

public class bnf_model {

    private String posture;
    private String nterminal;
    private String handle;

    public bnf_model(String posture,String nterminal,String handle){
        this.posture = posture;
        this.nterminal = nterminal;
        this.handle = handle;
    }

    public void setPosture(String str) { this.posture = str; }
    public void setNterminal(String str) { this.nterminal = str; }
    public void setHandle(String str) { this.handle = str; }

    public String getPosture() {return this.posture;}
    public String getNterminal() {return this.nterminal;}
    public String getHandle() {return this.handle;}
}