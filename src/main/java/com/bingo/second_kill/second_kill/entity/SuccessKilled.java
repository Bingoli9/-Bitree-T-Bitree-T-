package com.bingo.second_kill.second_kill.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("success_killed")
public class SuccessKilled {
	@TableField
	private Byte state;

	@TableField
	private Date createTime;

	@TableId
	private Long seckillId;

	@TableField
	private Long userPhone;

	// 多对一,因为一件商品在库存中有很多数量，对应的购买明细也有很多。
	private Seckill seckill;

	public Seckill getSeckill() {
		return seckill;
	}

	public void setSeckill(Seckill seckill) {
		this.seckill = seckill;
	}

	public Long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(Long seckillId) {
		this.seckillId = seckillId;
	}

	public Long getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(Long userPhone) {
		this.userPhone = userPhone;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "SuccessKilled [state=" + state + ", createTime=" + createTime + ", seckillId=" + seckillId
				+ ", userPhone=" + userPhone + "]";
	}

}