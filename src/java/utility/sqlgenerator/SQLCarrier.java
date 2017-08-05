  
package utility.sqlgenerator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Date;
import utility.Carrier;

import utility.QDate;
import utility.*;




public class SQLCarrier {
	public static void insert(CoreEntity core) throws Exception{
		
		core = setIdForInsert(core);
		core = setInsertDateForInsert(core);
		core = setModificationDateForInsert(core);
		String queryStr = SQLGenerator.insertGenerator(core);
//		SQLConnection.execInsertSql(queryStr);
	}
	
	private static CoreEntity setIdForInsert(CoreEntity coreEnt) throws Exception{
		String ent = getNewId();
		Class classes = ent.getClass();
		Method method = coreEnt.getClass().getMethod("setId", classes);
		method.invoke(coreEnt,ent );
		return coreEnt;
	}
	
	private static CoreEntity setInsertDateForInsert(CoreEntity coreEnt) throws Exception{
		String ent =  QDate.getCurrentDate();
		Class classes = ent.getClass();
		Method method = coreEnt.getClass().getMethod("setInsertDate", classes);
		method.invoke(coreEnt,ent );
		return coreEnt;
	}
	

	private static CoreEntity setIdForUpdate(CoreEntity coreEnt) throws Exception{
		String ent = "";
		Class classes = ent.getClass();
		Method method = coreEnt.getClass().getMethod("setId", classes);
		method.invoke(coreEnt,ent );
		return coreEnt;
	}
	
	private static CoreEntity setInsertDateForUpdate(CoreEntity coreEnt) throws Exception{
		String ent =  "";
		Class classes = ent.getClass();
		Method method = coreEnt.getClass().getMethod("setInsertDate", classes);
		method.invoke(coreEnt,ent );
		return coreEnt;
	}
	
	private static CoreEntity setModificationDateForInsert(CoreEntity coreEnt) throws Exception{
		String ent =  QDate.getCurrentDate();
		Class classes = ent.getClass();
		Method method = coreEnt.getClass().getMethod("setModificationDate", classes);
		method.invoke(coreEnt,ent );
		return coreEnt;
	}
	
	private static String 	getNewId() throws Exception{
		Date dt = new Date();
		return dt.getYear()+"."+dt.getMonth()+"."+dt.getDay()+"."+dt.getHours()+"."+dt.getMinutes()+"."+dt.getSeconds();
		  
	}
	
		
	
	public static void update(CoreEntity oldCore, CoreEntity newCore) throws Exception{
		newCore = setModificationDateForInsert(newCore);
		newCore = setIdForUpdate(newCore);
		newCore = setInsertDateForUpdate(newCore);
		//String queryStr = SQLGenerator.updateGenerator(oldCore, newCore);
		//SQLConnection.execUpdateSql(queryStr);
	}
	
	public static void delete(CoreEntity core) throws Exception{
		String queryStr = SQLGenerator.deleteGenerator(core);
		SQLConnection.execDeleteSql(queryStr);
	}
	
//	public static Carrier select(CoreEntity core) throws Exception{
//		String mtSt[] = core.getClass().getName().split("\\.");
//        String tableName =  mtSt[mtSt.length-1].substring("ENTITY".length(),"ENTITY".length()+1).toLowerCase()+mtSt[mtSt.length-1].substring("ENTITY".length()+1);
//     	return select(core,tableName);
//	}
	
	
	
//	public static Carrier select(CoreEntity core, String tableName) throws Exception{
//		String queryStr=SQLGenerator.selectGenerator(core);
////		Carrier qc1 = SQLConnection.execSelectSql(queryStr,tableName);
//		return qc1;
//	}
//	
//	public static Carrier selectByCondition(CoreEntity core, String tableName) throws Exception{
//		String queryStr=SQLGenerator.selectByConditionGenerator(core);
////		Carrier qc1 = SQLConnection.execSelectSql(queryStr,tableName);
//		return qc1;
//	}
//	
//	public static Carrier selectByCondition(CoreEntity core) throws Exception{
//		String mtSt[] = core.getClass().getName().split("\\.");
//        String tableName =  mtSt[mtSt.length-1].substring("ENTITY".length());
//        return selectByCondition(core,tableName);
//	}
//	
//	public static Carrier selectById(String tableName, String sqlId, String[] params) throws Exception{
//		String queryStr = SQLGenerator.selectByIdGenerator(sqlId, params);
//		Carrier qc = SQLConnection.execSelectSql(queryStr,tableName);
//		return qc;
//	}
	
}
