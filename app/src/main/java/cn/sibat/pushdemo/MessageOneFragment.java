package cn.sibat.pushdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author 沈吉荣
 */
public class MessageOneFragment extends Fragment {
    @BindView(R.id.listView)
    ListView listView;
    Unbinder unbinder;
    private Context context;
    List<MessageItem> messageItemList = new ArrayList<>();
    CommonAdapter<MessageItem>  messageItemCommonAdapter;


    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        messageItemList.add(new MessageItem("XX站点2号线站台楼梯附近发生疑似爆炸物"," 2017-8-2 12:23:03"));
        messageItemCommonAdapter = new CommonAdapter<MessageItem>(context,messageItemList,R.layout.item_lv_one) {
            @Override
            public void convert(CommonViewHolder helper, MessageItem item) {
                helper.setText(R.id.item_time,"下发时间："+item.getMessageTime());
                helper.setText(R.id.tv_item_message,item.getMessageInfo());
                helper.setClickListener(R.id.tv_submit, new CommonViewHolder.MyClickListener() {
                    @Override
                    public void onClick(View v) {
                        super.onClick(v);
                        new AlertDialog(mContext)
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

                                    }
                                })
                                .show();
                    }
                });
            }
        };
        listView.setAdapter(messageItemCommonAdapter);
        messageItemCommonAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //去消息详情页面
                MessageDetailActivity.actionStart(context);
            }
        });
    }

    /**
     * 获取数据
     */
    public void getData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}