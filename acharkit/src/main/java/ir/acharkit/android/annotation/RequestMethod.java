package ir.acharkit.android.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ir.acharkit.android.connection.ConnectionRequest;

/**
 * Author:  Alireza Tizfahm Fard
 * Date:    11/7/2017
 * Email:   alirezat775@gmail.com
 */

@StringDef({ConnectionRequest.Method.GET, ConnectionRequest.Method.POST, ConnectionRequest.Method.PUT, ConnectionRequest.Method.DELETE, ConnectionRequest.Method.PATCH, ConnectionRequest.Method.HEAD})
@Retention(RetentionPolicy.SOURCE)
public @interface RequestMethod {
}
