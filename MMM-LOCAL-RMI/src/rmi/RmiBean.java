package rmi;

import java.io.Serializable;
import java.sql.ResultSet;

import util.CurrStatus;
import util.MsgBean;


/**RmiBean ʵ���� serializable �����л��ӿ�
 * @author Cui
 * bean������඼�̳� RmiBean
 * ΪʲôҪ�����л�?
 *     ���Ҫͨ��Զ�̵ķ������ã�RMI��ȥ����һ��Զ�̶���ķ��������ڼ����A�е���
 *     ��һ̨�����B�Ķ���ķ�������ô����Ҫͨ��JNDI�����ȡ�����BĿ���������ã�
 *     �������B���͵�A������Ҫʵ�����л��ӿ�
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
