package com.tesmple.crowdsource.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

/**
 * Created by lypeer on 10/23/2015.
 */
public class NotificationLab extends Observable{

    /**
     * 静态的lab对象
     */
    private static NotificationLab sNotificationLab = null;

    /**
     * 将构造方法私有，使只能创建一个实例
     */
    private NotificationLab(){}

    private static List<Notification> sNotificationList = null;

    /**
     * 获得notificationlab的实例的方法
     * @return NotificationLab的对象
     */
    public static NotificationLab getInstance(){
        if(sNotificationLab == null){
            sNotificationLab = new NotificationLab();
        }
        return sNotificationLab;
    }

    /**
     * 往消息队列里面增加通知的方法
     * @param notification 要增加的通知
     */
    public void addNotification(Notification notification){
        if(sNotificationList == null){
            sNotificationList = new ArrayList<>();
        }
        sNotificationList.add(notification);
        setChanged();
        notifyObservers();
    }

    /**
     * 获得消息队列的方法
     * @return 返回当前用户的消息队列
     */
    public List<Notification> getNotificationList(){
        if(sNotificationList == null){
            sNotificationList = new ArrayList<>();
        }
        return sNotificationList;
    }

    /**
     * 是否存在没有读过的通知
     * @return 检查是否存在未读的通知的方法，返回false表示都读过了
     */
    public boolean isExistNotRead(){
        if(getNotificationList().size() == 0){
            return false;
        }else {
            for(Notification notification : getNotificationList()){
                if(!notification.isRead()){
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 清除通知的list的方法
     */
    public void clearList(){
        sNotificationList.clear();
    }

    /**
     * 反转list的方法
     */
    public void reverseList(){
        Collections.reverse(sNotificationList);
    }
}
