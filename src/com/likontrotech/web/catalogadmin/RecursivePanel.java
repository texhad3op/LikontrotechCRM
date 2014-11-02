package com.likontrotech.web.catalogadmin;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;


/**
 * This example list knows how to display sublists. It expects a list where each element is either a
 * string or another list.
 * 
 * @author Eelco Hillenius
 */
public final class RecursivePanel extends Panel
{
    /**
     * Constructor.
     * 
     * @param id
     *            The id of this component
     * @param list
     *            a list where each element is either a string or another list
     */
    public RecursivePanel(final String id, List list)
    {
        super(id);
        add(new Rows("rows", list));
        setVersioned(false);
    }

    /**
     * The list class.
     */
    private static class Rows extends ListView
    {
        /**
         * Construct.
         * 
         * @param name
         *            name of the component
         * @param list
         *            a list where each element is either a string or another list
         */
        public Rows(String name, List list)
        {
            super(name, list);
        }

        /**
         * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
         */
        protected void populateItem(ListItem listItem)
        {
            Object modelObject = listItem.getModelObject();

            if (modelObject instanceof List)
            {
                // create a panel that renders the sub lis
                RecursivePanel nested = new RecursivePanel("nested", (List)modelObject);
                listItem.add(nested);
                // if the current element is a list, we create a dummy row/
                // label element
                // as we have to confirm to our HTML definition, and set it's
                // visibility
                // property to false as we do not want LI tags to be rendered.
                WebMarkupContainer row = new WebMarkupContainer("row");
                row.setVisible(false);
                row.add(new WebMarkupContainer("label"));
                listItem.add(row);
            }
            else
            {
                // if the current element is not a list, we create a dummy panel
                // element
                // to confirm to our HTML definition, and set this panel's
                // visibility
                // property to false to avoid having the UL tag rendered
                RecursivePanel nested = new RecursivePanel("nested", null);
                nested.setVisible(false);
                listItem.add(nested);
                // add the row (with the LI element attached, and the label with
                // the
                // row's actual value to display
                WebMarkupContainer row = new WebMarkupContainer("row");
                final Object mo = modelObject;
                //row.add(new Label("label", modelObject.toString()));
                Link link = new Link("link") {
        			@Override
        			public void onClick() {
        				System.out.println(">>>>>>>>>:"+mo.toString());
        			}
        		};
        		link.add(new Label("label", modelObject.toString()));  
        		row.add(link);
                listItem.add(row);
            }
        }
    }
}