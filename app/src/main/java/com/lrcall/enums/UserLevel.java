package com.lrcall.enums;

/**
 * 用户级别
 * 
 * @author libit
 */
public enum UserLevel
{
    L1((byte) 0, "注册会员"), L2((byte) 10, "铜牌会员"), L3((byte) 20, "银牌会员"), L4((byte) 30, "金牌会员"), L5((byte) 40, "白金会员");
    private byte type;
    private String desc;

    private UserLevel(byte type, String desc)
    {
        this.type = type;
        this.desc = desc;
    }

    public byte getType()
    {
        return type;
    }

    public void setType(byte type)
    {
        this.type = type;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }
}
