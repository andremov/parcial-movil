package com.example.myapplication.network;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.objects.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {

    List<Message> messages = new ArrayList<Message>();
    Context context;
    String username;

    public MessageAdapter(Context context, String username) {
        this.context = context;
        this.username = username;
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);

        if (message.getmSender().equals(username)) {

            // this message was sent by us so let's create a basic chat bubble on the right
            convertView = messageInflater.inflate(R.layout.chat_bubble_send, null);
            holder.name = (TextView) convertView.findViewById(R.id.chat_send_username);
            holder.messageBody = (TextView) convertView.findViewById(R.id.chat_send_body);

            convertView.setTag(holder);
            holder.name.setText(message.getmSender());
            holder.messageBody.setText(message.getmBody());

        } else {

            // this message was sent by someone else so let's create an advanced chat bubble on the left
            convertView = messageInflater.inflate(R.layout.chat_bubble_received, null);
            holder.name = (TextView) convertView.findViewById(R.id.chat_received_username);
            holder.messageBody = (TextView) convertView.findViewById(R.id.chat_received_body);

            convertView.setTag(holder);
            holder.name.setText(message.getmSender());
            holder.messageBody.setText(message.getmBody());

        }

        return convertView;
    }

}

class MessageViewHolder {
    public TextView name;
    public TextView messageBody;
}