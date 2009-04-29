/**
 * Jahia Enterprise Edition v6
 *
 * Copyright (C) 2002-2009 Jahia Solutions Group. All rights reserved.
 *
 * Jahia delivers the first Open Source Web Content Integration Software by combining Enterprise Web Content Management
 * with Document Management and Portal features.
 *
 * The Jahia Enterprise Edition is delivered ON AN "AS IS" BASIS, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
 * IMPLIED.
 *
 * Jahia Enterprise Edition must be used in accordance with the terms contained in a separate license agreement between
 * you and Jahia (Jahia Sustainable Enterprise License - JSEL).
 *
 * If you are unsure which license is appropriate for your use, please contact the sales department at sales@jahia.com.
 */
package org.jahia.ajax.gwt.client.widget.calendar;

import java.util.Date;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.extjs.gxt.ui.client.widget.DatePicker;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.dom.client.Node;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;

/**
 * @author Khue Nguyen
 */
public class CalendarPicker extends DatePicker {

    private LayoutContainer hoursPanel;

    /**
     * The date time format used to format each entry (defaults to
     * {@link com.google.gwt.i18n.client.DateTimeFormat#getShortDateFormat()}.
     */
    private DateTimeFormat format = DateTimeFormat.getShortTimeFormat();
    private HourModel hour;
    private MinuteModel minute;
    private ComboBox hours;
    private ComboBox minutes;

    public CalendarPicker(Date date) {
        super();
        if (date==null){
            date = new Date();
        }
        this.setValue(date,true);
        DateWrapper dateWrapper = new DateWrapper(this.getValue());
        hour = new HourModel(dateWrapper.getHours());
        minute = new MinuteModel(dateWrapper.getMinutes());
    }

    public CalendarPicker() {
        this(null);
    }

    public DateTimeFormat getFormat() {
        return format;
    }

    public void setFormat(DateTimeFormat format) {
        this.format = format;
    }

    public HourModel getHour() {
        return hour;
    }

    public int getSelectedHour() {
        HourModel hourModel = this.hour;
        if (!this.hours.getSelection().isEmpty()){
            hourModel = (HourModel)this.hours.getSelection().get(0);
        }
        return Integer.parseInt(hourModel.getValue());
    }

    public ComboBox getHours() {
        return hours;
    }

    public void setHour(int hour) {
        this.hour = new HourModel(hour);
        if (hours != null) {
            hours.setValue(this.hour);
        }
    }

    public MinuteModel getMinute() {
        return minute;
    }

    public int getSelectedMinute() {
        MinuteModel minuteModel = this.minute;
        if (!this.minutes.getSelection().isEmpty()){
            minuteModel = (MinuteModel)this.minutes.getSelection().get(0);
        }
        return Integer.parseInt(minuteModel.getValue());
    }

    public ComboBox getMinutes() {
        return minutes;
    }

    public void setMinute(int minute) {
        this.minute = new MinuteModel(minute);
        if (minutes != null) {
            minutes.setValue(this.minute);
        }
    }

    /**
     * Sets the value of the date field.
     *
     * @param date the date
     * @param supressEvent true to spress the select event
     */
    public void setValue(Date date, boolean supressEvent) {
      super.setValue(date,supressEvent);
      if (date != null){
          this.setMinute(date.getMinutes());
          this.setHour(date.getHours());
      }
    }

    protected void initHours(){

        Date date = getValue();
        DateWrapper dateWrapper = null;
        if (date!=null){
            dateWrapper = new DateWrapper();
        } else {
            dateWrapper = new DateWrapper(date);
        }

        // hours input
        hoursPanel = new LayoutContainer(new CenterLayout());
        //hoursPanel.setWidth(175);
        hoursPanel.setStyleName("x-date-hours-panel");
        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setLayout(new FitLayout());
        hPanel.setHorizontalAlign(Style.HorizontalAlignment.LEFT);
        hPanel.setStyleName("x-date-hours-panel-inner");
        hoursPanel.add(hPanel,new TableData(Style.HorizontalAlignment.CENTER, Style.VerticalAlignment.MIDDLE));

        hours = new ComboBox();
        hours.setDisplayField("display");
        hours.setMinListWidth(40);
        hours.setWidth(40);
        hours.setStore(getHours(0,23));
        hours.setValue(hour != null ? hour : new HourModel(dateWrapper.getHours()));
        hours.addSelectionChangedListener(new SelectionChangedListener<HourModel>() {
            public void selectionChanged(SelectionChangedEvent se) {
                HourModel hourModel = (HourModel) se.getSelection().get(0);
                if (hourModel!=null){
                    hour = new HourModel(Integer.parseInt(hourModel.getValue()));
                }
            }
        });

        hours.addListener(Events.Change, new Listener<FieldEvent>() {
            public void handleEvent(FieldEvent be) {
                HourModel hourModel = (HourModel)be.value;
                if (hourModel!=null){
                    hour = new HourModel(Integer.parseInt(hourModel.getValue()));
                }
            }
        });
        hPanel.add(hours);

        HTML sep = new HTML(":");
        sep.setStyleName("x-date-hours-separator");
        hPanel.add(sep);

        minutes = new ComboBox();
        minutes.setDisplayField("display");
        minutes.setMinListWidth(40);
        minutes.setWidth(40);
        minutes.setStore(getMinutes(0,59));
        minutes.setValue(minute != null ? minute : new MinuteModel(dateWrapper.getMinutes()));
        minutes.addSelectionChangedListener(new SelectionChangedListener<MinuteModel>() {
            public void selectionChanged(SelectionChangedEvent se) {
                MinuteModel minuteModel = (MinuteModel) se.getSelection().get(0);
                if (minuteModel!=null){
                    minute = new MinuteModel(Integer.parseInt(minuteModel.getValue()));
                }
            }
        });
        minutes.addListener(Events.Change, new Listener<FieldEvent>() {
            public void handleEvent(FieldEvent be) {
                MinuteModel minuteModel = (MinuteModel)be.value;
                if (minuteModel!=null){
                    minute = new MinuteModel(Integer.parseInt(minuteModel.getValue()));
                }
            }
        });
        hPanel.add(minutes);
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        initHours();
        Node lastChild = DOM.getChild(getElement(), 3);
        getElement().insertBefore(hoursPanel.getElement(),lastChild);
        DOM.sinkEvents(hoursPanel.getElement(), Event.ONCHANGE);
        DOM.sinkEvents(hours.getElement(), Event.ONCHANGE);
        DOM.sinkEvents(minutes.getElement(), Event.ONCHANGE);
    }

    @Override
    protected void doAttachChildren() {
      super.doAttachChildren();
      WidgetHelper.doAttach(hoursPanel);
    }

    @Override
    protected void doDetachChildren() {
      super.doDetachChildren();
      WidgetHelper.doDetach(hoursPanel);
    }

    private class HourModel extends BaseModel {

        public HourModel() {
            super();
        }

        public HourModel(int value){
            super();
            set("value",String.valueOf(value));
            if (value<10){
                set("display","0" + value);
            } else {
                set("display",value);
            }
        }

        public String getValue() {
            return get("value");
        }

        public String toString() {
          return getValue();
        }
    }

    private class MinuteModel extends BaseModel {

        public MinuteModel() {
            super();
        }

        public MinuteModel(int value){
            super();
            set("value",String.valueOf(value));
            if (value<10){
                set("display","0" + value);
            } else {
                set("display",value);
            }
        }

        public String getValue() {
            return get("value");
        }

        public String toString() {
          return getValue();
        }

    }

    private ListStore<HourModel> getHours(int startHour, int endHour){
        ListStore<HourModel> hours = new ListStore<HourModel>();
        for (int i=startHour; i<=endHour; i++){
            hours.add(new HourModel(i));
        }
        return hours;
    }

    private ListStore<MinuteModel> getMinutes(int startMinute, int endMinute){
        ListStore<MinuteModel> minutes = new ListStore<MinuteModel>();
        for (int i=startMinute; i<=endMinute; i++){
            minutes.add(new MinuteModel(i));
        }
        return minutes;
    }

}
