package util;
import java.io.Serializable;

public class MsgBean implements Serializable 
{
	public final static long serialVersionUID = 10;
	public static String ClassName = "MsgBean";
	public long getClassId()
	{
		return serialVersionUID;
	}
	public String getClassName()
	{
		return ClassName;
	}
	public static final int CONST_PAGE_SIZE				= 20;
	
//	====================ϵͳ״̬	
	public static final int STA_SUCCESS						= 0000;	//�ɹ�	
	public static final int STA_DB_NO_DATA					= 0002; //���ݿ����
	public static final int STA_DB_ERROR					= 0003; //���ݿ����
	public static final int STA_SUBMIT_SUCCESS				= 1000;	//�ύ�ɹ�
	
	public static final int STA_FAILED						= 3006;	//ʧ��
//====================����	
	public static final int STA_MD5_CODE_ERROR				= 6001;	//У�������	
	public static final int STA_OLD_PASSWORD_ERROR			= 6002;	//ԭ�������		
	public static final int STA_NET_ERROR					= 6003;	//�������	
	public static final int STA_UNKHOWN_ERROR				= 9999;	//��δ֪�������
	
	public static String GetErrorMsg(String strCode)
	{
		String RetVal = "";
		int Code = Integer.parseInt(strCode);
		switch(Code)
		{
			case STA_SUBMIT_SUCCESS:
				RetVal = "�ύ�ɹ�";
				break;
			case STA_SUCCESS:
				RetVal = "�ɹ�";
				break;
			case STA_FAILED:
				RetVal = "ʧ��";
				break;
			case STA_MD5_CODE_ERROR:
				RetVal = "У�������";
				break;
			case STA_OLD_PASSWORD_ERROR:
				RetVal = "ԭ�������"; 
				break;
			case STA_NET_ERROR:
				RetVal = "�������";
				break;		
			case STA_DB_ERROR:
				RetVal = "���ݿ��������";
                break;
			case STA_UNKHOWN_ERROR:
			default:
				RetVal = "δ֪����";
				break;
		}
		return RetVal;
	}
	
	private int status = STA_SUCCESS;
	private Object msg;
	private int count = 0;	
	
	public MsgBean(int pStatus, Object pMsg) 
	{
		status = pStatus;
		msg = pMsg;
	}
	public MsgBean() 
	{
	}
	public Object getMsg() 
	{
		return msg;
	}
	public int getStatus() {
		return status;
	}
	public int getCount() {
		return count;
	}
}