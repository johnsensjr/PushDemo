package cn.sibat.pushdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.unread_msg_number)
    TextView unreadMsgNumber;
    @BindView(R.id.frameLayout_title_main)
    FrameLayout frameLayoutTitleMain;
    @BindView(R.id.btn_container_relay)
    RelativeLayout btnContainerRelay;
    @BindView(R.id.llayout_title)
    LinearLayout llayoutTitle;
    @BindView(R.id.layout_main_1)
    LinearLayout layoutMain1;
    @BindView(R.id.iv_keliu)
    ImageView ivKeliu;
    @BindView(R.id.iv_anjian)
    ImageView ivAnjian;
    @BindView(R.id.iv_jingli)
    ImageView ivJingli;

    private Context context;

    public static MainActivity mainActivity;

    @Override
    protected void onResume() {
        super.onResume();
        showMessageNum();
    }

    /**
     * 启动该activity
     *
     * @param context 上下文环境
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = MainActivity.this;
        mainActivity = this;
        showMessageNum();
    }

    public void showMessageNum() {
        if(AppInfo.getMessageNum(context)!=0){
            frameLayoutTitleMain.setVisibility(View.VISIBLE);
            unreadMsgNumber.setText(""+AppInfo.getMessageNum(context));
        }else {
            frameLayoutTitleMain.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.unread_msg_number, R.id.btn_container_relay, R.id.iv_keliu, R.id.iv_anjian, R.id.iv_jingli})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_container_relay:
                //跳转到消息主界面
                MessageActivity.actionStart(context);
                AppInfo.setMessageNum(context,0);
                break;
            case R.id.iv_keliu:
                //跳转到客流界面
                KeLiuActivity.actionStart(context);
                break;
            case R.id.iv_anjian:
                //跳转到安检界面
                AnjianActivity.actionStart(context);
                break;
            case R.id.iv_jingli:
                //跳转到警力界面
                JingliActivity.actionStart(context);
                break;
        }
    }
}
