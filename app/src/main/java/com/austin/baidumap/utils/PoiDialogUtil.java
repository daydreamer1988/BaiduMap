package com.austin.baidumap.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;

import java.util.List;

/**
 * Created by Austin on 2017/4/27.
 */

public class PoiDialogUtil {

    private static PoiDialogUtil instance;
    private static Context mContext;
    private ListView listView;

    private PoiDialogUtil(){
    }

    public static PoiDialogUtil getInstance(Context context){
        mContext = context;
        if(instance == null){
            return new PoiDialogUtil();
        }
        return instance;
    }


    public interface OnPoiItemSelected{
        void onPoiItemClick(PoiInfo poiInfo);

        void onLastPageClicked(int totalPageNum, int currentPageNum);

        void onNextPageClicked(int totalPageNum, int currentPageNum);
    }


    public void showPoiList(PoiResult poiResult, final OnPoiItemSelected listener) {
        final List<PoiInfo> allPoi = poiResult.getAllPoi();
        final int currentPageNum = poiResult.getCurrentPageNum();
        int currentPageCapacity = poiResult.getCurrentPageCapacity();
        List<CityInfo> suggestCityList = poiResult.getSuggestCityList();
        final int totalPageNum = poiResult.getTotalPageNum();
        int totalPoiNum = poiResult.getTotalPoiNum();

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setAdapter(new MyAdapter(mContext, poiResult), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int headerViewsCount = listView.getHeaderViewsCount();
                        if (which <= headerViewsCount-1) {
                            return;
                        }
                        listener.onPoiItemClick(allPoi.get(which-headerViewsCount));
                    }
                });

        if(currentPageNum!=0){
            builder.setNegativeButton("上一页", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onLastPageClicked(totalPageNum, currentPageNum);
                }
            });
        }

        if(currentPageNum<totalPageNum-1){
            builder.setPositiveButton("下一页", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onNextPageClicked(totalPageNum, currentPageNum);
                }
            });
        }

        final AlertDialog alertDialog =builder.create();


        listView = alertDialog.getListView();

        //LayoutAnimation
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.2f, 1, 0.2f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);

        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(800);

        AnimationSet set = new AnimationSet(mContext, null);
        set.addAnimation(scaleAnimation);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        controller.setDelay(0.1f);
        listView.setLayoutAnimation(controller);

        //Divider
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.WHITE, Color.GRAY, Color.WHITE}));
        listView.setDividerHeight(5);

        //HeaderView
        TextView headerView = new TextView(mContext);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        headerView.setGravity(Gravity.CENTER);
        headerView.setText("当前页(从0开始)：" + currentPageNum + "\n" +
                "总页数：" + totalPageNum + "\n" +
                "currentPageCapacity:" + currentPageCapacity + "\n"+
                "totalPoiNum:" + totalPoiNum);

        listView.addHeaderView(headerView);

        //Padding
        listView.setPadding(30, 0, 30, 0);

        alertDialog.show();
    }


    public void showPoiDetial(PoiDetailResult poiDetailResult) {
        LatLng location = poiDetailResult.getLocation();
        String address = poiDetailResult.address;
        int checkinNum = poiDetailResult.checkinNum;
        int commentNum = poiDetailResult.commentNum;
        String detailUrl = poiDetailResult.detailUrl;
        double environmentRating = poiDetailResult.environmentRating;
        double facilityRating = poiDetailResult.facilityRating;
        int favoriteNum = poiDetailResult.favoriteNum;
        int grouponNum = poiDetailResult.getGrouponNum();
        double hygieneRating = poiDetailResult.getHygieneRating();
        int imageNum = poiDetailResult.getImageNum();
        String name = poiDetailResult.getName();
        double overallRating = poiDetailResult.getOverallRating();
        double price = poiDetailResult.getPrice();
        double serviceRating = poiDetailResult.getServiceRating();
        String shopHours = poiDetailResult.getShopHours();
        double tasteRating = poiDetailResult.getTasteRating();
        double technologyRating = poiDetailResult.getTechnologyRating();
        String telephone = poiDetailResult.getTelephone();
        String uid = poiDetailResult.getUid();

        String message = "latitude:"+location.latitude+
                "\nlongitude:"+location.longitude+
                "\naddress:"+address+
                "\ncheckinNum:"+checkinNum+
                "\ncommentNum:"+commentNum+
                "\ndetailUrl:"+detailUrl+
                "\nenvironmentRating:"+environmentRating+
                "\nfacilityRating:"+facilityRating+
                "\nfavoriteNum:"+favoriteNum+
                "\ngrouponNum:"+grouponNum+
                "\nhygieneRating:"+hygieneRating+
                "\nimageNum:"+imageNum+
                "\nname:"+name+
                "\noverallRating:"+overallRating+
                "\nprice:"+price+
                "\nserviceRating:"+serviceRating+
                "\nshopHours:"+shopHours+
                "\ntasteRating:"+tasteRating+
                "\ntechnologyRating:"+technologyRating+
                "\ntelephone:"+telephone+
                "\nuid:"+uid;

        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setCancelable(true)
                .setMessage(message)
                .show();
    }


    private static class MyAdapter extends BaseAdapter {
        private final List<PoiInfo> allPoi;
        private Context mContext;
        private PoiResult poiResult;

        public MyAdapter(Context mContext, PoiResult poiResult) {
            this.mContext = mContext;
            this.poiResult = poiResult;
            allPoi = poiResult.getAllPoi();
        }

        @Override
        public int getCount() {
            return allPoi.size();
        }

        @Override
        public Object getItem(int position) {
            return allPoi.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, null);
            TextView tv = (TextView) itemView.findViewById(android.R.id.text1);
            PoiInfo poiInfo = allPoi.get(position);
            StringBuffer itemInfo = new StringBuffer();
            itemInfo.append("address:"+poiInfo.address);
            itemInfo.append("\ncity:"+poiInfo.city);
            itemInfo.append("\nname:" + poiInfo.name);
            if(poiInfo.location!=null) {
                itemInfo.append("\nlatitude:" + poiInfo.location.latitude);
                itemInfo.append("\nlongitude:" + poiInfo.location.longitude);
            }
            itemInfo.append("\nphonenum:" + poiInfo.phoneNum);
            itemInfo.append("\nuid:" + poiInfo.uid);
            itemInfo.append("\npostcode:" + poiInfo.postCode);
            itemInfo.append("\npoitype:" + poiInfo.type);

            tv.setText(itemInfo);

            return itemView;
        }
    }
}
