package com.licenta.mihai.givemebook_android.Models.NetModels.Replay;

/**
 * Created by mihai on 21.06.2017.
 */

public class NetInteractions {
    private Long myID;
    private Long friendID;
    private Integer type;

    public Long getMyID() {
        return myID;
    }

    public void setMyID(Long myID) {
        this.myID = myID;
    }

    public Long getFriendID() {
        return friendID;
    }

    public void setFriendID(Long friendID) {
        this.friendID = friendID;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
