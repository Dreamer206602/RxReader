package com.mx.model.weiboVideo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YiuChoi on 2016/4/12 0012.
 */
public class WeiboVideoResponse {
    @SerializedName("cards")
    private WeiboVideoCardsItem[] cardsItems;

    public WeiboVideoCardsItem[] getCardsItems() {
        return cardsItems;
    }

    public void setCardsItems(WeiboVideoCardsItem[] cardsItems) {
        this.cardsItems = cardsItems;
    }
}
