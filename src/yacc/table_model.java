package yacc;


import java.util.ArrayList;

public class table_model {

    private ArrayList<String> input_sign;
    private ArrayList<String> nterminal;
    private ArrayList<ArrayList<String>> result;

    public table_model(){
        this.input_sign = new ArrayList<String>();
        this.nterminal = new ArrayList<String>();
        this.result = new ArrayList<ArrayList<String>>();
    }

    public void add_input_sign(String sign) { this.input_sign.add(sign) ;}

    public void add_nterminal(String terminal) { this.nterminal.add(terminal) ;}

    public void add_result(ArrayList<String> result) { this.result.add(result) ;}

    public ArrayList<String> get_input_sign() { return this.input_sign;}

    public ArrayList<String> get_nterminal() { return this.nterminal;}

    public ArrayList<ArrayList<String>> get_result() { return this.result;}
}