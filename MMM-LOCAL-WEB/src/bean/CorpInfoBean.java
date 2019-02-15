package bean;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.CurrStatus;
import util.MsgBean;

public class CorpInfoBean extends RmiBean 
{	
	public final static long serialVersionUID = RmiBean.RMI_CORP_INFO;
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public CorpInfoBean() 
	{ 
		super.className = "CorpInfoBean";
	}
	
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0, 25);
		switch(currStatus.getCmd())
		{
			case 10://添加
			case 11://修改
				currStatus.setResult(MsgBean.GetResult(msgBean.getStatus()));
				msgBean = pRmi.RmiExec(0, this, 0, 25);
		    case 0://查询
		    	if(null != msgBean.getMsg() && ((ArrayList<?>)msgBean.getMsg()).size() > 0)
				{
					request.getSession().setAttribute("Corp_Info_" + Sid, (CorpInfoBean)((ArrayList<?>)msgBean.getMsg()).get(0));
				}
		    	currStatus.setJsp("Corp_Info.jsp?Sid=" + Sid);
		    	break;
		    case 1://查询
		    	if(null != msgBean.getMsg() && ((ArrayList<?>)msgBean.getMsg()).size() > 0)
				{
					request.getSession().setAttribute("User_Announce_" + Sid, (CorpInfoBean)((ArrayList<?>)msgBean.getMsg()).get(0));
				}
		    	currStatus.setJsp("User_Announce.jsp?Sid=" + Sid);
		    	break;
		}
		
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
	   	response.sendRedirect(currStatus.getJsp());
	}
	
	
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd) 
		{
		
			case 0://查询
			case 1://查询	
				Sql = " select t.id, t.cname, t.brief, t.contact, t.tel, t.addr, t.dept ,t.demo" +
					  " from corp_info t " +
					  " order by substr(t.id,3,4)";
				break;
			case 10://添加
				Sql = " insert into corp_info(id, cname, brief, contact, tel, addr, dept ,t.demo)" +
					  " values('"+id+"', '"+cname+"', '"+brief+"', '"+contact+"', '"+tel+"', '"+addr+"', '"+dept+"', '"+demo+"' )";
				break;
			case 11://修改
				Sql = " update corp_info set id='"+ id +"', cname = '"+ cname +"', brief = '"+ brief +"', contact = '"+ contact +"', tel = '"+ tel +"', " +
					  " addr = '"+ addr +"', dept = '"+ dept +"' , demo = '"+ demo +"'  ";		  
				break;
		}
		return Sql;
	}
	public boolean getData(ResultSet pRs) 
	{
		boolean IsOK = true;
		try
		{
			setId(pRs.getString(1));
			setCname(pRs.getString(2));
			setBrief(pRs.getString(3));
			setContact(pRs.getString(4));
			setTel(pRs.getString(5));
			setAddr(pRs.getString(6));
			setDept(pRs.getString(7));
			setDemo(pRs.getString(8));
		} 
		catch (SQLException sqlExp) 
		{
			sqlExp.printStackTrace();
		}		
		return IsOK;
	}
	public boolean getHtmlData(HttpServletRequest request) 
	{
		boolean IsOK = true;
		try 
		{	
			setId(CommUtil.StrToGB2312(request.getParameter("id")));
			setCname(CommUtil.StrToGB2312(request.getParameter("cname")));
			setBrief(CommUtil.StrToGB2312(request.getParameter("brief")));
			setContact(CommUtil.StrToGB2312(request.getParameter("contact")));
			setTel(CommUtil.StrToGB2312(request.getParameter("tel")));
			setAddr(CommUtil.StrToGB2312(request.getParameter("addr")));
			setDept(CommUtil.StrToGB2312(request.getParameter("dept")));
			setDemo(CommUtil.StrToGB2312(request.getParameter("demo")));
			setSid(CommUtil.StrToGB2312(request.getParameter("Sid")));
		}
		catch (Exception Exp) 
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	
	private String id;
	private String cname;
	private String brief;
	private String contact;
	private String tel;
	private String addr;
	private String dept;
	private String demo;
	
	private String Sid;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getDemo() {
		return demo;
	}

	public void setDemo(String demo) {
		this.demo = demo;
	}

	public String getSid() {
		return Sid;
	}

	public void setSid(String sid) {
		Sid = sid;
	}
}
