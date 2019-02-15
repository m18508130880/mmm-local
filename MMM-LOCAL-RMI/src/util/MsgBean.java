package util;
import java.io.Serializable;

/** ��װ�˲�ѯ���ݿ�ķ���ֵ��һ��������ȥ, �ͷ�װ�˷��ؽ������ ��MagBean ,
 *  MsgBean implements Serializable
 * @author Cui
 * ��Ϣbean
 */
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
	
	public static final int CONST_PAGE_SIZE				    = 25;  
	 
	//====================ϵͳ״̬=================================	
	public static final int STA_SUCCESS						= 0000;	//�ɹ�
	public static final int STA_DEAL_SUBMIT_SUCCESS			= 3000;	//�ύ�ɹ�
	public static final int STA_NET_ERROR					= 9993;	//�������
	public static final int STA_FAILED						= 9999;	//ʧ��	
	public static final int STA_ACCOUNT_NOT_EXIST			= 1001; //�û���������
	public static final int STA_ACCOUNT_PWD_ERROR			= 1002;	//�������
	public static final int STA_CHECK_CODE_ERROR			= 1003;	//��֤�����
	public static final int STA_ACCOUNT_OTP_ERROR			= 1004;	//��̬�������

	
	/**���ݴ���� int pStatus ��ȡ��Ӧ������״̬����    
	 * @param pStatus
	 *      һ����λ����  ��ʾ  ״̬  0000:�ɹ� , 3000:�ύ�ɹ� , 9999:ʧ�� �ȵ�.
	 * @return һ�������ַ���: ϵͳ״̬����
	 */
	public static String GetResult(int pStatus)
	{
		String RetVal = "";
		switch(pStatus)
		{
			//ϵͳ״̬	
			case STA_SUCCESS: 
			 	RetVal = "�ɹ�";
			 	break;
			case STA_FAILED: 
			 	RetVal = "ʧ��";	
			 	break;
			case STA_ACCOUNT_NOT_EXIST:
				RetVal = "�û���������";
				break;
			case STA_ACCOUNT_PWD_ERROR:
				RetVal = "�������";
				break;				
			case STA_CHECK_CODE_ERROR:
				RetVal = "��֤�����";
			    break;
			case STA_ACCOUNT_OTP_ERROR:
				RetVal = "��̬�������";
				break;
			case STA_DEAL_SUBMIT_SUCCESS:
				RetVal = "�ύ�ɹ�";
				break;
			default:
				RetVal = "ϵͳ����";
				break;
		}
		return RetVal;
	}
	

	/**���ݴ���� int pStatus ��ȡ��Ӧ������״̬����    
	 * @param pStatus
	 *      һ����λ����  ��ʾ  ״̬  0000:�ɹ� , 3000:�ύ�ɹ� , 9999:ʧ�� �ȵ�.
	 * @return һ�������ַ���: ϵͳ״̬����
	 */
	public static String GetNetResult(String pStatus)
	{
		String RetVal = "";
		switch(Integer.parseInt(pStatus))
		{
			//ϵͳ״̬	
			case STA_SUCCESS: 
			 	RetVal = "�ɹ�";
			 	break;
			case STA_FAILED: 
			 	RetVal = "ʧ��";	
			 	break;
			case STA_ACCOUNT_NOT_EXIST:
				RetVal = "�û���������";
				break;
			case STA_ACCOUNT_PWD_ERROR:
				RetVal = "�������";
				break;				
			case STA_CHECK_CODE_ERROR:
				RetVal = "��֤�����";
			    break;
			case STA_ACCOUNT_OTP_ERROR:
				RetVal = "��̬�������";
				break;
			default:
				RetVal = "ϵͳ����";
				break;
		}
		return RetVal;
	}
	
	private int status = STA_SUCCESS;
	private Object msg;
	private int count = 0;	
	
	/**
	 * �ղι�����
	 */
	public MsgBean() {
		
	}
	
	/** �������������Ĺ�����
	 * @param pStatus
	 *    һ����λ����  ��ʾ  ״̬  0000:�ɹ� , 3000:�ύ�ɹ� , 9999:ʧ�� �ȵ�.
	 * @param pMsg
	 *    һ��Object ���� 
	 * @param pCount
	 *    һ����¼��ֵ(���ݿ�????)
	 */
	public MsgBean(int pStatus, Object pMsg, int pCount) {
		status = pStatus;
		msg    = pMsg;
		count  = pCount;
	}
	
	
	public Object getMsg() {
		return msg;
	}
	
	public int getStatus() {
		return status;
	}
	
	public int getCount() {
		return count;
	}
}