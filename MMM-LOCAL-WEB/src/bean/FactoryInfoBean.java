package bean;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.CurrStatus;
import util.MsgBean;

public class FactoryInfoBean extends RmiBean
{
	public final static long	serialVersionUID	= RmiBean.RMI_FACTORY_INFO;

	public long getClassId()
	{
		return serialVersionUID;
	}

	public FactoryInfoBean()
	{
		super.className = "FactoryInfoBean";
	}

	/**
	 * @param request
	 * @param response
	 * @param pRmi
	 * @param pFromZone
	 * @throws ServletException
	 * @throws IOException
	 */
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus) request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0, 25);
		switch (currStatus.getCmd())
		{
			case 10:// 添加
			case 11:// 编辑
			case 12:// 删除
				currStatus.setResult(MsgBean.GetResult(msgBean.getStatus()));
				msgBean = pRmi.RmiExec(0, this, 0, 25);
			case 0: // 查询全部
				currStatus.setTotalRecord(msgBean.getCount());
				request.getSession().setAttribute("Factory_Info_" + Sid, (Object) msgBean.getMsg());
				currStatus.setJsp("Factory_Info.jsp?Sid=" + Sid);
				break;
		}
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
		response.sendRedirect(currStatus.getJsp());
	}

	/**
	 * 获取相应sql语句
	 * 
	 */
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
		case 0:	// 查询项目下全部
			Sql = " select t.sn, t.project_id, t.cname, t.addr, t.contact, t.tel, t.status, t.demo " +
				  " from factory_info t " +
				  " where t.project_id ='" + currStatus.getFunc_Project_Id() + "'" +
				  " order by t.sn";
			break;
		case 10:
			Sql = " insert into factory_info(project_id, cname, addr, contact, tel, status, demo) " +
				  " values('" + project + "','" + cname + "','" + addr + "','" + contact + "','" + tel + "','" + status + "','" + demo + "')";
			break;
		case 11:
			Sql = " update factory_info set project_id = '" + project + "', cname = '" + cname + "', addr = '" + addr + "', contact = '" + contact + "', tel = '" + tel + "', status = '" + status + "', demo = '" + demo + "'"+
				  " where sn = '"+sn+"'";
			break;
		case 12:
			Sql = " delete from factory_info where sn = '" + sn + "'";
			break;
		}
		return Sql;
	}

	/**
	 * 将数据库中 结果集的数据 封装到DevGjBean中
	 * 
	 */
	public boolean getData(ResultSet pRs)
	{
		boolean IsOK = true;
		try
		{
			setSn(pRs.getString(1));
			setProject(pRs.getString(2));
			setCname(pRs.getString(3));
			setAddr(pRs.getString(4));
			setContact(pRs.getString(5));
			setTel(pRs.getString(6));
			setStatus(pRs.getString(7));
			setDemo(pRs.getString(8));
		}
		catch (SQLException sqlExp)
		{
			sqlExp.printStackTrace();
		}
		return IsOK;
	}

	/**
	 * 得到页面数据
	 * 
	 * @param request
	 * @return 
	 */
	public boolean getHtmlData(HttpServletRequest request)
	{
		boolean IsOK = true;
		try
		{
			setSn(CommUtil.StrToGB2312(request.getParameter("sn")));
			setProject(CommUtil.StrToGB2312(request.getParameter("project")));
			setCname(CommUtil.StrToGB2312(request.getParameter("cname")));
			setAddr(CommUtil.StrToGB2312(request.getParameter("addr")));
			setContact(CommUtil.StrToGB2312(request.getParameter("contact")));
			setTel(CommUtil.StrToGB2312(request.getParameter("tel")));
			setStatus(CommUtil.StrToGB2312(request.getParameter("status")));
			setDemo(CommUtil.StrToGB2312(request.getParameter("demo")));
			
			setSid(CommUtil.StrToGB2312(request.getParameter("Sid")));
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}

	private String	sn;
	private String	project;
	private String	cname;
	private String	addr;
	private String	contact;
	private String	tel;
	private String	status;
	private String	demo;
	
	private String	Sid;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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