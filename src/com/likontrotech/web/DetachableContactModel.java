package com.likontrotech.web;

import java.math.BigInteger;

import javax.ejb.EJB;

import org.apache.wicket.model.LoadableDetachableModel;

import com.likontrotech.ejb.CompanyLocal;

public class DetachableContactModel extends LoadableDetachableModel<Object[]>{
	
	@EJB(name = "CompanyEJB")
	public CompanyLocal companyLocal;	
	
    private final long id;

    /**
     * @param c
     */
    public DetachableContactModel(Object[] c)
    {
        this(new BigInteger(String.valueOf(c[0])).longValue());
    }

    /**
     * @param id
     */
    public DetachableContactModel(long id)
    {
        if (id == 0)
        {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return Long.valueOf(id).hashCode();
    }

    /**
     * used for dataview with ReuseIfModelsEqualStrategy item reuse strategy
     * 
     * @see org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        else if (obj == null)
        {
            return false;
        }
        else if (obj instanceof DetachableContactModel)
        {
            DetachableContactModel other = (DetachableContactModel)obj;
            return other.id == id;
        }
        return false;
    }

    /**
     * @see org.apache.wicket.model.LoadableDetachableModel#load()
     */
    @Override
    protected Object[] load()
    {
        // loads contact from the database
        return null;//companyLocal.find(id);
    }
}
