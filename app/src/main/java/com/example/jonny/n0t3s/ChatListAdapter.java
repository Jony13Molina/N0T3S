package com.example.jonny.n0t3s;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.SendBird;
import com.sendbird.android.UserMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter {
    private static final int TYPE_MSG_SENT = 1;
    private static final int TYPE_MSG_RECEIVED = 2;

    private Context listContext;
    private List<Notification> chatList = new ArrayList<Notification>();

    FirebaseAuth mainUser = FirebaseAuth.getInstance();
     FirebaseUser fireUser = mainUser.getCurrentUser();
    public ChatListAdapter(Context context, List<Notification> messageList) {
        listContext = context;
        chatList = messageList;
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Notification chatMessage = (Notification) chatList.get(position);

        if (chatMessage.getEma().equals(fireUser.getEmail())) {

        Log.d("we are here here", "yup lolkjj hjhhjjhhj jjjjje");
            return TYPE_MSG_SENT;

        } else {

            return TYPE_MSG_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == TYPE_MSG_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.msg_sent_text, parent, false);
            Log.d("we are here here", "yup we wew eewe");
            return new SentMessageHolder(view);
        } else if (viewType == TYPE_MSG_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.msg_received_text, parent, false);
            return new ReceivedMessageHolder(view);
        }


        return null;
    }


       // return null;





    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Notification chatMessage = (Notification) chatList.get(position);

        switch (holder.getItemViewType()) {
            case TYPE_MSG_SENT:
                ((SentMessageHolder) holder).bind(chatMessage);

                Log.d("we are here here", "yeeeeeeeeeeee eeereeerreerer");
                break;


            case TYPE_MSG_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(chatMessage);
                break;
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, nameText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);

            nameText = (TextView) itemView.findViewById(R.id.name_msg_body);
        }

        void bind(Notification notiMessage) {
            messageText.setText(notiMessage.getSentMsg());
            nameText.setText("Me");



        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, nameText;

        ImageView msgImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.main_msg_body);
            nameText = (TextView) itemView.findViewById(R.id.msg_name);


        }

        void bind(Notification notiMsg) {
            messageText.setText(notiMsg.getSenderEmail());



            nameText.setText(notiMsg.getSentMsg());

        }
    }
}
