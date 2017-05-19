package entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ���ݿ��е�<cost>��,���ڴ���Ŀ�е�<�ʷѹ���>ҵ��
 * 
 * @author Martin
 *
 */
public class Cost implements Serializable {
	private static final long serialVersionUID = 310050620210009279L;
	// �ײ�id
	private Integer costId; // number(4) primary key,
	// �ײ�����
	private String name;// varchar(50) not null,
	// ����ʱ��
	private Integer baseDuration; // number(11),
	// ��������
	private Double baseCost; // number(7,2),
	// ��λ����,����Ǯÿ����
	private Double unitCost; // number(7,4),
	// �ײ�״̬(ö��):0-��ͨ,1-��ͣ
	private String status; // char(1),
	// �ʷ�˵��
	private String descr; // varchar2(100),
	// ���ݴ���ʱ��
	private Timestamp creatime; // date default sysdate ,
	// �ײͿ�ͨʱ��
	private Timestamp startime; // date,
	// �ʷ�����(ö��):1-����,2-�ײ�,3-��ʱ
	private String costType; // char(1)

	public Integer getCostId() {
		return costId;
	}

	public void setCostId(Integer costId) {
		this.costId = costId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getBaseDuration() {
		return baseDuration;
	}

	public void setBaseDuration(Integer baseDuration) {
		this.baseDuration = baseDuration;
	}

	public Double getBaseCost() {
		return baseCost;
	}

	public void setBaseCost(Double baseCost) {
		this.baseCost = baseCost;
	}

	public Double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Timestamp getCreatime() {
		return creatime;
	}

	public void setCreatime(Timestamp creatime) {
		this.creatime = creatime;
	}

	public Timestamp getStartime() {
		return startime;
	}

	public void setStartime(Timestamp startime) {
		this.startime = startime;
	}

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}
}
