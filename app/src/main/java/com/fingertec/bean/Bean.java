/*****************************
 * Copyright (c) 2014 by Hisunflytone Co. Ltd.  All rights reserved.
 ****************************/
package com.fingertec.bean;

import java.io.Serializable;

public class Bean implements Serializable {

    public int ret;

    public String msg;


    public int errorcode;

    public Bean() {
    }

    public Bean(int ret, String msg) {
        this.ret = ret;
        this.msg = msg;
    }

}
