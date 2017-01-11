package yacc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by huchangfa on 2016/12/23.
 */
public class yacc {
    public static ArrayList<bnf_model> data_array = new ArrayList<bnf_model>();
    public static ArrayList<first_follow_model> position_array = new ArrayList<first_follow_model>();
    public static ArrayList<first_model> first_list = new ArrayList<first_model>();
    public static table_model table = new table_model();
    /*
        读取bnf文件
     */
    public static void readfile(String path){
        try {
            InputStream is = new FileInputStream(path + "\\input.bnf");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine().trim();
            //System.out.println(line);
            while (line != null) {
                int flag = 0;
                String[] result = line.split(" ::= ");
                bnf_model data = new bnf_model(line,result[0].trim(),result[1].trim());
                for(int i=0; i<data_array.size();i++)
                {
                    if(data_array.get(i).getNterminal().equals(result[0]))
                    {
                        data_array.get(i).setHandle(data_array.get(i).getHandle().trim() + " | "+ result[1]);
                        flag = 1;                      
                    }
                }
                if(flag == 0)
                    data_array.add(data);
                line = reader.readLine().trim();             
                //System.out.println(line);
            }
        }catch(Exception e){
            //System.out.println(e);
        }
    }

    /*
        判断终结符，非终结符
     */
    public static boolean judge_terminal(String str){
        for (int i=0; i<data_array.size(); i++){
            if(data_array.get(i).getNterminal().equals(str))
                return false;
        }
        return true;
    }

    /*
        获取非终结符的位置
    */
    public static int get_terminal_position(String str){
   
        int i;
        int position = 0;
        for(i = 0 ; i <position_array.size(); i++){
            if(position_array.get(i).getNterminal().equals(str)){
                position = i;
                break;
            }
        }
        return position;
    }


    /*
        创建first列表
     */
    public static void create_firstlist(){
        int time =0;
        first_follow_model data;
        int count = -1;
        int temp = 0;
        while(count != temp)
        {
            count = temp;
            for (int i=0; i<data_array.size(); i++){
                //第一次新建一个列表
                if(time == 0)
                    data = new first_follow_model(data_array.get(i).getNterminal());
                else
                    data = position_array.get(i);
                //非终结符句柄
                String handle = data_array.get(i).getHandle().trim();
                //拆句柄
                String[] items = handle.split("\\|");
                for (String item : items) {
                    //System.out.println(item);
                    //拆项
                    item = item.trim();
                    String[] keys = item.split(" ");
                    for(String key : keys) {
                        int flag = 1;
                        //是终结符，first直接加上，跳出循环
                        if(judge_terminal(key)){
                            //System.out.println(data.getFirst().contains(key));
                            if(!data.getFirst().contains(key)){
                                data.add_mfirst(key);
                                temp += 1;
                            }
                            break;
                        }
                        //不是终结符，把非终结符的first加上，看是否含空判断是否继续循环
                        else{
                            if(time != 0){
                                int num = get_terminal_position(key);
                                if(!data.getFirst().containsAll(position_array.get(num).getFirst())){
                                    //??????
                                    data.add_setmfirst(position_array.get(num).getFirst());
                                    //System.out.println("2" + data.getFirst());
                                    temp += 1;
                                }
                            }
                            for(String string : data.getFirst()){
                                if(string.equals("\"\"")){
                                    flag = 0;
                                    break;
                                }
                            }
                        }
                        if(flag == 1) break;
                    }
                }
                if(time == 0)
                    position_array.add(data);
                else
                    position_array.set(i,data);
            }
            time = 2;
        }
        first_model first;
        //初始化firstlist
        for (int i = 0; i<data_array.size(); i++){
            first = new first_model(data_array.get(i).getNterminal());
            first.addSetfirst(position_array.get(i).getFirst());
            first_list.add(first);

        }
    }

    /*
        创建follow列表
    */
    public static void create_followlist(){
        position_array.get(0).add_mfollow("$");
        //int time = 0;
        int count = -1;
        int temp = 0;
        while(count!=temp){
            count = temp;
            for(int i=0; i<data_array.size(); i++){
                //非终结符句柄
                String handle = data_array.get(i).getHandle().trim();
                //拆句柄
                String[] items = handle.split("\\|");
                for (String item : items) {
                    //拆项
                    item = item.trim();
                    String[] keys = item.split(" ");
                    //第一个条件
                    for (int j = 0; j < keys.length-1; j++) {
                        //当前位置是不是终结符
                        if(!judge_terminal(keys[j])){
                            //下一位是不是终结符
                            int front = get_terminal_position(keys[j]);
                            int next = get_terminal_position(keys[j+1]);
                            if(!judge_terminal(keys[j+1])){
                                //不是，下一位的除了空的所有first加入到前一位终结符的follow中
                                Set<String> follow = new HashSet<String>();
                                for (String first_key : position_array.get(next).getFirst())
                                    if(!first_key.equals("\"\""))
                                        follow.add(first_key);
                                if(!position_array.get(front).getFollow().containsAll(follow)){
                                    for(String key : follow){
                                        if (!key.equals("\"\""))
                                            position_array.get(front).add_mfollow(key);
                                    }
                                    temp += 1;
                                }
                            }
                            //是则直接加
                            else{
                                if(!position_array.get(front).getFollow().contains(keys[j+1])){
                                    position_array.get(front).add_mfollow(keys[j+1]);
                                    temp += 1;
                                }
                            }
                        }
                    }
                    //第二个条件
                    for(int k = keys.length-1; k >= 0 ; k--){
                        int tem = 1;//判断是否继续的标志
                        if (!judge_terminal(keys[k])){
                            //该非终结符的列表位置
                            int num = get_terminal_position(keys[k]);
                            //把左边的follow加入到该非终结符的follow

                            if(!position_array.get(num).getFollow().containsAll(position_array.get(i).getFollow())){
                                position_array.get(num).add_setmfollow(position_array.get(i).getFollow());
                                temp += 1;
                            }
                            //判断该非终结符的first是否含空
                            for (String first : position_array.get(num).getFirst()){
                                if (first.equals("\"\"")){
                                    tem = 0;
                                    break;
                                }
                            }
                            if (tem == 0) continue;
                            else break;
                        }
                        else
                        {
                            break;
                        }
                    }
                }
            }
        }
    }

    /*
        判断LL（1）
    */
    public static boolean judge_LL1(){
        //条件一 任意两个 first（a）与 first （B）不相交
        for(int i=0; i<data_array.size(); i++) {
            //非终结符句柄
            String handle = data_array.get(i).getHandle().trim();
            //拆句柄
            String[] items = handle.split("\\|");
            //初始化 每项的first列表
            ArrayList<first_model> first_lists = new ArrayList<first_model>();

            for (String item : items) {
                //拆项
                item = item.trim();
                //每项的first集合为option
                first_model option = new first_model(item);
                //拆个
                String[] keys = item.split(" ");
                for (int j = 0; j < keys.length; j++) {
                    if(judge_terminal(keys[j])){
                        option.addfirst(keys[j]);
                        break;
                    }
                    else{
                        int num = get_terminal_position(keys[j]);
                        //获取非终结符的first集合
                        Set<String> result = position_array.get(num).getFirst();
                        option.addSetfirst(result);
                        //含有空，继续走下一个
                        if (result.contains("\"\"")){
                            continue;
                        }
                    }
                }
                //条件2：first中含空，其它的first与左边的非终结符没有交集
                for (int k = 0 ;k < first_lists.size(); k++){
                    for(String every_first : option.getfirst()){
                        if (first_lists.get(k).getfirst().contains(every_first))
                            return false;
                        if (every_first.equals("\"\"")){
                            for (int l = 0; l < first_lists.size(); l++){
                                for (String every_follow : position_array.get(i).getFollow()){
                                    if (first_lists.get(l).getfirst().contains(every_follow))
                                        return false;
                                }
                            }
                        }
                    }
                }
                first_lists.add(option);
            }
        }
        return true;
    }

    public static int get_sign_position(String key){
        for (int i = 0; i < table.get_input_sign().size() ; i++)
        {
            if (key.equals(table.get_input_sign().get(i))){
                return i;
            }
        }
        return -1;
    }

    public static void create_table(){
        //初始化列表的左和上
        for (int i=0; i<data_array.size(); i++){
            table.add_nterminal(data_array.get(i).getNterminal());
            String handle = data_array.get(i).getHandle().trim();
            //拆句柄
            String[] items = handle.split("\\|");
            for (String item : items) {
                //拆项
                item = item.trim();
                //拆个
                String[] keys = item.split(" ");
                for (int j = 0; j < keys.length; j++) {
                    if (judge_terminal(keys[j]) && !table.get_input_sign().contains(keys[j]) && !keys[j].equals("\"\"")){
                        table.add_input_sign(keys[j]);
                    }
                }
            }
        }
        table.add_input_sign("$");

        for(int i=0; i<data_array.size(); i++) {
            //非终结符句柄
            String handle = data_array.get(i).getHandle().trim();
            //拆句柄
            String[] items = handle.split("\\|");
            //初始化每个非终结符的resultlist
            ArrayList<String> results = new ArrayList<String>();
            int size = table.get_input_sign().size();
            for(int m = 0;m < size;m++ ){
                results.add("error");
            }
            for (String item : items) {
                //每个项的first集合为option
                first_model option = new first_model(handle);
                //拆项
                item = item.trim();
                //拆个
                String[] keys = item.split(" ");
                //第一个情况
                for (int j = 0; j < keys.length; j++) {
                    if (judge_terminal(keys[j])) {
                        option.addfirst(keys[j]);
                        break;
                    } else {
                        int num = get_terminal_position(keys[j]);
                        //获取非终结符的first集合
                        Set<String> result = position_array.get(num).getFirst();
                        option.addSetfirst(result);
                        //含有空，继续走下一个
                        if (result.contains("\"\"")) {
                            continue;
                        }
                        else break;
                    }
                }
                //第二种情况
                for (String every_first : option.getfirst()){
                    if (every_first.equals("\"\"")){
                        option.addSetfirst(position_array.get(i).getFollow());
                        break;
                    }
                }

                for (String every_key : option.getfirst()){
                    if(!every_key.equals("\"\"")){
                        int location = get_sign_position(every_key);
                        results.set(location,data_array.get(i).getNterminal().trim() + "->" + item.trim());
                    }
                }
            }
            table.add_result(results);
        }
    }

    public static int get_nterminal_position_from_table(String key) {
        for (int i = 0; i < table.get_nterminal().size(); i++) {
            if (table.get_nterminal().get(i).equals(key))
                return  i;
        }
        return -1;
    }

    public static int get_sign_position_from_table(String key) {
        for (int i = 0; i < table.get_input_sign().size(); i++) {
            if (table.get_input_sign().get(i).equals(key))
                return  i;
        }
        return -1;
    }

    public static boolean judge_input(String option){
        option = option + "$";
        Stack<String> save_data = new Stack<String>();
        save_data.push("$");
        String X = position_array.get(0).getNterminal();
        save_data.push(X);
        String[] keys = option.split(" ");
        int flag = 0;
        int x = 0;
        int y = 0;
        while(!X.equals("$")){
                if(!judge_terminal(X)){
                    x = get_nterminal_position_from_table(X);
                    y = get_sign_position_from_table(keys[flag]);  
                    if(y == -1) return false;
                }            
                if(keys[flag].equals(X) && flag < keys.length){           
                    save_data.pop();                    
                    flag += 1;
                }
                else if (judge_terminal(X)) {return  false;}
                else if (table.get_result().get(x).get(y).equals("error")) {return false;}
                else if (!table.get_result().get(x).get(y).equals("error")){
                    //System.out.println(table.get_result().get(x).get(y));
                    save_data.pop();
                    String[] items = table.get_result().get(x).get(y).split("->");
                    String[] individuals = items[1].split(" ");
                    for (int z = individuals.length-1; z >= 0; z--){
                        if(!individuals[z].equals("\"\""))
                            save_data.push(individuals[z]);
                    }   
                }
                if(!save_data.isEmpty())
                    X = save_data.peek();
                if(save_data.peek().equals("$") && !keys[flag].equals("$")){
                        return false;
                }
        }
        return true;
    }
    
    public static void read_tok(String path){
        for(int i=1 ; i<=10; i++){
            String result = new String();
            try {           
                InputStream is = new FileInputStream(path + "\\tokenstream" + i + ".tok");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine().trim();
                //System.out.println(line);
                while (line != null) {
                    result = result + line + " ";                  
                    line = reader.readLine().trim();
                //System.out.println(line);
                }                      
            }catch(Exception e){
                //System.out.println(e);
            }
            System.out.println("第"+ i +"个测试用例为:" + judge_input(result));
        }
    }
    
    public static void main(String[] args) throws Exception{
        //System.out.println(args[0]);
        readfile(args[0]);
        create_firstlist();
        create_followlist(); 
        System.out.println("是否为LL(1):" + judge_LL1());
        if(judge_LL1()){
             create_table();        
             read_tok(args[0]);
        }
        else{
            System.out.println("下一个测试用例");
        }
    }
    
}




