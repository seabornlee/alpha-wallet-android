package com.alphawallet.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.alphawallet.app.entity.tokens.Token;
import com.alphawallet.token.entity.ContractInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2/02/2019.
 * Stormbird in Singapore
 */
public class ContractLocator implements Parcelable
{
    public final String name;
    public final int chainId;
    private final ContractType type;

    public ContractLocator(String n, int chain)
    {
        name = n;
        chainId = chain;
        type = ContractType.NOT_SET;
    }

    public ContractLocator(String n, int chain, ContractType t)
    {
        name = n;
        chainId = chain;
        type = t;
    }

    protected ContractLocator(Parcel in)
    {
        this.name = in.readString();
        this.chainId = in.readInt();
        this.type = ContractType.values()[in.readInt()];
    }

    public boolean equals(Token token)
    {
        return (token != null && name != null && name.equalsIgnoreCase(token.getAddress()) && chainId == token.tokenInfo.chainId);
    }

    /* replace this with a one-liner use of stream when we up our minSdkVersion to 24 */
    public static ContractLocator[] fromAddresses(String[] addresses, int chainID) {
        ContractLocator[] retval = new ContractLocator[addresses.length];
        for (int i=0; i<addresses.length; i++) {
            retval[i] = new ContractLocator(addresses[i], chainID);
        }
        return retval;
    }

    public static List<ContractLocator> fromContractInfo(ContractInfo cInfo)
    {
        // public Map<Integer, List<String>> addresses = new HashMap<>();
        List<ContractLocator> retVal = new ArrayList<>();
        for (int chainId : cInfo.addresses.keySet())
        {
            for (String addr : cInfo.addresses.get(chainId))
            {
                retVal.add(new ContractLocator(addr, chainId));
            }
        }

        return retVal;
    }

    public static final Creator<ContractLocator> CREATOR = new Creator<ContractLocator>() {
        @Override
        public ContractLocator createFromParcel(Parcel in) {
            return new ContractLocator(in);
        }

        @Override
        public ContractLocator[] newArray(int size) {
            return new ContractLocator[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(name);
        dest.writeInt(chainId);
        dest.writeInt(type.ordinal());
    }
}
