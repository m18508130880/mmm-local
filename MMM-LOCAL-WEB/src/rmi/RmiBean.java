package rmi;

import java.io.Serializable;
import java.sql.ResultSet;

import util.CurrStatus;
import util.MsgBean;


/**RmiBean 实现了 serializable 可序列化接口
 * @author Cui
 * bean包里的类都继承 RmiBean
 * 为什么要可序列化?
 *     如果要通过远程的方法调用（RMI）去调用一个远程对象的方法，如在计算机A中调用
 *     另一台计算机B的对象的方法，那么你需要通过JNDI服务获取计算机B目标对象的引用，
 *     将对象从B传送到A，就需要实现序列化接口
 */
public abstract class RmiBean implements Serializable
{
	public final static String UPLOAD_PATH = "/www/MMM/MMM-LOCAL-WEB/files/excel/";
	
	public static final int	RMI_CORP_INFO						= 1;
	public static final int	RMI_PROJECT_INFO					= 2;
	public static final int	RMI_USER_INFO						= 3;
	public static final int	RMI_USER_ROLE						= 4;
	
	public static final int	RMI_FACTORY_INFO					= 5;
	public static final int	RMI_TEXTURE_INFO					= 6;
	public static final int	RMI_TYPE_INFO						= 7;
	public static final int	RMI_ON_HAND							= 8;
	public static final int	RMI_ON_INFO							= 9;

	public MsgBean    msgBean = null;
	public String     className;
	public CurrStatus currStatus = null;
	
	public RmiBean()
	{
		msgBean = new MsgBean(); 		
	}
	
	public String getClassName()
	{
		return className;
	}
	
	public abstract long getClassId();
	public abstract String getSql(int pCmd);
	public abstract boolean getData(ResultSet pRs);
}
