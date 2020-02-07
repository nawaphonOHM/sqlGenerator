import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;;

public class Main {
    private static String generateSQLTemplate(Map<String, Object > parameters){
		
		Map<String, Object> mappedParameters = null;
		Entry<String, String> keyValue = null;
		boolean addSpecialChar = false;

		final Queue<JoinDescription> joinTables;
		final StringBuilder sqlTemplate       = new StringBuilder();
		final String AS_TOKEN                 = "AS";
		final String QUOTE_TOKEN              = "\"";
		final String SPACE_TOKEN              = " ";
		final String SELECT_TOKEN             = "SELECT";
		final String FROM_TOKEN               = "FROM";
		final String JOIN_TOKEN               = "JOIN";
		final String COMMA                    = ",";
		final String ON_TOKEN                 = "ON";
		final String AND_TOKEN                = "AND";
		final String EQUAL_TOKEN              = "=";
		final String MORE_THAN_TOKEN          = ">";
		final String LESS_THAN_TOKEN          = "<";
		final String MORE_THAN_OR_EQUAL_TOKEN = MORE_THAN_TOKEN + EQUAL_TOKEN;
		final String LESS_THAN_OR_EQUAL_TOKEN = LESS_THAN_TOKEN + EQUAL_TOKEN;

		if( !parameters.containsKey(SELECT_TOKEN) ){ throw new IllegalArgumentException("There is no SELECT statement."); }
		if(parameters.get(SELECT_TOKEN) == null){ throw new IllegalArgumentException("SELECT parameter is void."); }
		if( !(parameters.get(SELECT_TOKEN) instanceof Map<?, ?>) ){ throw new IllegalArgumentException("Prefer Map."); }

		mappedParameters = (Map<String, Object>)parameters.get(SELECT_TOKEN);
		sqlTemplate.append(SELECT_TOKEN + SPACE_TOKEN);

		for(Entry<String, Object> mappedParameter : mappedParameters.entrySet()){
			if(addSpecialChar){ sqlTemplate.append(COMMA); }
			sqlTemplate.append(
				mappedParameter.getKey()   + 
				SPACE_TOKEN                + 
				AS_TOKEN                   + 
				SPACE_TOKEN                +
				QUOTE_TOKEN                + 
				mappedParameter.getValue() + 
				QUOTE_TOKEN
			);
			addSpecialChar |= true;
		}
		
		addSpecialChar &= false;
		if(!parameters.containsKey(FROM_TOKEN)){ 
			throw new IllegalArgumentException("There is no FROM statement."); 
		}
		if(parameters.get(FROM_TOKEN) == null){ 
			throw new NullPointerException("The value of from is null."); 
		}
		if(!(parameters.get(FROM_TOKEN) instanceof Map<?, ?>)){ 
			throw new IllegalArgumentException("A From prefers Map."); 
		}


		keyValue = ((Map<String, String>)parameters.get(FROM_TOKEN))
					.entrySet()
					.iterator()
					.next();
		
		sqlTemplate.append(SPACE_TOKEN + FROM_TOKEN + SPACE_TOKEN);
		sqlTemplate.append(
			keyValue.getKey()   + 
			SPACE_TOKEN         + 
			AS_TOKEN            + 
			SPACE_TOKEN         + 
			keyValue.getValue()
		);

		if(parameters.containsKey(JOIN_TOKEN)){
			if( !(parameters.get(JOIN_TOKEN) instanceof Queue<?>) ){ 
				throw new IllegalArgumentException("A join condition prefers Queue."); 
			}
			sqlTemplate.append(SPACE_TOKEN);
			joinTables = (Queue<JoinDescription>)parameters.get(JOIN_TOKEN);

			for(JoinDescription joinObj : joinTables){
				if(addSpecialChar){
					sqlTemplate.append(SPACE_TOKEN + AND_TOKEN + SPACE_TOKEN);
				}

				keyValue = joinObj.getParameter();
				sqlTemplate.append(
						joinObj.getJoinType().name() + 
						SPACE_TOKEN                  +
						JOIN_TOKEN                   +
						SPACE_TOKEN                  + 
						joinObj.getName()            + 
						ON_TOKEN                     + 
						keyValue.getKey()            + 
						EQUAL_TOKEN                  + 
						keyValue.getValue()
					);
				addSpecialChar |= true;
			}
		}

		return sqlTemplate.toString();
	}

	public static void main(String... args){
		Map<String, Object > parameters = new HashMap<String, Object >();
		Map<String, String> selectTables = new HashMap<String, String>();
		Map<String, String> fromTables = new HashMap<String, String>();

		selectTables.put("invoice.INVOICE_ID", "transectionID");
		selectTables.put("item.AMOUNT", "amount");
		parameters.put("SELECT", selectTables);

		
		parameters.put("FROM", selectTables);


		System.out.println(generateSQLTemplate(parameters));
	}
}