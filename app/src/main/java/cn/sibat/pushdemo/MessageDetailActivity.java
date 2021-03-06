package cn.sibat.pushdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.newera.okhttputils.listener.DisposeDataListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sibat.pushdemo.bean.Base64Util;
import cn.sibat.pushdemo.bean.JsonUtils;
import cn.sibat.pushdemo.bean.StatusListData;
import cn.sibat.pushdemo.bean.StringUtil;
import cn.sibat.pushdemo.bean.StyleData;
import cn.sibat.pushdemo.bean.UrlDetails;
import cn.sibat.pushdemo.serverconfig.BaseActivity;
import cn.sibat.pushdemo.serverconfig.RequestCenter;

public class MessageDetailActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.llayout_title)
    LinearLayout llayoutTitle;
    @BindView(R.id.tv_qianshou)
    TextView tvQianshou;
    @BindView(R.id.tv_qingbao)
    TextView tvQingbao;
    @BindView(R.id.tv_jieshu)
    TextView tvJieshu;
    @BindView(R.id.myListView)
    MyListView myListView;
    @BindView(R.id.tv_deviceAddress)
    TextView tvDeviceAddress;
    @BindView(R.id.tv_createTime)
    TextView tvCreateTime;
    @BindView(R.id.tv_keypersonState)
    TextView tvKeypersonState;
    @BindView(R.id.tv_keypersonType)
    TextView tvKeypersonType;
    @BindView(R.id.tv_compareSources)
    TextView tvCompareSources;
    @BindView(R.id.tv_keypersonId)
    TextView tvKeypersonId;
    @BindView(R.id.layout_deviceType1)
    LinearLayout layoutDeviceType1;
    @BindView(R.id.tv_eventType)
    TextView tvEventType;
    @BindView(R.id.tv_crowdNumber)
    TextView tvCrowdNumber;
    @BindView(R.id.tv_chaosDesity)
    TextView tvChaosDesity;
    @BindView(R.id.tv_crowdDesity)
    TextView tvCrowdDesity;
    @BindView(R.id.layout_deviceType2)
    LinearLayout layoutDeviceType2;
    @BindView(R.id.layout_deviceType3)
    LinearLayout layoutDeviceType3;
    @BindView(R.id.iv_deviceType)
    ImageView ivDeviceType;
    @BindView(R.id.layout_main_detail)
    LinearLayout layoutMainDetail;
    @BindView(R.id.iv_user1)
    ImageView ivUser1;
    @BindView(R.id.iv_user2)
    ImageView ivUser2;
    @BindView(R.id.iv_user3)
    ImageView ivUser3;

    private Context context;

    List<StatusListData> messageDetailList = new ArrayList<>();
    CommonAdapter<StatusListData> messageDetailCommonAdapter;

    String eventNo;
    String eventType;

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 启动该activity
     *
     * @param context 上下文环境
     */
    public static void actionStart(Context context, String eventNo, String eventType) {
        Intent intent = new Intent(context, MessageDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.putExtra("eventNo", eventNo);
        intent.putExtra("eventType", eventType);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detial);
        ButterKnife.bind(this);
        context = MessageDetailActivity.this;
        eventNo = getIntent().getStringExtra("eventNo");
        eventType = getIntent().getStringExtra("eventType");
        initData();
    }

    private void initData() {
        messageDetailCommonAdapter = new CommonAdapter<StatusListData>(context, messageDetailList, R.layout.item_lv_detail) {
            @Override
            public void convert(CommonViewHolder helper, StatusListData item) {
                helper.setText(R.id.tv_detail_time, item.getCreateTime());
                if (item.getIndex() == 0) {
                    helper.setViewVisibility(R.id.tv_detail_icon_up, View.INVISIBLE);
                } else {
                    helper.setViewVisibility(R.id.tv_detail_icon_up, View.VISIBLE);
                }
                if (item.getIndex() == messageDetailList.size() - 1) {
                    helper.setViewVisibility(R.id.tv_detail_icon_down, View.INVISIBLE);
                } else {
                    helper.setViewVisibility(R.id.tv_detail_icon_down, View.VISIBLE);
                }

                switch (item.getDealStatus()) {
                    case "1":
                        helper.setText(R.id.tv_detail_message, "事件开始");
                        helper.setViewVisibility(R.id.tv_message,View.GONE);
                        break;
                    case "2":
                        helper.setText(R.id.tv_detail_message, "事件审核完毕");
                        helper.setViewVisibility(R.id.tv_message,View.GONE);
                        break;
                    case "3":
                        helper.setText(R.id.tv_detail_message, "下发完毕");
                        helper.setViewVisibility(R.id.tv_message,View.GONE);
                        break;
                    case "4":
                        helper.setText(R.id.tv_detail_message, "签收完毕");
                        helper.setViewVisibility(R.id.tv_message,View.GONE);
                        break;
                    case "5":
                        helper.setText(R.id.tv_detail_message, "处置中");
                        helper.setViewVisibility(R.id.tv_message,View.VISIBLE);
                        helper.setText(R.id.tv_message,item.getDealDetail());
                        break;
                    case "6":
                        helper.setText(R.id.tv_detail_message, "处置完毕");
                        helper.setViewVisibility(R.id.tv_message,View.GONE);
                        break;
                }

                if ("".equals(item.getDealUserName())) {
                    helper.setViewVisibility(R.id.tv_detail_user, View.GONE);
                } else {
                    helper.setViewVisibility(R.id.tv_detail_user, View.VISIBLE);
                    helper.setText(R.id.tv_detail_user, item.getDealUserName() + "(" + item.getDealUserCode() + ")");
                }
                /*if ("0".equals(item.getDetail_iv())) {
                    helper.setViewVisibility(R.id.iv_message, View.GONE);
                } else {
                    helper.setViewVisibility(R.id.iv_message, View.VISIBLE);
                }*/
            /*    helper.setClickListener(R.id.iv_message, new CommonViewHolder.MyClickListener() {
                    @Override
                    public void onClick(View v) {
                        super.onClick(v);
                        new AlertDialog(context)
                                .builder()
                                .setTitle("消息反馈")
                                .setMsg("消息反馈详情内容...\n"+"消息反馈信息...")
                                .setNegativeButton("关闭", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                })
                                .show();
                    }
                });*/
            }
        };
        myListView.setAdapter(messageDetailCommonAdapter);
        messageDetailCommonAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_back, R.id.tv_qianshou, R.id.tv_qingbao, R.id.tv_jieshu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_qianshou:
                new AlertDialog(context)
                        .builder()
                        .setMsg("签收本条消息")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setPositiveButton("签收", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //调用签收接口
                                //调用签收接口
                                LoadingDialogShow("");
                                RequestCenter.findUrl2(eventNo, eventType, "1", "", new DisposeDataListener() {
                                    @Override
                                    public void onSuccess(Object responseObj) {
                                        LoadingDialogDismiss();
                                        getData();
                                        MessageActivity.messageActivity.vpInfoMyCouponFile.setCurrentItem(1);
                                        MessageTwoFragment.messageTwoFragment.getData();
                                    }

                                    @Override
                                    public void onFailure(Object reasonObj) {
                                        LoadingDialogDismiss();
                                    }
                                });

                            }
                        })
                        .show();
                break;
            case R.id.tv_qingbao:
                final AlertDialog alertDialog = new AlertDialog(context);
                alertDialog.builder()
                        .showEdit()
                        .setTitle("情报反馈")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setPositiveButton("提交", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String dealDetail = alertDialog.getEditText();
                                //调用情报反馈接口
                                LoadingDialogShow("");
                                RequestCenter.findUrl2(eventNo, eventType, "2", dealDetail, new DisposeDataListener() {
                                    @Override
                                    public void onSuccess(Object responseObj) {
                                        LoadingDialogDismiss();
                                        getData();
                                        MessageActivity.messageActivity.vpInfoMyCouponFile.setCurrentItem(2);
                                        MessageThreeFragment.messageThreeFragment.getData();
                                    }

                                    @Override
                                    public void onFailure(Object reasonObj) {
                                        LoadingDialogDismiss();
                                    }
                                });

                            }
                        })
                        .show();
                break;
            case R.id.tv_jieshu:
                new AlertDialog(context)
                        .builder()
                        .setMsg("是否提交结束处置？")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //调用结束处置接口
                                LoadingDialogShow("");
                                RequestCenter.findUrl2(eventNo, eventType, "0", "", new DisposeDataListener() {
                                    @Override
                                    public void onSuccess(Object responseObj) {
                                        LoadingDialogDismiss();
                                        getData();
                                    }

                                    @Override
                                    public void onFailure(Object reasonObj) {
                                        LoadingDialogDismiss();
                                    }
                                });

                            }
                        })
                        .show();
                break;
        }
    }

    /**
     * 获取数据
     */
    public void getData() {
        LoadingDialogShow("");
        RequestCenter.findUrl3(eventNo, eventType, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                LoadingDialogDismiss();
                String result = responseObj.toString();
                UrlDetails urlDetails = JsonUtils.jsonObject(UrlDetails.class, result);
                if (urlDetails != null) {
                    StyleData styleData = urlDetails.getData().getData();
                    showStyleData(styleData);
                    if (messageDetailList != null && messageDetailList.size() != 0) {
                        messageDetailList.clear();
                    }
                    messageDetailList.addAll(urlDetails.getData().getLogList());
                    for (int i = 0; i < messageDetailList.size(); i++) {
                        messageDetailList.get(i).setIndex(i);
                    }
                    messageDetailCommonAdapter.notifyDataSetChanged();
                } else {
                    CustomToast.showToast(context, "数据请求失败", 1000);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                LoadingDialogDismiss();
            }
        });

    }

    private void showStyleData(StyleData styleData) {
        layoutMainDetail.setVisibility(View.VISIBLE);
        if ("感知门".equals(styleData.getDeviceType())) {
            ivDeviceType.setImageResource(R.mipmap.img_main_11);
            ivUser1.setVisibility(View.VISIBLE);
            ivUser2.setVisibility(View.VISIBLE);
            ivUser3.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(styleData.getReserve2())) {
                ivUser1.setImageBitmap(Base64Util.base64ToBitmap(styleData.getReserve2()));
            }
            if (!TextUtils.isEmpty(styleData.getReserve3())) {
                ivUser2.setImageBitmap(Base64Util.base64ToBitmap(styleData.getReserve3()));
            }
        } else if ("AP".equals(styleData.getDeviceType())) {
            ivDeviceType.setImageResource(R.mipmap.img_main_14);
            ivUser1.setVisibility(View.VISIBLE);
            ivUser2.setVisibility(View.GONE);
            ivUser3.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(styleData.getReserve2())) {
                ivUser1.setImageBitmap(Base64Util.base64ToBitmap(styleData.getReserve2()));
            }
        } else if ("WIFI".equals(styleData.getDeviceType())) {
            ivDeviceType.setImageResource(R.mipmap.img_main_32);
            ivUser1.setVisibility(View.VISIBLE);
            ivUser2.setVisibility(View.GONE);
            ivUser3.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(styleData.getReserve2())) {
                ivUser1.setImageBitmap(Base64Util.base64ToBitmap(styleData.getReserve2()));
            }
        } else if ("深圳通".equals(styleData.getDeviceType())) {
            ivDeviceType.setImageResource(R.mipmap.img_main_31);
            ivUser1.setVisibility(View.VISIBLE);
            ivUser2.setVisibility(View.GONE);
            ivUser3.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(styleData.getReserve2())) {
                ivUser1.setImageBitmap(Base64Util.base64ToBitmap(styleData.getReserve2()));
            }
        } else if ("电子围栏".equals(styleData.getDeviceType())) {
            ivDeviceType.setImageResource(R.mipmap.img_main_10);
            ivUser1.setVisibility(View.VISIBLE);
            ivUser2.setVisibility(View.GONE);
            ivUser3.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(styleData.getReserve2())) {
                ivUser1.setImageBitmap(Base64Util.base64ToBitmap(styleData.getReserve2()));
            }
        } else if ("人脸识别".equals(styleData.getDeviceType())) {
            ivDeviceType.setImageResource(R.mipmap.img_main_27);
            ivUser1.setVisibility(View.VISIBLE);
            ivUser2.setVisibility(View.VISIBLE);
            ivUser3.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(styleData.getReserve2())) {
                ivUser1.setImageBitmap(Base64Util.base64ToBitmap(styleData.getReserve2()));
            }
            if (!TextUtils.isEmpty(styleData.getReserve3())) {
                ivUser2.setImageBitmap(Base64Util.base64ToBitmap(styleData.getReserve3()));
            }
        } else {
            ivDeviceType.setImageResource(R.mipmap.img_main_25);
            ivUser1.setVisibility(View.GONE);
            ivUser2.setVisibility(View.GONE);
            ivUser3.setVisibility(View.VISIBLE);
            Glide.with(context).load(styleData.getImagePath()).into(ivUser3);
        }
        if (!TextUtils.isEmpty(styleData.getDeviceType())) {
            layoutDeviceType1.setVisibility(View.VISIBLE);
            layoutDeviceType2.setVisibility(View.GONE);
            layoutDeviceType3.setVisibility(View.GONE);
        } else if ("stAlarm".equals(styleData.getDataSources())) {
            layoutDeviceType1.setVisibility(View.GONE);
            layoutDeviceType2.setVisibility(View.VISIBLE);
            layoutDeviceType3.setVisibility(View.GONE);
        } else {
            layoutDeviceType1.setVisibility(View.GONE);
            layoutDeviceType2.setVisibility(View.GONE);
            layoutDeviceType3.setVisibility(View.VISIBLE);
        }

        tvCreateTime.setText(StringUtil.toEmpty(styleData.getCreateTime()));
        tvDeviceAddress.setText(StringUtil.toEmpty(styleData.getDeviceAddress()));
        tvKeypersonState.setText("预警类别：" + StringUtil.toEmpty(styleData.getKeypersonState()));
        tvKeypersonType.setText("人员类别：" + StringUtil.toEmpty(styleData.getKeypersonType()));
        tvCompareSources.setText("比中信息：" + StringUtil.toEmpty(styleData.getCompareSources()));
        tvKeypersonId.setText(StringUtil.toEmpty(styleData.getKeypersonId()));
        tvEventType.setText("事件类型：" + StringUtil.toEmpty(styleData.getEventType()));
        tvCrowdNumber.setText("事件相关人数/总人数：" + StringUtil.toEmpty(styleData.getCrowdNumberWithEvent()) + "/" + StringUtil.toEmpty(styleData.getCrowdNumberAllImage()));
        tvChaosDesity.setText("混乱程度：" + (StringUtil.toEmpty(styleData.getChaosDesity()).equals("0") ? "混乱" : "严重混乱"));
        tvCrowdDesity.setText("过密程度：" + (StringUtil.toEmpty(styleData.getCrowdDesity()).equals("0") ? "过密" : "严重过密"));

        if (!TextUtils.isEmpty(styleData.getDealStatus())) {
            switch (styleData.getDealStatus()) {
                case "3":
                    tvQianshou.setVisibility(View.VISIBLE);
                    tvQingbao.setVisibility(View.GONE);
                    tvJieshu.setVisibility(View.GONE);
                    break;
                case "6":
                    tvQianshou.setVisibility(View.GONE);
                    tvQingbao.setVisibility(View.GONE);
                    tvJieshu.setVisibility(View.GONE);
                    break;
                default:
                    tvQianshou.setVisibility(View.GONE);
                    tvQingbao.setVisibility(View.VISIBLE);
                    tvJieshu.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            switch (styleData.getEventStatus()) {
                case "3":
                    tvQianshou.setVisibility(View.VISIBLE);
                    tvQingbao.setVisibility(View.GONE);
                    tvJieshu.setVisibility(View.GONE);
                    break;
                case "6":
                    tvQianshou.setVisibility(View.GONE);
                    tvQingbao.setVisibility(View.GONE);
                    tvJieshu.setVisibility(View.GONE);
                    break;
                default:
                    tvQianshou.setVisibility(View.GONE);
                    tvQingbao.setVisibility(View.VISIBLE);
                    tvJieshu.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }
}
