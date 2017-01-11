package com.fingertec.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jqr on 2016/12/27.
 */
public class MissionBean extends Bean {
    public String id;
    public String firstname;
    public String lastname;
    public ArrayList<MissionItem> data;

    public static class MissionItem implements Serializable {

        private String date;
        private String regulatoryname;//物业公司名称
        private String elevatornum;//电梯设备编号
        private String elevatorid;//电梯id
        private String maintenancetimes;//维保次数
        private String elevatorLocation;//电梯位置
        private boolean is_finished = false;//是否完成

        public String getElevatorLocation() {
            return elevatorLocation;
        }

        public void setElevatorLocation(String elevatorLocation) {
            this.elevatorLocation = elevatorLocation;
        }


        public boolean is_finished() {
            return is_finished;
        }

        public void setIs_finished(boolean is_finished) {
            this.is_finished = is_finished;
        }


        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getElevatorID() {
            return elevatorid;
        }

        public void setElevatorID(String elevatorID) {
            this.elevatorid = elevatorID;
        }

        public String getMaintenanceTimes() {
            return maintenancetimes;
        }

        public void setMaintenanceTimes(String maintenanceTimes) {
            this.maintenancetimes = maintenanceTimes;
        }

        public String getElevatorNum() {
            return elevatornum;
        }

        public void setElevatorNum(String elevatorNum) {
            this.elevatornum = elevatorNum;
        }

        public String getRegulatoryName() {
            return regulatoryname;
        }

        public void setRegulatoryName(String regulatoryName) {
            this.regulatoryname = regulatoryName;
        }


    }
}
