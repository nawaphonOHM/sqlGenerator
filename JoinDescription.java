import java.util.Map.Entry;

final public class JoinDescription {
    private final JoinEnum joinType;
    private final String name;
    private final Entry<String, String> parameter;

    public JoinDescription(JoinEnum joinType, Entry<String, String> parameter, String name){
        this.joinType = joinType;
        this.parameter = parameter;
        this.name = name;
    }

    public JoinEnum getJoinType(){
        return this.joinType;
    }
    public Entry<String, String> getParameter(){
        return this.parameter;
    }
    public String getName(){
        return this.name;
    }

}