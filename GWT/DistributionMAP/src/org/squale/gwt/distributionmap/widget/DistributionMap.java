/**
 * Copyright (C) 2008-2010, Squale Project - http://www.squale.org
 *
 * This file is part of Squale.
 *
 * Squale is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * Squale is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Squale.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package org.squale.gwt.distributionmap.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.squale.gwt.distributionmap.widget.bundle.DMResources;
import org.squale.gwt.distributionmap.widget.data.Parent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author fabrice
 */
public class DistributionMap
    extends Composite
{
    final private AsyncCallback<ArrayList<Parent>> callback = new AsyncCallback<ArrayList<Parent>>()
    {
        public void onSuccess( ArrayList<Parent> result )
        {
            Collections.sort( result, parentComparator );
            displayBoxes( result );
        }

        public void onFailure( Throwable caught )
        {
            mainPanel.clear();
            mainPanel.add( new HTML( caught.toString() ) );
        }
    };

    final private Comparator<Parent> parentComparator = new Comparator<Parent>()
    {
        @Override
        public int compare( Parent p1, Parent p2 )
        {
            return p1.getChildren().size() - p2.getChildren().size();
        }
    };

    final public static DMResources resources = GWT.create( DMResources.class );

    final private FlowPanel mainPanel = new FlowPanel();

    final private Widget loadingLabel = new HTML();

    final private PopupPanel detailPopup = new PopupPanel( true );

    private String bigBoxDetailPopupMessage;

    private String smallBoxDetailPopupMessage;

    private String detailURL = "";

    public DistributionMap()
    {
        resources.css().ensureInjected();

        loadingLabel.setStylePrimaryName( resources.css().loadingLabel() );

        mainPanel.setStylePrimaryName( resources.css().distributionMap() );

        initWidget( mainPanel );
    }

    public void startLoading()
    {
        mainPanel.clear();
        mainPanel.add( loadingLabel );
    }

    public AsyncCallback<ArrayList<Parent>> getCallback()
    {
        return callback;
    }

    private void displayBoxes( ArrayList<Parent> parents )
    {
        mainPanel.clear();
        for ( Parent parent : parents )
        {
            mainPanel.add( new BigBox( this, parent ) );
        }
    }

    void updateDetailPopup( int xPosition, int yPosition )
    {
        detailPopup.setPopupPosition( xPosition + 1 + Window.getScrollLeft(), yPosition + 1 + Window.getScrollTop() );
    }

    void hideDetailPopupForSmallBox()
    {
        detailPopup.setWidget( new HTML( bigBoxDetailPopupMessage ) );
    }

    void showDetailPopupForSmallBox( String message, int xPosition, int yPosition )
    {
        smallBoxDetailPopupMessage = message;
        detailPopup.setWidget( new HTML( smallBoxDetailPopupMessage ) );
        detailPopup.show();
        updateDetailPopup( xPosition, yPosition );
    }

    void hideDetailPopupForBigBox()
    {
        detailPopup.hide();
    }

    void showDetailPopupForBigBox( String message, int xPosition, int yPosition )
    {
        bigBoxDetailPopupMessage = message;
        detailPopup.setWidget( new HTML( bigBoxDetailPopupMessage ) );
        detailPopup.show();
        updateDetailPopup( xPosition, yPosition );
    }

    /**
     * @return the detailURL
     */
    public String getDetailURL()
    {
        return detailURL;
    }

    /**
     * @param detailURL the detailURL to set
     */
    public void setDetailURL( String detailURL )
    {
        this.detailURL = detailURL;
    }
}