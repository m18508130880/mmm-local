package util;

import java.io.Serializable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class CurrStatus implements Serializable
{
	private static final long serialVersionUID = 100;
  	private String CheckCode = "";    //��֤��
	private int Cmd;                  //��ѯ����
	private int CurrPage;             //��ǰҳ��
	private int TotalRecord;          //�ܼ�¼��
	private int TotalPages;           //��ҳ��
	private int Func_Id;         
	
	private String Func_Sort_Id;      //����                1�����롢2���ܾ���3������
	private String Func_Project_Id;   //��ĿID      
	private String Func_Type_Id;      //�ܾ�/����       1������    2����  
	private String Func_Sub_Type_Id;  //��/��              1���ꡢ    2����
	
	private Vector<Object> VecDate;
	private String Result = null;    //
	private int Curr_Status;         //��ǰ״̬
	private String Jsp;              //����jsp��ַ

	
	public String getFunc_Sort_Id() {
		return Func_Sort_Id;
	}
	public void setFunc_Sort_Id(String func_Sort_Id) {
		Func_Sort_Id = func_Sort_Id;
	}
	public String getFunc_Project_Id() {
		return Func_Project_Id;
	}
	public void setFunc_Project_Id(String func_Project_Id) {
		Func_Project_Id = func_Project_Id;
	}
	
	public String getFunc_Type_Id() {
		return Func_Type_Id;
	}
	public void setFunc_Type_Id(String func_Type_Id) {
		Func_Type_Id = func_Type_Id;
	}
	public String getFunc_Sub_Type_Id() {
		return Func_Sub_Type_Id;
	}
	public void setFunc_Sub_Type_Id(String func_Sub_Type_Id) {
		Func_Sub_Type_Id = func_Sub_Type_Id;
	}
	public String getCheckCode() {
		return CheckCode;
	}
	public void setCheckCode(String checkCode) {
		CheckCode = checkCode;
	}
	public int getCmd() {
		return Cmd;
	}
	public void setCmd(int cmd) {
		Cmd = cmd;
	}
	public int getCurrPage() {
		return CurrPage;
	}
	public void setCurrPage(int currPage) {
		CurrPage = currPage;
	}
	public int getTotalRecord() {
		return TotalRecord;
	}
	
	/** �����ܼ�¼��������ҳ��
	 * @param totalRecord
	 */
	public void setTotalRecord(int totalRecord) {
		TotalRecord = totalRecord;
		TotalPages = (totalRecord + MsgBean.CONST_PAGE_SIZE - 1)/MsgBean.CONST_PAGE_SIZE;
	}
	
	public int getTotalPages() {
		return TotalPages;
	}
	
	public void setTotalPages(int totalPages) {
		TotalPages = totalPages;
	}
	
	public int getFunc_Id() {
		return Func_Id;
	}
	
	public void setFunc_Id(int func_Id) {
		Func_Id = func_Id;
	}
	
	public Vector<Object> getVecDate() {
		return VecDate;
	}
	public void setVecDate(Vector<Object> vecDate) {
		VecDate = vecDate;
	}
	public String getResult() {
		return Result;
	}
	public void setResult(String result) {
		Result = result;
	}
	public int getCurr_Status() {
		return Curr_Status;
	}
	
	public void setCurr_Status(int curr_Status) {
		Curr_Status = curr_Status;
	}
	
	public String getJsp() {
		return Jsp;
	}
	public void setJsp(String jsp) {
		Jsp = jsp;
	}
	
	/** ��ȡrequest�� Cmd,CurrPage,VecDate������ ��װ�� CurrStatus�����е�ҳ������
	 * @param request
	 * @param pFromZone
	 *     ��һ��booleanֵ, true��ʾ: ������ҳ�� �� ��ǰҳ�� 
	 */
	public void getHtmlData(HttpServletRequest request, boolean pFromZone) {	
		try
		{
			if(pFromZone)
			{
				CurrPage = 1;
			}
			else
			{
				Cmd               = CommUtil.StrToInt(request.getParameter("Cmd"));
				CurrPage          = CommUtil.StrToInt(request.getParameter("CurrPage"));
				CurrPage          = CurrPage==0?1:CurrPage;			
				VecDate           = CommUtil.getDate(request.getParameter("BTime"),request.getParameter("ETime"));
				//INT
				Func_Id           = CommUtil.StrToInt(request.getParameter("Func_Id"));
				//String
				Func_Sort_Id      = CommUtil.StrToGB2312(request.getParameter("Func_Sort_Id"));
				Func_Project_Id   = CommUtil.StrToGB2312(request.getParameter("Func_Project_Id"));	
				Func_Type_Id      = CommUtil.StrToGB2312(request.getParameter("Func_Type_Id"));
				Func_Sub_Type_Id  = CommUtil.StrToGB2312(request.getParameter("Func_Sub_Type_Id"));			
				
			}
		}catch(Exception Ex)
		{
			Ex.printStackTrace();
		}
	}
	
	
	/** ��ȡ��ҳ��: ��ǰҳ/��ҳ,��ҳ,��һҳ,��һҳ , ����ҳ
	 * @param pForm �ַ���  ��ʾ: JSPҳ��ı� �� nameֵ
	 * @return ���� HTML�ַ��� ���������� 
	 */
	public String GetPageHtml(String pForm) 
	{
		String s = "";
		int TotalPages;		
		TotalPages = (TotalRecord + MsgBean.CONST_PAGE_SIZE -1)/MsgBean.CONST_PAGE_SIZE;    //�����ҳ��
		if(0 == TotalRecord)
		{
			CurrPage = 0;    //��ҳ��Ϊ0,��ǰҳ��Ϊ0;
		}
		s += "ҳ����<strong>" + CurrPage + "</strong>/<strong>" + TotalPages + "</strong><span>[��<b>" + TotalRecord +"</b>����¼]</span>";
		s += "<a href=# onclick='GoPage(1)'>&nbsp;��ҳ</a> ";		
		s += "<a href=# onclick='GoPage(" + (CurrPage - 1) +")'>&nbsp;��һҳ</a> ";	 	
		
		int beginPage = CurrPage - 4 < 1 ? 1 : CurrPage - 4;    // CurrPage >= 5 ʱ beginPage Ϊ  CurrPage - 4 
		if(beginPage <= TotalPages) 
		{
			for(int i=beginPage, j=0; i<=TotalPages&&j<6; i++,j++)
			{
				if(i == CurrPage)
				   s+="<a href=# onclick='GoPage("+i+")'><strong>"+i+"</strong></a> ";
				else
		           s+="<a href=# onclick='GoPage("+i+")'>"+i+"</a> ";
			}
		}
		if(CurrPage == TotalPages)
		{
			s+="<a >&nbsp;��һҳ</a>";		
			s+="<a >&nbsp;ĩҳ</a> ";
		}
		else
		{
			s+="<a href=# onclick='GoPage("+(CurrPage+1)+")'>&nbsp;��һҳ</a> ";
			s+="<a href=# onclick='GoPage("+TotalPages+")'>&nbsp;ĩҳ</a> ";
			
		}
		s+="����<input name='ToPage' type='text' size='2'>ҳ";
		s+="<input type='button' style='width:40px;height:20px' onClick='GoPage(" + pForm + ".ToPage.value)' value='ȷ��'/>";
		return s; 
	}
}
