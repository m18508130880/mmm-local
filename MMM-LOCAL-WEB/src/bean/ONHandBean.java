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

public class ONHandBean extends RmiBean
{
	public final static long	serialVersionUID	= RmiBean.RMI_ON_HAND;

	public long getClassId()
	{
		return serialVersionUID;
	}

	public ONHandBean()
	{
		super.className = "ONHandBean";
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
				request.getSession().setAttribute("ON_Hand_" + Sid, (Object) msgBean.getMsg());
				currStatus.setJsp("ON_Hand.jsp?Sid=" + Sid);
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
			Sql = " select t.sn, t.project_id, t.factory, t.type, t.texture, t.spec, t.ply, t.length, t.payPie, t.payWeight, t.pieWeight, t.weight, t.pay, t.pie, t.material, t.depot, t.payPrice, t.piePrice, t.trafficProce, t.price " +
				  " from on_hand t " +
				  " where t.project_id ='" + currStatus.getFunc_Project_Id() + "'" +
				  " order by t.sn";
			break;
		case 10: // 插入
			Sql = " insert into on_hand(project_id, factory, type, texture, spec, ply, length, payPie, payWeight, pieWeight, weight, pay, pie, material, depot, payPrice, piePrice, trafficProce, price) " +
				  " values('"+project+"', '"+factory+"', '"+type+"', '"+texture+"', '"+spec+"', '"+ply+"', '"+length+"', '"+payPie+"', '"+payWeight+"', '"+pieWeight+"', '"+weight+"', '"+pay+"', '"+pie+"', '"+material+"', '"+depot+"', '"+payPrice+"', '"+piePrice+"', '"+trafficProce+"', '"+price+"')";
			break;
		case 11: // 修改
			Sql = " update on_hand set project_id = '"+project+"', factory = '"+factory+"', type = '"+type+"', texture = '"+texture+"', spec = '"+spec+"', ply = '"+ply+"', length = '"+length+"', payPie = '"+payPie+"', payWeight = '"+payWeight+"', pieWeight = '"+pieWeight+"', weight = '"+weight+"', pay = '"+pay+"', pie = '"+pie+"', material = '"+material+"', depot = '"+depot+"', payPrice = '"+payPrice+"', piePrice = '"+piePrice+"', trafficProce = '"+trafficProce+"', price = '"+price+"'"+
				  " where sn = '"+sn+"'";
			break;
		case 12: // 删除
			Sql = " delete from on_hand where sn = '" + sn + "'";
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
			setFactory(pRs.getString(3));
			setType(pRs.getString(4));
			setTexture(pRs.getString(5));
			setSpec(pRs.getString(6));
			setPly(pRs.getString(7));
			setLength(pRs.getString(8));
			setPayPie(pRs.getString(9));
			setPayWeight(pRs.getString(10));
			setPieWeight(pRs.getString(11));
			setWeight(pRs.getString(12));
			setPay(pRs.getString(13));
			setPie(pRs.getString(14));
			setMaterial(pRs.getString(15));
			setDepot(pRs.getString(16));
			setPayPrice(pRs.getString(17));
			setPiePrice(pRs.getString(18));
			setTrafficProce(pRs.getString(19));
			setPrice(pRs.getString(20));
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
			setFactory(CommUtil.StrToGB2312(request.getParameter("factory")));
			setType(CommUtil.StrToGB2312(request.getParameter("type")));
			setTexture(CommUtil.StrToGB2312(request.getParameter("type")));
			setSpec(CommUtil.StrToGB2312(request.getParameter("spec")));
			setPly(CommUtil.StrToGB2312(request.getParameter("ply")));
			setLength(CommUtil.StrToGB2312(request.getParameter("length")));
			setPayPie(CommUtil.StrToGB2312(request.getParameter("payPie")));
			setPayWeight(CommUtil.StrToGB2312(request.getParameter("payWeight")));
			setPieWeight(CommUtil.StrToGB2312(request.getParameter("pieWeight")));
			setWeight(CommUtil.StrToGB2312(request.getParameter("weight")));
			setPay(CommUtil.StrToGB2312(request.getParameter("pay")));
			setPie(CommUtil.StrToGB2312(request.getParameter("pie")));
			setMaterial(CommUtil.StrToGB2312(request.getParameter("material")));
			setDepot(CommUtil.StrToGB2312(request.getParameter("depot")));
			setPayPrice(CommUtil.StrToGB2312(request.getParameter("payPrice")));
			setPiePrice(CommUtil.StrToGB2312(request.getParameter("piePrice")));
			setTrafficProce(CommUtil.StrToGB2312(request.getParameter("trafficProce")));
			setPrice(CommUtil.StrToGB2312(request.getParameter("price")));
			
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
	private String	factory;
	private String	type;
	private String	texture;
	private String	spec;
	private String	ply;
	private String	length;
	private String	payPie;
	private String	payWeight;
	private String	pieWeight;
	private String	weight;
	private String	pay;
	private String	pie;
	private String	material;
	private String	depot;
	private String	payPrice;
	private String	piePrice;
	private String	trafficProce;
	private String	price;
	
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

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getPly() {
		return ply;
	}

	public void setPly(String ply) {
		this.ply = ply;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getPayPie() {
		return payPie;
	}

	public void setPayPie(String payPie) {
		this.payPie = payPie;
	}

	public String getPayWeight() {
		return payWeight;
	}

	public void setPayWeight(String payWeight) {
		this.payWeight = payWeight;
	}

	public String getPieWeight() {
		return pieWeight;
	}

	public void setPieWeight(String pieWeight) {
		this.pieWeight = pieWeight;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getPay() {
		return pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public String getPie() {
		return pie;
	}

	public void setPie(String pie) {
		this.pie = pie;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getDepot() {
		return depot;
	}

	public void setDepot(String depot) {
		this.depot = depot;
	}

	public String getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}

	public String getPiePrice() {
		return piePrice;
	}

	public void setPiePrice(String piePrice) {
		this.piePrice = piePrice;
	}

	public String getTrafficProce() {
		return trafficProce;
	}

	public void setTrafficProce(String trafficProce) {
		this.trafficProce = trafficProce;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSid() {
		return Sid;
	}

	public void setSid(String sid) {
		Sid = sid;
	}

}